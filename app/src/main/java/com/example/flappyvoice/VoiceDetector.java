package com.example.flappyvoice;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class VoiceDetector {
    
    private static final String TAG = "VoiceDetector";
    
    private AudioRecord audioRecord;
    private Thread recordingThread;
    private boolean isRecording = false;
    private boolean voiceDetected = false;
    
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize;
    
    // Soglia per rilevare il suono (più basso = più sensibile)
    private static final int AMPLITUDE_THRESHOLD = 2000;
    
    private boolean isInitialized = false;
    private int initAttempts = 0;
    private static final int MAX_INIT_ATTEMPTS = 3;
    
    public VoiceDetector(Context context) {
        initializeAudioRecord();
    }
    
    private void initializeAudioRecord() {
        // Prova diversi audio sources
        int[] audioSources = {
            MediaRecorder.AudioSource.MIC,
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            MediaRecorder.AudioSource.VOICE_COMMUNICATION,
            MediaRecorder.AudioSource.DEFAULT
        };
        
        // Prova diversi sample rates
        int[] sampleRates = {44100, 22050, 16000, 11025, 8000};
        
        for (int audioSource : audioSources) {
            for (int sampleRate : sampleRates) {
                try {
                    bufferSize = AudioRecord.getMinBufferSize(
                        sampleRate,
                        CHANNEL_CONFIG,
                        AUDIO_FORMAT
                    );
                    
                    if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                        Log.w(TAG, "Invalid buffer size for sample rate: " + sampleRate);
                        continue;
                    }
                    
                    // Usa un buffer più grande per maggiore stabilità
                    bufferSize = bufferSize * 2;
                    
                    Log.d(TAG, "Trying AudioSource: " + audioSource + ", SampleRate: " + sampleRate);
                    
                    audioRecord = new AudioRecord(
                        audioSource,
                        sampleRate,
                        CHANNEL_CONFIG,
                        AUDIO_FORMAT,
                        bufferSize
                    );
                    
                    if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                        isInitialized = true;
                        Log.d(TAG, "AudioRecord initialized successfully with AudioSource: " + 
                              audioSource + ", SampleRate: " + sampleRate);
                        return;
                    } else {
                        Log.w(TAG, "Failed to initialize with AudioSource: " + audioSource + 
                              ", SampleRate: " + sampleRate);
                        if (audioRecord != null) {
                            try {
                                audioRecord.release();
                            } catch (Exception e) {
                                Log.e(TAG, "Error releasing failed AudioRecord: " + e.getMessage());
                            }
                            audioRecord = null;
                        }
                    }
                } catch (SecurityException e) {
                    Log.e(TAG, "Security exception for AudioSource " + audioSource + ": " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Illegal argument for AudioSource " + audioSource + ": " + e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, "Error with AudioSource " + audioSource + ": " + e.getMessage());
                }
            }
        }
        
        Log.e(TAG, "Failed to initialize AudioRecord with all configurations");
        isInitialized = false;
    }
    
    public boolean isInitialized() {
        return isInitialized;
    }
    
    public void start() {
        if (!isInitialized || audioRecord == null) {
            Log.e(TAG, "Cannot start - AudioRecord not initialized");
            
            // Prova a reinizializzare se non siamo al limite dei tentativi
            if (initAttempts < MAX_INIT_ATTEMPTS) {
                initAttempts++;
                Log.d(TAG, "Attempting to reinitialize AudioRecord (attempt " + initAttempts + ")");
                initializeAudioRecord();
                
                if (!isInitialized) {
                    return;
                }
            } else {
                return;
            }
        }
        
        if (isRecording) {
            Log.w(TAG, "Already recording");
            return;
        }
        
        try {
            audioRecord.startRecording();
            
            // Verifica che la registrazione sia effettivamente iniziata
            if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                Log.e(TAG, "AudioRecord is not in recording state");
                return;
            }
            
            isRecording = true;
            
            recordingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                    processAudio();
                }
            });
            recordingThread.start();
            
            Log.d(TAG, "Recording started successfully");
        } catch (IllegalStateException e) {
            Log.e(TAG, "Error starting recording: " + e.getMessage());
            isRecording = false;
        }
    }
    
    public void stop() {
        if (!isRecording) {
            return;
        }
        
        isRecording = false;
        
        if (recordingThread != null) {
            try {
                recordingThread.join(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error stopping recording thread: " + e.getMessage());
            }
            recordingThread = null;
        }
        
        if (audioRecord != null && audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            try {
                if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecord.stop();
                    Log.d(TAG, "Recording stopped");
                }
            } catch (IllegalStateException e) {
                Log.e(TAG, "Error stopping AudioRecord: " + e.getMessage());
            }
        }
        
        voiceDetected = false;
    }
    
    private void processAudio() {
        short[] buffer = new short[bufferSize];
        int consecutiveErrors = 0;
        
        Log.d(TAG, "Audio processing thread started");
        
        while (isRecording) {
            try {
                int bytesRead = audioRecord.read(buffer, 0, buffer.length);
                
                if (bytesRead > 0) {
                    consecutiveErrors = 0;
                    
                    // Calcola l'ampiezza media del segnale audio
                    long sum = 0;
                    for (int i = 0; i < bytesRead; i++) {
                        sum += Math.abs(buffer[i]);
                    }
                    int amplitude = (int) (sum / bytesRead);
                    
                    // Determina se la voce è rilevata
                    voiceDetected = amplitude > AMPLITUDE_THRESHOLD;
                    
                    // Log periodico per debug
                    if (System.currentTimeMillis() % 1000 < 50) {
                        Log.d(TAG, "Audio amplitude: " + amplitude + ", Voice detected: " + voiceDetected);
                    }
                    
                } else if (bytesRead == AudioRecord.ERROR_INVALID_OPERATION) {
                    Log.e(TAG, "Invalid operation while reading");
                    consecutiveErrors++;
                    if (consecutiveErrors > 10) {
                        Log.e(TAG, "Too many consecutive errors, stopping");
                        break;
                    }
                } else if (bytesRead == AudioRecord.ERROR_BAD_VALUE) {
                    Log.e(TAG, "Bad value while reading");
                    consecutiveErrors++;
                    if (consecutiveErrors > 10) {
                        Log.e(TAG, "Too many consecutive errors, stopping");
                        break;
                    }
                } else {
                    Log.w(TAG, "Unexpected bytes read: " + bytesRead);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error processing audio: " + e.getMessage());
                consecutiveErrors++;
                if (consecutiveErrors > 10) {
                    Log.e(TAG, "Too many consecutive errors, stopping");
                    break;
                }
            }
        }
        
        Log.d(TAG, "Audio processing thread stopped");
    }
    
    public boolean isVoiceDetected() {
        return voiceDetected && isRecording;
    }
    
    public void release() {
        stop();
        
        if (audioRecord != null) {
            try {
                audioRecord.release();
                Log.d(TAG, "AudioRecord released");
            } catch (Exception e) {
                Log.e(TAG, "Error releasing AudioRecord: " + e.getMessage());
            }
            audioRecord = null;
        }
        
        isInitialized = false;
    }
}

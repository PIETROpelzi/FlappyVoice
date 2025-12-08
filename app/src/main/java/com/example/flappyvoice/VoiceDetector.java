package com.example.flappyvoice;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class VoiceDetector {
    
    private AudioRecord audioRecord;
    private Thread recordingThread;
    private boolean isRecording = false;
    private boolean voiceDetected = false;
    
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(
        SAMPLE_RATE,
        CHANNEL_CONFIG,
        AUDIO_FORMAT
    );
    
    // Soglia per rilevare il suono (più basso = più sensibile)
    private static final int AMPLITUDE_THRESHOLD = 3000;
    
    public VoiceDetector(Context context) {
        try {
            audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                BUFFER_SIZE
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void start() {
        if (audioRecord == null || audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            return;
        }
        
        isRecording = true;
        audioRecord.startRecording();
        
        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                processAudio();
            }
        });
        recordingThread.start();
    }
    
    public void stop() {
        isRecording = false;
        
        if (recordingThread != null) {
            try {
                recordingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        if (audioRecord != null && audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            try {
                audioRecord.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        
        voiceDetected = false;
    }
    
    private void processAudio() {
        short[] buffer = new short[BUFFER_SIZE];
        
        while (isRecording) {
            int bytesRead = audioRecord.read(buffer, 0, BUFFER_SIZE);
            
            if (bytesRead > 0) {
                // Calcola l'ampiezza media del segnale audio
                long sum = 0;
                for (int i = 0; i < bytesRead; i++) {
                    sum += Math.abs(buffer[i]);
                }
                int amplitude = (int) (sum / bytesRead);
                
                // Determina se la voce è rilevata
                voiceDetected = amplitude > AMPLITUDE_THRESHOLD;
            }
        }
    }
    
    public boolean isVoiceDetected() {
        return voiceDetected;
    }
    
    public void release() {
        stop();
        
        if (audioRecord != null) {
            try {
                audioRecord.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            audioRecord = null;
        }
    }
}

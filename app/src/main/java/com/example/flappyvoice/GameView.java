package com.example.flappyvoice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    
    private static final String TAG = "GameView";
    
    private Thread gameThread;
    private SurfaceHolder holder;
    private boolean isPlaying;
    private boolean isGameOver;
    private boolean isStarted;
    
    private Bird bird;
    private List<Pipe> pipes;
    private Background background;
    private VoiceDetector voiceDetector;
    
    private int score;
    private long lastPipeTime;
    private static final long PIPE_SPAWN_INTERVAL = 2000; // 2 secondi
    
    private Paint scorePaint;
    private Paint gameOverPaint;
    private Paint instructionPaint;
    private Paint warningPaint;
    
    private int screenWidth;
    private int screenHeight;
    
    private boolean isTouching = false;
    private boolean voiceControlEnabled = false;
    
    public GameView(Context context) {
        super(context);
        holder = getHolder();
        
        Log.d(TAG, "GameView created");
        
        // Inizializza le paint
        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(80);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setShadowLayer(5, 2, 2, Color.BLACK);
        scorePaint.setAntiAlias(true);
        
        gameOverPaint = new Paint();
        gameOverPaint.setColor(Color.RED);
        gameOverPaint.setTextSize(100);
        gameOverPaint.setTextAlign(Paint.Align.CENTER);
        gameOverPaint.setShadowLayer(5, 2, 2, Color.BLACK);
        gameOverPaint.setAntiAlias(true);
        
        instructionPaint = new Paint();
        instructionPaint.setColor(Color.WHITE);
        instructionPaint.setTextSize(50);
        instructionPaint.setTextAlign(Paint.Align.CENTER);
        instructionPaint.setShadowLayer(3, 1, 1, Color.BLACK);
        instructionPaint.setAntiAlias(true);
        
        warningPaint = new Paint();
        warningPaint.setColor(Color.YELLOW);
        warningPaint.setTextSize(35);
        warningPaint.setTextAlign(Paint.Align.CENTER);
        warningPaint.setShadowLayer(3, 1, 1, Color.BLACK);
        warningPaint.setAntiAlias(true);
        
        try {
            voiceDetector = new VoiceDetector(context);
            Log.d(TAG, "VoiceDetector initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing VoiceDetector: " + e.getMessage());
        }
    }
    
    private void initGame() {
        try {
            pipes = new ArrayList<>();
            score = 0;
            isGameOver = false;
            isStarted = true;
            lastPipeTime = System.currentTimeMillis();
            
            if (screenWidth > 0 && screenHeight > 0) {
                bird = new Bird(screenWidth, screenHeight);
                background = new Background(screenWidth, screenHeight);
                Log.d(TAG, "Game initialized with screen: " + screenWidth + "x" + screenHeight);
            } else {
                Log.e(TAG, "Invalid screen dimensions");
            }
            
            if (voiceDetector != null) {
                voiceDetector.start();
                // Controlla se il voice detector funziona dopo 100ms
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            voiceControlEnabled = voiceDetector.isInitialized();
                            Log.d(TAG, "Voice control enabled: " + voiceControlEnabled);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Log.d(TAG, "Voice detection started");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing game: " + e.getMessage());
        }
    }
    
    @Override
    public void run() {
        Log.d(TAG, "Game loop started");
        while (isPlaying) {
            long startTime = System.currentTimeMillis();
            
            try {
                update();
                draw();
            } catch (Exception e) {
                Log.e(TAG, "Error in game loop: " + e.getMessage());
            }
            
            long timeThisFrame = System.currentTimeMillis() - startTime;
            if (timeThisFrame < 16) {
                try {
                    Thread.sleep(16 - timeThisFrame);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Sleep interrupted: " + e.getMessage());
                }
            }
        }
        Log.d(TAG, "Game loop stopped");
    }
    
    private void update() {
        if (!isStarted || isGameOver || bird == null) {
            return;
        }
        
        // Aggiorna il background
        if (background != null) {
            background.update();
        }
        
        // Controlla il livello audio E il touch come backup
        boolean shouldFly = false;
        
        if (voiceControlEnabled && voiceDetector != null) {
            shouldFly = voiceDetector.isVoiceDetected();
        } else {
            // Fallback: usa il touch
            shouldFly = isTouching;
        }
        
        bird.update(shouldFly);
        
        // Spawna nuovi tubi
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPipeTime > PIPE_SPAWN_INTERVAL) {
            pipes.add(new Pipe(screenWidth, screenHeight));
            lastPipeTime = currentTime;
        }
        
        // Aggiorna e rimuovi tubi
        List<Pipe> pipesToRemove = new ArrayList<>();
        for (Pipe pipe : pipes) {
            pipe.update();
            
            // Controlla se il tubo è passato dall'uccello
            if (!pipe.isScored() && pipe.getX() + pipe.getWidth() < bird.getX()) {
                pipe.setScored(true);
                score++;
            }
            
            // Rimuovi tubi fuori schermo
            if (pipe.getX() + pipe.getWidth() < 0) {
                pipesToRemove.add(pipe);
            }
            
            // Controlla collisioni
            if (checkCollision(pipe)) {
                gameOver();
            }
        }
        pipes.removeAll(pipesToRemove);
        
        // Controlla se l'uccello è fuori schermo
        if (bird.getY() <= 0 || bird.getY() >= screenHeight) {
            gameOver();
        }
    }
    
    private boolean checkCollision(Pipe pipe) {
        Rect birdRect = new Rect(
            (int) bird.getX(),
            (int) bird.getY(),
            (int) (bird.getX() + bird.getWidth()),
            (int) (bird.getY() + bird.getHeight())
        );
        
        Rect topPipeRect = new Rect(
            (int) pipe.getX(),
            0,
            (int) (pipe.getX() + pipe.getWidth()),
            (int) pipe.getTopHeight()
        );
        
        Rect bottomPipeRect = new Rect(
            (int) pipe.getX(),
            (int) (pipe.getTopHeight() + pipe.getGap()),
            (int) (pipe.getX() + pipe.getWidth()),
            screenHeight
        );
        
        return birdRect.intersect(topPipeRect) || birdRect.intersect(bottomPipeRect);
    }
    
    private void gameOver() {
        isGameOver = true;
        if (voiceDetector != null) {
            voiceDetector.stop();
        }
        Log.d(TAG, "Game Over - Score: " + score);
    }
    
    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                
                if (canvas == null) {
                    return;
                }
                
                // Disegna lo sfondo
                if (background != null) {
                    background.draw(canvas);
                } else {
                    canvas.drawColor(Color.rgb(135, 206, 235));
                }
                
                if (!isStarted) {
                    // Schermata iniziale
                    instructionPaint.setTextSize(60);
                    canvas.drawText("FLAPPY VOICE", screenWidth / 2f, screenHeight / 3f, instructionPaint);
                    instructionPaint.setTextSize(40);
                    canvas.drawText("Fai rumore per volare!", screenWidth / 2f, screenHeight / 2f, instructionPaint);
                    canvas.drawText("Tocca per iniziare", screenWidth / 2f, screenHeight / 2f + 80, instructionPaint);
                } else {
                    // Disegna i tubi
                    for (Pipe pipe : pipes) {
                        pipe.draw(canvas);
                    }
                    
                    // Disegna l'uccello
                    if (bird != null) {
                        bird.draw(canvas);
                    }
                    
                    // Disegna il punteggio
                    canvas.drawText("" + score, screenWidth / 2f, 100, scorePaint);
                    
                    // Indicatore voce o touch
                    if (voiceControlEnabled) {
                        if (voiceDetector != null && voiceDetector.isVoiceDetected()) {
                            Paint voiceIndicator = new Paint();
                            voiceIndicator.setColor(Color.GREEN);
                            voiceIndicator.setAlpha(150);
                            canvas.drawCircle(80, 80, 30, voiceIndicator);
                            
                            Paint textPaint = new Paint();
                            textPaint.setColor(Color.WHITE);
                            textPaint.setTextSize(30);
                            textPaint.setAntiAlias(true);
                            canvas.drawText("MIC", 80, 150, textPaint);
                        }
                    } else {
                        // Mostra indicatore touch
                        if (isTouching) {
                            Paint touchIndicator = new Paint();
                            touchIndicator.setColor(Color.CYAN);
                            touchIndicator.setAlpha(150);
                            canvas.drawCircle(80, 80, 30, touchIndicator);
                        }
                        
                        Paint textPaint = new Paint();
                        textPaint.setColor(Color.WHITE);
                        textPaint.setTextSize(30);
                        textPaint.setAntiAlias(true);
                        textPaint.setTextAlign(Paint.Align.LEFT);
                        canvas.drawText("TOUCH", 50, 150, textPaint);
                    }
                    
                    if (isGameOver) {
                        canvas.drawText("GAME OVER", screenWidth / 2f, screenHeight / 2f - 50, gameOverPaint);
                        canvas.drawText("Score: " + score, screenWidth / 2f, screenHeight / 2f + 50, scorePaint);
                        canvas.drawText("Tocca per riavviare", screenWidth / 2f, screenHeight / 2f + 150, instructionPaint);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error drawing: " + e.getMessage());
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        Log.e(TAG, "Error unlocking canvas: " + e.getMessage());
                    }
                }
            }
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isStarted) {
                    initGame();
                } else if (isGameOver) {
                    initGame();
                } else {
                    // Durante il gioco, se il voice control non funziona, usa il touch
                    if (!voiceControlEnabled) {
                        isTouching = true;
                    }
                }
                break;
                
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouching = false;
                break;
        }
        return true;
    }
    
    public void pause() {
        Log.d(TAG, "Pausing game");
        isPlaying = false;
        if (voiceDetector != null) {
            voiceDetector.stop();
        }
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Error joining game thread: " + e.getMessage());
        }
    }
    
    public void resume() {
        Log.d(TAG, "Resuming game");
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
        
        if (isStarted && !isGameOver && voiceDetector != null) {
            voiceDetector.start();
        }
    }
    
    public void destroy() {
        Log.d(TAG, "Destroying game");
        if (voiceDetector != null) {
            voiceDetector.release();
        }
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        Log.d(TAG, "Screen size: " + w + "x" + h);
    }
}

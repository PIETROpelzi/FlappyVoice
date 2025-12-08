package com.example.flappyvoice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    
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
    
    private int screenWidth;
    private int screenHeight;
    
    public GameView(Context context) {
        super(context);
        holder = getHolder();
        
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
        
        voiceDetector = new VoiceDetector(context);
    }
    
    private void initGame() {
        pipes = new ArrayList<>();
        score = 0;
        isGameOver = false;
        isStarted = true;
        lastPipeTime = System.currentTimeMillis();
        
        bird = new Bird(screenWidth, screenHeight);
        background = new Background(screenWidth, screenHeight);
        
        voiceDetector.start();
    }
    
    @Override
    public void run() {
        while (isPlaying) {
            long startTime = System.currentTimeMillis();
            
            update();
            draw();
            
            long timeThisFrame = System.currentTimeMillis() - startTime;
            if (timeThisFrame < 16) {
                try {
                    Thread.sleep(16 - timeThisFrame);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void update() {
        if (!isStarted || isGameOver) {
            return;
        }
        
        // Aggiorna il background
        background.update();
        
        // Controlla il livello audio e aggiorna l'uccello
        boolean isVoiceDetected = voiceDetector.isVoiceDetected();
        bird.update(isVoiceDetected);
        
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
        voiceDetector.stop();
    }
    
    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            
            // Disegna lo sfondo
            background.draw(canvas);
            
            if (!isStarted) {
                // Schermata iniziale
                instructionPaint.setTextSize(60);
                canvas.drawText("FLAPPY VOICE", screenWidth / 2, screenHeight / 3, instructionPaint);
                instructionPaint.setTextSize(40);
                canvas.drawText("Fai rumore per volare!", screenWidth / 2, screenHeight / 2, instructionPaint);
                canvas.drawText("Tocca per iniziare", screenWidth / 2, screenHeight / 2 + 80, instructionPaint);
            } else {
                // Disegna i tubi
                for (Pipe pipe : pipes) {
                    pipe.draw(canvas);
                }
                
                // Disegna l'uccello
                bird.draw(canvas);
                
                // Disegna il punteggio
                canvas.drawText("" + score, screenWidth / 2, 100, scorePaint);
                
                // Indicatore voce
                if (voiceDetector.isVoiceDetected()) {
                    Paint voiceIndicator = new Paint();
                    voiceIndicator.setColor(Color.GREEN);
                    voiceIndicator.setAlpha(150);
                    canvas.drawCircle(80, 80, 30, voiceIndicator);
                }
                
                if (isGameOver) {
                    canvas.drawText("GAME OVER", screenWidth / 2, screenHeight / 2 - 50, gameOverPaint);
                    canvas.drawText("Score: " + score, screenWidth / 2, screenHeight / 2 + 50, scorePaint);
                    canvas.drawText("Tocca per riavviare", screenWidth / 2, screenHeight / 2 + 150, instructionPaint);
                }
            }
            
            holder.unlockCanvasAndPost(canvas);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isStarted) {
                initGame();
            } else if (isGameOver) {
                initGame();
            }
        }
        return true;
    }
    
    public void pause() {
        isPlaying = false;
        if (voiceDetector != null) {
            voiceDetector.stop();
        }
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
        
        if (isStarted && !isGameOver && voiceDetector != null) {
            voiceDetector.start();
        }
    }
    
    public void destroy() {
        if (voiceDetector != null) {
            voiceDetector.release();
        }
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
    }
}

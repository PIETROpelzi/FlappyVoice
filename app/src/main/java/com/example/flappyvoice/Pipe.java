package com.example.flappyvoice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Pipe {
    
    private float x;
    private float topHeight;
    private float gap;
    private float width;
    private float speed;
    private boolean scored;
    
    private Paint pipePaint;
    private Paint pipeOutlinePaint;
    private Paint pipeHighlightPaint;
    private Paint pipeShadowPaint;
    
    private int screenHeight;
    
    public Pipe(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        this.width = 120;
        this.x = screenWidth;
        
        // GAP AUMENTATO! Da 400 a 550 pixel (molto più facile)
        this.gap = 550;
        
        this.speed = 6;
        this.scored = false;
        
        // Genera altezza casuale per il tubo superiore
        Random random = new Random();
        int minHeight = 200;
        int maxHeight = screenHeight - (int) gap - 200;
        this.topHeight = random.nextInt(maxHeight - minHeight) + minHeight;
        
        // Colori per i tubi (stile Mario)
        pipePaint = new Paint();
        pipePaint.setColor(Color.rgb(76, 175, 80)); // Verde
        pipePaint.setAntiAlias(true);
        pipePaint.setStyle(Paint.Style.FILL);
        
        pipeOutlinePaint = new Paint();
        pipeOutlinePaint.setColor(Color.rgb(56, 142, 60)); // Verde scuro
        pipeOutlinePaint.setAntiAlias(true);
        pipeOutlinePaint.setStyle(Paint.Style.STROKE);
        pipeOutlinePaint.setStrokeWidth(5);
        
        pipeHighlightPaint = new Paint();
        pipeHighlightPaint.setColor(Color.rgb(129, 199, 132)); // Verde chiaro
        pipeHighlightPaint.setAntiAlias(true);
        pipeHighlightPaint.setStyle(Paint.Style.FILL);
        
        pipeShadowPaint = new Paint();
        pipeShadowPaint.setColor(Color.rgb(46, 125, 50)); // Verde molto scuro
        pipeShadowPaint.setAntiAlias(true);
        pipeShadowPaint.setStyle(Paint.Style.FILL);
    }
    
    public void update() {
        x -= speed;
    }
    
    public void draw(Canvas canvas) {
        // Disegna il tubo superiore
        drawPipeSegment(canvas, x, 0, topHeight);
        
        // Disegna il tubo inferiore
        drawPipeSegment(canvas, x, topHeight + gap, screenHeight - (topHeight + gap));
    }
    
    private void drawPipeSegment(Canvas canvas, float x, float y, float height) {
        float capHeight = 40;
        
        // Disegna il corpo del tubo
        RectF bodyRect = new RectF(x + 15, y, x + width - 15, y + height - capHeight);
        
        // Ombra
        canvas.drawRect(x + 20, y, x + width - 15, y + height - capHeight, pipeShadowPaint);
        
        // Corpo principale
        canvas.drawRect(x + 15, y, x + width - 15, y + height - capHeight, pipePaint);
        
        // Highlight
        canvas.drawRect(x + 20, y, x + 30, y + height - capHeight, pipeHighlightPaint);
        
        // Bordo corpo
        canvas.drawRect(bodyRect, pipeOutlinePaint);
        
        // Disegna il cappello del tubo
        float capY = y + height - capHeight;
        RectF capRect = new RectF(x, capY, x + width, capY + capHeight);
        
        // Ombra cappello
        canvas.drawRoundRect(x + 5, capY, x + width, capY + capHeight, 8, 8, pipeShadowPaint);
        
        // Cappello principale
        canvas.drawRoundRect(capRect, 8, 8, pipePaint);
        
        // Highlight cappello
        canvas.drawRoundRect(x + 5, capY + 5, x + 25, capY + capHeight - 5, 5, 5, pipeHighlightPaint);
        
        // Bordo cappello
        canvas.drawRoundRect(capRect, 8, 8, pipeOutlinePaint);
        
        // Dettagli decorativi sul cappello
        Paint detailPaint = new Paint();
        detailPaint.setColor(Color.rgb(56, 142, 60));
        detailPaint.setAntiAlias(true);
        float detailY = capY + capHeight / 2;
        canvas.drawRect(x + 10, detailY - 2, x + width - 10, detailY + 2, detailPaint);
    }
    
    // Getters
    public float getX() {
        return x;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getTopHeight() {
        return topHeight;
    }
    
    public float getGap() {
        return gap;
    }
    
    public boolean isScored() {
        return scored;
    }
    
    public void setScored(boolean scored) {
        this.scored = scored;
    }
}

package com.example.flappyvoice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Background {
    
    private float cloudX1, cloudX2, cloudX3;
    private float cloudSpeed = 1.5f;
    
    private Paint skyPaint;
    private Paint groundPaint;
    private Paint grassPaint;
    private Paint cloudPaint;
    private Paint sunPaint;
    
    private int screenWidth;
    private int screenHeight;
    
    public Background(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        
        // Inizializza le posizioni delle nuvole
        cloudX1 = screenWidth * 0.2f;
        cloudX2 = screenWidth * 0.6f;
        cloudX3 = screenWidth * 1.2f;
        
        // Colori per lo sfondo
        skyPaint = new Paint();
        skyPaint.setColor(Color.rgb(135, 206, 235)); // Azzurro cielo
        
        groundPaint = new Paint();
        groundPaint.setColor(Color.rgb(139, 90, 43)); // Marrone terra
        
        grassPaint = new Paint();
        grassPaint.setColor(Color.rgb(34, 139, 34)); // Verde erba
        
        cloudPaint = new Paint();
        cloudPaint.setColor(Color.WHITE);
        cloudPaint.setAntiAlias(true);
        
        sunPaint = new Paint();
        sunPaint.setColor(Color.rgb(255, 223, 0)); // Giallo sole
        sunPaint.setAntiAlias(true);
    }
    
    public void update() {
        // Muovi le nuvole
        cloudX1 -= cloudSpeed;
        cloudX2 -= cloudSpeed;
        cloudX3 -= cloudSpeed;
        
        // Riposiziona le nuvole quando escono dallo schermo
        if (cloudX1 < -200) {
            cloudX1 = screenWidth + 200;
        }
        if (cloudX2 < -200) {
            cloudX2 = screenWidth + 200;
        }
        if (cloudX3 < -200) {
            cloudX3 = screenWidth + 200;
        }
    }
    
    public void draw(Canvas canvas) {
        // Disegna il cielo
        canvas.drawRect(0, 0, screenWidth, screenHeight, skyPaint);
        
        // Disegna il sole
        canvas.drawCircle(screenWidth - 150, 150, 60, sunPaint);
        
        // Disegna le nuvole
        drawCloud(canvas, cloudX1, screenHeight * 0.15f);
        drawCloud(canvas, cloudX2, screenHeight * 0.25f);
        drawCloud(canvas, cloudX3, screenHeight * 0.18f);
        
        // Disegna il terreno
        float groundHeight = 120;
        canvas.drawRect(0, screenHeight - groundHeight, screenWidth, screenHeight, groundPaint);
        
        // Disegna l'erba
        float grassHeight = 30;
        canvas.drawRect(0, screenHeight - groundHeight, screenWidth, screenHeight - groundHeight + grassHeight, grassPaint);
        
        // Disegna dettagli dell'erba
        Paint darkGrassPaint = new Paint();
        darkGrassPaint.setColor(Color.rgb(29, 119, 29));
        darkGrassPaint.setAntiAlias(true);
        darkGrassPaint.setStrokeWidth(3);
        
        for (int i = 0; i < screenWidth; i += 20) {
            float x = i;
            float y = screenHeight - groundHeight;
            canvas.drawLine(x, y, x - 3, y + 15, darkGrassPaint);
            canvas.drawLine(x, y, x + 3, y + 12, darkGrassPaint);
        }
    }
    
    private void drawCloud(Canvas canvas, float x, float y) {
        // Disegna una nuvola con cerchi sovrapposti
        canvas.drawCircle(x, y, 40, cloudPaint);
        canvas.drawCircle(x + 40, y, 50, cloudPaint);
        canvas.drawCircle(x + 80, y, 45, cloudPaint);
        canvas.drawCircle(x + 120, y, 40, cloudPaint);
        canvas.drawCircle(x + 40, y - 25, 35, cloudPaint);
        canvas.drawCircle(x + 80, y - 25, 40, cloudPaint);
    }
}

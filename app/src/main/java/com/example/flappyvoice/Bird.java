package com.example.flappyvoice;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Bird {
    
    private float x, y;
    private float velocityY;
    private float width, height;
    
    private static final float GRAVITY = 0.8f;
    private static final float LIFT = -12f;
    private static final float MAX_VELOCITY = 15f;
    
    private Paint bodyPaint;
    private Paint wingPaint;
    private Paint beakPaint;
    private Paint eyePaint;
    private Paint pupilPaint;
    
    private float wingAngle = 0;
    private boolean isFlapping = false;
    
    public Bird(int screenWidth, int screenHeight) {
        this.width = 80;
        this.height = 80;
        this.x = screenWidth / 4;
        this.y = screenHeight / 2;
        this.velocityY = 0;
        
        // Colori per il pappagallo
        bodyPaint = new Paint();
        bodyPaint.setColor(Color.rgb(46, 204, 113)); // Verde brillante
        bodyPaint.setAntiAlias(true);
        bodyPaint.setStyle(Paint.Style.FILL);
        
        wingPaint = new Paint();
        wingPaint.setColor(Color.rgb(26, 188, 156)); // Verde acqua
        wingPaint.setAntiAlias(true);
        wingPaint.setStyle(Paint.Style.FILL);
        
        beakPaint = new Paint();
        beakPaint.setColor(Color.rgb(241, 196, 15)); // Giallo
        beakPaint.setAntiAlias(true);
        beakPaint.setStyle(Paint.Style.FILL);
        
        eyePaint = new Paint();
        eyePaint.setColor(Color.WHITE);
        eyePaint.setAntiAlias(true);
        eyePaint.setStyle(Paint.Style.FILL);
        
        pupilPaint = new Paint();
        pupilPaint.setColor(Color.BLACK);
        pupilPaint.setAntiAlias(true);
        pupilPaint.setStyle(Paint.Style.FILL);
    }
    
    public void update(boolean isVoiceDetected) {
        // Se rileva la voce, l'uccello sale
        if (isVoiceDetected) {
            velocityY = LIFT;
            isFlapping = true;
        } else {
            // Altrimenti applica gravità
            velocityY += GRAVITY;
            isFlapping = false;
        }
        
        // Limita la velocità massima
        if (velocityY > MAX_VELOCITY) {
            velocityY = MAX_VELOCITY;
        }
        
        y += velocityY;
        
        // Aggiorna l'animazione delle ali
        if (isFlapping) {
            wingAngle = (wingAngle + 30) % 360;
        } else {
            wingAngle = (wingAngle + 10) % 360;
        }
    }
    
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);
        
        // Ruota leggermente in base alla velocità
        float rotation = velocityY * 3;
        if (rotation > 45) rotation = 45;
        if (rotation < -45) rotation = -45;
        canvas.rotate(rotation, width / 2, height / 2);
        
        // Disegna le ali
        drawWings(canvas);
        
        // Disegna il corpo (ovale)
        canvas.drawOval(10, 10, width - 10, height - 10, bodyPaint);
        
        // Disegna il becco
        Path beakPath = new Path();
        beakPath.moveTo(width - 15, height / 2);
        beakPath.lineTo(width + 10, height / 2 - 8);
        beakPath.lineTo(width + 10, height / 2 + 8);
        beakPath.close();
        canvas.drawPath(beakPath, beakPaint);
        
        // Disegna l'occhio
        float eyeX = width - 35;
        float eyeY = height / 2 - 10;
        canvas.drawCircle(eyeX, eyeY, 12, eyePaint);
        canvas.drawCircle(eyeX + 2, eyeY, 7, pupilPaint);
        
        // Disegna qualche dettaglio sul corpo
        Paint detailPaint = new Paint();
        detailPaint.setColor(Color.rgb(39, 174, 96));
        detailPaint.setAntiAlias(true);
        canvas.drawCircle(25, height - 25, 8, detailPaint);
        canvas.drawCircle(35, height - 30, 6, detailPaint);
        canvas.drawCircle(45, height - 28, 7, detailPaint);
        
        canvas.restore();
    }
    
    private void drawWings(Canvas canvas) {
        float wingY = height / 2;
        float wingSize = 25;
        
        // Calcola l'angolo delle ali in base all'animazione
        float currentWingAngle = (float) Math.sin(Math.toRadians(wingAngle)) * 30;
        
        // Ala sinistra (posteriore)
        canvas.save();
        canvas.rotate(currentWingAngle, 20, wingY);
        Path wingPath = new Path();
        wingPath.moveTo(20, wingY);
        wingPath.lineTo(5, wingY - wingSize);
        wingPath.lineTo(5, wingY - 5);
        wingPath.close();
        canvas.drawPath(wingPath, wingPaint);
        canvas.restore();
        
        // Ala destra (anteriore)
        canvas.save();
        canvas.rotate(-currentWingAngle, width - 20, wingY);
        Path wingPath2 = new Path();
        wingPath2.moveTo(width - 20, wingY);
        wingPath2.lineTo(width - 5, wingY - wingSize);
        wingPath2.lineTo(width - 5, wingY - 5);
        wingPath2.close();
        canvas.drawPath(wingPath2, wingPaint);
        canvas.restore();
    }
    
    // Getters
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
}

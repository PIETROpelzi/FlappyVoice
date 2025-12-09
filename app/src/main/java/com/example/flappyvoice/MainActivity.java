package com.example.flappyvoice;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private GameView gameView;
    private boolean permissionDeniedPermanently = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "onCreate called");
        Log.d(TAG, "Android Version: " + Build.VERSION.SDK_INT);
        Log.d(TAG, "Device: " + Build.MANUFACTURER + " " + Build.MODEL);
        
        try {
            // Nascondi la barra di stato e imposta fullscreen
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
            
            // Nascondi la navigation bar
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN
            );
            
            // Mantieni lo schermo acceso
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            
            // Richiedi permessi microfono
            if (checkPermission()) {
                Log.d(TAG, "Permission already granted");
                initGame();
            } else {
                Log.d(TAG, "Requesting permission");
                requestPermission();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "Errore inizializzazione: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void initGame() {
        try {
            Log.d(TAG, "Initializing game");
            gameView = new GameView(this);
            setContentView(gameView);
            Log.d(TAG, "Game initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing game: " + e.getMessage());
            Toast.makeText(this, "Errore creazione gioco: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        );
        boolean granted = result == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "Permission check result: " + granted);
        return granted;
    }
    
    private void requestPermission() {
        // Controlla se dobbiamo mostrare una spiegazione
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            Log.d(TAG, "Showing permission rationale");
            new AlertDialog.Builder(this)
                .setTitle("Permesso Microfono Necessario")
                .setMessage("Questo gioco utilizza il microfono per controllare l'uccello con la voce. " +
                           "Fai rumore per farlo volare! È necessario concedere il permesso per giocare.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            PERMISSION_REQUEST_CODE
                        );
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, 
                            "Il gioco non può funzionare senza il permesso microfono", 
                            Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .create()
                .show();
        } else {
            Log.d(TAG, "Requesting permission directly");
            ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                PERMISSION_REQUEST_CODE
            );
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        Log.d(TAG, "Permission result received for request code: " + requestCode);
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission GRANTED");
                Toast.makeText(this, "Permesso microfono concesso!", Toast.LENGTH_SHORT).show();
                initGame();
            } else {
                Log.d(TAG, "Permission DENIED");
                
                // Controlla se l'utente ha selezionato "Non chiedere più"
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                    permissionDeniedPermanently = true;
                    Log.d(TAG, "Permission denied permanently");
                    
                    new AlertDialog.Builder(this)
                        .setTitle("Permesso Negato")
                        .setMessage("Hai negato il permesso microfono in modo permanente. " +
                                   "Per giocare, devi abilitarlo manualmente nelle impostazioni dell'app.\n\n" +
                                   "Vai su: Impostazioni → App → Flappy Voice → Permessi → Microfono")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
                } else {
                    Toast.makeText(
                        this,
                        "Permesso microfono necessario per giocare!",
                        Toast.LENGTH_LONG
                    ).show();
                    finish();
                }
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        if (gameView != null) {
            try {
                gameView.pause();
            } catch (Exception e) {
                Log.e(TAG, "Error in onPause: " + e.getMessage());
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        
        // Nascondi di nuovo i controlli di sistema
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        
        if (gameView != null) {
            try {
                gameView.resume();
            } catch (Exception e) {
                Log.e(TAG, "Error in onResume: " + e.getMessage());
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        if (gameView != null) {
            try {
                gameView.destroy();
            } catch (Exception e) {
                Log.e(TAG, "Error in onDestroy: " + e.getMessage());
            }
        }
    }
}

# 🔧 GUIDA RAPIDA - Personalizzazione Flappy Voice

## 📍 Dove Trovare le Funzioni Principali

### 🎮 CONTROLLO GIOCO
| Funzione | File | Linea | Cosa Fa |
|----------|------|-------|---------|
| Game Loop | GameView.java | ~95 | Loop principale 60 FPS |
| Update | GameView.java | ~115 | Aggiorna fisica e logica |
| Draw | GameView.java | ~215 | Disegna tutto |
| Collisioni | GameView.java | ~175 | Verifica scontri |
| Game Over | GameView.java | ~195 | Fine gioco |

### 🎤 AUDIO
| Funzione | File | Linea | Cosa Fa |
|----------|------|-------|---------|
| Init Audio | VoiceDetector.java | ~40 | Inizializza microfono |
| Process Audio | VoiceDetector.java | ~165 | Analizza suono |
| Is Voice | VoiceDetector.java | ~210 | Controlla se c'è voce |

### 🦜 PAPPAGALLO
| Funzione | File | Linea | Cosa Fa |
|----------|------|-------|---------|
| Update Bird | Bird.java | ~75 | Fisica movimento |
| Draw Bird | Bird.java | ~95 | Disegna pappagallo |
| Draw Wings | Bird.java | ~140 | Animazione ali |

### 🌿 TUBI
| Funzione | File | Linea | Cosa Fa |
|----------|------|-------|---------|
| Constructor | Pipe.java | ~20 | Crea tubo random |
| Update Pipe | Pipe.java | ~65 | Muove tubo |
| Draw Pipe | Pipe.java | ~70 | Disegna tubo |

---

## ⚙️ COSTANTI DA MODIFICARE

### 🎯 DIFFICOLTÀ

#### Più Facile
```java
// Pipe.java linea ~35
this.gap = 650;        // Spazio maggiore (default 550)
this.speed = 5;        // Più lento (default 6)

// Bird.java linee 12-14
private static final float GRAVITY = 0.6f;    // Meno gravità (default 0.8)
private static final float LIFT = -14f;       // Più spinta (default -12)

// GameView.java linea ~60
private static final long PIPE_SPAWN_INTERVAL = 2500;  // Meno tubi (default 2000)
```

#### Più Difficile
```java
// Pipe.java linea ~35
this.gap = 450;        // Spazio minore
this.speed = 8;        // Più veloce

// Bird.java linee 12-14
private static final float GRAVITY = 1.0f;    // Più gravità
private static final float LIFT = -10f;       // Meno spinta

// GameView.java linea ~60
private static final long PIPE_SPAWN_INTERVAL = 1500;  // Più tubi
```

### 🎤 SENSIBILITÀ MICROFONO
```java
// VoiceDetector.java linea ~30
private static final int AMPLITUDE_THRESHOLD = 2000;

// Più sensibile (si attiva facilmente):  1000-1500
// Bilanciato (normale):                  2000-3000
// Meno sensibile (serve più volume):    4000-5000
```

---

## 🎨 PERSONALIZZAZIONE GRAFICA

### Colori Pappagallo
```java
// Bird.java linea ~25
bodyPaint.setColor(Color.rgb(46, 204, 113));    // Verde corpo
wingPaint.setColor(Color.rgb(26, 188, 156));    // Verde ali
beakPaint.setColor(Color.rgb(241, 196, 15));    // Giallo becco
```

### Colori Tubi
```java
// Pipe.java linea ~42
pipePaint.setColor(Color.rgb(76, 175, 80));           // Verde
pipeOutlinePaint.setColor(Color.rgb(56, 142, 60));   // Verde scuro
pipeHighlightPaint.setColor(Color.rgb(129, 199, 132)); // Verde chiaro
```

### Colori Sfondo
```java
// Background.java linea ~30
skyPaint.setColor(Color.rgb(135, 206, 235));    // Azzurro cielo
groundPaint.setColor(Color.rgb(139, 90, 43));   // Marrone terra
grassPaint.setColor(Color.rgb(34, 139, 34));    // Verde erba
sunPaint.setColor(Color.rgb(255, 223, 0));      // Giallo sole
```

---

## 🔢 DIMENSIONI

### Pappagallo
```java
// Bird.java linea ~20
this.width = 80;     // Larghezza
this.height = 80;    // Altezza
```

### Tubi
```java
// Pipe.java linea ~33
this.width = 120;    // Larghezza tubo
this.gap = 550;      // Spazio tra tubi
```

---

## 📊 PRESTAZIONI

### FPS Target
```java
// GameView.java linea ~105
Thread.sleep(16);    // 60 FPS (16ms per frame)
// Per 30 FPS: Thread.sleep(33);
```

---

## 🐛 DEBUG

### Attiva Log Dettagliati
```java
// VoiceDetector.java linea ~185
// Decommenta questa riga per vedere ampiezza audio
Log.d(TAG, "Audio amplitude: " + amplitude + ", Voice detected: " + voiceDetected);
```

### Mostra FPS
```java
// GameView.java nel metodo draw(), aggiungi:
long fps = 1000 / timeThisFrame;
canvas.drawText("FPS: " + fps, 100, 100, scorePaint);
```

---

## 🎯 MODIFICHE RAPIDE COMUNI

### 1. Rendere il Gioco Più Facile
**File da modificare:** `Pipe.java`
```java
this.gap = 650;  // Cambia questa linea (linea ~35)
```

### 2. Pappagallo Più Veloce
**File da modificare:** `Bird.java`
```java
private static final float LIFT = -15f;  // Cambia questa linea (linea ~13)
```

### 3. Microfono Più Sensibile
**File da modificare:** `VoiceDetector.java`
```java
private static final int AMPLITUDE_THRESHOLD = 1500;  // Cambia questa linea (linea ~30)
```

### 4. Tubi Più Lenti
**File da modificare:** `Pipe.java`
```java
this.speed = 4;  // Cambia questa linea (linea ~37)
```

### 5. Più Tempo Tra i Tubi
**File da modificare:** `GameView.java`
```java
private static final long PIPE_SPAWN_INTERVAL = 3000;  // Cambia questa linea (linea ~60)
```

---

## 📝 CHECKLIST MODIFICHE

Prima di compilare, verifica:
- [ ] Ho modificato le costanti corrette?
- [ ] Ho salvato tutti i file?
- [ ] Ho fatto Build → Clean Project?
- [ ] Ho fatto Build → Rebuild Project?

---

## 🚀 TEST RAPIDO

Dopo ogni modifica:
1. Build → Rebuild Project
2. Run sul dispositivo
3. Testa per 1-2 minuti
4. Regola se necessario

---

**📍 Usa questo file come riferimento rapido per modifiche veloci!**

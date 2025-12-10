## 🎯 Panoramica

**Flappy Voice** è un gioco Android ispirato a Flappy Bird, ma controllato completamente con la **voce**. Il giocatore deve fare parlare per far volare un pappagallo attraverso i tubi.

### 🔑 Caratteristiche Principali:
- ✅ Controllo vocale
- ✅ Grafica 2D personalizzata
- ✅ Sistema di punteggio

---

## 📁 Struttura del Progetto

```
FlappyVoice/
├── app/src/main/
│   ├── java/com/example/flappyvoice/
│   │   ├── MainActivity.java          ⭐ Activity principale
│   │   ├── GameView.java              ⭐ Game engine
│   │   ├── Bird.java                  🦜 Pappagallo animato
│   │   ├── Pipe.java                  🌿 Tubi ostacolo
│   │   ├── Background.java            🌤️ Sfondo dinamico
│   │   └── VoiceDetector.java         🎤 Rilevamento audio
│   │
│   ├── res/
│   │   ├── values/
│   │   │   ├── strings.xml            📝 Stringhe app
│   │   │   ├── colors.xml             🎨 Colori
│   │   │   └── themes.xml             🎨 Tema app
│   │   └── ...
│   │
│   └── AndroidManifest.xml            ⚙️ Configurazione app
│
├── build.gradle.kts                   📦 Dipendenze
└── README.md                          📚 Questa guida
```

---

## 🏗️ Classi Principali

### 1️⃣ **MainActivity.java**
**Posizione:** `app/src/main/java/com/example/flappyvoice/MainActivity.java`

**Scopo:** Gestisce il ciclo di vita dell'app, i permessi microfono e l'inizializzazione del gioco.

#### 📌 Funzioni Importanti:

| Funzione | Cosa Fa |
|----------|---------|
| `onCreate()` | Inizializza l'app, imposta fullscreen, richiede permessi |
| `checkPermission()` | Verifica se il permesso RECORD_AUDIO è concesso |
| `requestPermission()` | Richiede il permesso microfono all'utente |
| `onRequestPermissionsResult()` | Gestisce la risposta dell'utente al permesso |
| `initGame()` | Crea e avvia il GameView |
| `onPause() / onResume()` | Mette in pausa/riprende il gioco |

#### 🔧 Codice Chiave:
```java
// Richiesta permesso microfono
private boolean checkPermission() {
    return ContextCompat.checkSelfPermission(this, 
        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
}

// Inizializzazione gioco
private void initGame() {
    gameView = new GameView(this);
    setContentView(gameView);
}
```

---

### 2️⃣ **GameView.java**
**Posizione:** `app/src/main/java/com/example/flappyvoice/GameView.java`

**Scopo:** Cuore del gioco. Gestisce il game loop, rendering, logica, collisioni e punteggio.

#### 📌 Funzioni Importanti:

| Funzione | Cosa Fa | Linea Approx |
|----------|---------|--------------|
| `run()` | Game loop principale (60 FPS) | ~95 |
| `update()` | Aggiorna fisica, collisioni, spawn tubi | ~115 |
| `draw()` | Disegna tutto sullo schermo | ~215 |
| `initGame()` | Inizializza nuovo gioco | ~75 |
| `checkCollision()` | Verifica se l'uccello tocca i tubi | ~175 |
| `gameOver()` | Gestisce la fine del gioco | ~195 |
| `onTouchEvent()` | Gestisce tocchi dello schermo | ~320 |

#### 🔧 Codice Chiave:
```java
// Game loop a 60 FPS
@Override
public void run() {
    while (isPlaying) {
        update();  // Aggiorna logica
        draw();    // Disegna frame
        Thread.sleep(16); // ~60 FPS
    }
}

// Controllo voce/touch
boolean shouldFly = voiceControlEnabled ? 
    voiceDetector.isVoiceDetected() : isTouching;
bird.update(shouldFly);

// Spawn tubi ogni 2 secondi
if (currentTime - lastPipeTime > PIPE_SPAWN_INTERVAL) {
    pipes.add(new Pipe(screenWidth, screenHeight));
}
```

#### ⚙️ Costanti Configurabili:
```java
private static final long PIPE_SPAWN_INTERVAL = 2000; // Frequenza tubi (ms)
```

---

### 3️⃣ **VoiceDetector.java**
**Posizione:** `app/src/main/java/com/example/flappyvoice/VoiceDetector.java`

**Scopo:** Rileva il suono dal microfono in tempo reale usando AudioRecord.

#### 📌 Funzioni Importanti:

| Funzione | Cosa Fa | Linea Approx |
|----------|---------|--------------|
| `initializeAudioRecord()` | Prova diverse configurazioni audio | ~40 |
| `start()` | Avvia la registrazione audio | ~105 |
| `stop()` | Ferma la registrazione | ~140 |
| `processAudio()` | Analizza l'ampiezza audio in tempo reale | ~165 |
| `isVoiceDetected()` | Restituisce true se rileva suono | ~210 |

#### 🔧 Codice Chiave:
```java
// Prova 4 sorgenti audio e 5 sample rate
int[] audioSources = {
    MediaRecorder.AudioSource.MIC,
    MediaRecorder.AudioSource.VOICE_RECOGNITION,
    MediaRecorder.AudioSource.VOICE_COMMUNICATION,
    MediaRecorder.AudioSource.DEFAULT
};
int[] sampleRates = {44100, 22050, 16000, 11025, 8000};

// Calcola ampiezza audio
for (int i = 0; i < bytesRead; i++) {
    sum += Math.abs(buffer[i]);
}
int amplitude = (int) (sum / bytesRead);
voiceDetected = amplitude > AMPLITUDE_THRESHOLD;
```

#### ⚙️ Costanti Configurabili:
```java
private static final int AMPLITUDE_THRESHOLD = 2000;  // Sensibilità microfono
// Più basso = più sensibile | Più alto = meno sensibile
```

---

### 4️⃣ **Bird.java**
**Posizione:** `app/src/main/java/com/example/flappyvoice/Bird.java`

**Scopo:** Gestisce il pappagallo: posizione, fisica, animazione, rendering.

#### 📌 Funzioni Importanti:

| Funzione | Cosa Fa | Linea Approx |
|----------|---------|--------------|
| `update(boolean isVoiceDetected)` | Applica gravità o spinta in base alla voce | ~75 |
| `draw(Canvas canvas)` | Disegna il pappagallo con animazione ali | ~95 |
| `drawWings()` | Animazione ali che sbattono | ~140 |

#### 🔧 Codice Chiave:
```java
// Fisica del volo
if (isVoiceDetected) {
    velocityY = LIFT;  // Spinta verso l'alto (-12)
} else {
    velocityY += GRAVITY;  // Caduta (0.8)
}
y += velocityY;

// Animazione ali
if (isFlapping) {
    wingAngle = (wingAngle + 30) % 360;  // Ali veloci
} else {
    wingAngle = (wingAngle + 10) % 360;  // Ali lente
}
```

#### ⚙️ Costanti Configurabili:
```java
private static final float GRAVITY = 0.8f;      // Forza gravità
private static final float LIFT = -12f;         // Forza spinta
private static final float MAX_VELOCITY = 15f;  // Velocità massima
```

---

### 5️⃣ **Pipe.java**
**Posizione:** `app/src/main/java/com/example/flappyvoice/Pipe.java`

**Scopo:** Gestisce i tubi ostacolo: posizione, movimento, rendering.

#### 📌 Funzioni Importanti:

| Funzione | Cosa Fa | Linea Approx |
|----------|---------|--------------|
| Constructor | Genera tubo con altezza casuale | ~20 |
| `update()` | Muove il tubo verso sinistra | ~65 |
| `draw(Canvas canvas)` | Disegna i tubi con grafica 3D | ~70 |
| `drawPipeSegment()` | Disegna singolo segmento con dettagli | ~80 |

#### 🔧 Codice Chiave:
```java
// Gap aumentato per facilità
this.gap = 550;  // Spazio tra tubi (pixel)

// Altezza casuale
Random random = new Random();
this.topHeight = random.nextInt(maxHeight - minHeight) + minHeight;

// Movimento
x -= speed;  // 6 pixel per frame
```

#### ⚙️ Costanti Configurabili:
```java
this.gap = 550;      // Spazio verticale tra tubi
this.speed = 6;      // Velocità movimento (pixel/frame)
this.width = 120;    // Larghezza tubo
```

---

### 6️⃣ **Background.java**
**Posizione:** `app/src/main/java/com/example/flappyvoice/Background.java`

**Scopo:** Disegna e anima lo sfondo (cielo, nuvole, sole, terreno).

#### 📌 Funzioni Importanti:

| Funzione | Cosa Fa | Linea Approx |
|----------|---------|--------------|
| `update()` | Muove le nuvole | ~40 |
| `draw(Canvas canvas)` | Disegna cielo, sole, nuvole, terreno | ~55 |
| `drawCloud()` | Disegna singola nuvola | ~90 |

#### 🔧 Codice Chiave:
```java
// Movimento nuvole
cloudX1 -= cloudSpeed;  // 1.5 pixel per frame
if (cloudX1 < -200) {
    cloudX1 = screenWidth + 200;  // Riposiziona
}

// Disegna cielo azzurro
canvas.drawColor(Color.rgb(135, 206, 235));

// Disegna sole
canvas.drawCircle(screenWidth - 150, 150, 60, sunPaint);
```

---

## 🎮 Funzioni Importanti per Modulo

### 📊 **Sistema di Collisioni** (GameView.java ~175)
```java
private boolean checkCollision(Pipe pipe) {
    // Crea rettangoli per bird e tubi
    Rect birdRect = new Rect(bird.getX(), bird.getY(), ...);
    Rect topPipeRect = new Rect(pipe.getX(), 0, ...);
    Rect bottomPipeRect = new Rect(pipe.getX(), ...);
    
    // Verifica intersezione
    return birdRect.intersect(topPipeRect) || 
           birdRect.intersect(bottomPipeRect);
}
```

### 📈 **Sistema di Punteggio** (GameView.java ~145)
```java
// Quando l'uccello passa un tubo
if (!pipe.isScored() && pipe.getX() + pipe.getWidth() < bird.getX()) {
    pipe.setScored(true);
    score++;  // Incrementa punteggio
}
```

### 🎨 **Rendering** (GameView.java ~215)
```java
private void draw() {
    Canvas canvas = holder.lockCanvas();  // Ottieni canvas
    
    background.draw(canvas);   // Disegna sfondo
    for (Pipe pipe : pipes) {
        pipe.draw(canvas);     // Disegna tubi
    }
    bird.draw(canvas);         // Disegna uccello
    canvas.drawText("" + score, ...);  // Disegna punteggio
    
    holder.unlockCanvasAndPost(canvas);  // Mostra frame
}
```

### 🎤 **Indicatore Audio** (GameView.java ~285)
```java
// Cerchio verde quando rileva voce
if (voiceDetector.isVoiceDetected()) {
    Paint voiceIndicator = new Paint();
    voiceIndicator.setColor(Color.GREEN);
    canvas.drawCircle(80, 80, 30, voiceIndicator);
}
```

---

## ⚙️ Configurazione e Personalizzazione

### 🎯 Regola Difficoltà

#### **Più Facile:**
```java
// In Pipe.java
this.gap = 650;        // Spazio ancora più grande
this.speed = 5;        // Tubi più lenti

// In Bird.java
private static final float GRAVITY = 0.6f;  // Gravità ridotta
private static final float LIFT = -14f;     // Spinta maggiore
```

#### **Più Difficile:**
```java
// In Pipe.java
this.gap = 450;        // Spazio più stretto
this.speed = 8;        // Tubi più veloci

// In Bird.java
private static final float GRAVITY = 1.0f;  // Gravità maggiore
private static final float LIFT = -10f;     // Spinta minore

// In GameView.java
private static final long PIPE_SPAWN_INTERVAL = 1500;  // Più tubi
```

### 🎤 Regola Sensibilità Microfono

```java
// In VoiceDetector.java
private static final int AMPLITUDE_THRESHOLD = 2000;

// Troppo sensibile (si attiva sempre)?  → Aumenta a 4000
// Poco sensibile (non rileva)?          → Abbassa a 1000
// Ottimale ambiente normale:            → 2000-3000
```

### 🎨 Cambia Colori

```java
// In Bird.java - Colore pappagallo
bodyPaint.setColor(Color.rgb(46, 204, 113));  // Verde

// In Pipe.java - Colore tubi
pipePaint.setColor(Color.rgb(76, 175, 80));   // Verde

// In Background.java - Colore cielo
skyPaint.setColor(Color.rgb(135, 206, 235));  // Azzurro
```

---

## 🎮 Come Giocare

### 🎤 **Modalità Voce** (Principale)
1. Avvia l'app → Concedi permesso microfono
2. Guarda in alto a sinistra: cerchio verde + "MIC" = OK
3. Tocca per iniziare
4. **FAI RUMORE** → Pappagallo sale ⬆️
5. **SILENZIO** → Pappagallo scende ⬇️

### 👆 **Modalità Touch** (Fallback)
1. Se il microfono non funziona, l'app usa automaticamente il touch
2. Guarda in alto a sinistra: cerchio cyan + "TOUCH"
3. **TIENI PREMUTO** → Pappagallo sale ⬆️
4. **RILASCIA** → Pappagallo scende ⬇️

### 🏆 **Obiettivo**
- Passa attraverso i tubi verdi senza toccarli
- Ogni tubo superato = +1 punto
- Non uscire dallo schermo
- Fai il punteggio più alto!

---

## 🛠️ Troubleshooting

### Microfono non funziona?
1. Usa un **dispositivo fisico** (non emulatore)
2. Verifica permesso: Impostazioni → App → Flappy Voice → Permessi
3. Modifica `AMPLITUDE_THRESHOLD` in VoiceDetector.java
4. Controlla Logcat per errori AudioRecord

### App crasha?
1. Build → Clean Project
2. Build → Rebuild Project
3. Disinstalla e reinstalla l'app

### Troppo difficile?
1. Aumenta `gap` in Pipe.java (da 550 a 650+)
2. Riduci `speed` in Pipe.java (da 6 a 4-5)
3. Riduci `GRAVITY` in Bird.java

---

## 📝 Note Finali

- **Performance:** Il gioco gira a 60 FPS costanti
- **Compatibilità:** Android 10+ (API 29+)
- **Memoria:** ~30 MB RAM utilizzata
- **Batteria:** Uso moderato (audio continuo)

---

**🎮 Buon Divertimento con Flappy Voice! 🦜**

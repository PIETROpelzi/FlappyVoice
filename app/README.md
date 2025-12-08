# Flappy Voice - Gioco Android controllato dalla voce

## Descrizione
Flappy Voice è una versione del famoso gioco Flappy Bird controllata dalla voce. Invece di toccare lo schermo, il giocatore deve fare rumore (parlare, fischiare, cantare) per far volare il pappagallo verso l'alto. Quando non c'è rumore, il pappagallo scende per gravità.

## Caratteristiche
- 🎮 Gameplay classico di Flappy Bird
- 🎤 Controllo vocale: fai rumore per volare!
- 🦜 Grafica personalizzata con un pappagallo verde
- 🌤️ Sfondo animato con nuvole e sole
- 🌿 Tubi stile Mario con dettagli grafici
- 💚 Indicatore visivo quando la voce viene rilevata (cerchio verde)
- 📊 Punteggio in tempo reale

## Come giocare
1. Avvia l'app e concedi i permessi per il microfono
2. Tocca lo schermo per iniziare
3. Fai rumore (parla, fischia, canta) per far salire il pappagallo
4. Smetti di fare rumore per far scendere il pappagallo
5. Evita i tubi verdi
6. Cerca di ottenere il punteggio più alto!

## Struttura del progetto
```
app/src/main/java/com/example/flappyvoice/
├── MainActivity.java       - Activity principale, gestisce i permessi
├── GameView.java          - Vista del gioco, gestisce il game loop
├── Bird.java              - Classe del pappagallo con animazioni
├── Pipe.java              - Classe dei tubi ostacoli
├── Background.java        - Sfondo animato con nuvole
└── VoiceDetector.java     - Rilevamento audio del microfono
```

## Tecnologie utilizzate
- **Java puro** (nessun Kotlin)
- **Android Canvas** per la grafica 2D
- **AudioRecord API** per il rilevamento vocale
- **SurfaceView** per il rendering efficiente

## Impostazioni personalizzabili
Nel codice puoi modificare:
- `AMPLITUDE_THRESHOLD` in VoiceDetector.java (sensibilità del microfono)
- `PIPE_SPAWN_INTERVAL` in GameView.java (frequenza spawn tubi)
- `GRAVITY` e `LIFT` in Bird.java (fisica del volo)
- Colori e dimensioni in tutte le classi

## Requisiti
- Android API 29 (Android 10) o superiore
- Microfono funzionante
- Permesso RECORD_AUDIO

## Note
- Il cerchio verde in alto a sinistra indica quando la voce viene rilevata
- La sensibilità del microfono è calibrata per ambienti normali
- Se il gioco è troppo difficile/facile, modifica AMPLITUDE_THRESHOLD

## Compilazione
1. Apri il progetto in Android Studio
2. Sincronizza Gradle
3. Collega un dispositivo Android o avvia un emulatore
4. Clicca Run

Buon divertimento! 🎮🎤

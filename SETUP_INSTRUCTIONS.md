# 🎮 Flappy Voice - Setup e Risoluzione Problemi

## ⚠️ PROBLEMA RISOLTO: "Redeclaration of MainActivity"

Ho sistemato tutto! Il problema era causato dalla presenza contemporanea di file Kotlin e Java.

## 🔧 PASSI PER FAR FUNZIONARE IL PROGETTO

### Opzione 1: Script Automatico (CONSIGLIATO)

1. **Esegui lo script di pulizia:**
   - Vai nella cartella principale del progetto
   - Fai doppio click su `cleanup_kotlin.bat`
   - Aspetta che completi la pulizia

2. **In Android Studio:**
   - Apri il progetto
   - `File → Invalidate Caches / Restart` → Clicca "Invalidate and Restart"
   - Aspetta che Android Studio si riavvii
   - `File → Sync Project with Gradle Files`
   - `Build → Clean Project`
   - `Build → Rebuild Project`

3. **Testa l'app:**
   - Collega un dispositivo Android fisico (l'emulatore potrebbe non avere il microfono)
   - Premi il pulsante Run ▶️
   - Concedi i permessi microfono quando richiesto
   - Gioca!

### Opzione 2: Pulizia Manuale

Se lo script non funziona, elimina manualmente questi file:

```
❌ ELIMINA QUESTI FILE:
app/src/main/java/com/example/flappyvoice/MainActivity.kt
app/src/main/java/com/example/flappyvoice/ui/ (intera cartella)
app/src/androidTest/java/com/example/flappyvoice/ExampleInstrumentedTest.kt
app/src/test/java/com/example/flappyvoice/ExampleUnitTest.kt
```

Poi segui i passi del punto 2 dell'Opzione 1.

---

## 📁 STRUTTURA FINALE DEL PROGETTO (Solo Java)

```
FlappyVoice/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/flappyvoice/
│   │   │   │   ├── MainActivity.java ✅
│   │   │   │   ├── GameView.java ✅
│   │   │   │   ├── Bird.java ✅
│   │   │   │   ├── Pipe.java ✅
│   │   │   │   ├── Background.java ✅
│   │   │   │   └── VoiceDetector.java ✅
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml ✅
│   │   ├── androidTest/
│   │   │   └── java/.../ExampleInstrumentedTest.java ✅
│   │   └── test/
│   │       └── java/.../ExampleUnitTest.java ✅
│   └── build.gradle.kts ✅
├── build.gradle.kts ✅
├── gradle/libs.versions.toml ✅
└── cleanup_kotlin.bat 🆕
```

---

## ✅ FILE AGGIORNATI

Ho già aggiornato questi file per te:

1. ✅ `build.gradle.kts` (root) - Rimossi plugin Kotlin
2. ✅ `app/build.gradle.kts` - Configurato per Java puro
3. ✅ `gradle/libs.versions.toml` - Rimossi Kotlin e Compose
4. ✅ `AndroidManifest.xml` - Permessi microfono aggiunti
5. ✅ Creati tutti i file .java necessari
6. ✅ Creati file di test in Java

---

## 🎮 COME GIOCARE

1. **Avvia l'app** e concedi i permessi microfono
2. **Tocca lo schermo** per iniziare
3. **Fai rumore** (parla, fischia, canta) → il pappagallo sale ⬆️
4. **Silenzio** → il pappagallo scende ⬇️
5. **Evita i tubi verdi** 🌿
6. **Guarda il cerchio verde** in alto a sinistra: si accende quando rileva la voce

---

## 🔧 RISOLUZIONE PROBLEMI

### Problema: "Still redeclaration error"
**Soluzione:**
```bash
1. Chiudi Android Studio
2. Elimina manualmente MainActivity.kt
3. Elimina la cartella .gradle
4. Elimina la cartella app/build
5. Riapri Android Studio
6. File → Invalidate Caches / Restart
7. Sync Gradle
```

### Problema: "Microfono non funziona"
**Soluzione:**
- Usa un dispositivo Android FISICO (non emulatore)
- Verifica i permessi in Impostazioni → App → Flappy Voice → Permessi
- Prova ad alzare/abbassare la voce

### Problema: "Troppo difficile/facile"
**Soluzione:** Modifica `AMPLITUDE_THRESHOLD` in `VoiceDetector.java`:
- **Troppo difficile?** → Abbassa il valore (es: 2000)
- **Troppo facile?** → Alza il valore (es: 5000)

### Problema: "Build failed"
**Soluzione:**
```bash
1. Build → Clean Project
2. Build → Rebuild Project
3. Se persiste: elimina .gradle e app/build, poi riprova
```

---

## 📱 REQUISITI

- ✅ Android Studio (versione recente)
- ✅ Android SDK 29+ (Android 10+)
- ✅ Dispositivo Android fisico con microfono
- ✅ JDK 11

---

## 🆘 SUPPORTO

Se hai ancora problemi:

1. Verifica che NON ci siano file `.kt` nel progetto
2. Controlla che `build.gradle.kts` sia aggiornato
3. Esegui `cleanup_kotlin.bat`
4. Invalida cache e riavvia Android Studio

---

## 🎯 CARATTERISTICHE

- 🦜 Pappagallo verde animato
- 🌤️ Sfondo con nuvole animate
- 🌿 Tubi verdi stile Mario
- 🎤 Controllo vocale in tempo reale
- 💚 Indicatore visivo voce
- 📊 Sistema punteggio

---

**TUTTO È PRONTO! Esegui `cleanup_kotlin.bat` e poi compila in Android Studio!** 🚀

# 🎤 FIX MICROFONO - Flappy Voice

## ⚠️ PROBLEMA IDENTIFICATO

L'errore nel log:
```
NPC::validateUidPackagePair: uid not found: 10220 for package com.example.flappyvoice
```

Questo è un **bug noto** che si verifica quando:
1. Usi un **emulatore Android** (non ha microfono reale)
2. L'app non è correttamente registrata nel sistema audio

## ✅ SOLUZIONI

### 🔴 SOLUZIONE 1: USA DISPOSITIVO FISICO (RACCOMANDATO)

**L'emulatore NON ha supporto audio completo!**

1. **Collega un telefono/tablet Android reale via USB**
2. Abilita il **Debug USB** sul dispositivo:
   - Impostazioni → Info telefono
   - Tocca 7 volte su "Numero build"
   - Torna indietro → Opzioni sviluppatore
   - Abilita "Debug USB"

3. **Esegui lo script di pulizia:**
   ```
   Fai doppio click su: fix_microphone.bat
   ```

4. **In Android Studio:**
   - File → Invalidate Caches / Restart
   - Build → Clean Project
   - Build → Rebuild Project
   - Seleziona il tuo dispositivo fisico
   - Run ▶️

5. **Concedi il permesso microfono quando richiesto**

6. **Testa facendo RUMORE** (parla, fischia, batti le mani)

---

### 🟡 SOLUZIONE 2: Configura l'emulatore (se NON hai dispositivo fisico)

Se devi usare l'emulatore:

1. **Apri AVD Manager** in Android Studio
2. **Modifica l'emulatore**:
   - Clicca sull'icona matita accanto all'emulatore
   - "Show Advanced Settings"
   - Sezione "Camera":
     - Front: Webcam
     - Back: Webcam
   - Sezione "Audio":
     - ✅ "Enable audio input"
     - ✅ "Enable audio output"
   - Salva

3. **Avvia l'emulatore con parametri audio:**
   - Chiudi l'emulatore attuale
   - Apri Terminal/CMD
   - Vai alla cartella Android SDK/emulator
   - Esegui:
     ```
     emulator -avd YOUR_AVD_NAME -qemu -audio-in default
     ```

4. **Nelle impostazioni dell'emulatore:**
   - Settings → Apps → Permissions
   - Concedi manualmente il permesso Microfono a Flappy Voice

---

### 🟢 SOLUZIONE 3: Reinstallazione completa

1. **Disinstalla completamente l'app:**
   ```
   adb uninstall com.example.flappyvoice
   ```

2. **Pulisci tutto:**
   ```
   Esegui: fix_microphone.bat
   ```

3. **In Android Studio:**
   - File → Invalidate Caches / Restart
   - Build → Clean Project
   - Build → Rebuild Project

4. **Reinstalla ed esegui**

---

## 🔍 VERIFICA CHE IL MICROFONO FUNZIONI

### Test Rapido:

1. Avvia l'app
2. Guarda in alto a sinistra:
   - **Cerchio VERDE + "MIC"** = Microfono OK ✅
   - **"TOUCH"** = Microfono non funziona ❌

3. Se vedi "MIC", fai rumore:
   - Il cerchio diventa verde brillante
   - L'uccello sale

### Log di Debug:

Guarda il Logcat per questi messaggi:

**✅ Microfono OK:**
```
D/VoiceDetector: AudioRecord initialized successfully
D/VoiceDetector: Recording started successfully
D/VoiceDetector: Audio amplitude: 5000, Voice detected: true
```

**❌ Microfono NON OK:**
```
E/AudioRecord: Error code -20 when initializing
E/VoiceDetector: AudioRecord initialization failed
E/VoiceDetector: Cannot start - AudioRecord not initialized
```

---

## 📱 REQUISITI HARDWARE

### ✅ Dispositivi Supportati:
- **Smartphone Android reale** (consigliato)
- **Tablet Android reale**
- Emulatore con audio configurato

### ❌ NON Funziona su:
- Emulatore base senza configurazione audio
- Dispositivi senza microfono
- Android <6.0

---

## 🎮 COME GIOCARE CON LA VOCE

1. **Avvia l'app** → Concedi permesso microfono
2. **Verifica** che in alto a sinistra ci sia scritto "MIC"
3. **Tocca** per iniziare il gioco
4. **FAI RUMORE** (parla, urla, fischia, batti le mani):
   - L'uccello **SALE** ⬆️
   - Il cerchio verde si illumina
5. **SILENZIO**:
   - L'uccello **SCENDE** ⬇️
6. **Evita i tubi** verdi! 🌿

### Consigli:
- **Suoni continui** funzionano meglio (es: "aaahhh")
- **Più forte** = più facile da rilevare
- **Battere le mani** funziona bene
- **Fischiare** è molto efficace

---

## ⚙️ REGOLA LA SENSIBILITÀ

Se il microfono è troppo/poco sensibile, modifica in **VoiceDetector.java**:

```java
private static final int AMPLITUDE_THRESHOLD = 2000;
```

- **Troppo sensibile** (si attiva sempre): aumenta a 4000
- **Poco sensibile** (non rileva): abbassa a 1000
- **Default ottimale**: 2000-3000

---

## 🆘 TROUBLESHOOTING

### Problema: "Permission denied"
**Soluzione:**
- Disinstalla l'app completamente
- Esegui fix_microphone.bat
- Reinstalla e concedi il permesso

### Problema: "Microfono non si attiva"
**Soluzione:**
- Verifica che non ci siano altre app che usano il microfono
- Riavvia il dispositivo
- Prova su dispositivo fisico

### Problema: "App crasha all'avvio"
**Soluzione:**
- Build → Clean Project
- Build → Rebuild Project
- Disinstalla e reinstalla

### Problema: "AudioRecord error -20"
**Soluzione:**
- Questo è specifico dell'emulatore
- **USA UN DISPOSITIVO FISICO!**

---

## 📊 MODIFICHE EFFETTUATE

### ✅ AndroidManifest.xml
- Aggiunto `uses-feature` per microfono
- Configurazioni audio migliorate

### ✅ VoiceDetector.java
- Prova 4 diversi AudioSource
- Prova 5 diversi sample rate
- Buffer size ottimizzato
- Gestione errori completa
- Logging dettagliato

### ✅ MainActivity.java
- Dialog per spiegare il permesso
- Gestione "non chiedere più"
- Log dettagliati

### ✅ GameView.java
- Modalità dual (voce + touch)
- Indicatore visivo dello stato
- Fallback automatico

---

## 🚀 CHECKLIST FINALE

Prima di testare:

- [ ] Ho eseguito fix_microphone.bat
- [ ] Ho fatto Clean + Rebuild in Android Studio
- [ ] Sto usando un **DISPOSITIVO FISICO** (non emulatore)
- [ ] Ho abilitato il Debug USB sul dispositivo
- [ ] Ho concesso il permesso microfono all'app
- [ ] Non ci sono altre app che usano il microfono
- [ ] Ho controllato che il microfono del dispositivo funzioni

Se tutti i punti sono ✅, il gioco **DEVE** funzionare con la voce!

---

**🎤 IMPORTANTE: Usa un dispositivo Android REALE per il miglior risultato!**

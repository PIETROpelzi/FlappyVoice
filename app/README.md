# 🎮 Flappy Voice - Guida Rapida

## 🚀 Quick Start

1. **Apri il progetto** in Android Studio
2. **Collega un dispositivo Android fisico** via USB
3. **Run** ▶️
4. **Concedi permesso microfono**
5. **Fai rumore** per volare! 🎤

---

## 📋 Modifiche Recenti

### ✅ **COMPLETATO**

#### 1. **Gap tubi aumentato** (Pipe.java)
```java
this.gap = 550;  // Era 400 → Ora 550 (molto più facile!)
```

#### 2. **Scritta "Microfono non disponibile" rimossa** (GameView.java)
- Ora mostra sempre "Fai rumore per volare!"
- Interfaccia più pulita

#### 3. **README completo creato**
- Documentazione tecnica dettagliata
- Tutte le funzioni spiegate
- Guida personalizzazione

---

## 🎯 File Principali

```
app/src/main/java/com/example/flappyvoice/
├── MainActivity.java       → Gestione permessi e lifecycle
├── GameView.java          → Game loop, logica, rendering
├── VoiceDetector.java     → Rilevamento audio microfono
├── Bird.java              → Pappagallo (fisica + grafica)
├── Pipe.java              → Tubi ostacolo
└── Background.java        → Sfondo animato
```

---

## ⚙️ Configurazione Rapida

### Difficoltà
**Più Facile:**
- `Pipe.java` → `gap = 650`
- `Pipe.java` → `speed = 5`
- `Bird.java` → `GRAVITY = 0.6f`

**Più Difficile:**
- `Pipe.java` → `gap = 450`
- `Pipe.java` → `speed = 8`
- `Bird.java` → `GRAVITY = 1.0f`

### Sensibilità Microfono
`VoiceDetector.java` → `AMPLITUDE_THRESHOLD = 2000`
- Troppo sensibile? → `4000`
- Poco sensibile? → `1000`

---

## 🐛 Risoluzione Problemi

| Problema | Soluzione |
|----------|-----------|
| Microfono non funziona | Usa dispositivo fisico, non emulatore |
| App crasha | Build → Clean + Rebuild |
| Troppo difficile | Aumenta `gap` in Pipe.java |
| Troppo facile | Riduci `gap` in Pipe.java |

---

## 📚 Documentazione Completa

Leggi [README.md](../README.md) nella root del progetto per:
- Documentazione tecnica completa
- Spiegazione di ogni funzione
- Guide personalizzazione avanzate

---

**Versione:** 1.0  
**Data:** Dicembre 2024  
**Piattaforma:** Android 10+

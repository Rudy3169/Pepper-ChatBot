# 🤖 Pepper ChatBot

Benvenuti nel progetto **Pepper ChatBot**, un'applicazione Android avanzata sviluppata per il robot **Pepper**.
<br>L'app trasforma Pepper in un compagno di conversazione intelligente, integrando capacità di dialogo basate su Large Language Models (LLM) per offrire un'esperienza di interazione naturale e coinvolgente.

## Panoramica

Questo progetto è stato sviluppato nell'ambito di un tirocinio presso l'Università degli Studi di Torino (UNITO).
<br>L'obiettivo principale è fornire a Pepper un'interfaccia di dialogo moderna e reattiva, permettendogli di interagire con gli utenti tramite voce e interfaccia touch, supportato da un'intelligenza artificiale remota.

## Funzionalità Principali

- **Dialogo Intelligente**: Integrazione con un gateway LLM esterno per risposte contestuali, naturali e fluide.
- **Multimodalità**: Supporto completo per input vocale (Speech-to-Text) e testuale tramite un'interfaccia chat ottimizzata per il tablet del robot.
- **Feedback Robotico**: Gestione dinamica dei feedback del robot (LED oculari e del petto, sintesi vocale) per segnalare le fasi di ascolto e di elaborazione.
- **Multi-lingua**: Supporto integrato per Italiano e Inglese, con gestione dinamica delle traduzioni e dei temi.
- **Interfaccia Moderna**: Sviluppata interamente con **Jetpack Compose** per garantire una UI fluida, accessibile e accattivante.

## 🎨 Design dell'Interfaccia (Figma)

Il design dell'interfaccia è stato progettato per essere intuitivo e accessibile sul tablet di Pepper. Di seguito sono riportate le anteprime delle schermate principali e i relativi link al progetto Figma.

### Schermate Principali

**Welcome Screen (Light)**
[![Anteprima Welcome Screen Light](figma/Welcome%20Screen%20Light%20Italian.svg)](https://www.figma.com/design/zOiNIT1PvIKalsW73Otczu/Pepper-Chatbot?node-id=52-641&t=XyMCIWsTUsqaTWbV-4)

**Welcome Screen (Dark)**
[![Anteprima Welcome Screen Dark](figma/Welcome%20Screen%20Dark%20Italian.svg)](https://www.figma.com/design/zOiNIT1PvIKalsW73Otczu/Pepper-Chatbot?node-id=52-652&t=XyMCIWsTUsqaTWbV-4)

**Chat Screen (Light)**
[![Anteprima Chat Screen Light](figma/Chat%20Screen%20Light%20Italian.png)](https://www.figma.com/design/zOiNIT1PvIKalsW73Otczu/Pepper-Chatbot?node-id=5-10&t=XyMCIWsTUsqaTWbV-4)

**Chat Screen (Dark)**
[![Anteprima Chat Screen Dark](figma/Chat%20Screen%20Dark%20Italian.svg)](https://www.figma.com/design/zOiNIT1PvIKalsW73Otczu/Pepper-Chatbot?node-id=34-43&t=XyMCIWsTUsqaTWbV-4)

## 🛠️ Stack Tecnologico

- **Linguaggio**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architettura**: MVVM (Model-View-ViewModel)
- **Networking**: Ktor Client (per la comunicazione con l'LLM Gateway e i servizi di controllo del robot)
- **Robot SDK**: Aldebaran QiSDK
- **Gestione Stato**: LiveData e Coroutine per una gestione asincrona delle risposte.

## 📁 Struttura del Progetto

```text
app/src/main/java/it/diunito/pepper/
├── data/              # Strato dati: client Ktor, modelli API e servizi di comunicazione.
├── ui/                # Componenti UI, navigazione e logica di presentazione.
│   ├── components/    # Elementi UI riutilizzabili (pulsanti, bolle di chat, overlay).
│   ├── screens/       # Schermate dell'app (Splash, Menu, Chat).
│   ├── navigation/    # Gestione del grafo di navigazione e permessi.
│   └── viewmodel/     # Logica di business e gestione dello stato della conversazione.
└── MainActivity.kt    # Entry point dell'app e gestione del ciclo di vita del robot.
```

## ⚙️ Configurazione e Setup

### Prerequisiti
- **Android Studio** (versione Koala o successiva).
- **Pepper Robot** reale con QiSDK o emulatore Pepper configurato correttamente.
- Accesso a un **Gateway API** attivo per l'elaborazione del linguaggio naturale.

### Variabili di Ambiente
L'applicazione utilizza `BuildConfig` per la configurazione degli endpoint. Assicurati di definire i seguenti parametri nel tuo file `local.properties`:

- `GATEWAY_API_HOST`: L'indirizzo del server LLM (es. `http://192.168.x.x:5000`).
- `HEAD_API_HOST`: L'indirizzo per il controllo diretto delle funzionalità hardware (se applicabile).

## 👥 Contributi

Sviluppato presso il Dipartimento di Informatica dell'Università di Torino.

---
*Progetto realizzato per scopi accademici e di ricerca.*

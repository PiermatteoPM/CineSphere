# Prog_ISPW - Gestore di Collezione Musicali

## Descrizione del Progetto
Il progetto "Gestore di Collezione Musicali" è un'applicazione web che consente agli utenti di esplorare, condividere e scoprire nuove collezione musicali. L'autenticazione degli utenti offre funzionalità personalizzate, mentre la modalità Guest permette una visione limitata. L'app include una gestione completa delle collezione, filtri avanzati, moderazione amministrativa e un ruolo speciale per i supervisori.
L'app fornisce un'esperienza completa di gestione delle collezione, con funzionalità avanzate e una struttura flessibile per la persistenza dei dati.

## Caratteristiche Principali

1. **Autenticazione Utente**
   - Registrazione e accesso utente.
   - Accesso come Guest per visione limitata.

2. **Gestione delle Collezione**
   - Visualizzazione, ricerca ed esportazione di collezione esistenti.
   - Aggiunta di nuove collezione al database.

3. **Filtri e Consigli Personalizzati**
   - Filtraggio per genere musicale, tonalità emotiva e nome della collezione.

4. **Gestione del Profilo Utente**
   - Modifica delle informazioni personali e preferenze utente.

5. **Moderazione Amministrativa**
   - Approvazione delle nuove collezione attraverso il sistema di moderazione.

6. **Supervisore**
   - Utente speciale con privilegi:
      - Caricamento immediato di collezione senza approvazione.
      - Approvazione o rifiuto delle collezione degli utenti.
      - Notifiche automatiche all'autore delle decisioni sulla sua collezione.

7. **Guest**
   - Accesso limitato alla home page solo per visualizzare le collezione.

## Persistenza
L'applicazione implementa due tipi di persistenza: JSON e MySQL, offrendo flessibilità nella gestione dei dati.

## Tecnologie Coinvolte
- **Versione SDK 21.0.1**
- **Versione IntelliJ IDEA 2023.3.1**
- **Backend**: Java per la logica del server.
- **Frontend**: JavaFX per l'interfaccia utente.
- **Database**: MySQL per memorizzare informazioni su collezione e utenti.
- **Persistenza**: Implementata tramite JSON e MySQL.
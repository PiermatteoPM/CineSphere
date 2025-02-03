# Prog_ISPW - Gestore di Collezioni di Film e Serie TV

## Descrizione del Progetto
Il progetto "Gestore di Collezioni di Film e Serie TV" è un'applicazione web che consente agli utenti di esplorare, condividere e scoprire nuove collezioni. L'autenticazione degli utenti offre funzionalità personalizzate, mentre la modalità Guest permette una visione limitata. L'app include una gestione completa delle collezioni, filtri avanzati, moderazione amministrativa e un ruolo speciale per i supervisori.
L'app fornisce un'esperienza completa di gestione delle collezioni, con funzionalità avanzate e una struttura flessibile per la persistenza dei dati.

## Caratteristiche Principali

1. **Autenticazione Utente**
   - Registrazione e accesso utente.
   - Accesso come Guest per visione limitata.

2. **Gestione delle Collezioni**
   - Visualizzazione, ricerca ed esportazione di collezioni esistenti.
   - Aggiunta di nuove collezioni al database.

3. **Filtri e Consigli Personalizzati**
   - Filtraggio per genere e nome della collezione.

4. **Gestione del Profilo Utente**
   - Modifica delle informazioni personali e preferenze utente.

5. **Moderazione Amministrativa**
   - Approvazione delle nuove collezioni attraverso il sistema di moderazione.

6. **Supervisore**
   - Utente speciale con privilegi:
      - Caricamento immediato di collezioni senza approvazione.
      - Approvazione o rifiuto delle collezioni degli utenti.
      - Notifiche automatiche all'autore della decisione presa sulla sua collezione.

7. **Guest**
   - Accesso limitato alla home page solo per visualizzare le collezioni.

## Persistenza
L'applicazione implementa due tipi di persistenza: JSON e MySQL, offrendo flessibilità nella gestione dei dati. Inoltre è stata creata una versione Demo che non utilizza nessun tipo di persistenza.

## Tecnologie Coinvolte
- **Versione SDK 23.0.1**
- **Versione IntelliJ IDEA 2024.2.2**
- **Backend**: Java per la logica del server.
- **Frontend**: JavaFX per l'interfaccia utente.
- **Database**: MySQL per memorizzare informazioni su collezioni e utenti.
- **Persistenza**: Implementata tramite JSON, MYSQL, DEMO.

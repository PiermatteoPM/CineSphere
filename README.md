# Prog_ISPW - Gestore di Collezioni di Film e Serie TV

## Descrizione del Progetto
Il progetto "Gestore di Collection di Film e Serie TV" è un'applicazione web che consente agli utenti di esplorare, condividere e scoprire nuove collection. L'autenticazione degli utenti offre funzionalità personalizzate, mentre la modalità Guest permette una visione limitata. L'app include una gestione completa delle collection, filtri avanzati, moderazione amministrativa e un ruolo speciale per i supervisori.
L'app fornisce un'esperienza completa di gestione delle collection, con funzionalità avanzate e una struttura flessibile per la persistenza dei dati.

## Caratteristiche Principali

1. **Autenticazione Utente**
   - Registrazione e accesso utente.
   - Accesso come Guest per visione limitata.

2. **Gestione delle Collection**u
   - Visualizzazione, ricerca ed esportazione di collection esistenti.
   - Aggiunta di nuove collection al database.

3. **Filtri e Consigli Personalizzati**
   - Filtraggio per genere e nome della collection.

4. **Gestione del Profilo Utente**
   - Modifica delle informazioni personali e preferenze utente.

5. **Moderazione Amministrativa**
   - Approvazione delle nuove collection attraverso il sistema di moderazione.

6. **Supervisore**
   - Utente speciale con privilegi:
      - Caricamento immediato di collection senza approvazione.
      - Approvazione o rifiuto delle collection degli utenti.
      - Notifiche automatiche all'autore delle decisioni sulla sua collection.

7. **Guest**
   - Accesso limitato alla home page solo per visualizzare le collection.

## Persistenza
L'applicazione implementa due tipi di persistenza: JSON e MySQL, offrendo flessibilità nella gestione dei dati. Inoltre è stata creata na versione Demo che non utilizza nessun tipo di persistenza.

## Tecnologie Coinvolte
- **Versione SDK 21.0.1**
- **Versione IntelliJ IDEA 2023.3.1**
- **Backend**: Java per la logica del server.
- **Frontend**: JavaFX per l'interfaccia utente.
- **Database**: MySQL per memorizzare informazioni su collection e utenti.
- **Persistenza**: Implementata tramite JSON e MySQL.

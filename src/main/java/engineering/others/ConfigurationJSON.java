package engineering.others;

/**Questa classe è utile per centralizzare i percorsi dei file JSON, evitando di spargere stringhe hardcoded nel codice.
 Se un percorso cambia, basta modificarlo qui e tutto il progetto si aggiorna automaticamente.*/

public class ConfigurationJSON {
    private ConfigurationJSON(){}
    public static final String PERSISTENCE_BASE_DIRECTORY = "src/main/resources/persistence";  //Directory principale dei dati di persistenza.
    public static final String USER_BASE_DIRECTORY = "src/main/resources/persistence/users"; //Directory che contiene i file degli utenti registrati.
    public static final String PENDING_COLLECTIONSS_BASE_DIRECTORY = "src/main/resources/persistence/pendingCollectionss";//Directory delle collezioni in attesa di approvazione.
    public static final String APPROVED_COLLECTION_BASE_DIRECTORY = "src/main/resources/persistence/approvedCollectionss";//Directory delle collezioni approvate.
    public static final String USER_INFO_FILE_NAME = "userInfo.json";//Nome del file JSON che contiene le informazioni sugli utenti.
    public static final String NOTICE_FILE_NAME = "notice.json"; //Nome del file JSON che contiene le notifiche.
    public static final String FILE_EXTENCTION = ".json";//Estensione predefinita dei file JSON.
}

/** Evita duplicazione del codice:
 Se hai bisogno di accedere a userInfo.json, non devi scrivere il percorso ogni volta.

  Facilita modifiche future:
 Se cambi la posizione dei file, basta aggiornare un'unica classe.

 Migliora leggibilità e organizzazione:
 Tutti i percorsi relativi alla persistenza sono centralizzati in un'unica classe.*/
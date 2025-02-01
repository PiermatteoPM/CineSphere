package engineering.pattern.abstract_factory;

import engineering.dao.*;
import engineering.others.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Questa classe implementa il pattern Abstract Factory, un pattern di
 *  progettazione utilizzato per creare famiglie di oggetti correlati
 *  senza specificare le loro classi concrete.*/

/**In questo caso, DAOFactory è una classe astratta che fornisce un'interfaccia comune
 * per la creazione di oggetti DAO (Data Access Object), ossia classi che gestiscono l'accesso ai dati.
 * La factory seleziona dinamicamente il tipo di persistenza leggendo un file
 * di configurazione config.properties, che specifica se utilizzare MySQL, JSON o un database demo.*/

/**DAOFactory è astratta perché non può essere istanziata direttamente.*/
/** Serve solo come modello per le sottoclassi (MySQLDAOFactory, JsonDAOFactory, DemoDAOFactory)*/
/**Le sottoclassi implementano i metodi per creare le istanze specifiche di DAO*/
public abstract class DAOFactory {
    /**
     * me è un'istanza statica della factory, utilizzata per garantire che esista una sola istanza (pattern Singleton).
     */
    private static DAOFactory me = null;

    /**
     * Il costruttore è protected per impedire l'istanza diretta della classe da parte di classi esterne,
     * solo le sottoclassi possono chiamarlo
     */
    protected DAOFactory() {
    }

    /** Recupera dal file config.properties il tipo di persistenza utilizzata,
     se non è possibile come default viene utilizzato MYSQL */
    /**
     * synchronized: evita problemi di concorrenza quando più thread accedono alla factory contemporaneamente,
     * anche qua ce static, in modo tale da garantire solo un istanzazione di essa
     */

    public static synchronized DAOFactory getDAOFactory() {
        if (me == null) {
            Properties properties = new Properties(); /**tipo oggetto che si trova nella libreria di util*/

            /**Carica un file di configurazione config.properties che contiene informazioni sul tipo
             * di persistenza da usare,
             * se il file non è accessibile, stampa un errore usando Printer.errorPrint(e.getMessage());*/
            try (InputStream input = DAOFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
                properties.load(input);
            } catch (IOException e) {
                Printer.errorPrint(e.getMessage());
            }
            /**Legge dal file di configurazione il valore della chiave "persistence.type",
             * se il valore non è presente, usa "MYSQL" come valore predefinito*/
            String persistenceType = properties.getProperty("persistence.type", "MYSQL");
            var anEnum = Enum.valueOf(persistenceType); /**Usa Enum.valueOf(persistenceType) per convertire la stringa in un valore dell'enumerazione, che poi ti serve negli il sotto*/
            /**Impostazione della dersistenza scelta, creando l'istanza specifica di quest'ultima*/
            if (anEnum == Enum.MYSQL) {
                me = new MySQLDAOFactory();
            } else if (anEnum == Enum.JSON) {
                me = new JsonDAOFactory();
            } else if (anEnum == Enum.DEMO) {
                me = new DemoDAOFactory();
            }
        }
        return me; /**ritorna la persistenza scelta che viene salvata in me, sopra nell'attributo classe, così che le
         chiamate successive avranno la stessa istanza, o meglio tipo dell'istanza scelta dalla persistenza*/
    }
    /** Questi sono metodi astratti, devono essere implementati
     * nelle sottoclassi (MySQLDAOFactory, JsonDAOFactory, DemoDAOFactory)*/

    /**
     * Definizione perfetta di abstract e teoria aggiunta:
     * La keyword abstract permette di definire metodi senza implementarli, obbligando le
     * sottoclassi a fornirne una concreta implementazione. Questo favorisce
     * il polimorfismo e separa l'interfaccia dall'implementazione,
     * garantendo flessibilità ed estensibilità nel codice.
     */

    public abstract ClientDAO createClientDAO();

    public abstract CollectionDAO createCollectionDAO();

    public abstract NoticeDAO createNoticeDAO();
}
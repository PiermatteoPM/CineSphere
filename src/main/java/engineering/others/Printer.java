package engineering.others;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**La classe Printer è una utility class usata per la gestione dei log e della stampa dei messaggi nella console*/

//per log4j 2, ci ha costretto sonar
/**Log4j 2 è una libreria avanzata per la gestione dei log.
 ➜ Il Logger viene creato con LogManager.getLogger(Printer.class) e viene usato nei metodi print e println.
 ➜ Vantaggi di Log4j 2:
 -Migliore gestione dei log rispetto a System.out.println
 -Supporta diversi livelli di log (info, error, debug, ecc.)
 -I log possono essere salvati in file, database o inviati a un server.*/

public class Printer {

    // Crea un'istanza di Logger tramite Log4j 2
    private static final Logger logger = LogManager.getLogger(Printer.class);

    /**Impedisce l'istanziazione della classe perché Printer deve essere usata solo con metodi static.*/
    private Printer() {}

    /** Stampa in bianco e non va a capo */
    public static void print(String message) {
        // Usa Log4j 2 per stampare il messaggio
        logger.info(message);
    }

    /** Stampa in bianco e va a capo */
    public static void println(String message){
        print(String.format("%s%n", message));
    }

    /** Stampa in rosso e va a capo */
    public static void errorPrint(String message){
        // Sequenza di escape ANSI per il colore rosso
        String redColorCode = "\u001B[31m";

        // Sequenza di escape ANSI per ripristinare il colore predefinito
        String resetColorCode = "\u001B[0m";

        // Stampa il testo in rosso per gli errori
        message = redColorCode + message + resetColorCode;
        println(message);
    }

    /** Stampa in blu e va a capo */
    public static void logPrint(String message){
        // Sequenza di escape ANSI per il colore blu
        String blueColorCode = "\u001B[34m";

        // Sequenza di escape ANSI per ripristinare il colore predefinito
        String resetColorCode = "\u001B[0m";

        // Stampa il testo in blu per i log
        message = blueColorCode + message + resetColorCode;
        println(message);
    }
}

/**
  Migliora la gestione della stampa evitando l'uso diretto di System.out.println().
  Distingue messaggi normali, errori e log con colori diversi.
  Facilita la futura gestione dei log → Se un domani vuoi scrivere i log su file invece che in console, basta modificare Log4j.
  Rende il codice più leggibile e strutturato.*/
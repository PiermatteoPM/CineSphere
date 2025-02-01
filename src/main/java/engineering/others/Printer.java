package engineering.others;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Printer {

    // Crea un'istanza di Logger tramite Log4j 2
    private static final Logger logger = LogManager.getLogger(Printer.class);

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

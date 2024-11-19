package engineering.others;

public class Printer {

    private Printer(){}

    /** Stampa in bianco e non va a capo */
    public static void print(String message){
        System.out.print(message);
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

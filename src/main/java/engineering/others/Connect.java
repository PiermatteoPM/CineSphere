package engineering.others;

import java.io.*;
import java.sql.*;
import java.util.*;

/**La classe Connect gestisce la connessione al database usando JDBC e il design pattern Singleton.
 Legge le credenziali dal file connection.properties e crea un'unica connessione condivisa
 tra le varie parti del programma.*/

public class Connect {

    private String jdbc;
    private String user;
    private String password;
    private static Connect instance = null; //connessione unica per tutto il programma una volta scelta la persistenza
    private Connection conn = null;//connessione al database
    private static final String PATH = "src/main/resources/connection.properties"; //dove viene preso il tipo della persistenza scelta

    /**Impedisce la creazione di nuove istanze della classe.
     Garantisce che Connect possa essere usata solo tramite getInstance()*/
    private Connect() {}

    /** Singleton.
     * Se l'istanza non esiste, la crea.
     * Se esiste già, ritorna la stessa istanza.
     * Il modificatore synchronized evita problemi in caso di accesso concorrente (thread safety) */
    public static synchronized Connect getInstance() {
        if (instance == null) {
            instance = new Connect();
        }
        return instance;
    }

    /**Se la connessione non esiste, chiama getInfo() per leggere le credenziali e poi la crea con DriverManager.getConnection().
     Se esiste già, la riutilizza.
     Vantaggio: Mantiene una connessione unica e ottimizza l'uso delle risorse.
     Usa Printer.errorPrint() per stampare errori in caso di problemi con la connessione.*/
    public synchronized Connection getDBConnection() {
        if (this.conn == null) {
            getInfo();

            try{
                this.conn = DriverManager.getConnection(jdbc, user, password);
            } catch (SQLException e){
                Printer.errorPrint(String.format("Error in Connect.java %s", e.getMessage()));
            }

        }
        return this.conn;
    }
    /**Legge il file connection.properties per recuperare JDBC URL, user e password.
     Usa Properties per caricare le informazioni in modo dinamico.
     Se il file non viene trovato, stampa un errore.*/
    private void getInfo() {
        try(FileInputStream fileInputStream = new FileInputStream(PATH)) {

            // Load DB Connection info from Properties file
            Properties prop = new Properties() ;
            prop.load(fileInputStream);
            /** sono variabili che sono in connection.propertis*/
            jdbc = prop.getProperty("JDBC_URL") ;
            user = prop.getProperty("USER") ;
            password = prop.getProperty("PASSWORD") ;

        } catch (IOException e){
            Printer.errorPrint(e.getMessage());
        }
    }

}
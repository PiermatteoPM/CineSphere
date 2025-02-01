package engineering.pattern.observer;


/** Definisce l'interfaccia per tutti i subscribers per ricevere le notifiche dai publisher
 * Verrà usata come classe astratta implementata dai ConcreteObserver che realizzeranno update */

/**Questa interfaccia fa parte del Pattern Observer, un pattern comportamentale che
 * permette a un oggetto (Observer) di ricevere notifiche da un altro
 * oggetto (Subject o Publisher) quando il suo stato cambia.*/

/**Come funziona il Pattern Observer?
 Observer (Osservatore) → Interfaccia con il metodo update(), che viene chiamato quando il Publisher cambia stato.
 ConcreteObserver (Osservatore concreto) → Implementa Observer e definisce come reagire alle notifiche.
 Subject (Soggetto / Publisher) → Ha una lista di Observer e li notifica quando cambia stato.*/

/**Il Pattern Observer nel
 *  progetto viene usato per mantenere sincronizzati i dati tra il backend e l'interfaccia utente.*/

/**Utilità nel tuo progetto
 Aggiornare in tempo reale l'interfaccia grafica (UI):
 Ad esempio, la HomePageControllerGrafico viene notificata automaticamente quando una nuova collezione viene approvata.

 Disaccoppiare i componenti:
 CollectionCollectionss non sa quali classi la stanno osservando, migliorando la manutenibilità del codice.

 Implementare notifiche per gli utenti:
 Quando una collezione viene approvata, tutti gli utenti registrati ricevono una notifica senza dover controllare manualmente il database.
 * */

public interface Observer {
    void update();
}

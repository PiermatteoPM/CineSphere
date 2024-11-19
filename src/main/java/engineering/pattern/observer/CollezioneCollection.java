package engineering.pattern.observer;

import model.Collezione;

import java.util.*;

/** È l'argomento da osservare (il publisher, il ConcreteSubject)
 * se viene modificata la CollezioneCollection tramite i metodi addCollezione o removeCollezione, vengono successivamente
 * informati tutti gli observers(subscribers) utilizzando il metodo notifyObservers.

 * Questo model rappresenta la lista di Collezione approvate.

 * Viene utilizzato per aggiornare le istanze di HomePageControllerGrafico a ogni nuova aggiunta di una collezione.
 * */
public class CollezioneCollection extends Subject {
    private static CollezioneCollection collezioneCollection = null;

    /** Stato del subject */
    private List<Collezione> allColleziones = new ArrayList<>();

    /** Singleton poiché tutti gli utenti hanno la stessa vista dello strato di persistenza */
    public static CollezioneCollection getInstance() { //Pattern Singleton
        if (collezioneCollection == null) {
            collezioneCollection = new CollezioneCollection();
        }
        return collezioneCollection;
    }

    /** Costruttore privato affinché nessuno possa crearne una nuova istanza */
    private CollezioneCollection(){

    }

    /** Metodo setState()
     * Utilizzata da AddCollezioneCtrlGrafico se il supervisor carica una collezione direttamente
     * Utilizzata da PendingCollezioneCtrlGrafico se il supervisor accetta una collezione di un utente
     * */
    public void addCollezione(Collezione collezione) {
        allColleziones.add(collezione);
        notifyObservers();
    }

    /** Non implementato l'eliminazione di una collezione accettata */
    public void removeCollezione(Collezione collezione) {
        allColleziones.remove(collezione);
        notifyObservers();
    }

    /**
     * Metodo getState() in teoria utilizzato dai subscribers una volta notificati di dover svolgere un update() ?
     *
     * @return tutte le collezione
     */
    public List<Collezione> getState(){
        return allColleziones;
    }
}


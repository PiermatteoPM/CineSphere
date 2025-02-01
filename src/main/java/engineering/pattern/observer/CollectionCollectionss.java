package engineering.pattern.observer;

import model.Collection;

import java.util.*;

/** È l'argomento da osservare (il publisher, il ConcreteSubject)
 * se viene modificata la CollectionCollectionss tramite i metodi addCollection o removeCollection, vengono successivamente
 * informati tutti gli observers(subscribers) utilizzando il metodo notifyObservers.

 * Questo model rappresenta la lista di Collection approvate.

 * Viene utilizzato per aggiornare le istanze di HomePageControllerGrafico a ogni nuova aggiunta di una collection.
 * */
public class CollectionCollectionss extends Subject {
    private static CollectionCollectionss collectionCollection = null;

    /** Stato del subject */
    private List<Collection> allCollectionss = new ArrayList<>();

    /** Singleton poiché tutti gli utenti hanno la stessa vista dello strato di persistenza */
    public static CollectionCollectionss getInstance() { //Pattern Singleton
        if (collectionCollection == null) {
            collectionCollection = new CollectionCollectionss();
        }
        return collectionCollection;
    }

    /** Costruttore privato affinché nessuno possa crearne una nuova istanza */
    private CollectionCollectionss(){

    }

    /** Metodo setState()
     * Utilizzata da AddCollectionCtrlGrafico se il supervisor carica una collection direttamente
     * Utilizzata da PendingCollectionCtrlGrafico se il supervisor accetta una collection di un utente
     * */
    public void addCollection(Collection collection) {
        allCollectionss.add(collection);
        notifyObservers();
    }

    /** Non implementato l'eliminazione di una collection accettata */
    public void removeCollection(Collection collection) {
        allCollectionss.remove(collection);
        notifyObservers();
    }

    /**
     * Metodo getState() in teoria utilizzato dai subscribers una volta notificati di dover svolgere un update() ?
     *
     * @return tutte le collection
     */
    public List<Collection> getState(){
        return allCollectionss;
    }
}


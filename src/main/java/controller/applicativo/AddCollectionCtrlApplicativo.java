package controller.applicativo;

import engineering.bean.*;
import engineering.dao.*;
import engineering.pattern.observer.CollectionCollectionss;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.exceptions.*;

import model.Collection;

/**Funzionamento dettagliato
 Recupera il DAO corretto

 Ottiene un'istanza di CollectionDAO tramite DAOFactory (pattern Abstract Factory).
 Questo garantisce l'uso della giusta implementazione del DAO, a seconda della configurazione del sistema.
 Crea un oggetto Collection (Model)

 Usa i dati contenuti in CollectionBean per creare una nuova istanza di Collection.
 L'ID non viene impostato manualmente perché sarà gestito dal database.
 Tenta di inserire la collezione nel database

 Chiama il metodo insertCollection() del DAO per salvare la collezione.
 Applica il Pattern Observer per aggiornare la home page

 Se la collezione è approvata (collection.getApproved()), viene notificata all'Observer (CollectionCollectionss) per aggiornare la home.
 Gestisce le eccezioni personalizzate

 Se il link della collezione è già in uso, lancia CollectionLinkAlreadyInUseException.
 Se il nome della collezione è già in uso, lancia CollectionNameAlreadyInUseException.*/

public class AddCollectionCtrlApplicativo {

    public void insertCollection(CollectionBean cb) throws CollectionLinkAlreadyInUseException, CollectionNameAlreadyInUseException {

        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();         // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        // Crea la Collection (model), id verrà impostato dal dao
        Collection collection = new Collection(cb.getEmail(), cb.getUsername(), cb.getCollectionName(), cb.getLink(), cb.getCollectionGenre(), cb.getApproved());


        try{    // Invio Collection model al DAO
            dao.insertCollection(collection);

            /* Per pattern Observer !!! */
            if(collection.getApproved()){ // La notifica all observer solo se la collection è approvata -> Se è caricata da un supervisore
                CollectionCollectionss.getInstance().addCollection(collection);
            }

        } catch (CollectionLinkAlreadyInUseException e){
            throw new CollectionLinkAlreadyInUseException();
        } catch (CollectionNameAlreadyInUseException e) {
            throw new CollectionNameAlreadyInUseException();
        }
    }

}

/**Pattern di progettazione utilizzati
 Abstract Factory (DAOFactory):
 Permette di ottenere dinamicamente l'istanza corretta del DAO in base alla configurazione del sistema.

 Observer Pattern (CollectionCollectionss):
 Se la collezione è approvata, viene aggiunta automaticamente all'elenco osservato, aggiornando la home page.*/

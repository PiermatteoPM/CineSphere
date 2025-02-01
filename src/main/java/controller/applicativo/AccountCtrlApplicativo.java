package controller.applicativo;

import engineering.bean.*;
import engineering.dao.*;
import engineering.exceptions.*;
import engineering.others.Printer;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.pattern.observer.CollectionCollectionss;
import model.*;
import model.Collection;
//come funziona il MVC
/**L'utente invia una richiesta → Il Controller la riceve.
 Il Controller chiama il Model per ottenere i dati.
 Il Model interroga il DAO, che recupera i dati dal database.
 Il DAO restituisce i dati sotto forma di Bean.
 Il Model passa i dati al Controller.
 Il Controller passa i dati alla View.
 La View visualizza i dati all’utente.*/



import java.util.*;
/**Questa classe fornisce tre funzionalità principali:

 Recuperare tutte le collezioni di un utente (retrieveCollectionss)
 Aggiornare i generi preferiti dell'utente (updateGenreUser)
 Eliminare una collezione (deleteCollection)*/
public class AccountCtrlApplicativo {

    /**
     * Recupera tutte le collection globali by username
     */
    /**1. Metodo retrieveCollectionss(ClientBean clientBean)
     Scopo
     Recupera tutte le collezioni globali associate a un utente, basandosi sulla sua email.

     Funzionamento
     Ottiene l'istanza del DAO CollectionDAO tramite DAOFactory.
     Recupera la lista delle collezioni dall'email dell'utente.
     Converte ogni oggetto Collection in CollectionBean (Bean di trasferimento dati).
     Restituisce la lista di CollectionBean.*/
    public List<CollectionBean> retrieveCollectionss(ClientBean clientBean) {
        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();         // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)
        String email = clientBean.getEmail();

        // Recupero lista Collection
        List<Collection> collectionss = dao.retrieveCollectionssByEmail(email);

        ArrayList<CollectionBean> collectionssBean = new ArrayList<>();
        try {
            for (Collection p : collectionss){
                CollectionBean cb = new CollectionBean(p.getEmail(),p.getUsername(),p.getCollectionName(),p.getLink(),p.getCollectionGenre(),p.getApproved());
                cb.setId(p.getId());
                collectionssBean.add(cb);
            }
        } catch (LinkIsNotValidException e){
            // Non la valuto perché è un retrieve da persistenza, dove è stata caricata correttamente
            handleDAOException(e);
        }

        return collectionssBean;
    }

    /**2. Metodo updateGenreUser(ClientBean clientBean)
     Scopo
     Aggiorna le preferenze di genere dell'utente nel database.

     Funzionamento
     Ottiene l'istanza del DAO ClientDAO tramite DAOFactory.
     Crea un oggetto di tipo Client, distinguendo tra Supervisor e User (pattern eredità).
     Invia l'oggetto Client al DAO per aggiornare le preferenze nel database.*/

    /** Utilizzata per aggiornare i generi preferiti dell'utente in caso in cui prema il bottone Salva */
    public void updateGenreUser(ClientBean clientBean){
        ClientDAO dao = DAOFactory.getDAOFactory().createClientDAO();         // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        Client client;
        if(clientBean.isSupervisor()){
            client = new Supervisor(clientBean.getUsername(),clientBean.getEmail(),clientBean.getPreferences());
        } else {
            client = new User(clientBean.getUsername(),clientBean.getEmail(),clientBean.getPreferences());
        }

        // Invio utente model al DAO
        dao.updateGenreClient(client);
    }

    /** Metodo deleteCollection(CollectionBean cb)
     Scopo
     Permette all'autore di eliminare una collezione.
     Nota: questa funzionalità non è implementata nel front-end, ma solo nel back-end.

     Funzionamento
     Ottiene l'istanza di CollectionDAO tramite DAOFactory.
     Converte il CollectionBean in un oggetto Collection.
     Chiama il metodo deleteCollection() per rimuovere la collezione dal database.
     Se la collezione era approvata, viene anche rimossa dalla home tramite Observer Pattern.*/


    /** Utilizzata per permettere all'autore di eliminare le collection
     * Non è stato implementata l'eliminazione nel front-end, ma si nel back-end */
    public Boolean deleteCollection(CollectionBean cb){

        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO(); // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        Collection collection = new Collection(cb.getEmail(), cb.getUsername(), cb.getCollectionName(), cb.getLink(), cb.getCollectionGenre(), cb.getApproved());
        collection.setId(cb.getId());

        dao.deleteCollection(collection);

        if(collection.getApproved()) {
            /* OBSERVER -> REMOVE PER FAR AGGIORNARE LA HOME PAGE */
            CollectionCollectionss.getInstance().removeCollection(collection);
        }
        return true;
    }

    private void handleDAOException(Exception e) {
        Printer.logPrint(e.getMessage());
    }
}
/**Pattern Utilizzati
 Abstract Factory (DAOFactory)

 Permette di creare i DAO giusti in base alla configurazione del sistema.
 Observer Pattern (CollectionCollectionss)

 Se una collezione viene eliminata, viene aggiornata la home page rimuovendo la collezione anche dalla vista.
 Bean (DTO - Data Transfer Object)

 Usa ClientBean e CollectionBean per il trasferimento dei dati tra i livelli.
 */
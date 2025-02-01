package controller.applicativo;

import engineering.bean.*;
import engineering.dao.*;
import engineering.exceptions.*;
import engineering.others.Printer;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.pattern.observer.CollectionCollectionss;
import model.*;
import model.Collection;

import java.util.*;

public class AccountCtrlApplicativo {

    /**
     * Recupera tutte le collection globali by username
     */
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

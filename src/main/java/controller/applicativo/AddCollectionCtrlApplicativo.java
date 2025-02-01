package controller.applicativo;

import engineering.bean.*;
import engineering.dao.*;
import engineering.pattern.observer.CollectionCollectionss;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.exceptions.*;

import model.Collection;

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

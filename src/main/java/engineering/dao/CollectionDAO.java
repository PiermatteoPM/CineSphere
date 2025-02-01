package engineering.dao;

import engineering.exceptions.CollectionLinkAlreadyInUseException;
import engineering.exceptions.CollectionNameAlreadyInUseException;
import model.Collection;

import java.util.List;

public interface CollectionDAO {

    /** Inserisce una collection in persistenza*/
    void insertCollection(Collection collection) throws CollectionLinkAlreadyInUseException, CollectionNameAlreadyInUseException;

    /** Non serve che crea una nuova istanza di Collection*/
    Collection approveCollection(Collection collection);

    /** Elimina la collection */
    void deleteCollection(Collection collection);


    /** Recupera tutte le collection dell'utente dall'email */
    List<Collection> retrieveCollectionssByEmail(String email);
    List<Collection> retrievePendingCollectionss();
    List<Collection> retrieveApprovedCollectionss();


    /** Recupera tutte le collection, filtrandole per genere e titolo */
    List<Collection> searchCollectionByTitle(Collection collection);

    List<Collection> searchCollectionByGenre(Collection collection);

}


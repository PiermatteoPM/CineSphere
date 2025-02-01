package engineering.dao;

import engineering.exceptions.CollectionLinkAlreadyInUseException;
import engineering.exceptions.CollectionNameAlreadyInUseException;
import model.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionDAODemo implements CollectionDAO {

    private static List<Collection> collections = new ArrayList<>();
    //se voglio aggiungerle già
    /**static {
     collections.add(new Collection("user1", "... ,"user1@example.com", true));
     collections.add(new Collection("user2", "... ,"user2@example.com", false));
     collections.add(new Collection("user3", "... ,"user3@example.com", true));
     }*/

    @Override
    public void insertCollection(Collection collection) throws CollectionLinkAlreadyInUseException, CollectionNameAlreadyInUseException {
        if (collections.stream().anyMatch(c -> c.getLink().equals(collection.getLink()))) {
            throw new CollectionLinkAlreadyInUseException();
        }
        if (collections.stream().anyMatch(c -> c.getUsername().equals(collection.getUsername()) && c.getCollectionName().equals(collection.getCollectionName()))) {
            throw new CollectionNameAlreadyInUseException();
        }
        collections.add(collection);
    }
    /**Cerca la collezione con il link corrispondente.
     Se trovata, imposta approved = true e la restituisce.
     Se non trovata, restituisce null.*/
    @Override
    public Collection approveCollection(Collection collection) {
        for (Collection c : collections) {
            if (c.getLink().equals(collection.getLink())) {
                c.setApproved(true);
                return c;
            }
        }
        return null;
    }
    //Rimuove la collezione con il link corrispondente dalla lista.
    @Override
    public void deleteCollection(Collection collection) {
        collections.removeIf(c -> c.getLink().equals(collection.getLink()));
    }

    @Override
    public List<Collection> retrieveCollectionssByEmail(String email) {
        return collections.stream()
                .filter(c -> c.getEmail().equals(email))
                .toList();
    }

    @Override
    public List<Collection> retrievePendingCollectionss() {
        return collections.stream()
                .filter(c -> !c.getApproved())
                .toList();
    }

    @Override
    public List<Collection> retrieveApprovedCollectionss() {
        return collections.stream()
                .filter(Collection::getApproved)
                .toList();
    }


    @Override
    public List<Collection> searchCollectionByTitle(Collection collection) {
        return collections.stream()
                .filter(c -> c.getCollectionName().equalsIgnoreCase(collection.getCollectionName()))
                .toList();
    }

    @Override
    public List<Collection> searchCollectionByGenre(Collection collection) {
        return collections.stream()
                .filter(c -> c.getCollectionGenre().containsAll(collection.getCollectionGenre()))
                .toList();
    }
}

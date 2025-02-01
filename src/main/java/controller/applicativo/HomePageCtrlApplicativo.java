package controller.applicativo;

import engineering.bean.NoticeBean;
import engineering.bean.CollectionBean;
import engineering.dao.*;
import engineering.exceptions.LinkIsNotValidException;
import engineering.others.Printer;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.pattern.observer.Observer;
import engineering.pattern.observer.CollectionCollectionss;
import engineering.pattern.observer.Subject;
import model.Notice;
import model.Collection;

import java.util.*;

public class HomePageCtrlApplicativo {

    public List<CollectionBean> retriveCollectionssApproved() {

        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();        // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)
        List<Collection> collectionss = dao.retrieveApprovedCollectionss();              // Recupero lista Collection approvate

        return getCollectionssBean(collectionss);
    }

    public List<CollectionBean> searchCollectionByFilters(CollectionBean collectionBean) {

        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();  // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        Collection collection = new Collection();                             // Creo la entity da passare al DAO

        /* Popolo la collection da cercare con solo le informazioni di cui l'utente è interessato */
        collection.setCollectionName(collectionBean.getCollectionName());
        collection.setCollectionGenre(collectionBean.getCollectionGenre());

        List<Collection> collectionss;

        if (genreEmpty(collection.getCollectionGenre())) {
            collectionss = dao.searchCollectionByTitle(collection);  // Filtra solo per titolo
        } else {
            collectionss = dao.searchCollectionByGenre(collection);  // Filtra per titolo e generi
        }


        return getCollectionssBean(collectionss);
    }

    /** Nel caso in cui non volessimo che la view contattasse il model per fare attach */
    public void observeCollectionTable(Observer observer){
        Subject collectionCollection = CollectionCollectionss.getInstance();
        collectionCollection.attach(observer);
    }

    public List<CollectionBean> getCollectionssBean(List<Collection> collectionss){
        List<CollectionBean> collectionssBean = new ArrayList<>();           // Creo una lista di collectionBean da restituire al Grafico

        try {
            for (Collection p : collectionss){
                CollectionBean cb = new CollectionBean(p.getEmail(),p.getUsername(),p.getCollectionName(),p.getLink(),p.getCollectionGenre(),p.getApproved());
                cb.setId(p.getId());
                collectionssBean.add(cb);
            }
        } catch (LinkIsNotValidException e){
            // Non la valuto perché è un retrieve da persistenza, dove è stata caricata correttamente
            Printer.logPrint(String.format("HomePage APP: LinkIsNotValid %s", e.getMessage()));
        }
        return collectionssBean;
    }

    public void deleteSelectedCollection(CollectionBean collectionBean) {
        Collection collection = new Collection(collectionBean.getEmail(), collectionBean.getUsername(), collectionBean.getCollectionName(), collectionBean.getLink(), collectionBean.getCollectionGenre(), collectionBean.getApproved());

        if (collection.getApproved()){
            CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();
            dao.deleteCollection(collection);

            /* OBSERVER -> REMOVE PER FAR AGGIORNARE LA HOME PAGE */
            CollectionCollectionss.getInstance().removeCollection(collection);
        }
    }

    public void removeNotice(NoticeBean noticeBean) {
        NoticeDAO dao = DAOFactory.getDAOFactory().createNoticeDAO();   // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)
        Notice notice = new Notice(noticeBean.getTitle(), noticeBean.getBody(), noticeBean.getEmail());
        dao.deleteNotice(notice);
    }

    /** Utilizzata per un corretto filtraggio */
    private boolean genreEmpty(List<String> genre){
        return genre == null || genre.isEmpty();
    }


}

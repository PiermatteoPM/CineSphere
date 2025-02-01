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

public class PendingCollectionCtrlApplicativo {

    public void approveCollection(CollectionBean cb){
        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();

        Collection collection = new Collection(cb.getEmail(), cb.getUsername(), cb.getCollectionName(), cb.getLink(), cb.getCollectionGenre(), cb.getApproved());
        collection.setId(cb.getId());

        // Istanza di collection ha ancora il parametro approved a false
        Collection collectionApproved = dao.approveCollection(collection);
        cb.setApproved(collectionApproved.getApproved());

        /* OBSERVER -> ADD PER FAR AGGIORNARE LA HOME PAGE */
        CollectionCollectionss collectionCollection = CollectionCollectionss.getInstance();
        collectionCollection.addCollection(collection);
    }

    /** Recupera tutte le collection globali, sia approvate che non */
    public List<CollectionBean> retrieveCollectionss(){

        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();

        // Recupero lista Collection
        List<Collection> collectionss = dao.retrievePendingCollectionss();
        List<CollectionBean> collectionssBean = new ArrayList<>();

        try{
            for (Collection p : collectionss){
                CollectionBean cb = new CollectionBean(p.getEmail(),p.getUsername(),p.getCollectionName(),p.getLink(),p.getCollectionGenre(),p.getApproved());
                cb.setId(p.getId());

                collectionssBean.add(cb);
            }
        } catch (LinkIsNotValidException e){
            Printer.logPrint(e.getMessage());
        }

        return collectionssBean;
    }

    public void rejectCollection(CollectionBean cb) {
        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();

        Collection collection = new Collection(cb.getEmail(), cb.getUsername(), cb.getCollectionName(), cb.getLink(), cb.getCollectionGenre(), cb.getApproved());
        collection.setId(cb.getId());

        dao.deleteCollection(collection);
    }

    public void sendNotification(NoticeBean noticeBean) {

        NoticeDAO dao = DAOFactory.getDAOFactory().createNoticeDAO();
        Notice notice = new Notice(noticeBean.getTitle(),noticeBean.getBody(),noticeBean.getEmail());

        dao.addNotice(notice);
    }
}
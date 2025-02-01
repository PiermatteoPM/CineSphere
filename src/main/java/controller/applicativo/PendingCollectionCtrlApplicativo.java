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

/**Analisi della classe PendingCollectionCtrlApplicativo
 Questa classe è un controller applicativo che gestisce le collezioni in attesa di approvazione (pending collections).
 Si occupa di:

 Approvare una collezione.
 Recuperare la lista delle collezioni in attesa.
 Rifiutare una collezione.
 Inviare notifiche agli utenti.

 Segue l'architettura MVC (Model-View-Controller) e utilizza i design pattern Abstract Factory e Observer.*/

public class PendingCollectionCtrlApplicativo {

    /**1. approveCollection(CollectionBean cb)
     Scopo:
     Approva una collezione e la aggiunge alla home page.

     Funzionamento:
     Ottiene un'istanza di CollectionDAO tramite DAOFactory.
     Crea un oggetto Collection (Model) con i dati della CollectionBean ricevuta.
     Chiama approveCollection(collection) nel DAO, che imposta il flag approved = true.
     Aggiorna il bean (cb.setApproved(true)) per riflettere il cambiamento.
     Utilizza il pattern Observer per aggiornare la home page, notificando CollectionCollectionss dell'approvazione.*/

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

    /**2. retrieveCollectionss()
     Scopo:
     Recupera tutte le collezioni in attesa di approvazione dal database.

     Funzionamento:
     Ottiene un'istanza di CollectionDAO tramite DAOFactory.
     Recupera la lista di collezioni pending con dao.retrievePendingCollectionss().
     Converte gli oggetti Collection in CollectionBean per la UI.
     Gestisce eventuali errori (LinkIsNotValidException) usando Printer.logPrint().
     Restituisce la lista di CollectionBean alla view.*/

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
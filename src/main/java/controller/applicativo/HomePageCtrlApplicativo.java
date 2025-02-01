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

/**Analisi della classe HomePageCtrlApplicativo
 Questa classe è un controller applicativo che gestisce le operazioni relative alla home page dell'applicazione.
 Si occupa principalmente di:

 Recuperare le collezioni approvate.
 Filtrare le collezioni in base a titolo e genere.
 Gestire la rimozione delle collezioni e degli avvisi.
 Gestire l'osservazione delle collezioni tramite il pattern Observer.
 Segue l'architettura MVC (Model-View-Controller) e utilizza diversi design pattern, tra cui Abstract Factory e Observer Pattern.*/

public class HomePageCtrlApplicativo {

    /**1. retriveCollectionssApproved()
     Scopo
     Recupera tutte le collezioni che sono state approvate e devono essere visualizzate nella home page.

     Funzionamento:
     Ottiene un'istanza del CollectionDAO tramite DAOFactory.
     Recupera la lista delle collezioni approvate dal database (retrieveApprovedCollectionss()).
     Converte la lista di Collection in una lista di CollectionBean (DTO) utilizzando il metodo getCollectionssBean().
     Restituisce la lista di CollectionBean da mostrare nella home.*/

    public List<CollectionBean> retriveCollectionssApproved() {

        CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();        // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)
        List<Collection> collectionss = dao.retrieveApprovedCollectionss();              // Recupero lista Collection approvate

        return getCollectionssBean(collectionss);
    }

    /**2. searchCollectionByFilters(CollectionBean collectionBean)
     Scopo
     Permette agli utenti di cercare collezioni filtrando per titolo e/o genere.

     Funzionamento
     Ottiene un'istanza del CollectionDAO tramite DAOFactory.
     Crea un oggetto Collection e imposta solo i campi rilevanti per la ricerca.
     Controlla se il genere è vuoto:
     Se sì, filtra solo per titolo (searchCollectionByTitle()).
     Se no, filtra per titolo e genere (searchCollectionByGenre()).

     Converte i risultati in CollectionBean e li restituisce.
     Nota
     Il metodo ausiliario genreEmpty() verifica se la lista di generi è nulla o vuota.*/

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

    /**3. observeCollectionTable(Observer observer)
     Scopo:
     Permette a un Observer (ad esempio, la GUI) di osservare i cambiamenti nelle collezioni.

     Funzionamento:
     Recupera l'istanza del Subject (CollectionCollectionss), che rappresenta l'elenco delle collezioni.
     Registra l'Observer chiamando attach(observer), in modo che venga notificato quando le collezioni cambiano.

     Pattern utilizzato:
     Observer Pattern: La GUI può essere aggiornata automaticamente quando le collezioni vengono modificate.*/

    /** Nel caso in cui non volessimo che la view contattasse il model per fare attach */
    public void observeCollectionTable(Observer observer){
        Subject collectionCollection = CollectionCollectionss.getInstance();
        collectionCollection.attach(observer);
    }

    /**4. getCollectionssBean(List<Collection> collectionss)
     Scopo:
     Converte una lista di Collection (Model) in una lista di CollectionBean (DTO), che verrà restituita alla view.

     Funzionamento:
     Crea una nuova lista di CollectionBean.
     Itera su ogni Collection e crea un CollectionBean con gli stessi dati.
     Se si verifica l'eccezione LinkIsNotValidException, l'errore viene ignorato perché i dati provengono dal database.*/
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
    /**5. deleteSelectedCollection(CollectionBean collectionBean)
     Scopo:
     Permette agli utenti di eliminare una collezione approvata.

     Funzionamento:
     Converte il CollectionBean in un oggetto Collection.
     Se la collezione è approvata, procede con l'eliminazione:
     Ottiene l'istanza del CollectionDAO.
     Chiama deleteCollection() per rimuoverla dal database.
     Rimuove la collezione dall'Observer (CollectionCollectionss), aggiornando la home.

     Pattern utilizzato:
     Observer Pattern: Dopo la rimozione, la home viene aggiornata automaticamente.*/
    public void deleteSelectedCollection(CollectionBean collectionBean) {
        Collection collection = new Collection(collectionBean.getEmail(), collectionBean.getUsername(), collectionBean.getCollectionName(), collectionBean.getLink(), collectionBean.getCollectionGenre(), collectionBean.getApproved());

        if (collection.getApproved()){
            CollectionDAO dao = DAOFactory.getDAOFactory().createCollectionDAO();
            dao.deleteCollection(collection);

            /* OBSERVER -> REMOVE PER FAR AGGIORNARE LA HOME PAGE */
            CollectionCollectionss.getInstance().removeCollection(collection);
        }
    }

    /**6. removeNotice(NoticeBean noticeBean)
     Scopo:
     Elimina un avviso dalla home page.

     Funzionamento:
     Ottiene l'istanza di NoticeDAO tramite DAOFactory.
     Converte il NoticeBean in un oggetto Notice.
     Chiama deleteNotice() per rimuovere l'avviso dal database.*/

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

/**Pattern di progettazione utilizzati
 Abstract Factory (DAOFactory)

 Consente di ottenere l'istanza corretta dei DAO senza dipendere direttamente dall'implementazione concreta.
 Observer Pattern (CollectionCollectionss)

 Permette alla home di aggiornarsi automaticamente quando una collezione viene aggiunta o rimossa.*/
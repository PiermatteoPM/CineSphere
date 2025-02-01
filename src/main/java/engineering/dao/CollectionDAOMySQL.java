package engineering.dao;

import engineering.exceptions.*;
import engineering.others.*;
import engineering.query.*;

import model.Collection;
import view.first.utils.GenreManager;

import java.sql.*;
import java.util.*;

public class CollectionDAOMySQL implements CollectionDAO {

    /* Stringhe dei campi nel DB my SQL */
    private static final String USERNAME = "username";
    private static final String LINK = "link";
    private static final String EMAIL = "email";
    private static final String NAME_COLLECTION = "nameCollection";
    private static final String APPROVED = "approved";
    private static final String PENDING = "pending";
    private static final String SEARCH_WORD = "searchWord";
    private static final String SEARCH_FILTER = "searchFilter";
    private static final String SEARCH_GENRE = "searchGenre";


    /** Inserimento di una collection in db. Viene prima controllato che non ci sia già il link all'interno del DB, successivamente inserisce */
    public void insertCollection(Collection collection) throws CollectionLinkAlreadyInUseException, CollectionNameAlreadyInUseException {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            /* controllo se il link è univoco nel sistema */
            String collectionLink = collection.getLink();
            rs = QueryCollection.searchCollectionLink(stmt, collectionLink);

            if (rs.next()) {
                throw new CollectionLinkAlreadyInUseException();
            }

            /* Controllo se il titolo è univoco per l'utente */
            rs = QueryCollection.searchCollectionTitleByUsername(stmt, collection.getCollectionName(), collection.getUsername());

            if (rs.next()) {
                throw new CollectionNameAlreadyInUseException();
            }
            rs.close();

            QueryCollection.insertCollection(stmt, collection);

        } catch (SQLException e) {
            handleDAOException(e);
        } finally {
            closeResources(stmt,rs);
        }
    }

    /**
     * @param collection, utilizzato per il link per poter fare la retrive
     * @return collection modificata, non serve creare una nuova istanza.
     */
    public Collection approveCollection(Collection collection) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            String collectionLink = collection.getLink();
            rs = QueryCollection.searchCollectionLink(stmt, collectionLink); // Cerca in all_collection

            if (rs.next()) {
                QueryCollection.approveCollectionByLink(stmt, collection.getLink()); // Rimuove in collection_utente E in all_collection
                collection.setApproved(true); // Non mi piace, non è responsabilità sua
                rs.close();
            }

        } catch (SQLException e){
            handleDAOException(e);
        } finally {
            closeResources(stmt,rs);
        }
        return collection;
    }

    public void deleteCollection(Collection collection) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            String collectionLink = collection.getLink();
            rs = QueryCollection.searchCollectionLink(stmt, collectionLink); // Cerca in all_collection

            if (rs.next()) {
                QueryCollection.removeCollectionByLink(stmt, collection.getLink()); // Rimuove in collection_utente E in all_collection
                rs.close();
            }

        } catch (SQLException e){
            handleDAOException(e);
        } finally {
            closeResources(stmt,rs);
        }
    }


    public List<Collection> retrieveCollectionssByEmail(String email) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        List<Collection> collectionss = null;

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            rs = QueryCollection.retrieveCollectionClientByEmail(stmt,email);

            collectionss = new ArrayList<>(); // Una lista di collection

            List<String> genres; // Una lista per i generi

            while (rs.next()) {
                Collection collection = new Collection();
                fillModelFromResultSet(collection,rs);
                genres = GenreManager.retriveGenre(rs);
                collection.setCollectionGenre(genres);
                collectionss.add(collection);
            }
            rs.close();
        }
        catch (SQLException e){
            handleDAOException(e);
        }
        finally {
            closeResources(stmt,rs);
        }
        return collectionss;
    }

    public List<Collection> retrievePendingCollectionss() {
        return retrieveCollectionss(PENDING,null);
    }
    public List<Collection> retrieveApprovedCollectionss() {
        return retrieveCollectionss(APPROVED,null);
    }
    public List<Collection> searchCollectionByTitle(Collection collection) {
        return retrieveCollectionss(SEARCH_WORD,collection);
    }
    public List<Collection> searchCollectionByGenre(Collection collection) {
        return retrieveCollectionss(SEARCH_GENRE,collection);
    }
    public List<Collection> searchCollectionByFilters(Collection collectionSearch) {
        return retrieveCollectionss(SEARCH_FILTER,collectionSearch);
    }

    private List<Collection> retrieveCollectionss(String s,Collection p){
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        List<Collection> collectionss = new ArrayList<>(); // Initialize the list here

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            switch (s){
                case PENDING:  //Recupera tutte le collection da approvare (Per il gestore delle collection)
                    rs = QueryCollection.retrievePendingCollectionss(stmt);
                    break;
                case APPROVED: //Recupera tutte le collection già approvare (Per la home page)
                    rs = QueryCollection.retrieveApprovedCollectionss(stmt);
                    break;
                case SEARCH_WORD: //Recupera tutte le collection con il titolo
                    if(p != null){
                        rs = QueryCollection.searchCollectionTitle(stmt,p.getCollectionName());
                    }
                    break;
                case SEARCH_FILTER:
                    if(p != null){
                        rs = QueryCollection.searchCollectionssByFilter(stmt,p);
                    }
                    break;
                case SEARCH_GENRE:
                    if(p != null){
                        rs = QueryCollection.searchCollectionssByGenre(stmt,p);
                    }
                    break;
                default:
                    break;
            }

            while (true) {
                assert rs != null;
                if (!rs.next()) break;

                Collection collection = new Collection();
                fillModelFromResultSet(collection,rs); // Imposta i campi username, email, link, nome Collection e approved
                collectionss.add(collection);
            }

        } catch (SQLException e) {
            handleDAOException(e);
        } finally {
            closeResources(stmt,rs);
        }
        return collectionss;
    }

    private void closeResources(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            handleDAOException(e);
        }
    }

    private void handleDAOException(Exception e) {
        Printer.errorPrint(String.format("CollectionDAOMySQL: %s", e.getMessage()));

    }

    /** Imposta i campi username, email, link, nome Collection, approved, Generi */
    private void fillModelFromResultSet(Collection collection, ResultSet rs) throws SQLException {

        collection.setUsername(rs.getString(USERNAME));
        collection.setEmail(rs.getString(EMAIL));
        collection.setCollectionName(rs.getString(NAME_COLLECTION));
        collection.setLink(rs.getString(LINK));
        collection.setApproved(rs.getBoolean(APPROVED));

        List<String> genres = GenreManager.retriveGenre(rs);
        collection.setCollectionGenre(genres);
        
    }

}
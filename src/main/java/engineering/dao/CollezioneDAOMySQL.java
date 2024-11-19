package engineering.dao;

import engineering.exceptions.*;
import engineering.others.*;
import engineering.query.*;

import model.Collezione;
import view.first.utils.GenreManager;

import java.sql.*;
import java.util.*;

public class CollezioneDAOMySQL implements CollezioneDAO {

    /* Stringhe dei campi nel DB my SQL */
    private static final String USERNAME = "username";
    private static final String LINK = "link";
    private static final String EMAIL = "email";
    private static final String NAME_COLLEZIONE = "nameCollezione";
    private static final String APPROVED = "approved";
    private static final String PENDING = "pending";
    private static final String SEARCH_WORD = "searchWord";
    private static final String SEARCH_FILTER = "searchFilter";
    private static final String SEARCH_GENRE = "searchGenre";


    /** Inserimento di una collezione in db. Viene prima controllato che non ci sia già il link all'interno del DB, successivamente inserisce */
    public void insertCollezione(Collezione collezione) throws CollezioneLinkAlreadyInUseException, CollezioneNameAlreadyInUseException {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            /* controllo se il link è univoco nel sistema */
            String collezioneLink = collezione.getLink();
            rs = QueryCollezione.searchCollezioneLink(stmt, collezioneLink);

            if (rs.next()) {
                throw new CollezioneLinkAlreadyInUseException();
            }

            /* Controllo se il titolo è univoco per l'utente */
            rs = QueryCollezione.searchCollezioneTitleByUsername(stmt, collezione.getCollezioneName(), collezione.getUsername());

            if (rs.next()) {
                throw new CollezioneNameAlreadyInUseException();
            }
            rs.close();

            QueryCollezione.insertCollezione(stmt, collezione);

        } catch (SQLException e) {
            handleDAOException(e);
        } finally {
            closeResources(stmt,rs);
        }
    }

    /**
     * @param collezione, utilizzato per il link per poter fare la retrive
     * @return collezione modificata, non serve creare una nuova istanza.
     */
    public Collezione approveCollezione(Collezione collezione) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            String collezioneLink = collezione.getLink();
            rs = QueryCollezione.searchCollezioneLink(stmt, collezioneLink); // Cerca in all_collezione

            if (rs.next()) {
                QueryCollezione.approveCollezioneByLink(stmt, collezione.getLink()); // Rimuove in collezione_utente E in all_collezione
                collezione.setApproved(true); // Non mi piace, non è responsabilità sua
                rs.close();
            }

        } catch (SQLException e){
            handleDAOException(e);
        } finally {
            closeResources(stmt,rs);
        }
        return collezione;
    }

    public void deleteCollezione(Collezione collezione) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            String collezioneLink = collezione.getLink();
            rs = QueryCollezione.searchCollezioneLink(stmt, collezioneLink); // Cerca in all_collezione

            if (rs.next()) {
                QueryCollezione.removeCollezioneByLink(stmt, collezione.getLink()); // Rimuove in collezione_utente E in all_collezione
                rs.close();
            }

        } catch (SQLException e){
            handleDAOException(e);
        } finally {
            closeResources(stmt,rs);
        }
    }


    public List<Collezione> retrieveCollezionesByEmail(String email) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        List<Collezione> colleziones = null;

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            rs = QueryCollezione.retrieveCollezioneClientByEmail(stmt,email);

            colleziones = new ArrayList<>(); // Una lista di collezione

            List<String> genres; // Una lista per i generi musicali

            while (rs.next()) {
                Collezione collezione = new Collezione();
                fillModelFromResultSet(collezione,rs);
                genres = GenreManager.retriveGenre(rs);
                collezione.setCollezioneGenre(genres);
                colleziones.add(collezione);
            }
            rs.close();
        }
        catch (SQLException e){
            handleDAOException(e);
        }
        finally {
            closeResources(stmt,rs);
        }
        return colleziones;
    }

    public List<Collezione> retrievePendingColleziones() {
        return retrieveColleziones(PENDING,null);
    }
    public List<Collezione> retrieveApprovedColleziones() {
        return retrieveColleziones(APPROVED,null);
    }
    public List<Collezione> searchCollezioneByTitle(Collezione collezione) {
        return retrieveColleziones(SEARCH_WORD,collezione);
    }
    public List<Collezione> searchCollezioneByGenre(Collezione collezione) {
        return retrieveColleziones(SEARCH_GENRE,collezione);
    }
    public List<Collezione> searchCollezioneByFilters(Collezione collezioneSearch) {
        return retrieveColleziones(SEARCH_FILTER,collezioneSearch);
    }

    private List<Collezione> retrieveColleziones(String s,Collezione p){
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn;

        List<Collezione> colleziones = new ArrayList<>(); // Initialize the list here

        try {
            conn = Connect.getInstance().getDBConnection();
            stmt = conn.createStatement();

            switch (s){
                case PENDING:  //Recupera tutte le collezione da approvare (Per il gestore delle collezione)
                    rs = QueryCollezione.retrievePendingColleziones(stmt);
                    break;
                case APPROVED: //Recupera tutte le collezione già approvare (Per la home page)
                    rs = QueryCollezione.retrieveApprovedColleziones(stmt);
                    break;
                case SEARCH_WORD: //Recupera tutte le collezione con il titolo
                    if(p != null){
                        rs = QueryCollezione.searchCollezioneTitle(stmt,p.getCollezioneName());
                    }
                    break;
                case SEARCH_FILTER:
                    if(p != null){
                        rs = QueryCollezione.searchCollezionesByFilter(stmt,p);
                    }
                    break;
                case SEARCH_GENRE:
                    if(p != null){
                        rs = QueryCollezione.searchCollezionesByGenre(stmt,p);
                    }
                    break;
                default:
                    break;
            }

            while (true) {
                assert rs != null;
                if (!rs.next()) break;

                Collezione collezione = new Collezione();
                fillModelFromResultSet(collezione,rs); // Imposta i campi username, email, link, nome Collezione e approved
                colleziones.add(collezione);
            }

        } catch (SQLException e) {
            handleDAOException(e);
        } finally {
            closeResources(stmt,rs);
        }
        return colleziones;
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
        Printer.errorPrint(String.format("CollezioneDAOMySQL: %s", e.getMessage()));

    }

    /** Imposta i campi username, email, link, nome Collezione, approved, Generi musicali */
    private void fillModelFromResultSet(Collezione collezione, ResultSet rs) throws SQLException {

        collezione.setUsername(rs.getString(USERNAME));
        collezione.setEmail(rs.getString(EMAIL));
        collezione.setCollezioneName(rs.getString(NAME_COLLEZIONE));
        collezione.setLink(rs.getString(LINK));
        collezione.setApproved(rs.getBoolean(APPROVED));

        List<String> genres = GenreManager.retriveGenre(rs);
        collezione.setCollezioneGenre(genres);
        
    }

}
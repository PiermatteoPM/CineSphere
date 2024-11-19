package engineering.query;

import engineering.others.Printer;
import model.Collezione;

import java.sql.*;
import java.util.List;

public class QueryCollezione {

    private QueryCollezione(){}

    /** Carica la collezione sul database. */
    public static void insertCollezione(Statement stmt, Collezione collezione) throws SQLException {

        String email = collezione.getEmail();
        String username = collezione.getUsername();
        String link = collezione.getLink();
        String nameCollezione = collezione.getCollezioneName();
        List<String> collezioneGenre = collezione.getCollezioneGenre();

        int approved;
        if(collezione.getApproved()){
            approved = 1;
        } else {
            approved = 0;
        }

        try{
            // inserimento nella tabella dell'utente
            String insertCollezioneStatement = String.format(Queries.INSERT_COLLEZIONE_USER, nameCollezione, email, username, link, approved, buildGenresQueryString(collezioneGenre));
            stmt.executeUpdate(insertCollezioneStatement);
        } catch (SQLException e){
            handleException(e);
        }
    }


    /** Utilizzata al momento dell'inserimento per controllare se un link è già all'interno del db
     * SELECT * FROM all_collezione WHERE link = '%s' */
    public static ResultSet searchCollezioneLink(Statement stmt, String link) throws SQLException {
        String sql = String.format(Queries.SELECT_LINK_QUERY, link);
        return stmt.executeQuery(sql);
    }

    /** Cerca la parola
     * @param searchTerm passata come argomento */
    public static ResultSet searchCollezioneTitle(Statement stmt, String searchTerm) throws SQLException {
        String word = "%" + searchTerm + "%";
        String sql = String.format(Queries.SELECT_SEARCH_COLLEZIONE, word);
        return stmt.executeQuery(sql);
    }
    public static ResultSet searchCollezioneTitleByUsername(Statement stmt, String title, String username) throws SQLException {
        String sql = String.format(Queries.SELECT_TITLE_COLLEZIONE_BY_USERNAME, title, username);
        return stmt.executeQuery(sql);
    }

    /** Ritorna una lista di collezione che combaciano con i generi musicali selezionati */
    public static ResultSet searchCollezionesByFilter(Statement stmt, Collezione collezione) throws SQLException {

        String genre = buildGenresQueryString(collezione.getCollezioneGenre());
        genre = genre.replace(" ", "").replace(",", "");


        String sql = String.format(Queries.SELECT_SEARCH_COLLEZIONES_BY_FILTER,
                "%" + collezione.getCollezioneName() + "%",
                (genre.charAt(0) == '1') ? 1 : 0, // Azione
                (genre.charAt(1) == '1') ? 1 : 0, // Avventura
                (genre.charAt(2) == '1') ? 1 : 0, // Animazione
                (genre.charAt(3) == '1') ? 1 : 0, // Biografico
                (genre.charAt(4) == '1') ? 1 : 0, // Commedia
                (genre.charAt(5) == '1') ? 1 : 0, // Poliziesco
                (genre.charAt(6) == '1') ? 1 : 0, // Documentario
                (genre.charAt(7) == '1') ? 1 : 0, // Drammatico
                (genre.charAt(8) == '1') ? 1 : 0, // PerFamiglie
                (genre.charAt(9) == '1') ? 1 : 0, // Fantastico
                (genre.charAt(10) == '1') ? 1 : 0, // Noir
                (genre.charAt(11) == '1') ? 1 : 0, // GiocoAPremiTelevisivo
                (genre.charAt(12) == '1') ? 1 : 0, // Storico
                (genre.charAt(13) == '1') ? 1 : 0, // Horror
                (genre.charAt(14) == '1') ? 1 : 0, // Musica
                (genre.charAt(15) == '1') ? 1 : 0, // Musical
                (genre.charAt(16) == '1') ? 1 : 0, // Giallo
                (genre.charAt(17) == '1') ? 1 : 0, // Telegiornale
                (genre.charAt(18) == '1') ? 1 : 0, // Reality
                (genre.charAt(19) == '1') ? 1 : 0, // Sentimentale
                (genre.charAt(20) == '1') ? 1 : 0, // Fantascienza
                (genre.charAt(21) == '1') ? 1 : 0, // Cortometraggio
                (genre.charAt(22) == '1') ? 1 : 0, // Sportivo
                (genre.charAt(23) == '1') ? 1 : 0, // TalkShow
                (genre.charAt(24) == '1') ? 1 : 0, // Thriller
                (genre.charAt(25) == '1') ? 1 : 0, // Guerra
                (genre.charAt(26) == '1') ? 1 : 0  // Western
        );
        return stmt.executeQuery(sql);
    }

    /** Recupera tutta la collezione_utente, va usata con retriveGenreCollezione per ottenere
     * i generi musicali delle collezione caricate */
    public static ResultSet retriveCollezioneClientByUsername(Statement stmt, String username) throws SQLException {
        String sql = String.format(Queries.SELECT_COLLEZIONE_BY_USER,username);
        return stmt.executeQuery(sql);
    }

    /** Recupera tutta la collezione_utente, va usata con retriveGenreCollezione per ottenere
     * i generi musicali delle collezione caricate */
    public static ResultSet retrieveCollezioneClientByEmail(Statement stmt, String email) throws SQLException {
        String sql = String.format(Queries.SELECT_COLLEZIONE_BY_EMAIL,email);
        return stmt.executeQuery(sql);
    }

    /** Recupera da GENERI, i generi musicali della collezione passata come id */
    public static ResultSet retrieveGenreCollezioneById(Statement stmt, int id) throws SQLException {
        String sql = String.format(Queries.SELECT_GENRE_USER_QUERY,id);
        return stmt.executeQuery(sql);
    }

    public static ResultSet retrieveGenreCollezione(Statement stmt, String username) throws SQLException {
        String sql = String.format(Queries.SELECT_GENRE_COLLEZIONE, username);
        return stmt.executeQuery(sql);
    }

    public static ResultSet retrieveGenreCollezioneByLink(Statement stmt, String link) throws SQLException {
        String sql = String.format(Queries.SELECT_GENRE_COLLEZIONE_BY_LINK, link);
        return stmt.executeQuery(sql);
    }

    /** Rimuove la collezione su tutte le tabelle, dal link della collezione
     * */
    public static void removeCollezioneByLink(Statement stmt, String link) throws SQLException {
        String sql = String.format(Queries.DELETE_COLLEZIONE_BY_LINK_COLLEZIONE_UTENTE,link);
        stmt.executeUpdate(sql);
    }

    public static void approveCollezioneByLink(Statement stmt, String link) throws SQLException {
        String sql = String.format(Queries.UPDATE_APPROVE_COLLEZIONE,1,link);
        stmt.executeUpdate(sql);
    }

    public static ResultSet retrievePendingColleziones(Statement stmt) throws SQLException {
        String sql = String.format(Queries.SELECT_PENDING_COLLEZIONES,0);
        return stmt.executeQuery(sql);
    }

    public static ResultSet retrieveApprovedColleziones(Statement stmt) throws SQLException {
        String sql = String.format(Queries.SELECT_APPROVED_COLLEZIONES,1);
        return stmt.executeQuery(sql);
    }

    public static ResultSet searchCollezionesByGenre(Statement stmt, Collezione p) throws SQLException {

        String genre = buildGenresQueryString(p.getCollezioneGenre());
        genre = genre.replace(" ", "").replace(",", "");

        String sql = String.format(Queries.SELECT_SEARCH_BY_GENRE,
                "%" + p.getCollezioneName() + "%",
                (genre.charAt(0) == '1') ? 1 : 0, // Azione
                (genre.charAt(1) == '1') ? 1 : 0, // Avventura
                (genre.charAt(2) == '1') ? 1 : 0, // Animazione
                (genre.charAt(3) == '1') ? 1 : 0, // Biografico
                (genre.charAt(4) == '1') ? 1 : 0, // Commedia
                (genre.charAt(5) == '1') ? 1 : 0, // Poliziesco
                (genre.charAt(6) == '1') ? 1 : 0, // Documentario
                (genre.charAt(7) == '1') ? 1 : 0, // Drammatico
                (genre.charAt(8) == '1') ? 1 : 0, // PerFamiglie
                (genre.charAt(9) == '1') ? 1 : 0, // Fantastico
                (genre.charAt(10) == '1') ? 1 : 0, // Noir
                (genre.charAt(11) == '1') ? 1 : 0, // GiocoAPremiTelevisivo
                (genre.charAt(12) == '1') ? 1 : 0, // Storico
                (genre.charAt(13) == '1') ? 1 : 0, // Horror
                (genre.charAt(14) == '1') ? 1 : 0, // Musica
                (genre.charAt(15) == '1') ? 1 : 0, // Musical
                (genre.charAt(16) == '1') ? 1 : 0, // Giallo
                (genre.charAt(17) == '1') ? 1 : 0, // Telegiornale
                (genre.charAt(18) == '1') ? 1 : 0, // Reality
                (genre.charAt(19) == '1') ? 1 : 0, // Sentimentale
                (genre.charAt(20) == '1') ? 1 : 0, // Fantascienza
                (genre.charAt(21) == '1') ? 1 : 0, // Cortometraggio
                (genre.charAt(22) == '1') ? 1 : 0, // Sportivo
                (genre.charAt(23) == '1') ? 1 : 0, // TalkShow
                (genre.charAt(24) == '1') ? 1 : 0, // Thriller
                (genre.charAt(25) == '1') ? 1 : 0, // Guerra
                (genre.charAt(26) == '1') ? 1 : 0  // Western
        );

        return stmt.executeQuery(sql);
    }

    /** Non dovrebbe servire */
    public static ResultSet retrieveIDbyEmail(Statement stmt, String email) throws SQLException {
        String sql = String.format(Queries.SELECT_ID_BY_EMAIL, email);
        return stmt.executeQuery(sql);
    }

    /**Genera una stringa corretta per effettuare la query, impostando correttamente il true o false dei generi
     * la crea per un insert (0, 1, ...) se bisogna usarla per i search attenzione a spazi e virgole */
    private static String buildGenresQueryString(List<String> generi) {
        String[] genres = {"Azione", "Avventura", "Animazione", "Biografico", "Commedia", "Poliziesco", "Documentario",
                "Drammatico", "PerFamiglie", "Fantastico", "Noir", "GiocoAPremiTelevisivo", "Storico", "Horror",
                "Musica", "Musical", "Giallo", "Telegiornale", "Reality", "Sentimentale", "Fantascienza",
                "Cortometraggio", "Sportivo", "TalkShow", "Thriller", "Guerra", "Western"};
        StringBuilder query = new StringBuilder();

        if(generi == null){
            query.append("0, ".repeat(genres.length));
        } else {
            for (String genere : genres) {
                query.append(generi.contains(genere) ? "1, " : "0, ");
            }
        }

        // Rimuovi l'ultima virgola
        if (!query.isEmpty()) {
            query.setLength(query.length() - 2);
        }

        return query.toString();
    }

    /** Solo SQLException */
    private static void handleException(Exception e) {
        Printer.errorPrint(e.getMessage());
    }


}

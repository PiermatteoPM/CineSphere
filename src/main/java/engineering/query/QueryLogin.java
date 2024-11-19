package engineering.query;

import engineering.others.Printer;
import model.Login;

import java.sql.*;
import java.util.List;

public class QueryLogin {

    private QueryLogin() {
    }

    /** Carica nel database un nuovo utente e i suoi generi musicali preferiti */
    public static void registerUser(Statement stmt, Login user) {

        String name = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        boolean supervisor = user.isSupervisor();

        int bool = 0;

        if(supervisor){
            bool = 1;
        }

        try {
            // Esegui prima l'inserimento nella tabella 'user'
            String insertUserStatement = String.format(Queries.INSERT_USER, email, name, password, bool);
            stmt.executeUpdate(insertUserStatement);

            // Poi inserisci i generi musicali nella tabella 'generi_musicali'
            insertGeneri(stmt, email, user.getPreferences());
        } catch (SQLException e){
            handleException(e);
        }

    }

    /** Inserisce i generi musicali preferiti dall'utente, utilizzata al momento della registrazione dell'utente */
    public static void insertGeneri(Statement stmt, String userEmail, List<String> generi) {
        try{
            StringBuilder query = new StringBuilder(String.format(Queries.INSERT_GENERI_USER, buildGenresQueryString(generi, userEmail)));
            stmt.executeUpdate(query.toString());
        } catch (SQLException e){
            handleException(e);
        }
    }

    /** Aggiorna i generi musicali preferiti dell'utente */
    public static void uploadGeneri(Statement stmt, String userEmail, List<String> generi) {
        try {
            // Costruisci la query di aggiornamento
            String query = String.format(Queries.UPDATE_GENERI_USER,
                    generi.contains("Azione") ? 1 : 0,
                    generi.contains("Avventura") ? 1 : 0,
                    generi.contains("Animazione") ? 1 : 0,
                    generi.contains("Biografico") ? 1 : 0,
                    generi.contains("Commedia") ? 1 : 0,
                    generi.contains("Poliziesco") ? 1 : 0,
                    generi.contains("Documentario") ? 1 : 0,
                    generi.contains("Drammatico") ? 1 : 0,
                    generi.contains("PerFamiglie") ? 1 : 0,
                    generi.contains("Fantastico") ? 1 : 0,
                    generi.contains("Noir") ? 1 : 0,
                    generi.contains("GiocoAPremiTelevisivo") ? 1 : 0,
                    generi.contains("Storico") ? 1 : 0,
                    generi.contains("Horror") ? 1 : 0,
                    generi.contains("Musica") ? 1 : 0,
                    generi.contains("Musical") ? 1 : 0,
                    generi.contains("Giallo") ? 1 : 0,
                    generi.contains("Telegiornale") ? 1 : 0,
                    generi.contains("Reality") ? 1 : 0,
                    generi.contains("Sentimentale") ? 1 : 0,
                    generi.contains("Fantascienza") ? 1 : 0,
                    generi.contains("Cortometraggio") ? 1 : 0,
                    generi.contains("Sportivo") ? 1 : 0,
                    generi.contains("TalkShow") ? 1 : 0,
                    generi.contains("Thriller") ? 1 : 0,
                    generi.contains("Guerra") ? 1 : 0,
                    generi.contains("Western") ? 1 : 0,
                    userEmail);

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            handleException(e);
        }
    }

    /** Ritorna un ResultSet contenente tutti i campi di client recuperati tramite la email */
    public static ResultSet loginUser(Statement stmt, String email) {
        try{
            String sql = String.format(Queries.SELECT_USER_BY_EMAIL, email);
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public static ResultSet loginUserByUsername(Statement stmt, String username){
        try{
            String sql = String.format(Queries.SELECT_USER_BY_USERNAME, username);
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    public static ResultSet retrivePrefByEmail(Statement stmt, String email) throws SQLException{
        String sql = String.format(Queries.SELECT_GENRE_USER_QUERY, email);
        return stmt.executeQuery(sql);
    }

    // Query per prendere la password della email passata come argomento
    public static ResultSet getUserPassword(Statement stmt, String email) throws SQLException {
        String query = String.format(Queries.SELECT_PASSWORD_BY_EMAIL, email);
        return stmt.executeQuery(query);
    }

    public static ResultSet searchEmail(Statement stmt, String email) throws SQLException {
        String query = String.format(Queries.SEARCH_EMAIL, email);
        return stmt.executeQuery(query);
    }

    public static ResultSet searchUsername(Statement stmt, String username) throws SQLException {
        String query = String.format(Queries.SEARCH_USERNAME, username);
        return stmt.executeQuery(query);
    }

    private static String buildGenresQueryString(List<String> generi, String userEmail) {
        String[] genres = {"Azione", "Avventura", "Animazione", "Biografico", "Commedia", "Poliziesco", "Documentario", "Drammatico", "PerFamiglie", "Fantastico", "Noir", "GiocoAPremiTelevisivo", "Storico", "Horror", "Musica", "Musical", "Giallo", "Telegiornale", "Reality", "Sentimentale", "Fantascienza", "Cortometraggio", "Sportivo", "TalkShow", "Thriller", "Guerra", "Western"};
        StringBuilder query = new StringBuilder();

        // Aggiungi i valori booleani alla query
        for (String genere : genres) {
            query.append(generi.contains(genere) ? "1, " : "0, ");
        }

        query.append(String.format("'%s'", userEmail));

        return query.toString();
    }

    /** Solo SQLException */
    private static void handleException(Exception e) {
        Printer.errorPrint(String.format("QueryLogin: %s", e.getMessage()));
    }

}

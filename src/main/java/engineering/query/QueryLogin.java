package engineering.query;

import engineering.others.Printer;
import model.Login;

import java.sql.*;
import java.util.List;

public class QueryLogin {

    private QueryLogin() {
    }

    /** Carica nel database un nuovo utente e i suoi generi preferiti */
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

            // Poi inserisci i generi nella tabella
            insertGeneri(stmt, email, user.getPreferences());
        } catch (SQLException e){
            handleException(e);
        }

    }

    /** Inserisce i generi preferiti dall'utente, utilizzata al momento della registrazione dell'utente */
    public static void insertGeneri(Statement stmt, String userEmail, List<String> generi) {
        try{
            StringBuilder query = new StringBuilder(String.format(Queries.INSERT_GENERI_USER, buildGenresQueryString(generi, userEmail)));
            stmt.executeUpdate(query.toString());
        } catch (SQLException e){
            handleException(e);
        }
    }

    /** Aggiorna i generi preferiti dell'utente */
    public static void uploadGeneri(Statement stmt, String userEmail, List<String> generi) {
        try {
            // Costruisci la query di aggiornamento in modo più sicuro
            StringBuilder queryBuilder = new StringBuilder("UPDATE generi_user SET ");
            String[] genres = {"Azione", "Avventura", "Animazione", "Biografico", "Commedia", "Poliziesco", "Documentario", "Drammatico", "PerFamiglie", "Fantastico", "Noir", "GiocoAPremiTelevisivo", "Storico", "Horror", "Musica", "Musical", "Giallo", "Telegiornale", "Reality", "Sentimentale", "Fantascienza", "Cortometraggio", "Sportivo", "TalkShow", "Thriller", "Guerra", "Western"};

            for (String genre : genres) {
                queryBuilder.append(genre).append(" = ").append(generi.contains(genre) ? 1 : 0).append(", ");
            }

            queryBuilder.setLength(queryBuilder.length() - 2); // Rimuove l'ultima virgola
            queryBuilder.append(" WHERE email = ?");

            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(queryBuilder.toString())) {
                pstmt.setString(1, userEmail);
                pstmt.executeUpdate();
            }
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

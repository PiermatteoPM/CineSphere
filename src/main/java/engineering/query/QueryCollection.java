package engineering.query;

import engineering.others.Printer;
import model.Collection;

import java.sql.*;
import java.util.List;

/**static mi indica che il metodo appartiene alla classe stessa e non ad una sua istanza, di esso esiste solo
 * un'implentazione ed è unica. Può essere richiamato più volte da tutti senza creare un istanza di
 * QueryCollection (se publico però)*/

public class QueryCollection {

    private QueryCollection(){}

    /**Questo metodo inserisce una collection nel database.
     Riceve un oggetto Statement per eseguire la query SQL.
     Recupera i dati della collection (email, username, link, nameCollection, collectionGenre).
     Converte approved in un valore 0 o 1 per il database.*/
    public static void insertCollection(Statement stmt, Collection collection) throws SQLException {

        String email = collection.getEmail();
        String username = collection.getUsername();
        String link = collection.getLink();
        String nameCollection = collection.getCollectionName();
        List<String> collectionGenre = collection.getCollectionGenre();

        int approved;
        if(collection.getApproved()){ //è un boolean approed , quindi lo converte in 0 o 1 per il database( si poteva anche far rimanere boolean credo)
            approved = 1;
        } else {
            approved = 0;
        }

        try{
            // inserimento nella tabella dell'utente
            String insertCollectionStatement = String.format(Queries.INSERT_COLLECTION_USER, nameCollection, email, username, link, approved, buildGenresQueryString(collectionGenre));
            stmt.executeUpdate(insertCollectionStatement);
        } catch (SQLException e){
            handleException(e);
        }
    }


    /** Utilizzata al momento dell'inserimento per controllare se un link è già all'interno del db
     * SELECT * FROM all_collection WHERE link = '%s' */
    public static ResultSet searchCollectionLink(Statement stmt, String link) throws SQLException {
        String sql = String.format(Queries.SELECT_LINK_QUERY, link);
        return stmt.executeQuery(sql);
    }

    /** Cerca la parola
     * @param searchTerm passata come argomento */
    public static ResultSet searchCollectionTitle(Statement stmt, String searchTerm) throws SQLException {
        String word = "%" + searchTerm + "%";
        String sql = String.format(Queries.SELECT_SEARCH_COLLECTION, word);
        return stmt.executeQuery(sql);
    }
    public static ResultSet searchCollectionTitleByUsername(Statement stmt, String title, String username) throws SQLException {
        String sql = String.format(Queries.SELECT_TITLE_COLLECTION_BY_USERNAME, title, username);
        return stmt.executeQuery(sql);
    }

    /** Ritorna una lista di collection che combaciano con i generi selezionati */
    public static ResultSet searchCollectionssByFilter(Statement stmt, Collection collection) throws SQLException {
        String genre = buildGenresQueryString(collection.getCollectionGenre()).replace(" ", "").replace(",", "");
        int[] genreFlags = getGenreFlags(genre);

        String sql = String.format(Queries.SELECT_SEARCH_COLLECTIONSS_BY_FILTER,
                "%" + collection.getCollectionName() + "%",
                genreFlags[0], genreFlags[1], genreFlags[2], genreFlags[3], genreFlags[4],
                genreFlags[5], genreFlags[6], genreFlags[7], genreFlags[8], genreFlags[9],
                genreFlags[10], genreFlags[11], genreFlags[12], genreFlags[13], genreFlags[14],
                genreFlags[15], genreFlags[16], genreFlags[17], genreFlags[18], genreFlags[19],
                genreFlags[20], genreFlags[21], genreFlags[22], genreFlags[23], genreFlags[24],
                genreFlags[25], genreFlags[26]
        );

        return stmt.executeQuery(sql);
    }


    /** Recupera tutta la collection_utente, va usata con retriveGenreCollection per ottenere
     * i generi delle collezioni caricate, non usato mai */
    public static ResultSet retriveCollectionClientByUsername(Statement stmt, String username) throws SQLException {
        String sql = String.format(Queries.SELECT_COLLECTION_BY_USER,username);
        return stmt.executeQuery(sql);
    }

    /** Recupera tutta la collection_utente, va usata con retriveGenreCollection per ottenere
     * i generi delle collection caricate */
    public static ResultSet retrieveCollectionClientByEmail(Statement stmt, String email) throws SQLException {
        String sql = String.format(Queries.SELECT_COLLECTION_BY_EMAIL,email);
        return stmt.executeQuery(sql);
    }

    /** Recupera da GENERI, i generi della collection passata come id, non usati mai */
    public static ResultSet retrieveGenreCollectionById(Statement stmt, int id) throws SQLException {
        String sql = String.format(Queries.SELECT_GENRE_USER_QUERY,id);
        return stmt.executeQuery(sql);
    }

    public static ResultSet retrieveGenreCollection(Statement stmt, String username) throws SQLException {
        String sql = String.format(Queries.SELECT_GENRE_COLLECTION, username);
        return stmt.executeQuery(sql);
    }

    public static ResultSet retrieveGenreCollectionByLink(Statement stmt, String link) throws SQLException {
        String sql = String.format(Queries.SELECT_GENRE_COLLECTION_BY_LINK, link);
        return stmt.executeQuery(sql);
    }

    /** Rimuove la collection su tutte le tabelle, dal link della collection
     * */
    public static void removeCollectionByLink(Statement stmt, String link) throws SQLException {
        String sql = String.format(Queries.DELETE_COLLECTION_BY_LINK_COLLECTION_UTENTE,link);
        stmt.executeUpdate(sql);
    }

    public static void approveCollectionByLink(Statement stmt, String link) throws SQLException {
        String sql = String.format(Queries.UPDATE_APPROVE_COLLECTION,1,link);
        stmt.executeUpdate(sql);
    }

    public static ResultSet retrievePendingCollectionss(Statement stmt) throws SQLException {
        String sql = String.format(Queries.SELECT_PENDING_COLLECTIONSS,0);
        return stmt.executeQuery(sql);
    }

    public static ResultSet retrieveApprovedCollectionss(Statement stmt) throws SQLException {
        String sql = String.format(Queries.SELECT_APPROVED_COLLECTIONSS,1);
        return stmt.executeQuery(sql);
    }

    public static ResultSet searchCollectionssByGenre(Statement stmt, Collection p) throws SQLException {
        String genre = buildGenresQueryString(p.getCollectionGenre()).replace(" ", "").replace(",", "");
        int[] genreFlags = getGenreFlags(genre);

        String sql = String.format(Queries.SELECT_SEARCH_BY_GENRE,
                "%" + p.getCollectionName() + "%",
                genreFlags[0], genreFlags[1], genreFlags[2], genreFlags[3], genreFlags[4],
                genreFlags[5], genreFlags[6], genreFlags[7], genreFlags[8], genreFlags[9],
                genreFlags[10], genreFlags[11], genreFlags[12], genreFlags[13], genreFlags[14],
                genreFlags[15], genreFlags[16], genreFlags[17], genreFlags[18], genreFlags[19],
                genreFlags[20], genreFlags[21], genreFlags[22], genreFlags[23], genreFlags[24],
                genreFlags[25], genreFlags[26]
        );

        return stmt.executeQuery(sql);
    }

    /**
     * Converte la stringa di generi in un array di flag.
     */
    private static int[] getGenreFlags(String genre) {
        int[] flags = new int[27]; // Supponiamo ci siano 27 generi
        for (int i = 0; i < Math.min(flags.length, genre.length()); i++) {
            flags[i] = (genre.charAt(i) == '1') ? 1 : 0;
        }
        return flags;
    }


    /** Non utilizzato*/
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

package engineering.query;

public class Queries {

    private Queries(){}

    /* ---------- QUERY LOGIN ---------- */
    public static final String INSERT_USER = "INSERT INTO user (email, username, password, supervisor) VALUES ('%s','%s','%s','%d')";
    public static final String INSERT_GENERI_USER = "INSERT INTO generi_user (Azione, Avventura, Animazione, Biografico, Commedia, Poliziesco, Documentario, Drammatico, PerFamiglie, Fantastico, Noir, GiocoAPremiTelevisivo, Storico, Horror, Musica, Musical, Giallo, Telegiornale, Reality, Sentimentale, Fantascienza, Cortometraggio, Sportivo, TalkShow, Thriller, Guerra, Western, email) VALUES (%s)";



    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM user WHERE email = '%s'";
    public static final String SELECT_USER_BY_USERNAME = "SELECT * FROM user WHERE username = '%s'";
    public static final String SELECT_PASSWORD_BY_EMAIL = "SELECT password FROM user WHERE email = '%s'";
    public static final String SELECT_GENRE_USER_QUERY = "SELECT * FROM generi_user WHERE email = '%s'";

    public static final String SEARCH_USERNAME = "SELECT * FROM user WHERE username = '%s' ";
    public static final String SEARCH_EMAIL = "SELECT * FROM user WHERE email = '%s' ";

    public static final String UPDATE_GENERI_USER =
            "UPDATE generi_user SET " +
                    "Azione = %d, Avventura = %d, Animazione = %d, Biografico = %d, Commedia = %d, Poliziesco = %d, Documentario = %d, " +
                    "Drammatico = %d, PerFamiglie = %d, Fantastico = %d, Noir = %d, GiocoAPremiTelevisivo = %d, Storico = %d, " +
                    "Horror = %d, Musica = %d, Musical = %d, Giallo = %d, Telegiornale = %d, Reality = %d, Sentimentale = %d, " +
                    "Fantascienza = %d, Cortometraggio = %d, Sportivo = %d, TalkShow = %d, Thriller = %d, Guerra = %d, Western = %d" +
                    "WHERE email = '%s'";

    /* ---------- QUERY COLLEZIONE ---------- */
    public static final String INSERT_COLLEZIONE_USER = "INSERT INTO collezione_utente (nameCollezione, email, username, link, " +
            "approved, Azione, Avventura, Animazione, Biografico, Commedia, Poliziesco, Documentario, Drammatico, PerFamiglie, Fantastico, Noir, GiocoAPremiTelevisivo, Storico, Horror, Musica, Musical, Giallo, Telegiornale, Reality, Sentimentale, Fantascienza, Cortometraggio, Sportivo, TalkShow, Thriller, Guerra, Western) VALUES ('%s','%s','%s','%s','%d', %s)";
    public static final String INSERT_GENERI_COLLEZIONE = "INSERT INTO collezione_utente (Azione, Avventura, Animazione, Biografico, Commedia, Poliziesco, Documentario, Drammatico, PerFamiglie, " +
            "Fantastico, Noir, GiocoAPremiTelevisivo, Storico, Horror, Musica, Musical, Giallo, Telegiornale, Reality, " +
            "Sentimentale, Fantascienza, Cortometraggio, Sportivo, TalkShow, Thriller, Guerra, Western) VALUES (%s)";

    public static final String SELECT_LINK_QUERY = "SELECT * FROM collezione_utente WHERE link = '%s'";
    public static final String SELECT_COLLEZIONE_BY_USER = "SELECT * FROM collezione_utente WHERE username = '%s'"; // Recupero tutto ma non uso tutto
    public static final String SELECT_COLLEZIONE_BY_EMAIL = "SELECT * FROM collezione_utente WHERE email = '%s'";
    public static final String SELECT_SEARCH_BY_GENRE = "SELECT * FROM collezione_utente WHERE nameCollezione LIKE '%s' AND approved = '1' " +
            "AND Azione = '%d' AND Avventura = '%d' AND Animazione = '%d' AND Biografico = '%d' AND Commedia = '%d' " +
            "AND Poliziesco = '%d' AND Documentario = '%d' AND Drammatico = '%d' AND PerFamiglie = '%d' AND Fantastico = '%d' " +
            "AND Noir = '%d' AND GiocoAPremiTelevisivo = '%d' AND Storico = '%d' AND Horror = '%d' AND Musica = '%d' " +
            "AND Musical = '%d' AND Giallo = '%d' AND Telegiornale = '%d' AND Reality = '%d' AND Sentimentale = '%d' " +
            "AND Fantastico = '%d' AND Cortometraggio = '%d' AND Sportivo = '%d' AND TalkShow = '%d' AND Thriller = '%d' " +
            "AND Guerra = '%d' AND Western = '%d'";
    public static final String SELECT_SEARCH_COLLEZIONES_BY_FILTER = "SELECT * FROM collezione_utente WHERE nameCollezione LIKE '%s' " +
            "AND approved = '1' AND Azione = '%d' " +
            "AND Avventura = '%d' AND Animazione = '%d' AND Biografico = '%d' AND Commedia = '%d' " +
            "AND Poliziesco = '%d' AND Documentario = '%d' AND Drammatico = '%d' AND PerFamiglie = '%d' AND Fantastico = '%d' " +
            "AND Noir = '%d' AND GiocoAPremiTelevisivo = '%d' AND Storico = '%d' AND Horror = '%d' AND Musica = '%d' " +
            "AND Musical = '%d' AND Giallo = '%d' AND Telegiornale = '%d' AND Reality = '%d' AND Sentimentale = '%d' " +
            "AND Fantastico = '%d' AND Cortometraggio = '%d' AND Sportivo = '%d' AND TalkShow = '%d' AND Thriller = '%d' " +
            "AND Guerra = '%d' AND Western = '%d'";
    public static final String SELECT_TITLE_COLLEZIONE_BY_USERNAME = "SELECT * FROM collezione_utente WHERE nameCollezione = '%s' AND username = '%s'";

    public static final String SELECT_ALL_COLLEZIONE = "SELECT * FROM collezione_utente"; // Recupero tutto ma non uso tutto
    public static final String SELECT_PENDING_COLLEZIONES = "SELECT * FROM collezione_utente WHERE approved = '%d'";
    public static final String SELECT_APPROVED_COLLEZIONES = "SELECT * FROM collezione_utente WHERE approved = '%d'";
    public static final String SELECT_GENRE_COLLEZIONE_BY_ID = "SELECT * FROM generi_user WHERE id = '%d'";
    public static final String SELECT_ID_BY_EMAIL = "SELECT id FROM collezione_utente WHERE email = '%s'";
    public static final String SELECT_GENRE_COLLEZIONE = "SELECT Azione, Avventura, Animazione, Biografico, Commedia, Poliziesco, Documentario, Drammatico, PerFamiglie, Fantastico, Noir, GiocoAPremiTelevisivo, Storico, Horror, Musica, Musical, Giallo, Telegiornale, Reality, Sentimentale, Fantascienza, Cortometraggio, Sportivo, TalkShow, Thriller, Guerra, Western " +
            "FROM collezione_utente WHERE username = '?'";
    public static final String SELECT_GENRE_COLLEZIONE_BY_LINK = "SELECT Azione, Avventura, Animazione, Biografico, Commedia, Poliziesco, Documentario, Drammatico, PerFamiglie, Fantastico, Noir, GiocoAPremiTelevisivo, Storico, Horror, Musica, Musical, Giallo, Telegiornale, Reality, Sentimentale, Fantascienza, Cortometraggio, Sportivo, TalkShow, Thriller, Guerra, Western " +
            "FROM collezione_utente WHERE link = '?'";
    public static final String SELECT_SEARCH_COLLEZIONE = "SELECT * FROM collezione_utente WHERE nameCollezione LIKE '%s' AND approved = '1'";

    public static final String UPDATE_APPROVE_COLLEZIONE = "UPDATE collezione_utente SET approved = '%d' WHERE link = '%s' ";

    public static final String DELETE_COLLEZIONE_BY_LINK_COLLEZIONE_UTENTE = "DELETE FROM collezione_utente WHERE link = '%s'" ;

    public static final String INSERT_NOTICE_USER = "INSERT INTO notifiche (email, title, body) VALUES ('%s','%s','%s')";
    public static final String SELECT_NOTICE_USER = "SELECT * FROM notifiche WHERE email = '%s'";
    public static final String REMOVE_NOTICE_CLIENT = "DELETE FROM notifiche WHERE email = '%s' AND body = '%s'" ;

}

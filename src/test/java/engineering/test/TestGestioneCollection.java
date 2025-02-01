package engineering.test;

import engineering.dao.ClientDAO;
import engineering.dao.CollectionDAO;
import engineering.exceptions.*;
import engineering.pattern.abstract_factory.DAOFactory;
import model.Login;
import model.Collection;
import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.*;


public class TestGestioneCollection {

    /* Utente per test */
    private final String username = "testUser";
    private final String email = "testUser@gmail.com";
    private final List<String> generiUser = new ArrayList<>(List.of("Azione", "Drammatico"));

    /* Collection per test */
    private final String collectiontitle = "testCollection";
    private final List<String> generi = List.of(
            "Azione", "Avventura", "Animazione", "Biografico", "Commedia",
            "Poliziesco", "Documentario", "Drammatico", "PerFamiglie", "Fantastico",
            "Noir", "GiocoAPremiTelevisivo", "Storico", "Horror", "Musica",
            "Musical", "Giallo", "Telegiornale", "Reality", "Sentimentale",
            "Fantascienza", "Cortometraggio", "Sportivo", "TalkShow", "Thriller",
            "Guerra", "Western");
    private final int lunghezzalink = 10; // Lunghezza del link


    /** Testa il corretto caricamento della Collection in persistenza */
    @Test
    public void testAddCollection() {
        insertUser(); // L'utente della Collection deve esistere
        CollectionDAO collectionDAO = DAOFactory.getDAOFactory().createCollectionDAO();  // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        List<String> genresrandom = popolaGeneriCasuali(); // Popola la lista casuale di generi
        String linkrandom = generaLinkCasuale(); // Popola il link

        Collection collection = new Collection(email, username, collectiontitle, linkrandom, genresrandom, false);
        try {
            // Inserisci la Collection
            collectionDAO.insertCollection(collection);

            // Recupera le Collection dell'utente test
            List<Collection> retrievedCollectionss = collectionDAO.retrieveCollectionssByEmail(email);

            // Assert sul numero di Collection attese (1)
            Assert.assertEquals(1, retrievedCollectionss.size());
            // Assert se la Collection attesa abbia il titolo generato -> Il link è univoco nel sistema
            Assert.assertEquals(collectiontitle, retrievedCollectionss.getFirst().getCollectionName());

            // Elimino per permettere i corretti casi di test futuri
            collectionDAO.deleteCollection(retrievedCollectionss.getFirst());

        } catch (CollectionLinkAlreadyInUseException | CollectionNameAlreadyInUseException e) {
            // Se ci sono eccezioni, il test fallirà
            Assert.fail(String.format("Errore durante l'inserimento della Collection: %s", e.getMessage()));
        }
    }

    /** Testa la corretta eliminazione della Collection dalla persistenza */
    @Test
    public void testDeleteCollection() {
        insertUser(); // L'utente della Collection deve esistere
        List<String> genresrandom = popolaGeneriCasuali(); // Popola la lista casuale di generi
        String linkrandom = generaLinkCasuale(); // Popola il link

        // Crea una Collection di test
        Collection collection = new Collection(email, username, collectiontitle, linkrandom, genresrandom, false);

        try {
            // Inserimento Collection
            CollectionDAO collectionDAO = DAOFactory.getDAOFactory().createCollectionDAO();  // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)
            collectionDAO.insertCollection(collection);

            // Recupera la Collection appena inserita
            List<Collection> retrievedCollectionss = collectionDAO.retrieveCollectionssByEmail(email);
            Assert.assertEquals(1, retrievedCollectionss.size());

            // Elimina la Collection
            collectionDAO.deleteCollection(retrievedCollectionss.getFirst());

            // Assicurati che la Collection sia stata eliminata
            retrievedCollectionss = collectionDAO.retrieveCollectionssByEmail(email);

            for(Collection p: retrievedCollectionss){
                if(Objects.equals(p.getLink(), collection.getLink())){
                    Assert.assertEquals(0, retrievedCollectionss.size());
                }
            }

        } catch (CollectionLinkAlreadyInUseException | CollectionNameAlreadyInUseException e) {
            // Se ci sono eccezioni, il test fallirà
            Assert.fail(String.format("Errore durante l'inserimento della Collection: %s", e.getMessage()));
        }
    }

    /** Testa il corretto filtraggio di Collection, ne vengono caricate in un numero casuale
     * e ci si aspetta che tutte le Collection recuperate abbiano lo stesso genere musicale */
    @Test
    public void testRicercaCollectionPerGenere() {
        insertUser(); // L'utente della Collection deve esistere

        // Inserisce un numero casuale di Collection di test (da 1 a 10) nel sistema con la stessa lista di Collection
        List<String> genresrandom = popolaGeneriCasuali();
        List<Collection> collectioninserted = inserisciCollectionCasuali(genresrandom);

        // Effettua una ricerca utilizzando il filtro per genere
        CollectionDAO collectionDAO = DAOFactory.getDAOFactory().createCollectionDAO();

        // Creo la Collection da cercare
        Collection collectionfiltrata = new Collection();
        collectionfiltrata.setCollectionGenre(genresrandom);

        List<Collection> risultatoRicerca = collectionDAO.searchCollectionByGenre(collectionfiltrata);

        // Mi assicuro che il risultato contenga solo Collection con il genere specificato
        for (Collection collection : risultatoRicerca) {
            Assert.assertEquals(genresrandom, collection.getCollectionGenre());
        }

        // Elimina le Collection di test
        eliminaCollectionDiTest(collectioninserted);
    }

    /** Carica un numero random (max 10) di Collection con lo stesso genere musicale per valutare la correttezza
     * del filtraggio per genere musicale */
    private List<Collection> inserisciCollectionCasuali(List<String> genresrandom) {
        CollectionDAO collectionDAO = DAOFactory.getDAOFactory().createCollectionDAO();
        int numbercollectioninserted = getRandomNumberInRange();
        List<Collection> insertedCollectionss = new ArrayList<>();

        for (int i = 0; i < numbercollectioninserted; i++) {
            String generatedcollection = generaTitoloCollectionCasuale();
            String link = generaLinkCasuale();

            Collection collection = new Collection(email, username, generatedcollection, link, genresrandom, true);

            try {
                collectionDAO.insertCollection(collection);
                insertedCollectionss.add(collection);
            } catch (CollectionLinkAlreadyInUseException | CollectionNameAlreadyInUseException e) {
                e.fillInStackTrace();
            }
        }

        return insertedCollectionss;
    }

    /** Crea un link random per la Collection */
    private String generaLinkCasuale() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[lunghezzalink];

        secureRandom.nextBytes(bytes);

        // Converte i byte generati in una stringa usando Base64
        // L'encoder Base64 con URL-safe encoding, è comunemente utilizzato per generare link web.
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /** Crea una lista di generi musicali random */
    private List<String> popolaGeneriCasuali() {
        Random random = new Random();
        int numeroGeneri = random.nextInt(generi.size()) + 1;  // Genera una lunghezza casuale tra 1 e la lunghezza di GENRES inclusa

        List<String> genresRandom = new ArrayList<>(generi);

        // Mescola la lista per ottenere una sequenza casuale
        for (int i = 0; i < genresRandom.size(); i++) {
            int index = random.nextInt(genresRandom.size());
            String temp = genresRandom.get(i);
            genresRandom.set(i, genresRandom.get(index));
            genresRandom.set(index, temp);
        }

        // Mantieni solo i primi 'numeroGeneri' generi
        return genresRandom.subList(0, numeroGeneri);
    }

    /** Inserisce l'utente a cui sono collegate le Collection di test */
    private void insertUser() {
        ClientDAO clientDAO = DAOFactory.getDAOFactory().createClientDAO();

        // Uso come password lo username
        Login registrationUser = new Login(username, email, username, generiUser);

        try {
            clientDAO.insertClient(registrationUser);
        } catch (UsernameAlreadyInUseException | EmailAlreadyInUseException e) {
            e.fillInStackTrace(); // Ignoro
        }
    }

    /** Elimina la lista di Collection inserite */
    private void eliminaCollectionDiTest(List<Collection> collectionDiTest) {
        CollectionDAO collectionDAO = DAOFactory.getDAOFactory().createCollectionDAO();
        for (Collection collection : collectionDiTest) {
            collectionDAO.deleteCollection(collection);
        }
    }

    /** Titolo random */
    private String generaTitoloCollectionCasuale() {
        Random random = new Random();
        int numeroCasuale = random.nextInt(1000);  // Puoi regolare il range del numero a seconda delle tue preferenze
        return "MyCollection_" + numeroCasuale;
    }

    /** Numero random da 1 a 10*/
    private int getRandomNumberInRange() {
        Random random = new Random();
        return random.nextInt(10) + 1; // Genera un numero casuale tra 1 e 10
    }

}
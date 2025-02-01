package engineering.dao;

import com.google.gson.*;
import engineering.exceptions.*;
import engineering.others.*;
import model.Collection;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import java.util.stream.Stream;

public class CollectionDAOJSON implements CollectionDAO {

    private static final String ERROR_IMPLEMENTATION = "Non è stato implementato in JSON";

    /**
     * Questo metodo inserisce la collection sia sulla cartella del singolo utente
     * Aggiunge inoltre sulle cartelle generali delle collection approvate e delle collection in attesa di approvazione
     */
    public void insertCollection(Collection collection) throws CollectionLinkAlreadyInUseException {
        // Costruisco il percorso del file collection.json per l'utente
        Path userDirectory = Paths.get(ConfigurationJSON.USER_BASE_DIRECTORY, collection.getEmail());

        try {
            // Crea la directory utente se non esiste
            Files.createDirectories(userDirectory);

            // Genera un UUID univoco
            String uniqueId = UUID.randomUUID().toString();

            // Imposta l'ID della collection
            collection.setId(uniqueId);

            // Costruisco il percorso per la collection SENZA uuid per la cartella dell'utente
            String collectionFileName = formatCollectionFileName(collection.getCollectionName());
            Path collectionPath = userDirectory.resolve(collectionFileName + ConfigurationJSON.FILE_EXTENCTION);

            // Verifica se la collection esiste già per l'utente
            if (!Files.exists(collectionPath)) {
                // Usa Gson per convertire l'oggetto Collection in una rappresentazione JSON
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(collection);

                // Scrivi il JSON nel file collectionName.json nella cartella dell'utente
                Files.writeString(collectionPath, json);

                // Copia il file JSON nella cartella di tutte le collection ma stavolta aggiungi UUID
                String uuidCollectionFileName = collectionFileName + "[" + uniqueId + "]";
                Path allCollectionssPath;

                if (collection.getApproved()) {
                    // Se la collection è approvata, salvala nella cartella delle collection approvate
                    createDirectoryIfNotExists(Path.of(ConfigurationJSON.APPROVED_COLLECTION_BASE_DIRECTORY));
                    allCollectionssPath = Paths.get(ConfigurationJSON.APPROVED_COLLECTION_BASE_DIRECTORY, uuidCollectionFileName + ConfigurationJSON.FILE_EXTENCTION);
                } else {
                    // Altrimenti, salvala nella cartella delle collection non approvate
                    createDirectoryIfNotExists(Path.of(ConfigurationJSON.PENDING_COLLECTIONSS_BASE_DIRECTORY));
                    allCollectionssPath = Paths.get(ConfigurationJSON.PENDING_COLLECTIONSS_BASE_DIRECTORY, uuidCollectionFileName + ConfigurationJSON.FILE_EXTENCTION);
                }

                Files.copy(collectionPath, allCollectionssPath, StandardCopyOption.REPLACE_EXISTING);
                Printer.logPrint("CollectionDAOJSON: Collection inserita con successo!");

            } else {
                Printer.logPrint("CollectionDAOJSON: Una collection con questo nome esiste già per questo utente.");
                throw new CollectionLinkAlreadyInUseException();
            }
        } catch (IOException e) {
            handleDAOException(e);
        }
    }

    public Collection approveCollection(Collection collection) {
        // Passo 1: Aggiornare il campo nella cartella dell'utente proprietario della Collection
        boolean updatedInUserFolder = updateCollectionApprovedField(collection, ConfigurationJSON.USER_BASE_DIRECTORY);
        // Passo 2: Aggiornare il campo nella cartella delle Collection in attesa di approvazione
        boolean updatedInPendingFolder = updateCollectionApprovedField(collection, ConfigurationJSON.PENDING_COLLECTIONSS_BASE_DIRECTORY);

        // Passo 3: Copiare file della Collection nella cartella delle collection approvate
        //          Eliminare file della Collection nella cartella delle collection in attesa
        if (updatedInUserFolder && updatedInPendingFolder) {
            copyAndDeleteCollection(collection);
            collection.setApproved(true);
            return collection;
        }
        return null;
    }

    /**
     * Imposta il campo Approved a true all'interno del file della collection
     */
    private boolean updateCollectionApprovedField(Collection collection, String folderPath) {
        String fileName;
        Path collectionPath;
        if (folderPath.equals(ConfigurationJSON.PENDING_COLLECTIONSS_BASE_DIRECTORY) ||
                folderPath.equals(ConfigurationJSON.APPROVED_COLLECTION_BASE_DIRECTORY)) {
            fileName = addUuidToCollectionFileName(formatCollectionFileName(collection.getCollectionName()), collection.getId());
            collectionPath = Paths.get(folderPath, fileName + ConfigurationJSON.FILE_EXTENCTION);
        } else {
            fileName = formatCollectionFileName(formatCollectionFileName(collection.getCollectionName()));
            collectionPath = Paths.get(folderPath, collection.getEmail(), fileName + ConfigurationJSON.FILE_EXTENCTION);
        }
        if (Files.exists(collectionPath)) {
            try {
                // Leggi il contenuto del file
                String content = Files.readString(collectionPath);

                // Usa Gson per de-serializzare il contenuto JSON e ottenere la collection
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Collection updatedCollection = gson.fromJson(content, Collection.class);

                // Aggiorna lo stato "approved"
                updatedCollection.setApproved(true);

                // Converti l'oggetto Collection aggiornato in JSON
                String updatedJson = gson.toJson(updatedCollection);

                // Sovrascrivi il file con le informazioni aggiornate
                Files.writeString(collectionPath, updatedJson);
                return true;
            } catch (IOException e) {
                handleDAOException(e);
                return false;
            }
        } else {
            Printer.errorPrint("CollectionDAOJSON: File della collection non trovato.");
            return false;
        }
    }

    private String addUuidToCollectionFileName(String collectionName, String uuid) {
        // Sostituisce gli spazi con underscore, convergi tutto in minuscolo e aggiungi UUID tra parentesi quadre
        return formatCollectionFileName(collectionName) + "[" + uuid + "]";
    }

    private String formatCollectionFileName(String collectionName) {
        // Sostituisce gli spazi con underscore e convergi tutto in minuscolo
        return collectionName.replace(" ", "_").toLowerCase();
    }

    private void copyAndDeleteCollection(Collection collection) {
        String fileNameWithUUID = addUuidToCollectionFileName(formatCollectionFileName(collection.getCollectionName()), collection.getId());
        Path sourcePath = Paths.get(ConfigurationJSON.PENDING_COLLECTIONSS_BASE_DIRECTORY, fileNameWithUUID + ConfigurationJSON.FILE_EXTENCTION);
        Path destinationPath = Paths.get(ConfigurationJSON.APPROVED_COLLECTION_BASE_DIRECTORY, fileNameWithUUID + ConfigurationJSON.FILE_EXTENCTION);

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            deleteCollectionFromFolder(sourcePath);
        } catch (IOException e) {
            handleDAOException(e);
        }
    }

    /**
     * Per eliminare il file della Collection da una cartella
     */
    private boolean deleteCollectionFromFolder(Path collectionPath) {
        try {
            return Files.deleteIfExists(collectionPath); // Ritorna true se l'eliminazione ha avuto successo
        } catch (IOException e) {
            handleDAOException(e);
            return false; // Ritorna false se si verifica un'eccezione durante l'eliminazione
        }
    }

    /**
     * Questo metodo elimina il file della collection dalla cartella dell'utente
     * e dalla cartella globale delle collection (approvate o non approvate in base al caso).
     */
    public void deleteCollection(Collection collection) {
        // Costruisco il percorso del file collection.json per l'utente
        Path userDirectory = Paths.get(ConfigurationJSON.USER_BASE_DIRECTORY, collection.getEmail());
        // Costruisco il percorso per la collection SENZA uuid per la cartella dell'utente
        String collectionFileName = formatCollectionFileName(collection.getCollectionName());
        Path collectionPath = userDirectory.resolve(collectionFileName + ConfigurationJSON.FILE_EXTENCTION);

        // Elimina il file della collection dall'utente
        boolean deletedFromUserFolder = deleteCollectionFromFolder(collectionPath);

        // Nome del file con UUID della collection
        String uuidCollectionFileName = collectionFileName + "[" + collection.getId() + "]";

        // Verifica se la collection è approvata o meno
        Path allCollectionssPath;

        if (collection.getApproved()) {
            // Se la collection è approvata, elimina il file dalla cartella delle collection approvate
            allCollectionssPath = Paths.get(ConfigurationJSON.APPROVED_COLLECTION_BASE_DIRECTORY, uuidCollectionFileName + ConfigurationJSON.FILE_EXTENCTION);
        } else {
            // Altrimenti, elimina il file dalla cartella delle collection non approvate
            allCollectionssPath = Paths.get(ConfigurationJSON.PENDING_COLLECTIONSS_BASE_DIRECTORY, uuidCollectionFileName + ConfigurationJSON.FILE_EXTENCTION);
        }

        // Elimina il file dalla cartella globale delle collection
        boolean deletedFromGlobalFolder = deleteCollectionFromFolder(allCollectionssPath);

        if (deletedFromUserFolder && deletedFromGlobalFolder) {
            Printer.logPrint("CollectionDAOJSON: Collection eliminata con successo!");
        } else {
            Printer.logPrint("CollectionDAOJSON: Errore durante l'eliminazione della collection.");
        }
    }

    public List<Collection> retrieveCollectionssByEmail(String mail) {
        List<Collection> collectionList = new ArrayList<>();

        // Costruisco il percorso della directory dell'utente
        Path userDirectory = Paths.get(ConfigurationJSON.USER_BASE_DIRECTORY, mail);

        // Verifica se la directory dell'utente esiste
        if (Files.exists(userDirectory)) {
            collectionList = retrieveCollectionssFromDirectory(userDirectory);
        } else {
            Printer.errorPrint("CollectionDAOJSON: Utente non trovato!");
        }

        return collectionList;
    }

    private List<Collection> retrieveCollectionssFromDirectory(Path directory) {
        List<Collection> collectionss = new ArrayList<>();

        try (Stream<Path> paths = Files.list(directory)) {
            paths.filter(file ->
                            Files.isRegularFile(file) &&
                                    file.getFileName().toString().endsWith(ConfigurationJSON.FILE_EXTENCTION) &&
                                    !file.getFileName().toString().equals(ConfigurationJSON.USER_INFO_FILE_NAME))
                    .forEach(file -> {
                        try {
                            String content = Files.readString(file);
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            Collection collection = gson.fromJson(content, Collection.class);
                            if (collection != null) {
                                collectionss.add(collection);
                            }
                        } catch (IOException e) {
                            handleDAOException(e);
                        }
                    });
        } catch (IOException e) {
            handleDAOException(e);
        }

        return collectionss;
    }


    public List<Collection> retrievePendingCollectionss() {
        Path pendingCollectionssDirectory = Paths.get(ConfigurationJSON.PENDING_COLLECTIONSS_BASE_DIRECTORY);
        createDirectoryIfNotExists(pendingCollectionssDirectory);
        return retrieveCollectionssFromDirectory(pendingCollectionssDirectory);
    }

    public List<Collection> retrieveApprovedCollectionss() {
        Path approvedCollectionssDirectory = Paths.get(ConfigurationJSON.APPROVED_COLLECTION_BASE_DIRECTORY);
        createDirectoryIfNotExists(approvedCollectionssDirectory);
        return retrieveCollectionssFromDirectory(approvedCollectionssDirectory);
    }

    private void createDirectoryIfNotExists(Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            handleDAOException(e);
        }
    }

        public List<Collection> searchCollectionByTitle(Collection collection) {
        // Estrai il nome della collection da cercare
        String targetCollectionName = collection.getCollectionName().toLowerCase();

        // Recupera tutte le collection approvate
        List<Collection> allApprovedCollectionss = retrieveApprovedCollectionss();

        List<Collection> matchingCollectionss = new ArrayList<>();

        // Filtra le collection che contengono il titolo della collection di destinazione
        for (Collection p : allApprovedCollectionss) {
            if (p.getCollectionName().toLowerCase().contains(targetCollectionName)) {
                matchingCollectionss.add(p);
            }
        }

        return matchingCollectionss;
    }

    public List<Collection> searchCollectionByGenre(Collection collection) {
        Printer.logPrint(ERROR_IMPLEMENTATION);
        return Collections.emptyList();
    }

    /** Metodo utilizzato per notificare IOException */
    private void handleDAOException(Exception e) {
        Printer.errorPrint(String.format("CollectionDAOJSON: %s", e.getMessage()));
    }

}
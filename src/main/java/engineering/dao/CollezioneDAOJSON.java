package engineering.dao;

import com.google.gson.*;
import engineering.exceptions.*;
import engineering.others.*;
import model.Collezione;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import java.util.stream.Stream;

public class CollezioneDAOJSON implements CollezioneDAO {

    private static final String ERROR_IMPLEMENTATION = "Non è stato implementato in JSON";

    /**
     * Questo metodo inserisce la collezione sia sulla cartella del singolo utente
     * Aggiunge inoltre sulle cartelle generali delle collezione approvate e delle collezione in attesa di approvazione
     */
    public void insertCollezione(Collezione collezione) throws CollezioneLinkAlreadyInUseException {
        // Costruisco il percorso del file collezione.json per l'utente
        Path userDirectory = Paths.get(ConfigurationJSON.USER_BASE_DIRECTORY, collezione.getEmail());

        try {
            // Crea la directory utente se non esiste
            Files.createDirectories(userDirectory);

            // Genera un UUID univoco
            String uniqueId = UUID.randomUUID().toString();

            // Imposta l'ID della collezione
            collezione.setId(uniqueId);

            // Costruisco il percorso per la collezione SENZA uuid per la cartella dell'utente
            String collezioneFileName = formatCollezioneFileName(collezione.getCollezioneName());
            Path collezionePath = userDirectory.resolve(collezioneFileName + ConfigurationJSON.FILE_EXTENCTION);

            // Verifica se la collezione esiste già per l'utente
            if (!Files.exists(collezionePath)) {
                // Usa Gson per convertire l'oggetto Collezione in una rappresentazione JSON
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(collezione);

                // Scrivi il JSON nel file collezioneName.json nella cartella dell'utente
                Files.writeString(collezionePath, json);

                // Copia il file JSON nella cartella di tutte le collezione ma stavolta aggiungi UUID
                String uuidCollezioneFileName = collezioneFileName + "[" + uniqueId + "]";
                Path allCollezionesPath;

                if (collezione.getApproved()) {
                    // Se la collezione è approvata, salvala nella cartella delle collezione approvate
                    createDirectoryIfNotExists(Path.of(ConfigurationJSON.APPROVED_COLLEZIONE_BASE_DIRECTORY));
                    allCollezionesPath = Paths.get(ConfigurationJSON.APPROVED_COLLEZIONE_BASE_DIRECTORY, uuidCollezioneFileName + ConfigurationJSON.FILE_EXTENCTION);
                } else {
                    // Altrimenti, salvala nella cartella delle collezione non approvate
                    createDirectoryIfNotExists(Path.of(ConfigurationJSON.PENDING_COLLEZIONES_BASE_DIRECTORY));
                    allCollezionesPath = Paths.get(ConfigurationJSON.PENDING_COLLEZIONES_BASE_DIRECTORY, uuidCollezioneFileName + ConfigurationJSON.FILE_EXTENCTION);
                }

                Files.copy(collezionePath, allCollezionesPath, StandardCopyOption.REPLACE_EXISTING);
                Printer.logPrint("CollezioneDAOJSON: Collezione inserita con successo!");

            } else {
                Printer.logPrint("CollezioneDAOJSON: Una collezione con questo nome esiste già per questo utente.");
                throw new CollezioneLinkAlreadyInUseException();
            }
        } catch (IOException e) {
            handleDAOException(e);
        }
    }

    public Collezione approveCollezione(Collezione collezione) {
        // Passo 1: Aggiornare il campo nella cartella dell'utente proprietario della Collezione
        boolean updatedInUserFolder = updateCollezioneApprovedField(collezione, ConfigurationJSON.USER_BASE_DIRECTORY);
        // Passo 2: Aggiornare il campo nella cartella delle Collezione in attesa di approvazione
        boolean updatedInPendingFolder = updateCollezioneApprovedField(collezione, ConfigurationJSON.PENDING_COLLEZIONES_BASE_DIRECTORY);

        // Passo 3: Copiare file della Collezione nella cartella delle collezione approvate
        //          Eliminare file della Collezione nella cartella delle collezione in attesa
        if (updatedInUserFolder && updatedInPendingFolder) {
            copyAndDeleteCollezione(collezione);
            collezione.setApproved(true);
            return collezione;
        }
        return null;
    }

    /**
     * Imposta il campo Approved a true all'interno del file della collezione
     */
    private boolean updateCollezioneApprovedField(Collezione collezione, String folderPath) {
        String fileName;
        Path collezionePath;
        if (folderPath.equals(ConfigurationJSON.PENDING_COLLEZIONES_BASE_DIRECTORY) ||
                folderPath.equals(ConfigurationJSON.APPROVED_COLLEZIONE_BASE_DIRECTORY)) {
            fileName = addUuidToCollezioneFileName(formatCollezioneFileName(collezione.getCollezioneName()), collezione.getId());
            collezionePath = Paths.get(folderPath, fileName + ConfigurationJSON.FILE_EXTENCTION);
        } else {
            fileName = formatCollezioneFileName(formatCollezioneFileName(collezione.getCollezioneName()));
            collezionePath = Paths.get(folderPath, collezione.getEmail(), fileName + ConfigurationJSON.FILE_EXTENCTION);
        }
        if (Files.exists(collezionePath)) {
            try {
                // Leggi il contenuto del file
                String content = Files.readString(collezionePath);

                // Usa Gson per de-serializzare il contenuto JSON e ottenere la collezione
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Collezione updatedCollezione = gson.fromJson(content, Collezione.class);

                // Aggiorna lo stato "approved"
                updatedCollezione.setApproved(true);

                // Converti l'oggetto Collezione aggiornato in JSON
                String updatedJson = gson.toJson(updatedCollezione);

                // Sovrascrivi il file con le informazioni aggiornate
                Files.writeString(collezionePath, updatedJson);
                return true;
            } catch (IOException e) {
                handleDAOException(e);
                return false;
            }
        } else {
            Printer.errorPrint("CollezioneDAOJSON: File della collezione non trovato.");
            return false;
        }
    }

    private String addUuidToCollezioneFileName(String collezioneName, String uuid) {
        // Sostituisce gli spazi con underscore, convergi tutto in minuscolo e aggiungi UUID tra parentesi quadre
        return formatCollezioneFileName(collezioneName) + "[" + uuid + "]";
    }

    private String formatCollezioneFileName(String collezioneName) {
        // Sostituisce gli spazi con underscore e convergi tutto in minuscolo
        return collezioneName.replace(" ", "_").toLowerCase();
    }

    private void copyAndDeleteCollezione(Collezione collezione) {
        String fileNameWithUUID = addUuidToCollezioneFileName(formatCollezioneFileName(collezione.getCollezioneName()), collezione.getId());
        Path sourcePath = Paths.get(ConfigurationJSON.PENDING_COLLEZIONES_BASE_DIRECTORY, fileNameWithUUID + ConfigurationJSON.FILE_EXTENCTION);
        Path destinationPath = Paths.get(ConfigurationJSON.APPROVED_COLLEZIONE_BASE_DIRECTORY, fileNameWithUUID + ConfigurationJSON.FILE_EXTENCTION);

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            deleteCollezioneFromFolder(sourcePath);
        } catch (IOException e) {
            handleDAOException(e);
        }
    }

    /**
     * Per eliminare il file della Collezione da una cartella
     */
    private boolean deleteCollezioneFromFolder(Path collezionePath) {
        try {
            return Files.deleteIfExists(collezionePath); // Ritorna true se l'eliminazione ha avuto successo
        } catch (IOException e) {
            handleDAOException(e);
            return false; // Ritorna false se si verifica un'eccezione durante l'eliminazione
        }
    }

    /**
     * Questo metodo elimina il file della collezione dalla cartella dell'utente
     * e dalla cartella globale delle collezione (approvate o non approvate in base al caso).
     */
    public void deleteCollezione(Collezione collezione) {
        // Costruisco il percorso del file collezione.json per l'utente
        Path userDirectory = Paths.get(ConfigurationJSON.USER_BASE_DIRECTORY, collezione.getEmail());
        // Costruisco il percorso per la collezione SENZA uuid per la cartella dell'utente
        String collezioneFileName = formatCollezioneFileName(collezione.getCollezioneName());
        Path collezionePath = userDirectory.resolve(collezioneFileName + ConfigurationJSON.FILE_EXTENCTION);

        // Elimina il file della collezione dall'utente
        boolean deletedFromUserFolder = deleteCollezioneFromFolder(collezionePath);

        // Nome del file con UUID della collezione
        String uuidCollezioneFileName = collezioneFileName + "[" + collezione.getId() + "]";

        // Verifica se la collezione è approvata o meno
        Path allCollezionesPath;

        if (collezione.getApproved()) {
            // Se la collezione è approvata, elimina il file dalla cartella delle collezione approvate
            allCollezionesPath = Paths.get(ConfigurationJSON.APPROVED_COLLEZIONE_BASE_DIRECTORY, uuidCollezioneFileName + ConfigurationJSON.FILE_EXTENCTION);
        } else {
            // Altrimenti, elimina il file dalla cartella delle collezione non approvate
            allCollezionesPath = Paths.get(ConfigurationJSON.PENDING_COLLEZIONES_BASE_DIRECTORY, uuidCollezioneFileName + ConfigurationJSON.FILE_EXTENCTION);
        }

        // Elimina il file dalla cartella globale delle collezione
        boolean deletedFromGlobalFolder = deleteCollezioneFromFolder(allCollezionesPath);

        if (deletedFromUserFolder && deletedFromGlobalFolder) {
            Printer.logPrint("CollezioneDAOJSON: Collezione eliminata con successo!");
        } else {
            Printer.logPrint("CollezioneDAOJSON: Errore durante l'eliminazione della collezione.");
        }
    }

    public List<Collezione> retrieveCollezionesByEmail(String mail) {
        List<Collezione> collezioneList = new ArrayList<>();

        // Costruisco il percorso della directory dell'utente
        Path userDirectory = Paths.get(ConfigurationJSON.USER_BASE_DIRECTORY, mail);

        // Verifica se la directory dell'utente esiste
        if (Files.exists(userDirectory)) {
            collezioneList = retrieveCollezionesFromDirectory(userDirectory);
        } else {
            Printer.errorPrint("CollezioneDAOJSON: Utente non trovato!");
        }

        return collezioneList;
    }

    private List<Collezione> retrieveCollezionesFromDirectory(Path directory) {
        List<Collezione> colleziones = new ArrayList<>();

        try (Stream<Path> paths = Files.list(directory)) {
            paths.filter(file ->
                            Files.isRegularFile(file) &&
                                    file.getFileName().toString().endsWith(ConfigurationJSON.FILE_EXTENCTION) &&
                                    !file.getFileName().toString().equals(ConfigurationJSON.USER_INFO_FILE_NAME))
                    .forEach(file -> {
                        try {
                            String content = Files.readString(file);
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            Collezione collezione = gson.fromJson(content, Collezione.class);
                            if (collezione != null) {
                                colleziones.add(collezione);
                            }
                        } catch (IOException e) {
                            handleDAOException(e);
                        }
                    });
        } catch (IOException e) {
            handleDAOException(e);
        }

        return colleziones;
    }


    public List<Collezione> retrievePendingColleziones() {
        Path pendingCollezionesDirectory = Paths.get(ConfigurationJSON.PENDING_COLLEZIONES_BASE_DIRECTORY);
        createDirectoryIfNotExists(pendingCollezionesDirectory);
        return retrieveCollezionesFromDirectory(pendingCollezionesDirectory);
    }

    public List<Collezione> retrieveApprovedColleziones() {
        Path approvedCollezionesDirectory = Paths.get(ConfigurationJSON.APPROVED_COLLEZIONE_BASE_DIRECTORY);
        createDirectoryIfNotExists(approvedCollezionesDirectory);
        return retrieveCollezionesFromDirectory(approvedCollezionesDirectory);
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

        public List<Collezione> searchCollezioneByTitle(Collezione collezione) {
        // Estrai il nome della collezione da cercare
        String targetCollezioneName = collezione.getCollezioneName().toLowerCase();

        // Recupera tutte le collezione approvate
        List<Collezione> allApprovedColleziones = retrieveApprovedColleziones();

        List<Collezione> matchingColleziones = new ArrayList<>();

        // Filtra le collezione che contengono il titolo della collezione di destinazione
        for (Collezione p : allApprovedColleziones) {
            if (p.getCollezioneName().toLowerCase().contains(targetCollezioneName)) {
                matchingColleziones.add(p);
            }
        }

        return matchingColleziones;
    }

    public List<Collezione> searchCollezioneByGenre(Collezione collezione) {
        Printer.logPrint(ERROR_IMPLEMENTATION);
        return Collections.emptyList();
    }


    public List<Collezione> searchCollezioneByFilters(Collezione collezione) {
        return Collections.emptyList();
    }

    /** Metodo utilizzato per notificare IOException */
    private void handleDAOException(Exception e) {
        Printer.errorPrint(String.format("CollezioneDAOJSON: %s", e.getMessage()));
    }

}
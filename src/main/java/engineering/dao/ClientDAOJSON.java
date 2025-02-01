package engineering.dao;

import com.google.gson.*;
import engineering.exceptions.*;
import engineering.others.*;
import model.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class ClientDAOJSON implements ClientDAO {
    private static final String BASE_DIRECTORY = ConfigurationJSON.USER_BASE_DIRECTORY;

    public void insertClient(Login login) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        try {
            // Verifica se la cartella persistence esiste, altrimenti la crea
            Path persistenceDirectory = Paths.get(ConfigurationJSON.PERSISTENCE_BASE_DIRECTORY);
            if (!Files.exists(persistenceDirectory)) {
                Files.createDirectories(persistenceDirectory);
            }

            // Verifica se l'utente esiste già per email o username
            if (checkIfUserExistsByEmail(login.getEmail())) {
                throw new EmailAlreadyInUseException();
            }
            if (retrieveClientByUsername(login.getUsername()) != null) {
                throw new UsernameAlreadyInUseException();
            }

            // Crea la directory dell'utente e il file di informazioni
            Path userDirectory = Files.createDirectories(Paths.get(BASE_DIRECTORY, login.getEmail()));
            Path userInfoFile = userDirectory.resolve(ConfigurationJSON.USER_INFO_FILE_NAME);

            // Serializza l'oggetto Login in formato JSON e scrivi nel file
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(login);
            Files.writeString(userInfoFile, json);

            Printer.logPrint("ClientDAO: Utente inserito con successo!");
        } catch (IOException e) {
            handleDAOException(e);
        }
    }


    public Client loadClient(Login login) throws UserDoesNotExistException {
        try {
            Path userInfoFile = Paths.get(BASE_DIRECTORY, login.getEmail(), ConfigurationJSON.USER_INFO_FILE_NAME);

            if (Files.exists(userInfoFile)) {
                String content = Files.readString(userInfoFile);
                return parseClient(content);
            } else {
                Printer.logPrint("ClientDAO: Utente non trovato");
                throw new UserDoesNotExistException();
            }
        } catch (IOException e) {
            handleDAOException(e);
        }

        return null;
    }

    private Client parseClient(String content) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = gson.fromJson(content, JsonObject.class);

        boolean isSupervisor = jsonObject.getAsJsonPrimitive("supervisor").getAsBoolean();

        if (isSupervisor) {
            return parseSupervisor(content);
        } else {
            return parseUser(content);
        }
    }

    private User parseUser(String content) {
        return new GsonBuilder().setPrettyPrinting().create().fromJson(content, User.class);
    }

    private Supervisor parseSupervisor(String content) {
        return new GsonBuilder().setPrettyPrinting().create().fromJson(content, Supervisor.class);
    }


    public String getPasswordByEmail(String email) throws UserDoesNotExistException {
        try {
            Path userInfoFile = Paths.get(BASE_DIRECTORY, email, ConfigurationJSON.USER_INFO_FILE_NAME);

            if (!Files.exists(userInfoFile)) {
                throw new UserDoesNotExistException(); // Lanciare l'eccezione se il file non esiste
            }

            String content = Files.readString(userInfoFile);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
            return jsonObject.getAsJsonPrimitive("password").getAsString();

        } catch (IOException e) {
            handleDAOException(e);
            return ""; // Restituisce una stringa vuota in caso di eccezione
        }
    }


    public Client retrieveClientByUsername(String username) {
        try (Stream<Path> userDirectories = Files.list(Paths.get(BASE_DIRECTORY))) {
            return userDirectories
                    .filter(Files::isDirectory)
                    .map(this::getUserFromDirectory)
                    .filter(client -> client != null && client.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            handleDAOException(e);
            return null;
        }
    }

    public void updateGenreClient(Client client) {
        try {
            Path userInfoFile = Paths.get(BASE_DIRECTORY, client.getEmail(), ConfigurationJSON.USER_INFO_FILE_NAME);

            if (Files.exists(userInfoFile)) {
                String content = Files.readString(userInfoFile);
                JsonObject jsonObject = JsonParser.parseString(content).getAsJsonObject();

                // Aggiorna solo il campo delle preferenze
                JsonArray newPreferences = new Gson().toJsonTree(client.getPreferences()).getAsJsonArray();
                jsonObject.add("preferences", newPreferences);

                // Imposta l'indentazione del JSON
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String updatedJson = gson.toJson(jsonObject);

                // Sovrascrive il file solo con le informazioni aggiornate
                Files.writeString(userInfoFile, updatedJson);

                Printer.logPrint("CLIENTDAO: Preferenze utente aggiornate con successo!");
            } else {
                Printer.logPrint("CLIENTDAO: Utente non trovato o file userInfo.json mancante.");
            }
        } catch (IOException e) {
            handleDAOException(e);
        }
    }

    public void tryCredentialExisting(Login login) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        try{
            if(loadClient(login) != null){
                throw new EmailAlreadyInUseException();
            }
            if(retrieveClientByUsername(login.getUsername()) != null){
                throw new UsernameAlreadyInUseException();
            }
        } catch (UserDoesNotExistException e){
            handleDAOException(e);
        }
    }

    private Client getUserFromDirectory(Path userDirectory) {
        try {
            Path userFilePath = userDirectory.resolve(ConfigurationJSON.USER_INFO_FILE_NAME);

            if (Files.exists(userFilePath)) {
                String content = Files.readString(userFilePath);
                return parseClient(content);
            }
        } catch (IOException e) {
            handleDAOException(e);
        }

        return null;
    }

    private boolean checkIfUserExistsByEmail(String userEmail) {
        // Costruito il percorso della directory dell'utente basandosi sulla mail come nome utente
        Path userDirectory = Paths.get(BASE_DIRECTORY, userEmail);

        // Verifica se la directory dell'utente esiste
        return Files.exists(userDirectory);
    }
    private void handleDAOException(Exception e) {
        Printer.errorPrint(String.format("ClientDAOJSON: %s", e.getMessage()));
    }
}

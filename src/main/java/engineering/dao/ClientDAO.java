package engineering.dao;

import engineering.exceptions.*;
import model.*;

public interface ClientDAO {

    /** Inserimento dell'utente in persistenza*/
    void insertClient(Login registration) throws EmailAlreadyInUseException, UsernameAlreadyInUseException;

    /** Recupera le informazioni di un utente in persistenza, ottenuta dall'email */
    Client loadClient(Login login) throws UserDoesNotExistException;

    /** Recupera delle informazioni di un utente dalla persistenza, ottenuta dall'username unico */
    Client retrieveClientByUsername(String username) throws UserDoesNotExistException;

    /** Ottiene la password associata all'email */
    String getPasswordByEmail(String email) throws UserDoesNotExistException;

    /** Aggiorna i generi preferiti dall'utente, recuperato tramite email*/
    void updateGenreClient(Client client);

    void tryCredentialExisting(Login login) throws EmailAlreadyInUseException, UsernameAlreadyInUseException;
}
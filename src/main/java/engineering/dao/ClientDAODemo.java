package engineering.dao;

import engineering.exceptions.*;
import model.Client;
import model.Login;
import model.Supervisor;
import model.User;

import java.util.*;

public class ClientDAODemo implements ClientDAO {

    // Simulazione della memoria: Map email -> Client
    private static final Map<String, Client> clients = new HashMap<>();

    /**Questo blocco viene eseguito una sola volta, quando la classe viene caricata.*/
    static {
        // Dati preimpostati nella memoria
        clients.put("example@example.com", new User("exampleUser", "example@example.com", List.of("Azione", "Fantastico") , "asd123"));
        clients.put("test@test.com", new Supervisor("testUser", "test@test.com", List.of("Azione", "Fantastico"), "asd123"));
    }

    /**Controlla se l'email o l'username esistono già.
     Se l'email è già presente → throw new EmailAlreadyInUseException();
     Se l'username è già presente → throw new UsernameAlreadyInUseException();
     Altrimenti, aggiunge il nuovo utente alla Map come User.*/
    @Override
    public void insertClient(Login registration) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        for (Client client : clients.values()) {
            //è client che cambia ogni volta valore che poi verrà confrontato con registration.getEmail
            if (client.getEmail().equals(registration.getEmail())) {
                throw new EmailAlreadyInUseException();
            }
            if (client.getUsername().equals(registration.getUsername())) {
                throw new UsernameAlreadyInUseException();
            }
        }
        //viene messa prima la key e poi il valore dentro la map
        clients.put(registration.getEmail(), new User(registration.getUsername(), registration.getEmail(), new ArrayList<>(), registration.getPassword()));
    }

    /**Cerca un utente nella Map usando l'email.
     Se non lo trova, lancia un'eccezione UserDoesNotExistException.*/
    @Override
    public Client loadClient(Login login) throws UserDoesNotExistException {
        Client client = clients.get(login.getEmail());
        if (client == null) {
            throw new UserDoesNotExistException();
        }
        return client;
    }

    /**Scansiona tutti gli utenti nella Map per trovare quello con lo username richiesto.
     Se lo trova, lo restituisce; altrimenti, lancia UserDoesNotExistException.*/
    @Override
    public Client retrieveClientByUsername(String username) throws UserDoesNotExistException {
        for (Client client : clients.values()) {
            if (client.getUsername().equals(username)) {
                return client;
            }
        }
        throw new UserDoesNotExistException();
    }

    /**Controlla se l'utente esiste nella Map usando l'email.
     Se non esiste, lancia UserDoesNotExistException.
     Se esiste, ritorna la password.*/
    @Override
    public String getPasswordByEmail(String email) throws UserDoesNotExistException {
        Client client = clients.get(email);
        if (client == null) {
            throw new UserDoesNotExistException();
        }
        return clients.get(email).getPassword(); // Simulazione di password
    }

    /**Controlla se l'utente esiste nella Map.
     Se esiste, aggiorna la lista delle sue preferenze di genere*/
    @Override
    public void updateGenreClient(Client client) {
        Client existingClient = clients.get(client.getEmail());
        if (existingClient != null) {
            existingClient.setPreferences(client.getPreferences());
        }
    }

    /**Scansiona la Map per controllare se:
     L'email è già registrata → throw new EmailAlreadyInUseException();
     Lo username è già registrato → throw new UsernameAlreadyInUseException();*/
    @Override
    public void tryCredentialExisting(Login login) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        for (Client client : clients.values()) {
            if (client.getEmail().equals(login.getEmail())) {
                throw new EmailAlreadyInUseException();
            }
            if (client.getUsername().equals(login.getUsername())) {
                throw new UsernameAlreadyInUseException();
            }
        }
    }
}

/**A cosa serve questa classe?
 Simula un database di utenti in memoria, utile per testare senza dover usare un database vero.
 Implementa l'interfaccia ClientDAO, quindi può essere facilmente sostituita da un'implementazione che interagisce con un database reale.
 Vantaggi
 ✅ Non ha bisogno di un database per funzionare.
 ✅ Utile per testare la logica di registrazione e login.
 ✅ Implementa gestione degli errori con eccezioni.*/

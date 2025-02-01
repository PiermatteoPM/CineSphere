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

    static {
        // Dati preimpostati nella memoria
        clients.put("example@example.com", new User("exampleUser", "example@example.com", List.of("Azione", "Fantastico") , "asd123"));
        clients.put("test@test.com", new Supervisor("testUser", "test@test.com", List.of("Azione", "Fantastico"), "asd123"));
    }

    @Override
    public void insertClient(Login registration) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        for (Client client : clients.values()) {
            if (client.getEmail().equals(registration.getEmail())) {
                throw new EmailAlreadyInUseException();
            }
            if (client.getUsername().equals(registration.getUsername())) {
                throw new UsernameAlreadyInUseException();
            }
        }
        clients.put(registration.getEmail(), new User(registration.getUsername(), registration.getEmail(), new ArrayList<>(), registration.getPassword()));
    }

    @Override
    public Client loadClient(Login login) throws UserDoesNotExistException {
        Client client = clients.get(login.getEmail());
        if (client == null) {
            throw new UserDoesNotExistException();
        }
        return client;
    }

    @Override
    public Client retrieveClientByUsername(String username) throws UserDoesNotExistException {
        for (Client client : clients.values()) {
            if (client.getUsername().equals(username)) {
                return client;
            }
        }
        throw new UserDoesNotExistException();
    }

    @Override
    public String getPasswordByEmail(String email) throws UserDoesNotExistException {
        Client client = clients.get(email);
        if (client == null) {
            throw new UserDoesNotExistException();
        }
        return clients.get(email).getPassword(); // Simulazione di password
    }

    @Override
    public void updateGenreClient(Client client) {
        Client existingClient = clients.get(client.getEmail());
        if (existingClient != null) {
            existingClient.setPreferences(client.getPreferences());
        }
    }

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

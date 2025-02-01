package engineering.test;

import engineering.exceptions.*;
import org.junit.Assert;
import org.junit.Test;
import engineering.dao.ClientDAOJSON;
import model.Login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestForRegistration {

    private final String username = "testUser";
    private final String email = "testUser@gmail.com";
    private final List<String> generiUser  = new ArrayList<>(List.of("Drammatico", "Azione"));

    /**
     * Testo che l'inserimento di un utente avvenga con successo se tutti i valori sono validi (email e username generati random)
     */
    @Test
    public void testRegistrationWithValidData() {
        ClientDAOJSON clientDAO = new ClientDAOJSON();
        int res = -1;

        String randomUsername = generateRandomUsername();
        String randomEmail = generateRandomEmail();
        Login validLogin = new Login(randomUsername, randomEmail, "testPassword", Arrays.asList("Drammatico", "Azione"));

        try {
            clientDAO.insertClient(validLogin);

            if (clientDAO.loadClient(validLogin) != null) {
                res = 1;
            }

        } catch (EmailAlreadyInUseException | UsernameAlreadyInUseException | UserDoesNotExistException e) {
            res = 0;
        }
        Assert.assertEquals(1, res);
    }

    /**
     * Testo che l'inserimento restituisca l'eccezione "EmailAlreadyInUseException" se provo a registrare un utente
     */
    @Test
    public void testRegistrationWithEmailAlreadyExists() {

        ClientDAOJSON clientDAO = new ClientDAOJSON();
        int res;
        try {
            clientDAO.insertClient(new Login(username, email, username, generiUser));
        } catch (UsernameAlreadyInUseException | EmailAlreadyInUseException e) {
            e.fillInStackTrace(); // Ignoro
        }

        Login existingEmailLogin = new Login("nuovoTestUsername1616", email, "password123", generiUser);

        try {
            clientDAO.insertClient(existingEmailLogin);
            res = 0; // Se la registrazione riesce, il test fallisce

        } catch (EmailAlreadyInUseException e) {
            res = 1; // Se si verifica l'eccezione corretta, il test ha successo
        } catch (UsernameAlreadyInUseException e) {
            res = 0; // Se si verifica un'eccezione diversa, il test fallisce
        }
        Assert.assertEquals(1, res);
        if (res == 1) {
            System.out.println("Test passed: EmailAlreadyInUseException was correctly thrown.");
        }
    }

    /**
     * Testo che l'inserimento restituisca l'eccezione "UsernameAlreadyInUseException" se provo a registrare un utente con un
     * nome utente già in uso, in questo caso admin(Un supervisore già presente nel file system).
     * La mail invece viene generata random
     */
    @Test
    public void testRegistrationWithExistingUsername() {
        ClientDAOJSON clientDAO = new ClientDAOJSON();
        int res;
        try {
            clientDAO.insertClient(new Login(username, email, username, generiUser));
        } catch (UsernameAlreadyInUseException | EmailAlreadyInUseException e) {
            e.fillInStackTrace(); // Ignoro
        }

        Login existingUsernameLogin = new Login(username, "testEmail@example.com", username, generiUser);

        try {
            clientDAO.insertClient(existingUsernameLogin);
            res = 0; // Se la registrazione riesce, il test fallisce

        } catch (UsernameAlreadyInUseException e) {
            res = 1; // Se si verifica l'eccezione corretta, il test ha successo

        } catch (EmailAlreadyInUseException | IllegalArgumentException e) {
            res = 0; // Se si verifica un'eccezione diversa, il test fallisce
        }
        Assert.assertEquals(1, res);

        if (res == 1) {
            System.out.println("Test passed: UsernameAlreadyInUseException was correctly thrown.");
        }
    }

    private String generateRandomUsername() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "testUsername" + timestamp.substring(timestamp.length() - 3);
    }

    private String generateRandomEmail() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "testEmail" + timestamp.substring(timestamp.length() - 3) + "@test.com";
    }
}
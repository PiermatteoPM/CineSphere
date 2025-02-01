package engineering.bean;

import engineering.exceptions.InvalidEmailException;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.List;
/**è astratta e non può essere istanziata direttamente, ma vedrai dopo che questa classe viene
 * estesa da supervisorBean e UserBean, che sono le vere classi che vangono poi usate.
 * Gli verrà assegnata una delle due istanziazioni sopracitate.*/
public abstract class ClientBean {

    private String username;
    private String email;
    private List<String> preferences;

    protected boolean supervisor;
    /**contruttori protected, li può invocare solo chi estende la classe, oltre che la classe stessa*/
    protected ClientBean() {
    }

    protected ClientBean(String username, String email, List<String> preferences) throws InvalidEmailException {
        setUsername(username);
        setEmail(email);
        setPreferences(preferences);
    }
    /**getter e setter*/

    /**set dell'email con convalida prima, altrimenti porta errore*/
    public void setEmail(String email) throws InvalidEmailException {
        if(checkMailCorrectness(email)){
            this.email = email;
        } else {
            throw new InvalidEmailException();
        }
    }
    public String getEmail() {
        return email;
    }


    public void setUsername(String nome) {
        this.username = nome;
    }
    public String getUsername() {
        return username;
    }


    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }
    public List<String> getPreferences() {
        return preferences;
    }


    public boolean isSupervisor() {
        return supervisor;
    }

    /** Verifica se la email inserita rispetta i canoni per essere una email */
    private boolean checkMailCorrectness(String email) {
        return EmailValidator.getInstance().isValid(email);  //ce un inport sopra
    }
}

/**Le Bean Class (come ClientBean) sono oggetti che rappresentano i dati di un'entità nel sistema.
 🔹 Perché servono?
 ✅ Separano dati e logica di business → Non contengono logica complessa, solo dati e metodi di accesso.
 ✅ Sono riutilizzabili → Possono essere usati da più parti dell’applicazione.
 ✅ Facilitano la comunicazione tra layer → Usate per trasferire dati tra Controller e View.*/

/**Perché i Controller utilizzano i Bean?
 I Controller gestiscono la logica dell’applicazione e interagiscono con il modello dati.
 1️⃣ Ricevono dati dalla View (GUI, CLI, API).
 2️⃣ Usano i Bean per rappresentare questi dati.
 3️⃣ Passano i Bean al livello DAO (Database) per salvarli o recuperarli.
 4️⃣ Restituiscono i Bean alla View per mostrare i risultati.*/



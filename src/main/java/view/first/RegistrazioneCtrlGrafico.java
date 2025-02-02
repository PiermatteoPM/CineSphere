package view.first;

import controller.applicativo.RegistrazioneCtrlApplicativo;
import engineering.bean.*;
import engineering.exceptions.*;
import engineering.others.Printer;
import view.first.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.text.*;

import java.net.URL;
import java.util.*;

public class RegistrazioneCtrlGrafico implements Initializable {

    // ---------Nodi interfaccia----------
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confPassword;
    @FXML
    private Text errorField;

    @FXML
    private CheckBox azione;
    @FXML
    private CheckBox avventura;
    @FXML
    private CheckBox animazione;
    @FXML
    private CheckBox biografico;
    @FXML
    private CheckBox commedia;
    @FXML
    private CheckBox poliziesco;
    @FXML
    private CheckBox documentario;
    @FXML
    private CheckBox drammatico;
    @FXML
    private CheckBox perFamiglie;
    @FXML
    private CheckBox fantastico;
    @FXML
    private CheckBox noir;
    @FXML
    private CheckBox giocoAPremiTelevisivo;
    @FXML
    private CheckBox storico;
    @FXML
    private CheckBox horror;
    @FXML
    private CheckBox musica;
    @FXML
    private CheckBox musical;
    @FXML
    private CheckBox giallo;
    @FXML
    private CheckBox telegiornale;
    @FXML
    private CheckBox reality;
    @FXML
    private CheckBox sentimentale;
    @FXML
    private CheckBox fantascienza;
    @FXML
    private CheckBox cortometraggio;
    @FXML
    private CheckBox sportivo;
    @FXML
    private CheckBox talkShow;
    @FXML
    private CheckBox thriller;
    @FXML
    private CheckBox guerra;
    @FXML
    private CheckBox western;

    private List<CheckBox> checkBoxList;

    private SceneController sceneController;

    public void setAttributes(SceneController sceneController) {
        // Deve avere un userBean per compilare tutte le informazioni
        this.sceneController = sceneController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkBoxList = Arrays.asList(azione, avventura, animazione, biografico, commedia, poliziesco, documentario,
                drammatico, perFamiglie, fantastico, noir, giocoAPremiTelevisivo, storico, horror,
                musica, musical, giallo, telegiornale, reality, sentimentale, fantascienza,
                cortometraggio, sportivo, talkShow, thriller, guerra, western);
    }

    /**
     * Gestisce l'evento di clic sul pulsante di ritorno (back).
     * Chiude la finestra corrente e avvia la schermata di login.
     */
    @FXML
    protected void onBackClick(ActionEvent event){
        sceneController.goBack(event);
    }

    /** Gestisce l'evento di clic sul pulsante di registrazione.
     * Se la registrazione ha successo viene ottenuto lo UserBean dal controller Applicativo e
     * si imposta la scena sulla Home Page */
    @FXML
    protected void onRegisterClick(ActionEvent event){
        LoginBean regBean = new LoginBean();
        getData(regBean); /*raccoglie e valida i dati dell'utente dalla GUI e li salva in LoginBean. Se i dati sono validi, regBean viene passato al controller applicativo (RegistrazioneCtrlApplicativo) per registrare l'utente*/

        if (regBean.getEmail() != null && !regBean.getEmail().isEmpty()) {

            RegistrazioneCtrlApplicativo registrazioneCtrlApplicativo = new RegistrazioneCtrlApplicativo();

            try {
                ClientBean clientBean = new UserBean(regBean.getEmail());
                registrazioneCtrlApplicativo.registerUser(regBean, clientBean);

                Printer.logPrint("GUI Registration: User registered successfully");

                /* --------------- Mostro la home page -------------- */
                sceneController.goToScene(event, FxmlFileName.HOME_PAGE_FXML, clientBean);

            } catch (EmailAlreadyInUseException | UsernameAlreadyInUseException | InvalidEmailException e) {
                showError(e.getMessage());
            }
        }
    }

    /** Ottiene i dati inseriti dall'utente dalla GUI e restituisce un oggetto RegistrationBean. */
    private void getData(LoginBean loginBean) {

        String username = name.getText().trim(); //.trim() Rimuove gli spazi da inizio e fine stringa
        String userEmail = email.getText().trim();

        String userPassword = password.getText().trim();
        String userConfPw = confPassword.getText().trim();

        // Recupero preferenze aggiornate
        List<String> preferences = GenreManager.retrieveCheckList(checkBoxList);

        if (username.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userConfPw.isEmpty()) {
            showError("The fields are empty");
        } else if (!verificaPassword(userPassword, userConfPw)) {
            showError("Passwords do not match");
        } else {
            try{
                loginBean.setUsername(username);
                loginBean.setEmail(userEmail);
                loginBean.setPassword(userPassword);
                loginBean.setPreferences(preferences);
            } catch (InvalidEmailException e){
                showError(e.getMessage());
            }

        }
    }

    /** Mostra un messaggio di errore nell'interfaccia utente */
    private void showError(String message) {
        //
        errorField.setText(message);
        errorField.setVisible(true);
    }

    /** Verifica se le password inserite dall'utente coincidono. */
    private boolean verificaPassword(String password, String confermaPassword) {
        return password.equals(confermaPassword);
    }
}

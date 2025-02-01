package view.first;

import controller.applicativo.AccountCtrlApplicativo;
import engineering.bean.*;
import engineering.others.Printer;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import view.first.utils.*;

import java.net.URL;
import java.util.*;

public class AccountCtrlGrafico<T extends ClientBean> implements Initializable {

    @FXML
    public Button saveButton;
    @FXML
    private Label usernameText;
    @FXML
    private Label emailText;

    @FXML
    private TableView<CollectionBean> collectionTable;
    @FXML
    private TableColumn<CollectionBean, String> collectionNameColumn;
    @FXML
    private TableColumn<CollectionBean, List<String>> genreColumn;
    @FXML
    private TableColumn<CollectionBean, Boolean> approveColumn;
    @FXML
    private TableColumn<CollectionBean, String> linkColumn;

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

    private T clientBean;
    private List<CheckBox> checkBoxList;
    private ObservableList<CollectionBean> observableList;
    private SceneController sceneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkBoxList = Arrays.asList(azione, avventura, animazione, biografico, commedia, poliziesco, documentario,
                drammatico, perFamiglie, fantastico, noir, giocoAPremiTelevisivo, storico, horror,
                musica, musical, giallo, telegiornale, reality, sentimentale, fantascienza,
                cortometraggio, sportivo, talkShow, thriller, guerra, western);
    }
    public void showUserInfo(){
        usernameText.setText(clientBean.getUsername());
        emailText.setText(clientBean.getEmail());
        List<String> preferences = clientBean.getPreferences();

        // Imposta le CheckBox in base alle preferenze del client
        GenreManager.setCheckList(preferences,checkBoxList);
    }

    public void setAttributes(T clientBean, SceneController sceneController) { // T è una classe che estende ClientBean -> UserBean o SupervisorBean
        this.clientBean = clientBean;
        this.sceneController = sceneController;

        Printer.logPrint(String.format("GUI Account setAttributes: %s", clientBean));

        // Inizializza i dati nella GUI
        showUserInfo();
        // Recupera e visualizza le collection dell'utente
        retriveCollection();
    }

    /** Recupera tutte le collection dell'utente */
    public void retriveCollection() {
        AccountCtrlApplicativo accountCtrlApplicativo = new AccountCtrlApplicativo();

        // Recupera le collection dell'utente
        List<CollectionBean> userCollectionss = accountCtrlApplicativo.retrieveCollectionss(clientBean);

        // Imposto la struttura delle colonne della Table View
        List<TableColumn<CollectionBean, ?>> columns = Arrays.asList(collectionNameColumn, linkColumn, approveColumn, genreColumn);
        List<String> nameColumns = Arrays.asList("collectionName", "link", "approved", "collectionGenre");
        TableManager.setColumnsTableView(columns, nameColumns);
        linkColumn.setCellFactory(button -> new SingleButtonTableCell());
        approveColumn.setCellFactory(button -> new ImageButtonTableCell());

        observableList = FXCollections.observableArrayList(userCollectionss);
        collectionTable.setItems(observableList);
    }

    /** Gestisce il click sul pulsante Salva */
    @FXML
    public void onSaveClick(ActionEvent event) {
        // Recupera le preferenze aggiornate dalle CheckBox
        List<String> preferences = GenreManager.retrieveCheckList(checkBoxList);

        // Aggiorna le preferenze nel bean del cliente
        clientBean.setPreferences(preferences);

        AccountCtrlApplicativo accountCtrlApplicativo = new AccountCtrlApplicativo();
        // Aggiorna le preferenze nel backend
        accountCtrlApplicativo.updateGenreUser(clientBean);

        // Mostra una notifica pop-up
        sceneController.textPopUp(event, MessageString.UPDATED_PREFERNCES, false);
    }

    /** Gestisce il click sul pulsante Indietro */
    @FXML
    public void onBackClick(ActionEvent event) {
        // Torna alla schermata precedente
        sceneController.goBack(event);
    }

    /** Gestisce il click sul pulsante Aggiungi Collection */
    @FXML
    public void addCollectionClick(ActionEvent event) {
        // Passa alla schermata di caricamento della collection, passando il bean del client
        sceneController.goToScene(event, FxmlFileName.UPLOAD_COLLECTION_FXML, clientBean, observableList);
    }
    /** Gestisce il click sul pulsante Logout */
    @FXML
    public void onLogoutClick(ActionEvent event) {
        clientBean = null; // Resetta il bean client (opzionale)
        sceneController.goToSceneForLogout(event, FxmlFileName.LOGIN_FXML);
    }
}

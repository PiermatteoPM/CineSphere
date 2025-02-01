package view.first;

import controller.applicativo.AddCollectionCtrlApplicativo;
import engineering.bean.*;
import engineering.exceptions.*;

import engineering.others.Printer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;

import view.first.utils.*;

import java.net.URL;
import java.util.*;


public class AddCollectionCtrlGrafico<T extends ClientBean> implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField title;
    @FXML
    private TextField link;

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

    private ObservableList<CollectionBean> observableList;
    private T clientBean;
    private CollectionBean collectionBean;
    private SceneController sceneController;

    public void setAttributes(T clientBean, ObservableList<CollectionBean> observableList, SceneController sceneController) {
        // Deve avere un userBean per compilare tutte le informazioni
        this.clientBean = clientBean;
        this.sceneController = sceneController;
        this.observableList = observableList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkBoxList = Arrays.asList(azione, avventura, animazione, biografico, commedia, poliziesco, documentario,
                drammatico, perFamiglie, fantastico, noir, giocoAPremiTelevisivo, storico, horror,
                musica, musical, giallo, telegiornale, reality, sentimentale, fantascienza,
                cortometraggio, sportivo, talkShow, thriller, guerra, western);
    }

    @FXML
    public void onBackClick(ActionEvent event) {
        sceneController.goBack(event);
    }

    /** Click sul tasto carica Collection*/
    @FXML
    public void onUploadClick(ActionEvent event) {

        try{
            getDate();

            if(collectionBean != null){
                Printer.logPrint(String.format("GUI AddCollection: upload Collection: %s, nome: %s, genre: %s", collectionBean, collectionBean.getCollectionName(), collectionBean.getCollectionGenre()));

                // Invocazione metodo controller Applicativo che in teoria è static
                AddCollectionCtrlApplicativo addCollectionControllerApplicativo = new AddCollectionCtrlApplicativo();
                addCollectionControllerApplicativo.insertCollection(collectionBean);

                if(clientBean.isSupervisor()){
                    sceneController.textPopUp(event, MessageString.ADDED_COLLECTION,true);
                }
                else{
                    sceneController.textPopUp(event,MessageString.ADDED_PENDING_COLLECTION,true);
                }
                Printer.logPrint("GUI AddCollection: Collection Added");
            }
        } catch (CollectionLinkAlreadyInUseException | LinkIsNotValidException | CollectionNameAlreadyInUseException e){
            showError(e.getMessage());
        }
    }

    private void getDate() throws LinkIsNotValidException {

        String linkCollection = link.getText();
        String titolo = title.getText();

        List<String> genre = GenreManager.retrieveCheckList(checkBoxList);

        //Controllo sui campi vuoti
        if( linkCollection.isEmpty() || titolo.isEmpty() ){
            showError("There are empty fields!");
        } else if(genre.isEmpty()) {
            showError("Please enter at least one genre!");
        } else {
            collectionBean = new CollectionBean(clientBean.getEmail(), clientBean.getUsername(), titolo, linkCollection, genre, clientBean.isSupervisor());
            collectionBean.setId("");

            // Utilizzato esclusivamente per aggiornare la tabella nell'account utente
            if(observableList != null){ // è null solo se l'utente ha cliccato add Button da Home Page
                observableList.add(collectionBean); //Aggiungi
            }
        }
    }

    private void showError(String message) {
        // Mostra un messaggio di errore nell'interfaccia utente.
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

}

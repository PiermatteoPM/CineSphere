package view.first;

import engineering.bean.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.first.utils.*;

import java.net.URL;
import java.util.*;

public class FilterCtrlGrafico implements Initializable {

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
    boolean applyFilter = false;

    private CollectionBean collectionBean;
    private List<CheckBox> checkBoxList;
    private HomePageCtrlGrafico<?> homeController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkBoxList = Arrays.asList(azione, avventura, animazione, biografico, commedia, poliziesco, documentario,
                drammatico, perFamiglie, fantastico, noir, giocoAPremiTelevisivo, storico, horror,
                musica, musical, giallo, telegiornale, reality, sentimentale, fantascienza,
                cortometraggio, sportivo, talkShow, thriller, guerra, western);
    }

    public void setHomeInstance(HomePageCtrlGrafico<?> homeController){
        this.homeController = homeController;
    }

    /** Viene utilizzata da sceneController per impostare lo userBean e l'istanza di Scene controller da utilizzare */
    public void setAttributes(CollectionBean collectionBean) {
        // Deve avere un userBean per compilare tutte le informazioni
        this.collectionBean = collectionBean;
        setData();
    }

    @FXML
    private void onApplyClick(ActionEvent event) {
        // Devo modificare i campi del CollectionBean
        List<String> genre = GenreManager.retrieveCheckList(checkBoxList);


        collectionBean.setCollectionGenre(genre);

        applyFilter = !checkEmptyFields();
        homeController.setFilterApplied(applyFilter);
        homeController.onSearchCollectionClick();

        // Chiudi il popup
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void setData(){
        List<String> preferences = collectionBean.getCollectionGenre();

        if(preferences != null){
            GenreManager.setCheckList(preferences,checkBoxList);
        }

    }

    private boolean checkEmptyFields() {
        return checkBoxList.stream().noneMatch(CheckBox::isSelected);
    }

    @FXML
    public void onResetClick() {
        List<String> genre = new ArrayList<>();


        collectionBean.setCollectionGenre(genre);

        setData();
        applyFilter = false;
    }
}

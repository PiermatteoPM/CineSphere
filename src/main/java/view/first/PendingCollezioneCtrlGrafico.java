package view.first;

import engineering.bean.NoticeBean;
import engineering.others.Printer;
import javafx.collections.ObservableList;
import view.first.utils.*;

import controller.applicativo.PendingCollezioneCtrlApplicativo;
import engineering.bean.CollezioneBean;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;
public class PendingCollezioneCtrlGrafico implements Initializable {

    @FXML
    private TableColumn<CollezioneBean, List<String>> genreColumn;
    @FXML
    private TableColumn<CollezioneBean, Boolean> approveColumn;
    @FXML
    private TableColumn<CollezioneBean, String> collezioneNameColumn;
    @FXML
    private TableColumn<CollezioneBean, Boolean> usernameColumn;
    @FXML
    private TableColumn<CollezioneBean, String> linkColumn;
    @FXML
    private TableView<CollezioneBean> collezioneTable;

    private SceneController sceneController;
    private ObservableList<CollezioneBean> observableList;

    public void setAttributes(SceneController sceneController) {
        // Deve avere un userBean per compilare tutte le informazioni
        this.sceneController = sceneController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Printer.logPrint("GUI PendingCollezione: Inizio gestione collezione: ");

        // Recupera tutte le collezione pending, metodo pull
        PendingCollezioneCtrlApplicativo allCollezioneController = new PendingCollezioneCtrlApplicativo();
        List<CollezioneBean> collezionesPending = allCollezioneController.retrieveColleziones(); // Vengono recuperate tutte le collezione pending

        List<TableColumn<CollezioneBean, ?>> columns = Arrays.asList(collezioneNameColumn, linkColumn, usernameColumn, genreColumn); // Tutte le colonne della table view
        List<String> nameColumns = Arrays.asList("collezioneName", "link", "username","collezioneGenre");
        TableManager.setColumnsTableView(columns, nameColumns);

        // Aggiungi la colonna con bottoni "Approve" o "Reject"
        approveColumn.setCellFactory(button -> new DoubleButtonTableCell(this));
        linkColumn.setCellFactory(button -> new SingleButtonTableCell());

        TableManager tableManager = new TableManager();
        observableList = tableManager.handler(collezioneTable,collezionesPending);
    }

    @FXML
    public void onBackClick(ActionEvent event) {
        sceneController.goBack(event);
    }

    /** Public perché deve essere chiamata da DoubleButtonTableCell, è l'azione che viene compiuta al click del bottone Accept o Reject */
    public void handlerButton(CollezioneBean collezioneBean, boolean approve) {

        // Logica per gestire l'approvazione o il rifiuto della collezione
        PendingCollezioneCtrlApplicativo pendingCollezioneCtrlApplicativo = new PendingCollezioneCtrlApplicativo();

        String title;
        String body;

        if (approve) {
            Printer.logPrint(String.format("Approving collezione: %s", collezioneBean.getCollezioneName()));

            title = "Approved";
            body = String.format("Your collezione %s is approved!", collezioneBean.getCollezioneName());

            // Approva Collezione
            pendingCollezioneCtrlApplicativo.approveCollezione(collezioneBean);
        } else {
            Printer.logPrint(String.format("Rejecting collezione: %s", collezioneBean.getCollezioneName()));

            title = "Rejected";
            body = String.format("Your collezione %s is rejected!", collezioneBean.getCollezioneName());

            // Rifiuta Collezione
            pendingCollezioneCtrlApplicativo.rejectCollezione(collezioneBean);

        }

        NoticeBean noticeBean = new NoticeBean(title, body, collezioneBean.getEmail());
        pendingCollezioneCtrlApplicativo.sendNotification(noticeBean);
        Printer.print("Sending: ");
        Printer.print(String.valueOf(noticeBean));
        observableList.remove(collezioneBean);
    }
}
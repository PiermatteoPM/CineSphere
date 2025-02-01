package view.first;

import engineering.bean.NoticeBean;
import engineering.others.Printer;
import javafx.collections.ObservableList;
import view.first.utils.*;

import controller.applicativo.PendingCollectionCtrlApplicativo;
import engineering.bean.CollectionBean;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;
public class PendingCollectionCtrlGrafico implements Initializable {

    @FXML
    private TableColumn<CollectionBean, List<String>> genreColumn;
    @FXML
    private TableColumn<CollectionBean, Boolean> approveColumn;
    @FXML
    private TableColumn<CollectionBean, String> collectionNameColumn;
    @FXML
    private TableColumn<CollectionBean, Boolean> usernameColumn;
    @FXML
    private TableColumn<CollectionBean, String> linkColumn;
    @FXML
    private TableView<CollectionBean> collectionTable;

    private SceneController sceneController;
    private ObservableList<CollectionBean> observableList;

    public void setAttributes(SceneController sceneController) {
        // Deve avere un userBean per compilare tutte le informazioni
        this.sceneController = sceneController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Printer.logPrint("GUI PendingCollection: Inizio gestione collection: ");

        // Recupera tutte le collection pending, metodo pull
        PendingCollectionCtrlApplicativo allCollectionController = new PendingCollectionCtrlApplicativo();
        List<CollectionBean> collectionssPending = allCollectionController.retrieveCollectionss(); // Vengono recuperate tutte le collection pending

        List<TableColumn<CollectionBean, ?>> columns = Arrays.asList(collectionNameColumn, linkColumn, usernameColumn, genreColumn); // Tutte le colonne della table view
        List<String> nameColumns = Arrays.asList("collectionName", "link", "username","collectionGenre");
        TableManager.setColumnsTableView(columns, nameColumns);

        // Aggiungi la colonna con bottoni "Approve" o "Reject"
        approveColumn.setCellFactory(button -> new DoubleButtonTableCell(this));
        linkColumn.setCellFactory(button -> new SingleButtonTableCell());

        TableManager tableManager = new TableManager();
        observableList = tableManager.handler(collectionTable,collectionssPending);
    }

    @FXML
    public void onBackClick(ActionEvent event) {
        sceneController.goBack(event);
    }

    /** Public perché deve essere chiamata da DoubleButtonTableCell, è l'azione che viene compiuta al click del bottone Accept o Reject */
    public void handlerButton(CollectionBean collectionBean, boolean approve) {

        // Logica per gestire l'approvazione o il rifiuto della collection
        PendingCollectionCtrlApplicativo pendingCollectionCtrlApplicativo = new PendingCollectionCtrlApplicativo();

        String title;
        String body;

        if (approve) {
            Printer.logPrint(String.format("Approving collection: %s", collectionBean.getCollectionName()));

            title = "Approved";
            body = String.format("Your collection %s is approved!", collectionBean.getCollectionName());

            // Approva Collection
            pendingCollectionCtrlApplicativo.approveCollection(collectionBean);
        } else {
            Printer.logPrint(String.format("Rejecting collection: %s", collectionBean.getCollectionName()));

            title = "Rejected";
            body = String.format("Your collection %s is rejected!", collectionBean.getCollectionName());

            // Rifiuta Collection
            pendingCollectionCtrlApplicativo.rejectCollection(collectionBean);

        }

        NoticeBean noticeBean = new NoticeBean(title, body, collectionBean.getEmail());
        pendingCollectionCtrlApplicativo.sendNotification(noticeBean);
        Printer.print("Sending: ");
        Printer.print(String.valueOf(noticeBean));
        observableList.remove(collectionBean);
    }
}
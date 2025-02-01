package view.first;

import controller.applicativo.HomePageCtrlApplicativo;

import engineering.bean.*;
import engineering.others.Printer;
import engineering.pattern.observer.Observer;
import engineering.pattern.observer.*;

import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import model.Collection;
import view.first.utils.*;

import java.net.URL;
import java.util.*;

/**
 * Questa classe rappresenta il controller grafico della home page dell'applicazione,
 * fungendo da Concrete Observer nel contesto del pattern Observer. Si occupa di gestire
 * la visualizzazione delle collection, la gestione delle notifiche e l'interazione
 * con l'utente tramite una GUI JavaFX.
 *
 * @param <T> Tipo generico che estende {@link ClientBean}, rappresenta il tipo di bean utente
 *            che sarà gestito da questo controller.
 */
public class HomePageCtrlGrafico<T extends ClientBean> implements Initializable, Observer {

    @FXML
    private ContextMenu contextMenu;
    @FXML
    private TextField searchText;
    @FXML
    private TableView<CollectionBean> collectionTable;

    @FXML
    private TableColumn<CollectionBean, String> collectionNameColumn;
    @FXML
    private TableColumn<CollectionBean, List<String>> genreColumn;
    @FXML
    private TableColumn<CollectionBean, String> usernameColumn;
    @FXML
    private TableColumn<CollectionBean, String> linkColumn;

    @FXML
    private Button manager;
    @FXML
    private Button account;
    @FXML
    private Button addButton;
    @FXML
    private Button menu;

    private boolean filterApplied = false;

    private T clientBean;
    private SceneController sceneController;
    private final CollectionBean filterCollection = new CollectionBean(); // Contiene gli attributi secondo i quali filtrare le collectionss

    /** OBSERVER */
    private CollectionCollectionss collectionCollection; /** ISTANZA DEL MODEL (CONCRETE SUBJECT) */
    private List<Collection> collectionss = new ArrayList<>(); /** Observer state -> Model ma serve cosi per il pattern */
    private List<CollectionBean> collectionssBean = new ArrayList<>();

    /**
     * Inizializza il controller grafico della home page.
     *
     * @param location   Il percorso relativo o assoluto dell'oggetto alla fine della risoluzione
     *                   del URL o {@code null} se l'oggetto non ha un URL associato.
     * @param resources  Le risorse di localizzazione o {@code null} se l'oggetto non è stato localizzato.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Recupera tutte le collection
        Printer.logPrint("GUI Home Page");

        List<TableColumn<CollectionBean, ?>> columns = Arrays.asList(collectionNameColumn, linkColumn, usernameColumn, genreColumn);
        List<String> nameColumns = Arrays.asList("collectionName", "link", "username", "collectionGenre");
        linkColumn.setCellFactory(button -> new SingleButtonTableCell());

        /* BYPASSIAMO MVC PER PATTERN OBSERVER */
        collectionCollection = CollectionCollectionss.getInstance();
        collectionCollection.attach(this);

        /* Metodo pull per ricevere i dati dal dao */
        HomePageCtrlApplicativo homePageController = new HomePageCtrlApplicativo();
        collectionssBean = homePageController.retriveCollectionssApproved(); // Recupera le collection approvate

        TableManager.setColumnsTableView(columns, nameColumns);   // Aggiorna i parametri della tabella
        TableManager.updateTable(collectionTable, collectionssBean);
    }

    /** Viene utilizzata da sceneController per impostare lo userBean e l'istanza di Scene controller da utilizzare */
    public void setAttributes(T clientBean, SceneController sceneController) {
        this.clientBean = clientBean;
        this.sceneController = sceneController;
        initializeField();

        Printer.logPrint(String.format("GUI HomePage setAttributes: %s", clientBean));
    }

    public void setFilterApplied(boolean applyFilter) {
        this.filterApplied = applyFilter;
    }

    /** UTILIZZATA PER IL PATTERN OBSERVER */
    @Override
    public void update() {
        /* Ricevo una lista di Collection (model) la invio all'applicativo per una traduzione da model a bean
        * Potevo anche far fare il getState() all'applicativo in una funzione apposita */
        collectionss = collectionCollection.getState();

        HomePageCtrlApplicativo homePageCtrlApplicativo = new HomePageCtrlApplicativo();
        collectionssBean = homePageCtrlApplicativo.getCollectionssBean(collectionss);

        if(!filterApplied){ // Se i filtri non sono applicati allora aggiorna la tabella altrimenti no
            TableManager.addInTable(collectionTable, collectionssBean);
        }
    }

    /**
     * Inizializza i componenti visivi della home page in base allo stato
     * dell'utente rappresentato dal clientBean.
     */
    private void initializeField() {
        if(clientBean == null){
            Printer.logPrint("GUI HomePage: Entered as a Guest");

            menu.setVisible(false);
            addButton.setVisible(false);
            manager.setVisible(false);
            account.setText("Registrati");

        } else { // UserBean o SupervisorBean
            Printer.logPrint(String.format("GUI HomePage: Entered as Supervisor: %b", clientBean.isSupervisor()));

            addButton.setVisible(true);
            manager.setVisible(clientBean.isSupervisor());
            account.setText(clientBean.getUsername());
            menu.setVisible(!clientBean.isSupervisor());
        }
    }
    @FXML
    protected void onAccountClick(ActionEvent event) {
        if(clientBean == null){ // Utente Guest
            sceneController.goToScene(event, FxmlFileName.REGISTRATION_FXML);
        } else { // Utente registrato
            sceneController.goToScene(event, FxmlFileName.ACCOUNT_FXML, clientBean);
        }
    }

    @FXML
    protected void onAddCollectionClick(ActionEvent event) {
        sceneController.goToScene(event, FxmlFileName.UPLOAD_COLLECTION_FXML, clientBean);
    }

    @FXML
    protected void onManagerClick(ActionEvent event) {
        sceneController.goToScene(event, FxmlFileName.MANAGER_COLLECTION_FXML);
    }

    @FXML
    public void onSearchCollectionClick() {
        if(!Objects.equals(searchText.getText(), "")){
            this.filterApplied = true;
        }

        filterCollection.setCollectionName(searchText.getText());

        HomePageCtrlApplicativo homePageController = new HomePageCtrlApplicativo();
        collectionssBean = homePageController.searchCollectionByFilters(filterCollection);        // Recupera le collection approvate

        TableManager.updateTable(collectionTable, collectionssBean);
        Printer.logPrint(String.format("GUI HomePage: search clicked: %s, nome: %s, genre: %s", filterCollection, filterCollection.getCollectionName(), filterCollection.getCollectionGenre()));
    }

    @FXML
    protected void onFilterClick(ActionEvent event) {
        sceneController.goToFilterPopUp(event, clientBean, filterCollection,this);
    }

    @FXML
    protected void showContextMenu() {
        contextMenu.getItems().clear(); // Rimuovi tutti gli elementi dal ContextMenu

        UserBean userBean = (UserBean) clientBean;

        if (userBean.getNotices() == null || userBean.getNotices().isEmpty()) {
            // Nessuna notifica disponibile
            MenuItem noNotificationItem = new MenuItem("No notifications available");
            noNotificationItem.setDisable(true); // Impedisce l'interazione con l'elemento
            contextMenu.getItems().add(noNotificationItem);
        } else {
            // Notifiche disponibili, aggiungi gli elementi del menu
            for (NoticeBean noticeBean : userBean.getNotices()) {
                contextMenu.getItems().add(createStyledMenuItem(noticeBean));
            }
        }

        // Converti le coordinate locali del menu in coordinate dello schermo
        double screenX = menu.localToScreen(menu.getBoundsInLocal()).getMinX();
        double screenY = menu.localToScreen(menu.getBoundsInLocal()).getMinY();

        // Mostra il ContextMenu accanto al bottone
        contextMenu.show(menu, screenX, screenY);
    }

    /** Nel caso viene premuto il pulsante elimina */
    private void handleNoticeSelection(NoticeBean noticeBean) {
        List<NoticeBean> noticeBeanList = ((UserBean)clientBean).getNotices();
        HomePageCtrlApplicativo homePageCtrlApplicativo = new HomePageCtrlApplicativo();
        homePageCtrlApplicativo.removeNotice(noticeBean);
        noticeBeanList.remove(noticeBean);

        ((UserBean)clientBean).setNotices(noticeBeanList);
    }

    private MenuItem createStyledMenuItem(NoticeBean noticeBean) {
        // Crea una HBox con larghezza fissa per contenere il testo e il pulsante
        HBox hbox = new HBox();
        hbox.setPrefWidth(325); // Imposta la larghezza fissa desiderata

        // Crea un Text per il testo della notifica (supporta il wrapping)
        Text text = new Text(noticeBean.getTitle() + "\n" + noticeBean.getBody());
        text.setWrappingWidth(295); // Imposta la larghezza massima prima del wrapping

        // Aggiungi uno spazio vuoto tra il Text e il pulsante
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Crea un'ImageView per l'immagine del pulsante "delete"
        ImageView deleteImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/photo/delete.png"))));
        deleteImageView.setFitWidth(35);  // Imposta la larghezza desiderata dell'immagine
        deleteImageView.setFitHeight(35); // Imposta l'altezza desiderata dell'immagine

        // Crea un pulsante con l'ImageView
        Button deleteButton = new Button();
        deleteButton.setGraphic(deleteImageView);
        deleteButton.setOnAction(e -> handleNoticeSelection(noticeBean));

        // Rimuovi tutti gli altri stili associati al bottone
        deleteButton.getStyleClass().clear();

        // Imposta lo stile per il pulsante "delete"
        deleteButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-pref-height: 25px; -fx-pref-width: 25px; " +
                "-fx-min-width: -1; -fx-min-height: -1; -fx-background-radius: 50%; -fx-border-radius: 50%;");

        text.getStyleClass().clear();
        text.setStyle("-fx-font-size: 16px; -fx-fill: white;");

        // Aggiungi Text, spazio vuoto e pulsante alla HBox
        hbox.getChildren().addAll(text, spacer, deleteButton);
        hbox.setAlignment(Pos.CENTER_RIGHT);

        // Crea un MenuItem personalizzato con la HBox
        MenuItem menuItem = new MenuItem();
        menuItem.setGraphic(hbox);

        return menuItem;
    }
}

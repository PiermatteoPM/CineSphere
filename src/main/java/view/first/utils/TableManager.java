package view.first.utils;

import engineering.bean.CollectionBean;
import engineering.others.Printer;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TableManager {
    private boolean isUpdatingTableView = true;

    /** Associa a ciascuna colonna i relativi metodi get di CollectionBean
     * @param columns       è una lista di colonne, contiene le sole colonne semplici (no bottoni)
     * @param nameColumns   è una lista di stringhe, che viene utilizzata per recuperare i dati dai metodi get del CollectionBean
     */
    public static void setColumnsTableView(List<TableColumn<CollectionBean, ?>> columns, List<String> nameColumns) {

        // Collega i dati alle colonne della TableView
        int index = 0;
        for(TableColumn<CollectionBean, ? > column:columns){
            column.setCellValueFactory(new PropertyValueFactory<>(nameColumns.get(index)));
            index++;
        }
    }

    /**
     * @param collectionTable è la tabella vera e propria
     * @param collectionss     è la lista delle collection da rappresentare
     */
    public static void updateTable(TableView<CollectionBean> collectionTable, List<CollectionBean> collectionss) {

        List<CollectionBean> currentCollectionss = collectionTable.getItems();     // Ottenere la lista attuale di collection dalla TableView

        collectionss.removeAll(currentCollectionss);                              // Rimuove le collection già caricate, cosi da avere una lista di collection nuove

        ObservableList<CollectionBean> collectionData = FXCollections.observableArrayList(collectionss);
        collectionTable.setItems(collectionData);                               // Aggiornare la TableView con la lista aggiornata di collection
    }

    /**
     * @param collectionTable è la tabella vera e propria
     * @param collectionss     è la lista delle collection da rappresentare
     */
    public static void addInTable(TableView<CollectionBean> collectionTable, List<CollectionBean> collectionss) {
        List<CollectionBean> currentCollectionss = collectionTable.getItems(); // Ottenere la lista attuale di collection dalla TableView

        collectionss.removeIf(collection -> currentCollectionss.stream().anyMatch(currentCollection -> currentCollection.getLink().equals(collection.getLink())));
        currentCollectionss.addAll(collectionss);

        ObservableList<CollectionBean> collectionData = FXCollections.observableArrayList(currentCollectionss);
        collectionTable.setItems(collectionData); // Aggiornare la TableView con la lista aggiornata di collection
    }


    public ObservableList<CollectionBean> handler(TableView<CollectionBean> collectionTable, List<CollectionBean> collectionBeanList) {

        ObservableList<CollectionBean> observableList = FXCollections.observableList(collectionBeanList);
        collectionTable.setItems(observableList);

        // Aggiunta di un listener di modifica al ObservableList
        observableList.addListener((ListChangeListener<CollectionBean>) change -> {
            while (change.next()) {
                if (change.wasAdded() && isUpdatingTableView) { ///// non accade mai #########
                    Printer.logPrint(String.format("Elementi aggiunti: %s" , change.getAddedSubList()));
                    isUpdatingTableView = false;
                    collectionTable.getItems().addAll(change.getAddedSubList());
                    isUpdatingTableView = true;
                } else if (change.wasRemoved() && isUpdatingTableView) {
                    Printer.logPrint(String.format("Elementi rimossi: %s", change.getRemoved()));
                    isUpdatingTableView = false;
                    collectionTable.getItems().removeAll(change.getRemoved());
                    isUpdatingTableView = true;
                }
            }
        });

        return observableList;
    }

}

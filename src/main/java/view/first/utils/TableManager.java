package view.first.utils;

import engineering.bean.CollezioneBean;
import engineering.others.Printer;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TableManager {
    private boolean isUpdatingTableView = true;

    /** Associa a ciascuna colonna i relativi metodi get di CollezioneBean
     * @param columns       è una lista di colonne, contiene le sole colonne semplici (no bottoni)
     * @param nameColumns   è una lista di stringhe, che viene utilizzata per recuperare i dati dai metodi get del CollezioneBean
     */
    public static void setColumnsTableView(List<TableColumn<CollezioneBean, ?>> columns, List<String> nameColumns) {

        // Collega i dati alle colonne della TableView
        int index = 0;
        for(TableColumn<CollezioneBean, ? > column:columns){
            column.setCellValueFactory(new PropertyValueFactory<>(nameColumns.get(index)));
            index++;
        }
    }

    /**
     * @param collezioneTable è la tabella vera e propria
     * @param colleziones     è la lista delle collezione da rappresentare
     */
    public static void updateTable(TableView<CollezioneBean> collezioneTable, List<CollezioneBean> colleziones) {

        List<CollezioneBean> currentColleziones = collezioneTable.getItems();     // Ottenere la lista attuale di collezione dalla TableView

        colleziones.removeAll(currentColleziones);                              // Rimuove le collezione già caricate, cosi da avere una lista di collezione nuove

        ObservableList<CollezioneBean> collezioneData = FXCollections.observableArrayList(colleziones);
        collezioneTable.setItems(collezioneData);                               // Aggiornare la TableView con la lista aggiornata di collezione
    }

    /**
     * @param collezioneTable è la tabella vera e propria
     * @param colleziones     è la lista delle collezione da rappresentare
     */
    public static void addInTable(TableView<CollezioneBean> collezioneTable, List<CollezioneBean> colleziones) {
        List<CollezioneBean> currentColleziones = collezioneTable.getItems(); // Ottenere la lista attuale di collezione dalla TableView

        colleziones.removeIf(collezione -> currentColleziones.stream().anyMatch(currentCollezione -> currentCollezione.getLink().equals(collezione.getLink())));
        currentColleziones.addAll(colleziones);

        ObservableList<CollezioneBean> collezioneData = FXCollections.observableArrayList(currentColleziones);
        collezioneTable.setItems(collezioneData); // Aggiornare la TableView con la lista aggiornata di collezione
    }


    public ObservableList<CollezioneBean> handler(TableView<CollezioneBean> collezioneTable, List<CollezioneBean> collezioneBeanList) {

        ObservableList<CollezioneBean> observableList = FXCollections.observableList(collezioneBeanList);
        collezioneTable.setItems(observableList);

        // Aggiunta di un listener di modifica al ObservableList
        observableList.addListener((ListChangeListener<CollezioneBean>) change -> {
            while (change.next()) {
                if (change.wasAdded() && isUpdatingTableView) { ///// non accade mai #########
                    Printer.logPrint(String.format("Elementi aggiunti: %s" , change.getAddedSubList()));
                    isUpdatingTableView = false;
                    collezioneTable.getItems().addAll(change.getAddedSubList());
                    isUpdatingTableView = true;
                } else if (change.wasRemoved() && isUpdatingTableView) {
                    Printer.logPrint(String.format("Elementi rimossi: %s", change.getRemoved()));
                    isUpdatingTableView = false;
                    collezioneTable.getItems().removeAll(change.getRemoved());
                    isUpdatingTableView = true;
                }
            }
        });

        return observableList;
    }

}

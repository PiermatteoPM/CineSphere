package view.first.utils;

import engineering.bean.CollezioneBean;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import view.first.PendingCollezioneCtrlGrafico;


// Classe TableCell personalizzata per aggiungere due bottoni nella cella
public class DoubleButtonTableCell extends TableCell<CollezioneBean, Boolean> {

    private final Button approveButton = new Button("V");
    private final Button rejectButton = new Button("X");

    public DoubleButtonTableCell(PendingCollezioneCtrlGrafico pendingCollezioneCtrlGrafico) {

        approveButton.setOnAction(e -> {
            TableRow<CollezioneBean> tableRow = getTableRow();
            if (tableRow != null) {
                CollezioneBean collezioneBean = tableRow.getItem();
                handlePendingButton(pendingCollezioneCtrlGrafico, collezioneBean, true);
            }
        });

        rejectButton.setOnAction(e -> {
            TableRow<CollezioneBean> tableRow = getTableRow();
            if (tableRow != null) {
                CollezioneBean collezioneBean = tableRow.getItem();
                handlePendingButton(pendingCollezioneCtrlGrafico, collezioneBean, false);
            }
        });


        approveButton.setStyle("-fx-background-color: #e8b910; -fx-text-fill: white; -fx-pref-height: 25px; -fx-pref-width: 25px; " +
                "-fx-min-width: -1; -fx-min-height: -1; -fx-background-radius: 50%; -fx-stroke: 50; -fx-border-radius: 50%;");

        rejectButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-pref-height: 25px; -fx-pref-width: 25px; " +
                "-fx-min-width: -1; -fx-min-height: -1; -fx-background-radius: 50%; -fx-stroke: 50; -fx-border-radius: 50%;");
    }

    public void handlePendingButton(PendingCollezioneCtrlGrafico pendingCollezioneCtrlGrafico, CollezioneBean collezioneBean, boolean approve) {
        pendingCollezioneCtrlGrafico.handlerButton(collezioneBean,approve);
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(createButtonBox(approveButton, rejectButton));
        }
    }

    private HBox createButtonBox(Button... buttons) {
        HBox buttonBox = new HBox(5); // 5 è lo spazio tra i bottoni
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(buttons);
        return buttonBox;
    }
}
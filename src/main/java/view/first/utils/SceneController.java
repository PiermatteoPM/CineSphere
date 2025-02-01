package view.first.utils;

import engineering.bean.*;
import engineering.others.Printer;

import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import view.first.*;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class SceneController {
    public SceneController(){
        sceneStack = new LinkedList<>();
    }
    private final Deque<Scene> sceneStack;
    private static final String SET_ATTRIBUTES = "setAttributes"; //Funzione esistente in ogni controller grafico

    /** Utilizzato da Home e Account per passare la Collection ad Add */
    @FXML
    public <T> void goToScene(ActionEvent event, String fxmlPath, ClientBean clientBean, CollectionBean collectionBean, ObservableList<CollectionBean> observableList) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            T controller = loader.getController();
            setAttributes(controller, clientBean, collectionBean, observableList);

            switchScene(event, root);
        } catch (IOException e) {
            handleSceneLoadError(e);
        }
    }

    /** Utilizzato da Home e Account per passare la Collection ad Add */
    @FXML
    public void goToScene(ActionEvent event, String fxmlPath, ClientBean clientBean,ObservableList<CollectionBean> observableList) {
        goToScene(event, fxmlPath, clientBean, null, observableList);
    }

    @FXML
    public void goToScene(ActionEvent event, String fxmlPath, ClientBean clientBean) {
        goToScene(event, fxmlPath, clientBean, null, null);
    }

    @FXML
    public void goToScene(ActionEvent event, String fxmlPath) {
        goToScene(event, fxmlPath, null, null, null);
    }

    @FXML
    public void goToSceneForLogout(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Cambia scena
            switchScene(event, root);
        } catch (IOException e) {
            handleSceneLoadError(e);
        }
    }



    private void switchScene(ActionEvent event, Parent root) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sceneStack.push(stage.getScene()); // Push current scene onto stack

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * HomePage: setAttributes(SceneController scene Controller, T clientBean)
     * Account: setAttributes(SceneController sceneController, T clientBean)

     * Registration: setAttributes(SceneController sceneController)
     * TextPopUp: setAttributes(SceneController sceneController)

     * AddCollection: setAttributes(SceneController sceneController, T clientBean, ObservableList<CollectionBean> observableList)
     * Filter: setAttributes(SceneController sceneController, CollectionBean collectionBean)
     * */
    private void setAttributes(Object controller, ClientBean clientBean, CollectionBean collectionBean, ObservableList<CollectionBean> observableList) {
        Class<?>[] parameterTypes = {ClientBean.class, CollectionBean.class, ObservableList.class};

        for (Class<?> paramType : parameterTypes) {
            try {
                // Gestione setAttributes con due parametri

                Method setAttributes = controller.getClass().getMethod(SET_ATTRIBUTES, paramType, SceneController.class);

                // Verifica il tipo di parametro e invoca il metodo corrispondente solo se il parametro non è null
                if (paramType == ClientBean.class) {
                    setAttributes.invoke(controller, clientBean, this);         // Account e HomePage ok
                } else if (paramType == CollectionBean.class && collectionBean != null) {
                    setAttributes.invoke(controller, collectionBean, this);       // Filter
                }
                return; // Esci dal metodo se trova una firma valida

            } catch (IllegalAccessException | InvocationTargetException e) {
                // Gestione dell'errore in caso di problemi con la riflessione
                handleSceneLoadError(e);
                return; // Esci dal metodo in caso di eccezione
            } catch (NoSuchMethodException ignored) {
                //
            }
        }

        // Se non trova alcuna firma valida, gestisci il caso generico con una firma con solo SceneController
        try {
            Method setAttributes = controller.getClass().getMethod(SET_ATTRIBUTES, SceneController.class);
            setAttributes.invoke(controller, this);
            return;
        } catch (IllegalAccessException | InvocationTargetException e) {
            // Gestione dell'errore in caso di problemi con la riflessione
            handleSceneLoadError(e);
            return; // Esci dal metodo in caso di eccezione
        } catch (NoSuchMethodException ignored) {
            //
        }

        // Se non trova alcuna firma valida, gestisci il caso generico con una firma (SceneController, T, ObservableList<CollectionBean>)
        try {
            Method setAttributes = controller.getClass().getMethod(SET_ATTRIBUTES, CollectionBean.class);
            setAttributes.invoke(controller, collectionBean);
            return;
        } catch (IllegalAccessException | InvocationTargetException e) {
            // Gestione dell'errore in caso di problemi con la riflessione
            handleSceneLoadError(e);
            return; // Esci dal metodo in caso di eccezione
        } catch (NoSuchMethodException ignored) {
            //
        }

        // Se non trova alcuna firma valida, gestisci il caso generico con una firma (SceneController, T, ObservableList<CollectionBean>)
        try {
            Method setAttributes = controller.getClass().getMethod(SET_ATTRIBUTES, ClientBean.class, ObservableList.class,  SceneController.class);
            setAttributes.invoke(controller, clientBean, observableList, this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Gestione dell'errore in caso di problemi con la riflessione
            handleSceneLoadError(e);
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        if (!sceneStack.isEmpty()) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(sceneStack.pop()); // Pop the last scene from stack
            stage.show();
        }
    }

    @FXML
    public void pushCurrentScene(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sceneStack.push(stage.getScene()); // Push current scene onto stack
    }


    public void textPopUp(ActionEvent event, String text, boolean back) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlFileName.POP_UP_FXML));
            Parent root = loader.load();

            // Ottieni l'istanza del controller
            TextPopUp controller = loader.getController();
            setAttributes(controller, null,null, null);

            // Utilizza il controller per chiamare la funzione setText
            controller.setText(text);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if(back){
                controller.setPreviousEvent(event);
            } else {
                controller.setPreviousEvent(null);
            }

            Stage popupStage = new Stage();
            popupStage.initOwner(stage);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            popupStage.showAndWait();
        } catch (IOException e) {
            // Gestione dell'errore durante il caricamento del popup
            handleSceneLoadError(e);
        }
    }

    public void goToFilterPopUp(ActionEvent event, ClientBean clientBean, CollectionBean collectionBean, HomePageCtrlGrafico<?> istanza) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlFileName.POP_UP_FXML_FILTER));
            Parent root = loader.load();

            // Ottieni l'istanza del controller
            FilterCtrlGrafico controller = loader.getController();
            controller.setHomeInstance(istanza);
            setAttributes(controller, clientBean, collectionBean, null);

            // Stage di partenza
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Stage popupStage = new Stage();
            popupStage.initOwner(stage);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            popupStage.showAndWait();
        } catch (IOException e) {
            // Gestione dell'errore durante il caricamento del popup
            handleSceneLoadError(e);
        }
    }

    private void handleSceneLoadError(Exception e) {
        Printer.errorPrint(String.format("SceneController: %s", e.getMessage()));
    }
}


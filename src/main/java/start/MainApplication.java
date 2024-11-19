package start;

import engineering.others.Printer;

import view.first.utils.FxmlFileName;
import view.second.LoginCLI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Carica le proprietà dal file di configurazione
        Properties properties = loadConfigurationProperties();

        // Ottieni il tipo di interfaccia dalle proprietà
        int interfaceType = Integer.parseInt(properties.getProperty("interface.type","1"));

        if (interfaceType == 1) {
            // Interfaccia grafica
            loadGraphicalInterface(stage);
        } else if (interfaceType == 2) {
            // Interfaccia a riga di comando
            startCommandLineInterface();
        } else {
            Printer.errorPrint("Tipo di interfaccia specificata nel file di configurazione non valida: Default interfaccia grafica");
            // Interfaccia grafica
            loadGraphicalInterface(stage);
        }
    }

    private void loadGraphicalInterface(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FxmlFileName.LOGIN_FXML));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void startCommandLineInterface() {
        LoginCLI loginCLI = new LoginCLI();
        loginCLI.start();
    }

    /** Lettura dal file di configurazione per la scelta dell'interfaccia */
    private Properties loadConfigurationProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                Printer.errorPrint("MainApplication: Impossibile trovare il file di configurazione.");
            }
        } catch (IOException e) {
           Printer.errorPrint(String.format("MainApplication: Errore durante la lettura del file di configurazione %s", e.getMessage()));
        }
        return properties;
    }

    public static void main(String[] args) {
        launch();
    }
}

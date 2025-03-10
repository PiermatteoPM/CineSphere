package view.second;

import controller.applicativo.AccountCtrlApplicativo;
import engineering.bean.ClientBean;
import engineering.bean.CollectionBean;
import engineering.others.Printer;
import view.second.utils.GenreManager;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Questa classe gestisce l'interfaccia a riga di comando (CLI) per l'account utente.
 */
public class AccountCLI {

    private final Scanner scanner = new Scanner(System.in);
    private ClientBean clientBean;

    /**
     * Imposta il bean del cliente per l'interfaccia utente.
     *
     * @param clientBean Il bean del cliente da impostare.
     */
    public void setClientBean(ClientBean clientBean) {
        this.clientBean = clientBean;
    }

    /**
     * Avvia l'interfaccia a riga di comando per l'account utente.
     */
    public void start() {
        while (true) {
            Printer.println("----- Profilo -----");

            displayUserInfo(clientBean);

            // Mostra il menu
            Printer.println("1. Carica nuova Collection");
            Printer.println("2. Reimposta le tue preferenze");
            Printer.println("0. Esci");

            Printer.print("Scegli un'opzione: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addCollection();
                    break;
                case 2:
                    updatePreferences();
                    break;
                case 0:
                    return;
                default:
                    Printer.errorPrint("Scelta non disponibile !");
                    break;
            }
        }
    }

    private void displayUserInfo(ClientBean clientBean) {
        if (clientBean != null) {
            Printer.println(String.format("Username: %s", clientBean.getUsername()));
            Printer.println(String.format("Email: %s", clientBean.getEmail()));

            if (clientBean.getPreferences() != null && !clientBean.getPreferences().isEmpty()) {
                Printer.println(String.format("Generi preferiti: %s", String.join(", ", clientBean.getPreferences())));
            } else {
                Printer.println("Nessun genere preferito impostato.");
            }

            Printer.println("Le tue collection:");
            displayUserCollectionss();
        }
    }

    private void displayUserCollectionss() {
        AccountCtrlApplicativo accountCtrlApplicativo = new AccountCtrlApplicativo();
        List<CollectionBean> userCollectionss = accountCtrlApplicativo.retrieveCollectionss(clientBean);
        for (CollectionBean collection : userCollectionss) {
            String approvalStatus = collection.getApproved() ? "Approved" : "In attesa";
            Printer.println(String.format(("%s - Titolo: %s, Link: %s"), approvalStatus, collection.getCollectionName(), collection.getLink()));
        }
    }

    private void updatePreferences() {
        GenreManager genreManager = new GenreManager();
        Map<Integer, String> availableGenres = genreManager.getAvailableGenres();

        Printer.println("Generi disponibili:");
        genreManager.printGenres(availableGenres);

        Printer.print("Inserisci i numeri corrispondenti ai generi contenuti nella Collection (separati da virgola): ");
        String genreInput = scanner.next();

        List<String> preferences = genreManager.extractGenres(availableGenres, genreInput);
        clientBean.setPreferences(preferences);

        AccountCtrlApplicativo accountCtrlApplicativo = new AccountCtrlApplicativo();
        accountCtrlApplicativo.updateGenreUser(clientBean);
        Printer.println("Preferenze aggiornate");
    }

    private void addCollection() {
        AddCollectionCLI addCollectionCLI = new AddCollectionCLI();
        addCollectionCLI.setClientBean(clientBean);
        addCollectionCLI.start();
    }
}

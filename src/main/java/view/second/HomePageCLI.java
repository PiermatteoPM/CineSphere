package view.second;

import controller.applicativo.HomePageCtrlApplicativo;
import engineering.bean.*;
import engineering.others.Printer;

import java.util.List;
import java.util.Scanner;

/**
 * Questa classe gestisce l'interfaccia a riga di comando (CLI) della home page dell'applicazione.
 *
 * @param <T> Tipo del bean del cliente (UserBean, SupervisorBean, ecc.).
 */
public class HomePageCLI<T extends ClientBean> {

    private Scanner scanner = new Scanner(System.in);
    private CollezioneBean collezioneBean = new CollezioneBean();
    private T clientBean;

    /**
     * Imposta il bean del cliente per l'interfaccia utente.
     *
     * @param clientBean Il bean del cliente da impostare.
     */
    public void setClientBean(T clientBean) {
        this.clientBean = clientBean;
    }

    /**
     * Imposta il bean della collezione per l'interfaccia utente.
     *
     * @param collezioneBean Il bean della collezione da impostare.
     */
    public void setCollezioneBean(CollezioneBean collezioneBean) {
        this.collezioneBean = collezioneBean;
    }

    /**
     * Avvia l'interfaccia a riga di comando della home page.
     */
    public void start() {
        scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMenu();

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    showAllColleziones();
                    break;
                case 2:
                    // Devo differenziare user e supervisor da guest
                    addCollezione();
                    break;
                case 3:
                    addFilter();
                    break;
                case 4:
                    deleteFilter();
                    break;
                case 5:
                    searchCollezione();
                    break;
                case 6:
                    // Passa al menu del profilo utente
                    if (clientBean == null) {
                        // Vai alla registrazione
                        goToRegistration();
                        exit = true; // Esci dal loop e dal programma
                    } else {
                        account();
                    }
                    break;
                case 7:
                    // Passa al menu approvazione collezione
                    // Verifica per maggiore sicurezza
                    if (clientBean.isSupervisor()) {
                        goToApproveCollezione();
                    }
                    break;
                case 0:
                    exit = true; // Esci dal loop e dal programma
                    break;
                default:
                    Printer.errorPrint("Scelta non valida. Riprova.");
            }
        }
    }

    /**
     * Passa al menu di gestione delle collezione in attesa di approvazione.
     */
    private void goToApproveCollezione() {
        ManageCollezionesCLI manager = new ManageCollezionesCLI();
        manager.start();
    }

    /**
     * Passa al menu di registrazione utente.
     */
    private void goToRegistration() {
        RegistrationCLI registrationCLI = new RegistrationCLI();
        registrationCLI.start();
    }

    /**
     * Passa al menu dell'account utente.
     */
    private void account() {
        AccountCLI accountCLI = new AccountCLI();
        accountCLI.setClientBean(clientBean);
        accountCLI.start();
    }

    /**
     * Stampa il menu principale dell'interfaccia utente.
     */
    private void printMenu() {
        Printer.println("\n ----- Home Page ----- ");
        Printer.println("1. Visualizza tutte le collezione");
        if (clientBean != null) {
            Printer.println("2. Aggiungi una collezione");
        } else {
            Printer.println("2. !!! Non puoi aggiungere collezione -> Registrati!!!");
        }
        Printer.println("3. Applica un filtro di ricerca");
        Printer.println("4. Elimina il filtro di ricerca");
        Printer.println("5. Cerca una collezione per nome");
        if (clientBean != null) {
            Printer.println("6. Visualizza il tuo profilo");
        } else {
            Printer.println("6. !!! Non hai un profilo da visualizzare -> Registrati !!!");
        }

        if (clientBean != null && clientBean.isSupervisor()) {
            Printer.println("7. Gestisci colleziones in attesa di approvazione");
        }
        Printer.println("0. Esci");
        Printer.println("Scegli un'opzione: ");
    }

    /**
     * Visualizza tutte le collezione approvate.
     */
    private void showAllColleziones() {
        // Utilizza i metodi del controller applicativo per recuperare e stampare le collezione approvate
        HomePageCtrlApplicativo homePageController = new HomePageCtrlApplicativo();
        List<CollezioneBean> collezioneBeans = homePageController.retriveCollezionesApproved();

        int collezioneNumber = 1;
        for (CollezioneBean collezione : collezioneBeans) {
            Printer.println(String.format("%d. Titolo: %s Creatore: %s Generi della Collezione: %s",
                    collezioneNumber, collezione.getCollezioneName(), collezione.getUsername(), collezione.getCollezioneGenre()));
            collezioneNumber++;
        }

        while (true) {
            // Per copiare il link della collezione, l'utente deve inserire il numero corrispondente
            Printer.print("Inserisci il numero della collezione per ricevere il link (0 per tornare al menu): ");

            int collezioneChoice = scanner.nextInt();

            if (collezioneChoice > 0 && collezioneChoice <= collezioneBeans.size()) {
                CollezioneBean selectedCollezione = collezioneBeans.get(collezioneChoice - 1);
                Printer.println(String.format("Link: %s", selectedCollezione.getLink()));
            } else if (collezioneChoice == 0) {
                // Torna al menu principale
                break;
            } else {
                Printer.errorPrint("! Scelta non valida ! -> Riprova");
            }
        }
    }

    /**
     * Aggiunge una nuova collezione.
     */
    private void addCollezione() {
        AddCollezioneCLI addCollezioneCLI = new AddCollezioneCLI();
        addCollezioneCLI.setClientBean(clientBean);
        addCollezioneCLI.start();
    }

    /**
     * Applica un filtro di ricerca alle collezione.
     */
    private void addFilter() {
        Printer.errorPrint("Non è ancora stato implementato.");
    }

    /**
     * Elimina i filtri di ricerca applicati alle collezione.
     */
    private void deleteFilter() {
        Printer.errorPrint("Non è ancora stato implementato.");
    }

    /**
     * Cerca una collezione per nome.
     */
    private void searchCollezione() {
        Printer.print("Inserisci il nome della collezione da cercare: ");
        String collezioneName = scanner.nextLine();

        HomePageCtrlApplicativo homePageController = new HomePageCtrlApplicativo();
        collezioneBean.setCollezioneName(collezioneName);
        List<CollezioneBean> collezionesList = homePageController.searchCollezioneByFilters(collezioneBean);

        Printer.println(String.format("Collezione che contengono: \"%s\" nel titolo: ", collezioneName));

        if (collezionesList.isEmpty()) {
            Printer.println("Nessuna collezione trovata con il nome specificato.");
        } else {
            int index = 1;
            for (CollezioneBean collezione : collezionesList) {
                Printer.println(String.format("%d. Nome: %s, Username: %s, Generi: %s",
                        index, collezione.getCollezioneName(), collezione.getUsername(), collezione.getCollezioneGenre()));
                index++;
            }

            int selectedCollezioneIndex;
            while (true) {
                Printer.print("Inserisci il numero corrispondente alla collezione desiderata (inserisci 0 per uscire): ");
                selectedCollezioneIndex = scanner.nextInt();

                if (selectedCollezioneIndex == 0) {
                    break; // Esce dal ciclo interno se l'utente inserisce 0
                }

                if (selectedCollezioneIndex >= 1 && selectedCollezioneIndex <= collezionesList.size()) {
                    CollezioneBean selectedCollezione = collezionesList.get(selectedCollezioneIndex - 1);
                    Printer.println(String.format("Link della collezione selezionata: %s", selectedCollezione.getLink()));
                } else {
                    Printer.errorPrint("! Selezione non valida-> Riprova !");
                }
            }
        }
    }
}

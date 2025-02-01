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
    private CollectionBean collectionBean = new CollectionBean();
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
     * Imposta il bean della collection per l'interfaccia utente.
     *
     * @param collectionBean Il bean della collection da impostare.
     */
    public void setCollectionBean(CollectionBean collectionBean) {
        this.collectionBean = collectionBean;
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
                    showAllCollectionss();
                    break;
                case 2:
                    // Devo differenziare user e supervisor da guest
                    addCollection();
                    break;
                case 3:
                    addFilter();
                    break;
                case 4:
                    deleteFilter();
                    break;
                case 5:
                    searchCollection();
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
                    // Passa al menu approvazione collection
                    // Verifica per maggiore sicurezza
                    if (clientBean.isSupervisor()) {
                        goToApproveCollection();
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
     * Passa al menu di gestione delle collection in attesa di approvazione.
     */
    private void goToApproveCollection() {
        ManageCollectionssCLI manager = new ManageCollectionssCLI();
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
        Printer.println("1. Visualizza tutte le collection");
        if (clientBean != null) {
            Printer.println("2. Aggiungi una collection");
        } else {
            Printer.println("2. !!! Non puoi aggiungere collection -> Registrati!!!");
        }
        Printer.println("3. Applica un filtro di ricerca");
        Printer.println("4. Elimina il filtro di ricerca");
        Printer.println("5. Cerca una collection per nome");
        if (clientBean != null) {
            Printer.println("6. Visualizza il tuo profilo");
        } else {
            Printer.println("6. !!! Non hai un profilo da visualizzare -> Registrati !!!");
        }

        if (clientBean != null && clientBean.isSupervisor()) {
            Printer.println("7. Gestisci collection in attesa di approvazione");
        }
        Printer.println("0. Esci");
        Printer.println("Scegli un'opzione: ");
    }

    /**
     * Visualizza tutte le collection approvate.
     */
    private void showAllCollectionss() {
        // Utilizza i metodi del controller applicativo per recuperare e stampare le collection approvate
        HomePageCtrlApplicativo homePageController = new HomePageCtrlApplicativo();
        List<CollectionBean> collectionBeans = homePageController.retriveCollectionssApproved();

        int collectionNumber = 1;
        for (CollectionBean collection : collectionBeans) {
            Printer.println(String.format("%d. Titolo: %s Creatore: %s Generi della Collection: %s",
                    collectionNumber, collection.getCollectionName(), collection.getUsername(), collection.getCollectionGenre()));
            collectionNumber++;
        }

        while (true) {
            // Per copiare il link della collection, l'utente deve inserire il numero corrispondente
            Printer.print("Inserisci il numero della collection per ricevere il link (0 per tornare al menu): ");

            int collectionChoice = scanner.nextInt();

            if (collectionChoice > 0 && collectionChoice <= collectionBeans.size()) {
                CollectionBean selectedCollection = collectionBeans.get(collectionChoice - 1);
                Printer.println(String.format("Link: %s", selectedCollection.getLink()));
            } else if (collectionChoice == 0) {
                // Torna al menu principale
                break;
            } else {
                Printer.errorPrint("! Scelta non valida ! -> Riprova");
            }
        }
    }

    /**
     * Aggiunge una nuova collection.
     */
    private void addCollection() {
        AddCollectionCLI addCollectionCLI = new AddCollectionCLI();
        addCollectionCLI.setClientBean(clientBean);
        addCollectionCLI.start();
    }

    /**
     * Applica un filtro di ricerca alle collection.
     */
    private void addFilter() {
        Printer.errorPrint("Non è ancora stato implementato.");
    }

    /**
     * Elimina i filtri di ricerca applicati alle collection.
     */
    private void deleteFilter() {
        Printer.errorPrint("Non è ancora stato implementato.");
    }

    /**
     * Cerca una collection per nome.
     */
    private void searchCollection() {
        Printer.print("Inserisci il nome della collection da cercare: ");
        String collectionName = scanner.nextLine();

        HomePageCtrlApplicativo homePageController = new HomePageCtrlApplicativo();
        collectionBean.setCollectionName(collectionName);
        List<CollectionBean> collectionssList = homePageController.searchCollectionByFilters(collectionBean);

        Printer.println(String.format("Collection che contengono: \"%s\" nel titolo: ", collectionName));

        if (collectionssList.isEmpty()) {
            Printer.println("Nessuna collection trovata con il nome specificato.");
        } else {
            int index = 1;
            for (CollectionBean collection : collectionssList) {
                Printer.println(String.format("%d. Nome: %s, Username: %s, Generi: %s",
                        index, collection.getCollectionName(), collection.getUsername(), collection.getCollectionGenre()));
                index++;
            }

            int selectedCollectionIndex;
            while (true) {
                Printer.print("Inserisci il numero corrispondente alla collection desiderata (inserisci 0 per uscire): ");
                selectedCollectionIndex = scanner.nextInt();

                if (selectedCollectionIndex == 0) {
                    break; // Esce dal ciclo interno se l'utente inserisce 0
                }

                if (selectedCollectionIndex >= 1 && selectedCollectionIndex <= collectionssList.size()) {
                    CollectionBean selectedCollection = collectionssList.get(selectedCollectionIndex - 1);
                    Printer.println(String.format("Link della collection selezionata: %s", selectedCollection.getLink()));
                } else {
                    Printer.errorPrint("! Selezione non valida-> Riprova !");
                }
            }
        }
    }
}

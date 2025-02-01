package view.second;

import controller.applicativo.PendingCollectionCtrlApplicativo;
import engineering.bean.CollectionBean;
import engineering.others.Printer;

import java.util.List;
import java.util.Scanner;

/**
 * Questa classe gestisce l'interfaccia a riga di comando (CLI) per la gestione delle collection in attesa di approvazione.
 */
public class ManageCollectionssCLI {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Avvia l'interfaccia a riga di comando per la gestione delle collection in attesa.
     */
    public void start() {
        PendingCollectionCtrlApplicativo pendingCollectionCtrlApplicativo = new PendingCollectionCtrlApplicativo();
        List<CollectionBean> collectionssPending = pendingCollectionCtrlApplicativo.retrieveCollectionss();
        Printer.println("\n----- Gestisci Collection -----");
        int index = 0;
        for (CollectionBean collection : collectionssPending) {
            index++;
            handleCollection(collection, index);
        }
        Printer.println("--- Non ci sono più collection da gestire ---");
    }

    /**
     * Espone la collection all'utente e attende la risposta (accetta o rifiuta) prima di visualizzare la prossima collection.
     *
     * @param collection La collection da gestire.
     * @param index    L'indice della collection.
     */
    private void handleCollection(CollectionBean collection, int index) {
        Printer.println("---" + index + "---");
        Printer.println("Nome: " + collection.getCollectionName());
        Printer.println("Creatore: " + collection.getUsername());
        Printer.println("Generi: " + collection.getCollectionGenre());
        Printer.println("Link: " + collection.getLink());

        Printer.println("\nOpzioni:");
        Printer.println("1. Accetta");
        Printer.println("2. Rifiuta");
        Printer.println("0. Esci");

        int choice = getChoice();

        switch (choice) {
            case 1:
                acceptCollection(collection);
                break;
            case 2:
                rejectCollection(collection);
                break;
            case 0:
                Printer.println("Uscita dalla gestione collection in attesa.");
                break;
            default:
                Printer.errorPrint("Scelta non valida. Riprova.");
                handleCollection(collection, index);
                break;
        }
    }

    /**
     * Ottiene la scelta dell'utente.
     *
     * @return La scelta dell'utente.
     */
    private int getChoice() {
        Printer.print("Inserisci la tua scelta: ");
        return scanner.nextInt();
    }

    /**
     * Accetta la collection.
     *
     * @param collection La collection da accettare.
     */
    private void acceptCollection(CollectionBean collection) {
        PendingCollectionCtrlApplicativo pendingCollectionCtrlApplicativo = new PendingCollectionCtrlApplicativo();
        pendingCollectionCtrlApplicativo.approveCollection(collection);

        Printer.println("Collection approvata.");
    }

    /**
     * Rifiuta la collection.
     *
     * @param collection La collection da rifiutare.
     */
    private void rejectCollection(CollectionBean collection) {
        PendingCollectionCtrlApplicativo pendingCollectionCtrlApplicativo = new PendingCollectionCtrlApplicativo();
        pendingCollectionCtrlApplicativo.rejectCollection(collection);

        Printer.println("Collection rifiutata.");
    }
}

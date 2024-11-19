package view.second;

import controller.applicativo.PendingCollezioneCtrlApplicativo;
import engineering.bean.CollezioneBean;
import engineering.others.Printer;

import java.util.List;
import java.util.Scanner;

/**
 * Questa classe gestisce l'interfaccia a riga di comando (CLI) per la gestione delle collezione in attesa di approvazione.
 */
public class ManageCollezionesCLI {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Avvia l'interfaccia a riga di comando per la gestione delle collezione in attesa.
     */
    public void start() {
        PendingCollezioneCtrlApplicativo pendingCollezioneCtrlApplicativo = new PendingCollezioneCtrlApplicativo();
        List<CollezioneBean> collezionesPending = pendingCollezioneCtrlApplicativo.retrieveColleziones();
        Printer.println("\n----- Gestisci Colleziones -----");
        int index = 0;
        for (CollezioneBean collezione : collezionesPending) {
            index++;
            handleCollezione(collezione, index);
        }
        Printer.println("--- Non ci sono più collezione da gestire ---");
    }

    /**
     * Espone la collezione all'utente e attende la risposta (accetta o rifiuta) prima di visualizzare la prossima collezione.
     *
     * @param collezione La collezione da gestire.
     * @param index    L'indice della collezione.
     */
    private void handleCollezione(CollezioneBean collezione, int index) {
        Printer.println("---" + index + "---");
        Printer.println("Nome: " + collezione.getCollezioneName());
        Printer.println("Creatore: " + collezione.getUsername());
        Printer.println("Generi: " + collezione.getCollezioneGenre());
        Printer.println("Link: " + collezione.getLink());

        Printer.println("\nOpzioni:");
        Printer.println("1. Accetta");
        Printer.println("2. Rifiuta");
        Printer.println("0. Esci");

        int choice = getChoice();

        switch (choice) {
            case 1:
                acceptCollezione(collezione);
                break;
            case 2:
                rejectCollezione(collezione);
                break;
            case 0:
                Printer.println("Uscita dalla gestione collezione in attesa.");
                break;
            default:
                Printer.errorPrint("Scelta non valida. Riprova.");
                handleCollezione(collezione, index);
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
     * Accetta la collezione.
     *
     * @param collezione La collezione da accettare.
     */
    private void acceptCollezione(CollezioneBean collezione) {
        PendingCollezioneCtrlApplicativo pendingCollezioneCtrlApplicativo = new PendingCollezioneCtrlApplicativo();
        pendingCollezioneCtrlApplicativo.approveCollezione(collezione);

        Printer.println("Collezione approvata.");
    }

    /**
     * Rifiuta la collezione.
     *
     * @param collezione La collezione da rifiutare.
     */
    private void rejectCollezione(CollezioneBean collezione) {
        PendingCollezioneCtrlApplicativo pendingCollezioneCtrlApplicativo = new PendingCollezioneCtrlApplicativo();
        pendingCollezioneCtrlApplicativo.rejectCollezione(collezione);

        Printer.println("Collezione rifiutata.");
    }
}

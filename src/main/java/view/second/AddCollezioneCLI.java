package view.second;

import controller.applicativo.AddCollezioneCtrlApplicativo;
import engineering.bean.*;
import engineering.exceptions.*;
import engineering.others.Printer;
import view.second.utils.GenreManager;

import java.util.*;

/**
 * Questa classe gestisce l'interfaccia a riga di comando (CLI) per l'aggiunta di una collezione.
 */
public class AddCollezioneCLI {
    private ClientBean clientBean;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Imposta il bean del cliente per l'interfaccia utente.
     *
     * @param bean Il bean del cliente da impostare.
     */
    public void setClientBean(ClientBean bean){
        this.clientBean = bean;
    }

    /**
     * Avvia l'interfaccia a riga di comando per l'aggiunta di una collezione.
     */
    public void start() {
        CollezioneBean collezioneBean = new CollezioneBean();

        collezioneBean.setApproved(clientBean.isSupervisor());
        collezioneBean.setUsername(clientBean.getUsername());
        collezioneBean.setEmail(clientBean.getEmail());

        // Chiedi all'utente di inserire i dati della collezione
        Printer.print("Inserisci il titolo della collezione: ");
        collezioneBean.setCollezioneName(scanner.nextLine());

        boolean linkIsValid = false;
        while (!linkIsValid) {
            Printer.print("Inserisci il link della collezione: ");
            try {
                collezioneBean.setLink(scanner.nextLine());
                linkIsValid = true; // Se non viene lanciata l'eccezione, il link è valido e usciamo dal ciclo
            } catch (LinkIsNotValidException e) {
                Printer.errorPrint("! Link non valido-> Riprova !");
            }
        }

        // Richiedi all'utente di selezionare i generi musicali
        GenreManager genreManager = new GenreManager();
        Map<Integer, String> availableGenres = genreManager.getAvailableGenres();
        genreManager.printGenres(availableGenres);

        // Richiedi all'utente di selezionare i generi preferiti
        Printer.print("Inserisci i numeri corrispondenti ai generi musicali contenuti nella Collezione (separati da virgola): ");
        String genreInput = scanner.next();

        List<String> preferences = genreManager.extractGenres(availableGenres, genreInput);
        collezioneBean.setCollezioneGenre(preferences);

        try {
            // Invocazione del controller applicativo per inserire la collezione
            AddCollezioneCtrlApplicativo addCollezioneControllerApplicativo = new AddCollezioneCtrlApplicativo();
            addCollezioneControllerApplicativo.insertCollezione(collezioneBean);

            Printer.println("Collezione aggiunta con successo!");
        } catch (CollezioneLinkAlreadyInUseException e) {
            Printer.errorPrint(" ! Il link relativo collezione è già presente nel sistema !");
        } catch (CollezioneNameAlreadyInUseException e) {
            Printer.errorPrint(" ! Hai  già usato questo titolo !");
        }
    }
}

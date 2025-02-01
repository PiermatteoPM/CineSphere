package view.second;

import controller.applicativo.AddCollectionCtrlApplicativo;
import engineering.bean.*;
import engineering.exceptions.*;
import engineering.others.Printer;
import view.second.utils.GenreManager;

import java.util.*;

/**
 * Questa classe gestisce l'interfaccia a riga di comando (CLI) per l'aggiunta di una collection.
 */
public class AddCollectionCLI {
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
     * Avvia l'interfaccia a riga di comando per l'aggiunta di una collection.
     */
    public void start() {
        CollectionBean collectionBean = new CollectionBean();

        collectionBean.setApproved(clientBean.isSupervisor());
        collectionBean.setUsername(clientBean.getUsername());
        collectionBean.setEmail(clientBean.getEmail());

        // Chiedi all'utente di inserire i dati della collection
        Printer.print("Inserisci il titolo della collection: ");
        collectionBean.setCollectionName(scanner.nextLine());

        boolean linkIsValid = false;
        while (!linkIsValid) {
            Printer.print("Inserisci il link della collection: ");
            try {
                collectionBean.setLink(scanner.nextLine());
                linkIsValid = true; // Se non viene lanciata l'eccezione, il link è valido e usciamo dal ciclo
            } catch (LinkIsNotValidException e) {
                Printer.errorPrint("! Link non valido-> Riprova !");
            }
        }

        // Richiedi all'utente di selezionare i generi
        GenreManager genreManager = new GenreManager();
        Map<Integer, String> availableGenres = genreManager.getAvailableGenres();
        genreManager.printGenres(availableGenres);

        // Richiedi all'utente di selezionare i generi preferiti
        Printer.print("Inserisci i numeri corrispondenti ai generi contenuti nella Collection (separati da virgola): ");
        String genreInput = scanner.next();

        List<String> preferences = genreManager.extractGenres(availableGenres, genreInput);
        collectionBean.setCollectionGenre(preferences);

        try {
            // Invocazione del controller applicativo per inserire la collection
            AddCollectionCtrlApplicativo addCollectionControllerApplicativo = new AddCollectionCtrlApplicativo();
            addCollectionControllerApplicativo.insertCollection(collectionBean);

            Printer.println("Collection aggiunta con successo!");
        } catch (CollectionLinkAlreadyInUseException e) {
            Printer.errorPrint(" ! Il link relativo collection è già presente nel sistema !");
        } catch (CollectionNameAlreadyInUseException e) {
            Printer.errorPrint(" ! Hai  già usato questo titolo !");
        }
    }
}

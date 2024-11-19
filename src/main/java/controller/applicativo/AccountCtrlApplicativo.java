package controller.applicativo;

import engineering.bean.*;
import engineering.dao.*;
import engineering.exceptions.*;
import engineering.others.Printer;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.pattern.observer.CollezioneCollection;
import model.*;

import java.util.*;

public class AccountCtrlApplicativo {

    /**
     * Recupera tutte le collezione globali by username
     */
    public List<CollezioneBean> retrieveColleziones(ClientBean clientBean) {
        CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO();         // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)
        String email = clientBean.getEmail();

        // Recupero lista Collezione
        List<Collezione> colleziones = dao.retrieveCollezionesByEmail(email);

        ArrayList<CollezioneBean> collezionesBean = new ArrayList<>();
        try {
            for (Collezione p : colleziones){
                CollezioneBean pB = new CollezioneBean(p.getEmail(),p.getUsername(),p.getCollezioneName(),p.getLink(),p.getCollezioneGenre(),p.getApproved());
                pB.setId(p.getId());
                collezionesBean.add(pB);
            }
        } catch (LinkIsNotValidException e){
            // Non la valuto perché è un retrieve da persistenza, dove è stata caricata correttamente
            handleDAOException(e);
        }

        return collezionesBean;
    }

    /** Utilizzata per aggiornare i generi musicali preferiti dell'utente in caso in cui prema il bottone Salva */
    public void updateGenreUser(ClientBean clientBean){
        ClientDAO dao = DAOFactory.getDAOFactory().createClientDAO();         // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        Client client;
        if(clientBean.isSupervisor()){
            client = new Supervisor(clientBean.getUsername(),clientBean.getEmail(),clientBean.getPreferences());
        } else {
            client = new User(clientBean.getUsername(),clientBean.getEmail(),clientBean.getPreferences());
        }

        // Invio utente model al DAO
        dao.updateGenreClient(client);
    }

    /** Utilizzata per permettere all'autore di eliminare le collezione
     * Non è stato implementata l'eliminazione nel front-end, ma si nel back-end */
    public Boolean deleteCollezione(CollezioneBean pB){

        CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO(); // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        Collezione collezione = new Collezione(pB.getEmail(), pB.getUsername(), pB.getCollezioneName(), pB.getLink(), pB.getCollezioneGenre(), pB.getApproved());
        collezione.setId(pB.getId());

        dao.deleteCollezione(collezione);

        if(collezione.getApproved()) {
            /* OBSERVER -> REMOVE PER FAR AGGIORNARE LA HOME PAGE */
            CollezioneCollection.getInstance().removeCollezione(collezione);
        }
        return true;
    }

    private void handleDAOException(Exception e) {
        Printer.logPrint(e.getMessage());
    }
}

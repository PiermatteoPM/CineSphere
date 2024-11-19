package controller.applicativo;

import engineering.bean.*;
import engineering.dao.*;
import engineering.pattern.observer.CollezioneCollection;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.exceptions.*;

import model.Collezione;

public class AddCollezioneCtrlApplicativo {

    public void insertCollezione(CollezioneBean pB) throws CollezioneLinkAlreadyInUseException, CollezioneNameAlreadyInUseException {

        CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO();         // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        // Crea la Collezione (model), id verrà impostato dal dao
        Collezione collezione = new Collezione(pB.getEmail(), pB.getUsername(), pB.getCollezioneName(), pB.getLink(), pB.getCollezioneGenre(), pB.getApproved());


        try{    // Invio Collezione model al DAO
            dao.insertCollezione(collezione);

            /* Per pattern Observer !!! */
            if(collezione.getApproved()){ // La notifica all observer solo se la collezione è approvata -> Se è caricata da un supervisore
                CollezioneCollection.getInstance().addCollezione(collezione);
            }

        } catch (CollezioneLinkAlreadyInUseException e){
            throw new CollezioneLinkAlreadyInUseException();
        } catch (CollezioneNameAlreadyInUseException e) {
            throw new CollezioneNameAlreadyInUseException();
        }
    }

}

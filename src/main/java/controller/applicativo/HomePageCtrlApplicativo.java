package controller.applicativo;

import engineering.bean.NoticeBean;
import engineering.bean.CollezioneBean;
import engineering.dao.*;
import engineering.exceptions.LinkIsNotValidException;
import engineering.others.Printer;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.pattern.observer.Observer;
import engineering.pattern.observer.CollezioneCollection;
import engineering.pattern.observer.Subject;
import model.Notice;
import model.Collezione;

import java.util.*;

public class HomePageCtrlApplicativo {

    public List<CollezioneBean> retriveCollezionesApproved() {

        CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO();        // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)
        List<Collezione> colleziones = dao.retrieveApprovedColleziones();              // Recupero lista Collezione approvate

        return getCollezionesBean(colleziones);
    }

    public List<CollezioneBean> searchCollezioneByFilters(CollezioneBean collezioneBean) {

        CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO();  // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        Collezione collezione = new Collezione();                             // Creo la entity da passare al DAO

        /* Popolo la collezione da cercare con solo le informazioni di cui l'utente è interessato */
        collezione.setCollezioneName(collezioneBean.getCollezioneName());
        collezione.setCollezioneGenre(collezioneBean.getCollezioneGenre());

        List<Collezione> colleziones;

        if (genreEmpty(collezione.getCollezioneGenre())) {
            colleziones = dao.searchCollezioneByTitle(collezione);  // Filtra solo per titolo
        } else {
            colleziones = dao.searchCollezioneByGenre(collezione);  // Filtra per titolo e generi
        }


        return getCollezionesBean(colleziones);
    }

    /** Nel caso in cui non volessimo che la view contattasse il model per fare attach */
    public void observeCollezioneTable(Observer observer){
        Subject collezioneCollection = CollezioneCollection.getInstance();
        collezioneCollection.attach(observer);
    }

    public List<CollezioneBean> getCollezionesBean(List<Collezione> colleziones){
        List<CollezioneBean> collezionesBean = new ArrayList<>();           // Creo una lista di collezioneBean da restituire al Grafico

        try {
            for (Collezione p : colleziones){
                CollezioneBean pB = new CollezioneBean(p.getEmail(),p.getUsername(),p.getCollezioneName(),p.getLink(),p.getCollezioneGenre(),p.getApproved());
                pB.setId(p.getId());
                collezionesBean.add(pB);
            }
        } catch (LinkIsNotValidException e){
            // Non la valuto perché è un retrieve da persistenza, dove è stata caricata correttamente
            Printer.logPrint(String.format("HomePage APP: LinkIsNotValid %s", e.getMessage()));
        }
        return collezionesBean;
    }

    public void deleteSelectedCollezione(CollezioneBean collezioneBean) {
        Collezione collezione = new Collezione(collezioneBean.getEmail(), collezioneBean.getUsername(), collezioneBean.getCollezioneName(), collezioneBean.getLink(), collezioneBean.getCollezioneGenre(), collezioneBean.getApproved());

        if (collezione.getApproved()){
            CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO();
            dao.deleteCollezione(collezione);

            /* OBSERVER -> REMOVE PER FAR AGGIORNARE LA HOME PAGE */
            CollezioneCollection.getInstance().removeCollezione(collezione);
        }
    }

    public void removeNotice(NoticeBean noticeBean) {
        NoticeDAO dao = DAOFactory.getDAOFactory().createNoticeDAO();   // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)
        Notice notice = new Notice(noticeBean.getTitle(), noticeBean.getBody(), noticeBean.getEmail());
        dao.deleteNotice(notice);
    }

    /** Utilizzata per un corretto filtraggio */
    private boolean genreEmpty(List<String> genre){
        return genre == null || genre.isEmpty();
    }


}

package controller.applicativo;

import engineering.bean.*;
import engineering.dao.*;
import engineering.exceptions.*;
import engineering.others.Printer;
import engineering.pattern.abstract_factory.DAOFactory;
import engineering.pattern.observer.CollezioneCollection;

import model.*;

import java.util.*;

public class PendingCollezioneCtrlApplicativo {

    public void approveCollezione(CollezioneBean pB){
        CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO();

        Collezione collezione = new Collezione(pB.getEmail(), pB.getUsername(), pB.getCollezioneName(), pB.getLink(), pB.getCollezioneGenre(), pB.getApproved());
        collezione.setId(pB.getId());

        // Istanza di collezione ha ancora il parametro approved a false
        Collezione collezioneApproved = dao.approveCollezione(collezione);
        pB.setApproved(collezioneApproved.getApproved());

        /* OBSERVER -> ADD PER FAR AGGIORNARE LA HOME PAGE */
        CollezioneCollection collezioneCollection = CollezioneCollection.getInstance();
        collezioneCollection.addCollezione(collezione);
    }

    /** Recupera tutte le collezione globali, sia approvate che non */
    public List<CollezioneBean> retrieveColleziones(){

        CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO();

        // Recupero lista Collezione
        List<Collezione> colleziones = dao.retrievePendingColleziones();
        List<CollezioneBean> collezionesBean = new ArrayList<>();

        try{
            for (Collezione p : colleziones){
                CollezioneBean pB = new CollezioneBean(p.getEmail(),p.getUsername(),p.getCollezioneName(),p.getLink(),p.getCollezioneGenre(),p.getApproved());
                pB.setId(p.getId());

                collezionesBean.add(pB);
            }
        } catch (LinkIsNotValidException e){
            Printer.logPrint(e.getMessage());
        }

        return collezionesBean;
    }

    public void rejectCollezione(CollezioneBean pB) {
        CollezioneDAO dao = DAOFactory.getDAOFactory().createCollezioneDAO();

        Collezione collezione = new Collezione(pB.getEmail(), pB.getUsername(), pB.getCollezioneName(), pB.getLink(), pB.getCollezioneGenre(), pB.getApproved());
        collezione.setId(pB.getId());

        dao.deleteCollezione(collezione);
    }

    public void sendNotification(NoticeBean noticeBean) {

        NoticeDAO dao = DAOFactory.getDAOFactory().createNoticeDAO();
        Notice notice = new Notice(noticeBean.getTitle(),noticeBean.getBody(),noticeBean.getEmail());

        dao.addNotice(notice);
    }
}
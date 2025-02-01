package controller.applicativo;

import engineering.bean.*;
import engineering.dao.*;
import engineering.exceptions.*;
import engineering.pattern.abstract_factory.DAOFactory;
import model.*;

/**Analisi della classe LoginCtrlApplicativo
 Questa classe è un controller applicativo che gestisce il processo di login per l'applicazione.
 Si occupa principalmente di:

 Verificare le credenziali dell'utente.
 Caricare il profilo dell'utente se il login è valido.
 Recuperare gli avvisi (notifiche) associati all'utente.
 Segue l'architettura MVC (Model-View-Controller)
 e utilizza il design pattern Abstract Factory per ottenere l'istanza corretta del DAO.*/

import java.util.*;

public class LoginCtrlApplicativo {

    /** Il metodo accede allo strato di persistenza per verificare se le credenziali per l'accesso sono valide
     * L'email deve essere registrata
     * La password associata deve essere come quella inserita in fate di login
     * Il loginBean contiene il campo mail e il campo password
     * Effettua una Query per recuperare la password e confrontarla con quella inserita  */


    /**1. verificaCredenziali(LoginBean bean)
     Scopo
     Verifica se l'email esiste nel database e se la password fornita è corretta.

     Funzionamento:
     Ottiene un'istanza di ClientDAO tramite DAOFactory.
     Recupera la password associata all'email dal database (getPasswordByEmail()).

     Confronta la password recuperata con quella inserita dall'utente:
     Se diverse, lancia un'eccezione IncorrectPasswordException.
     Se uguali, il login è valido.*/
    public void verificaCredenziali(LoginBean bean) throws IncorrectPasswordException, UserDoesNotExistException {

        ClientDAO dao = DAOFactory.getDAOFactory().createClientDAO();         // Crea l'istanza corretta del DAO (Impostata nel file di configurazione)

        String password = dao.getPasswordByEmail(bean.getEmail());

        if (!password.equals(bean.getPassword())){
            throw new IncorrectPasswordException();
        }
    }

    /**2. loadUser(LoginBean bean)
     Scopo:
     Carica il profilo dell'utente dalla persistenza e crea un oggetto ClientBean per l'interfaccia grafica.

     Funzionamento:
     Ottiene un'istanza di ClientDAO tramite DAOFactory.
     Crea un oggetto Login (Model) per comunicare con il DAO.
     Carica il profilo dell'utente chiamando loadClient().
     Se l'utente è un normale User, crea un UserBean e carica le notifiche (retriveNotice()).
     Se l'utente è un Supervisor, crea un SupervisorBean senza notifiche.
     Restituisce l'oggetto ClientBean da mostrare nella UI.*/

    /** Recupera l'User dalla persistenza e crea una nuova istanza di UserBean */
    public ClientBean loadUser(LoginBean bean) throws UserDoesNotExistException, InvalidEmailException {

        ClientDAO dao = DAOFactory.getDAOFactory().createClientDAO();
        Login login = new Login(bean.getEmail(), bean.getPassword());           // Creo model Login per comunicare con il dao

        try{
            Client client = dao.loadClient(login);

            if(!client.isSupervisor()){
                UserBean userBean = new UserBean(client.getUsername(),client.getEmail(),client.getPreferences());

                List<NoticeBean> noticeBeanList = new ArrayList<>();
                List<Notice> noticeList = retriveNotice(client);

                for(Notice notice: noticeList){
                    NoticeBean noticeBean = new NoticeBean(notice.getTitle(),notice.getBody(),notice.getEmail());
                    noticeBeanList.add(noticeBean);
                }
                userBean.setNotices(noticeBeanList);
                return userBean;

            } else {
                return new SupervisorBean(client.getUsername(),client.getEmail(),client.getPreferences());
            }

        } catch (UserDoesNotExistException e){
            throw new UserDoesNotExistException();
        } catch (InvalidEmailException e) {
            throw new InvalidEmailException();
        }
    }

    /**3. retriveNotice(Client user)
     Scopo:
     Recupera tutte le notifiche (avvisi) associate all'utente.

     Funzionamento:
     Ottiene un'istanza di NoticeDAO tramite DAOFactory.
     Chiama retrieveNotice() per ottenere gli avvisi dal database.
     Restituisce una lista di oggetti Notice.*/

    public List<Notice> retriveNotice(Client user){
        NoticeDAO dao = DAOFactory.getDAOFactory().createNoticeDAO();
        return dao.retrieveNotice(user);
    }
}

/**Abstract Factory (DAOFactory)
 Permette di ottenere dinamicamente l'istanza corretta del DAO, indipendentemente dall'implementazione concreta.*/
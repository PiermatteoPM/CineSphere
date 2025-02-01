package engineering.pattern.abstract_factory;

import engineering.dao.*;
/** Questa classe è una concreta implementazione della
 *  Abstract Factory (DAOFactory), che fornisce oggetti DAO specifici per l'uso con MySQL.*/

/**Estende DAOFactory, quindi eredita il suo comportamento e implementa i metodi astratti.
 * Override dei metodi → Ogni metodo restituisce un'istanza di un DAO specifico per MySQL.*/

public class MySQLDAOFactory extends DAOFactory {
    /** qui ce l'ovverride sui metodi della factory, quindi si implementano restituendo i dao della persistenza scelta*/
    @Override
    public ClientDAO createClientDAO() {
        return new ClientDAOMySQL();
    }

    @Override
    public CollectionDAO createCollectionDAO() {
        return new CollectionDAOMySQL();
    }

    @Override
    public NoticeDAO createNoticeDAO() {
        return new NoticeDAOMySQL();
    }

}

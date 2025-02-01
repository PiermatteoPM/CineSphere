package engineering.pattern.abstract_factory;

import engineering.dao.*;

/** Questa classe è una concreta implementazione della
 *  Abstract Factory (DAOFactory), che fornisce oggetti DAO specifici per l'uso con la DEMO.*/

/**Estende DAOFactory, quindi eredita il suo comportamento e implementa i metodi astratti.
 * Override dei metodi → Ogni metodo restituisce un'istanza di un DAO specifico per la DEMO.*/

public class DemoDAOFactory extends DAOFactory {
    /** qui ce l'ovverride sui metodi della factory, quindi si implementano restituendo i dao della persistenza scelta*/
    @Override
    public ClientDAO createClientDAO() {
        return new ClientDAODemo();
    }

    @Override
    public CollectionDAO createCollectionDAO() {
        // Simulazione CollectionDAO
        return new CollectionDAODemo();
    }

    @Override
    public NoticeDAO createNoticeDAO() {
        // Simulazione NoticeDAO
        return new NoticeDAODemo();
    }
}

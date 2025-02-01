package engineering.pattern.abstract_factory;

import engineering.dao.*;

public class DemoDAOFactory extends DAOFactory {

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

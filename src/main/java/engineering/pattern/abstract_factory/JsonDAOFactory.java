package engineering.pattern.abstract_factory;

import engineering.dao.*;

public class JsonDAOFactory extends DAOFactory {
    @Override
    public ClientDAO createClientDAO() {
        return new ClientDAOJSON();
    }

    @Override
    public CollectionDAO createCollectionDAO() {
        return new CollectionDAOJSON();
    }

    @Override
    public NoticeDAO createNoticeDAO() {
        return new NoticeDAOJSON();
    }
}

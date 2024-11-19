package engineering.pattern.abstract_factory;

import engineering.dao.*;

public class JsonDAOFactory extends DAOFactory {
    @Override
    public ClientDAO createClientDAO() {
        return new ClientDAOJSON();
    }

    @Override
    public CollezioneDAO createCollezioneDAO() {
        return new CollezioneDAOJSON();
    }

    @Override
    public NoticeDAO createNoticeDAO() {
        return new NoticeDAOJSON();
    }
}

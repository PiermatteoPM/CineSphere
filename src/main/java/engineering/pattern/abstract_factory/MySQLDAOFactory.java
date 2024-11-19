package engineering.pattern.abstract_factory;

import engineering.dao.*;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public ClientDAO createClientDAO() {
        return new ClientDAOMySQL();
    }

    @Override
    public CollezioneDAO createCollezioneDAO() {
        return new CollezioneDAOMySQL();
    }

    @Override
    public NoticeDAO createNoticeDAO() {
        return new NoticeDAOMySQL();
    }

}

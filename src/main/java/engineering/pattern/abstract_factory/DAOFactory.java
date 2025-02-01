package engineering.pattern.abstract_factory;

import engineering.dao.*;
import engineering.others.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class DAOFactory {
    private static DAOFactory me = null;

    protected DAOFactory(){
    }

    /** Recupera dal file config.properties il tipo di persistenza utilizzata,

     se non è possibile come default viene utilizzato MYSQL */
    public static synchronized DAOFactory getDAOFactory(){
        if ( me == null ){
            Properties properties = new Properties();

            try (InputStream input = DAOFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
                properties.load(input);
            } catch (IOException e){
                Printer.errorPrint(e.getMessage());
            }

            String persistenceType = properties.getProperty("persistence.type", "MYSQL");
            var anEnum = Enum.valueOf(persistenceType);

            if (anEnum == Enum.MYSQL) {
                me = new MySQLDAOFactory();
            } else if (anEnum == Enum.JSON) {
                me = new JsonDAOFactory();
            } else if (anEnum == Enum.DEMO) {
                me = new DemoDAOFactory();
            }
        }
        return me;
    }

        public abstract ClientDAO createClientDAO();
        public abstract CollectionDAO createCollectionDAO();
        public abstract NoticeDAO createNoticeDAO();

    }
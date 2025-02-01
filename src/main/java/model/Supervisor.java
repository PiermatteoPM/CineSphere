package model;

import java.util.List;

public class Supervisor extends Client {
    /**supervisor impostato a true, ha dei privilegi diversi da quello dello user normale*/
    public Supervisor(String username, String email, List<String> preferences){
        super(username, email, preferences);
        super.supervisor = true;
    }
    /** aggiunto con la password per la versione demo*/
    public Supervisor(String username, String email, List<String> preferences,String password){
        super(username, email, preferences,password);
        super.supervisor = true;
    }

}
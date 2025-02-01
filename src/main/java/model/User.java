package model;

import java.util.List;

public class User extends Client {

    /**ovviamente un user normale ha il supervisor che da dei privilegi, impostato a false per default*/
    public User(String username, String email, List<String> preferences){
        super(username, email, preferences);
        super.supervisor = false;
    }
    /**aggiunto con password per la versione demo*/
    public User(String username, String email, List<String> preferences,String password){
        super(username, email, preferences,password);
        super.supervisor = false;
    }
}


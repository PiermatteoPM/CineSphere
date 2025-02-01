package model;

import java.util.List;

public class Supervisor extends Client {

    public Supervisor(String username, String email, List<String> preferences){
        super(username, email, preferences);
        super.supervisor = true;
    }

    public Supervisor(String username, String email, List<String> preferences,String password){
        super(username, email, preferences,password);
        super.supervisor = true;
    }

}
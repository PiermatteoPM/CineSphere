package model;

import java.util.List;

public class Login extends Client {

    /** Utilizzato in fase di login */
    public Login(String email, String password){
        setEmail(email);
        setPassword(password);
    }

    /** Utilizzato in fase di registrazione */
    public Login(String username, String email, String password, List<String> preferences){
        super(username, email, preferences);
        setPassword(password);
    }

    public Login() {

    }



}

package model;

import java.util.List;

/** Rappresenta la base di ogni utilizzatore della piattaforma */
public abstract class Client {

    private String email;
    private String username;
    private List<String> preferences;
    private String password;

    protected boolean supervisor; /**permette di dare dei privilegi diversi se impostato a true*/

    /** creazione di un'istanza senza inserire parametri*/
    protected Client(){}

    /** creazione di un oggetto client con i parametri per ogni cliente, senza password, quindi utile
     * per visualizzare le informazioni */
    protected Client(String username, String email, List<String> preferences){
        this.username = username;
        this.email = email;
        this.preferences = preferences;
    }
    /** utile quando si crea un utente e salvare tutte le sue informazioni, questo costruttore e la password sono
     * state aggiunte per permettere il funzionamento della parte demo*/
    protected Client(String username, String email, List<String> preferences,String password){
        this.username = username;
        this.email = email;
        this.preferences = preferences;
        this.password = password;
    }
    /** getter e setter, quelli riguardante la password sono state aggiunte per la demo*/
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }
    public List<String> getPreferences() {
        return preferences;
    }

    public String getPassword(){
        return password;
    }

    public boolean isSupervisor() {
        return supervisor;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

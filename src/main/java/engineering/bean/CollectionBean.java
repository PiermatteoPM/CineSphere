package engineering.bean;

import engineering.exceptions.LinkIsNotValidException;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;

public class CollectionBean {
    private String username;
    private String email;
    private String link;
    private String collectionName;
    private List<String> collectionGenre;
    private boolean approved = false;
    private String id;

    public CollectionBean() {
    }
    /**Se il metodo rileva un problema (ad esempio, un link non valido), invece di
     * gestirlo direttamente, "avvisa" chi lo chiama che potrebbe verificarsi
     * un errore, e lascia a loro la gestione dell'eccezione. Questo il throws e nome_eccezione*/
    public CollectionBean(String email, String username, String collectionName, String link, List<String> collectionGenre, boolean approved) throws LinkIsNotValidException {
        setEmail(email);
        setLink(link);
        setCollectionName(collectionName);
        setUsername(username);
        setCollectionGenre(collectionGenre);
        setApproved(approved);
    }

    public CollectionBean(String email, String username, String collectionName, String link, List<String> collectionGenre, boolean approved, String id) throws LinkIsNotValidException {
        this(email,username,collectionName,link, collectionGenre, approved);
        setId(id);
    }
    /**getter e setter*/

    public void setId(String id) {
        this.id = id;
    }
    /**setta il link e può dire a chi lo chiama che si può, verificare un eccezione, in questo caso lanciata
     * se il link non è valido. Altrimenti setta il link normalmente*/
    public void setLink(String link) throws LinkIsNotValidException {
        /**funzione che si trova sotto, per validare il link*/
        if(isValidLink(link)){
            this.link = link;
        } else {
            throw new LinkIsNotValidException();
        }
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setCollectionGenre(List<String> collectionGenre) {
        this.collectionGenre = collectionGenre;
    }
    public void setApproved(boolean approved){
        this.approved = approved;
    }

    public String getLink() {
        return link;
    }
    public String getUsername() {
        return username;
    }
    public String getCollectionName() {
        return collectionName;
    }
    public String getEmail() {
        return email;
    }
    public List<String> getCollectionGenre() {
        return collectionGenre;
    }
    public boolean getApproved(){
        return approved;
    }
    public String getId() {
        return id;
    }

    /**funzione che permette di validare il link, ce un import sopra*/
    private boolean isValidLink(String input) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(input);
    }
}

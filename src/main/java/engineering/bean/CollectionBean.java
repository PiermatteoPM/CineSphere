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

    public void setId(String id) {
        this.id = id;
    }
    public void setLink(String link) throws LinkIsNotValidException {
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


    private boolean isValidLink(String input) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(input);
    }
}

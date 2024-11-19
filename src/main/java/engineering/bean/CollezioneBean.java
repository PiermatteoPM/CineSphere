package engineering.bean;

import engineering.exceptions.LinkIsNotValidException;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;

public class CollezioneBean {
    private String username;
    private String email;
    private String link;
    private String collezioneName;
    private List<String> collezioneGenre;
    private boolean approved = false;
    private String id;

    public CollezioneBean() {
    }

    public CollezioneBean(String email, String username, String collezioneName, String link, List<String> collezioneGenre, boolean approved) throws LinkIsNotValidException {
        setEmail(email);
        setLink(link);
        setCollezioneName(collezioneName);
        setUsername(username);
        setCollezioneGenre(collezioneGenre);
        setApproved(approved);
    }

    public CollezioneBean(String email, String username, String collezioneName, String link, List<String> collezioneGenre, boolean approved, String id) throws LinkIsNotValidException {
        this(email,username,collezioneName,link, collezioneGenre, approved);
        setId(id);
    }

    /*public CollezioneBean(String email, String username, String collezioneName, String link, List<String> collezioneGenre, boolean approved) throws LinkIsNotValidException {
        this(email,username,collezioneName,link, collezioneGenre, approved);
    }*/


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
    public void setCollezioneName(String collezioneName) {
        this.collezioneName = collezioneName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setCollezioneGenre(List<String> collezioneGenre) {
        this.collezioneGenre = collezioneGenre;
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
    public String getCollezioneName() {
        return collezioneName;
    }
    public String getEmail() {
        return email;
    }
    public List<String> getCollezioneGenre() {
        return collezioneGenre;
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

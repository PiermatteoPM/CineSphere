package model;

import java.util.*;

public class Collezione {
    private String id;
    private String collezioneName;
    private String link;
    private String username;
    private String email;
    private List<String> collezioneGenre;
    private boolean approved;

    public Collezione(){
        this.approved = false;
    }

    public Collezione(String email, String username, String collezioneName, String link, List<String> collezioneGenre, boolean approved){
        this.email = email;
        this.username = username;
        this.collezioneName = collezioneName;
        this.link = link;
        this.collezioneGenre = collezioneGenre;
        this.approved = approved;
        this.id = "";
    }

    public Collezione(String email, String username, String collezioneName, String link, List<String> collezioneGenre, boolean approved, String id){
        this(email, username, collezioneName, link, collezioneGenre, approved);
        this.id = id;
    }

    /*public Collezione(String email, String username, String collezioneName, String link, List<String> collezioneGenre, boolean approved){
        this(email,username,collezioneName,link, collezioneGenre, approved);
        setId("");
        this.id = "";
    }*/




    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


    public void setCollezioneName(String collezioneName) {
        this.collezioneName = collezioneName;
    }

    public String getCollezioneName() {
        return collezioneName;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setCollezioneGenre(List<String> collezioneGenre) {
        this.collezioneGenre = collezioneGenre;
    }

    public List<String> getCollezioneGenre() {
        return collezioneGenre;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public void setApproved(boolean approved){
        this.approved = approved;
    }

    public boolean getApproved(){
        return approved;
    }


}

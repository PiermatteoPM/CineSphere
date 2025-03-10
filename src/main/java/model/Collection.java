package model;

import java.util.*;

public class Collection {
    private String id;
    private String collectionName;
    private String link;
    private String username;
    private String email;
    private List<String> collectionGenre;
    private boolean approved;

    public Collection(){
        this.approved = false;
    }

    public Collection(String email, String username, String collectionName, String link, List<String> collectionGenre, boolean approved){
        this.email = email;
        this.username = username;
        this.collectionName = collectionName;
        this.link = link;
        this.collectionGenre = collectionGenre;
        this.approved = approved;
        this.id = "";
    }

    public Collection(String email, String username, String collectionName, String link, List<String> collectionGenre, boolean approved, String id){
        this(email, username, collectionName, link, collectionGenre, approved);
        this.id = id;
    }


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


    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setCollectionGenre(List<String> collectionGenre) {
        this.collectionGenre = collectionGenre;
    }

    public List<String> getCollectionGenre() {
        return collectionGenre;
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

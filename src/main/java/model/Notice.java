package model;

public class Notice {
    private String email;
    private String title;
    private String body;

    /**inizializza l'oggetto notice per intero*/
    public Notice(String title, String body, String email){
        this.email = email;
        this.title = title;
        this.body = body; /**contenuto testuale della notifica*/
    }
    /**getter e setter*/
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**utile tenerlo per completezza, altrimenti in futuro il contenuto della notifica non può
     * essere mai aggiornato*/
    public void setBody(String body) {
        this.body = body;
    }
    public String getBody() {
        return body;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

package engineering.dao;

import engineering.exceptions.CollezioneLinkAlreadyInUseException;
import engineering.exceptions.CollezioneNameAlreadyInUseException;
import model.Collezione;

import java.util.List;

public interface CollezioneDAO {

    /** Inserisce una collezione in persistenza*/
    void insertCollezione(Collezione collezione) throws CollezioneLinkAlreadyInUseException, CollezioneNameAlreadyInUseException;

    /** Non serve che crea una nuova istanza di Collezione*/
    Collezione approveCollezione(Collezione collezione);

    /** Elimina la collezione */
    void deleteCollezione(Collezione collezione);


    /** Recupera tutte le collezione dell'utente dall'email */
    List<Collezione> retrieveCollezionesByEmail(String email);
    List<Collezione> retrievePendingColleziones();
    List<Collezione> retrieveApprovedColleziones();


    /** Recupera tutte le collezione, filtrandole per genere  */
    List<Collezione> searchCollezioneByFilters(Collezione collezione);

    List<Collezione> searchCollezioneByTitle(Collezione collezione);

    List<Collezione> searchCollezioneByGenre(Collezione collezione);

}


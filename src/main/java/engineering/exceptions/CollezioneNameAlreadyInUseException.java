package engineering.exceptions;

public class CollezioneNameAlreadyInUseException extends Exception {
    public CollezioneNameAlreadyInUseException() {
        super("This title is already used!");
    }
}

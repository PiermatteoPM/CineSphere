package engineering.exceptions;

public class CollezioneLinkAlreadyInUseException extends Exception {
    public CollezioneLinkAlreadyInUseException() {
        super("This link is already used!");
    }
}

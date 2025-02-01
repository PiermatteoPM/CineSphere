package engineering.exceptions;

public class CollectionLinkAlreadyInUseException extends Exception {
    public CollectionLinkAlreadyInUseException() {
        super("This link is already used!");
    }
}

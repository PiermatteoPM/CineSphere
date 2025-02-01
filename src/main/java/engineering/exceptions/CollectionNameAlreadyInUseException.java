package engineering.exceptions;

public class CollectionNameAlreadyInUseException extends Exception {
    public CollectionNameAlreadyInUseException() {
        super("This title is already used!");
    }
}

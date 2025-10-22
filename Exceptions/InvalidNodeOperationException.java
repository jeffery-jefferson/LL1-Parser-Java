package Exceptions;

public class InvalidNodeOperationException extends InvalidNodeException {
    public InvalidNodeOperationException(String exceptionMessage) {
        super(exceptionMessage);
    }
    public InvalidNodeOperationException() {}
}

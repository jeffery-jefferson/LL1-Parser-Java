package Exceptions;

public class InvalidTableOperationException extends Exception {
    public InvalidTableOperationException(String exceptionMessage) {
        super(exceptionMessage);
    }
    public InvalidTableOperationException() {}
}

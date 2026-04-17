package Demo_Login.Demo.Exceptions.ExceptionsClasses;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}

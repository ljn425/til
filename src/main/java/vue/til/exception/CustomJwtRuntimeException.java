package vue.til.exception;

public class CustomJwtRuntimeException extends RuntimeException{
    public CustomJwtRuntimeException(String message) {
        super(message);
    }
}

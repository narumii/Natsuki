package pw.narumi.exception;

public class NatsukiException extends RuntimeException {

    public NatsukiException() {
        super();
    }

    public NatsukiException(final String message) {
        super(message);
    }

    public NatsukiException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NatsukiException(final Throwable cause) {
        super(cause);
    }

    protected NatsukiException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

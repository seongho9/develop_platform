package me.seongho9.dev.excepction.container;

public class ContainerConflictException extends RuntimeException {
    public ContainerConflictException() {
        super();
    }

    public ContainerConflictException(String message) {
        super(message);
    }

    public ContainerConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerConflictException(Throwable cause) {
        super(cause);
    }

    protected ContainerConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

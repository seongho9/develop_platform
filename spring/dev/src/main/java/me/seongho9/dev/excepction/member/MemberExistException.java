package me.seongho9.dev.excepction.member;

public class MemberExistException extends RuntimeException {
    public MemberExistException() {
        super();
    }

    public MemberExistException(String message) {
        super(message);
    }

    public MemberExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberExistException(Throwable cause) {
        super(cause);
    }

    protected MemberExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

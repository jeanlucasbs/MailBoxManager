package com.jeanlucas.mailboxmanager.Exception;

public class InvalidSubjectException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidSubjectException(String message) {super(message);}
}

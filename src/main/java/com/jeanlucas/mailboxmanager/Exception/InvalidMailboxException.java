package com.jeanlucas.mailboxmanager.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMailboxException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InvalidMailboxException(String message){
        super(message);
    }
}

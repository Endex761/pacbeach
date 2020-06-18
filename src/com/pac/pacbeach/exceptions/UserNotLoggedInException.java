package com.pac.pacbeach.exceptions;

public class UserNotLoggedInException extends Exception
{
    public UserNotLoggedInException(){
        super("Utente non loggato.");
    }
}

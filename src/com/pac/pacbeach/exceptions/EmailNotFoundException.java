package com.pac.pacbeach.exceptions;

public class EmailNotFoundException extends Exception
{
    public EmailNotFoundException()
    {
        super("Utente con email inserita non trovato.");
    }
}

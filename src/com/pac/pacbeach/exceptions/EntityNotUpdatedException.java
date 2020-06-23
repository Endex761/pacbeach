package com.pac.pacbeach.exceptions;

public class EntityNotUpdatedException extends Exception
{
    public EntityNotUpdatedException()
    {
        super("Errore nell'aggiornamento dei dati.");
    }
}


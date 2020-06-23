package com.pac.pacbeach.exceptions;

public class EntityNotDeletedException extends Exception
{
    public EntityNotDeletedException()
    {
        super("Errore nella cancellazione.");
    }
}

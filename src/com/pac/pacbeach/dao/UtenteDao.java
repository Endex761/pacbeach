package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EntityNotUpdatedException;
import com.pac.pacbeach.model.Utente;

import javax.persistence.NoResultException;

/**
 * Classe per l'accesso ai dati degli utenti
 */
public class UtenteDao extends Dao
{
    public static Utente creaUtente(Utente utente) throws DuplicatedEntryException
    {
        return (Utente) create(utente);
    }

    public static Utente getUtente(String email) throws NoResultException
    {
        String queryString = "from Utente where email = ?0";

        return (Utente) getOne(queryString, email);
    }

    public static Utente getUtente(int idUtente) throws NoResultException
    {
        String queryString = "from Utente where idUtente = ?0";

        return (Utente) getOne(queryString, idUtente);
    }

    public static void aggiornaUtente(Utente utente) throws EntityNotUpdatedException
    {
       update(utente);
    }
}

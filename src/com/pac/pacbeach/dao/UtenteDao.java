package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EmailNotFoundException;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class UtenteDao extends Dao
{
    public static void creaUtente(Utente utente) throws DuplicatedEntryException
    {
        create(utente);
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

    public static void aggiornaUtente(Utente utente)
    {
       update(utente);
    }
}

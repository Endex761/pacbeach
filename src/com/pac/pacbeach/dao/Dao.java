package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EntityNotDeletedException;
import com.pac.pacbeach.exceptions.EntityNotUpdatedException;
import com.pac.pacbeach.utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Classe di base per Data Access Object
 * Viene estesa dalle seingole classi DAO
 */
public class Dao
{
    /**
     * Metodo per la creazione di un ogetto all'interno del Database
     * @param obj da creare
     * @throws DuplicatedEntryException se viene violata la chiave primaria
     * @return oggetto aggiornato con id nel database
     */
    protected static Object create(Object obj) throws DuplicatedEntryException
    {
        Session session = HibernateUtils.getSession();

        Transaction transaction = null;
        try
        {
            transaction = session.beginTransaction();

            session.save(obj);

            transaction.commit();

            return obj;
        }
        catch (ConstraintViolationException e)
        {
            //1062 duplicated primary key
            if(e.getErrorCode() == 1062)
                throw new DuplicatedEntryException();

            assert transaction != null;
            transaction.rollback();

            return null;
        }

    }

    /**
     * Aggiorna un ogetto all'interno del database
     * @param obj ogetto modificato
     */
    protected static void update(Object obj) throws EntityNotUpdatedException
    {
        Session session = HibernateUtils.getSession();

        Transaction transaction = null;

        try
        {
            transaction = session.beginTransaction();

            session.update(obj);

            transaction.commit();
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            assert transaction != null;
            transaction.rollback();
            throw new EntityNotUpdatedException();
        }
    }

    /**
     * Recupera un singolo elemento dal database eseguendo una query
     * @param queryString query in HQL (Hibernate Query Language)
     * @param params lista ordinata di parametri della query
     * @return l'ogetto richiesto dalla query
     * @throws NoResultException se la query non restituisce alcun risultato
     */
    protected static Object getOne(String queryString, Object ...params ) throws NoResultException
    {
        Session session = HibernateUtils.getSession();

        Query query = session.createQuery(queryString);

        for(int i = 0; i < params.length; ++i)
            query.setParameter(i, params[i]);

        return query.getSingleResult();
    }

    /**
     * Recupera una lista di elementi dal database eseguendo una query
     * @param queryString query in HQL (Hibernate Query Language)
     * @param params lista ordinata di parametri della query
     * @return lista di ogetti richiesti dalla query
     * @throws NoResultException se la query non restituisce alcun risultato
     */
    protected static List get(String queryString, Object ...params) throws NoResultException
    {
        Session session = HibernateUtils.getSession();

        Query query = session.createQuery(queryString);

        for(int i = 0; i < params.length; ++i)
            query.setParameter(i, params[i]);

        return query.getResultList();
    }

    /**
     * Elimina un ogetto dal database
     * @param obj ogetto da eliminare
     * @throws EntityNotDeletedException se avviene un errore in fase di cancellazione
     */
    protected static void delete(Object obj) throws EntityNotDeletedException
    {
        Session session = HibernateUtils.getSession();

        Transaction transaction = null;
        try
        {
            transaction = session.beginTransaction();

            session.delete(obj);

            transaction.commit();
        }
        catch (HibernateException e)
        {
            //TODO remove
            e.printStackTrace();
            assert transaction != null;
            transaction.rollback();
            throw new EntityNotDeletedException();
        }

    }
}

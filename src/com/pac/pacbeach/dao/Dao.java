package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.HibernateUtils;
import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class Dao
{
    protected static void create(Object obj) throws DuplicatedEntryException
    {
        Session session = HibernateUtils.getSession();

        Transaction transaction = null;
        try
        {
            transaction = session.beginTransaction();

            session.save(obj);

            transaction.commit();
        }
        catch (ConstraintViolationException e)
        {
            //1062 duplicated email
            if(e.getErrorCode() == 1062)
                throw new DuplicatedEntryException();

            transaction.rollback();
        }
    }

    protected static void update(Object obj)
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
            transaction.rollback();
        }
    }

    protected static Object getOne(String queryString, Object ...params ) throws NoResultException
    {
        Session session = HibernateUtils.getSession();

        Query query = session.createQuery(queryString);

        for(int i = 0; i < params.length; ++i)
            query.setParameter(i, params[i]);

        return query.getSingleResult();
    }

    protected static List get(String queryString, Object ...params) throws NoResultException
    {
        Session session = HibernateUtils.getSession();

        Query query = session.createQuery(queryString);

        for(int i = 0; i < params.length; ++i)
            query.setParameter(i, params[i]);

        return query.getResultList();
    }

    protected static void delete(Object obj)
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
            e.printStackTrace();
            transaction.rollback();
        }
    }
}

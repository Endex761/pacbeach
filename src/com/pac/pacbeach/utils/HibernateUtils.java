package com.pac.pacbeach.utils;

import com.pac.pacbeach.model.*;

import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;

/**
 * Classe per la gestione della persistenza con Hibernate
 */
public class HibernateUtils
{
    private static SessionFactory sessionFactory;
    private static Boolean serverStarted = false;

    /**
     * Funzione di inizializzazione di una session factory.
     */
    public static void initSessionFactory()
    {
        Configuration config = new Configuration();

        config.configure("hibernate-config.xml");

        config.addAnnotatedClass(Utente.class);
        config.addAnnotatedClass(Ombrellone.class);
        config.addAnnotatedClass(Prenotazione.class);
        config.addAnnotatedClass(Prodotto.class);
        config.addAnnotatedClass(Ordine.class);
        config.addAnnotatedClass(ProdottoOrdine.class);

        sessionFactory = config.buildSessionFactory();
    }

    /**
     * Funzione che restituisce una sessione di connessione al DB
     */
    public static Session getSession()
    {
        if(sessionFactory == null)
            initSessionFactory();

        Session session = null;
        try
        {
            session = sessionFactory.openSession();
        }
        catch (HibernateError he)
        {
            System.err.println("MySql Server is not running");
        }

        return session;
    }

    /**
     * Funzione richiamata una sola volta per avviare il Server Hibernate ed evitare tempi di attesa durante il primo login/registrazione
     */
    public static void initServer()
    {
        if(!serverStarted)
        {
            Session s = getSession();
            Query q = s.createQuery("from Prodotto where id=1");
            q.getResultList();

            serverStarted = true;
        }
    }
}

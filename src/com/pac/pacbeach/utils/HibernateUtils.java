package com.pac.pacbeach.utils;

import com.pac.pacbeach.model.Ombrellone;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.Utente;

import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

//Classe per gestire le sessioni di connessione con il databse.
public class HibernateUtils
{
    private static SessionFactory sessionFactory;

    //Inizializza una nuova factory per le sessioni.
    public static void initSessionFactory()
    {
        Configuration config = new Configuration();
        config.configure("hibernate-config.xml");
        config.addAnnotatedClass(Utente.class);
        config.addAnnotatedClass(Ombrellone.class);
        config.addAnnotatedClass(Prenotazione.class);
        StandardServiceRegistry srvcReg = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        sessionFactory = config.buildSessionFactory();
    }

    //Ottieni una sessione di connessione al database
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
            //TODO remove
            he.printStackTrace();
        }

        return session;
    }
}

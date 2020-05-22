package com.pac.pacbeach.utils;

import com.pac.pacbeach.model.Utente;

import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtils
{
    private static SessionFactory sessionFactory;

    public static void initSessionFactory()
    {
        Configuration config = new Configuration();
        config.configure("hibernate-config.xml");
        config.addAnnotatedClass(Utente.class);
        StandardServiceRegistry srvcReg = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        sessionFactory = config.buildSessionFactory();
    }

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

package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EntityNotUpdatedException;
import com.pac.pacbeach.model.Ombrellone;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Classe per l'accesso ai dati degli ombrelloni
 */
public class OmbrelloneDao extends Dao
{
    public static void creaOmbrellone(Ombrellone ombrellone) throws DuplicatedEntryException
    {
        create(ombrellone);
    }

    public static Ombrellone getOmbrellone(int idOmbrellone) throws NoResultException
    {
        String queryString = "from Ombrellone where idOmbrellone = ?0";

        return (Ombrellone) getOne(queryString, idOmbrellone);
    }

    public static List<Ombrellone> getOmbrelloni() throws NoResultException
    {
        String queryString = "from Ombrellone";

        return (List<Ombrellone>) get(queryString);
    }

    public static void aggiornaOmbrellone(Ombrellone ombrellone) throws EntityNotUpdatedException
    {
        update(ombrellone);
    }
}

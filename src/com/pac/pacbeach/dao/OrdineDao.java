package com.pac.pacbeach.dao;

import com.pac.pacbeach.model.Ordine;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Classe per l'accesso ai dati degli ordini
 */
public class OrdineDao extends Dao
{
    public static List<Ordine> ordiniPrenotazione(Integer idPrenotazione) throws NoResultException
    {
        String queryString = "from Ordine where refPrenotazione = ?0";

        return (List<Ordine>) get(queryString, idPrenotazione);
    }

}

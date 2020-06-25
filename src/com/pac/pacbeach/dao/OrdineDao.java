package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EntityNotUpdatedException;
import com.pac.pacbeach.model.Ordine;
import com.pac.pacbeach.model.Prenotazione;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Classe per l'accesso ai dati degli ordini
 */
public class OrdineDao extends Dao
{
    public static Ordine creaOrdine(Ordine o) throws DuplicatedEntryException
    {
        return (Ordine) create(o);
    }

    public static List<Ordine> ordiniPrenotazione(Integer idPrenotazione) throws NoResultException
    {
        String queryString = "from Ordine where refPrenotazione = ?0";

        return (List<Ordine>) get(queryString, idPrenotazione);
    }

    public static List<Ordine> ordiniUtente(Integer idUtente) throws NoResultException
    {
        String queryString = "FROM Ordine o INNER JOIN o.prenotazione as p INNER JOIN p.utente as u WHERE u.idUtente = ?0";

        return (List<Ordine>) get(queryString, idUtente);
    }

    public static List<Ordine> ordiniDaConsegnare() throws NoResultException
    {
        String queryString = "FROM Ordine WHERE stato != 'D'";

        return (List<Ordine>) get(queryString);
    }

    public static void aggiornaOrdine(Ordine o) throws EntityNotUpdatedException
    {
        update(o);
    }

    public static Ordine ordinePerId(Integer idOrdine) throws NoResultException
    {
        String queryString = "from Ordine where idOrdine = ?0";

        return (Ordine) getOne(queryString, idOrdine);
    }

}

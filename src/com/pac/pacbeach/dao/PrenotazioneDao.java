package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.model.Prenotazione;


import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Classe per l'accesso ai dati delle prenotazioni
 */
public class PrenotazioneDao extends Dao
{
    public static Prenotazione getPrenotazione(int idPrenotazione) throws NoResultException
    {
        String queryString = "from Prenotazione where idPrenotazione = ?0";

        return (Prenotazione) getOne(queryString, idPrenotazione);
    }

    public static List<Prenotazione> getPrenotazioni(Timestamp orarioInizio, Timestamp orarioFine, int idOmbrellone) throws NoResultException
    {
        String queryString = "from Prenotazione where refOmbrellone = ?0 and (orarioFine > ?1 and orarioInizio < ?2) or (orarioInizio > ?3 and orarioFine < ?4)";

        return (List<Prenotazione>) get(queryString, idOmbrellone, orarioInizio, orarioFine, orarioInizio, orarioFine);

    }

    public static List<Prenotazione> getPrenotazioni(Timestamp orarioInizio, Timestamp orarioFine) throws NoResultException
    {
        String queryString = "from Prenotazione where (orarioFine > ?0 and orarioInizio < ?1) or (orarioInizio > ?2 and orarioFine < ?3)";

        return (List<Prenotazione>) get(queryString, orarioInizio,orarioFine, orarioInizio, orarioFine);
    }

    public static List<Prenotazione> getPrenotazioniUtente(int idUtente, Timestamp orarioInizio, Timestamp orarioFine) throws NoResultException
    {
        String queryString = "from Prenotazione where refUtente = ?0 and (orarioFine > ?1 and orarioInizio < ?2) or (orarioInizio > ?3 and orarioFine < ?4)";

        return (List<Prenotazione>) get(queryString, idUtente, orarioInizio, orarioFine, orarioInizio, orarioFine);
    }

    public static void creaPrenotazione(Prenotazione prenotazione) throws DuplicatedEntryException
    {
        create(prenotazione);
    }

    public static void aggiornaPrenotazione(Prenotazione prenotazione)
    {
        update(prenotazione);
    }


}

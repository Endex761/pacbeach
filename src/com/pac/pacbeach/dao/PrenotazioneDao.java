package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.model.Prenotazione;


import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.util.List;

public class PrenotazioneDao extends Dao
{
    public static Prenotazione getPrenotazione(int idPrenotazione) throws NoResultException
    {
        String queryString = "from Prenotazione where idPrenotazione = ?0";

        return (Prenotazione) getOne(queryString, idPrenotazione);
    }

    public static List<Prenotazione> getPrenotazioni(Timestamp orarioInizio, Timestamp orarioFine, int idOmbrellone)
    {
        String queryString = "from Prenotazione where refOmbrellone = ?0 and (orarioFine > ?1 or orarioInizio > ?2)";

        return (List<Prenotazione>) get(queryString, idOmbrellone, orarioInizio, orarioFine);

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

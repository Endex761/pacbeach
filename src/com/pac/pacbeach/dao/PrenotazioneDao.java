package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.model.Prenotazione;


import javax.persistence.NoResultException;

public class PrenotazioneDao extends Dao
{
    public static Prenotazione getPrenotazione(int idPrenotazione) throws NoResultException
    {

        String queryString = "from Prenotazione where idPrenotazione = ?0";

        return (Prenotazione) getOne(queryString, idPrenotazione);

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

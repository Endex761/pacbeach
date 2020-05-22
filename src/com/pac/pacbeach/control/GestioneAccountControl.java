package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.UtenteDao;
import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EmailNotFoundException;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.Result;

import javax.persistence.NoResultException;

public class GestioneAccountControl
{
    public static Result creaNuovoUtente(String email, String nome, String cognome, String telefono)
    {
        Utente utente = new Utente(email, nome, cognome, telefono);
        try
        {
            UtenteDao.creaUtente(utente);
        }
        catch (DuplicatedEntryException e)
        {
            return new Result("Esiste già un utente con questa mail.", false);
        }

        return new Result("Utente creato con successo.");
    }

    public static Result getUtenteByEmail(String email)
    {
        try
        {
            Utente u = UtenteDao.getUtente(email);

            return new Result("Dati utente",true, u);
        }
        catch (NoResultException e)
        {
            return new Result("Nessun utente con questa email.", false);
        }
    }

    public static Result aggiornaUtente(Utente u)
    {
        UtenteDao.aggiornaUtente(u);

        return new Result("Utente aggiornato con successo.");
    }
}

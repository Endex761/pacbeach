package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.PrenotazioneDao;
import com.pac.pacbeach.dao.UtenteDao;
import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.Result;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.sql.Date;

public class GestionePrenotazioneControl
{
    public static Result creaNuovaPrenotazione(String orario, String pagata, String idUtente, String idOmbrellone)
    {
        try
        {
            int id = Integer.parseInt(idUtente);
            int ombrellone = Integer.parseInt(idOmbrellone);
            Utente utente = UtenteDao.getUtente(id);
            float costo = 30.00F; //=generaCosto();
            Date ora = new Date(System.currentTimeMillis());
            Boolean pag = Boolean.parseBoolean(pagata);
            Prenotazione prenotazione = new Prenotazione(ora, pag, costo, utente,ombrellone);

            PrenotazioneDao.creaPrenotazione(prenotazione);

            return new Result("Prenotazione creata con successo.");

        }
        catch (NumberFormatException e)
        {
            return new Result("Errore, id utente errato.", false);
        }
        catch (NoResultException e)
        {
            return new Result("Errore, utente non trovato.", false);
        }
        catch (DuplicatedEntryException e)
        {
            return new Result("Errore, idPrenotazione duplicato.");
        }


    }
}

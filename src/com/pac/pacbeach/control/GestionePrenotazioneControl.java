package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.OmbrelloneDao;
import com.pac.pacbeach.dao.PrenotazioneDao;
import com.pac.pacbeach.dao.UtenteDao;
import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.model.Ombrellone;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.Result;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class GestionePrenotazioneControl
{
    public static Result creaNuovaPrenotazione(String orarioInizio, String orarioFine, String pagata, String idUtente, String idOmbrellone)
    {
        try
        {
            int idUtenteInt = Integer.parseInt(idUtente);
            int idOmbrelloneInt = Integer.parseInt(idOmbrellone);

            float costo = 30.00F; //=generaCosto();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Timestamp orarioInizioDate = new Timestamp(dateFormat.parse(orarioInizio.replace('T',' ')).getTime());
            Timestamp orarioFineDate = new Timestamp(dateFormat.parse(orarioFine.replace('T', ' ')).getTime());;

            Boolean pagatoBoolean = Boolean.parseBoolean(pagata);

            Boolean libero = controllaAltrePrenotazioni(orarioInizioDate, orarioFineDate, idOmbrelloneInt);

            Ombrellone ombrellone = OmbrelloneDao.getOmbrellone(idOmbrelloneInt);

            if(ombrellone.isPrenotabile() && libero)
            {
                Utente utente = UtenteDao.getUtente(idUtenteInt);

                Prenotazione prenotazione = new Prenotazione(orarioInizioDate, orarioFineDate, pagatoBoolean, costo, utente, ombrellone);

                PrenotazioneDao.creaPrenotazione(prenotazione);

                return new Result("Prenotazione creata con successo.");
            }
            else
            {
                return  new Result("Errore, ombrellone non prenotabile.", false);
            }


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
            return new Result("Errore, ID prenotazione duplicato.", false);
        }
        catch (ParseException e)
        {
            return new Result("Erroe, data nel formato non valido.", false);
        }


    }

    private static boolean controllaAltrePrenotazioni(Timestamp orarioInizio, Timestamp orarioFine, int idOmbrellone)
    {
        try
        {
            List<Prenotazione> prenotazioni = PrenotazioneDao.getPrenotazioni(orarioInizio, orarioFine, idOmbrellone);

            if(prenotazioni.isEmpty())
                return true;
        }
        catch (NoResultException e)
        {
            return true;
        }

        return false;
    }
}

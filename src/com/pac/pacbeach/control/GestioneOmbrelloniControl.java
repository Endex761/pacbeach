package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.OmbrelloneDao;
import com.pac.pacbeach.dao.PrenotazioneDao;
import com.pac.pacbeach.model.Ombrellone;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.wrapper.WrapperArrayList;
import com.pac.pacbeach.utils.DateFormatter;
import com.pac.pacbeach.utils.Result;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

/**
 * Classe controller per la gestione degli ombrelloni
 */
public class GestioneOmbrelloniControl
{
    /**
     * Metodo per ottenere lo stato degli ombrelloni data una fascia oraria
     * @param orarioInizio inizio fascia oraria
     * @param orarioFine fine fascia oraria
     * @return oggetto Result che contiene una lista di ombrelloni con flag prenotabile impostato in base alla disponibilita nelle fasce orarie scelte
     */
    public static Result statoOmbrelloni(String orarioInizio, String orarioFine)
    {
        try
        {
            //Converto le date da stringa a timestamp
            Timestamp orarioInizioDate = DateFormatter.format(orarioInizio);
            Timestamp orarioFineDate = DateFormatter.format(orarioFine);

            //Prendo dal database la lista degli ombrelloni e delle prenotazioni comprese tra orarioInizio e orarioFine
            List<Ombrellone> ombrelloni = OmbrelloneDao.getOmbrelloni();
            List<Prenotazione> prenotazioni = PrenotazioneDao.getPrenotazioni(orarioInizioDate, orarioFineDate);


            //Per ogni ombrellone setto se è prenotabile o no durante la fascia oraria specificata.
            for(Prenotazione p : prenotazioni)
            {
                for(int i = 0; i < ombrelloni.size(); ++i)
                {
                    if(p.getOmbrellone().getIdOmbrellone().equals(ombrelloni.get(i).getIdOmbrellone()))
                    {
                        //Setta lo stato dell'ombrellone a non prenotabile
                        Ombrellone o = ombrelloni.get(i);
                        o.setPrenotabile(false);
                        ombrelloni.set(i, o);
                    }
                }
            }

            //Lista aggiornata degli ombrelloni da inviare al front-end
            WrapperArrayList<Ombrellone> wrapperOmbrelloni = new WrapperArrayList<>(ombrelloni);

            return new Result(wrapperOmbrelloni,"ombrelloni");
        }
        catch (NoResultException e)
        {
            return new Result("Errore, nessun ombrellone trovato.", false);
        }
        catch (ParseException e)
        {
            return new Result("Errore, formato data errato.", false);
        }



    }
}

package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.OmbrelloneDao;
import com.pac.pacbeach.dao.PrenotazioneDao;
import com.pac.pacbeach.model.Ombrellone;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.wrapper.WrapperArrayList;
import com.pac.pacbeach.utils.Result;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class GestioneOmbrelloniControl
{
    public static Result statoOmbrelloni(String orarioInizio, String orarioFine)
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Timestamp orarioInizioDate = new Timestamp(dateFormat.parse(orarioInizio.replace('T',' ')).getTime());
            Timestamp orarioFineDate = new Timestamp(dateFormat.parse(orarioFine.replace('T', ' ')).getTime());

            List<Ombrellone> ombrelloni = OmbrelloneDao.getOmbrelloni();
            List<Prenotazione> prenotazioni = PrenotazioneDao.getPrenotazioni(orarioInizioDate, orarioFineDate);


            //Per ogni ombrellone setto se è prenotabile o no durante la fascia oraria specificata.
            for(Prenotazione p : prenotazioni)
            {
                for(int i = 0; i < ombrelloni.size(); ++i)
                {
                    if(p.getOmbrellone().getIdOmbrellone().equals(ombrelloni.get(i).getIdOmbrellone()))
                    {
                        Ombrellone o = ombrelloni.get(i);
                        o.setPrenotabile(false);
                        ombrelloni.set(i, o);
                    }
                }
            }

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

package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.OmbrelloneDao;
import com.pac.pacbeach.dao.PrenotazioneDao;
import com.pac.pacbeach.dao.UtenteDao;
import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.model.Ombrellone;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.model.wrapper.WrapperArrayList;
import com.pac.pacbeach.utils.DateFormatter;
import com.pac.pacbeach.utils.Result;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

/**
 * Classe control per la gestione delle prenotazioni
 */
public class GestionePrenotazioneControl
{
    /**
     * Metodo per la creazione di una nuova prenotazione
     * @param orarioInizio orario di inzio della prenotazione
     * @param orarioFine orario di fine della prenotazione
     * @param pagata indica se l'utente ha già pagato la prenotazione
     * @param ospiti lista degli ospiti dell'ombrellone
     * @param idUtente id dell'utente che ha effettuato la prenotazione
     * @param idOmbrellone id delgli ombrelloni da prenotare (separati da ,)
     * @return oggetto Result con informazioni sullo stato della prenotazione
     */
    public static Result creaNuovaPrenotazione(String orarioInizio, String orarioFine, String pagata, String ospiti, Integer idUtente, String idOmbrellone)
    {
        try
        {
            //Ottengo la lista degli ombrelloni che l'utente desidera prenotare
            String[] idOmbrelloni = idOmbrellone.split(",");

            //Lista degli id interi degli ombrelloni
            int[] idOmbrelloniInt = new int[idOmbrelloni.length];

            //Calcolo del costo totale
            float costo = 30.00F; //=generaCosto(); TODO genera costo


            //Converto le date da stringa a timestamp
            Timestamp orarioInizioDate = DateFormatter.formatDate(orarioInizio);
            Timestamp orarioFineDate = DateFormatter.formatDate(orarioFine);

            //Converto il flag pagato
            Boolean pagatoBoolean = Boolean.parseBoolean(pagata);

            boolean tuttiLiberi = true;

            //Per ogni ombrellone da prenotare controllo che non sia già prenotato nelle fasce orarie scelte
            for(int i = 0; i < idOmbrelloniInt.length; ++i)
            {
                idOmbrelloniInt[i] = Integer.parseInt(idOmbrelloni[i]);

                boolean libero = controllaAltrePrenotazioni(orarioInizioDate, orarioFineDate, idOmbrelloniInt[i]);

                Ombrellone ombrellone = OmbrelloneDao.getOmbrellone(idOmbrelloniInt[i]);

                if(!libero || !ombrellone.isPrenotabile())
                {
                    tuttiLiberi = false;
                    break;
                }
            }

            //Se tutti gli ombrelloni selezionati sono prenotabili creo le singole prenotazioni
            if(tuttiLiberi)
            {
                Utente utente = UtenteDao.getUtente(idUtente);

                for(int i = 0; i < idOmbrelloniInt.length; ++i)
                {
                    Ombrellone ombrellone = OmbrelloneDao.getOmbrellone(idOmbrelloniInt[i]);

                    Prenotazione prenotazione = new Prenotazione(orarioInizioDate, orarioFineDate, pagatoBoolean, costo, ospiti, utente, ombrellone);

                    //Salvo la prenotazione nel database
                    PrenotazioneDao.creaPrenotazione(prenotazione);
                }

                return new Result("Prenotazione creata con successo.");

            }
            else
            {
                return new Result("Errore, ombrellone non prenotabile.", false);
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
            return new Result("Errore, data nel formato non valido.", false);
        }
    }

    /**
     * Metodo per ottenere l'elenco delle prenotazione in una fascia oraria
     * @param orarioInizio inizio fascia oraria
     * @param orarioFine fine fascia oraria
     * @return oggetto Result con lista di prenotazioni richieste
     */
    public static Result elencoPrenotazioni(String orarioInizio, String orarioFine)
    {
        try
        {
            //Converto le date da stringa a timestamp
            Timestamp orarioInizioDate = DateFormatter.formatDate(orarioInizio);
            Timestamp orarioFineDate = DateFormatter.formatDate(orarioFine);

            List<Prenotazione> prenotazioni = PrenotazioneDao.getPrenotazioni(orarioInizioDate, orarioFineDate);

            //TODO serve questo ciclo?
            for(int i = 0; i < prenotazioni.size(); ++i)
            {
                Prenotazione p = prenotazioni.get(i);

                //p.setUtente(null);
                //p.setPagata(null);
                //p.setCosto(null);
                //p.setEffettuata(null);
                //p.setIdPrenotazione(null);
                prenotazioni.set(i, p);
            }

            //Wrapper per far funzionare la xml converter
            WrapperArrayList<Prenotazione> prenotazioni1 = new WrapperArrayList<>(prenotazioni);

            return new Result(prenotazioni1, "Prenotazioni");
        }
        catch (NoResultException e)
        {
            return new Result("Errore, impossibile caricare i dati delle prenotazioni.");
        }
        catch (ParseException e)
        {
            return new Result("Errore, formato data non valido.");
        }
    }

    /**
     * Metodo che restitiuisce la lista delle prenotazioni relative ad un utente in una fascia oraria
     * @param idUtente id dell'utente
     * @param orarioInizio inizio fascia oraria
     * @param orarioFine fine fascia oraria
     * @return oggetto Result con lista delle prenotazioni
     */
    public static Result elencoPrenotazioniUtente(Integer idUtente, String orarioInizio, String orarioFine)
    {
        try
        {
            //Converto le date da stringa a timestamp
            Timestamp orarioInizioDate = DateFormatter.formatDate(orarioInizio);
            Timestamp orarioFineDate = DateFormatter.formatDate(orarioFine);

            List<Prenotazione> prenotazioni = PrenotazioneDao.getPrenotazioniUtente(idUtente, orarioInizioDate, orarioFineDate);

            //Rimuovo l'id dell'utente TODO serve davvero rimuoverlo?
            for(int i = 0; i < prenotazioni.size(); ++i)
            {
                Prenotazione p = prenotazioni.get(i);

                p.setUtente(null);
                //p.setPagata(null);
                //p.setCosto(null);
                //p.setEffettuata(null);
                //p.setIdPrenotazione(null);
                prenotazioni.set(i, p);
            }

            //Wrapper per far funzionare la xml converter
            WrapperArrayList<Prenotazione> prenotazioni1 = new WrapperArrayList<>(prenotazioni);
            return new Result(prenotazioni1, "Prenotazioni");
        }
        catch (NoResultException e)
        {
            return new Result("Errore, impossibile caricare i dati delle prenotazioni.");
        }
        catch (ParseException e)
        {
            return new Result("Errore, formato data non valido.");
        }
    }

    /**
     * Metodo privato che controlla se sono presenti prenotazioni per un dato ombrellone in una data fascia oraria
     * @param orarioInizio inizio fascia oraria
     * @param orarioFine fine fascia oraria
     * @param idOmbrellone id dell'ombrellone da controllare
     * @return flag true se non ci sono altre prenotazioni, false altrimenti
     */
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

    /**
     * Metodo che imposta una prenotazione come pagata
     * @param idPrenotazione id prenotazione da impostare come pagata
     * @return oggetto Result con esito dell'operazione
     */
    public static Result impostaComePagata(String idPrenotazione)
    {
        try
        {
            int idPrenotazioneInt = Integer.parseInt(idPrenotazione);

            Prenotazione prenotazioneDaAggiornare = PrenotazioneDao.getPrenotazione(idPrenotazioneInt);

            if(prenotazioneDaAggiornare.getPagata())
                return new Result("Errore, prenotazione già pagata", false);

            prenotazioneDaAggiornare.setPagata(true);

            PrenotazioneDao.aggiornaPrenotazione(prenotazioneDaAggiornare);

            return new Result("Prenotazione pagata con successo.");
        }
        catch (NoResultException e)
        {
            return new Result("Errore, codice prenotazione inesistente.");
        }

    }


}

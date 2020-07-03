package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.OmbrelloneDao;
import com.pac.pacbeach.dao.PrenotazioneDao;
import com.pac.pacbeach.dao.UtenteDao;
import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EntityNotDeletedException;
import com.pac.pacbeach.exceptions.EntityNotUpdatedException;
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
     * @param orarioInizio orario di inizio della prenotazione
     * @param orarioFine orario di fine della prenotazione
     * @param pagata indica se l'utente ha già pagato la prenotazione
     * @param ospiti lista degli ospiti dell'ombrellone
     * @param idUtente id dell'utente che ha effettuato la prenotazione
     * @param idOmbrellone id degli ombrelloni da prenotare (separati da ,)
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

            //Converto le date da stringa a timestamp
            Timestamp orarioInizioDate = DateFormatter.format(orarioInizio);
            Timestamp orarioFineDate = DateFormatter.format(orarioFine);

            //Calcolo del costo totale
            float costo = generaCosto(orarioInizioDate, orarioFineDate);

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

            //Se tutti gli ombrelloni selezionati sono prenotabile creo le singole prenotazioni
            if(tuttiLiberi)
            {
                Utente utente = UtenteDao.getUtente(idUtente);

                for(int id : idOmbrelloniInt)
                {
                    Ombrellone ombrellone = OmbrelloneDao.getOmbrellone(id);

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
     * Metodo per la creazione di una nuova prenotazione con dettagli utente non registrato
     * @param orarioInizio orario di inizio della prenotazione
     * @param orarioFine orario di fine della prenotazione
     * @param pagata indica se l'utente ha già pagato la prenotazione
     * @param ospiti lista degli ospiti dell'ombrellone
     * @param idOmbrellone id degli ombrelloni da prenotare (separati da ,)
     * @param nome nome dell'utente che sta prenotando
     * @param cognome cognome dell'utente che sta prenotando
     * @param telefono telefono dell'utente che sta prenotando
     * @param email email dell'utente che sta prenotando
     * @return oggetto Result con risultato dell'operazione
     */
    public static Result creaNuovaPrenotazione(String orarioInizio, String orarioFine, String pagata, String ospiti, String idOmbrellone, String nome, String cognome, String telefono, String email)
    {
        Result res;

        //Genero una password casuale che manderò poi per email all'utente
        String password = GestioneAccountControl.generaPasswordCasuale();

        res = GestioneAccountControl.creaNuovoUtente(email, password, nome, cognome, telefono);

        if(res.success())
        {
            Integer idUtente = (Integer) res.getContent();

            res = creaNuovaPrenotazione(orarioInizio, orarioFine, pagata, ospiti, idUtente, idOmbrellone);
        }
        else
        {
            res = GestioneAccountControl.getUtenteByEmail(email);

            if(res.success())
            {
                Utente u = (Utente) res.getContent();

                Integer idUtente = u.getIdUtente();

                res = creaNuovaPrenotazione(orarioInizio, orarioFine, pagata, ospiti, idUtente, idOmbrellone);
            }
        }

        return res;
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
            Timestamp orarioInizioDate = DateFormatter.format(orarioInizio);
            Timestamp orarioFineDate = DateFormatter.format(orarioFine);

            List<Prenotazione> prenotazioni = PrenotazioneDao.getPrenotazioni(orarioInizioDate, orarioFineDate);

            //Rimuovo i dettagli superflui dell'utente
            for(int i = 0; i < prenotazioni.size(); ++i)
            {
                Prenotazione p = prenotazioni.get(i);

                Utente u = p.getUtente();

                u.setPassword(null);
                u.setDataRegistrazione(null);
                u.setRuolo(null);

                p.setUtente(u);

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
     * Metodo che restituisce la lista delle prenotazioni relative ad un utente in una fascia oraria
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
            Timestamp orarioInizioDate = DateFormatter.format(orarioInizio);
            Timestamp orarioFineDate = DateFormatter.format(orarioFine);

            List<Prenotazione> prenotazioni = PrenotazioneDao.getPrenotazioniUtente(idUtente, orarioInizioDate, orarioFineDate);

            //Rimuovo i dettagli dell'utente
            for(int i = 0; i < prenotazioni.size(); ++i)
            {
                Prenotazione p = prenotazioni.get(i);
                Ombrellone o = p.getOmbrellone();
                o.setPrenotabile(null);
                p.setOmbrellone(o);
                p.setUtente(null);
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
     * Metodo che restituisce la lista delle prenotazioni relative ad un utente in una fascia oraria
     * @param idUtente id dell'utente
     * @return oggetto Result con lista delle prenotazioni
     */
    public static Result elencoPrenotazioniUtente(Integer idUtente)
    {
        try
        {

            List<Prenotazione> prenotazioni = PrenotazioneDao.getPrenotazioniUtente(idUtente);

            //Rimuovo i dettagli dell'utente
            for(int i = 0; i < prenotazioni.size(); ++i)
            {
                Prenotazione p = prenotazioni.get(i);
                Ombrellone o = p.getOmbrellone();
                o.setPrenotabile(null);
                p.setOmbrellone(o);
                p.setUtente(null);
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
            String[] idPrenotazioni =  idPrenotazione.split(",");
            int[] idPrenotazioniInt = new int[idPrenotazioni.length];

            for(int i=0; i<idPrenotazioniInt.length; ++i)
            {
                idPrenotazioniInt[i] = Integer.parseInt(idPrenotazioni[i]);
            }

            for(int id : idPrenotazioniInt)
            {
                Prenotazione prenotazioneDaAggiornare = PrenotazioneDao.getPrenotazione(id);

                if(prenotazioneDaAggiornare.getPagata())
                    return new Result("Errore, prenotazione già pagata", false);

                prenotazioneDaAggiornare.setPagata(true);

                PrenotazioneDao.aggiornaPrenotazione(prenotazioneDaAggiornare);
            }

            return new Result("Prenotazione pagata con successo.");
        }
        catch (NoResultException e)
        {
            return new Result("Errore, codice prenotazione inesistente.", false);
        }
        catch (EntityNotUpdatedException e)
        {
            return new Result("Errore aggiornamento prenotazione.");
        }

    }

    /**
     * Metodo per la cancellazione di una prenotazione
     * @param idPrenotazione id della prenotazione da cancellare
     * @param idUtente id dell'utente che sta tentando di cancellare la prenotazione
     * @param privilegiato flag se l'utente può comunque effettuare la cancellazione
     * @return oggetto Result con esito della cancellazione
     */
    public static Result eliminaPrenotazione(String idPrenotazione, Integer idUtente, Boolean privilegiato)
    {
        try
        {
            String[] idPrenotazioni =  idPrenotazione.split(",");
            int[] idPrenotazioniInt = new int[idPrenotazioni.length];

            for(int i=0; i<idPrenotazioniInt.length; ++i)
            {
                idPrenotazioniInt[i] = Integer.parseInt(idPrenotazioni[i]);
            }

            for (int id : idPrenotazioniInt)
            {
                Prenotazione p;
                try
                {
                    p = PrenotazioneDao.getPrenotazione(id);
                }
                catch (NoResultException e)
                {
                    System.out.println("Non trovata" + id);
                    continue;
                }

                if(p.getOrarioInizio().getTime() <= System.currentTimeMillis())
                {
                    return new Result("Non puoi annullare una prenotazione passata o in corso.", false);
                }

                if (p.getUtente().getIdUtente() == idUtente || privilegiato)
                {
                    PrenotazioneDao.eliminaPrenotazione(p);
                }
            }

            return new Result("Prenotazione eliminata con successo");

        }
        catch(NumberFormatException e)
        {
            return new Result("Formato id prenotazione errato", false);
        }
        catch(EntityNotDeletedException e)
        {
            return new Result("Errore durante l'eliminazione della prenotazione", false);
        }

    }

    /**
     * Metodo per il calcolo del costo totale
     * @param inizio inizio fascia oraria
     * @param fine fine fascia oraria
     * @return costo totale in float
     */
    private static float generaCosto(Timestamp inizio, Timestamp fine)
    {
        final float TARIFFA_ORARIA = 2.0F;
        final float TARIFFA_MEZZA_GIORNATA = 8.0F;
        final float TARIFFA_GIORNALIERA = 15.0F;

        long tempo = fine.getTime() - inizio.getTime();
        tempo = tempo/1000; //secondi
        tempo = tempo/60;   //minuti
        tempo = tempo/60;   //ore

        if(tempo == 6)
            return TARIFFA_MEZZA_GIORNATA;
        else if(tempo == 12)
            return TARIFFA_GIORNALIERA;
        else
            return tempo * TARIFFA_ORARIA;
    }
}

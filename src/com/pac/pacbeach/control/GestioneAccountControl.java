package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.UtenteDao;
import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EntityNotUpdatedException;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.PasswordStorage;
import com.pac.pacbeach.utils.Result;

import javax.persistence.NoResultException;

/**
 * Classe controller per la gestione degli account utente
 */
public class GestioneAccountControl
{
    /**
     * Metodo per la creazione di un nuovo utente
     * @param email email dell'utente
     * @param password password in chiaro dell'utente
     * @param nome  nome dell'utente
     * @param cognome cognome dell'utente
     * @param telefono n. telefono dell'utente
     * @return oggetto Result con id dell'utente appena creato
     */
    public static Result creaNuovoUtente(String email, String password, String nome, String cognome, String telefono)
    {
        Utente utente;
        try
        {
            //Faccio l'hash della password in chiaro
            String hashedPassword = PasswordStorage.createHash(password);

            //Definisco il ruolo (Hardcoded per questa implementazione)
            String ruolo = "utente";

            utente = new Utente(email, hashedPassword, nome, cognome, telefono, ruolo);

            //Creo il nuovo utente all'interno del Database
            utente = UtenteDao.creaUtente(utente);
        }
        catch (DuplicatedEntryException e)
        {
            return new Result("Esiste già un utente con questa mail.", false);
        }
        catch (PasswordStorage.CannotPerformOperationException e)
        {
            return new Result("Errore creazione utente." + e.getMessage(), false);
        }

        return new Result("Utente creato con successo.", true, utente.getIdUtente());
    }

    /**
     * Metodo per la gestione del login
     * @param email email dell'utente
     * @param password password in chiaro dell'utente
     * @return oggetto Result
     */
    public static Result login(String email, String password)
    {
        String errorMessage = "L'email o la password inseriti non sono corretti.";

        try
        {
            //Leggo dal database l'utente con l'email fornita
            Utente u = UtenteDao.getUtente(email);

            //Verifico che la password inserita combaci con quella nel database
            boolean isCorrect = PasswordStorage.verifyPassword(password, u.getPassword());

            //Se la password è corretta
            if(isCorrect)
            {
                u.setPassword(null);
                u.setNome(null);
                u.setCognome(null);
                u.setConfermaAccount(null);
                u.setDataRegistrazione(null);
                u.setTelefono(null);

                //Restituisco al front-end il ruolo, l'id e l'email dell'utente
                return new Result(u.getRuolo(), true, u);
            }
            else
            {
                return new Result(errorMessage, false);
            }
        }
        catch (NoResultException | PasswordStorage.InvalidHashException e)
        {
            //Se l'email inserita non corrisponde a nessun account.
            //Se la password inserita è errata
            return new Result(errorMessage, false);
        }
        catch (PasswordStorage.CannotPerformOperationException e)
        {
            return new Result("Errore in fase di autenticazione." + e.getMessage(), false);
        }
    }

    /**
     * Metodo per ottenere le informazioni di un utente fornendo l'email
     * @param email email dell'utente
     * @return oggetto Result con le informazioni dell'utente
     */
    public static Result getUtenteByEmail(String email)
    {
        try
        {
            //Leggo i dati dell'utente dal database
            Utente u = UtenteDao.getUtente(email);

            //Rimuovo il campo password
            u.setPassword(null);

            return new Result("Dati utente",true, u);
        }
        catch (NoResultException e)
        {
            return new Result("Nessun utente con questa email.", false);
        }
    }

    /**
     * Metodo per aggiornare i dati di un utente all'interno del database
     * @param u dati dell'utente
     * @return oggetto Result con messaggio di conferma
     */
    public static Result aggiornaUtente(Utente u)
    {
        try
        {
            UtenteDao.aggiornaUtente(u);

        }
        catch (EntityNotUpdatedException e)
        {
            return new Result("Errore aggiornamento dati utente.", false);
        }

        return new Result("Utente aggiornato con successo.");
    }

    protected static String generaPasswordCasuale()
    {
        //Non implementata in questa versione
        return "12345678";
    }
}

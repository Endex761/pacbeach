package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.UtenteDao;
import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EmailNotFoundException;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.PasswordStorage;
import com.pac.pacbeach.utils.Result;

import javax.imageio.spi.ServiceRegistry;
import javax.persistence.NoResultException;

public class GestioneAccountControl
{
    public static Result creaNuovoUtente(String email, String password, String nome, String cognome, String telefono)
    {
        try
        {
            String hashedPassword = PasswordStorage.createHash(password);
            Utente utente = new Utente(email, hashedPassword, nome, cognome, telefono);
            UtenteDao.creaUtente(utente);
        }
        catch (DuplicatedEntryException e)
        {
            return new Result("Esiste già un utente con questa mail.", false);
        }
        catch (PasswordStorage.CannotPerformOperationException e)
        {
            return new Result("Errore creazione utente." + e.getMessage(), false);
        }

        return new Result("Utente creato con successo.");
    }

    public static Result login(String email, String password)
    {
        String errorMessage = "L'email o la password inseriti non sono corretti.";
        try
        {
            Utente u = UtenteDao.getUtente(email);

            Boolean isCorrenct = PasswordStorage.verifyPassword(password, u.getPassword());

            if(isCorrenct)
            {
                u.setPassword(null);
                u.setNome(null);
                u.setCognome(null);
                u.setConfermaAccount(null);
                u.setDataRegistrazione(null);
                u.setTelefono(null);

                return new Result(u.getRuolo(), true, u);
            }
            else
            {
                return new Result(errorMessage, false);
            }
        }
        catch (NoResultException e)
        {
            //Se l'email inserita non corrisponde a nessun account.
            return new Result(errorMessage, false);
        }
        catch (PasswordStorage.InvalidHashException | PasswordStorage.CannotPerformOperationException e )
        {
            return new Result("Errore in fase di autenticazione." + e.getMessage(), false);
        }
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

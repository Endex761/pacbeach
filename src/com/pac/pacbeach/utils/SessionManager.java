package com.pac.pacbeach.utils;

import com.pac.pacbeach.exceptions.UserNotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Classe per la gestione della sessione http
 */
public class SessionManager
{
    private final HttpSession session;

    public SessionManager(HttpServletRequest request)
    {
        session = request.getSession();
    }

    /**
     * @return true se l'utente è loggato
     * @throws UserNotLoggedInException se l'utente non è loggato
     */
    public boolean isLoggedIn() throws UserNotLoggedInException
    {
        if(session.getAttribute("idUtente") != null)
            return true;
        else
            throw new UserNotLoggedInException();
    }

    /**
     * @return id dell'utente loggato
     * @throws UserNotLoggedInException se nessun utente è loggato
     */
    public Integer getLoggedUserId() throws UserNotLoggedInException
    {
        if(isLoggedIn())
        {
            return (Integer) session.getAttribute("idUtente");
        }

        return null;
    }

    /**
     * @return email dell'utente loggato
     * @throws UserNotLoggedInException se nessun utente è loggato
     */
    public String getLoggedUserEmail() throws UserNotLoggedInException
    {
        if(isLoggedIn())
        {
            return (String) session.getAttribute("email");
        }

        return null;
    }

    /**
     * @return la stringa del ruolo dell'utente loggato (bagnino, utente, barista)
     * @throws UserNotLoggedInException se nessun utente è loggato
     */
    public String getLoggedUserRole() throws  UserNotLoggedInException
    {
        if(isLoggedIn())
        {
            return (String) session.getAttribute("ruolo");
        }

        return null;
    }

    public void setAttribute(String name, Object attribute)
    {
        session.setAttribute(name, attribute);
    }

    public Object getAttribute(String name) { return session.getAttribute(name); }

    public void logout()
    {
        session.invalidate();
    }
}

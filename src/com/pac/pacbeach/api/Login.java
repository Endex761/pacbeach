package com.pac.pacbeach.api;

import com.pac.pacbeach.control.GestioneAccountControl;
import com.pac.pacbeach.exceptions.UserNotLoggedInException;
import com.pac.pacbeach.exceptions.ValidationException;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.RequestValidator;
import com.pac.pacbeach.utils.Result;
import com.pac.pacbeach.utils.SessionManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  Servlet per la gestione del Login
 */

@WebServlet("/api/login")
public class Login extends HttpServlet {
    /**
     * Gestisce la richiesta post per l'effettivo login
     * Parametri della richiesta:
     *  - email
     *  - password
     * Risposta
     *  Oggetto Responde in formato xml
     *   - success = true
     *      > message = ruolo dell'utente loggato
     *   - success = false
     *      > message = messaggio d'errore
     *
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");

        RequestValidator r = new RequestValidator(request);

        Result res;

        try
        {
            String email = r.getParameter("email");
            String password = r.getParameter("password");

            res = GestioneAccountControl.login(email, password);

            if(res.success())
            {
                //Se il login è stato effettuato con successo
                Utente u = (Utente) res.getContent();

                //Prendo la sessione corrente
                SessionManager session = new SessionManager(request);

                //E inizializzo i parametri di sessione
                session.setAttribute("email", u.getEmail());
                session.setAttribute("idUtente", u.getIdUtente());
                session.setAttribute("ruolo", u.getRuolo());

                //Rimuovo dalla risposta i dettagli dell'utente prima di mandarla
                res.setContent(null);
            }
        }
        catch (ValidationException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");

        SessionManager sessionManager = new SessionManager(request);

        Result res;

        try
        {
            String ruolo = sessionManager.getLoggedUserRole();

            res = new Result(ruolo);
        }
        catch (UserNotLoggedInException e)
        {
            res = new Result("Utente non loggato", false);
        }

        response.getWriter().write(res.toXmlString());
    }
}

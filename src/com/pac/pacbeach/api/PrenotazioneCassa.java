package com.pac.pacbeach.api;

import com.pac.pacbeach.control.GestionePrenotazioneControl;
import com.pac.pacbeach.exceptions.UserNotLoggedInException;
import com.pac.pacbeach.exceptions.ValidationException;
import com.pac.pacbeach.utils.RequestValidator;
import com.pac.pacbeach.utils.Result;
import com.pac.pacbeach.utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/cassiere/prenotazione")
public class PrenotazioneCassa extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");

        RequestValidator r = new RequestValidator(request);

        SessionManager sessionManager = new SessionManager(request);

        Result res;

        try
        {
            if(sessionManager.getLoggedUserRole().equals("utente"))
            {
                res = new Result("Utente non autorizzato", false);
            }
            else
            {
                String nome = r.getParameter("nome", 45);
                String cognome = r.getParameter("cognome", 45);
                String telefono = r.getParameter("telefono",20);
                String email = r.getParameter("email", 100);

                String orarioInizio = r.getParameter("orarioInizio");
                String orarioFine = r.getParameter("orarioFine");
                String pagata = r.getParameter("pagata");
                String idOmbrellone = r.getParameter("idOmbrellone");
                String ospiti = r.getParameter("ospiti", false , 0, 500);

                res = GestionePrenotazioneControl.creaNuovaPrenotazione(orarioInizio, orarioFine, pagata, ospiti, idOmbrellone, nome, cognome, telefono, email);
            }

        }
        catch (ValidationException | UserNotLoggedInException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");

        RequestValidator r = new RequestValidator(request);
        SessionManager sessionManager = new SessionManager(request);

        Result res;
        try
        {
            String role = sessionManager.getLoggedUserRole();
            if(!role.equals("utente"))
            {
                String idPrenotazione = r.getParameter("idPrenotazione");

                res = GestionePrenotazioneControl.impostaComePagata(idPrenotazione);
            }
            else
            {
                res = new Result("Utente non autorizzato", false);
            }

        }
        catch (ValidationException | UserNotLoggedInException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }
}

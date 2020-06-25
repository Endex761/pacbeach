package com.pac.pacbeach.api;

import com.pac.pacbeach.control.GestioneOrdiniControl;
import com.pac.pacbeach.control.GestionePrenotazioneControl;
import com.pac.pacbeach.dao.PrenotazioneDao;
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

@WebServlet("/api/prenotazione")
public class Prenotazione extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/xml");

        RequestValidator r = new RequestValidator(request);

        SessionManager sessionManager = new SessionManager(request);

        Result res;

        try
        {
            String orarioInizio = r.getParameter("orarioInizio");
            String orarioFine = r.getParameter("orarioFine");
            String pagata = r.getParameter("pagata");
            String idOmbrellone = r.getParameter("idOmbrellone");
            String ospiti = r.getParameter("ospiti", false );

            Integer idUtente = sessionManager.getLoggedUserId();

            res = GestionePrenotazioneControl.creaNuovaPrenotazione(orarioInizio, orarioFine, pagata, ospiti, idUtente, idOmbrellone);
        }
        catch (ValidationException | UserNotLoggedInException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/xml");
        SessionManager sessionManager = new SessionManager(request);
        RequestValidator r = new RequestValidator(request);
        Result res;

        try
        {
            String orarioInizio = r.getParameter("orarioInizio", false);
            String orarioFine = r.getParameter("orarioFine", false);


            if(sessionManager.getLoggedUserRole().equals("utente"))
            {
                Integer idUtente = sessionManager.getLoggedUserId();

                if(orarioInizio == null && orarioFine == null)
                    res = GestionePrenotazioneControl.elencoPrenotazioniUtente(idUtente);
                else
                    res = GestionePrenotazioneControl.elencoPrenotazioniUtente(idUtente, orarioInizio, orarioFine);
            }
            else
            {
                res = GestionePrenotazioneControl.elencoPrenotazioni(orarioInizio, orarioFine);
            }

        }
        catch (ValidationException | UserNotLoggedInException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }



    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/xml");

        RequestValidator r = new RequestValidator(request);

        SessionManager sessionManager = new SessionManager(request);

        Result res;

        try
        {
            String idPrenotazioni = r.getParameter("idPrenotazione");
            int idUtente = sessionManager.getLoggedUserId();

            boolean privilegiato = false;

            if(sessionManager.getLoggedUserRole().equals("cassiere"))
                privilegiato = true;

            res = GestionePrenotazioneControl.eliminaPrenotazione(idPrenotazioni, idUtente, privilegiato);
        }
        catch (ValidationException | UserNotLoggedInException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());

    }
}

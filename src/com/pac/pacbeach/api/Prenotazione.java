package com.pac.pacbeach.api;

import com.pac.pacbeach.control.GestionePrenotazioneControl;
import com.pac.pacbeach.exceptions.ValidationException;
import com.pac.pacbeach.utils.RequestValidator;
import com.pac.pacbeach.utils.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/prenotazione")
public class Prenotazione extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");

        RequestValidator r = new RequestValidator(request);
        Result res = null;

        try
        {
            String orarioInizio = r.getParameter("orarioInizio");
            String orarioFine = r.getParameter("orarioFine");
            String pagata = r.getParameter("pagata");
            String idUtente = "1"; //=getUtenteLoggato();
            String idOmbrellone = r.getParameter("idOmbrellone");

            res = GestionePrenotazioneControl.creaNuovaPrenotazione(orarioInizio, orarioFine, pagata, idUtente, idOmbrellone);
        }
        catch (ValidationException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/xml");

        RequestValidator r = new RequestValidator(request);
        Result res = null;

        try
        {
            String orarioInizio = r.getParameter("orarioInizio");
            String orarioFine = r.getParameter("orarioFine");

            res = GestionePrenotazioneControl.elencoPrenotazioni(orarioInizio, orarioFine);
        }
        catch (ValidationException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }
}

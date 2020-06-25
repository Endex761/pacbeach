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

@WebServlet("/api/pagamento")
public class Pagamento extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/xml");

        RequestValidator r = new RequestValidator(request);

        SessionManager sessionManager = new SessionManager(request);

        Result res;

        try
        {
            String idPrenotazioni = r.getParameter("idPrenotazione");

            sessionManager.isLoggedIn();

            res = GestionePrenotazioneControl.impostaComePagata(idPrenotazioni);
        }
        catch (ValidationException | UserNotLoggedInException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

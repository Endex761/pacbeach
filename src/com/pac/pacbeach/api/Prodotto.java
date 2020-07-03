package com.pac.pacbeach.api;

import com.pac.pacbeach.control.GestioneProdottiControl;
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

@WebServlet("/api/prodotto")
public class Prodotto extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");

        SessionManager sessionManager = new SessionManager(request);

        RequestValidator r = new RequestValidator(request);

        Result res = null;
        try
        {
            String terminati = r.getParameter("terminati", false);

            if(sessionManager.isLoggedIn())
            {
                res = GestioneProdottiControl.listaProdotti(terminati);
            }
            
        }
        catch (ValidationException | UserNotLoggedInException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }
}

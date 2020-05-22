package com.pac.pacbeach.api;

import com.pac.pacbeach.control.GestioneAccountControl;
import com.pac.pacbeach.exceptions.ValidationException;
import com.pac.pacbeach.utils.RequestValidator;
import com.pac.pacbeach.utils.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/registrazione")
public class Registrazione extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");
        RequestValidator r = new RequestValidator(request);

        Result res = null;

        try
        {
            String email = r.getParameter("email");
            String nome = r.getParameter("nome");
            String cognome = r.getParameter("cognome");
            String telefono = r.getParameter("telefono");

            res = GestioneAccountControl.creaNuovoUtente(email, nome, cognome, telefono);
        }
        catch (ValidationException e)
        {
            res = new Result(e.getMessage(), false);
        }


        response.getWriter().write(res.toXmlString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

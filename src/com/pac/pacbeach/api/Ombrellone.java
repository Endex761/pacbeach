package com.pac.pacbeach.api;

import com.pac.pacbeach.control.GestioneOmbrelloniControl;
import com.pac.pacbeach.exceptions.ValidationException;
import com.pac.pacbeach.utils.RequestValidator;
import com.pac.pacbeach.utils.Result;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/ombrellone")
public class Ombrellone extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");

        RequestValidator rv = new RequestValidator(request);

        Result res;

        try
        {
            String orarioInizio = rv.getParameter("orarioInizio");
            String orarioFine = rv.getParameter("orarioFine");

            res = GestioneOmbrelloniControl.statoOmbrelloni(orarioInizio, orarioFine);
        }
        catch (ValidationException e)
        {
            res = new Result(e.getMessage(), false);
        }

        response.getWriter().write(res.toXmlString());
    }
}

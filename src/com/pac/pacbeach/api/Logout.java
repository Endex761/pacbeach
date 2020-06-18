package com.pac.pacbeach.api;

import com.pac.pacbeach.utils.Result;
import com.pac.pacbeach.utils.SessionManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/logout")
public class Logout extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");

        SessionManager sessionManager = new SessionManager(request);
        sessionManager.logout();

        Result res = new Result("Logout effettuato con successo");

        response.getWriter().write(res.toXmlString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}

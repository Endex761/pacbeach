package com.pac.pacbeach.pages;

import com.pac.pacbeach.exceptions.UserNotLoggedInException;
import com.pac.pacbeach.utils.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bar")
public class Bar extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionManager sessionManager = new SessionManager(request);

        try
        {
            String ruolo = sessionManager.getLoggedUserRole();
            if(ruolo.equals("utente"))
                request.getRequestDispatcher("./bar.html").forward(request, response);
            else
                response.sendRedirect("/");
        }
        catch (UserNotLoggedInException e)
        {
            response.sendRedirect("/");
        }

    }
}

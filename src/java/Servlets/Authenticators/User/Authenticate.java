/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Authenticators.User;

import Objects.User.User;
import Objects.Authenticators.User.AuthSafe;
import Objects.Authenticators.User.UserSafe;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Moses
 */
public class Authenticate extends HttpServlet {
private final String URL_AUTHENTICATE="authenticate.jsp";
private final String URL_LOGIN="Login.jsp";
private final String URL_REGISTER="register.jsp";
private final String URL_INDEX="index.jsp";
private final String URL_COMPLETE_SPECIFICATION="completeSpecification.jsp";
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection=new SQLDriverMacaronBa().getConnection())
        {
            doIt(request, response, connection);
        } catch (Exception ex)
        {
            
        } 
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void doIt(HttpServletRequest request, HttpServletResponse response,Connection connection) throws UnsupportedEncodingException
    {
        request.setCharacterEncoding("UTF-8");
        
        try {
            UserSafe usersafe=new UserSafe(request,connection);
            if(!usersafe.getLogged())
            {
                response.sendRedirect(URL_LOGIN+"?att=loginFirst");
                return;
            }
            User user=usersafe.getUser();
            AuthSafe authsafe=new AuthSafe(user.getUserId(),connection);
            
            if(!authsafe.isValid())
            {
                response.sendRedirect(URL_AUTHENTICATE+"?att=notvalid");
                return;
            }
            
            if(!authsafe.isInTime())
            {
                response.sendRedirect(URL_AUTHENTICATE+"?att=notintime");
                return;
            }
            
            String authCode=request.getParameter("authCode");
            
            if(Methods.Methods.isNullOrEmpty(authCode))
            {
                response.sendRedirect(URL_AUTHENTICATE+"att=wrong");
                authsafe.addAuthAttempt();
                return;
            }
            
            
            if(authsafe.verifyAuthCode(authCode))
            {
                response.sendRedirect(URL_COMPLETE_SPECIFICATION);
                return;
            }else
            {
                response.sendRedirect(URL_AUTHENTICATE+"?att=wrong");
                //no need to add attempt here,it will add automatilcaly in verifyAuthCode function
                return;
            }
            
            
        } catch (Exception ex) {
            
        }
    }
}

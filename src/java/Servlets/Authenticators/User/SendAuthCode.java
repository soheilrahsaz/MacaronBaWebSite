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
public class SendAuthCode extends HttpServlet {
private final String URL_AUTHENTICATE="authenticate.jsp";
private final String URL_LOGIN="Login.jsp";   
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
        try(Connection connection=new SQLDriverMacaronBa().getConnection())
        {
            
            doIt(request, response, connection);
            connection.close();
        } catch (Exception e) {
        }
        
        
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
        try(Connection connection=new SQLDriverMacaronBa().getConnection())
        {
            
            doIt(request, response, connection);
        } catch (Exception e) {
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

    private void doIt(HttpServletRequest request, HttpServletResponse response,Connection connection)
            throws ServletException, IOException
    {
        try {
            UserSafe usersafe=new UserSafe(request,connection);
            if(!usersafe.getLogged())
            {
                response.sendRedirect(URL_LOGIN+"?att=loginfirst");
                return;
            }
            User user=usersafe.getUser();
            AuthSafe authsafe=new AuthSafe(user.getUserId(),connection);
            
            if(authsafe.isAuthRequestBlock())
            {
                response.sendRedirect(URL_AUTHENTICATE+"?att=blockrequest");
                return;
            }
            
            authsafe.generateCode();
            response.sendRedirect(URL_AUTHENTICATE+"?att=sent");
            return;
            
        } catch (Exception ex) {
            response.getWriter().println(ex.getMessage());
        } 
    }
}

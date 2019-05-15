/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Authenticators.User;

import ExceptionsChi.DoesntExistException;
import Objects.Authenticators.User.AuthSafe;
import Objects.Authenticators.User.RegisterSafe;
import Objects.Authenticators.User.UserSafe;
import SQL.Commands.AuthenticatorSQLCommands;
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
import javax.servlet.http.HttpSession;

/**
 * used after sending primitive datas for registrations
 * @author Moses
 */
public class RegisterCheck extends HttpServlet {
private final String URL_AUTHENTICATE="authenticate.jsp";
private final String URL_LOGIN="Login.jsp";
private final String URL_REGISTER="register.jsp";
private final String URL_INDEX="index.jsp";
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
    {
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
             request.setCharacterEncoding("UTF-8");

        try 
        {
            AuthenticatorSQLCommands authSQL=new AuthenticatorSQLCommands(connection);
//            RegisterSafe reg=new RegisterSafe(request,connection);
            //if user has registered too much, he can't register again for at least a day
//            if(reg.isRegisterBlocked())
//            {
//                response.sendRedirect("register.html?att=bl");
//                return;
//            }
            //if user is already logged in !
            UserSafe userSafe=new UserSafe(request, connection);
            if(userSafe.getLogged())
            {
                response.sendRedirect(URL_INDEX);
                return;
            }
            String phoneNumber=request.getParameter("phoneNumber");
            String password=request.getParameter("password");
            
            if(Methods.Methods.isNullOrEmpty(phoneNumber,password))
            {
                response.sendRedirect(URL_REGISTER+"?att=wrong");
//                reg.addRegisterAttempt();
                return;
            }
            
            if(authSQL.usernameExists(phoneNumber))
            {
                response.sendRedirect(URL_REGISTER+"?att=exists");
//                reg.addRegisterAttempt();
                return;
            }
            
            int userId=authSQL.regiserUser(phoneNumber, password);
//            reg.addRegisterAttempt();
            
            String MSS_Token=Methods.Encryptors.Encrypt1(userId);
            HttpSession session=request.getSession(true);
            session.setAttribute("MSS_Token", MSS_Token);
            
            AuthSafe auth=new AuthSafe(userId, connection);
            auth.generateCode();
            response.sendRedirect(URL_AUTHENTICATE);
            return;
            
            
            
        } catch (Exception ex) 
        {
           response.getWriter().println(ex.getMessage());
        } 
        
        
    }
}

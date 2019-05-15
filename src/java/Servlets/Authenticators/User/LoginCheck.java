/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Authenticators.User;

import Objects.Authenticators.User.LoginSafe;
import ExceptionsChi.DoesntExistException;
import Objects.User.Determiner;
import Objects.User.User;
import Objects.Authenticators.User.UserSafe;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
 * used only after client's logging in
 * @author Moses
 */
public class LoginCheck extends HttpServlet {
    private final String URL_LOGIN_PAGE="Login.jsp";
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
    
    private void doIt(HttpServletRequest request, HttpServletResponse response,Connection connection) throws UnsupportedEncodingException, IOException, ClassNotFoundException, SQLException
    {
        request.setCharacterEncoding("UTF-8");
        
        //if user is already logged in
        UserSafe userSafe=new UserSafe(request, connection);
        if(userSafe.getLogged())
        {
            response.sendRedirect(URL_INDEX);
            return;
        }
        
        LoginSafe log=null;
        try {
            log=new LoginSafe(request,connection);
            //if client is blocked he can't login for 3 minutes
            if(log.isLoginBlock())
            {
                response.sendRedirect(URL_LOGIN_PAGE+"?att=bl");
                return;
            }
            
            //client's not blocked
            String username=request.getParameter("userName");
            String password=request.getParameter("password");
            
            if(Methods.Methods.isNullOrEmpty(username,password))
            {
                response.sendRedirect(URL_LOGIN_PAGE+"?att=wr");
                log.addLoginAttempt();
                return;
            }
            
            User user= log.login(username, password);
            log.resetLoginAttempt();//reset login attempt if user puts right password.(for easier access next time)
//            log.blockLogin();//hehehe:D to stop hacker from gussing passwords and just putting right password every 2 times :D
            
            //if user is blocked!
            if(user.getStatus()==-1)
            {
                response.sendRedirect(URL_LOGIN_PAGE+"?att=bn");
                return;
            }
            
            String MSS_Token=Methods.Encryptors.Encrypt1(user.getUserId());
            HttpSession session=request.getSession();
            session.setAttribute("MSS_Token", MSS_Token);
            String path=new Determiner(user).pathDeterminer();
            
            response.sendRedirect(path);
            return;
            
        } catch (DoesntExistException ex) 
        {
            if(ex.getMessage().startsWith("user"))
            {
                try {
                    log.addLoginAttempt();
                } catch (SQLException ex1) {}
                
                response.sendRedirect(URL_LOGIN_PAGE+"?att=wr");
                return;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(LoginCheck.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}

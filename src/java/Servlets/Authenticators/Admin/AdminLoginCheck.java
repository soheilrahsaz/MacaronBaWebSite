/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Authenticators.Admin;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminLoginSafe;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.IOException;
import java.io.PrintWriter;
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
 *
 * @author Moses
 */
public class AdminLoginCheck extends HttpServlet {
private final String URL_ADMIN_LOGIN="AdminPanel/Login.jsp";
private final String URL_ADMIN_PANEL_INDEX="AdminPanel/index.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminLoginCheck</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminLoginCheck at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

 
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
        processRequest(request, response);
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
            response.getWriter().println(e.getMessage());
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
            throws ServletException, IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException
    {
        request.setCharacterEncoding("UTF-8");
        AdminLoginSafe adminLogin=null;
        
        try {
            adminLogin=new AdminLoginSafe(request, connection);
            
            if(adminLogin.isLoginBlock())
            {
                response.sendRedirect(URL_ADMIN_LOGIN+"?att=block");
                return;
            }
            
            String userName=request.getParameter("userName");
            String password=request.getParameter("password");
            
            if(Methods.Methods.isNullOrEmpty(userName,password))
            {
                adminLogin.addLoginAttempt();
                response.sendRedirect(URL_ADMIN_LOGIN+"?att=wrong");
                return;
            }
            
            Admin admin=adminLogin.login(userName, password);
            adminLogin.resetLoginAttempt();
//            adminLogin.blockLogin();
            
            if(admin.getStatus()==-1)
            {
                response.sendRedirect(URL_ADMIN_LOGIN+"?att=ban");
                return;
            }
            
            
            int adminId=admin.getAdminId();
            String MSSA_Token=Methods.Encryptors.Encrypt1(adminId);
            HttpSession session=request.getSession();
            session.setAttribute("MSSA_Token", MSSA_Token);
            
            response.sendRedirect(URL_ADMIN_PANEL_INDEX);
            return;
            
        } catch (DoesntExistException ex)
        {
            if(ex.getMessage().startsWith("Admin"))
            {
                adminLogin.addLoginAttempt();
                response.sendRedirect(URL_ADMIN_LOGIN+"?att=wrong");
            }
        }
        
    }
}

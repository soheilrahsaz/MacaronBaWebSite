/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.User;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.SiteSetting.SmsConfig;
import Objects.User.User;
import SQL.Commands.AuthenticatorSQLCommands;
import SQL.Commands.SiteSettingSQLCommands;
import SQL.Commands.UserSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import SmsPishgam.Methods.SmsSender;
import SmsPishgam.Objects.Sms;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Moses
 */
public class AddUser extends HttpServlet {
private final String URL_INDEX="../index.jsp";
private final String URL_LOGIN="../Login.jsp";
private final String URL_ADD_USER="AddUser.jsp";
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
            out.println("<title>Servlet AddUser</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddUser at " + request.getContextPath() + "</h1>");
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
        try (Connection connection=new SQLDriverMacaronBa().getConnection())
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
            throws ServletException, IOException, SQLException, ClassNotFoundException, DoesntExistException, NoSuchAlgorithmException
    {
        request.setCharacterEncoding("UTF-8");
        AdminSafe adminsafe=new AdminSafe(request, connection);
        
        if(!adminsafe.getLogged())
        {
            response.sendRedirect(URL_LOGIN+"?att=loginfirst");
            return;
        }
        
        Admin admin=adminsafe.getAdmin();
        if(!admin.haveAccessTo("addUser"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        String phoneNumber=request.getParameter("phoneNumber");
        String password=request.getParameter("password");
        
        if(Methods.Methods.isNullOrEmpty(phoneNumber,password))
        {
            response.sendRedirect(URL_ADD_USER+"?att=empty");
            return;
        }
        phoneNumber=Methods.Methods.normalizePhone(phoneNumber);
        
        AuthenticatorSQLCommands authSQL=new AuthenticatorSQLCommands(connection);
        if(authSQL.usernameExists(phoneNumber))
        {
            response.sendRedirect(URL_ADD_USER+"?att=exists");
            return;
        }
        
        String hashed=Methods.Encryptors.MD5Hasher(password);
        UserSQLCommands userSQL=new UserSQLCommands(connection);
        int userId=userSQL.addUser(phoneNumber, hashed,1);
        
        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        SmsConfig smsConfig=siteSQL.getSmsConfig();
        if(smsConfig.isConfig("addUser"))
        {
            String text="سلام ."
                    + "شما در قصر فردوس ثبت نام شدید."
                    + "با وارد کردن اطلاعات ورود خود در سایت "
                    + "macaronba.ir"
                    + "وارد شده و مشخصات خود را تکمیل کنید و از خدمات سایت ما استفاده کنید.\n"
                    + "رمز:"
                    + password
                    +"\n"
                    + "قصر فردوس.";
            
            Sms sms=new Sms().setReciever(phoneNumber).setText(text).setUserId(userId);
            SmsSender sender=new SmsSender(connection);
            sender.sendSms(sms);
        }
        
        response.sendRedirect(URL_ADD_USER+"?att=ok");
        return;
        
    }
}

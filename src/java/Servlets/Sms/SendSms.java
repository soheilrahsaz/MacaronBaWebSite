/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Sms;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.User.User;
import SQL.Commands.UserSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import SmsPishgam.Methods.SmsSender;
import SmsPishgam.Objects.Sms;
import java.io.IOException;
import java.io.PrintWriter;
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
public class SendSms extends HttpServlet {
private final String URL_LOGIN="../Login.jsp";
private final String URL_INDEX="../index.jsp";
private final String URL_SMSes="Smses.jsp";
private final String URL_SEND_SMS="SendSms/";
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
            out.println("<title>Servlet SendSms</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SendSms at " + request.getContextPath() + "</h1>");
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
            throws ServletException, IOException, SQLException, ClassNotFoundException, DoesntExistException
    {
        request.setCharacterEncoding("UTF-8");
        AdminSafe adminsafe=new AdminSafe(request, connection);
        
        if(!adminsafe.getLogged())
        {
            response.sendRedirect(URL_LOGIN+"?att=loginfirst");
            return;
        }
        
        Admin admin=adminsafe.getAdmin();
        if(!admin.haveAccessTo("sendSms"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        String userid=request.getParameter("userId");
        String text=request.getParameter("text");
        int userId=Integer.parseInt(userid);
        if(Methods.Methods.isNullOrEmpty(text))
        {
            response.sendRedirect(URL_SEND_SMS+userid+"?att=empty");
            return;
        }
        if(!Methods.Methods.isEnoughLong(text, 180))
        {
            response.sendRedirect(URL_SEND_SMS+userid+"?att=toolong");
            return;
        }
        
        UserSQLCommands userSQL=new UserSQLCommands(connection);
        User user=userSQL.getUserFull(userId);
        
        Sms sms=new Sms();
        sms.setReciever(user.getPhoneNumber()).setText(text).setUserId(userId);
        SmsSender sender=new SmsSender(connection);
        sender.sendSms(sms);
        
        response.sendRedirect(URL_SMSes+"?att=ok");
        return;
        
    }
}

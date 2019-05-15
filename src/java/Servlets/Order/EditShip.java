/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Order;

import ExceptionsChi.DoesntExistException;
import Methods.EmailerMoses;
import Methods.ParamsEmail;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.SiteSetting.SmsConfig;
import Objects.User.User;
import SQL.Commands.OrderSQLCommands;
import SQL.Commands.SiteSettingSQLCommands;
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
public class EditShip extends HttpServlet {
    private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_LOGIN="/AdminPanel/Login.jsp";
private final String URL_ORDER="/AdminPanel/Orders/List/";

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
            out.println("<title>Servlet EditShip</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditShip at " + request.getContextPath() + "</h1>");
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
        if(!admin.haveAccessTo("editOrder"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        
        
        String order=request.getParameter("orderId");
        String ship=request.getParameter("shipId");
        
        int orderId=Integer.parseInt(order);
        int shipId=Integer.parseInt(ship);
        
        String status=request.getParameter("shipStatus");
        String shipType=request.getParameter("shipTypeId");
        
        int statusId=Integer.parseInt(status);
        int shipTypeId=Integer.parseInt(shipType);
        
        OrderSQLCommands orderSQL=new OrderSQLCommands(connection);
        orderSQL.editShip(shipId, shipTypeId, statusId);
        
        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        SmsConfig smsConfig=siteSQL.getSmsConfig();
        User user=orderSQL.getUserDetailSms(orderId);
        if(smsConfig.isConfig("sendOrder") && statusId==1)
        {
            
            Sms sms=new Sms().setReciever(user.getPhoneNumber()).setText(getSMSText(orderId)).setUserId(user.getUserId());
            SmsSender sender=new SmsSender(connection);
            sender.sendSms(sms);
        }
        
        try {
            if(statusId==1)
            {
                EmailerMoses emailer=new EmailerMoses(ParamsEmail.USER_NAME, ParamsEmail.PASSWORD,25 , 110, 43, ParamsEmail.HOST);
                emailer.sendEmail(user.getEmail(), getSMSText(statusId), "تغییر وضعیت سفارش");
            }
        } catch (Exception ex) {
            
        }
        
        response.sendRedirect(URL_ORDER+order+"?att=ok");
        return;
    }
    
    private String getSMSText(int orderId)
    {
        String text="سفارش با کد "
                + orderId
                + " ارسال شد."
                + "ماکارون با";
        return text;
    }
}

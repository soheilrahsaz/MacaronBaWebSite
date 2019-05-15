/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Partners;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.Partner.Partner;
import Objects.SiteSetting.SmsConfig;
import Objects.User.User;
import SQL.Commands.OrderSQLCommands;
import SQL.Commands.PartnerSQLCommands;
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
public class EditPartnerOrder extends HttpServlet {
private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_LOGIN="/AdminPanel/Login.jsp";
private final String URL_ORDER="/AdminPanel/Partners/Orders/";
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
            out.println("<title>Servlet EditPartnerOrder</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditPartnerOrder at " + request.getContextPath() + "</h1>");
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
        
        int orderId=Integer.parseInt(order);
        
        String status=request.getParameter("status");
        int statusId=Integer.parseInt(status);
        PartnerSQLCommands partnerSQL=new PartnerSQLCommands(connection);
        OrderSQLCommands orderSQL=new OrderSQLCommands(connection);
        partnerSQL.editReceipt(orderId, statusId);
        
        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        SmsConfig smsConfig=siteSQL.getSmsConfig();
        if(smsConfig.isConfig("statusOrder"))
        {
            Partner partner=partnerSQL.getUserDetailSms(orderId);
            Sms sms=new Sms().setReciever(partner.getPhoneNumber()).setText(getSMSText(orderId)).setUserId(-3);
            SmsSender sender=new SmsSender(connection);
            sender.sendSms(sms);
        }
            
        
        
        response.sendRedirect(URL_ORDER+order+"?att=ok");
        return;
    }
    
    private String getSMSText(int status)
    {
        switch(status)
        {
            case 1:return"سفارش شما در صف انتظار قرار گرفت.قصر فردوس";
            case 2:return "سفارش شما تایید شد و در حال پیگیری می باشد.قصر فردوس";
            case 3:return "سفارش شما با موفقیت تکمیل شد. قصر فردوس";
            case 4:return "سفارش شما رد شد، لطفا از طریق پنل خود پیگیری بفرمایید. قصر فرودس";
            default: return "";
        }
    }
    
}

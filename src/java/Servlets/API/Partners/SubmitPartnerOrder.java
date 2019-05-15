/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.API.Partners;

import ExceptionsChi.DoesntExistException;
import Methods.API.Authenticators.Responser;
import Methods.API.Orders.OrderObjector;
import Methods.API.Partner.PartnerCompleteOrder;
import Methods.API.Partner.PartnerOrderChecker;
import Objects.Partner.PartnerReceipt;
import Objects.Partner.PartnerSafe;
import Objects.Payment.Payment;
import Objects.Ship.Ship;
import Objects.SiteSetting.SmsConfig;
import SQL.Commands.OrderHistorySQLCommands;
import SQL.Commands.PartnerSQLCommands;
import SQL.Commands.SiteSettingSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import SmsPishgam.Methods.SmsSender;
import SmsPishgam.Objects.Sms;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class SubmitPartnerOrder extends HttpServlet {

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
            out.println("<title>Servlet SubmitPartnerOrder</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SubmitPartnerOrder at " + request.getContextPath() + "</h1>");
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
        response.setContentType("text/html;charset=UTF-8");
        try (Connection connection=new SQLDriverMacaronBa().getConnection())
        {
            doIt(request, response, connection);
        }catch (Exception ex)
        {
            response.getWriter().println(ex.getMessage());
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
        PrintWriter out=response.getWriter();
        Responser responser=new Responser();
        request.setCharacterEncoding("UTF-8");
        PartnerSafe partnerSafe=new PartnerSafe(request, connection);
        if(!partnerSafe.getLogged())
        {
            out.println(responser.response(-2).toString());
            return;
        }
        
        String data="";
        BufferedReader buffreader=request.getReader();
        int i=0;
        while((i=buffreader.read())!=-1)
        {
            data+=(char)i;
        }
        int partnerId=partnerSafe.getPartner().getPartnerId();
        
        JSONObject dataObject=new JSONObject(data);
        PartnerSQLCommands partnerSQL=new PartnerSQLCommands(connection);
        
        int orderId=partnerSQL.getEmptyCartId(partnerId);
        if(orderId==-1)
        {
            out.println(responser.response(10));
            return;
        }
        
        PartnerReceipt receipt=partnerSQL.getPartnerReceipt(orderId);
        
        if(receipt.getParts().size()==0)
        {
          out.println(responser.response(10));
            return;  
        }
        
        PartnerOrderChecker orderChecker=new PartnerOrderChecker(receipt, connection,partnerSafe.getPartner());
        if(!orderChecker.isValid())
        {
            out.println(responser.response(orderChecker.getRanOutResponse(), 20));
            return;
        }
        
        OrderHistorySQLCommands orderHisSQL=new OrderHistorySQLCommands(connection);
        OrderObjector orderObjector=new OrderObjector();
        
        Ship ship=orderObjector.getShipObject(dataObject.optJSONObject("ship")); 
        if(!partnerSQL.isAddressValid(ship.getAddress().getAddressId(), partnerId))
        {
            out.println(responser.response(30));
            return;
        }
        
        if(!orderHisSQL.isShipTypeValid(ship.getShipType().getShipTypeId()))
        {
            out.println(responser.response(30).toString());
            return;
        }
        
        Payment payment=orderObjector.getPayemntObject(dataObject.optJSONObject("payment"));
        if(!orderHisSQL.isPaymentTypeValid(payment.getPaymentType().getPaymentTypeId()))
        {
            out.println(responser.response(30).toString());
            return;
        }
        
        PartnerCompleteOrder completeOrder=new PartnerCompleteOrder(connection,partnerSafe.getPartner());
        completeOrder.completeOrder(receipt, ship, payment);
        
        JSONObject resp=new JSONObject();
        resp.put("orderId", orderId);
        
        out.println(responser.response(resp, 1));
        out.flush();
        
        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        SmsConfig smsconfig=siteSQL.getSmsConfig();
        if(smsconfig.isConfig("setOrder"))
        {
            Sms sms=new Sms();
            sms.setReciever(partnerSafe.getPartner().getPhoneNumber()).setUserId(-3)
                    .setText("سفارش شما با شماره پیگیری "
                            + orderId
                            + " ثبت شد.");
            SmsSender sender=new SmsSender(connection);
            sender.sendSms(sms);
        }
        
        if(smsconfig.isConfig("setOrderManeger"))
        {
            Sms sms=new Sms();
            sms.setReciever(smsconfig.getAdminNumber()).setUserId(0)
                    .setText("سفارش همکار جدیدی با شماره "
                            + orderId
                            + " ثبت شد. به پنل ادمین مراجعه کنید");
            SmsSender sender=new SmsSender(connection);
            sender.sendSms(sms);
        }
        
    }
}

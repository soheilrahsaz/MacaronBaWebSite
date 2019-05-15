/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.API.Authenticators;

import ExceptionsChi.DoesntExistException;
import Methods.API.Authenticators.Responser;
import Methods.API.User.UserJsoner;
import Objects.Authenticators.User.AuthSafe;
import Objects.Authenticators.User.UserSafe;
import Objects.SiteSetting.SmsConfig;
import Objects.User.User;
import SQL.Commands.AuthenticatorSQLCommands;
import SQL.Commands.SiteSettingSQLCommands;
import SQL.Commands.UserSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import SmsPishgam.Methods.SmsSender;
import SmsPishgam.Objects.Sms;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class Register extends HttpServlet {

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
            out.println("<title>Servlet Register</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Register at " + request.getContextPath() + "</h1>");
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
    throws ServletException, IOException, SQLException, NoSuchAlgorithmException, DoesntExistException, ClassNotFoundException 
    {
        PrintWriter out=response.getWriter();
        Responser responser=new Responser();
        request.setCharacterEncoding("UTF-8");
//        UserSafe userSafe=new UserSafe(request, connection);
//        if(userSafe.getLogged())
//        {
//            out.println(responser.response(-2).toString());
//            return;
//        }
        
        String data="";
        BufferedReader buffreader=request.getReader();
        int i=0;
        while((i=buffreader.read())!=-1)
        {
            data+=(char)i;
        }
        
        JSONObject userAuth=new JSONObject(data);
        String userName=userAuth.optString("userName");
        String password=userAuth.optString("password");
        String invitedBy=userAuth.optString("invitedBy");
        
        
        if(Methods.Methods.isNullOrEmpty(userName,password))
        {
            out.println(responser.response(10));
            return;
        }
        
        userName=Methods.Methods.normalizePhone(userName);
        
        if(userName.length()!=12)
        {
            out.println(responser.response(10));
            return;
        }
        
        AuthenticatorSQLCommands authSQL=new AuthenticatorSQLCommands(connection);
        if(authSQL.usernameExists(userName))
        {
            out.println(responser.response(20));
            return;
        }
        
        int userId=authSQL.regiserUser(userName, password);
        String MSS_Token=Methods.Encryptors.Encrypt1(userId);
        AuthSafe auth=new AuthSafe(userId, connection);
        auth.generateCode();
        
        UserSQLCommands userSQL=new UserSQLCommands(connection);
        User user=userSQL.getUserFull(userId);
        UserJsoner userJsoner=new UserJsoner();
        JSONObject userObject=userJsoner.getUserInfoObject(user);
        JSONObject authObject=responser.getAuthObject(auth);
        JSONObject object=new JSONObject();
        object.put("MSS_Token", MSS_Token);
        object.put("user", userObject);
        object.put("auth", authObject);
        
        JSONObject responseObject=responser.response(object, 1);
        out.println(responseObject.toString());
        
        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        SmsConfig smsConfig=siteSQL.getSmsConfig();
        
        if(smsConfig.isConfig("register"))
        {
            Sms sms=new Sms();
            sms.setReciever(userName).setUserId(userId).setText("ثبت نام شما با موفقیت انجام شد، پس از تکمیل مشخصات حساب خود، میتوانید از امکانات سایت ماکارون با استفاده نمایید.");
            SmsSender sender=new SmsSender(connection);
            sender.sendSms(sms);
        }
        
        String userInviter=Methods.Encryptors.Decript1(invitedBy);
        if(!Methods.Methods.isNumber(userInviter))
        {
            return;
        }
        int inviter=Integer.parseInt(userInviter);
        int userIdInviter=userSQL.getUserInviterId(inviter);
        if(userIdInviter==-1)
        {
            return;
        }
        
        userSQL.addInvitedBy(userIdInviter, userId);
        
        int invitedCount=userSQL.getInvitedCount(userIdInviter);
        HashMap<String,String> datas=new SiteSettingSQLCommands(connection).getAutoDiscountConfig();
        int minInvite=Integer.parseInt(datas.get("inviteDiscountCount"));
        if(invitedCount>=minInvite)
        {
            userSQL.addDiscountTicket(Double.parseDouble(datas.get("inviteDiscountEffect")),
                    new Timestamp(System.currentTimeMillis()+604800000), userIdInviter);
            userSQL.invalidateInvitedPrize(userIdInviter);
        }
        
        
    }
    
}

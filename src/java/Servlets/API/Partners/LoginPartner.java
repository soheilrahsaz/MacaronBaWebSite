/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.API.Partners;

import ExceptionsChi.DoesntExistException;
import Methods.API.Authenticators.Responser;
import Methods.API.Partner.PartnerJsoner;
import Methods.API.User.UserJsoner;
import Objects.Authenticators.User.LoginSafe;
import Objects.Partner.Partner;
import Objects.Partner.PartnerLoginSafe;
import Objects.User.User;
import SQL.Commands.UserSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
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
public class LoginPartner extends HttpServlet {

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
            out.println("<title>Servlet LoginPartner</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginPartner at " + request.getContextPath() + "</h1>");
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
            throws ServletException, IOException, SQLException, ClassNotFoundException, NoSuchAlgorithmException, DoesntExistException
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
        userName=Methods.Methods.normalizePhone(userName);
        
        PartnerLoginSafe loginSafe=new PartnerLoginSafe(request, connection);
        Partner partner=null;
        try {
             partner=loginSafe.login(userName, password);
        
        } catch (DoesntExistException ex) {
            out.println(responser.response(10));
            return;
        }
        
        String MSS_Token=Methods.Encryptors.Encrypt1(partner.getPartnerId());
        
        PartnerJsoner partnerJsoner=new PartnerJsoner(connection);
        JSONObject partnerObject=partnerJsoner.getPartnerJson(partner);
        JSONObject fullObject=new JSONObject();
        fullObject.put("user", partnerObject);
        fullObject.put("MSSP_Token", MSS_Token);
        JSONObject responseObject=responser.response(fullObject, 1);
        out.println(responseObject.toString());
        
    }
}

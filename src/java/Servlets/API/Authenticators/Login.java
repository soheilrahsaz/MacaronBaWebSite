/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.API.Authenticators;

import ExceptionsChi.DoesntExistException;
import Methods.API.Authenticators.Responser;
import Methods.API.Orders.AddPart;
import Methods.API.Orders.OrderObjector;
import Methods.API.User.UserJsoner;
import Objects.Authenticators.User.LoginSafe;
import Objects.Authenticators.User.UserSafe;
import Objects.Order.Part;
import Objects.User.User;
import SQL.Commands.UserSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.BufferedReader;
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
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class Login extends HttpServlet {

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
        
        LoginSafe loginSafe=new LoginSafe(request, connection);
        User user=null;
        try {
             user=loginSafe.login(userName, password);
        
        } catch (DoesntExistException ex) {
            out.println(responser.response(10));
            return;
        }
        
        String MSS_Token=Methods.Encryptors.Encrypt1(user.getUserId());
        UserSQLCommands userSQL=new UserSQLCommands(connection);
        
        UserJsoner userJsoner=new UserJsoner();
        JSONObject userObject=userJsoner.getUserInfoObject(userSQL.getUserFull(user.getUserId()));
        JSONObject fullObject=new JSONObject();
        fullObject.put("user", userObject);
        fullObject.put("MSS_Token", MSS_Token);
        JSONObject responseObject=responser.response(fullObject, 1);
        out.println(responseObject.toString());
        
    }

}

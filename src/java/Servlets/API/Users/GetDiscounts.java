/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.API.Users;

import ExceptionsChi.DoesntExistException;
import Methods.API.Authenticators.Responser;
import Methods.API.User.UserJsoner;
import Objects.Authenticators.User.UserSafe;
import Objects.User.DiscountTicket;
import SQL.Commands.UserSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class GetDiscounts extends HttpServlet {

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
            out.println("<title>Servlet GetDiscounts</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetDiscounts at " + request.getContextPath() + "</h1>");
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
        UserSafe user=new UserSafe(request, connection);
        if(!user.getLogged())
        {
            out.println(responser.response(-2).toString());
            return;
        }
        
//        String data="";
//        BufferedReader buffreader=request.getReader();
//        int i=0;
//        while((i=buffreader.read())!=-1)
//        {
//            data+=(char)i;
//        }

        UserSQLCommands userSQL=new UserSQLCommands(connection);
        LinkedList<DiscountTicket> discounts=userSQL.getDiscountsValid(user.getUser().getUserId());
        UserJsoner userJsoner=new UserJsoner();
        JSONArray discountArray=userJsoner.getDiscountsArray(discounts);
        out.println(responser.response(discountArray, 1));
        
    }
}

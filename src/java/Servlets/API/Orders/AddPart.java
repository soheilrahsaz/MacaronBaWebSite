/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.API.Orders;

import ExceptionsChi.DoesntExistException;
import Methods.API.Authenticators.Responser;
import Methods.API.Orders.CheckPart;
import Methods.API.Orders.OrderObjector;
import Objects.Authenticators.User.UserSafe;
import Objects.Order.Part;
import SQL.Commands.OrderSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
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
public class AddPart extends HttpServlet {

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
        
        String data="";
        BufferedReader buffreader=request.getReader();
        int i=0;
        while((i=buffreader.read())!=-1)
        {
            data+=(char)i;
        }
        
        OrderSQLCommands orderSQL=new OrderSQLCommands(connection);
        OrderObjector objector=new OrderObjector();
        Part part=objector.getPartObject(new JSONObject(data));
        CheckPart checkpart=new CheckPart(connection, part);
        if(!checkpart.isValid())
        {
            out.println(responser.response(10));
            return;
        }
        
        int orderId=orderSQL.getCartOrderId(user.getUser().getUserId());
        Methods.API.Orders.AddPart addpart=new Methods.API.Orders.AddPart(connection, orderId, part);
        addpart.addPart();
        out.println(responser.response(1).toString());
    }
}

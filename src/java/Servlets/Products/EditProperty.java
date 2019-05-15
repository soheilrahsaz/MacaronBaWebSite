/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Products;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.Printers.AdminPanel.Parameters;
import SQL.Commands.ProductSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
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
public class EditProperty extends HttpServlet {
    private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_LOGIN="/AdminPanel/Login.jsp";
private final String URL_PRODUCTS="/AdminPanel/Products/ProductsList.jsp";
private final String URL_LIST="/AdminPanel/Products/List/";
   

   
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
            response.sendRedirect(URL_LOGIN+"?att=loginFirst");
            return;
        }
        
        Admin admin=adminsafe.getAdmin();
        if(!admin.haveAccessTo("editProduct"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        String product=request.getParameter("productId");
        String property=request.getParameter("propertyId");
        
        int productId=Integer.parseInt(product);
        int propertyId=Integer.parseInt(property);
        
        String name=request.getParameter("name");
        String status=request.getParameter("status");
        
        if(Methods.Methods.isNullOrEmpty(name,status))
        {
            response.sendRedirect(URL_LIST+product+"/"+property+"?att=empty");
            return;
        }
        
        
        ProductSQLCommands productSQL=new ProductSQLCommands(connection);
        
        if(productSQL.propertyExistsExcept(productId,propertyId, name))
        {
            response.sendRedirect(URL_LIST+product+"/"+property+"?att=nameExists");
            return;
        }
        
        String multiple=""+request.getParameter("multiple");
        String description=request.getParameter("description");
        String effectAll=""+request.getParameter("effectAll");
        
        int statusId=Integer.parseInt(status);
        boolean multipleBool=multiple.equalsIgnoreCase("on");
        boolean effectAllBool=effectAll.equalsIgnoreCase("on");
        
        productSQL.editProperty(propertyId, name, multipleBool,effectAllBool, statusId,description);
        
        response.sendRedirect(URL_LIST+product+"/"+property+"?att=ok");
        return;
    }
}

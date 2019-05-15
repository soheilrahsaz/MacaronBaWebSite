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
import Objects.Product.Product;
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
 * /AdminPanel/Products/EditProduct
 * @author Moses
 */
public class EditProduct extends HttpServlet {
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
        
        ProductSQLCommands productSQL=new ProductSQLCommands(connection);
        
        String category=request.getParameter("category");
        String productId=request.getParameter("productId");
        String productName=request.getParameter("productName");
        String description=""+request.getParameter("description");
        String basePrice=request.getParameter("basePrice");
        String status=request.getParameter("status");
        
        int productID=Integer.parseInt(productId);
        Product product=productSQL.getProduct(productID);
        
        if(product.getProductId()==0)
        {
            response.sendRedirect(URL_PRODUCTS+"?att=noProduct");
            return;
        }
        
        if(Methods.Methods.isNullOrEmpty(category,productName,basePrice,status))
        {
            response.sendRedirect(URL_LIST+productId+"?att=empty");
            return;
        }
        
        if(!Methods.Methods.isNumber(basePrice))
        {
            response.sendRedirect(URL_LIST+productId+"?att=number");
            return;
        }
        
        if(productSQL.productNameExists(productName, productID))
        {
            response.sendRedirect(URL_LIST+productId+"?att=nameExists");
            return;
        }
        
        int categoryId=productSQL.getCategoryId(category);
        int price=Integer.parseInt(basePrice);
        int statusId=Integer.parseInt(status);
        productSQL.editProduct(productID, productName, categoryId, description, price,statusId);
        response.sendRedirect(URL_LIST+productId+"?att=ok");
        return;
        
    }
                    
}

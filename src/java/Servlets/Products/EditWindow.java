/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Products;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
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
public class EditWindow extends HttpServlet {
    
    private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_LOGIN="/AdminPanel/Login.jsp";
private final String URL_ADD_WINDOW="/AdminPanel/Products/AddProductWindow.jsp";
private final String URL_WINDOWS_LIST="/AdminPanel/Products/Windows/List/";
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
            out.println("<title>Servlet EditWindow</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditWindow at " + request.getContextPath() + "</h1>");
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
        AdminSafe adminSafe=new AdminSafe(request, connection);
        
        if(!adminSafe.getLogged())
        {
            response.sendRedirect(URL_LOGIN+"?att=loginFirst");
            return;
        }
        
        Admin admin=adminSafe.getAdmin();
        if(!admin.haveAccessTo("editWindow"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        String product=request.getParameter("productId");
        String category=request.getParameter("category");
        String name=request.getParameter("name");
        String description=""+request.getParameter("description");
        String price=request.getParameter("price");
        String count=request.getParameter("count");
        String status=request.getParameter("status");
        
        int productId=Integer.parseInt(product);
        
        if(Methods.Methods.isNullOrEmpty(name,price,category,count))
        {
            response.sendRedirect(URL_WINDOWS_LIST+product+"?att=empty");
            return;
        }
        ProductSQLCommands productSQL=new ProductSQLCommands(connection);
        
        if(!Methods.Methods.isNumber(price) || !Methods.Methods.isNumber(count))
        {
            response.sendRedirect(URL_WINDOWS_LIST+product+"?att=number");
            return;
        }
        int countVal=Integer.parseInt(count);
        int priceVal=Integer.parseInt(price);
        int categoryId=Integer.parseInt(category);
        int statusId=Integer.parseInt(status);
        
        productSQL.editWindow(productId, name, categoryId, priceVal, countVal, description, statusId);
        response.sendRedirect(URL_WINDOWS_LIST+product+"?att=ok");
        return;
        
    }
}

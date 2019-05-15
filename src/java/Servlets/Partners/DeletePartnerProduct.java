/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Partners;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import SQL.Commands.PartnerSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
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
public class DeletePartnerProduct extends HttpServlet {
private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_ADMIN_LOGIN="../Login.jsp";
private final String URL_ADD_PRODUCT="AddProduct.jsp";
private final String URL_PRODUCT_LIST="Products.jsp";
private final String URL_SHOW_PRODUCT="Products/";
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
            out.println("<title>Servlet DeletePartnerProduct</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeletePartnerProduct at " + request.getContextPath() + "</h1>");
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
            throws ServletException, IOException, SQLException, ClassNotFoundException, DoesntExistException, NoSuchAlgorithmException
    {
        request.setCharacterEncoding("UTF-8");
        AdminSafe adminSafe=new AdminSafe(request, connection);
        
        if(!adminSafe.getLogged())
        {
            response.sendRedirect(URL_ADMIN_LOGIN+"?att=loginFirst");
            return;
        }
        
        Admin admin=adminSafe.getAdmin();
        if(!admin.haveAccessTo("productPartner"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        String product=request.getParameter("productId");
        int productId=Integer.parseInt(product);
        
        PartnerSQLCommands partnerSQL=new PartnerSQLCommands(connection);
        partnerSQL.deleteUpdatePartnerProduct(productId);
        
        response.sendRedirect(URL_PRODUCT_LIST+"?att=ok");
        return;
    }
}

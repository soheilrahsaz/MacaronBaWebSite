/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Products;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.Product.Picture;
import SQL.Commands.ProductSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
public class DeletePicture extends HttpServlet {
    private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_LOGIN="/AdminPanel/Login.jsp";
private final String URL_PRODUCTS="/AdminPanel/Products/ProductsList.jsp";
private final String URL_LIST="/AdminPanel/Products/List/";
private final String URL_WINDOW_LIST="/AdminPanel/Products/Windows/List/";
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
        try (Connection connection=new SQLDriverMacaronBa().getConnection())
        {
            doIt(request, response, connection);
        } catch (Exception e) {
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
        processRequest(request, response);
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
        
        String type=""+request.getParameter("type");
        boolean isWindow=type.equals("window");
        
        Admin admin=adminSafe.getAdmin();
        if(!isWindow && !admin.haveAccessTo("editProduct"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        if(!admin.haveAccessTo("editWindow"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        String pictureId=request.getParameter("pictureId");
        int pictureID=Integer.parseInt(pictureId);
        ProductSQLCommands productSQL=new ProductSQLCommands(connection);
        Picture picture=productSQL.getPicture(pictureID);
        if(picture.getPictureId()==0)
        {
            response.sendRedirect(URL_PRODUCTS+"?att=noPicture");
            return;
        }
        
        productSQL.deletePicture(pictureID);
        
        try 
        {
            String path=System.getProperty("catalina.home")+"/"+getServletContext().getInitParameter("productPicture")+"/"+picture.getProductId()+"/"+picture.getUrl();
            Files.deleteIfExists(Paths.get(path));
        } catch (Exception e) {
        }
        
        if(isWindow)
        {
            response.sendRedirect(URL_WINDOW_LIST+picture.getProductId()+"?att=ok");
            return;
        }
        response.sendRedirect(URL_LIST+picture.getProductId()+"?att=ok");
        return;
            
    }
}

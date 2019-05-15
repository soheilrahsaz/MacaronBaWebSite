/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Products;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.Product.Product;
import SQL.Commands.ProductSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Moses
 */
@MultipartConfig
public class AddPicture extends HttpServlet {
    private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_LOGIN="/AdminPanel/Login.jsp";
private final String URL_PRODUCTS="/AdminPanel/Products/ShowProduct.jsp";
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddPicture</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddPicture at " + request.getContextPath() + "</h1>");
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
            doId(request, response, connection);
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

    private void doId(HttpServletRequest request, HttpServletResponse response,Connection connection)
            throws ServletException, IOException, SQLException, ClassNotFoundException, DoesntExistException
    {
        request.setCharacterEncoding("UTF-8");
        
        AdminSafe adminsafe=new AdminSafe(request, connection);
        if(!adminsafe.getLogged())
        {
            response.sendRedirect(URL_LOGIN+"?att=loginFirst");
            return;
        }
        
        String type=""+request.getParameter("type");
        boolean isWindow=type.equals("window");
        
        Admin admin=adminsafe.getAdmin();
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
        
        
        ProductSQLCommands productSQL=new ProductSQLCommands(connection);
        String productId=request.getParameter("productId");
        int productID=Integer.parseInt(productId);
        Product product=productSQL.getProduct(productID);
        int size=0;
        if(product.getProductId()!=0)
        {
            size=product.getPictures().size();
           
        }else
        {
            size=productSQL.getProductWindow(productID).getPictures().size();
        }
        
        int count=0;
        
        Random random=new Random();
        Collection<Part> parts=request.getParts();
        for(Part part:parts)
        {
            if(part.getName().equalsIgnoreCase("picture")&& (count+size)<4)
            {
                String filename=""+part.getSubmittedFileName();
                
                if(filename.isEmpty())
                continue; 
                
                String extension=Methods.Methods.getExtension(filename);
                String newFileName=""+
                        ((char)(random.nextInt(26)+'a'))+
                        ((char)(random.nextInt(26)+'a'))+
                        ((char)(random.nextInt(26)+'a'))+
                        ((char)(random.nextInt(26)+'a'))+
                        ((char)(random.nextInt(26)+'a'))+"."+extension;
                String path=Methods.Methods.mkdirForProuductPic(getServletContext().getInitParameter("productPicture"), productID)+newFileName;
                BufferedInputStream buffIn=new BufferedInputStream(part.getInputStream());
                BufferedOutputStream buffout=new BufferedOutputStream(new FileOutputStream(path));
                int read=0;
                while((read=buffIn.read())!=-1)
                {
                    buffout.write(read);
                }
                buffIn.close();
                buffout.close();
                
                productSQL.addProductPicture(productID, newFileName);
                
                count++;
            }
        }
        
        if(isWindow)
        {
            response.sendRedirect(URL_WINDOW_LIST+productId+"?att=ok");
            return;
        }
        
        response.sendRedirect(URL_LIST+productId+"?att=ok");
        return;
        
    }
}

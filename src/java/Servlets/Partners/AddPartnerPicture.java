/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Partners;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.Partner.PartnerProduct;
import Objects.Product.Product;
import SQL.Commands.PartnerSQLCommands;
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
public class AddPartnerPicture extends HttpServlet {
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
            out.println("<title>Servlet AddPartnerPicture</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddPartnerPicture at " + request.getContextPath() + "</h1>");
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
            response.sendRedirect(URL_ADMIN_LOGIN+"?att=loginFirst");
            return;
        }
        
        Admin admin=adminsafe.getAdmin();
        
        if(!admin.haveAccessTo("productPartner"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        
        ProductSQLCommands productSQL=new ProductSQLCommands(connection);
        PartnerSQLCommands partnerSQL=new PartnerSQLCommands(connection);
        
        String productId=request.getParameter("productId");
        int productID=Integer.parseInt(productId);
        PartnerProduct product=partnerSQL.getPartnerProduct(productID);
        int size=0;
        if(product.getProductId()!=0)
        {
            size=product.getPictures().size();
           
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
        
        
        response.sendRedirect(URL_SHOW_PRODUCT+productId+"?att=ok");
        return;
        
    }
}

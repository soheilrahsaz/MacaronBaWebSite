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
public class AddWindow extends HttpServlet {
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
            out.println("<title>Servlet AddWindow</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddWindow at " + request.getContextPath() + "</h1>");
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
        if(!admin.haveAccessTo("addWindow"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        String category=request.getParameter("category");
        String name=request.getParameter("name");
        String description=""+request.getParameter("description");
        String price=request.getParameter("price");
        String count=request.getParameter("count");
        
        if(Methods.Methods.isNullOrEmpty(name,price,category,count))
        {
            response.sendRedirect(URL_ADD_WINDOW+"?att=empty");
            return;
        }
        ProductSQLCommands productSQL=new ProductSQLCommands(connection);
        
        if(!Methods.Methods.isNumber(price) || !Methods.Methods.isNumber(count))
        {
            response.sendRedirect(URL_ADD_WINDOW+"?att=number");
            return;
        }
        int countVal=Integer.parseInt(count);
        int priceVal=Integer.parseInt(price);
        int categoryId=Integer.parseInt(category);
        int productId=productSQL.addProductWindow(name, categoryId,priceVal, countVal, description);
        
        Random random=new Random();
        
        Collection<Part> parts=request.getParts();
        int countPic=0;
        for(Part part:parts)
        {
            if(part.getName().equalsIgnoreCase("picture") && countPic<4)
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
                String path=Methods.Methods.mkdirForProuductPic(getServletContext().getInitParameter("productPicture"), productId)+newFileName;
                BufferedInputStream buffIn=new BufferedInputStream(part.getInputStream());
                BufferedOutputStream buffout=new BufferedOutputStream(new FileOutputStream(path));
                int read=0;
                while((read=buffIn.read())!=-1)
                {
                    buffout.write(read);
                }
                buffIn.close();
                buffout.close();
                
                productSQL.addProductPicture(productId, newFileName);
                
                countPic++;
                
            }
        }
        
        response.sendRedirect(URL_WINDOWS_LIST+productId+"?att=ok");
        return;

    }
}

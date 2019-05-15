/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.SiteSetting.Video;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import SQL.Commands.SiteSettingSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
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
public class SetMainVideo extends HttpServlet {
private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_LOGIN="/AdminPanel/Login.jsp";
private final String URL_MAIN_VIDEO="MainVideo.jsp";
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
            out.println("<title>Servlet SetMainVideo</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SetMainVideo at " + request.getContextPath() + "</h1>");
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
        AdminSafe adminsafe=new AdminSafe(request, connection);
        
        if(!adminsafe.getLogged())
        {
            response.sendRedirect(URL_LOGIN+"?att=loginfirst");
            return;
        }
        
        Admin admin=adminsafe.getAdmin();
        if(!admin.haveAccessTo("addVideo"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
        
        String name=request.getParameter("name");
        String description=request.getParameter("description");
        
        if(Methods.Methods.isNullOrEmpty(name))
        {
            response.sendRedirect(URL_MAIN_VIDEO+"?att=empty");
            return;
        }
        
        int sectionId=0;
        
        
        Part part=request.getPart("video");
        Random random=new Random();
        String filename=""+part.getSubmittedFileName();
        
        if(Methods.Methods.isNullOrEmpty(filename))
        {
            response.sendRedirect(URL_MAIN_VIDEO+"?att=nofile");
            return;
        }

        String extension=Methods.Methods.getExtension(filename);
        String newFileName=""+
                ((char)(random.nextInt(26)+'a'))+
                ((char)(random.nextInt(26)+'a'))+
                ((char)(random.nextInt(26)+'a'))+
                ((char)(random.nextInt(26)+'a'))+
                ((char)(random.nextInt(26)+'a'))+"."+extension;
        String path=Methods.Methods.mkdirForVideo(getServletContext().getInitParameter("Videos"), sectionId)+newFileName;
        BufferedInputStream buffIn=new BufferedInputStream(part.getInputStream());
        BufferedOutputStream buffout=new BufferedOutputStream(new FileOutputStream(path));
        int read=0;
        while((read=buffIn.read())!=-1)
        {
            buffout.write(read);
        }
        buffIn.close();
        buffout.close();
       
        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        int videoId=siteSQL.addVideo(newFileName, name, description, "", sectionId, admin.getAdminId(), 1,1);
        siteSQL.setMainVideo(videoId);
        response.sendRedirect(URL_MAIN_VIDEO+"?att=ok");
        return;
        
    }
}

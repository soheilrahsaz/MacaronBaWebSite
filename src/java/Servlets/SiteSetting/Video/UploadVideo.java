/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.SiteSetting.Video;

import ExceptionsChi.DoesntExistException;
import Methods.API.Authenticators.Responser;
import Objects.Authenticators.User.UserSafe;
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
public class UploadVideo extends HttpServlet {
private final static String URL_UPLAOD_VIDEO="dashboard/upload";
private final static String URL_LOGIN="returnUrl=%2Fdashboard";
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
            out.println("<title>Servlet UploadVideo</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadVideo at " + request.getContextPath() + "</h1>");
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
        PrintWriter out=response.getWriter();
        Responser responser=new Responser();
        request.setCharacterEncoding("UTF-8");
        UserSafe user=new UserSafe(request, connection);
        if(!user.getLogged())
        {
//            response.sendRedirect(URL_LOGIN+"?att=login");
            out.println(responser.response(-2));
            return;
        }
        
        
        
        String sectionid=request.getParameter("sectionId");
        String name=request.getParameter("name");
        String description=""+request.getParameter("description");
        String tags="";
        
        if(Methods.Methods.isNullOrEmpty(sectionid,name))
        {
//            response.sendRedirect(URL_UPLAOD_VIDEO+"?att=empty");
//            out.println("empty");
            out.println(responser.response(10));
            return;
        }
        
        
        int sectionId=Integer.parseInt(sectionid);
        
        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        int uploadedCount=siteSQL.uploadedBySection(user.getUser().getUserId(), sectionId);
        if(uploadedCount>=2)
        {
//            response.sendRedirect(URL_UPLAOD_VIDEO+"?att=toomanyupload");
//              out.println("toomanyupload");
              out.println(responser.response(20));
            return;
        }
                
        Part part=request.getPart("video");
        if(part==null)
        {
//            out.println("nofile");
            out.println(responser.response(30));
            return;
        }
        
        Random random=new Random();
        String filename=""+part.getSubmittedFileName();
        
        if(Methods.Methods.isNullOrEmpty(filename))
        {
//            response.sendRedirect(URL_UPLAOD_VIDEO+"?att=nofile");
//            out.println("nofile");
            out.println(responser.response(30));
            return;
        }
        
        if(part.getSize()>40*1024*1024)
        {
//            response.sendRedirect(URL_UPLAOD_VIDEO+"?att=filelarge");
//            out.println("filelarge");
            out.println(responser.response(40));
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
       
        int videoId=siteSQL.addVideo(newFileName, name, description, tags, sectionId, user.getUser().getUserId(), 0,0);
//        response.sendRedirect(URL_UPLAOD_VIDEO+"?att=ok");
//        out.println("ok");
        out.println(responser.response(1));
        return;
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.SiteSetting;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.Admin.AdminSafe;
import Objects.SiteSetting.LinkStat;
import SQL.Commands.SiteSettingSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Moses
 */
public class SetLinksStatus extends HttpServlet {
private final String URL_INDEX="/AdminPanel/index.jsp";
private final String URL_LOGIN="/AdminPanel/Login.jsp";
private final String URL_SITE_SETTING="SiteInfo.jsp";
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
            out.println("<title>Servlet SetLinksStatus</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SetLinksStatus at " + request.getContextPath() + "</h1>");
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
        if(!admin.haveAccessTo("editSiteView"))
        {
            response.sendRedirect(URL_INDEX+"?att=noaccess");
            return;
        }
     
        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        LinkedList<LinkStat> linkstats=siteSQL.getLinkStats();
        
        for(LinkStat linkstat:linkstats)
        {
            String stat=request.getParameter(linkstat.getLink()+"box");
            if(Methods.Methods.isNullOrEmpty(stat))
                linkstat.setStatus(1);
            else if(stat.equalsIgnoreCase("on"))
                linkstat.setStatus(0);
            
            String newText=request.getParameter(linkstat.getLink());
            if(!linkstat.getText().equals(newText))
                linkstat.setText(newText);
        }
        
        siteSQL.editLinkStats(linkstats);
        String views=request.getParameter("views");
        String chat=request.getParameter("chat");
        
        if(Methods.Methods.isNullOrEmpty(views))
            siteSQL.setViewsAvailable(false);
        else if(views.equalsIgnoreCase("on"))
            siteSQL.setViewsAvailable(true);
        
        if(Methods.Methods.isNullOrEmpty(chat))
            siteSQL.setChatsAvailable(false);
        else if(chat.equalsIgnoreCase("on"))
            siteSQL.setChatsAvailable(true);
        
        response.sendRedirect(URL_SITE_SETTING+"?att=ok");
        return;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.API.SiteSettings;

import ExceptionsChi.DoesntExistException;
import Methods.API.Authenticators.Responser;
import Methods.API.SiteSetting.SiteSettingJsoner;
import Objects.SiteSetting.Advertisment;
import Objects.SiteSetting.LinkStat;
import Objects.SiteSetting.SlideShow;
import Objects.Text.Text;
import SQL.Commands.CommunicationSQLCommands;
import SQL.Commands.SiteSettingSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class GetSiteData extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
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
//        UserSafe user=new UserSafe(request, connection);
//        if(!user.getLogged())
//        {
//            out.println(responser.response(-2).toString());
//            return;
//        }
        
//        String data="";
//        BufferedReader buffreader=request.getReader();
//        int i=0;
//        while((i=buffreader.read())!=-1)
//        {
//            data+=(char)i;
//        }

        SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
        CommunicationSQLCommands comSQL=new CommunicationSQLCommands(connection);
        LinkedList<Text> text=comSQL.getTexts();
        HashMap<String,String> data=siteSQL.getSiteInfo();
        LinkedList<SlideShow> slideShows=siteSQL.getSlideShows();
        LinkedList<Advertisment> advertisments=siteSQL.getAdvertisments();
        LinkedList<LinkStat> linkstats=siteSQL.getLinkStats();
        HashMap<String,Integer> views=siteSQL.getViews();
        
        SiteSettingJsoner jsoner=new SiteSettingJsoner(connection);
        JSONObject siteObject=jsoner.getSiteDataJson(data,text,slideShows,advertisments,linkstats, views);
        JSONObject responseObject=responser.response(siteObject, 1);
        out.println(responseObject.toString());
        
    }
}

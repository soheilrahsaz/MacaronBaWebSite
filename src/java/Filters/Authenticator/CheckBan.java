/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Filters.Authenticator;

import Objects.Authenticators.BlockCheck;
import SQL.Commands.AuthenticatorSQLCommands;
import SQL.Commands.SiteSettingSQLCommands;
import SQL.Driver.SQLDriverMacaronBa;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Moses
 */
public class CheckBan implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        try {
            Connection connection=new SQLDriverMacaronBa().getConnection();
            AuthenticatorSQLCommands authsql=new AuthenticatorSQLCommands(connection);
            String ip=request.getRemoteAddr();
            
            if(authsql.isBanned(ip))
            {
                connection.close();
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("You're banned from this server."
                        + "Please contact administrator if you think this is a mistake"
                        + "شما از استفاده از این سایت محروم شده اید.");
                
            }else
            {
                String uri=((HttpServletRequest)request).getRequestURI();
                
                if(!uri.startsWith("/API") && !uri.startsWith("/AdminPanel") && !uri.startsWith("/MacaronBaData")
                        && !uri.startsWith("/assets")
                        && !uri.startsWith("/footer")
                        && !uri.startsWith("/runtime")
                        && !uri.startsWith("/styles")
                        && !uri.startsWith("/banner")
                        && !uri.startsWith("/main")
                        && !uri.startsWith("/common")
                        && !uri.startsWith("/poly")
                        && !uri.startsWith("/scripts")
                        && !uri.startsWith("/IRANSans")
                        && !uri.startsWith("/9")
                        && !uri.startsWith("/8")
                        && !uri.startsWith("/7")
                        && !uri.startsWith("/6")
                        && !uri.startsWith("/5")
                        && !uri.startsWith("/4")
                        && !uri.startsWith("/3")
                        && !uri.startsWith("/2")
                        && !uri.startsWith("/1")
                        && !uri.startsWith("/macaron-")
                        && !uri.startsWith("/gallary")
                        && !uri.startsWith("/favicon")
                        && !uri.startsWith("/null")
                        && !uri.startsWith("/fa"))
                {
//                    System.out.println(uri);
                    SiteSettingSQLCommands siteSQL=new SiteSettingSQLCommands(connection);
                    siteSQL.addView();
                }
                
                
                connection.close();
                chain.doFilter(request, response);
            }
            
        } catch (Exception ex) {
            response.getWriter().println(ex.getMessage());
        } 
        
    }

    @Override
    public void destroy() {
        
    }
    
    
}

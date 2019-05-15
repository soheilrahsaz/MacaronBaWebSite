/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Authenticators.User;

import ExceptionsChi.DoesntExistException;
import Objects.User.User;
import SQL.Commands.AuthenticatorSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 * This class is almost used everywhere, used to check if the client has logged in or not
 * @author Moses
 */
public class UserSafe {
    private HttpServletRequest request;
    private AuthenticatorSQLCommands authSQL;
    private String MSS_Token;
    private User user;
    private boolean logged=false;
    private int userId;
    
    public UserSafe(HttpServletRequest request,Connection connection) throws ClassNotFoundException, SQLException
    {
        this.request=request;
        this.authSQL=new AuthenticatorSQLCommands(connection);
        isLoggedIn();
    }
    
    
    /**
     * checks to see if the client that has requested for the page is logged in with an account or not
     *  true if it has. it returns false if there is no user with the mss_token
     *  false if the user hasn't logged in or he's account is banned
     * @throws SQLException 
     */
    private void isLoggedIn() throws SQLException
    {
        HttpSession session=request.getSession(false);
        if(session==null)
        {
            isLoggedThroughAPI();
            return;
        }
            
        if(session.getAttribute("MSS_Token")==null)
        {
            isLoggedThroughAPI();
            return;
        }
        this.MSS_Token=session.getAttribute("MSS_Token").toString();
        
        String userId=Methods.Encryptors.Decript1(this.MSS_Token);
        if(!Methods.Methods.isNumber(userId))
        {
            this.logged=false;
            return ;
                    
        }
        
        this.userId=Integer.valueOf(userId);
        try
        {
            this.user=authSQL.getUser(this.userId);
            
        } catch (DoesntExistException ex)
        {
            this.logged=false;
            return;
        }
        
        if(user.getStatus()==-1)
        {
            this.logged=false;
            return;
        }
        
        this.logged=true;
        return;
    }
    
    private void isLoggedThroughAPI() throws SQLException
    {
        this.MSS_Token=request.getHeader("MSS_Token");
        if(Methods.Methods.isNullOrEmpty(MSS_Token))
        {
            this.MSS_Token=request.getParameter("MSS_Token");
        }
        if(Methods.Methods.isNullOrEmpty(MSS_Token))
        {
            this.logged=false;
            return;
        }
        
        String userId=Methods.Encryptors.Decript1(this.MSS_Token);
        if(!Methods.Methods.isNumber(userId))
        {
            this.logged=false;
            return ;
                    
        }
        
        this.userId=Integer.valueOf(userId);
        try
        {
            this.user=authSQL.getUser(this.userId);
            
        } catch (DoesntExistException ex)
        {
            this.logged=false;
            return;
        }
        
        if(user.getStatus()==-1)
        {
            this.logged=false;
            return;
        }
        
        this.logged=true;
        return;
    }
    
    /**
     * if the user has logged in, it will return it's data
     * @return the user object that has logged in
     * @throws DoesntExistException if user hasn't logged in or it's blocked
     */
    public User getUser() throws DoesntExistException
    {
        if(!logged)
        {
            throw new DoesntExistException("user doesn't exsit");
        }
        
        return this.user;
    }
     /**
     * if the user has logged in, it will return it's full data
     * @return the user object that has logged in
     * @throws DoesntExistException if user hasn't logged in or it's blocked
     */
    public User getUserFull() throws SQLException, DoesntExistException
    {
        return this.authSQL.getUserFull(this.userId);
    }
    
    /**
     * 
     * @return true if user is logged in and false if user is blocked or not logged in
     */
    public boolean getLogged()
    {
        return this.logged;
    }
}

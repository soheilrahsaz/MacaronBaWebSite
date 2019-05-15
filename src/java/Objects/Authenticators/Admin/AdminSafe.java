/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Authenticators.Admin;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import SQL.Commands.AuthenticatorSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * this object is used in admin pages, showing that if the admin is logged in or not
 * @author Moses
 */
public class AdminSafe {
    private HttpServletRequest request;
    private AuthenticatorSQLCommands authSQL;
    private String MSSA_Token;
    private Admin admin;
    private boolean logged=false;
    
    public AdminSafe(HttpServletRequest request,Connection connection) throws SQLException, ClassNotFoundException
    {
        this.request=request;
        this.authSQL=new AuthenticatorSQLCommands(connection);
        isLoggedIn();
    }
    
    /**
     * checks to see if the client that has requested for the page is logged in with an admin account or not
     * it returns true if it has. it returns false if there is no admin with the mssa_token
     * it returns false if the admin hasn't logged in or he's account is banned
     * @return true if it has logged in and false it hasn't
     * @throws SQLException 
     */
    private void isLoggedIn() throws SQLException
    {
        HttpSession session=request.getSession(false);
        if(session==null)
        {
            this.logged=false;
            return;
        }
        if(session.getAttribute("MSSA_Token")==null)
        {
            this.logged=false;
            return;
        }
        
        this.MSSA_Token=session.getAttribute("MSSA_Token").toString();
        
        String userId=Methods.Encryptors.Decript1(this.MSSA_Token);
        if(!Methods.Methods.isNumber(userId))
        {
            this.logged=false;
            return;
        }
        
        try
        {
            this.admin=authSQL.getAdmin(Integer.valueOf(userId));
            
        } catch (DoesntExistException ex)
        {
            this.logged=false;
            return;
        }
        
        if(admin.getStatus()==-1)
        {
            this.logged=false;
            return;
        }
        
        this.logged=true;
        return;
    }
    
    /**
     * if the admin has logged in, it will return it's daata
     * @return the admin object that has logged in
     * @throws DoesntExistException if admin hasn't logged in or it's blocked
     */
    public Admin getAdmin() throws DoesntExistException
    {
        if(!logged)
        {
            throw new DoesntExistException("admin doesn't exsit");
        }
        
        return this.admin;
    }
    
     /**
     * 
     * @return  true if admin is logged in and false if its blocked or not logged in
     */
    public boolean getLogged()
    {
        return this.logged;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Authenticators.Admin;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Authenticators.BlockCheck;
import Objects.User.User;
import SQL.Commands.AuthenticatorSQLCommands;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

/**
 * this object is for logging in an Admin
 * @author Moses
 */
public class AdminLoginSafe {
    private AuthenticatorSQLCommands authSQL;
    private BlockCheck blockCheck;
    private HttpServletRequest request;
    private String ip;
    
    public AdminLoginSafe(HttpServletRequest request,Connection connection) throws ClassNotFoundException, SQLException, DoesntExistException
    {
        this.authSQL=new AuthenticatorSQLCommands(connection);
        this.request=request;
        this.ip=this.request.getRemoteAddr();
        this.blockCheck=this.authSQL.getBlockCheck(ip);
    }
    
    /**
     * checks to see if the ip is allowed to login or not
     * @return  true if it's blocked and false if it's not blocked
     */
    public boolean isLoginBlock()
    {
        
        if(System.currentTimeMillis()-blockCheck.getloginBlock()>180000)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * each time user puts wrong password, he's attempt will increase by 1 and if it reaches 3, he will be blocked for 3 minutes
     * @throws SQLException 
     */
    public void addLoginAttempt() throws SQLException
    {
        authSQL.addLoginAttempt(this.ip);
        if(blockCheck.getLoginAttempt()>=2)
        {
            blockLogin();
            resetLoginAttempt();
        }
    }
    
    /**
     * blocks user from loging in
     * @throws SQLException 
     */
    public void blockLogin() throws SQLException
    {
        authSQL.blockLogin(this.ip);
    }
    
    /**
     * resets user's attempts for loging in 
     * @throws SQLException 
     */
    public void resetLoginAttempt() throws SQLException
    {
        authSQL.resetLoginAttempt(this.ip);
    }
    
    /**
     * before using this function you must check if the user is blocked or not 
     * it will try to login and if there was a admin it will return it's object and if there wasn't, it will throw a doesntExistException
     * @param userName username of the client 
     * @param password password of the client
     * @return a Admin object including main datas of an Admin
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     * @throws DoesntExistException if there wasn't a user with this user name and password it will throw this exception
     */
    public Admin login(String userName,String password) throws NoSuchAlgorithmException, SQLException, DoesntExistException
    {
        String hashed=Methods.Encryptors.MD5Hasher(password);
        Admin admin=this.authSQL.getAdmin(userName, hashed);
        return admin;
        
    }
}

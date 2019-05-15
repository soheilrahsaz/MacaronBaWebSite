/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Authenticators.User;

import ExceptionsChi.DoesntExistException;
import Objects.Authenticators.BlockCheck;
import Objects.User.User;
import SQL.Commands.AuthenticatorSQLCommands;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

/**
 * This class is used when clients need to login or register. It can check their IPs to see whether they're ok or not.
 * If user puts wrong password 3 times in a row, he will be blocked for 3 minutes.
 * If user puts wrong password for 1-2 times then he puts the correct password, he's attempts will reset
 * @author Moses
 */
public class LoginSafe {
    private AuthenticatorSQLCommands authSQL;
    private HttpServletRequest request;
    private String ip;
    private BlockCheck blockCheck;
    private String MSS_Token;
    
    public LoginSafe(HttpServletRequest request,Connection connection) throws SQLException, ClassNotFoundException, DoesntExistException
    {
        this.authSQL=new AuthenticatorSQLCommands(connection);
        this.request=request;
        this.ip=request.getRemoteAddr();
        this.blockCheck=new BlockCheck(ip,connection);
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
     * it will try to login and if there was a user it will return it's object and if there wasn't, it will throw a doesntExistException
     * @param userName username of the client (in this case his phone number)
     * @param password password of the client
     * @return a User object including main datas of an user
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     * @throws DoesntExistException if there wasn't a user with this user name and password it will throw this exception
     */
    public User login(String userName,String password) throws NoSuchAlgorithmException, SQLException, DoesntExistException
    {
        String hashed=Methods.Encryptors.MD5Hasher(password);
        User user=authSQL.getUser(userName, hashed);
        return user;
        
    }
    
   
    
}

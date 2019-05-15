/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Authenticators.User;

import ExceptionsChi.DoesntExistException;
import Objects.Authenticators.BlockCheck;
import SQL.Commands.AuthenticatorSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

/**
 * this object is used when a client wants to register
 * @author Moses
 */
public class RegisterSafe {
    private HttpServletRequest request;
    private String ip;
    private AuthenticatorSQLCommands authSQL;
    private BlockCheck blockCheck;
    
    public RegisterSafe(HttpServletRequest request,Connection connection) throws ClassNotFoundException, SQLException, DoesntExistException
    {
        this.request=request;
        this.ip=request.getRemoteAddr();
        this.authSQL=new AuthenticatorSQLCommands(connection);
        this.blockCheck=new BlockCheck(ip,connection);
    }
    
    /**
     * adds and attempt to registerAttempt and if it reaches 5, the user gets blocked for 1 day!
     * @throws SQLException 
     */
    public void addRegisterAttempt() throws SQLException
    {
        this.authSQL.addRegisterAttempt(this.ip);
        if(blockCheck.getRegisterAttempt()>=4)
        {
            blockRegister();
            resetRegisterAttempts();
        }
    }
    
    /**
     * checks to see of the user is blocked from registering or not.returns true it he's blocked and returns false if he's not.blocked for 1 day
     * @return 
     */
    public boolean isRegisterBlocked()
    {
        if(System.currentTimeMillis()-blockCheck.getRegisterBlock()>8640000)
        {
            return false;
        }else
        {
            return true;
        }
        
    }
    
    /**
     * puts nows time milies to check later if the user is blocked from registering or not
     * @throws SQLException 
     */
    public void blockRegister() throws SQLException
    {
        this.authSQL.blockRegister(ip);
        
    }
    
    /**
     * resets registerAttempts by changing its attemtps to 0
     * @throws SQLException 
     */
    public void resetRegisterAttempts() throws SQLException
    {
        this.authSQL.resetRegisterAttempts(this.ip);
    }
    
    /**
     * sets register time to now, then later it could be checked to see how many time a day does someone 
     * @throws SQLException 
     */
    public void setRegisterTime() throws SQLException
    {
        this.authSQL.setRegisterTime(this.ip);
    }
    
}

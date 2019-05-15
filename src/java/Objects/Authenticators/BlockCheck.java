/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Authenticators;

import ExceptionsChi.DoesntExistException;
import SQL.Commands.AuthenticatorSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Moses
 */
public class BlockCheck {
    private String ip;
    private int loginAttempt;
    private int registerAttempt;
    private long loginBlock;
    private long registerBlock;
    private int status;
    
    /**
     * don't use this constructor directly unless you're creating it with another function
     */
    public BlockCheck()
    {
        
    }
    /**
     * you can also create this object with this constructor by passing client's IP 
     * @param ip 
     */
    public BlockCheck(String ip,Connection connection) throws ClassNotFoundException, SQLException, DoesntExistException
    {
        this.ip=ip;
        new AuthenticatorSQLCommands(connection).getBlockCheck(this);
    }

    public String getIp() {
        return ip;
    }

    public BlockCheck setIp(String ip) {
        this.ip = ip;
    return this;}

    public int getLoginAttempt() {
        return loginAttempt;
    }

    public BlockCheck setLoginAttempt(int loginAttempt) {
        this.loginAttempt = loginAttempt;
    return this;}

    public int getRegisterAttempt() {
        return registerAttempt;
    }

    public BlockCheck setRegisterAttempt(int registerAttempt) {
        this.registerAttempt = registerAttempt;
    return this;}

    public long getloginBlock() {
        return loginBlock;
    }

    public BlockCheck setloginBlock(long loginBlock) {
        this.loginBlock = loginBlock;
    return this;}

    public long getRegisterBlock() {
        return registerBlock;
    }

    public BlockCheck setRegisterBlock(long registerBlock) {
        this.registerBlock = registerBlock;
    return this;}

    public int getStatus() {
        return status;
    }

    public BlockCheck setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

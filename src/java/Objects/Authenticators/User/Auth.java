/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//database name: users.auth
package Objects.Authenticators.User;

import ExceptionsChi.DoesntExistException;
import SQL.Commands.AuthenticatorSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *    
 * This object is used when need to sign up a user and authenticate
 * create this object with authCreator
 * @author Moses
 */
public class Auth {
    private int userId;
    private int authCode;
    private int authAttempt;
    private int authRequest;
    private long authTime;
    
    private long authRequestBlock;
    private int status;
   
    
    /**
     * DO NOT create object with this constructor directly
     * only use it when you're going to create this object with another function
     */
    public Auth()
    {
        
    }
    /**
     * you can also create this object just by sending userId to it
     * @param userId
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws Exception 
     */
    public Auth(int userId,Connection connection) throws ClassNotFoundException, SQLException, DoesntExistException
    {
        this.userId=userId;
        new AuthenticatorSQLCommands(connection).getAuth(this);
    }
    
    public int getUserId() {
        return userId;
    }

    public Auth setUserId(int userId) {
        this.userId = userId;
    return this;}

    public int getAuthCode() {
        return authCode;
    }

    public Auth setAuthCode(int authCode) {
        this.authCode = authCode;
        return this;
    }

    public int getAuthAttempt() {
        return authAttempt;
    }

    public Auth setAuthAttempt(int authAttempt) {
        this.authAttempt = authAttempt;
    return this;}

    public int getAuthRequest() {
        return authRequest;
    }

    public Auth setAuthRequest(int authRequest) {
        this.authRequest = authRequest;
    return this;}

    public long getAuthTime() {
        return authTime;
    }

    public Auth setAuthTime(long authTime) {
        this.authTime = authTime;
    return this;}

    

    public long getAuthRequestBlock() {
        return authRequestBlock;
    }

    public Auth setAuthRequestBlock(long authRequestBlock) {
        this.authRequestBlock = authRequestBlock;
    return this;}

    public int getStatus() {
        return status;
    }

    public Auth setStatus(int status) {
        this.status = status;
    return this;}
    
    
    
}

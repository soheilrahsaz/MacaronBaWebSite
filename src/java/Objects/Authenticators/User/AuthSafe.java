/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Authenticators.User;

import ExceptionsChi.DoesntExistException;
import Objects.User.User;
import SQL.Commands.AuthenticatorSQLCommands;
import SmsPishgam.Methods.SmsSender;
import SmsPishgam.Objects.Sms;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Moses
 */
public class AuthSafe {
    private Connection connection;
    private AuthenticatorSQLCommands authSQL;
    private int userId;
    private Auth auth;
    private User user;
    public AuthSafe(int userId,Connection connection) throws ClassNotFoundException, SQLException, DoesntExistException
    {
        this.connection=connection;
        this.authSQL=new AuthenticatorSQLCommands(connection);
        this.userId=userId;
        this.auth=authSQL.getAuth(userId);
        this.user=this.authSQL.getUser(userId);
        
    }
    
    public AuthSafe(String userId,Connection connection) throws ClassNotFoundException, SQLException, DoesntExistException
    {
        this.connection=connection;
        this.authSQL=new AuthenticatorSQLCommands(connection);
        this.userId=Integer.valueOf(userId);
        this.auth=authSQL.getAuth(this.userId);
        this.user=this.authSQL.getUser(this.userId);

    }
    
    /**
     * adding an attempt each time user guess the code wrong and if it reaches 5 times, his code will invalidate and the user has to 
     * request for the code again
     * @throws SQLException 
     */
    public void addAuthAttempt() throws SQLException
    {
        this.authSQL.addAuthAttempt(this.userId);
        if(auth.getAuthAttempt()>=4)
        {
            invalidateCode();
        }
    }
    
    /**
     * adding an attempt each time user requests for a code and if it reaches 5, the user will get blocked for 6 hours
     * @throws SQLException 
     */
    public void addRequestAttempt() throws SQLException
    {
        this.authSQL.addAuthRequestAttempt(this.userId);
        if(auth.getAuthRequest()>=4)
        {
            blockRequest();
        }
    }
    
    /**
     * puts now's time millis to check later if it has passed 6 hours or not.
     * @throws SQLException 
     */
    public void blockRequest() throws SQLException
    {
        this.authSQL.blockAuthRequest(this.userId);
        resetRequestAttempt();
    }
    
    /**
     * changes the status of the auth to 2
     * @throws SQLException 
     */
    private void invalidateCode() throws SQLException
    {
        this.authSQL.invalidateAuthCode(this.userId);
    }
    
    
    
    /**
     * it will generate a 5 digit code and adds it to database with nows time milies to check later if 
     * authentication is happening in time or not. and it will return the generated code .
     * @return 5 digit generated code 
     * @throws SQLException 
     */
    public int generateCode() throws SQLException
    {
        int authCode=Math.abs(Methods.Methods.getRandomInt(89999)+10000);
        sendAuthCode(authCode);
        this.auth.setAuthTime(System.currentTimeMillis());
        
        this.authSQL.setAuthCode(authCode,this.userId);
        addRequestAttempt();
        resetAuthAttempt();
        
        return authCode;
    }
    
    /**
     * sends the auth code to phone number
     * @param authCode 
     */
    private void sendAuthCode(int authCode)
    {
        Sms sms=new Sms();
        sms.setText("کد احراز هویت شما :\n"+authCode)
                .setUserId(userId)
                .setReciever(this.user.getPhoneNumber());
        SmsSender sender=new SmsSender(connection);
        sender.sendSms(sms);
        
    }
    /**
     * checks to see if the client is blocked from requesting by comparing the time of blocked and now's time to see if it has passed 
     * 6 hours or not
     * @return true if user is blocked and false if the user isn't blocked
     */
    public boolean isAuthRequestBlock()
    {
        if(System.currentTimeMillis()-this.auth.getAuthRequestBlock()>21600000)
        {
            return false;
        }else
        {
            return true;
            
        }
    }
    
    /**
     * resets request attempts by changing it's request attempts to 0
     * @throws SQLException 
     */
    public void resetRequestAttempt() throws SQLException
    {
        this.authSQL.resetAuthRequestAttempt(this.userId);
    }
    
    /**
     * resets authAttempts by changing it to 0. Used when user requests for a new code.
     * @throws SQLException 
     */
    public void resetAuthAttempt() throws SQLException 
    {
        this.authSQL.resetAuthAttempt(this.userId);
    }
    
    /**
     * checks if user is verifying in time or not. If it had passed mor than 2 minutes it will return false.
     * @return true if its under 2 minutes and false if it is more than 2 minutes
     */
    public boolean isInTime()
    {
        if(System.currentTimeMillis()-this.auth.getAuthTime()>600000)
        {
            return false;
        }else
        {
            return true;
        }
    }
    
    /**
     * compares the code sent by the user with code in the database and if they are equal it will changes the status of the user to 20 
     * and returns true.If they did not match, it will add an authAttempt and return false.
     * @param authCode the code sent by the user
     * @return true if codes matches and false if it doesn't
     * @throws SQLException 
     */
    public boolean verifyAuthCode(String authCode) throws SQLException
    {
        if(authCode.equals(String.valueOf(auth.getAuthCode())))
        {
            this.authSQL.authoriseUser(this.userId);
            return true;
        }else
        {
            addAuthAttempt();
            return false;
        }
    }
    
    /**
     * after putting wrong code 5 times, your code gets invalidated, this function checks to see if the code is still valid or not
     * @return 
     */
    public boolean isValid()
    {
        if(this.auth.getStatus()==2)
        {
            return false;
        }else
        {
            return true;
        }
    }
    
    public Auth getAuth()
    {
        return this.auth;
    }
}

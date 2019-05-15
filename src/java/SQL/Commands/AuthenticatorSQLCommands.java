/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import Objects.Authenticators.User.Auth;
import Objects.Authenticators.BlockCheck;
import ExceptionsChi.DoesntExistException;
import Objects.Admin.Access;
import Objects.Admin.Admin;
import Objects.User.User;
import SQL.Driver.SQLDriverMacaronBa;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Moses
 */
public class AuthenticatorSQLCommands {
    private Connection connection;
    
    public AuthenticatorSQLCommands(Connection connection)
    {
        this.connection=connection;
    }
    
    
    /**
     * checks if the user ip address is banned from the site or not.returns true if yes and false if not
     * @param ip
     * @return true if ip address is banned, false if it's not banned
     * @throws SQLException 
     */
    public boolean isBanned(String ip) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `status` FROM blockcheck WHERE ip=?");
        prep.setString(1, ip);
        ResultSet res=prep.executeQuery();
        res.last();
        if(res.getRow()==0)
        {
            addIp(ip);
            return false;
        }

        res.first();
        if(res.getInt("status")==2)
        {
            return true;
        }else
        {
            return false;
        }
        
    }
    
    public void addIp(String ip) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO blockcheck (ip) VALUES (?)");
        prep.setString(1, ip);
        prep.executeUpdate();
    }
    
    /**
     * creates an Auth object with userId of the client
     * @param userId the userId of the client
     * @return auth object
     * @throws SQLException
     * @throws DoesntExistException throws this exception when there's no user with that userId 
     */
    public Auth getAuth(int userId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM users_auth WHERE `userId`=?");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        res.last();
        if(res.getRow()==0)
        {
            createAuth(userId);
            Auth auth=new Auth();
            auth.setUserId(userId);
            return auth;
        }
        res.first();
        Auth auth=new Auth();
        auth.setUserId(userId).setAuthAttempt(res.getInt("authAttempt")).setAuthCode(res.getInt("authCode"))
                .setAuthRequest(res.getInt("authRequest")).setStatus(res.getInt("status"))
                .setAuthRequestBlock(res.getLong("authRequestBlock"))
                .setAuthTime(res.getLong("authTime"));
        return auth;
    }
    
    public void createAuth(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO users_auth (`userId`) VALUES (?)");
        prep.setInt(1, userId);
        prep.executeUpdate();
    }
    /**
     * creates an Auth object with userId of the client
     * @param auth the auth object which is beieng created and already has userId in it
     * @throws SQLException
     * @throws DoesntExistException throws this exception when there's no user with that userId
     */
    public void getAuth(Auth auth) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM users_auth WHERE `userId`=?");
        prep.setInt(1, auth.getUserId());
        ResultSet res=prep.executeQuery();
        res.last();
        if(res.getRow()==0)
        {
            createAuth(auth.getUserId());
            return;
        }
        res.first();
        auth.setAuthAttempt(res.getInt("authAttempt")).setAuthCode(res.getInt("authCode"))
                .setAuthRequest(res.getInt("authRequest")).setStatus(res.getInt("status"))
                .setAuthRequestBlock(res.getLong("authRequestBlock"))
                .setAuthTime(res.getLong("authTime"));
    }
    
    /**
     * creates a BlockCheck object and obtains data with ip address of the user
     * @param ip ip address of client
     * @return the blockcheck obejct
     * @throws SQLException
     * @throws DoesntExistException throws this when ip address doesn't exist in the table
     */
    public BlockCheck getBlockCheck(String ip) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM blockcheck WHERE ip=?");
        prep.setString(1, ip);
        ResultSet res=prep.executeQuery();
        res.last();
        if(res.getRow()==0)
        {
            throw new DoesntExistException("NEW ip");
        }
        res.first();
        BlockCheck blockCheck=new BlockCheck();
        blockCheck.setIp(res.getString("ip")).setLoginAttempt(res.getInt("loginAttempt"))
                .setRegisterAttempt(res.getInt("registerAttempt")).setloginBlock(res.getLong("loginBlock"))
                .setRegisterBlock(res.getLong("registerBlock"));
        return blockCheck;
    }
    
    /**
     * creates a BlockCheck object and obtains data with ip address of the user
     * @param blockCheck the blockcheck object that is being created which already has ip address in it
     * @throws SQLException
     * @throws DoesntExistException throws this when ip address doesn't exist in the table
     */
    public void getBlockCheck(BlockCheck blockCheck) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM blockcheck WHERE ip=?");
        prep.setString(1, blockCheck.getIp());
        ResultSet res=prep.executeQuery();
        res.last();
        if(res.getRow()==0)
        {
            throw new DoesntExistException("NEW ip");
        }
        res.first();
        blockCheck.setLoginAttempt(res.getInt("loginAttempt"))
                .setRegisterAttempt(res.getInt("registerAttempt")).setloginBlock(res.getLong("loginBlock"))
                .setRegisterBlock(res.getLong("registerBlock"));
    }
    
    /**
     * add's an attempt to blockcheck table whenever a user puts a wrong password
     * Used in loginsafe
     * @param ip
     * @throws SQLException 
     */
    public void addLoginAttempt(String ip) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE blockcheck SET `loginAttempt`=`loginAttempt`+1 WHERE `ip`=?");
        prep.setString(1, ip);
        prep.executeUpdate();
        
    }
    
    /**
     * puts now's time millis in blocklogin field to check to see whether the user is blocked or not.
     * Used in loginsafe
     * @param ip
     * @throws SQLException 
     */
    public void blockLogin(String ip) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE blockcheck SET `loginBlock`=? WHERE `ip`=?");
        prep.setLong(1, System.currentTimeMillis());
        prep.setString(2, ip);
        prep.executeUpdate();
    }
    
    /**
     * resets login attempts
     * Used in loginSafe
     * @param ip ip address of the client
     * @throws SQLException 
     */
    public void resetLoginAttempt(String ip) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE blockcheck SET `loginAttempt`=0 WHERE ip=?");
        prep.setString(1, ip);
        prep.executeUpdate();
    }
    
    /**
     * trys to find a user with username and password and if found returns the user object with it's userId
     * @param username username of the client which is trying to login
     * @param password password of the user
     * @return returns User object 
     * @throws DoesntExistException if the user doesn't exist, it throws this exceptions
     */
    public User getUser(String username,String password) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `phoneNumber`,`status`,`password`,`userId` FROM users WHERE `phoneNumber`=?");
        prep.setString(1, username);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            throw new DoesntExistException("user doesn't exist.");
        }
        
        
        if(!res.getString("password").equals(password))
        {
            throw new DoesntExistException("user doesn't exist.");
        }
        
        User user=new User();
        user.setPhoneNumber(res.getString("phoneNumber")).setStatus(res.getInt("status")).setUserId(res.getInt("userId"));
        
        return user;
    }
    
    /**
     * trys to find a user with userId and if found returns the user object 
     * @param userId the userId of the client
     * @throws DoesntExistException if the user doesn't exist, it throws this exceptions
     */
    public User getUser(int userId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `phoneNumber`,`status`,`email` FROM users WHERE `userId`=?");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        res.last();
        if(res.getRow()==0)
        {
            throw new DoesntExistException("user doesn't exist.");
        }
        
        res.first();
        User user=new User();
        user.setPhoneNumber(res.getString("phoneNumber")).setEmail(res.getString("email")).setStatus(res.getInt("status")).setUserId(userId);
        
        return user;
    }
    
    /**
     * creates the user object with it's userId (which is already in the user objcet)
     * @param user the objcet that needs to be created
     * @throws SQLException
     * @throws DoesntExistException if the user doesn't exist, it throws this exceptions
     */
     public void getUser(User user) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `phoneNumber`,`status` FROM users WHERE `userId`=?");
        prep.setInt(1, user.getUserId());
        ResultSet res=prep.executeQuery();
        res.last();
        if(res.getRow()==0)
        {
            throw new DoesntExistException("user doesn't exist.");
        }
        
        res.first();
        user.setPhoneNumber(res.getString("phoneNumber")).setStatus(res.getInt("status"));
    }
    
     /**
     * creates the user object with all of it's data with it's userId 
     * @param userId user id of the user
     * @throws SQLException
     * @throws DoesntExistException if the user doesn't exist, it throws this exceptions
     * @return an user object with full data in it
     */
    public User getUserFull(int userId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM users WHERE `userId`=?");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            throw new DoesntExistException("No User With This UserId");
        }
        User user=new User();
        user.setBirthDate(res.getTimestamp("birthDate")).setDateRegistered(res.getTimestamp("dateRegistered"))
                .setEmail(res.getString("email")).setFirstName(res.getString("firstName")).setLastName(res.getString("lastName"))
                .setPhoneNumber(res.getString("phoneNumber")).setStatus(res.getInt("status"));
        return user;
    }
     /**
      * adds an attempt to registerAttempts
      * @param ip the ip address of the client
      * @throws SQLException 
      */
     public void addRegisterAttempt(String ip) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE blockcheck SET `registerAttempt`=`registerAttempt`+1 WHERE `ip`=?");
         prep.setString(1, ip);
         prep.executeUpdate();
     }
     
     /**
      * puts nows time milies in blockregister field to check later if the user is allowed to register or not
      * @param ip ip address of the client
      * @throws SQLException 
      */
     public void blockRegister(String ip) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE blockcheck SET `registerBlock`=? WHERE `ip`=?");
         prep.setLong(1, System.currentTimeMillis());
         prep.setString(2, ip);
         prep.executeUpdate();
     }
     
     /**
      * changing registerAttempts from blockcheck table to 0
      * @param ip the ip address of the client
      * @throws SQLException 
      */
     public void resetRegisterAttempts(String ip) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE blockcheck SET `registerAttempt`=0 WHERE `ip`=?");
         prep.setString(1, ip);
         prep.executeUpdate();
     }
     
     public void setRegisterTime(String ip) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE blockcheck SET registerTime=? WHERE `ip`=?");
         prep.setLong(1, System.currentTimeMillis());
         prep.setString(2, ip);
         prep.executeUpdate();
         
     }
     /**
      * checks if the username(or in this case,phonenumber) exists or not.returns true if it exists and false if it doesn't
      * @param username
      * @return true if exists, false if doesn't exist
      */
     public boolean usernameExists(String username) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("SELECT `id` from users WHERE `phoneNumber`=?");
         prep.setString(1, username);
         ResultSet res=prep.executeQuery();
         if(res.first())
         {
             return true;
         }else
         {
             return false;
         }
     }
     
     /**
      * checks if userId exists or not.returns true if it does, and returns false if doesn't.
      * @param userId
      * @return true if userId exists, false if it doesn't
      * @throws SQLException 
      */
     public boolean userIdExists(int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM users WHERE `userId`=?");
         prep.setInt(1, userId);
         ResultSet res=prep.executeQuery();
         if(res.first())
         {
             return true;
         }
         return false;
     }

     /**
      * registers a user by adding it's phonenumber and his md5 hash password and it's userId
      * @param userName username of the client ( in this case it is his phoneNumber)
      * @param password the password of the user
      * @throws SQLException 
      * @return the user id of the user created
      */
     public int regiserUser(String userName,String password) throws SQLException, NoSuchAlgorithmException
     {
         String hashedpass=Methods.Encryptors.MD5Hasher(password);
         int userId;
         while(true)
         {
             userId=Math.abs(Methods.Methods.getRandomInt());
             if(!userIdExists(userId))
                 break;
         }
         
         PreparedStatement prep=this.connection.prepareStatement("INSERT INTO users (`phoneNumber`,`password`,`userId`) VALUES (?,?,?)");
         prep.setString(1, userName);
         prep.setString(2, hashedpass);
         prep.setInt(3, userId);
         prep.executeUpdate();
         
         return userId;
     }
     
     /**
      * adds an attempt in users_auth table each time user guesses the code wrong
      * @param userId userId of the client
      * @throws SQLException 
      */
     public void addAuthAttempt(int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE users_auth SET `authAttempt`=`authAttempt`+1 WHERE `userId`=?");
         prep.setInt(1, userId);
         prep.executeUpdate();
     }
     
     /**
      * adds anattempt in users_auth table , field authRequest each time user requests for a code
      * @param userId userId of the client
      * @throws SQLException 
      */
     public void addAuthRequestAttempt(int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE users_auth SET `authRequest`=`authRequest`+1 WHERE `userId`=?");
         prep.setInt(1, userId);
         prep.executeUpdate();
         
     }
     
     /**
      * puts now's time millis in users_auth table authRequestBlock field, in order to check later to see if the user is blocked or not
      * @param userId the userId of the client
      * @throws SQLException 
      */
     public void blockAuthRequest(int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE users_auth SET `authRequestBlock`=? WHERE `userId`=?");
         prep.setLong(1, System.currentTimeMillis());
         prep.setInt(2, userId);
         prep.executeUpdate();
     }
     
     /**
      * changes the status of the users_auth table row to 2
      * @param userId the userId of the client
      * @throws SQLException 
      */
     public void invalidateAuthCode(int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE users_auth SET `status`=2 WHERE `userId`=?");
         prep.setInt(1, userId);
         prep.executeUpdate();
     }
     
     /**
      * puts the generated code in the authCode and puts now's time milies in order to check later if it has validated in the right time or not
      * @param authCode the genereated code
      * @param userId the userId of the client
      * @throws SQLException 
      */
     public void setAuthCode(int authCode,int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE users_auth SET `authCode`=?,`authTime`=?,`status`=1 WHERE `userId`=?");
         prep.setInt(1, authCode);
         prep.setLong(2, System.currentTimeMillis());
         prep.setInt(3, userId);
         prep.executeUpdate();
     }
     
     /**
      * changes the authRequest attempts to 0 
      * @param userId the userId of the client
      * @throws SQLException 
      */
     public void resetAuthRequestAttempt(int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE users_auth SET `authRequest`=0 WHERE `userId`=?");
         prep.setInt(1, userId);
         prep.executeUpdate();
     }
     
     /**
      * changes the authAttempt to 0
      * @param userId the userId of the client
      * @throws SQLException 
      */
     public void resetAuthAttempt(int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE users_auth SET `authAttempt`=0 WHERE `userId`=?");
         prep.setInt(1, userId);
         prep.executeUpdate();
     }
     
     /**
      * changes the status of the user to 20
      * @param userId the userId of the client
      * @throws SQLException 
      */
     public void authoriseUser(int userId) throws SQLException
     {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE users SET `status`=20 WHERE `userId`=?");
         prep.setInt(1, userId);
         prep.executeUpdate();
     }
     
     /**
      * returns an admin object with it's username and password,
      * this function is used when logging in. it returns an admin if login is successful and throws an exception if there 
      * is no admin with this user name and password
      * @param userName
      * @param password
      * @return an admin object with admin datas in it
      * @throws SQLException
      * @throws DoesntExistException throws this exception when there was no admin with this username and password
      */
     public Admin getAdmin(String userName,String password) throws SQLException, DoesntExistException
     {
         PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM admin WHERE `userName`=?");
         prep.setString(1, userName);
         ResultSet res=prep.executeQuery();
         
         if(!res.first())
         {
             throw new DoesntExistException("Admin not found With This Username and Passowrd");
         }
         
         if(!res.getString("password").equals(password))
         {
             throw new DoesntExistException("Admin not found With This Username and Passowrd");
         }
         
         Admin admin=new Admin();
         admin.setUserName(userName).setStatus(res.getInt("status")).setAdminId(res.getInt("adminId"))
                 .setAccess(getAccess(res.getInt("accessId")));
         return admin;
     }
     
     /**
      * returns an admin object with it's adminId 
      * throws an exception if there is no admin with this user name and password
      * @param adminId
      * @return an admin object with admin datas in it
      * @throws SQLException
      * @throws DoesntExistException throws this exception when there was no admin with this adminId
      */
     public Admin getAdmin(int adminId) throws SQLException, DoesntExistException
     {
         PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM admin WHERE `adminId`=?");
         prep.setInt(1, adminId);
         ResultSet res=prep.executeQuery();
         
         if(!res.first())
         {
             throw new DoesntExistException("No Admin With This Username and Passowrd");
         }
         
         Admin admin=new Admin();
         admin.setUserName(res.getString("userName")).setStatus(res.getInt("status")).setAdminId(res.getInt("adminId"))
                 .setAccess(getAccess(res.getInt("accessId")));
         return admin;
     }
     
     /**
      * returns an access object containing access levels to different parts of the webSite
      * @param accessId
      * @return an access object
      * @throws SQLException 
      */
     public Access getAccess(int accessId) throws SQLException
     {
         
         PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM admin_access WHERE `accessId`=?");
         prep.setInt(1, accessId);
         ResultSet res=prep.executeQuery();
         res.first();
         Access access=new Access();
         access.setAccessId(accessId).setName(res.getString("name")).setAccesses(res.getString("accesses"))
                 .setMaster(res.getInt("master"));
         return access;
     }
    
}

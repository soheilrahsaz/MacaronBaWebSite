/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import Objects.Admin.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Moses
 */
public class AdminSQLCommands {
    private Connection connection; 
    
    public AdminSQLCommands(Connection connection)
    {
        this.connection=connection;
    }
    
    /**
     * shows if the admin id already exists or not 
     * @param adminId
     * @return true if exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean adminIdExists(int adminId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `admin` WHERE `adminId`=?");
        prep.setInt(1, adminId);
        return prep.executeQuery().first();
    }
    
    /**
     * shows if the access id already exists or not 
     * @param accessId
     * @return true if exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean accessIdExists(int accessId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `admin_access` WHERE `accessId`=?");
        prep.setInt(1, accessId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds an admin to data base
     * @param userName
     * @param password MD5 hashed password
     * @param accessId the id of the added access of this admin
     * @return the id of the added admin
     * @throws SQLException 
     */
    public int addAdmin(String userName,String password,int accessId) throws SQLException
    {
        int adminId;
        Random random=new Random();
        while(true)
        {
            adminId=Math.abs(random.nextInt());
            if(!adminIdExists(adminId)) 
                break;
        } 
            
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `admin` (`adminId`,`userName`,`password`,`accessId`) VALUES (?,?,?,?)");
        prep.setInt(1, adminId);
        prep.setString(2, userName);
        prep.setString(3, password);
        prep.setInt(4, accessId);
        prep.execute();
        return adminId;
    }
    
    /**
     * adds an access to data base 
     * @param accesses the name of the accesses which this admin is going to have
     * @return the id of the added access
     * @throws SQLException 
     */
    public int addAccess(LinkedList<String> accesses) throws SQLException
    {
        int accessId;
        Random random=new Random();
        while(true)
        {
            accessId=Math.abs(random.nextInt());
            if(!accessIdExists(accessId)) 
                break;
        }
        String access="";
        for(String str:accesses)
        {
            access+=str+";";
        }
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `admin_access` (`accessId`,`accesses`) VALUES (?,?)");
        prep.setInt(1, accessId);
        prep.setString(2, access);
        prep.execute();
        return accessId;
    }
    
    /**
     * retrieves all admins 
     * @return a linked list of admin
     * @throws SQLException 
     */
    public LinkedList<Admin> getAdmins() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `admin` WHERE `status`>-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Admin> admins=new LinkedList<Admin>();
        while(res.next())
        {
            Admin admin=new Admin();
            admin.setAdminId(res.getInt("adminId")).setDateAdded(res.getTimestamp("dateAdded"))
                    .setStatus(res.getInt("status")).setUserName(res.getString("userName"));
            admins.add(admin);
        }
        return admins;
    }
        
    /**
     * changes the access of an admin
     * @param accessId
     * @param accesses
     * @throws SQLException 
     */
    public void editAccess(int accessId,LinkedList<String> accesses) throws SQLException
    {
        String access="";
        for(String str:accesses)
        {
            access+=str+";";
        }
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `admin_access` SET `accesses`=? WHERE `accessId`=?");
        prep.setString(1, access);
        prep.setInt(2, accessId);
        prep.execute();
    }
    
    /**
     * changes the status of an admin to -1 so that he couldn't login again
     * @param adminId
     * @throws SQLException 
     */
    public void deleteAdmin(int adminId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `admin` SET `status`=-1 WHERE `adminId`=?");
        prep.setInt(1, adminId);
        prep.execute();
    }
    
    /**
     * edits an admin's password
     * @param adminId
     * @param password
     * @throws SQLException 
     */
    public void editAdminPassword(int adminId,String password) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `admin` SET `password`=? WHERE `adminId`=?");
        prep.setString(1, password);
        prep.setInt(2, adminId);
        prep.execute();
    }
}

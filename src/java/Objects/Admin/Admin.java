/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Admin;

import java.sql.Timestamp;

/**
 *
 * @author Moses
 */
public class Admin {
    private int adminId;
    private String userName;
    private Access access;
    private int status;
    private Timestamp dateAdded;
    
    /**
     * shows if the admin has access to a section or not
     * @param section the name of the section has to be exact
     * @return 
     */
    public boolean haveAccessTo(String section)
    {
        if(access.isMaster())
        {
            return true;
        }
        
        if(access.getAccesses().contains(section))
        {
            return true;
        }
        
        return false;
    }

    public Timestamp getDateAdded() {
        return dateAdded;
    }

    public Admin setDateAdded(Timestamp dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }
    
    

    public int getAdminId() {
        return adminId;
    }

    public Admin setAdminId(int adminId) {
        this.adminId = adminId;
    return this;}

    public String getUserName() {
        return userName;
    }

    public Admin setUserName(String userName) {
        this.userName = userName;
    return this;}

    public Access getAccess() {
        return access;
    }

    public Admin setAccess(Access access) {
        this.access = access;
    return this;}

    public int getStatus() {
        return status;
    }

    public Admin setStatus(int status) {
        this.status = status;
    return this;}
    
    
    
}

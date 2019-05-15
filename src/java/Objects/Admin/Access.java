/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Admin;

import java.util.ArrayList;

/**
 * used to see admin's access for different actions 
 * it's still not complete and has a lot of stuff to be added later
 * @author Moses
 */
public class Access {
    private int accessId;
    private String name;
    private boolean master;
    private ArrayList<String> accesses;

    
    
    public ArrayList<String> getAccesses() {
        return accesses;
    }

    public Access setAccesses(ArrayList<String> accesses) {
        this.accesses = accesses;
        return this;
    }
    
    public Access setAccesses(String accesses)
    {
        String access[]=accesses.split(";");
        this.accesses=new ArrayList<String>();
        for(String str:access)
        {
            this.accesses.add(str);
        }
        return this;
    }
    

    public String getName() {
        return name;
        
    }

    public Access setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isMaster() {
        return master;
    }

    public Access setMaster(int master) {
        this.master = (master==1);
        return this;
    }

    public int getAccessId() {
        return accessId;
    }

    public Access setAccessId(int accessId) {
        this.accessId = accessId;
        return this;
    }
    
}

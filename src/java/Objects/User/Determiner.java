/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.User;

import java.util.HashMap;

/**
 * used to determine where user should go and what it should do based on it's status and other things
 * @author Moses
 */
public class Determiner {
    private User user;
    private HashMap<Integer,String> paths;
    
    public Determiner(User user)
    {
        this.user=user;
        paths();
    }
    
    public String pathDeterminer()
    {
        return this.paths.get(user.getStatus());
    }
    
    private void paths()
    {
        this.paths=new HashMap<Integer, String>();
        this.paths.put(10, "authenticate.jsp");
        this.paths.put(20, "completeSpecification.jsp");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.InPerson;

import Objects.User.User;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class InPerson {
    private int inpersonId;
    private User user;
    private Timestamp date;
    private LinkedList<InPersonItem> items;
    private int status;

    public User getUser() {
        return user;
    }

    public InPerson setUser(User user) {
        this.user = user;
        return this;
    }

    
    
    public LinkedList<InPersonItem> getItems() {
        return items;
    }

    public InPerson setItems(LinkedList<InPersonItem> items) {
        this.items = items;
        return this;
    }

    
    
    public int getInpersonId() {
        return inpersonId;
    }

    public InPerson setInpersonId(int inpersonId) {
        this.inpersonId = inpersonId;
    return this;}

    

    public Timestamp getDate() {
        return date;
    }

    public InPerson setDate(Timestamp date) {
        this.date = date;
    return this;}

    public int getStatus() {
        return status;
    }

    public InPerson setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

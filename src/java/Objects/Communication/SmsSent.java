/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Communication;

import Objects.User.User;
import java.sql.Timestamp;

/**
 *
 * @author Moses
 */
public class SmsSent {
    private int id;
    private User user;
    private Timestamp date;
    private String text;

    public int getId() {
        return id;
    }

    public SmsSent setId(int id) {
        this.id = id;
        
    return this;}

    public User getUser() {
        return user;
    }

    public SmsSent setUser(User user) {
        this.user = user;
    return this;}

    public Timestamp getDate() {
        return date;
    }

    public SmsSent setDate(Timestamp date) {
        this.date = date;
    return this;}

    public String getText() {
        return text;
    }

    public SmsSent setText(String text) {
        this.text = text;
    return this;}
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Chat;

import java.sql.Timestamp;

/**
 *
 * @author Moses
 */
public class Chat {
    private int id;
    private int userId;
    private Timestamp date;
    private String text;
    private int status;
    private int sender;
    private int adminStatus;
    private String name;

    public String getName() {
        return name;
    }

    public Chat setName(String name) {
        this.name = name;
        return this;
    }
    
    
    public int getAdminStatus() {
        return adminStatus;
    }

    public Chat setAdminStatus(int adminStatus) {
        this.adminStatus = adminStatus;
        return this;
    }
    
    

    public int getId() {
        return id;
    }

    public Chat setId(int id) {
        this.id = id;
        
    return this;}

    public int getUserId() {
        return userId;
    }

    public Chat setUserId(int userId) {
        this.userId = userId;
    return this;}

    public Timestamp getDate() {
        return date;
    }

    public Chat setDate(Timestamp date) {
        this.date = date;
    return this;}

    public String getText() {
        return text;
    }

    public Chat setText(String text) {
        this.text = text;
    return this;}

    public int getStatus() {
        return status;
    }

    public Chat setStatus(int status) {
        this.status = status;
    return this;}

    public int getSender() {
        return sender;
    }

    public Chat setSender(int sender) {
        this.sender = sender;
    return this;}
    
    
}

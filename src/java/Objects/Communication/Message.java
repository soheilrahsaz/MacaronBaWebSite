/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Communication;

import Objects.User.User;

/**
 *
 * @author Moses
 */
public class Message {
    private int messsageId;
    private boolean isUser;
    private String name;
    private String phoneNumber;
    private User user;
    private String text;
    private int status;

    public int getMesssageId() {
        return messsageId;
    }

    public Message setMesssageId(int messsageId) {
        this.messsageId = messsageId;
        
    return this;}

    public boolean isIsUser() {
        return isUser;
    }

    public Message setIsUser(boolean isUser) {
        this.isUser = isUser;
    return this;}

    public String getName() {
        return name;
    }

    public Message setName(String name) {
        this.name = name;
    return this;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Message setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    return this;}

    public User getUser() {
        return user;
    }

    public Message setUser(User user) {
        this.user = user;
    return this;}

    public String getText() {
        return text;
    }

    public Message setText(String text) {
        this.text = text;
    return this;}

    public int getStatus() {
        return status;
    }

    public Message setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

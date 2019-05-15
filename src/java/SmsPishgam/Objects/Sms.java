/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SmsPishgam.Objects;

/**
 *
 * @author Moses
 */
public class Sms {
    private int userId;
    private String text;
    private String reciever;//98xxxxxxxxxx

    public int getUserId() {
        return userId;
    }

    public Sms setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    
    
    public String getText() {
        return text;
    }

    public Sms setText(String text) {
        this.text = text;
        return this;
    }

    public String getReciever() {
        return reciever;
    }

    public Sms setReciever(String reciever) {
        this.reciever = reciever;
        return this;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Order;

import Objects.Payment.Payment;
import Objects.Ship.Ship;
import Objects.User.DiscountTicket;
import Objects.User.User;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Receipt {
    private int oderId;
    private User user;
    private Timestamp date;
    private Payment payment;
    private Ship ship;
    private int status;
    private LinkedList<Part> parts;
    private DiscountTicket discount;

    public DiscountTicket getDiscount() {
        return discount;
    }

    public Receipt setDiscount(DiscountTicket discount) {
        this.discount = discount;
        return this;
    }
    
    

    public LinkedList<Part> getParts() {
        return parts;
    }

    public Receipt setParts(LinkedList<Part> parts) {
        this.parts = parts;
        return this;
    }
    
    

    public int getOderId() {
        return oderId;
    }

    public Receipt setOderId(int oderId) {
        this.oderId = oderId;
        
    return this;}

    

    public Timestamp getDate() {
        return date;
    }

    public Receipt setDate(Timestamp date) {
        this.date = date;
    return this;}

    public User getUser() {
        return user;
    }

    public Receipt setUser(User user) {
        this.user = user;
        return this;
    }

    public Payment getPayment() {
        return payment;
    }

    public Receipt setPayment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public Ship getShip() {
        return ship;
    }

    public Receipt setShip(Ship ship) {
        this.ship = ship;
        return this;
    }

 


    public int getStatus() {
        return status;
    }

    public Receipt setStatus(int status) {
        this.status = status;
    return this;}
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Partner;

import Objects.Payment.Payment;
import Objects.Ship.Ship;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class PartnerReceipt {
    private int orderId;
    private Partner partner;
    private Timestamp date;
    private Payment payment;
    private Ship ship;
    private String ip;
    private int status;
    private LinkedList<PartnerPart> parts;

    public LinkedList<PartnerPart> getParts() {
        return parts;
    }

    public PartnerReceipt setParts(LinkedList<PartnerPart> parts) {
        this.parts = parts;
        return this;
    }
    
    

    public int getOrderId() {
        return orderId;
    }

    public PartnerReceipt setOrderId(int orderId) {
        this.orderId = orderId;
    return this;}

    public Partner getPartner() {
        return partner;
    }

    public PartnerReceipt setPartner(Partner partner) {
        this.partner = partner;
    return this;}

    public Timestamp getDate() {
        return date;
    }

    public PartnerReceipt setDate(Timestamp date) {
        this.date = date;
    return this;}

    public Payment getPayment() {
        return payment;
    }

    public PartnerReceipt setPayment(Payment payment) {
        this.payment = payment;
    return this;}

    public Ship getShip() {
        return ship;
    }

    public PartnerReceipt setShip(Ship ship) {
        this.ship = ship;
    return this;}

    public String getIp() {
        return ip;
    }

    public PartnerReceipt setIp(String ip) {
        this.ip = ip;
    return this;}

    public int getStatus() {
        return status;
    }

    public PartnerReceipt setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

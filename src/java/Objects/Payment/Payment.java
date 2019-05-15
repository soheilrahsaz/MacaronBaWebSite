/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Payment;

import java.sql.Timestamp;

/**
 *
 * @author Moses
 */
public class Payment {
    private int paymentId;
    private int price;
    private PaymentType paymentType;
    private String approve;
    private Timestamp date;
    private int status;

    public Timestamp getDate() {
        return date;
    }

    public Payment setDate(Timestamp date) {
        this.date = date;
        return this;
    }
    
    public int getPaymentId() {
        return paymentId;
    }

    public Payment setPaymentId(int paymentId) {
        this.paymentId = paymentId;
        
    return this;}

    public int getPrice() {
        return price;
    }

    public Payment setPrice(int price) {
        this.price = price;
    return this;}

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public Payment setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    return this;}

    public String getApprove() {
        return approve;
    }

    public Payment setApprove(String approve) {
        this.approve = approve;
    return this;}

    public int getStatus() {
        return status;
    }

    public Payment setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Payment;

/**
 *
 * @author Moses
 */
public class PaymentType {
    private int paymentTypeId;
    private String name;
    private String description;
    private String url;
    private int status;
    
    

    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public PaymentType setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
        
    return this;}

    public String getName() {
        return name;
    }

    public PaymentType setName(String name) {
        this.name = name;
    return this;}

    public String getDescription() {
        return description;
    }

    public PaymentType setDescription(String description) {
        this.description = description;
    return this;}

    public String getUrl() {
        return url;
    }

    public PaymentType setUrl(String url) {
        this.url = url;
    return this;}

    public int getStatus() {
        return status;
    }

    public PaymentType setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Partner;

import java.sql.Timestamp;

/**
 *
 * @author Moses
 */
public class Partner {
    private int partnerId;
    private String name;
    private String phoneNumber;
    private String email;
    private Timestamp dateRegistered;
    private int status;
    private PartnerSeries series;

    public PartnerSeries getSeries() {
        return series;
    }

    public Partner setSeries(PartnerSeries series) {
        this.series = series;
        return this;
    }
    
    

    public int getPartnerId() {
        return partnerId;
    }

    public Partner setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    return this;}

    public String getName() {
        return name;
    }

    public Partner setName(String name) {
        this.name = name;
    return this;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Partner setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    return this;}

    public String getEmail() {
        return email;
    }

    public Partner setEmail(String email) {
        this.email = email;
    return this;}

    public Timestamp getDateRegistered() {
        return dateRegistered;
    }

    public Partner setDateRegistered(Timestamp dateRegistered) {
        this.dateRegistered = dateRegistered;
    return this;}

    public int getStatus() {
        return status;
    }

    public Partner setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

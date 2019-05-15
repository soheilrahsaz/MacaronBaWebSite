/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.User;

import java.sql.Timestamp;

/**
 *
 * @author Moses
 */
public class DiscountTicket {
    private int discountId;
    private int userId;
    private double effect;
    private Timestamp expireDate;
    private int status; //-1:deleted/0:ok/1:used

    public int getDiscountId() {
        return discountId;
    }

    public DiscountTicket setDiscountId(int discountId) {
        this.discountId = discountId;
    return this;}

    public int getUserId() {
        return userId;
    }

    public DiscountTicket setUserId(int userId) {
        this.userId = userId;
    return this;}

    public double getEffect() {
        return effect;
    }

    public DiscountTicket setEffect(double effect) {
        this.effect = effect;
    return this;}

    public Timestamp getExpireDate() {
        return expireDate;
    }

    public DiscountTicket setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    return this;}

    public int getStatus() {
        return status;
    }

    public DiscountTicket setStatus(int status) {
        this.status = status;
    return this;}

    
}

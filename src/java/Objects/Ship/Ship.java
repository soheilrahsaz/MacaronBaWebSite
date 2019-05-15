/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Ship;

import Objects.User.UserAddress;
import java.sql.Timestamp;

/**
 *
 * @author Moses
 */
public class Ship {
    private int shipId;
    private ShipType shipType;
    private UserAddress address;
    private Timestamp date;
    private int status;

    public Timestamp getDate() {
        return date;
    }

    public Ship setDate(Timestamp date) {
        this.date = date;
        return this;
    }
    
    public int getShipId() {
        return shipId;
    }

    public Ship setShipId(int shipId) {
        this.shipId = shipId;
        
    return this;}

    public ShipType getShipType() {
        return shipType;
    }

    public Ship setShipType(ShipType shipType) {
        this.shipType = shipType;
    return this;}

    public UserAddress getAddress() {
        return address;
    }

    public Ship setAddress(UserAddress address) {
        this.address = address;
    return this;}

    /**
     * -1:deleted/0:pending/1:sent/2:delivered	
     * @return 
     */
    public int getStatus() {
        return status;
    }

    public Ship setStatus(int status) {
        this.status = status;
    return this;}
    
    
    
}

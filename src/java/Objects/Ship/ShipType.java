/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Ship;

/**
 *
 * @author Moses
 */
public class ShipType {
    private int shipTypeId;
    private String name;
    private String description;

    public int getShipTypeId() {
        return shipTypeId;
    }

    public ShipType setShipTypeId(int shipTypeId) {
        this.shipTypeId = shipTypeId;   
    return this;}

    public String getName() {
        return name;
    }

    public ShipType setName(String name) {
        this.name = name;
    return this;}

    public String getDescription() {
        return description;
    }

    public ShipType setDescription(String description) {
        this.description = description;
    return this;}
    
    
}

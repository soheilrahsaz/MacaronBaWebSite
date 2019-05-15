/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.InPerson;

/**
 *
 * @author Moses
 */
public class InPersonItem {
    private int inpersonId;
    private int itemId;
    private String description;
    private int price;
    private int status;

    public int getInpersonId() {
        return inpersonId;
    }

    public InPersonItem setInpersonId(int inpersonId) {
        this.inpersonId = inpersonId;
        return this;
    }

    public int getItemId() {
        return itemId;
    }

    public InPersonItem setItemId(int itemId) {
        this.itemId = itemId;
        
    return this;}

    public String getDescription() {
        return description;
    }

    public InPersonItem setDescription(String description) {
        this.description = description;
    return this;}

    public int getPrice() {
        return price;
    }

    public InPersonItem setPrice(int price) {
        this.price = price;
    return this;}

    public int getStatus() {
        return status;
    }

    public InPersonItem setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

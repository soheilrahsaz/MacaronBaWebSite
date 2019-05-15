/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Order;

import Objects.Product.Product;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Item {
    private int partId;
    private int itemId;
    private int count;
    private int status;
    private LinkedList<ItemProperty> itemProperties;

    public LinkedList<ItemProperty> getItemProperties() {
        return itemProperties;
    }

    public Item setItemProperties(LinkedList<ItemProperty> itemProperties) {
        this.itemProperties = itemProperties;
        return this;
    }
    
    

    public int getPartId() {
        return partId;
    }

    public Item setPartId(int partId) {
        this.partId = partId;
        
    return this;}

    public int getItemId() {
        return itemId;
    }

    public Item setItemId(int itemId) {
        this.itemId = itemId;
    return this;}

    public int getCount() {
        return count;
    }

    public Item setCount(int count) {
        this.count = count;
    return this;}

    public int getStatus() {
        return status;
    }

    public Item setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

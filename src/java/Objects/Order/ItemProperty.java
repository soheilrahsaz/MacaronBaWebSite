/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Order;

import Objects.Product.Property;
import Objects.Product.PropertyValue;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class ItemProperty {
    private int itemId;
    private int itemPropertyId;
    private Property property;
    private LinkedList<PropertyValue> propertyValues;
    private int status;

    public int getItemId() {
        return itemId;
    }

    public ItemProperty setItemId(int itemId) {
        this.itemId = itemId;
        
        return this;
    }

    public int getItemPropertyId() {
        return itemPropertyId;
    }

    public ItemProperty setItemPropertyId(int itemPropertyId) {
        this.itemPropertyId = itemPropertyId;
        return this;
    }

    public Property getProperty() {
        return property;
    }

    public ItemProperty setProperty(Property property) {
        this.property = property;
    return this;
    }
    

    public LinkedList<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public ItemProperty setPropertyvaValues(LinkedList<PropertyValue> prpertyvaValue) {
        this.propertyValues = prpertyvaValue;
    return this;
    }
    

    public int getStatus() {
        return status;
    }

    public ItemProperty setStatus(int status) {
        this.status = status;
    return this;
    }
    
    
}

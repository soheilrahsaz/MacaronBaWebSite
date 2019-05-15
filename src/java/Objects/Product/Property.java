/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Product;

import java.util.LinkedList;

/**
 * this object represents a property. every product has several properties, it has an Id, name, status and linked list of PropertyValue 
 * 
 * @author Moses
 */
public class Property {
    private int propertyId;
    private String name;
    private LinkedList<PropertyValue> values;
    private int status;
    private boolean multiple;
    private String description;
    private boolean effectAll;

    public boolean isEffectAll() {
        return effectAll;
    }

    public Property setEffectAll(boolean effectAll) {
        this.effectAll = effectAll;
        return this;
    }
    
    
    
    
    public Property()
    {
        this.values=new LinkedList<PropertyValue>();
    }

    public String getDescription() {
        return description;
    }

    public Property setDescription(String description) {
        this.description = description;
        return this;
    }

    
    
    public boolean isMultiple() {
        return multiple;
    }

    public Property setMultiple(boolean multiple) {
        this.multiple = multiple;
        return this;
    }

    
    
    public int getPropertyId() {
        return propertyId;
    }

    public Property setPropertyId(int propertyId) {
        this.propertyId = propertyId;
        
    return this;}

    public String getName() {
        return name;
    }

    public Property setName(String name) {
        this.name = name;
    return this;}

    public LinkedList<PropertyValue> getValues() {
        return values;
    }

    public Property addValue(PropertyValue value)
    {
        this.values.add(value);
        return this;
    }
    
    public Property setValues(LinkedList<PropertyValue> values) {
        this.values = values;
    return this;}

    public int getStatus() {
        return status;
    }

    public Property setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

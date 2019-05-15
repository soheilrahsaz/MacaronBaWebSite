/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Product;

/**
 * this item is only used inside a property. every property has several propertyvalues. 
 * it has an Id, name and int pricebyscale which shows changes in the price based on selected scale
 * @author Moses
 */
public class PropertyValue {
    private int valueId;
    private String name;
    private int priceByScale;
    
    public PropertyValue()
    {
        
    }

    public int getValueId() {
        return valueId;
    }

    public PropertyValue setValueId(int valueId) {
        this.valueId = valueId;
        
    return this;}

    public String getName() {
        return name;
    }

    public PropertyValue setName(String name) {
        this.name = name;
    return this;}

    public int getPriceByScale() {
        return priceByScale;
    }

    public PropertyValue setPriceByScale(int priceByName) {
        this.priceByScale = priceByName;
    return this;}
    
    
}

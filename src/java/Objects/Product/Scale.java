/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Product;

import java.util.LinkedList;

/**
 * scale of sale
 * @author Moses
 */
public class Scale {
    private int scaleId;
    private String name;
    private LinkedList<ScaleValue> values;
    
    public Scale()
    {
        this.values=new LinkedList<ScaleValue>();
    }

    public int getScaleId() {
        return scaleId;
    }

    public Scale setScaleId(int scaleId) {
        this.scaleId = scaleId;
    return this;}

    public String getName() {
        return name;
    }
    
    public Scale addValue(ScaleValue value)
    {
        this.values.add(value);
        return this;
    }

    public Scale setName(String name) {
        this.name = name;
    return this;}

    public LinkedList<ScaleValue> getValues() {
        return values;
    }

    public Scale setValues(LinkedList<ScaleValue> values) {
        this.values = values;
        return this;
    }
    
    
    
}

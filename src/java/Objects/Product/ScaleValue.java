/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Product;

import java.util.LinkedList;

/**
 * only used inside a scale object
 * @author Moses
 */
public class ScaleValue {
    private int valueId;
    private double value;
    private double effect;
    private String description;
    private String picture;
    private LinkedList<ScalePackage> packages;

    public LinkedList<ScalePackage> getPackages() {
        return packages;
    }

    public ScaleValue setPackages(LinkedList<ScalePackage> packages) {
        this.packages = packages;
        return this;
    }
    
    

    public String getDescription() {
        return description;
    }

    public ScaleValue setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public ScaleValue setPicture(String picture) {
        this.picture = picture;
        return this;
    }
    
    

    public double getEffect() {
        return effect;
    }

    public ScaleValue setEffect(double effect) {
        this.effect = effect;
        return this;
    }
    

    public int getValueId() {
        return valueId;
    }

    public ScaleValue setValaueId(int valaueId) {
        this.valueId = valaueId;
        return this;
    }

    public double getValue() {
        return value;
    }

    public ScaleValue setValue(double value) {
        this.value = value;
        return this;
    }
    
    
}

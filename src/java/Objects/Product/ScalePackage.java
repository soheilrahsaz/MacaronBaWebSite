/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Product;

/**
 *
 * @author Moses
 */
public class ScalePackage {
    private int packageId;
    private int scaleId;
    private String name;
    private String description;
    private String picture;
    private int price;
    private int status;

    public int getPackageId() {
        return packageId;
    }

    public ScalePackage setPackageId(int packageId) {
        this.packageId = packageId;
        
    return this;}

    public int getScaleId() {
        return scaleId;
    }

    public ScalePackage setScaleId(int scaleId) {
        this.scaleId = scaleId;
    return this;}

    public String getName() {
        return name;
    }

    public ScalePackage setName(String name) {
        this.name = name;
    return this;}

    public String getDescription() {
        return description;
    }

    public ScalePackage setDescription(String description) {
        this.description = description;
    return this;}

    public String getPicture() {
        return picture;
    }

    public ScalePackage setPicture(String picture) {
        this.picture = picture;
    return this;}

    public int getPrice() {
        return price;
    }

    public ScalePackage setPrice(int price) {
        this.price = price;
    return this;}

    public int getStatus() {
        return status;
    }

    public ScalePackage setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Product;

import java.util.LinkedList;

/**
 * this item represents a product, it includes a category,description, id, name, scale of sale(weight,item), base scale , status, linkedlist of properties  
 * and a linklisted of ProductPictures 
 * @author Moses
 */
public class Product {
    private Category category;
    private int productId;
    private String name;
    private String description;
    private Scale scale;
    private int basePrice;
    private ScaleValue baseScale;
    private int status;
    private LinkedList<Property> properties;
    private LinkedList<Picture> pictures;
    
    public Product()
    {
        this.properties=new LinkedList<Property>();
        this.pictures=new LinkedList<Picture>();
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    
    public int getProductId() {
        return productId;
    }

    public Product setProductId(int productId) {
        this.productId = productId;
    return this;}

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
    return this;}

    public Scale getScale() {
        return scale;
    }

    public Product setScale(Scale scale) {
        this.scale = scale;
    return this;}

    public int getBasePrice() {
        return basePrice;
    }

    public Product setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    return this;}

    public ScaleValue getBaseScale() {
        return baseScale;
    }

    public Product setBaseScale(ScaleValue baseScale) {
        this.baseScale = baseScale;
    return this;}

    public int getStatus() {
        return status;
    }

    public Product setStatus(int status) {
        this.status = status;
    return this;}

    public LinkedList<Property> getProperties() {
        return properties;
    }

    public Product addProperty(Property property)
    {
        this.properties.add(property);
        return this;
    }
    
    public Product setProperties(LinkedList<Property> properties) {
        this.properties = properties;
    return this;}

   

    public Product addPictureUrl(Picture picture)
    {
        this.pictures.add(picture);
        return this;
    }
    
    public Product setPicture(LinkedList<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }

   
    public LinkedList<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(LinkedList<Picture> pictures) {
        this.pictures = pictures;
    }

    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }
    
    
    
    
}

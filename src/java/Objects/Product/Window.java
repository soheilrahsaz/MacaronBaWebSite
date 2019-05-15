/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Product;

import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Window {
    private int productId;
    private String name;
    private int count;
    private int price;
    private String description;
    private LinkedList<Picture> pictures;
    private int status;
    private Category category;

    public Category getCategory() {
        return category;
    }

    public Window setCategory(Category category) {
        this.category = category;
        return this;
    }
    
    
    
    public int getPrice() {
        return price;
    }

    public Window setPrice(int price) {
        this.price = price;
        return this;
    }

    
    
    public LinkedList<Picture> getPictures() {
        return pictures;
    }

    public Window setPictures(LinkedList<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }

    
    
    public int getProductId() {
        return productId;
    }

    public Window setProductId(int productId) {
        this.productId = productId;
        
    return this;}

    public String getName() {
        return name;
    }

    public Window setName(String name) {
        this.name = name;
    return this;}

    public int getCount() {
        return count;
    }

    public Window setCount(int count) {
        this.count = count;
    return this;}

    public String getDescription() {
        return description;
    }

    public Window setDescription(String description) {
        this.description = description;
    return this;}

    public int getStatus() {
        return status;
    }

    public Window setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

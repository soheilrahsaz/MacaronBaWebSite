/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Partner;

import Objects.Product.Category;
import Objects.Product.Picture;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class PartnerProduct {
    private int productId;
    private PartnerCategory category;
    private String name;
    private int price;
    private int count;
    private String description;
    private int status;
    private LinkedList<Picture> pictures;

    public LinkedList<Picture> getPictures() {
        return pictures;
    }

    public PartnerProduct setPictures(LinkedList<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }
    
    

    public int getProductId() {
        return productId;
    }

    public PartnerProduct setProductId(int productId) {
        this.productId = productId;
    return this;}

    public PartnerCategory getCategory() {
        return category;
    }

    public PartnerProduct setCategory(PartnerCategory category) {
        this.category = category;
    return this;}

    public String getName() {
        return name;
    }

    public PartnerProduct setName(String name) {
        this.name = name;
    return this;}

    public int getPrice() {
        return price;
    }

    public PartnerProduct setPrice(int price) {
        this.price = price;
    return this;}

    public int getCount() {
        return count;
    }

    public PartnerProduct setCount(int count) {
        this.count = count;
    return this;}

    public String getDescription() {
        return description;
    }

    public PartnerProduct setDescription(String description) {
        this.description = description;
    return this;}

    public int getStatus() {
        return status;
    }

    public PartnerProduct setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

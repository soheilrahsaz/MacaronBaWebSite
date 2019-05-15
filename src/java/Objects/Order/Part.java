/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Order;

import Objects.Product.Product;
import Objects.Product.Scale;
import Objects.Product.ScalePackage;
import Objects.Product.ScaleValue;
import Objects.Product.Window;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Part {
    private int orderId;
    private int partId;
    private Product product;
    private Window productWindow;
    private LinkedList<Item> items;
    private Scale scale;
    private ScaleValue scaleValue;
    private ScalePackage scalePackage;
    private int count;
    private int finalPrice;
    private boolean window;
    private int status;

    public Window getProductWindow() {
        return productWindow;
    }

    public Part setProductWindow(Window productWindow) {
        this.productWindow = productWindow;
        return this;
    }
    
    

    public ScalePackage getScalePackage() {
        return scalePackage;
    }

    public Part setScalePackage(ScalePackage scalePackage) {
        this.scalePackage = scalePackage;
        return this;
    }

    
    
    public LinkedList<Item> getItems() {
        return items;
    }

    public Part setItems(LinkedList<Item> items) {
        this.items = items;
        return this;
    }
    
    public int getOrderId() {
        return orderId;
    }

    public Part setOrderId(int orderId) {
        this.orderId = orderId;
        
    return this;}

    public int getPartId() {
        return partId;
    }

    public Part setPartId(int partId) {
        this.partId = partId;
    return this;}

    public Product getProduct() {
        return product;
    }

    public Part setProduct(Product product) {
        this.product = product;
    return this;}

    public Scale getScale() {
        return scale;
    }

    public Part setScale(Scale scale) {
        this.scale = scale;
    return this;}

    public ScaleValue getScaleValue() {
        return scaleValue;
    }

    public Part setScaleValue(ScaleValue scaleValue) {
        this.scaleValue = scaleValue;
    return this;}

    public int getCount() {
        return count;
    }

    public Part setCount(int count) {
        this.count = count;
    return this;}

    public int getFinalPrice() {
        return finalPrice;
    }

    public Part setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    return this;}

    public boolean isWindow() {
        return window;
    }

    public Part setWindow(int window) {
        this.window = window==1;
    return this;}
    
    public Part setWindow(boolean window) {
        this.window = window;
    return this;}

    public int getStatus() {
        return status;
    }

    public Part setStatus(int status) {
        this.status = status;
    return this;}
    
    
    
}

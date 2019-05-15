/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Partner;

/**
 *
 * @author Moses
 */
public class PartnerPart {
    private int partId;
    private int orderId;
    private PartnerProduct product;
    private int count;
    private int finalPrice;
    private int status;

    public int getPartId() {
        return partId;
    }

    public PartnerPart setPartId(int partId) {
        this.partId = partId;
    return this;}

    public int getOrderId() {
        return orderId;
    }

    public PartnerPart setOrderId(int orderId) {
        this.orderId = orderId;
    return this;}

    public PartnerProduct getProduct() {
        return product;
    }

    public PartnerPart setProduct(PartnerProduct product) {
        this.product = product;
    return this;}

    public int getCount() {
        return count;
    }

    public PartnerPart setCount(int count) {
        this.count = count;
    return this;}

    public int getFinalPrice() {
        return finalPrice;
    }

    public PartnerPart setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    return this;}

    public int getStatus() {
        return status;
    }

    public PartnerPart setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

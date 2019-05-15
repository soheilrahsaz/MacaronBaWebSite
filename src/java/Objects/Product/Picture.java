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
public class Picture {
    private int pictureId;
    private String url;
    private int productId;

    public int getPictureId() {
        return pictureId;
    }

    public Picture setPictureId(int pictureId) {
        this.pictureId = pictureId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Picture setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getProductId() {
        return productId;
    }

    public Picture setProductId(int productId) {
        this.productId = productId;
        return this;
    }
    
    
}

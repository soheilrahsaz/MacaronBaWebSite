/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Communication;

import Objects.Product.Product;
import Objects.User.User;
import java.sql.Timestamp;

/**
 *
 * @author Moses
 */
public class Comment {
    private int commentId;
    private int productId;
    private User user;
    private String productName;
    private Timestamp date;
    private String comment;
    private int status;

    public User getUser() {
        return user;
    }

    public Comment setUser(User user) {
        this.user = user;
        return this;
    }
    
    

    public int getProductId() {
        return productId;
    }

    public Comment setProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public Comment setProductName(String productName) {
        this.productName = productName;
        return this;
    }
    
    

    public int getCommentId() {
        return commentId;
    }

    public Comment setCommentId(int commentId) {
        this.commentId = commentId;
        
    return this;}

    public Timestamp getDate() {
        return date;
    }

    public Comment setDate(Timestamp date) {
        this.date = date;
    return this;}

    public String getComment() {
        return comment;
    }

    public Comment setComment(String comment) {
        this.comment = comment;
    return this;}

    public int getStatus() {
        return status;
    }

    public Comment setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

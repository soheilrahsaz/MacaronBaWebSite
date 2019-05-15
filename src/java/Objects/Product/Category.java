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
public class Category {
    private int categoryId;
    private String name;

    public int getCategoryId() {
        return categoryId;
    }

    public Category setCategoryId(int categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }
    
    
}

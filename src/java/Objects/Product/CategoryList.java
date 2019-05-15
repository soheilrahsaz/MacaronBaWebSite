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
public class CategoryList {
    private int categoryId;
    private String name;
    private CategoryList parent;
    private LinkedList<CategoryList> children;

    public CategoryList getParent() {
        return parent;
    }

    public CategoryList setParent(CategoryList parent) {
        this.parent = parent;
        return this;
    }

    public LinkedList<CategoryList> getChildren() {
        return children;
    }

    public CategoryList setChildren(LinkedList<CategoryList> children) {
        this.children = children;
        return this;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public CategoryList setCategoryId(int categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryList setName(String name) {
        this.name = name;
        return this;
    }
    
    
}

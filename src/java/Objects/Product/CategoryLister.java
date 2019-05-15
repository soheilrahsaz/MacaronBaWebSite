/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Product;

import SQL.Commands.ProductSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Moses
 */
public class CategoryLister {
    private Connection connection;
    private ProductSQLCommands productSQL;
    private CategoryList head;
    private String dropDown="";
    
    public CategoryLister(Connection conneciton) throws SQLException
    {
        this.productSQL=new ProductSQLCommands(connection);
        this.head=this.productSQL.getHeadCategory();
        this.head.setChildren(this.productSQL.getChildrenOf(this.head));
    }
    public CategoryList getHead() {
        return head;
    }

    public CategoryLister setHead(CategoryList head) {
        this.head = head;
        return this;
    }
    
    CategoryList temp=this.head;
    /**
     * returns option tags including all categories
     * @return 
     */
    public String toDropDown()
    {
       
        
//        this.dropDown+="<option>"++"</option>";
        
        
        
        return dropDown;
    }
    
    
}

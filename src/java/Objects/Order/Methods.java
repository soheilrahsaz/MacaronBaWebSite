/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Order;

import Objects.Product.ScaleValue;
import SQL.Commands.ProductSQLCommands;
import java.sql.Connection;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Methods {
    private ProductSQLCommands productSQL;
    
    public Methods(Connection connection)
    {
        this.productSQL=new ProductSQLCommands(connection);
    }
    
    public LinkedList<Integer> getPrice(Receipt receipt)
    {
        LinkedList<Integer> price=new LinkedList<Integer>();
       
        
        return price;
    }
        
}

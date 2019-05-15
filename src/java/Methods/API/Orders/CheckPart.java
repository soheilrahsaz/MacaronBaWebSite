/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Orders;

import Objects.Order.Item;
import Objects.Order.ItemProperty;
import Objects.Order.Part;
import Objects.Product.Product;
import Objects.Product.PropertyValue;
import Objects.Product.Window;
import SQL.Commands.ProductSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Moses
 */
public class CheckPart {
    private ProductSQLCommands productSQL;
    private Part part;
    private boolean isValid=false;
    
    public CheckPart(Connection connection,Part part) throws SQLException
    {
        this.part=part;
        this.productSQL=new ProductSQLCommands(connection);
        this.isValid=validate();
    }
    
    private boolean validate() throws SQLException
    {
        if(part.isWindow())
        {
            return validateWindow();
        }else
        {
            return validateProduct();
        }
    }
    
    private boolean validateWindow() throws SQLException
    {
        if(!this.productSQL.productIdExists(part.getProductWindow().getProductId()))
        {
            return false;
        }
        
        Window window=this.productSQL.getProductWindow(part.getProductWindow().getProductId());
        if(window.getProductId()==0)
        {
            return false;
        }
        
        if(window.getStatus()!=1)
        {
            return false;
        }
        
        if(window.getCount()-part.getCount()<0)
        {
            return false;
        }
        
        return true;
    }
    
    private boolean validateProduct() throws SQLException
    {
        if(!this.productSQL.productIdExists(part.getProduct().getProductId()))
        {
            return false;
        }
        
        Product product=this.productSQL.getProduct(part.getProduct().getProductId());
        
        if(product.getProductId()==0)
        {
            return false;
        }
        
        if(product.getStatus()!=1)
        {
            return false;
        }
        
        if(!this.productSQL.isScaleValueValid(product.getProductId(), part.getScaleValue().getValueId()))
        {
            return false;
        }
        
        if(!this.productSQL.isScalePackageValid(part.getScaleValue().getValueId(), part.getScalePackage().getPackageId()))
        {
            return false;
        }
        
        for(Item item:part.getItems())
        {
            for(ItemProperty prop:item.getItemProperties())
            {
                if(!this.productSQL.isPropertyValid(product.getProductId(), prop.getProperty().getPropertyId()))
                    return false;
                
                for(PropertyValue value:prop.getPropertyValues())
                {
                    if(!this.productSQL.isPropertyValueValid(prop.getProperty().getPropertyId(), value.getValueId()))
                        return false;
                }
                
            }
        }
        
        return true;
        
    }
    
    public boolean isValid()
    {
        return this.isValid;
    }
    
}

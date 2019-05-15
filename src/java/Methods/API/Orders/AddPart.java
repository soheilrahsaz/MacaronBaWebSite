/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Orders;

import Objects.Order.Item;
import Objects.Order.ItemProperty;
import Objects.Order.Part;
import Objects.Product.PropertyValue;
import SQL.Commands.OrderSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Moses
 */
public class AddPart {
    private final int orderId;
    private Part part;
    private OrderSQLCommands orderSQL;
    private int partId;
    
    public AddPart(Connection connection,int orderId,Part part)
    {
        this.orderId=orderId;
        this.orderSQL=new OrderSQLCommands(connection);
        this.part=part;
    }
    
    public void addPart() throws SQLException
    {
        if(part.isWindow())
        {
            addWindowPart();
        }else
        {
            addProductPart();
        }
       
    }
    
    private void addWindowPart() throws SQLException
    {
        int repeatedPartId=this.orderSQL.isPartRepeated(orderId, part.getProductWindow().getProductId());
        if(repeatedPartId==-1)
        {
            this.partId=this.orderSQL.addPart(orderId, part.getProductWindow().getProductId(), part.getCount());
        }else
        {
            this.partId=repeatedPartId;
            this.orderSQL.addCountToPart(partId, part.getCount());
        }
        
        
    }
    
    private void addProductPart() throws SQLException
    {
        this.partId=this.orderSQL.addPart(orderId, part.getProduct().getProductId(), part.getScaleValue().getValueId(),
                part.getScalePackage().getPackageId(),part.getCount());
        
        for(Item item:part.getItems())
        {
            addItem(item);
        }
    }
    
    private void addItem(Item item) throws SQLException
    {
        int itemId=this.orderSQL.addOrderItem(partId,item.getCount());
        for(ItemProperty property:item.getItemProperties())
        {
            addItemProperties(property, itemId);
        }
    }
    
    private void addItemProperties(ItemProperty property,int itemId) throws SQLException
    {
        int propertyId=property.getProperty().getPropertyId();
        for(PropertyValue value:property.getPropertyValues())
        {
            this.orderSQL.addOrderItemProperty(itemId, propertyId, value.getValueId());
        }
    }
    
}

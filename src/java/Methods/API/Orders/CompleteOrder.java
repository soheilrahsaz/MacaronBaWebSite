/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Orders;

import ExceptionsChi.DoesntExistException;
import Objects.Order.Item;
import Objects.Order.ItemProperty;
import Objects.Order.Part;
import Objects.Order.Receipt;
import Objects.Payment.Payment;
import Objects.Product.PropertyValue;
import Objects.Ship.Ship;
import Objects.User.DiscountTicket;
import SQL.Commands.OrderHistorySQLCommands;
import SQL.Commands.OrderSQLCommands;
import SQL.Commands.ProductSQLCommands;
import SQL.Commands.UserSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Moses
 */
public class CompleteOrder {
    private OrderSQLCommands orderSQL;
    private OrderHistorySQLCommands orderHisSQL;
    private ProductSQLCommands productSQL;
    private UserSQLCommands userSQL;
//    public CompleteOrder(OrderSQLCommands orderSQL)
//    {
//        this.orderSQL=orderSQL;
//    }
    
    public CompleteOrder(Connection connection)
    {
        this.orderSQL=new OrderSQLCommands(connection);
        this.orderHisSQL=new OrderHistorySQLCommands(connection);
        this.productSQL=new ProductSQLCommands(connection);
        this.userSQL=new UserSQLCommands(connection);
    }
        
    public void completeOrder(int orderId,Receipt receipt,Ship ship,Payment payment,DiscountTicket discount) throws SQLException, DoesntExistException
    {
        
        int price=setPrices(receipt);
        
        int shipId=orderSQL.addShip(ship.getShipType().getShipTypeId(), ship.getAddress().getAddressId());
        int payStatus=0;
        if(payment.getPaymentType().getPaymentTypeId()==2)
        {
            payStatus=0;
        }else if(payment.getPaymentType().getPaymentTypeId()==3)
        {
            payStatus=1;
        }
        
        int paymentId=orderSQL.addPayment(price, payment.getPaymentType().getPaymentTypeId(), "", payStatus);
        
        
        orderSQL.setOrderComplete(orderId, shipId, paymentId,discount.getDiscountId());
        userSQL.invalidateDiscount(discount.getDiscountId());
        decreaseWindowCount(receipt);
        this.productSQL.runOutWindows();
    }
    
    private void decreaseWindowCount(Receipt receipt) throws SQLException
    {
        for(Part part:receipt.getParts())
        {
            if(part.isWindow())
            {
                this.productSQL.decreaseWindowCount(part.getProductWindow().getProductId(), part.getCount());
            }
        }
    }
    
    /**
     * calculates and returns the receipt price
     * @param receipt
     * @return
     * @throws SQLException 
     */
    public int setPrices(Receipt receipt) throws SQLException
    {
        int payPrice=0;
        for(Part part:receipt.getParts())
        {
            int price=0;
            if(part.isWindow())
            {
                price=part.getProductWindow().getPrice()*part.getCount();
                orderSQL.setPartFinalPrice(part.getPartId(),price );
            }else
            {
                price=calculateProductPrice(part);
                orderSQL.setPartFinalPrice(part.getPartId(),price ); 
            }
            payPrice+=price;
        }
        
        return payPrice;
    }
    
    /**
     * calculates the whole price of a product and returns the value
     * @param part
     * @return 
     */
    public static int calculateProductPrice(Part part)
    {
        if(part.isWindow())
        {
            return part.getProductWindow().getPrice()*part.getCount();
        }
        
        int wholePrice=0;
        for(Item item:part.getItems())
        {
            int propPrices=0;
            for(ItemProperty prop:item.getItemProperties())
            {
                for(PropertyValue value:prop.getPropertyValues())
                {
                    propPrices+=value.getPriceByScale();
                }
            }
            wholePrice+=propPrices*item.getCount();
        }
        
        if(part.getProduct().getProductId()!=1)
        {
            wholePrice*=part.getScaleValue().getValue();
        }
        
        wholePrice+=part.getScalePackage().getPrice();
        
        wholePrice+=(part.getScaleValue().getValue())*part.getProduct().getBasePrice();
        
        wholePrice*=part.getCount();
        
        wholePrice*=part.getScaleValue().getEffect()/100;
        
        return wholePrice;
    }
    
    
}

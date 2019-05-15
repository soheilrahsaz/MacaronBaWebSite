/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import ExceptionsChi.DoesntExistException;
import Objects.Order.Item;
import Objects.Order.ItemProperty;
import Objects.Order.Part;
import Objects.Order.Receipt;
import Objects.Partner.Partner;
import Objects.Payment.Payment;
import Objects.Payment.PaymentType;
import Objects.Product.Product;
import Objects.Product.Property;
import Objects.Product.PropertyValue;
import Objects.Product.ScalePackage;
import Objects.Ship.Ship;
import Objects.Ship.ShipType;
import Objects.User.User;
import Objects.User.UserAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Random;

/**
 * this object is used for ordering and geting order's history
 * @author Moses
 */
public class OrderSQLCommands {
    private Connection connection;
    private UserSQLCommands userSQL;
    private ProductSQLCommands productSQL;
    public OrderSQLCommands(Connection connection)
    {
        this.connection=connection;
        this.userSQL=new UserSQLCommands(connection);
        this.productSQL=new ProductSQLCommands(connection);
    }
    
    /**
     * retrieves a paymenttype from data base
     * @param paymentTypeId the id of the payment
     * @return an paymentType object
     * @throws SQLException
     * @throws DoesntExistException if there weren't any payment type with this id 
     */
    public PaymentType getPaymentType(int paymentTypeId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment_type` WHERE `paymentTypeId`=?");
        prep.setInt(1, paymentTypeId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            throw new DoesntExistException("paymenttype");
        }
        
        PaymentType paymentType=new PaymentType();
        paymentType.setPaymentTypeId(paymentTypeId).setName(res.getString("name")).setDescription(res.getString("description"))
                .setUrl(res.getString("url")).setStatus(res.getInt("status"));
        
        return paymentType;
    }
    
    /**
     * retrieves all peymenttypes except deleted ones
     * @return a linked list of payment types
     * @throws SQLException 
     */
    public LinkedList<PaymentType> getPaymentTypes() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment_type` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<PaymentType> types=new LinkedList<PaymentType>();
        while(res.next())
        {
            PaymentType paymentType=new PaymentType();
            paymentType.setPaymentTypeId(res.getInt("paymentTypeId")).setName(res.getString("name")).setDescription(res.getString("description"))
                .setUrl(res.getString("url")).setStatus(res.getInt("status"));
                types.add(paymentType);
        }
            
        return types;
    }
    
    /**
     * retrieves all payments from data base
     * @return a linked list of payments
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Payment> getPayments() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Payment> payments=new LinkedList<Payment>();
        while(res.next())
        {
            Payment payment=new Payment();
            payment.setApprove(res.getString("approve")).setPaymentId(res.getInt("paymentId"))
                    .setPaymentType(getPaymentType(res.getInt("paymentTypeId"))).setDate(res.getTimestamp("date"))
                    .setPrice(res.getInt("price")).setStatus(res.getInt("status"));
            payments.add(payment);
        }
        
        return payments;
    }
    
    /**
     * retrieves one payment 
     * @param paymentId the id of the payment 
     * @return
     * @throws SQLException
     * @throws DoesntExistException if payment doesn't exist
     */
    public Payment getPayment(int paymentId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment` WHERE `paymentId`=?");
        prep.setInt(1, paymentId);
        ResultSet res=prep.executeQuery();
        Payment payment=new Payment();
        if(!res.first())
        {
            return payment;
        }
        
        payment.setApprove(res.getString("approve")).setPaymentId(res.getInt("paymentId"))
                .setPaymentType(getPaymentType(res.getInt("paymentTypeId")))
                .setDate(res.getTimestamp("date"))
                .setPrice(res.getInt("price")).setStatus(res.getInt("status"));
            
            return payment;
    }
    
    /**
     * 
     * @param paymentTypeId
     * @return true if id exists and false if not
     * @throws SQLException 
     */
    public boolean paymentTypeIdExists(int paymentTypeId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `payment_type` WHERE `paymentTypeId`=?");
        prep.setInt(1, paymentTypeId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds a payment type 
     * @param name
     * @param description
     * @param url
     * @return it returns the id of the added payment type
     * @throws SQLException 
     */
    public int addPaymentType(String name,String description,String url) throws SQLException
    {
        int paymentTypeId;
        Random random=new Random();
        while(true)
        {
            paymentTypeId=Math.abs(random.nextInt());
            if(!paymentTypeIdExists(paymentTypeId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `paymentType` (`paymentTypeId`,`name`,`description`,`url`) VALUES (?,?,?,?)");
        prep.setInt(1, paymentTypeId);
        prep.setString(2, name);
        prep.setString(3, description);
        prep.setString(4, url);
        prep.execute();
        
        return paymentTypeId;
    }
    
    /**
     * 
     * @param paymentId
     * @return true if it exist and false if it doesn't
     * @throws SQLException 
     */
    public boolean paymentIdExists(int paymentId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `payment` WHERE `paymentId`=?"); 
        prep.setInt(1, paymentId); 
        return prep.executeQuery().first();
    }
        
    /**
     * adds a peyment into data base
     * @param price
     * @param paymentTypeId
     * @param approve
     * @return the id of the added payment
     * @throws SQLException 
     */
    public int addPayment(int price,int paymentTypeId,String approve,int status) throws SQLException
    {
        int paymentId;
        Random random=new Random();
        while(true)
        {
            paymentId=Math.abs(random.nextInt());
            if(!paymentIdExists(paymentId))
                break;
        }
        
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `payment` (`paymentId`,`price`,`paymentTypeId`,`approve`,`status`) VALUES (?,?,?,?,?)");
        prep.setInt(1, paymentId);
        prep.setInt(2, price);
        prep.setInt(3, paymentTypeId);
        prep.setString(4, approve);
        prep.setInt(5,status);
        prep.execute();
        
        return paymentId;
    }
        
    /**
     * retrieves all ship types
     * @return a linkedlist of ship type
     * @throws SQLException 
     */
    public LinkedList<ShipType> getShipTypes() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareCall("SELECT * FROM `ship_type` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<ShipType> types=new LinkedList<ShipType>();
        while(res.next())
        {
            ShipType type=new ShipType();
            type.setShipTypeId(res.getInt("shipTypeId")).setName(res.getString("name")).setDescription(res.getString("description"));
            types.add(type);
        }
        return types;
    }
    /**
     * retrieves one ship type 
     * @param shipTypeId
     * @return a ship type object
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public ShipType getShipType(int shipTypeId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `ship_type` WHERE `shipTypeId`=?");
        prep.setInt(1, shipTypeId);
        ResultSet res=prep.executeQuery();
        ShipType type=new ShipType();
        if(!res.first())
        {
            return type;
        }
        
        
        type.setShipTypeId(shipTypeId).setName(res.getString("name")).setDescription(res.getString("description"));
        
        return type;
    }
    
    /**
     * 
     * @param shipTypeId
     * @return true if it does
     * @throws SQLException 
     */
    public boolean shipTypeIdExists(int shipTypeId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `ship_type` WHERE `shipTypeId`=?");
        prep.setInt(1, shipTypeId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds ship type to data base
     * @param name
     * @param description
     * @return the id of the added ship type
     * @throws SQLException 
     */
    public int addShipType(String name,String description) throws SQLException
    {
        int shipTypeId;
        Random random=new Random();
        while(true)
        {
            shipTypeId=Math.abs(random.nextInt());
            if(!shipTypeIdExists(shipTypeId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `ship_type` (`shipTypeId`,`name`,`description`) VALUES (?,?,?)");
        prep.setInt(1, shipTypeId);
        prep.setString(2, name);
        prep.setString(3, description);
        prep.execute();
        return shipTypeId;
    }
    
    /**
     * retrievs all ships
     * @return a linekd list of ship
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Ship> getShips() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `ship` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Ship> ships=new LinkedList<Ship>();
        while(res.next())
        {
            Ship ship=new Ship();
            ship.setAddress(this.userSQL.getAddress(res.getInt("addressId"))).setShipId(res.getInt("shipId")).setDate(res.getTimestamp("date"))
                    .setShipType(getShipType(res.getInt("shipTypeId"))).setStatus(res.getInt("status"));
            ships.add(ship);
        }
        
        return ships;
    }
    /**
     * retrieves one ship
     * @param shipId
     * @return
     * @throws SQLException
     * @throws DoesntExistException if it doesn't exist
     */
    public Ship getShip(int shipId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `ship` WHERE `shipId`=?");
        prep.setInt(1, shipId);
        ResultSet res=prep.executeQuery();
        Ship ship=new Ship();
        if(!res.first())
        {
            return ship;
        }
        
        ship.setAddress(this.userSQL.getAddress(res.getInt("addressId"))).setShipId(res.getInt("shipId")).setDate(res.getTimestamp("date"))
                .setShipType(getShipType(res.getInt("shipTypeId"))).setStatus(res.getInt("status"));
        return ship;
    }
        
    /**
     * 
     * @param shipId
     * @return true if it exists
     * @throws SQLException 
     */
    public boolean shipIdExists(int shipId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `ship` WHERE `shipId`=?");
        prep.setInt(1, shipId);
        return prep.executeQuery().first();
    }
        
    /**
     * adds a ship to data base
     * @param shipTypeId
     * @param addressId
     * @return the id of the added ship
     * @throws SQLException 
     */
    public int addShip(int shipTypeId,int addressId) throws SQLException
    {
        int shipId;
        Random random=new Random();
        while(true)
        {
            shipId=Math.abs(random.nextInt());
            if(!shipIdExists(shipId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `ship` (`shipTypeId`,`addressId`,`shipId`) VALUES (?,?,?)");
        prep.setInt(1, shipTypeId);
        prep.setInt(2, addressId);
        prep.setInt(3, shipId);
        prep.execute();
        return shipId;
    }  
    /**
     * retrieves all order item properties attached to a item 
     * @param itemId
     * @return a linked list of order item property
     * @throws SQLException 
     */
    public LinkedList<ItemProperty> getOrderItemProperties(int itemId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_item_property` WHERE `itemId`=? AND `status`!=-1");
        prep.setInt(1, itemId);
        ResultSet res=prep.executeQuery();
        LinkedList<ItemProperty> properties=new LinkedList<ItemProperty>();
        
        while(res.next())
        {
            int propertyId=res.getInt("propertyId");
            ItemProperty property=new ItemProperty();
            property.setItemId(itemId).setItemPropertyId(res.getInt("itemPropertyId"))
                    .setProperty(getProperty(propertyId))
                    .setPropertyvaValues(getPropertyValues(itemId,propertyId));
            properties.add(property);
        }
        return properties;
    }
    
    public LinkedList<PropertyValue> getPropertyValues(int itemId, int propertyId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_item_property` WHERE `itemId`=? AND `propertyId`=? AND `status`!=-1");
        prep.setInt(1, itemId);
        prep.setInt(2, propertyId);
        ResultSet res=prep.executeQuery();
        LinkedList<PropertyValue> values=new LinkedList<PropertyValue>();
        while(res.next())
        {
            values.add(getPropertyValue(res.getInt("valueId")));
        }
        return values;
    }
        
    
    /**
     * retrieves 1 item property
     * @param itemPropertyId
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public ItemProperty getOrderItemProperty(int itemPropertyId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_item_property` WHERE `itemPropertyId`=?");
        prep.setInt(1, itemPropertyId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            throw new DoesntExistException("itemproperty");
        }
        int propertyId=res.getInt("propertyId");
        ItemProperty property=new ItemProperty();
        property.setItemId(res.getInt("itemId")).setItemPropertyId(itemPropertyId)
                .setProperty(getProperty(propertyId))
                .setPropertyvaValues(getPropertyValues(res.getInt("itemId"),propertyId));
        return property;
    }
       
    /**
     * 
     * @param itemPropertyId
     * @return true if it exists
     * @throws SQLException 
     */
    public boolean orderItemPropertyIdExists(int itemPropertyId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_item_property` WHERE `itemPropertyId`=? ");
        prep.setInt(1, itemPropertyId);
        return prep.executeQuery().first();
    }
    
    public int addOrderItemProperty(int itemId,int propertyId,int valueId) throws SQLException
    {
        int itemPropertyId;
        Random random=new Random();
        while(true)
        {
            itemPropertyId=Math.abs(random.nextInt());
            if(!orderItemPropertyIdExists(itemPropertyId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `order_item_property` (`itemId`,`itemPropertyId`,`propertyId`,`valueId`) VALUES (?,?,?,?)");
        prep.setInt(1, itemId);
        prep.setInt(2, itemPropertyId);
        prep.setInt(3, propertyId);
        prep.setInt(4, valueId);
        prep.execute();
        return itemPropertyId;
    }
    
    
    /**
     * gets one propertyvalue from data base
     * @param propertyValueId
     * @return a property value object
     * @throws SQLException 
     */
    private PropertyValue getPropertyValue(int propertyValueId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_property_value` WHERE `valueId`=? ");
        prep.setInt(1, propertyValueId);
        ResultSet res=prep.executeQuery();
        PropertyValue value=new PropertyValue();
        if(!res.first())
        {
            return value;
        }
        value.setName(res.getString("name")).setPriceByScale(res.getInt("priceByScale")).setValueId(res.getInt("valueId"));
        return value;
    }
        
    /**
     * retrieves a property without inside values
     * @param propertyId
     * @return a property objcet
     * @throws SQLException 
     */
    private Property getProperty(int propertyId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_property WHERE `propertyId`=?");
        prep.setInt(1, propertyId);
        ResultSet res=prep.executeQuery();
        res.first();
        
        Property property=new Property();
        property.setName(res.getString("name")).setPropertyId(res.getInt("propertyId"))
                    .setStatus(res.getInt("status")).setMultiple(res.getBoolean("multiple"))
                .setDescription(res.getString("description"));
        return property;
        
    }
    /**
     * retrieves all order items attached to a orderId
     * @param orderId
     * @return a linked list of OrderItem
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Item> getOrderItems(int partId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_item` WHERE `partId`=? AND `status`!=-1");
        prep.setInt(1, partId);
        ResultSet res=prep.executeQuery();
        LinkedList<Item> items=new LinkedList<Item>();
        while(res.next())
        {
            int itemId=res.getInt("itemId");
            Item item=new Item();
            item.setCount(res.getInt("count"))
                    .setItemProperties(getOrderItemProperties(itemId))
                    .setPartId(partId)
                    .setStatus(res.getInt("status"));
            items.add(item);
        }
        return items;
    }
    /**
     * retrieves 1 product without inside detail
     * @param productId
     * @return a product object
     * @throws SQLException
     * @throws DoesntExistException 
     */    
    public Product getProduct(int productId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product` WHERE `productId`=? AND `status`=-1");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
             throw new DoesntExistException("product"); 
        }
            
        Product product=new Product();
        product.setProductId(productId).setName(res.getString("name"))
                    .setBasePrice(res.getInt("basePrice"))
                    .setDescription(res.getString("description"))
                    .setPicture(this.productSQL.getProductPictures(productId))
                    .setCategory(this.productSQL.getCategory(res.getInt("categoryId")))
                    .setStatus(res.getInt("status"));
        return product;
    }
    
    /**
     * retrieves 1 order item
     * @param itemId
     * @return an orderitem object
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public Item getOrderItem(int itemId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_item` WHERE `itemId`=? ");
        prep.setInt(1, itemId);
        ResultSet res=prep.executeQuery();
        if(!res.next())
        {
            throw new DoesntExistException("orderitem");
        }
        Item item=new Item();
        item.setCount(res.getInt("count"))
                .setItemProperties(getOrderItemProperties(itemId))
                .setPartId(res.getInt("partId"))
                .setStatus(res.getInt("status"));
        return item;
    } 
        
    /**
     * 
     * @param orderItemId
     * @return true if it does
     * @throws SQLException 
     */
    public boolean orderItemIdExists(int orderItemId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `order_item` WHERE `itemId`=?");
        prep.setInt(1, orderItemId);
        return prep.executeQuery().first();
    }
    /**
     * adds an order item into data base 
     * @param orderId
     * @param productId
     * @param count
     * @param finalPrice
     * @return the id of the added order item
     * @throws SQLException 
     */
    public int addOrderItem(int partId,int count) throws SQLException
    {
        int itemId;
        Random random=new Random();
        while(true)
        {
            itemId=Math.abs(random.nextInt());
            if(!orderItemIdExists(itemId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `order_item` (`partId`,`itemId`,`count`) VALUES (?,?,?)");
        prep.setInt(1, partId);
        prep.setInt(2, itemId);
        prep.setInt(3, count);
        
        prep.execute();
        
        return itemId;
    }

    /**
     * retrieves all order receipts  attached to a user
     * @param userId
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Receipt> getOrderReceipts(int userId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_receipt` WHERE `userId`=? AND `status`!=-1");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<Receipt> receipts=new LinkedList<Receipt>();
        while(res.next())
        {
            int orderId=res.getInt("orderId");
            Receipt receipt=new Receipt();
            receipt.setDate(res.getTimestamp("date")).setOderId(orderId)
                    .setDiscount(this.userSQL.getDiscount(res.getInt("discountId")))
                    .setParts(getParts(orderId)).setPayment(getPayment(res.getInt("paymentId")))
                    .setShip(getShip(res.getInt("shipId"))).setStatus(res.getInt("status"))
                    .setUser(this.userSQL.getUserFull(res.getInt("userId")));
            receipts.add(receipt);
        }
        return receipts;
    }
    
    /**
     * retrieves all order receipts  attached to a user
     * @param userId
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Receipt> getOrderReceiptsDone(int userId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_receipt` WHERE `userId`=? AND `status`>0 ");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<Receipt> receipts=new LinkedList<Receipt>();
        while(res.next())
        {
            int orderId=res.getInt("orderId");
            Receipt receipt=new Receipt();
            receipt.setDate(res.getTimestamp("date")).setOderId(orderId)
                    .setDiscount(this.userSQL.getDiscount(res.getInt("discountId")))
                    .setParts(getParts(orderId)).setPayment(getPayment(res.getInt("paymentId")))
                    .setShip(getShip(res.getInt("shipId"))).setStatus(res.getInt("status"))
                    .setUser(this.userSQL.getUserFull(res.getInt("userId")));
            receipts.add(receipt);
        }
        return receipts;
    }
    
    /**
     * retrieves 1 orderReceipt from database
     * @param orderId
     * @return an order receipt object
     * @throws SQLException
     * @throws DoesntExistException if it doesnt exists with the given id
     */
    public Receipt getOrderReceipt(int orderId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_receipt` WHERE `orderId`=? ");
        prep.setInt(1, orderId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            throw new DoesntExistException("orderreceipt");
        }
        Receipt receipt=new Receipt();
        receipt.setDate(res.getTimestamp("date")).setOderId(orderId)
                .setDiscount(this.userSQL.getDiscount(res.getInt("discountId")))
                    .setParts(getParts(orderId)).setPayment(getPayment(res.getInt("paymentId")))
                    .setShip(getShip(res.getInt("shipId"))).setStatus(res.getInt("status"))
                    .setUser(this.userSQL.getUserFull(res.getInt("userId")));
        return receipt;
    }
    
    /**
     * 
     * @param orderId
     * @return true if it exists
     * @throws SQLException 
     */
    public boolean orderReceiptIdExists(int orderId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `order_receipt` WHERE `orderId`=?");
        prep.setInt(1, orderId);
        return prep.executeQuery().first();
    }
        
    /**
     * adds a order receipt to data base 
     * @param userId
     * @return id of the added order receipt
     * @throws SQLException 
     */
    public int addOrderReceipt(int userId) throws SQLException
    {
        int orderId;
        Random random=new Random();
        while(true)
        {
            orderId=Math.abs(random.nextInt());
            if(!orderReceiptIdExists(orderId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `order_receipt` (`orderId`,`userId`) VALUES (?,?)");
        prep.setInt(1, orderId);
        prep.setInt(2, userId);
        prep.execute();
        return orderId;
    }
            
    /**
     * retrieves all parts of a order receipt
     * @param orderId
     * @return a linked list of part
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Part> getParts(int orderId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `order_part`.* , `product_scale_value`.`scaleId` FROM `order_part` " +
"LEFT OUTER JOIN `product_scale_value` ON `order_part`.`scaleValueId`=`product_scale_value`.`valueId` " +
"WHERE `order_part`.`orderId`=? AND `order_part`.`status`!=-1");
        prep.setInt(1, orderId);
        ResultSet res=prep.executeQuery();
        LinkedList<Part> parts=new LinkedList<Part>();
        while(res.next())
        {
            int partId=res.getInt("partId");
            int scaleValueId=res.getInt("scaleValueId");
            Part part=new Part();
            part.setCount(res.getInt("count")).setFinalPrice(res.getInt("finalPrice")).setItems(getOrderItems(partId))
                    .setScalePackage(getScalePackage(res.getInt("packageId")))
                    .setOrderId(orderId).setPartId(partId)
//                    .setProduct(this.productSQL.getProductOnlyMain(res.getInt("productId")))
                    .setScaleValue(this.productSQL.getScaleValue(scaleValueId))
                    .setScale(this.productSQL.getScaleOnlyDetail(res.getInt("scaleId")))
                    .setStatus(res.getInt("status"))
                    .setWindow(res.getInt("window"));
            if(part.isWindow())
            {
                part.setProductWindow(this.productSQL.getProductWindow(res.getInt("productId")));
            }else
            {
               part.setProduct(this.productSQL.getProductOnlyMain(res.getInt("productId")));
            }
            parts.add(part);
        }
            return parts;
    }
    /**
     * retrieves one part
     * @param partId
     * @return a part object
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public Part getPart(int partId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `order_part`.* , `product_scale_value`.`scaleId` FROM `order_part` " +
"INNER JOIN `product_scale_value` ON `order_part`.`scaleValueId`=`product_scale_value`.`valueId` " +
"WHERE `order_part`.`partId`=?");
        prep.setInt(1, partId);
        ResultSet res=prep.executeQuery();
        Part part=new Part();
        if(!res.first())
        {
            return part;
        }
        int scaleValueId=res.getInt("scaleValueId");
        part.setCount(res.getInt("count")).setFinalPrice(res.getInt("finalPrice")).setItems(getOrderItems(partId))
                .setOrderId(res.getInt("orderId")).setPartId(partId).setProduct(this.productSQL.getProductOnlyMain(partId))
                .setScaleValue(this.productSQL.getScaleValue(scaleValueId))
                .setScale(this.productSQL.getScaleOnlyDetail(res.getInt("scaleId")))
                .setStatus(res.getInt("status"))
                .setWindow(res.getInt("window"));
        return part;
    }
    
    /**
     * updates a ship in data base
     * @param shipId
     * @param shipTypeId
     * @param status
     * @throws SQLException 
     */
    public void editShip(int shipId,int shipTypeId,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `ship` SET `shipTypeId`=? , `status`=? WHERE `shipId`=?");
        prep.setInt(1, shipTypeId);
        prep.setInt(2, status);
        prep.setInt(3, shipId);
        prep.execute();
    }
    /**
     * updates a payment in data base
     * @param paymentId
     * @param status
     * @param approve
     * @param paymentTypeId
     * @throws SQLException 
     */
    public void editPayment(int paymentId,int status,String approve,int paymentTypeId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `payment` SET `paymentTypeId`=? , `approve`=?, `status`=? WHERE `paymentId`=?");
        prep.setInt(1, paymentTypeId);
        prep.setString(2, approve);
        prep.setInt(3, status);
        prep.setInt(4, paymentId);
        prep.execute();
    }
    
    /**
     * updates a receipt in data base
     * @param orderId
     * @param status
     * @throws SQLException 
     */
    public void editReceipt(int orderId,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `order_receipt` SET `status`=? WHERE `orderId`=?");
        prep.setInt(1, status);
        prep.setInt(2, orderId);
        prep.execute();
    }
    
    /**
     * retrieves a package based on its id 
     * @param scalePackageId
     * @return a ScalePackage object
     * @throws SQLException 
     */
    public ScalePackage getScalePackage(int scalePackageId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `scale_package` WHERE `packageId`=?");
        prep.setInt(1, scalePackageId);
        ResultSet res=prep.executeQuery();
        ScalePackage scalePackage=new ScalePackage();
        if(!res.first())
        {
            return scalePackage;
        }
        scalePackage.setDescription(res.getString("description")).setName(res.getString("name")).setPackageId(scalePackageId)
                    .setPicture(res.getString("picture")).setPrice(res.getInt("price"))
                    .setScaleId(res.getInt("scaleValueId")).setStatus(res.getInt("status"));
        
        return scalePackage;
    }
    
    /**
     * updates the status of the ship type to -1
     * @param shipTypeId the id of the ship type that is being deleted
     * @throws SQLException 
     */
    public void deleteShipType(int shipTypeId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `ship_type` SET `status`=-1 WHERE `shipTypeId`=?");
        prep.setInt(1, shipTypeId);
        prep.execute();
    }
    
    /**
     * shows if the name of the ship type already exists or not
     * @param name
     * @return true if it exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean shipTypeExists(String name) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `ship_type` WHERE `name`=?");
        prep.setString(1, name);
        ResultSet res=prep.executeQuery();
        return res.first();
    }
    
    /**
     * updates the status of a payment type
     * @param paymentTypeId
     * @param status -1:deleted/ 0:Deactivated / 1:Active
     * @throws SQLException 
     */
    public void updatePaymentType(int paymentTypeId,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `payment_type` SET `status`=? WHERE `paymentTypeId`=?");
        prep.setInt(1, status);
        prep.setInt(2, paymentTypeId);
        prep.execute();
    }
    
    /**
     * shows if the part id already exists or not 
     * @param partId
     * @return
     * @throws SQLException 
     */
    public boolean partIdExists(int partId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `order_part` WHERE `partId`=?");
        prep.setInt(1, partId);
        return prep.executeQuery().first();
    }
    /**
     * adds a part to data base which the product isn't a window product 
     * @param orderId
     * @param productId
     * @param scaleValueId
     * @param packageId
     * @param count
     * @return
     * @throws SQLException 
     */
    public int addPart(int orderId,int productId,int scaleValueId,int packageId,int count) throws SQLException
    {
        int partId;
        Random random=new Random();
        while(true)
        {
            partId=Math.abs(random.nextInt());
            if(!partIdExists(partId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `order_part` (`orderId`,`partId`,`productId`,`scaleValueId`,`packageId`,`count`,`window`) VALUES (?,?,?,?,?,?,?)");
        prep.setInt(1, orderId);
        prep.setInt(2, partId);
        prep.setInt(3, productId);
        prep.setInt(4, scaleValueId);
        prep.setInt(5, packageId);
        prep.setInt(6, count);
        prep.setBoolean(7, false);
        prep.execute();
        return partId;
    }
    
    /**
     * adds a part to data base which is a window product
     * @param orderId
     * @param productId
     * @param count
     * @return
     * @throws SQLException 
     */
    public int addPart(int orderId,int productId,int count) throws SQLException
    {
        int partId;
        Random random=new Random();
        while(true)
        {
            partId=Math.abs(random.nextInt());
            if(!partIdExists(partId))
                break;
        }
         PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `order_part` (`orderId`,`partId`,`productId`,`count`,`window`) VALUES (?,?,?,?,?)");
        prep.setInt(1, orderId);
        prep.setInt(2, partId);
        prep.setInt(3, productId);
        prep.setInt(4, count);
        prep.setBoolean(5, true);
        prep.execute();
        return partId;
    }
    
    public User getUserDetailSms(int orderId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `users`.`userId`,`users`.`phoneNumber`,`users`.`email` FROM `users` " +
"INNER JOIN `order_receipt`  " +
"ON `order_receipt`.`userId`=`users`.`userId` " +
"WHERE `order_receipt`.`orderId`=?");
        prep.setInt(1, orderId);
        ResultSet res=prep.executeQuery();
        User user=new User();
        if(!res.first())
        {
            return user;
        }
        
        user.setUserId(res.getInt("userId")).setPhoneNumber(res.getString("phoneNumber")).setEmail(res.getString("email")); 
        return user;
        
    }
    
    
    
    /**
     * gets the last order receipt representing the open un submitted cart and if there wasn't any, it will add one and return that one
     * @param userId
     * @return the order if of the open cart
     * @throws SQLException 
     */
    public int getCartOrderId(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_receipt` WHERE `userId`=? AND `status`=0");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return addOrderReceipt(userId);
        }
        return res.getInt("orderId");
    }
    
    /**
     * changes the status of the part to -1
     * @param partId
     * @throws SQLException 
     */
    public void deletePart(int partId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `order_part` SET `status`=-1 WHERE `partId`=?");
        prep.setInt(1, partId);
        prep.execute();
    }
    
    /**
     * shows if the part given is form the user or not
     * @param userId
     * @param partId
     * @return
     * @throws SQLException 
     */
    public boolean isPartFromUser(int userId,int partId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `order_part`.partId FROM `order_receipt` " +
"INNER JOIN `order_part` " +
"ON `order_part`.orderId=`order_receipt`.orderId " +
"WHERE `order_receipt`.userId=? " +
"AND `order_receipt`.status=0 " +
"AND `order_part`.partId=?");
        
        prep.setInt(1, userId);
        prep.setInt(2, partId);
        return prep.executeQuery().first();
    }
    
    /**
     * returns empty cart order id and if there wasn't one it returns -1
     * @param userId
     * @return the order id and if there wasn't one returns -1
     * @throws SQLException 
     */
    public int getEmptyCartId(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `orderId` FROM `order_receipt` WHERE `status`=0 AND `userId`=? ");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return -1;
        }
        
        return res.getInt("orderId");
    }
    
    /**
     * sets the value 'finalPrice' and changes the status of the part to 1
     * @param partId
     * @param finalPrice
     * @throws SQLException 
     */
    public void setPartFinalPrice(int partId,int finalPrice) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `order_part` SET `finalPrice`=?,`status`=1 WHERE `partId`=?");
        prep.setInt(1, finalPrice);
        prep.setInt(2, partId);
        prep.execute();
    }
    
    /**
     * completes the order by setting it's order and payment id and changing it's status to 1
     * @param orderId
     * @param shipId
     * @param paymentId
     * @throws SQLException 
     */
    public void setOrderComplete(int orderId,int shipId,int paymentId,int discountId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `order_receipt` SET `paymentId`=?,`shipId`=?,`status`=1,`date`=?,`discountId`=? WHERE `orderId`=?");
        prep.setInt(1, paymentId);
        prep.setInt(2, shipId);
        prep.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        prep.setInt(4, discountId);
        prep.setInt(5, orderId);
        prep.execute();
    }
    
    public boolean isDiscountValid(int id,int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `discount_ticket` WHERE `id`=? AND `userId`=? AND `status`=0");
        prep.setInt(1, id);
        prep.setInt(2, userId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return false;
        }
        
        if(res.getTimestamp("expireDate").getTime()<System.currentTimeMillis())
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * shows if the product adding to cart has already been there or not
     * @param orderId
     * @param productId
     * @return if it's repeated it returns the part id and if not it returns -1
     * @throws SQLException 
     */
    public int isPartRepeated(int orderId,int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_part` WHERE `orderId`=? AND `productId`=? AND `status`!=-1");
        prep.setInt(1, orderId);
        prep.setInt(2, productId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return -1;
        }
        
        return res.getInt("partId");
    }
    
    /**
     * increases the count of an already added part
     * @param partId
     * @param count
     * @throws SQLException 
     */
    public void addCountToPart(int partId,int count) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `order_part` SET `count`=`count`+? WHERE `partId`=?");
        prep.setInt(1, count);
        prep.setInt(2, partId);
        prep.execute();
    }
}

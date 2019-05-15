/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import ExceptionsChi.DoesntExistException;
import Objects.InPerson.InPerson;
import Objects.InPerson.InPersonItem;
import Objects.Order.Item;
import Objects.Order.ItemProperty;
import Objects.Order.Part;
import Objects.Order.Receipt;
import Objects.Payment.Payment;
import Objects.Payment.PaymentType;
import Objects.Product.Product;
import Objects.Product.Property;
import Objects.Product.PropertyValue;
import Objects.Product.ScalePackage;
import Objects.Ship.Ship;
import Objects.Ship.ShipType;
import Objects.User.UserAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Moses
 */
public class OrderHistorySQLCommands {
    private Connection connection;
    private UserSQLCommands userSQL;
    private ProductSQLCommands productSQL;
    
    public OrderHistorySQLCommands(Connection connection)
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment_type` WHERE `paymentTypeId`=?  ");
        prep.setInt(1, paymentTypeId);
        ResultSet res=prep.executeQuery();
        PaymentType paymentType=new PaymentType();
        if(!res.first())
        {
            return paymentType;
        }
        
        
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment_type` ");
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment`  ");
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment` WHERE `paymentId`=?  ");
        prep.setInt(1, paymentId);
        ResultSet res=prep.executeQuery();
        Payment payment=new Payment();
        if(!res.first())
        {
            return payment.setPaymentType(new PaymentType());
        }
        
        payment.setApprove(res.getString("approve")).setPaymentId(res.getInt("paymentId")).setDate(res.getTimestamp("date"))
                .setPaymentType(getPaymentType(res.getInt("paymentTypeId")))
                .setPrice(res.getInt("price")).setStatus(res.getInt("status"));
            
            return payment;
    }
    
   
    
    
    
        
    /**
     * retrieves all ship types
     * @return a linkedlist of ship type
     * @throws SQLException 
     */
    public LinkedList<ShipType> getShipTypes() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareCall("SELECT * FROM `ship_type`  ");
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `ship_type` WHERE `shipTypeId`=?  ");
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
     * retrievs all ships
     * @return a linekd list of ship
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Ship> getShips() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `ship`  ");
        ResultSet res=prep.executeQuery();
        LinkedList<Ship> ships=new LinkedList<Ship>();
        while(res.next())
        {
            Ship ship=new Ship();
            ship.setAddress(this.userSQL.getAddress(res.getInt("addressId")))
                    .setShipId(res.getInt("shipId")).setDate(res.getTimestamp("date"))
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
            return ship.setAddress(new UserAddress()).setShipType(new ShipType());
        }
        
        ship.setAddress(this.userSQL.getAddress(res.getInt("addressId"))).setShipId(res.getInt("shipId")).setDate(res.getTimestamp("date"))
                .setShipType(getShipType(res.getInt("shipTypeId"))).setStatus(res.getInt("status"));
        return ship;
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
        int propertyId=0,temp=0;
        while(res.next())
        {
            propertyId=res.getInt("propertyId");
            if(temp==propertyId)
                continue;
            ItemProperty property=new ItemProperty();
            property.setItemId(itemId).setItemPropertyId(res.getInt("itemPropertyId"))
                    .setProperty(getProperty(propertyId))
                    .setPropertyvaValues(getPropertyValues(itemId,propertyId));
            properties.add(property);
            temp=propertyId;
        }
        return properties;
    }
    public LinkedList<PropertyValue> getPropertyValues(int itemId,int propertyId) throws SQLException
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_item_property` WHERE `itemPropertyId`=?  ");
        prep.setInt(1, itemPropertyId);
        ResultSet res=prep.executeQuery();
        ItemProperty property=new ItemProperty();
        if(!res.first())
        {
            return property;
        }
        int propertyId=res.getInt("propertyId");
        
        property.setItemId(res.getInt("itemId")).setItemPropertyId(itemPropertyId)
                .setProperty(getProperty(propertyId))
                .setPropertyvaValues(getPropertyValues(res.getInt("itemId"), propertyId));
        return property;
    }
       
    
    /**
     * gets one propertyvalue from data base
     * @param propertyValueId
     * @return a property value object
     * @throws SQLException 
     */
    private PropertyValue getPropertyValue(int propertyValueId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_property_value` WHERE `valueId`=?  ");
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_property WHERE `propertyId`=?  ");
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
                    .setPartId(partId).setItemId(itemId)
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product` WHERE `productId`=?");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        Product product=new Product();
        if(!res.first())
        {
             return product;
        }
            
        
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_item` WHERE `itemId`=?  ");
        prep.setInt(1, itemId);
        ResultSet res=prep.executeQuery();
        Item item=new Item();
        if(!res.next())
        {
            return item;
        }
        
        item.setCount(res.getInt("count"))
                .setItemProperties(getOrderItemProperties(itemId))
                .setPartId(res.getInt("partId"))
                .setStatus(res.getInt("status"));
        return item;
    } 
        

    /**
     * retrieves all order receipts  attached to a user
     * @param userId
     * @param status
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Receipt> getOrderReceipts(int userId,int status) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_receipt` WHERE `userId`=? AND `status`=? ORDER BY `id` DESC");
        prep.setInt(1, userId);
        prep.setInt(2, status);
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
    public LinkedList<Receipt> getOrderReceipts(int userId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_receipt` WHERE `userId`=? AND `status`>0 ORDER BY `id` DESC");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<Receipt> receipts=new LinkedList<Receipt>();
        while(res.next())
        {
            int orderId=res.getInt("orderId");
            Receipt receipt=new Receipt();
            receipt.setDate(res.getTimestamp("date")).setOderId(orderId)
                    .setParts(getParts(orderId)).setDiscount(this.userSQL.getDiscount(res.getInt("discountId")))
                    .setPayment(getPayment(res.getInt("paymentId")))
                    .setShip(getShip(res.getInt("shipId"))).setStatus(res.getInt("status"))
                    .setUser(this.userSQL.getUserFull(res.getInt("userId")));
            receipts.add(receipt);
        }
        return receipts;
    }
    
    /**
     * retrieves all order receipts  attached to a user
     * @param userId
     * @param status
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Receipt> getOrderReceipts() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_receipt` WHERE `status`>0 ORDER BY `id` DESC");
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
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `order_receipt` WHERE `orderId`=?  ");
        prep.setInt(1, orderId);
        ResultSet res=prep.executeQuery();
        Receipt receipt=new Receipt();
        if(!res.first())
        {
            return receipt;
        }
        
        receipt.setDate(res.getTimestamp("date")).setOderId(orderId).setDiscount(this.userSQL.getDiscount(res.getInt("discountId")))
                    .setParts(getParts(orderId)).setPayment(getPayment(res.getInt("paymentId")))
                    .setShip(getShip(res.getInt("shipId"))).setStatus(res.getInt("status"))
                    .setUser(this.userSQL.getUserFull(res.getInt("userId")));
        return receipt;
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
"WHERE `order_part`.`orderId`=? AND `order_part`.`status`!=-1 ");
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
                .setScalePackage(getScalePackage(res.getInt("packageId")))
                .setOrderId(res.getInt("orderId")).setPartId(partId).setProduct(this.productSQL.getProductOnlyMain(res.getInt("productId")))
                .setScaleValue(this.productSQL.getScaleValue(scaleValueId))
                .setScale(this.productSQL.getScaleOnlyDetail(res.getInt("scaleId")))
                .setStatus(res.getInt("status"))
                .setWindow(res.getInt("window"));
        
        return part;
    }
    
    /**
     * retrieves a package based on its id 
     * @param scalePackageId
     * @return a ScalePackage object
     * @throws SQLException 
     */
    public ScalePackage getScalePackage(int scalePackageId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `scale_package` WHERE `packageId`=? ");
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
     * retrieves all in person items attached to a inperson
     * @param inPersonId
     * @return a linked list of inpersonitem
     * @throws SQLException 
     */
    public LinkedList<InPersonItem> getInPersonItems(int inPersonId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `inperson_item` WHERE `inpersonId`=?");
        prep.setInt(1, inPersonId);
        ResultSet res=prep.executeQuery();
        LinkedList<InPersonItem> items=new LinkedList<InPersonItem>();
        while(res.next())
        {
            InPersonItem item=new InPersonItem();
            item.setDescription(res.getString("description")).setInpersonId(inPersonId)
                    .setItemId(res.getInt("itemId")).setPrice(res.getInt("price"))
                    .setStatus(res.getInt("status"));
            items.add(item);
        }
        return items;
    }
    
    /**
     * retrieves all inpersons attached to a user
     * @param userId
     * @return a linked list of inperson
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<InPerson> getInpersons(int userId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `inperson` WHERE `userId`=? AND `status`!=-1");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<InPerson> inpersons=new LinkedList<InPerson>();
        while(res.next())
        {
            int inpersonId=res.getInt("inpersonId");
            InPerson inperson=new InPerson();
            inperson.setDate(res.getTimestamp("date")).setUser(this.userSQL.getUserFull(res.getInt("userId")))
                    .setInpersonId(inpersonId)
                    .setItems(getInPersonItems(inpersonId)).setStatus(res.getInt("status"));
            inpersons.add(inperson);
        }
        return inpersons;
    }
            
    /**
     * retrieves one inperson
     * @param inpersonId the id of the inperson
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public InPerson getInperson(int inpersonId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `inperson` WHERE `inpersonId`=?");
        prep.setInt(1, inpersonId);
        ResultSet res=prep.executeQuery();
        InPerson inperson=new InPerson();
        if(!res.first())
        {
            return inperson;
        }
            inperson.setDate(res.getTimestamp("date")).setUser(this.userSQL.getUserFull(res.getInt("userId")))
                    .setInpersonId(inpersonId)
                    .setItems(getInPersonItems(inpersonId)).setStatus(res.getInt("status"));
                    
            return inperson;
        
    }
    
    /**
     * shows if person id exists or not 
     * @param inpersonId
     * @return true if yes
     * @throws SQLException 
     */
    public boolean inPersonIdExists(int inpersonId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `inperson` WHERE `inpersonId`=?");
        prep.setInt(1, inpersonId);
        return prep.executeQuery().first();
    }
    /**
     * shows if item id exists or not 
     * @param itemId
     * @return true if yes
     * @throws SQLException 
     */
    public boolean inPersonItemIdExists(int itemId) throws SQLException
    {
       PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `inperson_item` WHERE `itemId`=?");
        prep.setInt(1, itemId);
        return prep.executeQuery().first(); 
    }
    
    /**
     * adds an inperson to data base
     * @param userId
     * @return the id of the added inperson
     * @throws SQLException 
     */
    public int addInPerson(int userId) throws SQLException
    {
        int inpersonId;
        Random random=new Random();
        while(true)
        {
            inpersonId=Math.abs(random.nextInt());
            if(!inPersonIdExists(inpersonId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `inperson` (`inpersonId`,`userId`) VALUES (?,?)");
        prep.setInt(1, inpersonId);
        prep.setInt(2, userId);
        prep.execute();
        return inpersonId;
    }
    /**
     * adds an inperson item to data base
     * @param inpersonId
     * @param description
     * @param price
     * @return the id of the added inperson
     * @throws SQLException 
     */
    public int addInPersonItem(int inpersonId,String description,int price) throws SQLException
    {
         int itemId;
        Random random=new Random();
        while(true)
        {
            itemId=Math.abs(random.nextInt());
            if(!inPersonItemIdExists(itemId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `inperson_item` (`inpersonId`,`itemId`,`description`,`price`) VALUES (?,?,?,?)");
        prep.setInt(1, inpersonId);
        prep.setInt(2, itemId);
        prep.setString(3, description);
        prep.setInt(4, price);
        prep.execute();
        return itemId;
    }

    /**
     * deletes an item
     * @param itemId
     * @throws SQLException 
     */
    public void deleteInPersonItem(int itemId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM `inperson_item` WHERE `itemId`=?");
        prep.setInt(1, itemId);
        prep.execute();
        
    }
    /**
     * adds a discount
     * @param userId
     * @param effect
     * @param expire
     * @throws SQLException 
     */
    public void addDiscount(int userId,double effect,Timestamp expire) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `discount_ticket` (`userId`,`effect`,`expireDate`) VALUES (?,?,?)");
        prep.setInt(1, userId);
        prep.setDouble(2, effect);
        prep.setTimestamp(3, expire);
        prep.execute();
    }
    /**
     * changes the status of the discount to -1
     * @param id
     * @throws SQLException 
     */
    public void deleteDiscount(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `discount_ticket` SET `status`=-1 WHERE `id`=?");
        prep.setInt(1, id);
        prep.execute();
    }
    
    /**
     * changes the status of the inperson to -1
     * @param inpersonId
     * @throws SQLException 
     */
    public void deleteInperson(int inpersonId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `inperson` SET `status`=-1 WHERE `inpersonId`=?");
        prep.setInt(1, inpersonId);
        prep.execute();
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
     * shows if ship type is valid or not
     * @param shipTypeId
     * @return
     * @throws SQLException 
     */
    public boolean isShipTypeValid(int shipTypeId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `ship_type` WHERE `shipTypeId`=? AND `status`!=-1");
        prep.setInt(1, shipTypeId);
        return prep.executeQuery().first();
    }
    
    /**
     * shows if payment type is valid or not
     * @param paymentTypeId
     * @return
     * @throws SQLException 
     */
    public boolean isPaymentTypeValid(int paymentTypeId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `payment_type` WHERE `paymentTypeId`=? AND `status`=1");
        prep.setInt(1, paymentTypeId);
        return prep.executeQuery().first();
    }
}

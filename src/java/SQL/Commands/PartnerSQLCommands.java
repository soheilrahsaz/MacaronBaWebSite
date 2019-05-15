/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import ExceptionsChi.DoesntExistException;
import Objects.Partner.Partner;
import Objects.Partner.PartnerCategory;
import Objects.Partner.PartnerPart;
import Objects.Partner.PartnerProduct;
import Objects.Partner.PartnerReceipt;
import Objects.Partner.PartnerSeries;
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
 *
 * @author Moses
 */
public class PartnerSQLCommands {
    private Connection connection;
    private ProductSQLCommands productSQL;
    private OrderHistorySQLCommands orderHisSQL;
    public PartnerSQLCommands(Connection connection)
    {
        this.connection=connection;
        this.productSQL=new ProductSQLCommands(connection);
        this.orderHisSQL=new OrderHistorySQLCommands(connection);
    }
    
    /**
     * retrieves all partners except the deleted ones
     * @return a linked list of partners  
     * @throws SQLException 
     */
    public LinkedList<Partner> getPartners() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Partner> partners=new LinkedList<Partner>();
        while(res.next())
        {
            Partner partner=new Partner();
            partner.setPartnerId(res.getInt("partnerId")).setName(res.getString("name")).setPhoneNumber(res.getString("phoneNumber"))
                    .setEmail(res.getString("email")).setDateRegistered(res.getTimestamp("dateRegistered"))
                    .setSeries(getPartnerSeries(res.getInt("series")))
                    .setStatus(res.getInt("status"));
            partners.add(partner);
        }
        return partners;
    }
    
    /**
     * retrieves one parnter
     * @param partnerId
     * @return
     * @throws SQLException 
     */
    public Partner getPartner(int partnerId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner` WHERE `partnerId`=?");
        prep.setInt(1, partnerId);
        ResultSet res=prep.executeQuery();
        Partner partner=new Partner();
        if(!res.first())
        {
            return partner;
        }
        partner.setPartnerId(res.getInt("partnerId")).setName(res.getString("name")).setPhoneNumber(res.getString("phoneNumber"))
                .setEmail(res.getString("email")).setDateRegistered(res.getTimestamp("dateRegistered"))
                .setSeries(getPartnerSeries(res.getInt("series")))
                .setStatus(res.getInt("status"));
        return partner;
    }
    
    /**
     * used for logging in 
     * @param userName
     * @param password hashed password
     * @return a partner object representing the logged in client
     * @throws SQLException
     * @throws DoesntExistException if no partner exists with this intel
     */
    public Partner getPartner(String userName,String password) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner` WHERE `phoneNumber`=? AND `password`=?");
        prep.setString(1, userName);
        prep.setString(2, password);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            throw new DoesntExistException("partner");
        }
        Partner partner=new Partner();
        partner.setPartnerId(res.getInt("partnerId")).setName(res.getString("name")).setPhoneNumber(res.getString("phoneNumber"))
                .setEmail(res.getString("email")).setDateRegistered(res.getTimestamp("dateRegistered"))
                .setSeries(getPartnerSeries(res.getInt("series")))
                .setStatus(res.getInt("status"));
        return partner;
    }
    
    /**
     * shows if partner id exists or not 
     * @param parnerId
     * @return
     * @throws SQLException 
     */
    public boolean partnerIdExist(int parnerId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner` WHERE `partnerId`=?");
        prep.setInt(1, parnerId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds a partner to data base 
     * @param name
     * @param phoneNumber
     * @param password
     * @return the id of the added partner 
     * @throws SQLException 
     */
    public int addPartner(String name,String phoneNumber,String password,int series) throws SQLException
    {
        int partnerId;
        Random random=new Random();
        while(true)
        {
            partnerId=Math.abs(random.nextInt());
            if(!partnerIdExist(partnerId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `partner` (`partnerId`,`name`,`phoneNumber`,`password`,`series`) VALUES (?,?,?,?,?)");
        prep.setInt(1, partnerId);
        prep.setString(2, name);
        prep.setString(3, phoneNumber);
        prep.setString(4, password);
        prep.setInt(5, series);
        prep.execute();
        
        return partnerId;
    }
    /**
     * completes specifiacitons of a partner
     * @param partnerId
     * @param name
     * @param email
     * @throws SQLException 
     */
    public void updatePartner(int partnerId,String name,String email) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner` SET `name`=? , `email`=? WHERE `partnerId`=?");
        prep.setString(1, name);
        prep.setString(2, email);
        prep.setInt(3, partnerId);
        prep.execute();
    }
    
    /**
     * completes specifiacitons of a partner
     * @param partnerId
     * @param name
     * @param email
     * @throws SQLException 
     */
    public void updatePartner(int partnerId,String name,String phoneNumber,int series) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner` SET `name`=? , `phoneNumber`=?,`series`=? WHERE `partnerId`=?");
        prep.setString(1, name);
        prep.setString(2, phoneNumber);
        prep.setInt(3, series);
        prep.setInt(4, partnerId);
        prep.execute();
    }
    /**
     * changes partner series of a partner
     * @param partnerId
     * @param series
     * @throws SQLException 
     */
    public void updatePartner(int partnerId,int series) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner` SET `series`=? WHERE `partnerId`=?");
        prep.setInt(1, series);
        prep.setInt(2, partnerId);
        prep.execute();
    }
    
    /**
     * retrieves all partner product categories
     * @return
     * @throws SQLException 
     */
    public LinkedList<PartnerCategory> getPartnerCategories() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_category`");
        ResultSet res=prep.executeQuery();
        LinkedList<PartnerCategory> categories=new LinkedList<PartnerCategory>();
        while(res.next())
        {
            PartnerCategory category=new PartnerCategory();
            category.setId(res.getInt("id")).setName(res.getString("name"));
            categories.add(category);
        }
        return categories;
    }
    
    /**
     * retrieves one category
     * @param id
     * @return
     * @throws SQLException 
     */
    public PartnerCategory getPartnerCategory(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_category` WHERE `id`=?");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        PartnerCategory category=new PartnerCategory();
        if(!res.first())
        {
            return category;
        }
        category.setId(res.getInt("id")).setName(res.getString("name"));
        return category;
    }
    
    /**
     * adds a partner product category
     * @param name
     * @throws SQLException 
     */
    public void addPartnerCategory(String name) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `partner_category` (`name`) VALUES (?)");
        prep.setString(1, name);
        prep.execute();
    }
    
    /**
     * deletes a partner product category completely from data base
     * @param id
     * @throws SQLException 
     */
    public void deletePartnerCategory(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM `partner_category` WHERE `id`=?");
        prep.setInt(1, id);
        prep.execute();
    }
    
    /**
     * retrieves all partner products except the deleted ones
     * @return a linked list of partner products
     * @throws SQLException 
     */
    public LinkedList<PartnerProduct> getPartnerProducts() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_product` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<PartnerProduct> products=new LinkedList<PartnerProduct>();
        while(res.next())
        {
            PartnerProduct product=new PartnerProduct();
            product.setProductId(res.getInt("productId")).setCategory(getPartnerCategory(res.getInt("categoryId")))
                    .setName(res.getString("name")).setPrice(res.getInt("price"))
                    .setCount(res.getInt("count"))
                    .setPictures(this.productSQL.getProductPictures(res.getInt("productId")))
                    .setDescription(res.getString("description"))
                    .setStatus(res.getInt("status"));
            products.add(product);
        }
        return products;
    }
    
    /**
     * retrieves a partner product
     * @param productId
     * @return a partner product object
     * @throws SQLException 
     */
    public PartnerProduct getPartnerProduct(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_product` WHERE `productId`=?");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        PartnerProduct product=new PartnerProduct();
        if(!res.first())
        {
            return product;
        }
        product.setProductId(res.getInt("productId")).setCategory(getPartnerCategory(res.getInt("categoryId")))
                .setName(res.getString("name")).setPrice(res.getInt("price"))
                .setCount(res.getInt("count")).setPictures(this.productSQL.getProductPictures(productId))
                .setDescription(res.getString("description"))
                .setStatus(res.getInt("status"));
        
        return product;
    }
    
    /**
     * shows if productId exists or not
     * @param productId
     * @return true if yes
     * @throws SQLException 
     */
    public boolean partnerProductIdExists(int productId) throws SQLException
    {
        return this.productSQL.productIdExists(productId);
    }
    /**
     * adds a partner product
     * @param categoryId
     * @param name
     * @param price
     * @param count
     * @param description
     * @return the id of the added partner product
     * @throws SQLException 
     */
    public int addPartnerProduct(int categoryId,String name,int price,int count,String description) throws SQLException
    {
        int productId;
        Random random=new Random();
        while(true)
        {
            productId=Math.abs(random.nextInt());
            if(!partnerProductIdExists(productId))
                break;
        }
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `partner_product` (`productId`,`categoryId`,`name`,`price`,`count`,`description`) VALUES (?,?,?,?,?,?)");
        prep.setInt(1, productId);
        prep.setInt(2, categoryId);
        prep.setString(3, name);
        prep.setInt(4, price);
        prep.setInt(5, count);
        prep.setString(6, description);
        prep.execute();
        
        return productId;
    }
    
    /**
     * updates the values of a partner product
     * @param productId
     * @param categoryId
     * @param name
     * @param price
     * @param count
     * @param description
     * @param status
     * @throws SQLException 
     */
    public void updatePartnerProduct(int productId,int categoryId,String name,int price,int count,String description,int status) throws SQLException
    {
         PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_product` SET `categoryId`=?,`name`=?,`price`=?,`count`=?,`description`=?,`status`=? WHERE `productId`=?");
         prep.setInt(1, categoryId);
         prep.setString(2, name);
         prep.setInt(3, price);
         prep.setInt(4, count);
         prep.setString(5, description);
         prep.setInt(6, status);
         prep.setInt(7, productId);
         prep.execute();
         
    }
    
    /**
     * changes the status of a partner product to -1 
     * @param productId
     * @throws SQLException 
     */
    public void deleteUpdatePartnerProduct(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_product` SET `status`=-1 WHERE `productId`=?");
        prep.setInt(1, productId);
        prep.execute();
    }
    
    /**
     * retrieves all receipts of a partner 
     * @param partnerId
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<PartnerReceipt> getPartnerReceipts(int partnerId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_receipt` WHERE `partnerId`=? AND `status`>0");
        prep.setInt(1, partnerId);
        ResultSet res=prep.executeQuery();
        LinkedList<PartnerReceipt> receipts=new LinkedList<PartnerReceipt>();
        while(res.next())
        {
            PartnerReceipt receipt=new PartnerReceipt();
            receipt.setOrderId(res.getInt("orderId")).setPartner(getPartner(res.getInt("partnerId")))
                    .setDate(res.getTimestamp("date")).setIp(res.getString("ip")).setParts(getPartnerParts(res.getInt("orderId")))
                    .setPayment(this.orderHisSQL.getPayment(res.getInt("paymentId")))
                    .setShip(this.orderHisSQL.getShip(res.getInt("shipId")))
                    .setStatus(res.getInt("status"));
            receipts.add(receipt);
        }
        return receipts;
    }
    
    /**
     * retrieves all receipts of a partner 
     * @param partnerId
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<PartnerReceipt> getPartnerReceipts() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_receipt` WHERE `status`>0 ORDER BY `id` DESC");
        ResultSet res=prep.executeQuery();
        LinkedList<PartnerReceipt> receipts=new LinkedList<PartnerReceipt>();
        while(res.next())
        {
            PartnerReceipt receipt=new PartnerReceipt();
            receipt.setOrderId(res.getInt("orderId")).setPartner(getPartner(res.getInt("partnerId")))
                    .setDate(res.getTimestamp("date")).setIp(res.getString("ip"))
                    .setPayment(this.orderHisSQL.getPayment(res.getInt("paymentId")))
                    .setShip(getShipForPartner(res.getInt("shipId"))).setParts(getPartnerParts(res.getInt("orderId")))
                    .setStatus(res.getInt("status"));
            receipts.add(receipt);
        }
        return receipts;
    }
    
    
    /**
     * retrieves one partner receipt
     * @param orderId
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public PartnerReceipt getPartnerReceipt(int orderId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_receipt` WHERE `orderId`=?");
        prep.setInt(1, orderId);
        ResultSet res=prep.executeQuery();
        PartnerReceipt receipt=new PartnerReceipt();
        if(!res.next())
        {
            return receipt;
        }
        receipt.setOrderId(res.getInt("orderId")).setPartner(getPartner(res.getInt("partnerId")))
                .setDate(res.getTimestamp("date")).setIp(res.getString("ip"))
                .setPayment(this.orderHisSQL.getPayment(res.getInt("paymentId")))
                .setShip(getShipForPartner(res.getInt("shipId"))).setParts(getPartnerParts(orderId))
                .setStatus(res.getInt("status"));
        return receipt;
    }
    
    public Ship getShipForPartner(int shipId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `ship` WHERE `shipId`=?");
        prep.setInt(1, shipId);
        ResultSet res=prep.executeQuery();
        Ship ship=new Ship();
        if(!res.first())
        { 
            return ship.setAddress(new UserAddress()).setShipType(new ShipType());
        }
        
        ship.setAddress(getPartnerAddress(res.getInt("addressId"))).setShipId(res.getInt("shipId")).setDate(res.getTimestamp("date"))
                .setShipType(this.orderHisSQL.getShipType(res.getInt("shipTypeId"))).setStatus(res.getInt("status"));
        return ship;
    }
    
    /**
     * shows if order id exists or not 
     * @param orderId
     * @return true if it does
     * @throws SQLException 
     */
    public boolean partnerReceiptIdExists(int orderId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_receipt` WHERE `orderId`=?");
        prep.setInt(1, orderId);
        return prep.executeQuery().first();
    }
    
    /**
     * opens a cart 
     * @param partnerId
     * @return the id of the opend receipt
     * @throws SQLException 
     */
    public int addPartnerReceipt(int partnerId) throws SQLException
    {
        int orderId;
        Random random=new Random();
        while(true)
        {
            orderId=Math.abs(random.nextInt());
            if(!partnerReceiptIdExists(orderId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `partner_receipt` (`orderId`,`partnerId`) VALUES (?,?)");
        prep.setInt(1, orderId);
        prep.setInt(2, partnerId);
        prep.execute();
        
        return orderId;
    }
    
    /**
     * changes the status of a receipt . -1:deleted/0:unSubmittedCart/1:waiting/2:approved/3:done/4:denied
     * @param orderId
     * @param status
     * @throws SQLException 
     */
    public void setPartnerReceiptStatus(int orderId,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_receipt` SET `status`=? WHERE `orderId`=?");
        prep.setInt(1, status);
        prep.setInt(2, orderId);
        prep.execute();
    }
    
    /**
     * retrieves all parts attached to a receipt
     * @param orderId
     * @return a linked list of parts
     * @throws SQLException 
     */
    public LinkedList<PartnerPart> getPartnerParts(int orderId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_part` WHERE `orderId`=? AND `status`!=-1");
        prep.setInt(1, orderId);
        ResultSet res=prep.executeQuery();
        LinkedList<PartnerPart> parts=new LinkedList<PartnerPart>();
        while(res.next())
        {
            PartnerPart part=new PartnerPart();
            part.setPartId(res.getInt("partId")).setOrderId(orderId).setProduct(getPartnerProduct(res.getInt("productId")))
                    .setCount(res.getInt("count")).setFinalPrice(res.getInt("finalPrice"))
                    .setStatus(res.getInt("status"));
            parts.add(part);
        }
        return parts;
    }
    
    /**
     * shows if a part id is used or not 
     * @param partId
     * @return
     * @throws SQLException 
     */
    public boolean partnerPartIdExists(int partId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_part` WHERE `partId`=?");
        prep.setInt(1, partId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds a part to a receipt
     * @param orderId
     * @param productId
     * @param count
     * @return
     * @throws SQLException 
     */
    public int addPartnerPart(int orderId,int productId,int count) throws SQLException
    {
        int partId = 0;
        Random random=new Random();
        while(true)
        {
            partId=Math.abs(random.nextInt());
            if(!partnerPartIdExists(partId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `partner_part` (`partId`,`orderId`,`productId`,`count`) VALUES (?,?,?,?)");
        prep.setInt(1, partId);
        prep.setInt(2, orderId);
        prep.setInt(3, productId);
        prep.setInt(4, count);
        prep.execute();
        
        return partId;
    }
    
    /**
     * retrieves the last open cart id and if there wasn't one, it will create a new one and return that one
     * @param partnerId
     * @return
     * @throws SQLException 
     */
    public int getCartId(int partnerId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_receipt` WHERE `status`=0 AND `partnerId`=? ORDER BY `id` DESC LIMIT 1");
        prep.setInt(1, partnerId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return addPartnerReceipt(partnerId);
        }
        return res.getInt("orderId");
    }
    
    /**
     * retrieves the last open cart id and if there wasn't one, it will return -1
     * @param partnerId
     * @return
     * @throws SQLException 
     */
    public int getEmptyCartId(int partnerId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_receipt` WHERE `status`=0 AND `partnerId`=? ORDER BY `id` DESC LIMIT 1");
        prep.setInt(1, partnerId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return -1;
        }
        return res.getInt("orderId");
    }
    
    public LinkedList<UserAddress> getPartnerAddresses(int partnerId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_address` WHERE `partnerId`=? AND `status`!=-1");
        prep.setInt(1, partnerId);
        ResultSet res=prep.executeQuery();
        LinkedList<UserAddress> addresses=new LinkedList<UserAddress>();
        while(res.next())
        {
            UserAddress address=new UserAddress();
            address.setAddress(res.getString("address")).setAddressId(res.getInt("addressId"))
                    .setPhoneNumber(res.getString("phoneNumber")).setPostalCode(res.getString("postalCode"))
                    .setUserId(partnerId).setStatus(res.getInt("status"));
            addresses.add(address);
        }
        return addresses;
    }
    
    public UserAddress getPartnerAddress(int addressId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_address` WHERE `addressId`=?");
        prep.setInt(1, addressId);
        UserAddress address=new UserAddress();
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return address;
        }
        address.setAddress(res.getString("address")).setAddressId(res.getInt("addressId"))
                    .setPhoneNumber(res.getString("phoneNumber")).setPostalCode(res.getString("postalCode"))
                    .setUserId(res.getInt("partnerId")).setStatus(res.getInt("status"));
        return address;
        
    }
    
    public boolean partnerAddressIdExists(int addressId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_address` WHERE `addressId`=?");
        prep.setInt(1, addressId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds an address for a partner
     * @param partnerId
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @return
     * @throws SQLException 
     */
    public int addPartnerAddress(int partnerId,String address,String postalCode,String phoneNumber) throws SQLException
    {
        int addressId;
        Random random=new Random();
        while(true)
        {
            addressId=Math.abs(random.nextInt());
            if(!partnerAddressIdExists(addressId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `partner_address` (`partnerId`,`addressId`,`address`,`postalCode`,`phoneNumber`) VALUES (?,?,?,?,?)");
        prep.setInt(1, partnerId);
        prep.setInt(2, addressId);
        prep.setString(3, address);
        prep.setString(4, postalCode);
        prep.setString(5, phoneNumber);
        prep.execute();
        return addressId;
    }
    
    /**
     * changes the status of the address to -1
     * @param addressId
     * @throws SQLException 
     */
    public void deletePartnerAddress(int addressId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_address` SET `status`=-1 WHERE `addressId`=?");
        prep.setInt(1, addressId);
        prep.execute();
    }
    
    public void editPartnerAddress(int addressId,String address,String postalCode,String phoneNumber) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_address` SET `address`=?,`postalCode`=?,`phoneNumber`=? WHERE `addressId`=?");
        prep.setString(1, address);
        prep.setString(2, postalCode);
        prep.setString(3, phoneNumber); 
        prep.setInt(4, addressId);
        prep.execute();
        
    }
    
    /**
     * shows if the product is available and has enough counts
     * @param productId
     * @param count
     * @return
     * @throws SQLException 
     */
    public boolean isPartOk(int productId,int count) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_product` WHERE `productId`=? AND `status`=1 AND `count`>=?");
        prep.setInt(1, productId);
        prep.setInt(2, count); 
        return prep.executeQuery().first();
    }
    
    /**
     * after submitting order, reduces the count bought from data base
     * @param productId
     * @param count
     * @throws SQLException 
     */
    public void reduceCount(int productId,int count) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_product` SET `count`=`count`-? WHERE `productId`=?");
        prep.setInt(1, count);
        prep.setInt(2, productId);
        prep.execute();
    }
    
    /**
     * after each order submition, this will run out each item that has finished
     * @throws SQLException 
     */
    public void runOut() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_product` SET `status`=2 WHERE `count`<=0");
        prep.execute();
    }
    
    /**
     * deletes a part from the given cart id(always pass the new cart id)
     * @param partId
     * @param cartId
     * @throws SQLException 
     */
    public void deletePartnerPart(int partId,int cartId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_part` SET `status`=-1 WHERE `partId`=? AND `orderId`=?");
        prep.setInt(1, partId);
        prep.setInt(2, cartId);
        prep.execute();
    }

    /**
     * shows if the address is valid for a partner or not 
     * @param addressId
     * @param partnerId
     * @return
     * @throws SQLException 
     */
    public boolean isAddressValid(int addressId,int partnerId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_address` WHERE `addressId`=? AND `partnerId`=?");
        prep.setInt(1, addressId);
        prep.setInt(2, partnerId);
        return prep.executeQuery().first();
    }
    
    public void setPartFinalPrice(int partId,int price) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_part` SET `finalPrice`=? , `status`=1 WHERE `partId`=?");
        prep.setInt(1, price);
        prep.setInt(2, partId);
        prep.execute();
    }
    
    /**
     * completes the order by setting shipId and paymentId and date and changing the status of the receipt to 1
     * @param orderId
     * @param shipId
     * @param paymentId
     * @throws SQLException 
     */
    public void completeOrder(int orderId,int shipId,int paymentId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_receipt` SET `paymentId`=? , `shipId`=? , `date`=?,`status`=1 WHERE `orderId`=?");
        prep.setInt(1, paymentId);
        prep.setInt(2, shipId);
        prep.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        prep.setInt(4, orderId);
        prep.execute();
    }
    
    /**
     * shows if the client has already added this product to his cart or not 
     * @param cartId
     * @param productId
     * @return if yes, returns partId and if not returns -1
     * @throws SQLException 
     */
    public int isProductInCart(int cartId,int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_part` WHERE `orderId`=? AND `productId`=? AND `status`!=-1");
        prep.setInt(1, cartId);
        prep.setInt(2, productId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return -1;
        }
        return res.getInt("partId");
    }
    
    /**
     * adds counts to an existing part in a cart
     * @param partId
     * @param count
     * @throws SQLException 
     */
    public void addCountPart(int partId,int count) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_part` SET `count`=`count`+? WHERE `partId`=?");
        prep.setInt(1, count);
        prep.setInt(2, partId);
        prep.execute();
    }
    
    public Partner getUserDetailSms(int orderId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `partner`.`partnerId`,`partner`.`phoneNumber` FROM `partner` " +
"INNER JOIN `partner_receipt`  " +
"ON `partner_receipt`.`partnerId`=`partner`.`partnerId` " +
"WHERE `partner_receipt`.`orderId`=?");
        prep.setInt(1, orderId);
        ResultSet res=prep.executeQuery();
        Partner user=new Partner();
        if(!res.first())
        {
            return user;
        }
        
        user.setPartnerId(res.getInt("partnerId")).setPhoneNumber(res.getString("phoneNumber")); 
        return user;
        
    }
    
    /**
     * changes the status of a receipt
     * @param orderId
     * @param status
     * @throws SQLException 
     */
    public void editReceipt(int orderId,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_receipt` SET `status`=? WHERE `orderId`=?");
        prep.setInt(1, status);
        prep.setInt(2, orderId);
        prep.execute();
    }
    
    /**
     * gets all series except the deleted ones
     * @return
     * @throws SQLException 
     */
    public LinkedList<PartnerSeries> getPartnerSeries() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_series` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<PartnerSeries> series=new LinkedList<PartnerSeries>();
        while(res.next())
        {
            PartnerSeries seri=new PartnerSeries();
            seri.setId(res.getInt("id")).setEffect(res.getDouble("effect")).setName(res.getString("name"));
            series.add(seri);
        }
        return series;
    }
    
    /**
     * gets one seri based on it's id
     * @param id
     * @return
     * @throws SQLException 
     */
    public PartnerSeries getPartnerSeries(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_series` WHERE `id`=?");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        PartnerSeries seri=new PartnerSeries();
        if(!res.next())
        {
            return seri;
        }
        seri.setId(res.getInt("id")).setEffect(res.getDouble("effect")).setName(res.getString("name"));
        return seri;
    }
    
    /**
     * adds a PartnerSeries
     * @param name
     * @param effect
     * @throws SQLException 
     */
    public void addPartnerSeries(String name,double effect) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `partner_series` (`name`,`effect`) VALUES (?,?)");
        prep.setString(1, name);
        prep.setDouble(2, effect);
        prep.execute();
    }
    
    /**
     * updates the status of the series to -1
     * @param id
     * @throws SQLException 
     */
    public void deletePartnerSeries(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_series` SET `status`=-1 WHERE `id`=?");
        prep.setInt(1, id);
        prep.execute();
    }
    /**
     * updates the details of a partnerseries
     * @param id
     * @param name
     * @param effect
     * @throws SQLException 
     */
    public void updatePartnerSeries(int id,String name,double effect) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner_series` SET `name`=?,`effect`=? WHERE `id`=?");
        prep.setString(1, name);
        prep.setDouble(2, effect);
        prep.setInt(3, id);
        prep.execute();
    }
    
    /**
     * changes the status of a pertner to -1
     * @param partnerId
     * @throws SQLException 
     */
    public void deletePartner(int partnerId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner` SET `status`=-1 WHERE `partnerId`=?");
        prep.setInt(1, partnerId);
        prep.execute();
    }
    
    /**
     * changes the password of a partner
     * @param partnerId
     * @param password md5 hashed passsword 
     * @throws SQLException 
     */
    public void changePartnerPassword(int partnerId,String password) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `partner` set `password`=? WHERE `partnerId`=?");
        prep.setString(1, password);
        prep.setInt(2, partnerId);
        prep.execute();
        
    }
    
}

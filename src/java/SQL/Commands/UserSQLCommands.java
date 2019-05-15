/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import ExceptionsChi.DoesntExistException;
import Objects.User.DiscountTicket;
import Objects.User.User;
import Objects.User.UserAddress;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Moses
 */
public class UserSQLCommands {
    private Connection connection;
    private AuthenticatorSQLCommands authSQL;
    
    public UserSQLCommands(Connection connection)
    {
        this.connection=connection;
        this.authSQL=new AuthenticatorSQLCommands(connection);
    }
    
    /**
     * creates the user object with all of it's data with it's userId 
     * @param userId user id of the user
     * @throws SQLException
     * @throws DoesntExistException if the user doesn't exist, it throws this exceptions
     * @return an user object with full data in it
     */
    public User getUserFull(int userId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM users WHERE `userId`=?");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            throw new DoesntExistException("No User With This UserId");
        }
        User user=new User().setId(res.getInt("id"));
        user.setBirthDate(res.getTimestamp("birthDate")).setDateRegistered(res.getTimestamp("dateRegistered"))
                .setUserId(res.getInt("userId"))
                .setEmail(res.getString("email")).setFirstName(res.getString("firstName")).setLastName(res.getString("lastName"))
                .setPhoneNumber(res.getString("phoneNumber")).setStatus(res.getInt("status"));
        return user;
    }
    
    /**
     * retrieves all addresses
     * @param userId
     * @return a linked list of user addresses
     * @throws SQLException 
     */
    public LinkedList<UserAddress> getAddresses(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `users_address` WHERE `userId`=? AND `status`!=-1");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<UserAddress> addresses=new LinkedList<UserAddress>();
        while(res.next())
        {
            UserAddress address=new UserAddress();
            address.setAddress(res.getString("address")).setAddressId(res.getInt("addressId"))
                    .setLatitude(res.getFloat("latitude")).setLongitude(res.getFloat("longitude"))
                    .setPhoneNumber(res.getString("phoneNumber")).setPostalCode(res.getString("postalCode"))
                    .setUserId(userId).setStatus(res.getInt("status"));
            addresses.add(address);
        }
        return addresses;
            
    }
    
    public UserAddress getAddress(int addressId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `users_address` WHERE `addressId`=?");
        prep.setInt(1, addressId);
        ResultSet res=prep.executeQuery();
        UserAddress address=new UserAddress();
        if(!res.first())
        {
            return address;
        }
        
        address.setAddress(res.getString("address")).setAddressId(addressId)
                .setLatitude(res.getFloat("latitude")).setLongitude(res.getFloat("longitude"))
                .setPhoneNumber(res.getString("phoneNumber")).setPostalCode(res.getString("postalCode"))
                .setUserId(res.getInt("userID")).setStatus(res.getInt("status"));
        
        return address;
    }
    
    /**
     * 
     * @param addressId
     * @return true if exists
     * @throws SQLException 
     */
    public boolean addressIdExists(int addressId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `users_address` WHERE `addressId`=?");
        prep.setInt(1, addressId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds an address to database based on user
     * @param userId
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @return the id of the added address
     * @throws SQLException 
     */
    public int addAddress(int userId,String address,String postalCode,String phoneNumber) throws SQLException
    {
        int addressId;
        Random random=new Random();
        while(true)
        {
            addressId=Math.abs(random.nextInt());
            if(!addressIdExists(addressId))
                break;
        }
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `users_address` (`addressId`,`userId`,`address`,`postalCode`,`phoneNumber`) VALUES (?,?,?,?,?)");
        prep.setInt(1, addressId);
        prep.setInt(2, userId);
        prep.setString(3, address);
        prep.setString(4, postalCode);
        prep.setString(5, phoneNumber);
        prep.execute();
        
        return addressId;
    }
    
    
    /**
     * adds an address to database based on user
     * @param userId
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @return the id of the added address
     * @throws SQLException 
     */
    public int addAddress(int userId,String address,String postalCode,String phoneNumber,float latitude,float longitude) throws SQLException
    {
        int addressId;
        Random random=new Random();
        while(true)
        {
            addressId=Math.abs(random.nextInt());
            if(!addressIdExists(addressId))
                break;
        }
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `users_address` (`addressId`,`userId`,`address`,`postalCode`,`phoneNumber`,`latitude`,`longitude`) VALUES (?,?,?,?,?,?,?)");
        prep.setInt(1, addressId);
        prep.setInt(2, userId);
        prep.setString(3, address);
        prep.setString(4, postalCode);
        prep.setString(5, phoneNumber);
        prep.setFloat(6, latitude);
        prep.setFloat(7, longitude);
        prep.execute();
        
        return addressId;
    }
    
    
    /**
     * returns all users
     * @return a linked list of user
     * @throws SQLException 
     */
    public LinkedList<User> getAllUsers() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `users` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<User> users=new LinkedList<User>();
        while(res.next())
        {
            User user=new User();
            user.setBirthDate(res.getTimestamp("birthDate")).setDateRegistered(res.getTimestamp("dateRegistered")).setUserId(res.getInt("userId"))
                    .setEmail(res.getString("email")).setFirstName(res.getString("firstName")).setLastName(res.getString("lastName"))
                    .setPhoneNumber(res.getString("phoneNumber")).setStatus(res.getInt("status")).setRegisterType(res.getInt("registerType"));
            users.add(user);
        }
        return users;
    }
        
    /**
     * retrieves all users that were invited by the given user Id
     * @param userId
     * @return a linked list of user
     * @throws SQLException 
     */
    public LinkedList<User> getUsersInvitedBy(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `users_invite`.* , `users`.* " +
"FROM `users_invite` " +
"INNER JOIN `users` " +
"ON `users_invite`.`userId`=`users`.`userId` " +
"WHERE `users_invite`.`invitedBy`=?");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<User> users=new LinkedList<User>();
        while(res.next())
        {
            User user=new User();
            user.setBirthDate(res.getTimestamp("birthDate")).setUserId(res.getInt("userId"))
                    .setDateRegistered(res.getTimestamp("dateRegistered"))
                .setEmail(res.getString("email")).setFirstName(res.getString("firstName")).setLastName(res.getString("lastName"))
                .setPhoneNumber(res.getString("phoneNumber")).setStatus(res.getInt("status"));
                users.add(user);
        }
        return users;
    }
    
    public DiscountTicket getDiscount(int discountId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `discount_ticket` WHERE `id`=?");
        prep.setInt(1, discountId);
        ResultSet res=prep.executeQuery();
        DiscountTicket discount=new DiscountTicket();
        if(!res.first())
        {
            return discount;
        }
        discount.setDiscountId(res.getInt("id")).setEffect(res.getDouble("effect"))
                    .setExpireDate(res.getTimestamp("expireDate"))
                    .setUserId(res.getInt("userId")).setStatus(res.getInt("status"));
        return discount;
    }
    
    /**
     * retrieves all discount tickets of a user
     * @param userId the id of the user
     * @return a linked list of discount ticket
     * @throws SQLException 
     */
    public LinkedList<DiscountTicket> getDiscounts(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `discount_ticket` WHERE `userId`=? AND `status`!=-1");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<DiscountTicket> discounts=new LinkedList<DiscountTicket>();
        while(res.next())
        {
            DiscountTicket discount=new DiscountTicket();
            discount.setDiscountId(res.getInt("id")).setEffect(res.getDouble("effect"))
                    .setExpireDate(res.getTimestamp("expireDate"))
                    .setUserId(res.getInt("userId")).setStatus(res.getInt("status"));
            discounts.add(discount);
        }
        return discounts;
    }
    
    /**
     * retrieves all discount tickets of a user that hasn't expired yet
     * @param userId the id of the user
     * @return a linked list of discount ticket
     * @throws SQLException 
     */
    public LinkedList<DiscountTicket> getDiscountsValid(int userId) throws SQLException
    {
        long now=System.currentTimeMillis();
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `discount_ticket` WHERE `userId`=? AND `status`=0");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<DiscountTicket> discounts=new LinkedList<DiscountTicket>();
        while(res.next())
        {
            if(res.getTimestamp("expireDate").getTime()<now)
                continue;
            
            DiscountTicket discount=new DiscountTicket();
            discount.setDiscountId(res.getInt("id")).setEffect(res.getDouble("effect"))
                    .setExpireDate(res.getTimestamp("expireDate"))
                    .setUserId(res.getInt("userId")).setStatus(res.getInt("status"));
            discounts.add(discount);
        }
        return discounts;
    }
    
    /**
     * retrieves all discount tickets of a user
     * @param userId the id of the user
     * @param status -1:deleted/0:ok/1:used	
     * @return a linked list of discount ticket
     * @throws SQLException 
     */
    public LinkedList<DiscountTicket> getDiscounts(int userId,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `discount_ticket` WHERE `userId`=? AND `status`!=-1");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<DiscountTicket> discounts=new LinkedList<DiscountTicket>();
        while(res.next())
        {
            DiscountTicket discount=new DiscountTicket();
            discount.setDiscountId(res.getInt("id")).setEffect(res.getDouble("effect"))
                    .setExpireDate(res.getTimestamp("expireDate"))
                    .setUserId(res.getInt("userId")).setStatus(res.getInt("status"));
            discounts.add(discount);
        }
        return discounts;
    }
    
    /**
     * adds a discount ticket
     * @param effect
     * @param expireDate
     * @param userId
     * @throws SQLException 
     */
    public void addDiscountTicket(double effect,Timestamp expireDate,int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `discount_ticket` (`effect`,`expireDate`,`userId`) VALUES (?,?,?)");
        prep.setDouble(1, effect);
        prep.setTimestamp(2, expireDate);
        prep.setInt(3, userId);
        prep.execute();
    }
    
    /**
     * adds a discount ticket
     * @param effect
     * @param userId
     * @throws SQLException 
     */
    public void addDiscountTicket(double effect,int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `discount_ticket` (`effect`,`userId`) VALUES (?,?)");
        prep.setDouble(1, effect);
        prep.setInt(2, userId);
        prep.execute();
    }
    
    /**
     * changes the status of a ticket to -1
     * @param id
     * @throws SQLException 
     */
    public void deleteDiscountTicket(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `discount_ticket` SET `status`=-1 WHERE `id`=?");
        prep.setInt(1, id);
        prep.execute(); 
    }
    
    /**
     * adds a user to data base from admin panel with status 20 which means the phonenumber is authed :D
     * @param phoneNumber the phone number of the user
     * @param password MD5 hashed password
     * @param registerType 0:user /1:by admin
     * @return the id of the added user
     * @throws SQLException 
     */
    public int addUser(String phoneNumber,String password,int registerType) throws SQLException
    {
        int userId;
        Random random=new Random();
        while(true)
        {
            userId=Math.abs(random.nextInt());
            if(!this.authSQL.userIdExists(userId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `users` (`userId`,`phoneNumber`,`password`,`status`,`registerType`) VALUES (?,?,?,20)");
        prep.setInt(1, userId);
        prep.setString(2, phoneNumber);
        prep.setString(3, password);
        prep.setInt(4, registerType);
        prep.execute();
        return userId;
    }
    
    /**
     * 
     * @return a linked list of users who has birthday in 3 days 
     * @throws SQLException 
     */
    public LinkedList<User> getNearBirthDays() throws SQLException
    {
        Calendar today=Calendar.getInstance();
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `users` where `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<User> users=new LinkedList<User>();
        while(res.next())
        {
            Timestamp birthstamp=res.getTimestamp("birthDate");
            if(birthstamp==null)
                continue;
            Calendar birthCal=Calendar.getInstance();
            birthCal.setTimeInMillis(birthstamp.getTime());
            Calendar cal=Calendar.getInstance();
            cal.set(today.get(Calendar.YEAR), birthCal.get(Calendar.MONTH), birthCal.get(Calendar.DAY_OF_MONTH));
            long compare=Math.abs(cal.getTimeInMillis()-today.getTimeInMillis());
            if(compare<=259200000)
            {
                User user=new User().setUserId(res.getInt("userId")).setFirstName(res.getString("firstName"))
                        .setLastName(res.getString("lastName")).setBirthDate(birthstamp);
                users.add(user);
            }
            
        }
        return users;
    }
        
    /**
     * edits an address in data base, based on it's id
     * @param addressId
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @throws SQLException 
     */
    public void editAddress(int addressId,String address,String postalCode,String phoneNumber) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `users_address` SET `address`=?,`postalCode`=?,`phoneNumber`=? WHERE `addressId`=?");
        prep.setString(1, address);
        prep.setString(2, postalCode);
        prep.setString(3, phoneNumber);
        prep.setInt(4, addressId);
        prep.execute();
    }
    
    /**
     * changes the status of the address to -1 
     * @param addressId
     * @throws SQLException 
     */
    public void deleteAddress(int addressId,int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `users_address` SET `status`=-1 WHERE `addressId`=? AND `userId`=?");
        prep.setInt(1, addressId);
        prep.setInt(2, userId);
        prep.execute();
    }

    /**
     * sets the specifications of a user
     * @param email
     * @param firstName
     * @param lastName
     * @param birthDate
     * @throws SQLException 
     */
    public void setSpecificatoins(int userId,String email,String firstName,String lastName,Date birthDate) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `users` SET `email`=?,`firstName`=?,`lastName`=?,`birthDate`=?,`status`=30 WHERE `userId`=?");
        prep.setString(1, email);
        prep.setString(2, firstName);
        prep.setString(3, lastName);
        prep.setDate(4, birthDate);
        prep.setInt(5, userId);
        prep.execute();
    }
    
     /**
     * sets the specifications of a user
     * @param email
     * @param firstName
     * @param lastName
     * @param birthDate
     * @throws SQLException 
     */
    public void setSpecificatoins(int userId,String email,String firstName,String lastName) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `users` SET `email`=?,`firstName`=?,`lastName`=?,`status`=30 WHERE `userId`=?");
        prep.setString(1, email);
        prep.setString(2, firstName);
        prep.setString(3, lastName);
        
        prep.setInt(4, userId);
        prep.execute();
    }
    
    /**
     * adds a user invited
     * @param userIdInviter
     * @param userIdInvited
     * @throws SQLException 
     */
    public void addInvitedBy(int userIdInviter,int userIdInvited) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `users_invite` (`userId`,`invitedBy`) VALUES (?,?)");
        prep.setInt(1, userIdInvited);
        prep.setInt(2, userIdInviter);
        prep.execute();
    }
    
    /**
     * gets userId based on id
     * @param id
     * @return
     * @throws SQLException 
     */
    public int getUserInviterId(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `userId` FROM `users` WHERE `id`=?");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return -1;
        }
        
        return res.getInt("userId");
    }
    
    /**
     * returns the amount of user that a user has invited and hasn't got prize for it
     * @param userId
     * @return
     * @throws SQLException 
     */
    public int getInvitedCount(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT COUNT(id) AS counts FROM `users_invite` WHERE `invitedBy`=? AND `prized`=0");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return 0;
        }
        
        return res.getInt("counts");
    }
    
    /**
     * changes prized value to 1 so that it means this has gotten prize for invited users
     * @param userId
     * @throws SQLException 
     */
    public void invalidateInvitedPrize(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `users_invite` SET `prized`=1 WHERE `invitedBy`=?");
        prep.setInt(1, userId);
        prep.execute();
    }
    
    /**
     * changes the status of the discount to 1
     * @param id
     * @throws SQLException 
     */
    public void invalidateDiscount(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `discount_ticket` SET `status`=1 WHERE `id`=?");
        prep.setInt(1, id);
        prep.execute(); 
    }
    
    /**
     * shows if the address given is from the user or not
     * @param userId
     * @param addressId
     * @return
     * @throws SQLException 
     */
    public boolean isAddressValid(int userId,int addressId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `users_address` WHERE `userId`=? AND `addressId`=?");
        prep.setInt(1, userId);
        prep.setInt(2, addressId);
        return prep.executeQuery().first();
    }
    
    /**
     * gets the name of the user
     * @param userId
     * @return "firstName"+" "+"lastName"
     * @throws SQLException 
     */
    public String getName(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `firstName`,`lastName` FROM `users` WHERE `userId`=?");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        if(res.first())
        {
            return res.getString("firstName")+" "+res.getString("lastName");
        }else
        {
            return "";
        }
    }
    
    /**
     * chagnes the status of a user to -1
     * @param userId
     * @throws SQLException 
     */
    public void deleteUser(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `users` SET `status`=-1 WHERE `userId`=?");
        prep.setInt(1, userId);
        prep.execute();
        
    }
    
    /**
     * changes the password of a user
     * @param userId
     * @param password md5 hashed password
     * @throws SQLException 
     */
    public void changeUserPassword(int userId,String password) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `users` SET `password`=? WHERE `userId`=?");
        prep.setString(1, password);
        prep.setInt(2, userId);
        prep.execute();
        
    }
}

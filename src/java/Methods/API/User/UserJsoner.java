/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.User;

import Methods.API.Orders.OrderJsoner;
import Objects.User.DiscountTicket;
import Objects.User.User;
import Objects.User.UserAddress;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class UserJsoner {
    private OrderJsoner orderJsoner=new OrderJsoner();
    /**
     * retrieves only firstname and lastname and phone number and status
     * @param user
     * @return 
     */
    public JSONObject getUserInfoObject(User user)
    {
        JSONObject userObject=new JSONObject();
        userObject.put("phoneNumber", user.getPhoneNumber());
        userObject.put("firstName", user.getFirstName());
        userObject.put("lastName", user.getLastName());
        userObject.put("status", user.getStatus());
        
        return userObject;
    }
    
    /**
     * retrieves all user's info
     * @param user
     * @param addresses
     * @return 
     */
    public JSONObject getUserFull(User user,LinkedList<UserAddress> addresses)
    {
        JSONObject userObject=new JSONObject();
        userObject.put("firstName", user.getFirstName());
        userObject.put("lastName", user.getLastName());
        userObject.put("phoneNumber", user.getPhoneNumber());
        userObject.put("birthDate", Methods.Methods.getShamsiDate(user.getBirthDate()));
        userObject.put("registerDate", Methods.Methods.getShamsiDate(user.getDateRegistered()));
        userObject.put("email", user.getEmail());
        userObject.put("address",getAddressArray(addresses) );
        userObject.put("status", user.getStatus());
        userObject.put("inviteToken", Methods.Encryptors.Encrypt1(user.getId()));
        return userObject;
            
    }
    
    public JSONArray getAddressArray(LinkedList<UserAddress> addresses)
    {
        JSONArray addressArray=new JSONArray();
        int i=0;
        for(UserAddress address:addresses)
        {
            addressArray.put(i,this.orderJsoner.getAddressJson(address));
            i++;
        }
        return addressArray;
    }
    
    public JSONArray getDiscountsArray(LinkedList<DiscountTicket> discounts)
    {
        JSONArray discountArray=new JSONArray();
        int i=0;
        for(DiscountTicket discount:discounts)
        {
            discountArray.put(i,getDiscountObject(discount));
            i++;
        }
        return discountArray;
    }
    
    public JSONObject getDiscountObject(DiscountTicket discount)
    {
        JSONObject discountObject=new JSONObject();
        discountObject.put("discountId", discount.getDiscountId());
        discountObject.put("effect", discount.getEffect());
        return discountObject;
    }
        
    
}

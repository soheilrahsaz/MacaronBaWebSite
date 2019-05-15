/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.User;

import Objects.User.UserAddress;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class UserObjector {
    public UserAddress getAddress(JSONObject addressObject)
    {
        UserAddress address=new UserAddress();
        address.setAddress(addressObject.optString("address"));
        address.setPhoneNumber(addressObject.optString("phoneNumber"));
        address.setPostalCode(addressObject.optString("postalCode"));
        address.setAddressId(addressObject.optInt("addressId"));
        address.setLatitude(addressObject.optFloat("latitude"));
        address.setLongitude(addressObject.optFloat("longitude"));
        return address;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.User;

/**
 *
 * @author Moses
 */
public class UserAddress {
    private int userId;
    private int addressId;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private float longitude;
    private float latitude;
    private int status;

    public float getLongitude() {
        return longitude;
    }

    public UserAddress setLongitude(float longitude) {
        this.longitude = longitude;
        return this;
    }

    public float getLatitude() {
        return latitude;
    }

    public UserAddress setLatitude(float latitude) {
        this.latitude = latitude;
        return this;
    }

    
    
    public int getUserId() {
        return userId;
    }

    public UserAddress setUserId(int userId) {
        this.userId = userId;
    return this;}

    public int getAddressId() {
        return addressId;
    }

    public UserAddress setAddressId(int addressId) {
        this.addressId = addressId;
    return this;}

    public String getAddress() {
        return address;
    }

    public UserAddress setAddress(String address) {
        this.address = address;
    return this;}

    public String getPostalCode() {
        return postalCode;
    }

    public UserAddress setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    return this;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserAddress setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    return this;}

    public int getStatus() {
        return status;
    }

    public UserAddress setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

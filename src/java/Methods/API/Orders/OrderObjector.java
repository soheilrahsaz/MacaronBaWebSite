/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Orders;

import Objects.Order.Item;
import Objects.Order.ItemProperty;
import Objects.Order.Part;
import Objects.Payment.Payment;
import Objects.Payment.PaymentType;
import Objects.Product.Product;
import Objects.Product.Property;
import Objects.Product.PropertyValue;
import Objects.Product.ScalePackage;
import Objects.Product.ScaleValue;
import Objects.Product.Window;
import Objects.Ship.Ship;
import Objects.Ship.ShipType;
import Objects.User.DiscountTicket;
import Objects.User.UserAddress;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class OrderObjector {
    
    public Part getPartObject(JSONObject partJson)
    {
        Part part=new Part();
        part.setCount(partJson.optInt("count"));
        part.setWindow(partJson.optBoolean("window"));
        
        if(part.isWindow())
        {
            part.setProductWindow(getProductWindowObject(partJson.optJSONObject("product")));
            
        }else
        {
            part.setProduct(getProductObject(partJson.optJSONObject("product")));
            part.setItems(getItemsObject(partJson.optJSONArray("item")));
            part.setScalePackage(getScalePackageObject(partJson.optJSONObject("scalePackage")));
            part.setScaleValue(getScaleValueObject(partJson.optJSONObject("scaleValue")));
            
            LinkedList<ItemProperty> effectAlls=getItemPropertiesObject(partJson.optJSONObject("effectAll").optJSONArray("itemProperty"));
            organize(part,effectAlls);
        }
        
        return part;
    }
    
    public ScaleValue getScaleValueObject(JSONObject scaleValueJson)
    {
        ScaleValue scaleValue=new ScaleValue();
        scaleValue.setValaueId(scaleValueJson.optInt("valueId"));
        return scaleValue;
    }
    
    public ScalePackage getScalePackageObject(JSONObject scalePackageJson)
    {
        ScalePackage scalePackage=new  ScalePackage();
        scalePackage.setPackageId(scalePackageJson.optInt("packageId"));
        return scalePackage;
    }
    
    public LinkedList<Item> getItemsObject(JSONArray itemArray)
    {
        LinkedList<Item> items=new LinkedList<Item>();
        for (int i = 0; i < itemArray.length(); i++) 
        {
            items.add(getItemObject(itemArray.optJSONObject(i)));
        }
        return items;
    }
        
    public Item getItemObject(JSONObject itemJson)
    {
        Item item=new Item();
        item.setCount(itemJson.optInt("count"));
        item.setItemProperties(getItemPropertiesObject(itemJson.optJSONArray("itemProperty")));
        return item;
    }
        
    public LinkedList<ItemProperty> getItemPropertiesObject(JSONArray itemPropertiesArray)
    {
        LinkedList<ItemProperty> properties=new LinkedList<ItemProperty>();
        for (int i = 0; i < itemPropertiesArray.length(); i++) 
        {
            properties.add(getItemPropertyObject(itemPropertiesArray.optJSONObject(i)));
        }
        return properties;
    }
    
    public ItemProperty getItemPropertyObject(JSONObject propertyJson)
    {
        ItemProperty property=new ItemProperty();
        property.setProperty(getPropertyObject(propertyJson.optJSONObject("property")));
        property.setPropertyvaValues(getPropetyValuesObject(propertyJson.optJSONArray("propertyValue")));
        return property;        
    }
    
    private LinkedList<PropertyValue> getPropetyValuesObject(JSONArray propertyValueArray)
    {
        LinkedList<PropertyValue> values=new LinkedList<PropertyValue>();
        for (int i = 0; i < propertyValueArray.length(); i++) 
        {
            values.add(getPropertyValueObject(propertyValueArray.optJSONObject(i)));
        }
        return values;
    }
    
    private PropertyValue getPropertyValueObject(JSONObject valueObject)
    {
        PropertyValue value=new PropertyValue();
        value.setValueId(valueObject.optInt("valueId"));
        return value;
    }
    
    private Property getPropertyObject(JSONObject propertyJson)
    {
        Property property=new Property();
        property.setPropertyId(propertyJson.optInt("propertyId"));
        return property;
    }
    
    /**
     * gets a json object that represents a product and extracts data out of it and puts inside a product object
     * @param productjson
     * @return 
     */
    private Product getProductObject(JSONObject productjson)
    {
        Product product=new Product();
        product.setProductId(productjson.optInt("productId"));
        return product;
    }
    
    /**
     * gets a json object that represents a window and extracts data out of it and puts inside a window object
     * @param windowJson
     * @return 
     */
    private Window getProductWindowObject(JSONObject windowJson)
    {
        Window window=new Window();
        window.setProductId(windowJson.optInt("productId"));
        return window;
    }

    /**
     * organizes the part, it will merge parts that all the same and will put the effectall attribute inside them the way it should be
     * @param part 
     */
    private void organize(Part part,LinkedList<ItemProperty> effectalls)
    {
        for (int i = 0; i < part.getItems().size(); i++) 
        {
            Item item=part.getItems().get(i);
            for (int j = 0; j < part.getItems().size(); j++) 
            {
                if(i==j)
                    continue;
                
                Item item2=part.getItems().get(j);
                
                if(itemEqual(item,item2))
                {
                    item.setCount(item.getCount()+item2.getCount());
                    part.getItems().remove(j);
                    j--;
                }
                    
                
            }
        }
        
        for(Item item:part.getItems())
        {
            for(ItemProperty prop:effectalls)
            {
                item.getItemProperties().add(prop);
            }
        }
        
    }
    
    /**
     * compares two items and shows if they're exactly the same or not except their count
     * @param item1 
     * @param item2
     * @return true if they're the same and false if they aren't
     */
    private boolean itemEqual(Item item1,Item item2)
    {
        if(item1.getItemProperties().size()!=item2.getItemProperties().size())
            return false;
        
        for (int i = 0; i < item1.getItemProperties().size(); i++) 
        {
            ItemProperty prop1=item1.getItemProperties().get(i);
            ItemProperty prop2=item2.getItemProperties().get(i);
            
            if(prop1.getProperty().getPropertyId()!=prop2.getProperty().getPropertyId())
                return false;
            
            if(!propertyEqual(prop1.getPropertyValues(), prop2.getPropertyValues()))
                return false;
            
        }
        
        return true;
    }
    
    /**
     * shows if two properties are exactly the same or not
     * 
     * @return 
     */
    private boolean propertyEqual(LinkedList<PropertyValue> vals1,LinkedList<PropertyValue> vals2)
    {
        if(vals1.size()!=vals2.size())
        {
            return false;
        }
        
        for (int i = 0; i < vals1.size(); i++) 
        {
            if(vals1.get(i).getValueId()!=vals2.get(i).getValueId())
                return false;
        }
        
        return true;
    }
    
    public Ship getShipObject(JSONObject ship)
    {
        Ship shipObject=new Ship();
        shipObject.setAddress(new UserAddress().setAddressId(ship.optJSONObject("address").optInt("addressId")));
        shipObject.setShipType(new ShipType().setShipTypeId(ship.optJSONObject("shipType").optInt("shipTypeId")));
        return shipObject;
    }
    
    public Payment getPayemntObject(JSONObject payment)
    {
        Payment paymentObject=new Payment();
        paymentObject.setPaymentType(new PaymentType().setPaymentTypeId(payment.optJSONObject("paymentType").optInt("paymentTypeId")));
        return paymentObject;
    }
    
    public DiscountTicket getDiscountObject(JSONObject discount)
    {
        DiscountTicket discountObject=new DiscountTicket();
        discountObject.setDiscountId(discount.optInt("discountId"));
        return discountObject;
        
    }
    
}

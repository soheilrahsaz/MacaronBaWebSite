/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Orders;

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
import Objects.Product.Scale;
import Objects.Product.ScalePackage;
import Objects.Product.ScaleValue;
import Objects.Product.Window;
import Objects.Ship.Ship;
import Objects.Ship.ShipType;
import Objects.User.User;
import Objects.User.UserAddress;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class OrderJsoner {
    private final String urlPic="/MacaronBaData/Products/";
    public JSONArray getReceiptArray(LinkedList<Receipt> receipts)
    {
        JSONArray receiptsArray=new JSONArray();
        int i=0;
        for(Receipt receipt:receipts)
        {
            receiptsArray.put(i,getReceiptJson(receipt));
            i++;
        }
        return receiptsArray;
    }
    
    public JSONObject getReceiptCartJson(Receipt receipt)
    {
        JSONObject receiptObject=new JSONObject();
        receiptObject.put("orderId", receipt.getOderId());
//        receiptObject.put("date", Methods.Methods.getShamsiDate(receipt.getDate()));
        receiptObject.put("status", receipt.getStatus());
//        receiptObject.put("price", partsPrices(receipt.getParts()));
        
        receiptObject.put("user", getUserJson(receipt.getUser()));
//        receiptObject.put("ship", getShipJson(receipt.getShip()));
//        receiptObject.put("payment",getPaymentJson(receipt.getPayment()));
        receiptObject.put("parts", getPartsJsonCart(receipt.getParts()));
        return receiptObject;
    }
    
    public JSONObject getReceiptJson(Receipt receipt)
    {
        JSONObject receiptObject=new JSONObject();
        receiptObject.put("orderId", receipt.getOderId());
        receiptObject.put("date", Methods.Methods.getShamsiDate(receipt.getDate()));
        receiptObject.put("status", receipt.getStatus());
        receiptObject.put("price", partsPrices(receipt.getParts()));
        
        
        receiptObject.put("user", getUserJson(receipt.getUser()));
        receiptObject.put("ship", getShipJson(receipt.getShip()));
        receiptObject.put("payment",getPaymentJson(receipt.getPayment()));
        receiptObject.put("parts", getPartsJson(receipt.getParts()));
        return receiptObject;
    }
    private int partsPrices(LinkedList<Part> parts)
    {
        int price=0;
        for(Part part:parts)
        {
            price+=part.getFinalPrice();
        }
        return price;
    }
    /**
     * gets a linked list of part and turns them to json array
     * @param parts
     * @return 
     */
    public JSONArray getPartsJson(LinkedList<Part> parts)
    {
        JSONArray partsArray=new JSONArray();
        int i=0;
        for(Part part:parts)
        {
            partsArray.put(i,getPartJson(part));
            i++;
        }
        return partsArray;
    }
      
    /**
     * gets a linked list of part and turns them to json array
     * @param parts
     * @return 
     */
    public JSONArray getPartsJsonCart(LinkedList<Part> parts)
    {
        JSONArray partsArray=new JSONArray();
        int i=0;
        for(Part part:parts)
        {
            partsArray.put(i,getPartJsonCart(part));
            i++;
        }
        return partsArray;
    }
    /**
     * gets a part and turns it to json object
     * @param part
     * @return 
     */
    public JSONObject getPartJsonCart(Part part)
    {
        JSONObject partObject=new JSONObject();
        partObject.put("partId", part.getPartId());
        partObject.put("count", part.getCount());
        partObject.put("finalPrice", CompleteOrder.calculateProductPrice(part));
        partObject.put("window", part.isWindow());
        if(!part.isWindow())
        {
            partObject.put("product", getProductJson(part.getProduct()));
            partObject.put("scale", getScaleJson(part.getScale()));
            partObject.put("scaleValue", getScaleValueJson(part.getScaleValue()));
            partObject.put("scalePackage", getScalePackageJson(part.getScalePackage()));
            partObject.put("item",getItemsJson(part.getItems()));
            partObject.put("picture", (Methods.Methods.isNullOrEmpty(part.getScalePackage().getPicture()) ? "empty":this.urlPic+part.getProduct().getProductId()+"/"+part.getScalePackage().getPicture()));
            
        }else
        {
            partObject.put("product", getProductWindowJson(part.getProductWindow()));
            partObject.put("picture", (part.getProductWindow().getPictures().isEmpty() ? "empty":this.urlPic+part.getProductWindow().getProductId()+"/"+part.getProductWindow().getPictures().getFirst().getUrl()));
        }
            
        return partObject;
    }
    /**
     * gets a part and turns it to json object
     * @param part
     * @return 
     */
    public JSONObject getPartJson(Part part)
    {
        JSONObject partObject=new JSONObject();
        partObject.put("partId", part.getPartId());
        partObject.put("count", part.getCount());
        partObject.put("finalPrice", part.getFinalPrice());
        partObject.put("window", part.isWindow());
        if(!part.isWindow())
        {
            partObject.put("product", getProductJson(part.getProduct()));
            partObject.put("scale", getScaleJson(part.getScale()));
            partObject.put("scaleValue", getScaleValueJson(part.getScaleValue()));
            partObject.put("scalePackage", getScalePackageJson(part.getScalePackage()));
            partObject.put("item",getItemsJson(part.getItems()));
            partObject.put("picture", (Methods.Methods.isNullOrEmpty(part.getScalePackage().getPicture()) ? "empty":this.urlPic+part.getProduct().getProductId()+"/"+part.getScalePackage().getPicture()));
            
        }else
        {
            partObject.put("product", getProductWindowJson(part.getProductWindow()));
            partObject.put("picture", (part.getProductWindow().getPictures().isEmpty() ? "empty":this.urlPic+part.getProductWindow().getProductId()+"/"+part.getProductWindow().getPictures().getFirst().getUrl()));
        }
            
        return partObject;
    }
    
    /**
     * gets a linked list of items and turns them to json
     * @param items
     * @return 
     */
    public JSONArray getItemsJson(LinkedList<Item> items)
    {
        JSONArray itemsArray=new JSONArray();
        int i=0;
        for(Item item:items)
        {
            itemsArray.put(i,getItemJson(item));
            i++;
        }
        return itemsArray;
            
    }
        
    /**
     * gets an item and turns it to json object
     * @param item
     * @return 
     */
    public JSONObject getItemJson(Item item)
    {
        JSONObject itemObject=new JSONObject();
        itemObject.put("itemId", item.getItemId());
        itemObject.put("count", item.getCount());
        itemObject.put("itemProperty",getItemPropertiesJson(item.getItemProperties()) );
        return itemObject;
    }
        
    /**
     * gets a linked list of item properties and turns them to json array
     * @param properties
     * @return 
     */
    public JSONArray getItemPropertiesJson(LinkedList<ItemProperty> properties)
    {
        JSONArray itemPropertiesArray=new JSONArray();
        int i=0;
        for(ItemProperty property:properties)
        {
            itemPropertiesArray.put(i,getItemPropertyJson(property));
            i++;
        }
        return itemPropertiesArray;
    }
        
    /**
     * gets an item property and turns it to json object
     * @param property
     * @return 
     */
    public JSONObject getItemPropertyJson(ItemProperty property)
    {
        JSONObject itemPropertyObject=new JSONObject();
        itemPropertyObject.put("itemPropertyId", property.getItemPropertyId());
        itemPropertyObject.put("property",getPropertyJson(property.getProperty()) );
        itemPropertyObject.put("propertyValue",getPropertyValues(property.getPropertyValues()) );
        return itemPropertyObject;
    }
    
    /**
     * gets a linked list of property values and turns them to a json array
     * @param values
     * @return 
     */
    private JSONArray getPropertyValues(LinkedList<PropertyValue> values)
    {
        JSONArray propertiesArray=new JSONArray();
        int i=0;
        for(PropertyValue value:values)
        {
            propertiesArray.put(i,getPropertyValue(value));
            i++;
        }
        return propertiesArray;
    }
        
    /**
     * gets a propertyvalue and turns it to json
     * @param value
     * @return 
     */
    private JSONObject getPropertyValue(PropertyValue value)
    {
        JSONObject propertyObject=new JSONObject();
        propertyObject.put("valueId", value.getValueId());
        propertyObject.put("name", value.getName());
        return propertyObject;
    }
        
    /**
     * gets a property and turns it to a json object
     * @param property
     * @return 
     */
    private JSONObject getPropertyJson(Property property)
    {
        JSONObject propertyObject=new JSONObject();
        propertyObject.put("propertyId", property.getPropertyId());
        propertyObject.put("name", property.getName());
        propertyObject.put("description", property.getDescription());
        return propertyObject;
    }
    
    /**
     * gets a scale package and turns it to json with only main data
     * @param scalePackage
     * @return 
     */
    private JSONObject getScalePackageJson(ScalePackage scalePackage)
    {
        JSONObject scalePackageObject=new JSONObject();
        scalePackageObject.put("packageId", scalePackage.getPackageId());
        scalePackageObject.put("name", scalePackage.getName());
        scalePackageObject.put("description", scalePackage.getDescription());
        scalePackageObject.put("price", scalePackage.getPrice());
        return scalePackageObject;
    }
        
    
    /**
     * gets a scale value and turns it to json 
     * but with only main data . value id and value and description and effect
     * @param value
     * @return 
     */
    private JSONObject getScaleValueJson(ScaleValue value)
    {
        JSONObject valueObject=new JSONObject();
        valueObject.put("valueId", value.getValueId());
        valueObject.put("value", value.getValue());
        valueObject.put("description", value.getDescription());
        valueObject.put("effect", value.getEffect());
        return valueObject;
    }
        
    
    /**
     * gets a scale and turns it to json only with scale Id and name
     * @param scale
     * @return 
     */
    private JSONObject getScaleJson(Scale scale)
    {
        JSONObject scaleObject=new JSONObject();
        scaleObject.put("scaleId", scale.getScaleId());
        scaleObject.put("name", scale.getName());
        return scaleObject;
    }
        
    
    /**
     * gets a Window and turns it to a json object 
     * it only needs name and description and productId
     * @param window
     * @return 
     */
    private JSONObject getProductWindowJson(Window window)
    {
        JSONObject windowObject=new JSONObject();
        windowObject.put("productId", window.getProductId());
        windowObject.put("name", window.getName());
        windowObject.put("description", window.getDescription());
        return windowObject;
    }
    /**
     * gets a product and turns it to a json object 
     * it only needs name and description and productId
     * @param product
     * @return 
     */
    private JSONObject getProductJson(Product product)
    {
        JSONObject productObject=new JSONObject();
        productObject.put("productId", product.getProductId());
        productObject.put("name", product.getName());
        productObject.put("description", product.getDescription());
        return productObject;
    }
        
    
    /**
     * gets a payment and turns it to a json object
     * @param payment
     * @return 
     */
    public JSONObject getPaymentJson(Payment payment)
    {
        JSONObject paymentObject=new JSONObject();
        paymentObject.put("paymentId", payment.getPaymentId());
        paymentObject.put("price", payment.getPrice());
        paymentObject.put("approve", payment.getApprove());
        paymentObject.put("status", payment.getStatus());
        paymentObject.put("date",Methods.Methods.getShamsiDate(payment.getDate()));
        paymentObject.put("paymentType",getPaymentTypeObject(payment.getPaymentType()));
        return paymentObject;
    }
        
    /**
     * gets a PaymentType and turns it to a json object
     * @param type
     * @return 
     */
    public JSONObject getPaymentTypeObject(PaymentType type)
    {
        JSONObject typeObject=new JSONObject();
        typeObject.put("paymentTypeId", type.getPaymentTypeId());
        typeObject.put("name", type.getName());
        typeObject.put("description", type.getDescription());
        
        return typeObject;
    }
    
    /**
     * gets a user and turns it to a json object, it onlu needs user id and phone number
     * @param user
     * @return 
     */
    private JSONObject getUserJson(User user)
    {
        JSONObject userObject=new JSONObject();
//        userObject.put("userId", user.getUserId());
        userObject.put("phoneNumber", user.getPhoneNumber());
        return userObject;
    }
    /**
     * gets  a Ship and turns it to a json object
     * @param ship
     * @return 
     */
    public JSONObject getShipJson(Ship ship)
    {
        JSONObject shipObject=new JSONObject();
        shipObject.put("shipId", ship.getShipId());
        shipObject.put("date", Methods.Methods.getShamsiDate(ship.getDate()));
        shipObject.put("status", ship.getStatus());
        shipObject.put("shipType",getShipTypeJson(ship.getShipType()));
        shipObject.put("address", getAddressJson(ship.getAddress()));
        return shipObject;
    }
    
    /**
     * gets an UserAddress and turns it to a json object
     * @param address
     * @return 
     */
    public JSONObject getAddressJson(UserAddress address)
    {
        JSONObject addressObject=new JSONObject();
        addressObject.put("addressId", address.getAddressId());
        addressObject.put("address", address.getAddress());
        addressObject.put("phoneNumber", address.getPhoneNumber());
        addressObject.put("postalCode", address.getPostalCode());
        addressObject.put("latitude", address.getLatitude());
        addressObject.put("longitude", address.getLongitude());
        return addressObject;
    }
        
        
    /**
     * gets a shiptype and turns it to json
     * @param shipType
     * @return 
     */
    public JSONObject getShipTypeJson(ShipType shipType)
    {
        JSONObject shipTypeObject=new JSONObject();
        shipTypeObject.put("shipTypeId", shipType.getShipTypeId());
        shipTypeObject.put("name", shipType.getName());
        shipTypeObject.put("description", shipType.getDescription());
        return shipTypeObject;
    }
        
    public JSONArray getInpersonArray(LinkedList<InPerson> inpersons)
    {
        JSONArray inpersonArray=new JSONArray();
        int i=0;
        for(InPerson inperson:inpersons)
        {
            inpersonArray.put(i,getInpersonJson(inperson));
            i++;
        }
        return inpersonArray;
    }
    
    public JSONObject getInpersonJson(InPerson inperson)
    {
        JSONObject inpersonObject=new JSONObject();
        inpersonObject.put("inpersonId",inperson.getInpersonId());
        inpersonObject.put("date",Methods.Methods.getShamsiDate(inperson.getDate()));
        inpersonObject.put("items",getInpersonItemArray(inperson.getItems()));
        return inpersonObject;
    }
    
    public JSONArray getInpersonItemArray(LinkedList<InPersonItem> items)
    {
        JSONArray itemArray=new JSONArray();
        int i=0;
        for(InPersonItem item:items)
        {
            itemArray.put(i,getInpersonItemJson(item));
            i++;
        }
        return itemArray;
    }
    
    public JSONObject getInpersonItemJson(InPersonItem item)
    {
        JSONObject itemObject=new JSONObject();
        itemObject.put("itemId", item.getItemId());
        itemObject.put("description", item.getDescription());
        itemObject.put("price", item.getPrice());
        return itemObject;
    }
    
    public JSONArray getPaymentTypeArray(LinkedList<PaymentType> types)
    {
        JSONArray typesArray=new JSONArray();
        int i=0;
        for(PaymentType type:types)
        {
            typesArray.put(i,getPaymentTypeObject(type));
            i++;
        }
        return typesArray;
    }
    
    public JSONArray getShipTypeArray(LinkedList<ShipType> types)
    {
        JSONArray typesArray=new JSONArray();
        int i=0;
        for(ShipType type:types)
        {
            typesArray.put(i,getShipTypeJson(type));
            i++;
        }
        return typesArray;
    }
    
//    public JSONArray getOrdersTable(LinkedList<Receipt> receipts)
//    {
//        JSONArray orderTable=new JSONArray();
//        int i=0;
//        for(Receipt receipt:receipts)
//        {
//            
//        }
//        
//    }
//    
//    public JSONObject getReceiptMainDetailObject(Receipt receipt)
//    {
//        
//    }
//    
//    public JSONArray getPartsMainDetailArray(LinkedList<Part> parts)
//    {
//        JSONArray partsArray=new JSONArray();
//        int i=0;
//        for(Part part:parts)
//        {
//            partsArray.put(i,getPartMainDetailObject(part));
//            i++;
//        }
//        return partsArray;
//    }
//    
//    public JSONObject getPartMainDetailObject(Part part)
//    {
//        
//    }
        
}

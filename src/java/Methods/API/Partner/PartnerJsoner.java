/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Partner;

import Methods.API.Orders.OrderJsoner;
import Methods.API.Products.ProductJsoner;
import Objects.Partner.Partner;
import Objects.Partner.PartnerCategory;
import Objects.Partner.PartnerPart;
import Objects.Partner.PartnerProduct;
import Objects.Partner.PartnerReceipt;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class PartnerJsoner {
        private final String urlPic="/MacaronBaData/Products/";

    private ProductJsoner productJsoner;
    private OrderJsoner orderJsoner;
    public PartnerJsoner(Connection connection) throws SQLException
    {
        this.productJsoner=new ProductJsoner(connection);
        this.orderJsoner=new OrderJsoner();
    }
    
    public JSONObject getPartnerJson(Partner partner)
    {
        JSONObject partnerObject=new JSONObject();
        partnerObject.put("name", partner.getName());
        partnerObject.put("phoneNumber", partner.getPhoneNumber());
        partnerObject.put("email", partner.getEmail());
        partnerObject.put("dateRegistered", partner.getDateRegistered());
        partnerObject.put("status", partner.getStatus());
        return partnerObject;
    }
    
    public JSONArray getPartnerProductsArray(LinkedList<PartnerProduct> products,Partner partner)
    {
        JSONArray productsArray=new JSONArray();
        int i=0;
        for(PartnerProduct product:products)
        {
            if(product.getStatus()==0 || product.getStatus()==4)
                continue;
            
            productsArray.put(i,getPartnerProduct(product,partner));
            i++;
        }
        return productsArray;
    }
    
    public JSONObject getPartnerProduct(PartnerProduct product,Partner partner)
    {
        JSONObject productObject=new JSONObject();
        productObject.put("productId", product.getProductId());
        productObject.put("name", product.getName());
        productObject.put("count", product.getCount());
        productObject.put("description", product.getDescription());
        productObject.put("status", product.getStatus());
        productObject.put("price", (int)(product.getPrice()*(partner.getSeries().getEffect())/100));
        productObject.put("category",getPartnerCategoryJson(product.getCategory()));
        productObject.put("picture", this.productJsoner.getPicturesJson(product.getProductId(), product.getPictures()));
        return productObject;
    }
    
    public JSONArray getPartnerCategoriesArray(LinkedList<PartnerCategory> categories)
    {
        JSONArray categoriesArray=new JSONArray();
        int i=0;
        for(PartnerCategory category:categories)
        {
            categoriesArray.put(i,getPartnerCategoryJson(category));
            i++;
        }
        return categoriesArray;
    }
    
    public JSONObject getPartnerCategoryJson(PartnerCategory category)
    {
        JSONObject categoryObject=new JSONObject();
        categoryObject.put("categoryId", category.getId());
        categoryObject.put("name", category.getName());
        return categoryObject;
        
    }
    
    public JSONArray getPartnerReceiptArray(LinkedList<PartnerReceipt> receipts,Partner partner)
    {
        JSONArray receiptsArray=new JSONArray();
        int i=0;
        for(PartnerReceipt receipt:receipts)
        {
            if(receipt.getStatus()==0)
                continue;
            receiptsArray.put(i,getPartnerReceiptJson(receipt,partner));
            i++;
        }
        return receiptsArray;
    }
    
    public JSONObject getPartnerReceiptJson(PartnerReceipt receipt,Partner partner)
    {
        JSONObject receiptObject=new JSONObject();
        receiptObject.put("orderId", receipt.getOrderId());
        receiptObject.put("date", receipt.getDate());
        receiptObject.put("status", receipt.getStatus());
        receiptObject.put("price", getReceiptPrice(receipt));
        
        receiptObject.put("partner", getPartnerJson(receipt.getPartner()));
        receiptObject.put("ship", this.orderJsoner.getShipJson(receipt.getShip()));
        receiptObject.put("payment", this.orderJsoner.getPaymentJson(receipt.getPayment()));
        receiptObject.put("parts",getPartsArray(receipt.getParts(),partner));
        
        return receiptObject;
        
    }
    
    public JSONObject getPartnerReceiptCartJson(PartnerReceipt receipt,Partner partner)
    {
        JSONObject receiptObject=new JSONObject();
        receiptObject.put("orderId", receipt.getOrderId());
//        receiptObject.put("date", receipt.getDate());
        receiptObject.put("status", receipt.getStatus());
//        receiptObject.put("price", getReceiptPrice(receipt));
        
        receiptObject.put("partner", getPartnerJson(receipt.getPartner()));
//        receiptObject.put("ship", this.orderJsoner.getShipJson(receipt.getShip()));
//        receiptObject.put("payment", this.orderJsoner.getPaymentJson(receipt.getPayment()));
        receiptObject.put("parts",getPartsCartArray(receipt.getParts(),partner)); 
        
        return receiptObject;
        
    }
    
    public JSONArray getPartsCartArray(LinkedList<PartnerPart> parts,Partner partner)
    {
        JSONArray partsArray=new JSONArray();
        int i=0;
        for(PartnerPart part:parts)
        {
            partsArray.put(i,getPartCartJson(part,partner));
            i++;
        }
        return partsArray;
    }
    
    public JSONObject getPartCartJson(PartnerPart part,Partner partner)
    {
        JSONObject partObject=new JSONObject();
        partObject.put("partId", part.getPartId());
        partObject.put("count", part.getCount());
        partObject.put("finalPrice", (int)(part.getProduct().getPrice()*(partner.getSeries().getEffect())/100));
        partObject.put("product", getPartnerProduct(part.getProduct(),partner));
        partObject.put("picture", (part.getProduct().getPictures().isEmpty() ? "empty":this.urlPic+part.getProduct().getProductId()+"/"+part.getProduct().getPictures().getFirst().getUrl()));

                    
        return partObject;
    }
    
    
    public JSONArray getPartsArray(LinkedList<PartnerPart> parts,Partner partner)
    {
        JSONArray partsArray=new JSONArray();
        int i=0;
        for(PartnerPart part:parts)
        {
            partsArray.put(i,getPartJson(part,partner));
            i++;
        }
        return partsArray;
    }
    
    public JSONObject getPartJson(PartnerPart part,Partner partner)
    {
        JSONObject partObject=new JSONObject();
        partObject.put("partId", part.getPartId());
        partObject.put("count", part.getCount());
        partObject.put("finalPrice", part.getFinalPrice());
        partObject.put("product", getPartnerProduct(part.getProduct(),partner));
        partObject.put("picture", (part.getProduct().getPictures().isEmpty() ? "empty":this.urlPic+part.getProduct().getProductId()+"/"+part.getProduct().getPictures().getFirst().getUrl()));

        return partObject;
    }
    
    private int getReceiptPrice(PartnerReceipt receipt)
    {
        int price=0;
        for(PartnerPart part:receipt.getParts())
        {
            price+=part.getFinalPrice();
        }
        return price;
    }
}

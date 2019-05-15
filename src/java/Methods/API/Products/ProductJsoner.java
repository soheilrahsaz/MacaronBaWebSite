/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Products;

import Methods.API.User.UserJsoner;
import Objects.Communication.Comment;
import Objects.Product.Category;
import Objects.Product.ColorMap;
import Objects.Product.Picture;
import Objects.Product.Product;
import Objects.Product.Property;
import Objects.Product.PropertyValue;
import Objects.Product.Scale;
import Objects.Product.ScalePackage;
import Objects.Product.ScaleValue;
import SQL.Commands.ProductSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class is used to convert stuff to json
 * @author Moses
 */
public class ProductJsoner {
    private String urlPic="/MacaronBaData/Products/";
//    private HashMap<String,String> colorTaste=new HashMap<String,String>();
    private UserJsoner userJsoner=new UserJsoner();
    private ProductSQLCommands productSQL;
    private LinkedList<ColorMap> colorMaps;
    public ProductJsoner(Connection connection) throws SQLException
    {
        this.productSQL=new ProductSQLCommands(connection);
        this.colorMaps=this.productSQL.getColorMaps();
        
//        this.colorTaste.put("توت فرنگی", "red");
//        this.colorTaste.put("آلبالو", "red");
//        this.colorTaste.put("انار", "red");
//        this.colorTaste.put("لبو", "red");
//        this.colorTaste.put("گریپ فروت", "red");
//        this.colorTaste.put("سیب قرمز", "red");
//        this.colorTaste.put("ذغال اخته", "red");
//        this.colorTaste.put("زرشک", "red");
//        this.colorTaste.put("عناب", "red");
//        
//        
//        this.colorTaste.put("زعفران", "yellow");
//        this.colorTaste.put("انبه", "yellow");
//        this.colorTaste.put("لیمو", "yellow");
//        this.colorTaste.put("آناناس", "yellow");
//        this.colorTaste.put("به", "yellow");
//        this.colorTaste.put("گلابی", "yellow");
//        this.colorTaste.put("زردآلو", "yellow");
//        this.colorTaste.put("شلیل", "yellow");
//        this.colorTaste.put("نخودچی", "yellow");
//        this.colorTaste.put("ذرت", "yellow");
//        
//        this.colorTaste.put("بلوبری", "purple");
//        this.colorTaste.put("شاه توت", "purple");
//        this.colorTaste.put("انگور سیاه", "purple");
//        this.colorTaste.put("گیلاس", "purple");
//        this.colorTaste.put("انجیر", "purple");
//        
//        this.colorTaste.put("پسته ای", "lime");
//        this.colorTaste.put("نعنایی", "lime");
//        this.colorTaste.put("کیوی", "lime");
//        this.colorTaste.put("زیتون", "lime");
//        this.colorTaste.put("خیار", "lime");
//        this.colorTaste.put("سیب سبز", "lime");
//        this.colorTaste.put("انگور", "lime");
//        this.colorTaste.put("کرفس", "lime");
//        this.colorTaste.put("گوجه سبز", "lime");
//        this.colorTaste.put("هل", "lime");
//        this.colorTaste.put("لیمو", "lime");
//        
//        this.colorTaste.put("پرتغال", "orange");
//        this.colorTaste.put("هویج", "orange");
//        this.colorTaste.put("نارنگی", "orange");
//        this.colorTaste.put("کدوحلوایی", "orange");
//        this.colorTaste.put("طالبی", "orange");
//        this.colorTaste.put("گرمک", "orange");
//        this.colorTaste.put("خرمالو", "orange");
//        this.colorTaste.put("هلو", "orange");
//        
//        this.colorTaste.put("شکلات", "black");
//        this.colorTaste.put("شکلات تلخ", "black");
//        this.colorTaste.put("زغالی", "black");
//        this.colorTaste.put("ذغالی", "black");
//        
//        this.colorTaste.put("بادامی", "cream");
//        this.colorTaste.put("وانیل", "cream");
//        this.colorTaste.put("نارنگی", "cream");
//        this.colorTaste.put("دارچین", "cream");
//        this.colorTaste.put("موز", "cream");
//        
//        this.colorTaste.put("گل محمدی", "pink");
//        this.colorTaste.put("هندونه", "pink");
//        
//        this.colorTaste.put("قهوه", "brown");
//        this.colorTaste.put("نسکافه", "brown");
//        this.colorTaste.put("نوتلا", "brown");
        
        
    }
    
    private String findColor(String taste)
    {
        for(ColorMap colormap:colorMaps)
        {
            if(colormap.getTaste().equals(taste))
                return colormap.getColor();
        }
        return "";
    }
    
    /**
     * gets a linked list of category and turns them to json array
     * @param categories
     * @return 
     */
    public JSONArray getCategoriesArray(LinkedList<Category> categories)
    {
        JSONArray categoriesArray=new JSONArray();
        int i=0;
        for(Category category:categories)
        {
            categoriesArray.put(i,getCategoryJson(category));
            i++;
        }
        return categoriesArray;
    }
    /**
     * gets a linked list of products and turns them to jsonarray but only main details
     * @param products
     * @return 
     */
    public JSONArray getProductsJsonOnlyMainArray(LinkedList<Product> products)
    {
        JSONArray productsArray=new JSONArray();
        int i=0;
        for(Product product:products)
        {
            productsArray.put(i,getProductJsonOnlyMainJson(product));
            i++;
        }
        return productsArray;
    }
    
    /**
     * only id,name,description,base price,picture,category and status and pictures
     * @param product
     * @return 
     */
    public JSONObject getProductJsonOnlyMainJson(Product product)
    {
        JSONObject productObject=new JSONObject();
        productObject.put("productId", product.getProductId());
        productObject.put("name", product.getName());
        productObject.put("description", product.getDescription());
        productObject.put("basePrice", product.getBasePrice());
        productObject.put("picture", getPicturesJson(product.getProductId(),product.getPictures()));
        productObject.put("category", getCategoryJson(product.getCategory()));
        productObject.put("status", product.getStatus());
        return productObject;
    }
    
    /**
     * gets a linked list of comment and turns them to json array
     * @param comments
     * @return 
     */
    public JSONArray getCommentArray(LinkedList<Comment> comments)
    {
        JSONArray commentArray=new JSONArray();
        int i=0;
        for(Comment comment:comments)
        {
            commentArray.put(i,getCommentJson(comment));
            i++;
        }
        return commentArray;
    }
    /**
     * 
     * @param comment
     * @return 
     */
    public JSONObject getCommentJson(Comment comment)
    {
        JSONObject commentObject=new JSONObject();
        commentObject.put("commentId",comment.getCommentId());
        commentObject.put("comment",comment.getComment());
        commentObject.put("date",comment.getDate());
        commentObject.put("user",this.userJsoner.getUserInfoObject(comment.getUser()));
        return commentObject;
    }
    
    /**
     * gets a product and turns it to a json
     * @param product
     * @return 
     */
    public JSONObject getProductJson(Product product,LinkedList<Comment> comments)
    {
        JSONObject productObject=new JSONObject();
        productObject.put("productId", product.getProductId());
        productObject.put("name", product.getName());
        productObject.put("description", product.getDescription());
        productObject.put("basePrice", product.getBasePrice());
        productObject.put("status", product.getStatus());
        
        productObject.put("comment", getCommentArray(comments));
        productObject.put("category", getCategoryJson(product.getCategory()));
        productObject.put("picture", getPicturesJson(product.getProductId(),product.getPictures()));
        productObject.put("scale", getScaleJson(product.getProductId(),product.getScale()));
        productObject.put("property",getPropertiesJson(product.getProperties()));
        
        return productObject;
    }
    
    /**
     * gets a product and turns it to a json
     * @param product
     * @return 
     */
    public JSONObject getProductJson(Product product)
    {
        JSONObject productObject=new JSONObject();
        productObject.put("productId", product.getProductId());
        productObject.put("name", product.getName());
        productObject.put("description", product.getDescription());
        productObject.put("basePrice", product.getBasePrice());
        productObject.put("status", product.getStatus());
        
        productObject.put("category", getCategoryJson(product.getCategory()));
        productObject.put("picture", getPicturesJson(product.getProductId(),product.getPictures()));
        productObject.put("scale", getScaleJson(product.getProductId(),product.getScale()));
        productObject.put("property",getPropertiesJson(product.getProperties()));
        
        return productObject;
    }
    
    /**
     * gets a property and turns it to a json object
     * @param properties
     * @return 
     */
    public JSONArray getPropertiesJson(LinkedList<Property> properties)
    {
        JSONArray propertiesArray=new JSONArray();
        int i=0;
        for(Property property:properties)
        {
            if(property.getStatus()!=2)
                continue;
            
            propertiesArray.put(i,getPropertyJson(property));
            i++;
        }
        return propertiesArray;
    }
    
    /**
     * gets a property and turns it to a json object
     * @param property
     * @return 
     */
    public JSONObject getPropertyJson(Property property)
    {
        JSONObject propertyObject=new JSONObject();
        propertyObject.put("propertyId", property.getPropertyId());
        propertyObject.put("name", property.getName());
        propertyObject.put("description", property.getDescription());
        propertyObject.put("multiple", property.isMultiple());
        propertyObject.put("effectAll", property.isEffectAll());
        if(property.getPropertyId()==1||property.getPropertyId()==2)
        {
            propertyObject.put("value", getPropertyValuesWithColorJson(property.getValues()));
        }else
        {
            propertyObject.put("value", getPropertyValuesJson(property.getValues()));
        }
        
        return propertyObject;
    }
    
    /**
     * gets a linked list of property values and turns them to a json array but with color attribute 
     * static colors got from hashmap
     * @param values
     * @return 
     */
    public JSONArray getPropertyValuesWithColorJson(LinkedList<PropertyValue> values)
    {
        JSONArray valuesArray=new JSONArray();
        int i=0;
        for(PropertyValue value:values)
        {
            valuesArray.put(i,getPropertyValueWithColorJson(value));
            i++;
        }
        return valuesArray;
    }
    
    /**
     * gets a property value and turns it to a json object
     * like below \n
     * "valueId": 380411954,
          "name": "توت فرنگی",
          "priceByScale": 0
     * @param value
     * @return 
     */
    public JSONObject getPropertyValueWithColorJson(PropertyValue value)
    {
        JSONObject valueObject=new JSONObject();
        valueObject.put("valueId", value.getValueId());
        valueObject.put("name", value.getName());
        valueObject.put("color", findColor(value.getName()));
        valueObject.put("price", value.getPriceByScale());
        return valueObject;
    }
    
    /**
     * gets a linked list of property values and turns them to a json array
     * @param values
     * @return 
     */
    public JSONArray getPropertyValuesJson(LinkedList<PropertyValue> values)
    {
        JSONArray valuesArray=new JSONArray();
        int i=0;
        for(PropertyValue value:values)
        {
            valuesArray.put(i,getPropertyValueJson(value));
            i++;
        }
        return valuesArray;
            
    }
        
    /**
     * gets a property value and turns it to a json object
     * like below \n
     * "valueId": 380411954,
          "name": "توت فرنگی",
          "priceByScale": 0
     * @param value
     * @return 
     */
    public JSONObject getPropertyValueJson(PropertyValue value)
    {
        JSONObject valueObject=new JSONObject();
        valueObject.put("valueId", value.getValueId());
        valueObject.put("name", value.getName());
        valueObject.put("price", value.getPriceByScale());
        return valueObject;
    }
        
    
    /**
     * gets a scale and turns it to a json object
     * @param scale
     * @return 
     */
    public JSONObject getScaleJson(int productId,Scale scale)
    {
        JSONObject scaleObject=new JSONObject();
        scaleObject.put("scaleId", scale.getScaleId());
        scaleObject.put("name", scale.getName());
        scaleObject.put("value", getScaleValuesJson(productId,scale.getValues()));
        return scaleObject;
    }
    
    /**
     * gets a linked list of values and turns them to a json array
     * @param values
     * @return 
     */
    public JSONArray getScaleValuesJson(int productId,LinkedList<ScaleValue> values)
    {
        JSONArray scaleValuesArray=new JSONArray();
        int i=0;
        for(ScaleValue value:values)
        {
            scaleValuesArray.put(i,getScaleValueJson(productId,value));
            i++;
        }
        return scaleValuesArray;
    }
    
    /**
     * gets a Scale Value object and turns it to a json object
     * @param value
     * @return 
     */
    public JSONObject getScaleValueJson(int productId,ScaleValue value)
    {
        JSONObject valueObject=new JSONObject();
        valueObject.put("valueId", value.getValueId());
        valueObject.put("value", value.getValue());
        valueObject.put("description", value.getDescription());
        valueObject.put("picture",this.urlPic+productId+"/"+ value.getPicture());
        valueObject.put("effect", value.getEffect());
        valueObject.put("packages", getScalePackagesJson(productId,value.getPackages()));
        return valueObject;
    }
    
    /**
     * gets a linked list of scale packages and turns them to a json array
     * @param scalePackages
     * @return 
     */
    public JSONArray getScalePackagesJson(int productId,LinkedList<ScalePackage> scalePackages)
    {
        JSONArray scalePackagesArray=new JSONArray();
        int i=0;
        for(ScalePackage scalePackage:scalePackages)
        {
            scalePackagesArray.put(i, getScalePackageJson(productId,scalePackage));
            i++;
        }
        return scalePackagesArray;
    }
        
    /**
     * gets a scale package and turns it to a json object
     * @param scalePackage
     * @return 
     */
    public JSONObject getScalePackageJson(int productId,ScalePackage scalePackage)
    {
        JSONObject scalePackageObject=new JSONObject();
        scalePackageObject.put("packageId", scalePackage.getPackageId());
        scalePackageObject.put("name", scalePackage.getName());
        scalePackageObject.put("description", scalePackage.getDescription());
        scalePackageObject.put("picture",this.urlPic+productId+"/"+ scalePackage.getPicture());
        scalePackageObject.put("price", scalePackage.getPrice());
        return scalePackageObject;
    }
        
        
        
    /**
     * gets a linked list of pictures and turns them to a json array
     * @param pictures
     * @return 
     */
    public JSONArray getPicturesJson(int productId,LinkedList<Picture> pictures)
    {
        JSONArray pictureArray=new JSONArray();
        int i=0;
        for(Picture picture:pictures)
        {
            pictureArray.put(i, getPictureJson(productId,picture));
            i++;
        }
        return pictureArray;
    }
    
    /**
     * gets a picture and turns it to json
     * as below sample\n
     * "pictureId": 859066978,
      "url": "ykybh.jpeg"
     * @param picture
     * @return 
     */
    public JSONObject getPictureJson(int productId,Picture picture)
    {
        JSONObject pictureObject=new JSONObject();
        pictureObject.put("pictureId", picture.getPictureId());
        pictureObject.put("url", this.urlPic+productId+"/"+picture.getUrl());
        return pictureObject;
    }
    /**
     * gets a category and turns it to json 
     * as below sample
     * "category": {
    "categoryId": 3,
    "name": "ماکارون"
  }
     * @param category
     * @return 
     */
    public JSONObject getCategoryJson(Category category)
    {
        JSONObject categoryObject=new JSONObject();
        categoryObject.put("categoryId", category.getCategoryId());
        categoryObject.put("name", category.getName());
        return categoryObject;
    }
}

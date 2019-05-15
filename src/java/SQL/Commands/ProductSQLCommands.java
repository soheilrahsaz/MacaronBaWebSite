/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import ExceptionsChi.DoesntExistException;
import Objects.Product.Category;
import Objects.Product.CategoryList;
import Objects.Product.ColorMap;
import Objects.Product.Product;
import Objects.Product.Picture;
import Objects.Product.Property;
import Objects.Product.PropertyValue;
import Objects.Product.Scale;
import Objects.Product.ScalePackage;
import Objects.Product.ScaleValue;
import Objects.Product.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Random;

/**
 * stores necessary SQL commands for product activity
 * @author Moses
 */
public class ProductSQLCommands {
    private Connection connection;
    
    public ProductSQLCommands(Connection connection)
    {
        this.connection=connection;
    }
    /**
     * retrieves scale values from product_scale_value by scale id
     * @param scaleId the id of the scale
     * @return a linked list of scale values
     * @throws SQLException 
     */
    public LinkedList<ScaleValue> getScaleValues(int scaleId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_scale_value WHERE `scaleId`=?  AND `status`!=-1 ORDER BY value ASC");
        prep.setInt(1, scaleId);
        ResultSet res=prep.executeQuery();
        LinkedList<ScaleValue> values=new LinkedList<ScaleValue>();
        while(res.next())
        {
            int scaleValue=res.getInt("valueId");
            ScaleValue value=new ScaleValue();
            value.setValaueId(scaleValue)
                    .setValue(res.getDouble("value"))
                    .setDescription(res.getString("description"))
                    .setPackages(getScalePackages(scaleValue))
                    .setPicture(res.getString("picture"))
                    .setEffect(res.getDouble("effect"));
            values.add(value);
        }
       
        return values;
    }
    /**
     * retrieves a sclae value
     * @param scaleValue
     * @return
     * @throws SQLException 
     */
    public ScaleValue getScaleValue(int scaleValue) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_scale_value` WHERE `valueId`=?");
        prep.setInt(1, scaleValue);
        ResultSet res=prep.executeQuery();
        ScaleValue value=new ScaleValue();
        if(!res.first())
        {
            return value;
        }
        value.setValaueId(res.getInt("valueId"))
                .setValue(res.getDouble("value")).setDescription(res.getString("description"))
                .setPackages(getScalePackages(scaleValue))
                .setPicture(res.getString("picture"))
                .setEffect(res.getDouble("effect"));
        return value;
        
    }
    
    /**
     * retrieves all packages attached to a scale value
     * @param scaleValueId
     * @return a linked list of scalePackage
     * @throws SQLException 
     */
    public LinkedList<ScalePackage> getScalePackages(int scaleValueId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `scale_package` WHERE `scaleValueId`=? AND `status`!=-1");
        prep.setInt(1, scaleValueId);
        ResultSet res=prep.executeQuery();
        LinkedList<ScalePackage> scalePackages=new LinkedList<ScalePackage>();
        while(res.next())
        {
            ScalePackage scalePackage=new ScalePackage();
            scalePackage.setDescription(res.getString("description")).setName(res.getString("name")).setPackageId(res.getInt("packageId"))
                    .setPicture(res.getString("picture")).setPrice(res.getInt("price"))
                    .setScaleId(scaleValueId).setStatus(res.getInt("status"));
            scalePackages.add(scalePackage);
        }
        return scalePackages;
    }
    
    /**
     * 
     * @param packageId
     * @return true if it exists and false if it doesnt 
     * @throws SQLException 
     */
    public boolean scalePackageIdExists(int packageId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `scale_package` WHERE `packageId`=?");  
        prep.setInt(1, packageId); 
        return prep.executeQuery().first();
    }  
    
    /**
     * adds a scale package to data base 
     * @param scaleValue
     * @param name
     * @param description
     * @param picture
     * @param price
     * @return the id of the added package
     * @throws SQLException 
     */
    public int addScalePackage(int scaleValue,String name,String description,String picture,int price) throws SQLException
    {
        int scalePackageId;
        Random random=new Random();
        while(true)
        {
            scalePackageId=Math.abs(random.nextInt());
            if(!scalePackageIdExists(scalePackageId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `scale_package` (`packageId`,`scaleValueId`,`name`,`description`,`picture`,`price`) VALUES (?,?,?,?,?,?)");
        prep.setInt(1, scalePackageId);
        prep.setInt(2, scaleValue);
        prep.setString(3, name);
        prep.setString(4, description);
        prep.setString(5, picture);
        prep.setInt(6, price);
        prep.executeUpdate();
        
        return scalePackageId;
    }
        
        
        
    /**
     * retrieves a scale from product_scale and its values inside it
     * @param scaleId
     * @return a Scale object
     * @throws SQLException 
     */
    public Scale getScale(int scaleId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_scale WHERE `scaleId`=? ");
        prep.setInt(1, scaleId);
        ResultSet res=prep.executeQuery();
        Scale scale=new Scale();
        if(res.first())
        {
            scale.setScaleId(scaleId).setName(res.getString("name")).setValues(getScaleValues(scaleId));
        }
        
        return scale;
    }
    
    /**
     * retrieves a scale from product_scale without its values inside it
     * @param scaleId
     * @return a Scale object
     * @throws SQLException 
     */
    public Scale getScaleOnlyDetail(int scaleId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_scale WHERE `scaleId`=? ");
        prep.setInt(1, scaleId);
        ResultSet res=prep.executeQuery();
        Scale scale=new Scale();
        if(res.first())
        {
            scale.setScaleId(scaleId).setName(res.getString("name"));
        }
        
        return scale;
    }
    /**
     * retrieves all property value from product_property_value based on property Id 
     * @param propertyId
     * @return a linked list of PropertyValue
     * @throws SQLException 
     */
    public LinkedList<PropertyValue> getPropertyValues(int propertyId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_property_value WHERE `propertyId`=? AND `status`!=-1");
        prep.setInt(1, propertyId);
        ResultSet res=prep.executeQuery();
        LinkedList<PropertyValue> values=new LinkedList<PropertyValue>();
        while(res.next())
        {
            PropertyValue value=new PropertyValue();
            value.setName(res.getString("name")).setPriceByScale(res.getInt("priceByScale")).setValueId(res.getInt("valueId"));
            values.add(value);
        }
        return values;
            
    }
    /**
     * retrieves all properties attached to a product by product id from product_property table
     * @param productId
     * @return a linked list of properties 
     * @throws SQLException 
     */
    public LinkedList<Property> getProperties(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_property WHERE `productId`=? AND `status`!=-1 ORDER BY `propertyId` ASC");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        LinkedList<Property> properties=new LinkedList<Property>();
        while(res.next())
        {
            Property property=new Property();
            property.setName(res.getString("name")).setPropertyId(res.getInt("propertyId"))
                    .setStatus(res.getInt("status")).setMultiple(res.getBoolean("multiple")).setEffectAll(res.getBoolean("effectAll"))
                    .setValues(getPropertyValues(res.getInt("propertyId"))).setDescription(res.getString("description"));
            properties.add(property);
        }
        
        return properties;
        
    }
    /**
     * retrieves all pictures from a product by product Id from product_picture table
     * @param productId
     * @return
     * @throws SQLException 
     */
    public LinkedList<Picture> getProductPictures(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_picture WHERE `productId`=?");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        LinkedList<Picture> pictures=new LinkedList<Picture>();
        while(res.next())
        {
            Picture picture=new Picture();
            picture.setPictureId(res.getInt("pictureId")).setUrl(res.getString("url"));
            pictures.add(picture);
        }
        return pictures;
           
    }
    
    public Category getCategory(int categoryId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_category WHERE `categoryId`=?");
        prep.setInt(1, categoryId);
        ResultSet res=prep.executeQuery();
        Category category=new Category();
        if(res.first())
        {
            category.setCategoryId(categoryId).setName(res.getString("name"));
        }
        return category;
    }
    /**
     * retrieves a product completely with all of its contents
     * @param productId
     * @return a product Object
     * @throws SQLException 
     */
    public Product getProduct(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product WHERE `productId`=? ");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        Product product=new Product();
        if(res.first())
        {
            product.setProductId(productId).setName(res.getString("name"))
                    .setScale(getScale(res.getInt("scaleId")))
                    .setBasePrice(res.getInt("basePrice"))
//                    .setBaseScale()
                    .setDescription(res.getString("description"))
                    .setPicture(getProductPictures(productId))
                    .setProperties(getProperties(productId))
                    .setCategory(getCategory(res.getInt("categoryId")))
                    .setStatus(res.getInt("status"));
            
        }
        
        return product;
    }
    /**
     * retrieves all products but just with enought detail to show in tables
     * @return linklist of products
     * @throws SQLException 
     */
    public LinkedList<Product> getAllProductsForShow() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Product> products=new LinkedList<Product>();
        while(res.next())
        {
            Product product=new Product();
            int productId=res.getInt("productId");
            product.setProductId(productId).setName(res.getString("name")).setDescription(res.getString("description"))
                    .setBasePrice(res.getInt("basePrice"))
                    .setPicture(getProductPictures(productId))
                    .setCategory(getCategory(res.getInt("categoryId")))
                    .setStatus(res.getInt("status"));
            products.add(product);
        }
        
        return products;
    }
    
    public LinkedList<Product> getAllProductsForShowUser() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product WHERE `status`>0 AND `status`<4");
        ResultSet res=prep.executeQuery();
        LinkedList<Product> products=new LinkedList<Product>();
        while(res.next())
        {
            Product product=new Product();
            int productId=res.getInt("productId");
            product.setProductId(productId).setName(res.getString("name")).setDescription(res.getString("description"))
                    .setBasePrice(res.getInt("basePrice"))
                    .setPicture(getProductPictures(productId))
                    .setCategory(getCategory(res.getInt("categoryId")))
                    .setStatus(res.getInt("status"));
            products.add(product);
        }
        
        return products;
    }
    
    /**
     * retrieves all products completely and full content
     * @return linklist of products
     * @throws SQLException 
     */
    public LinkedList<Product> getAllProducts() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Product> products=new LinkedList<Product>();
        while(res.next())
        {
            Product product=new Product();
            int productId=res.getInt("productId");
            product.setProductId(productId).setName(res.getString("name"))
                    .setScale(getScale(res.getInt("scaleId"))).setBasePrice(res.getInt("basePrice"))
//                    .setBaseScale()
                    .setPicture(getProductPictures(productId))
                    .setProperties(getProperties(productId))
                    .setCategory(getCategory(res.getInt("categoryId")))
                    .setStatus(res.getInt("status"));
            products.add(product);
        }
        
        return products;
    }
    /**
     * adds a scale value
     * @param scaleId id of the scale
     * @param value value 
     * @param effect 
     * @param description 
     * @param picture 
     * @throws SQLException 
     * @return the id of the added scale value
     */
    public int addScaleValue(int scaleId,double value,double effect,String description,String picture) throws SQLException
    {
        int valueId;
        while(true)
        {
            valueId=Math.abs(new Random().nextInt());
            if(!scaleValueIdExists(valueId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO product_scale_value (`scaleId`,`valueId`,`value`,`effect`,`description`,`picture`) VALUES (?,?,?,?,?,?)");
        prep.setInt(1, scaleId);
        prep.setInt(2, valueId);
        prep.setDouble(3, value);
        prep.setDouble(4, effect);
        prep.setString(5, description);
        prep.setString(6, picture);
        prep.executeUpdate();
        
        return valueId;
    }
    /**
     * adds an scale to data base
     * @param name name of the scale
     * @throws SQLException 
     * @return id of the added scale
     */
    public int addScale(String name) throws SQLException
    {
        int scaleId;
        while(true)
        {
            scaleId=Math.abs(new Random().nextInt());
            if(!scaleIdExists(scaleId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO product_scale (`scaleId`,`name`) VALUE (?,?)");
        prep.setInt(1, scaleId);
        prep.setString(2, name);
        prep.executeUpdate();
        
        return scaleId;
    }
    /**
     * adds an property value 
     * @param propertyId the parent property id
     * @param name the name of the property value
     * @param priceByScale increased price by scale
     * @return the id of the added property
     * @throws SQLException 
     */
    public int addPropertyValue(int propertyId,String name,int priceByScale) throws SQLException
    {
        int valueId;
        while(true)
        {
            valueId=Math.abs(new Random().nextInt());
            if(!propertyValueIdExists(valueId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO product_property_value (`propertyId`,`valueId`,`name`,`priceByScale`) VALUES (?,?,?,?)");
        prep.setInt(1, propertyId);
        prep.setInt(2, valueId);
        prep.setString(3, name);
        prep.setInt(4, priceByScale);
        prep.executeUpdate();
        
        return valueId;
    }
    
    /**
     * adds an property for a product in data base
     * @param productId
     * @param name the name of the property
     * @param multiple
     * @return the id of the added property
     * @throws SQLException 
     */
    public int addProperty(int productId,String name,boolean multiple,boolean effectAll,String description) throws SQLException
    {
        int propertyId;
        while(true)
        {
            propertyId=Math.abs(new Random().nextInt());
            if(!propertyIdExists(propertyId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO product_property (`productId`,`propertyId`,`name`,`multiple`,`effectAll`,`description`) VALUES (?,?,?,?,?,?)");
        prep.setInt(1, productId);
        prep.setInt(2, propertyId);
        prep.setString(3, name);
        prep.setBoolean(4, multiple);
        prep.setBoolean(5, effectAll);
        prep.setString(6, description);
        prep.executeUpdate();
        return productId;
    }
    
    /**
     * adds a product picture to data base
     * @param productId the parent product id
     * @return the id of the added picture
     * @param url the url path of the picture
     * @throws SQLException 
     */
    public int addProductPicture(int productId,String url) throws SQLException
    {
        int pictureId;
        while(true)
        {
            pictureId=Math.abs(new Random().nextInt());
            if(!pictureIdExists(pictureId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO product_picture (`productId`,`pictureId`,`url`) VALUES (?,?,?)");
        prep.setInt(1, productId);
        prep.setInt(2, pictureId);
        prep.setString(3, url);
        prep.executeUpdate();
        
        return pictureId;
    }
    
    /**
     * adds a product to data base 
     * @param categoryId the parent category of the product
     * @param name the name of the product
     * @param scaleId scale of sale
     * @param basePrice base price of the product
     * @return the id of the added product
     * @throws SQLException 
     */
    public int addProduct(int categoryId,String name,String description,int scaleId,int basePrice) throws SQLException
    {
        int productId;
        while(true)
        {
            productId=Math.abs(new Random().nextInt());
            if(!productIdExists(productId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO product (`productId`,`name`,`scaleId`,`basePrice`,`categoryId`,`description`) VALUES (?,?,?,?,?,?)");
        prep.setInt(1, productId);
        prep.setString(2, name);
        prep.setInt(3, scaleId);
        prep.setInt(4, basePrice);
        prep.setInt(5, categoryId);
        prep.setString(6, description);
        prep.executeUpdate();
        
        return productId;
    }
    
    /**
     * adds a product to data base 
     * @param categoryId the parent category of the product
     * @param name the name of the product
     * @param scaleId scale of sale
     * @param basePrice base price of the product
     * @return the id of the added product
     * @throws SQLException 
     */
    public int addProduct(int categoryId,String name,int basePrice) throws SQLException
    {
        int productId;
        while(true)
        {
            productId=Math.abs(new Random().nextInt());
            if(!productIdExists(productId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO product (`productId`,`name`,`basePrice`,`categoryId`) VALUES (?,?,?,?)");
        prep.setInt(1, productId);
        prep.setString(2, name);
        prep.setInt(3, basePrice);
        prep.setInt(4, categoryId);
        prep.executeUpdate();
        
        return productId;
    }
    
    /**
     * set's scale id into product in product table
     * @param productId the id of the product
     * @param scaleId the id of the scale to be set
     * @throws SQLException 
     */
    public void setScale(int productId,int scaleId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE product SET `scaleId`=? WHERE `productId`=?");
        prep.setInt(1, scaleId);
        prep.setInt(2, productId);
        prep.executeUpdate();
    }
    
    /**
     * retrieves the main head of the category which has no parent (it's parent id is 0)
     * @return a CategoryObject
     * @throws SQLException 
     */
    public CategoryList getHeadCategory() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_category WHERE `parent`=0");
        ResultSet res=prep.executeQuery();
        CategoryList category=new CategoryList();
        res.first();
        category.setCategoryId(res.getInt("categoryId")).setName(res.getString("name"));
        return category;
    }
    
    /**
     * retrieves the children of a parent and the children of the children and the children of the children of the children and ... :D
     * @param parent
     * @return
     * @throws SQLException 
     */
    public LinkedList<CategoryList> getChildrenOf(CategoryList parent) throws SQLException
    {
        int parentId=parent.getCategoryId();
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_category WHERE `parent`=?");
        prep.setInt(1, parentId);
        ResultSet res=prep.executeQuery();
        LinkedList<CategoryList> categories=new LinkedList<CategoryList>();
        while(res.next())
        {
            CategoryList category=new CategoryList();
            category.setCategoryId(res.getInt("categoryId")).setParent(parent).setName(res.getString("name"))
                    .setChildren(getChildrenOf(category));
            categories.add(category);
        }
        
        return categories;
    }
    
    
    /**
     * retrieves all scales with their values inside 
     * @return a linked list of scales 
     * @throws SQLException 
     */
    public LinkedList<Scale> getScales() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_scale WHERE `status`!=-1 GROUP BY `name`");
        ResultSet res=prep.executeQuery();
        LinkedList<Scale> scales=new LinkedList<Scale>();
        while(res.next())
        {
            int scaleId=res.getInt("scaleId");
            Scale scale=new Scale();
            scale.setName(res.getString("name")).setScaleId(scaleId).setValues(getScaleValues(scaleId));
            scales.add(scale);
        }
        return scales;
    }
    
    /**
     * gets a name and show if the name is already used in product list or not
     * @param name
     * @return true if the name already exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean productNameExists(String name) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM product WHERE `name`=? AND `status`!=-1");
        prep.setString(1, name);
        ResultSet res=prep.executeQuery();
        return res.first();
    }
    
    /**
     * retrieves all categories from product_category table
     * @return a linked list of category
     * @throws SQLException 
     */
    public LinkedList<Category> getAllCategories() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_category");
        ResultSet res=prep.executeQuery();
        LinkedList<Category> categories=new LinkedList<Category>();
        while(res.next())
        {
            Category category=new Category();
            category.setCategoryId(res.getInt("categoryId")).setName(res.getString("name"));
            categories.add(category);
        }
        
        return categories;
        
    }
    
    /**
     * retrieves category Id by category name
     * @param Category the name of the category
     * @return int representing id of the category
     * @throws SQLException 
     */
    public int getCategoryId(String Category) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareCall("SELECT `categoryId` FROM product_category WHERE `name`=?");
        prep.setString(1, Category);
        ResultSet res=prep.executeQuery();
        res.first();
        return res.getInt("categoryId");
    }
    
    /**
     * checks to see if the unique id is already used or not 
     * @param id
     * @return
     * @throws SQLException 
     */
    public boolean productIdExists(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product WHERE `productId`=? ");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        if(res.first())
        {
            return true;
        }
        
        return productWindowIdExists(id);
    }
    
    /**
     * shows if productId exists or not
     * @param productId
     * @return true if yes
     * @throws SQLException 
     */
    public boolean partnerProductIdExists(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `partner_product` WHERE `productId`=?");
        prep.setInt(1, productId);
        return prep.executeQuery().first();
    }
    
    private boolean productWindowIdExists(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_window` WHERE `productId`=? ");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        if(res.first())
        {
            return true;
        }
        
        return partnerProductIdExists(id);
    }
     /**
     * checks to see if the unique id is already used or not 
     * @param id
     * @return
     * @throws SQLException 
     */
    public boolean pictureIdExists(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_picture WHERE `pictureId`=?");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        return res.first();
    }
     /**
     * checks to see if the unique id is already used or not 
     * @param id
     * @return
     * @throws SQLException 
     */
    public boolean propertyIdExists(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_property WHERE `propertyId`=? ");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        return res.first();
    }
     /**
     * checks to see if the unique id is already used or not 
     * @param id
     * @return
     * @throws SQLException 
     */
    public boolean propertyValueIdExists(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_property_value WHERE `propertyId`=?");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        return res.first();
    }
        /**
     * checks to see if the unique id is already used or not 
     * @param id
     * @return
     * @throws SQLException 
     */ 
    public boolean scaleIdExists(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_scale WHERE `scaleId`=? ");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        return res.first();
    }
     /**
     * checks to see if the unique id is already used or not 
     * @param id
     * @return
     * @throws SQLException 
     */    
    public boolean scaleValueIdExists(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_scale_value WHERE `valueId`=? ");
        prep.setInt(1, id);
        ResultSet res=prep.executeQuery();
        return res.first();
    }
        
    /**
     * retrieves a property
     * @param propertyId
     * @return a property objcet
     * @throws SQLException 
     */
    public Property getProperty(int propertyId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_property WHERE `propertyId`=?");
        prep.setInt(1, propertyId);
        ResultSet res=prep.executeQuery();
        res.first();
        
        Property property=new Property();
        property.setName(res.getString("name")).setPropertyId(res.getInt("propertyId"))
                    .setStatus(res.getInt("status")).setMultiple(res.getBoolean("multiple")).setEffectAll(res.getBoolean("effectAll"))
                .setValues(getPropertyValues(res.getInt("propertyId"))).setDescription(res.getString("description"));
        return property;
        
    }
    
    /**
     * retrieves the id of the scale
     * @param scaleName
     * @return
     * @throws SQLException 
     */   
    public int getScaleId(String scaleName) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `scaleId` FROM product_scale WHERE `name`=? AND `status`!=-1");
        prep.setString(1, scaleName);
        ResultSet res=prep.executeQuery();
        res.first();
        return res.getInt("scaleId");
    }
    
    /**
     * checks to see if the product name is already in the table excepet the product id that's being edited
     * @param name the name of the product
     * @param productId the exception product id
     * @return true if it already exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean productNameExists(String name,int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `productId` FROM product WHERE `name`=?  AND `status`!=-1");
        prep.setString(1, name);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return false;
        }
        
        int secondId=res.getInt("productId");
        if(secondId==productId)
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * edits an already existing product
     * @param productId the id of the product that is being edited
     * @param name
     * @param categoryId
     * @param description
     * @param price
     * @throws SQLException 
     */
    public void editProduct(int productId,String name,int categoryId,String description,int price,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE product SET `name`=? , `description`=? , `basePrice`=? ,`categoryId`=?,`status`=? WHERE `productId`=?");
        prep.setString(1, name);
        prep.setString(2, description);
        prep.setInt(3, price);
        prep.setInt(4, categoryId);
        prep.setInt(5, status);
        prep.setInt(6, productId);
        prep.executeUpdate();
    }
    
    /**
     * retrieves a single picture from data base 
     * @param pictureId the id of the returning picture
     * @return a picture object
     * @throws SQLException 
     */
    public Picture getPicture(int pictureId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM product_picture WHERE `pictureId`=?");
        prep.setInt(1, pictureId);
        ResultSet res=prep.executeQuery();
        Picture picture=new Picture();
        if(res.first())
        {
            picture.setPictureId(pictureId).setUrl(res.getString("url")).setProductId(res.getInt("productId"));
        }
        return picture;
    } 
    
    /**
     * deletes a picture from data base
     * @param pictureId the id of the deleting picture
     * @throws SQLException 
     */
    public void deletePicture(int pictureId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM  product_picture WHERE `pictureId`=?");
        prep.setInt(1, pictureId);
        prep.execute();
    }
    
    /**
     * edits the name of a scale
     * @param scaleId the id of the scale that need editing
     * @param name the second name
     * @throws SQLException 
     */
    public void editScale(int scaleId,String name) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE product_scale SET `name`=? WHERE `scaleId`=?");
        prep.setString(1, name);
        prep.setInt(2, scaleId);
        prep.executeUpdate();
    }
    
    /**
     * checks to see if the scale value already exists under the scale id or not 
     * @param scaleId the parent scale id
     * @param value the new value wanting to add
     * @return true if it already exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean scaleValueExist(int scaleId,double value) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM product_scale_value WHERE `value`=? AND `scaleId`=? AND `status`!=-1 ");
        prep.setDouble(1, value);
        prep.setInt(2, scaleId);
        ResultSet res=prep.executeQuery();
        return res.first();
    }
    
    /**
     * deletes a scale value from data base
     * @param valueId the id of the value to be deleted
     * @throws SQLException 
     */
    public void deleteScaleValue(int valueId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_scale_value` SET `status`=-1 WHERE `valueId`=?");
        prep.setInt(1, valueId);
        prep.execute();
    }
    
    /**
     * shows that if the product already has this property or not
     * @param productId the id of the parent product
     * @param name the name of adding property
     * @return true if it already exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean propertyExists(int productId,String name) throws SQLException, SQLException, SQLException, SQLException, SQLException, SQLException, SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `product_property` WHERE  `productId`=? AND `name`=? AND `status`!=-1");
        prep.setInt(1, productId);
        prep.setString(2, name);
        return prep.executeQuery().first();
    }
    
    /**
     * shows that if the property already has this value or not
     * @param propertyId the id of the parent property
     * @param name the name of adding value
     * @return true if it already exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean propertyValueExists(int propertyId,String name) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `product_property_value` WHERE  `propertyId`=? AND `name`=? AND `status`!=-1");
        prep.setInt(1, propertyId);
        prep.setString(2, name);
        return prep.executeQuery().first();
    }
    
    /**
     * sets the status of the product in the data base to -1
     * @param productId id of the product to be deleted
     * @throws SQLException 
     */
    public void deleteProduct(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product` SET `status`=-1 WHERE `productId`=?");
        prep.setInt(1, productId);
        prep.executeUpdate();
    }
    
    /**
     * deletes a property from database
     * @param propertyId the id of the property
     * @throws SQLException 
     */
    public void deleteProperty(int propertyId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_property` SET `status`=-1 WHERE `propertyId`=?");
        prep.setInt(1, propertyId);
        prep.executeUpdate();
    }
    
    /**
     * deletes all the of the values attached to a property
     * @param propertyId the id of the parent property
     * @throws SQLException 
     */
    public void deletePropertyValues(int propertyId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_property_value` SET `status`=-1 WHERE `propertyId`=?");
        prep.setInt(1, propertyId);
        prep.executeUpdate();
    }
    
    /**
     * deletes a value from database
     * @param valueId the id of the value
     * @throws SQLException 
     */
    public void deletePropertyValue(int valueId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_property_value` SET `status`=-1 WHERE `valueId`=?");
        prep.setInt(1, valueId);
        prep.executeUpdate();
    }
    
    /**
     * shows if the name is already in use by other properties or not 
     * @param propertyId the exception property id
     * @param name the new name 
     * @return true if it's in use and false if it's not
     * @throws SQLException 
     */
    public boolean propertyExistsExcept(int productId,int propertyId,String name)throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `propertyId` FROM `product_property` WHERE `name`=? AND `productId`=? AND `propertyId`!=? AND `status`!=-1");
        prep.setString(1, name); 
        prep.setInt(2, productId);
                
        prep.setInt(3, propertyId);
        return prep.executeQuery().first();
        
    }
    
    /**
     * edits a property in data base 
     * @param propertyId the id of the property to be edited
     * @param name
     * @param multiple
     * @param status
     * @throws SQLException 
     */
    public void editProperty(int propertyId,String name,boolean multiple,boolean effectAll,int status,String description) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_property` SET `name`=? , `multiple`=?, `status`=?,`description`=?,`effectAll`=? WHERE `propertyId`=?");
        prep.setString(1, name);
        prep.setBoolean(2, multiple);
        prep.setInt(3, status);
        prep.setString(4, description);
        prep.setBoolean(5, effectAll);
        prep.setInt(6, propertyId);
        prep.executeUpdate();
        
    }
    
    /**
     * retrieves 1 product without inside detail
     * @param productId
     * @return a product object
     * @throws SQLException
     * @throws DoesntExistException 
     */    
    public Product getProductOnlyMain(int productId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product` WHERE `productId`=? ");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        Product product=new Product();
        if(!res.first())
        {
             return product;
        }
        
        product.setProductId(productId).setName(res.getString("name"))
//                .setScale(getScale(res.getInt("scaleId")))
                    .setBasePrice(res.getInt("basePrice"))
                    .setDescription(res.getString("description"))
                    .setPicture(getProductPictures(productId))
                    .setCategory(getCategory(res.getInt("categoryId")))
                    .setStatus(res.getInt("status"));
        return product;
    }

    /**
     * shows if the package name is already being used under the value or not 
     * @param valueId the parent value
     * @param name the new name
     * @return true if it already exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean packageNameExists(int valueId,String name) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `scale_package` WHERE `name`=? AND `scaleValueId`=? AND `status`!=-1");
        prep.setString(1, name);
        prep.setInt(2, valueId);
        return prep.executeQuery().first();
    }
        
    /**
     * changes the status of the row to -1
     * @param packageId the id of the package
     * @throws SQLException 
     */
    public void deleteScalePackage(int packageId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `scale_package` SET `status`=-1 WHERE `packageId`=?");
        prep.setInt(1, packageId);
        prep.execute();
    }
    
    /**
     * edits a scale value
     * @param scaleValue
     * @param value
     * @param effect
     * @param description
     * @throws SQLException 
     */
    public void editScaleValue(int valueId,double value,double effect,String description) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_scale_value` SET `value`=?, `effect`=?,`description`=? WHERE `valueId`=?");
        prep.setDouble(1, value);
        prep.setDouble(2, effect);
        prep.setString(3, description);
        prep.setInt(4, valueId);
        prep.execute();
    }
    
    /**
     * edits a scale value
     * @param scaleValue
     * @param value
     * @param effect
     * @param description
     * @param picture
     * @throws SQLException 
     */
    public void editScaleValue(int valueId,double value,double effect,String description,String picture) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_scale_value` SET `value`=?, `effect`=?,`description`=?,`picture`=? WHERE `valueId`=?");
        prep.setDouble(1, value);
        prep.setDouble(2, effect);
        prep.setString(3, description);
        prep.setString(4, picture);
        prep.setInt(5, valueId);
        prep.execute();
    }
    
     /**
     * checks to see if the scale value already exists under the scale id or not 
     * @param scaleId the parent scale id
     * @param value the new value wanting to add
     * @return true if it already exists and false if it doesn't
     * @throws SQLException 
     */
    public boolean scaleValueExist(double value,int valueId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `valueId` FROM product_scale_value WHERE `value`=? AND `status`!=-1 ");
        prep.setDouble(1, value);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return false;
        }
        res.beforeFirst();
        while(res.next())
        {
                if(res.getInt("valueId")==valueId)
            {
                return false;
            }
        }
            
        return true;
    }
    
    /**
     * retrieves all window product (except the deleted ones)
     * @return a linked list of Windo
     * @throws SQLException 
     */
    public LinkedList<Window> getProductWindows() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_window` WHERE `status`>0 ");
        ResultSet res=prep.executeQuery();
        LinkedList<Window> windows=new LinkedList<Window>();
        while(res.next())
        {
            int productId=res.getInt("productId");
            Window window=new Window();
            window.setCount(res.getInt("count")).setDescription(res.getString("description")).setPrice(res.getInt("price"))
                    .setCategory(getCategory(res.getInt("categoryId")))
                    .setName(res.getString("name")).setProductId(res.getInt("productId")).setPictures(getProductPictures(productId))
                    .setStatus(res.getInt("status"));
            windows.add(window);
        }
        return windows;
    }
    
    /**
     * retrieves all window product (except the deleted ones)
     * @return a linked list of Windo
     * @throws SQLException 
     */
    public LinkedList<Window> getProductWindowsList() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_window` WHERE `status`!=-1 ");
        ResultSet res=prep.executeQuery();
        LinkedList<Window> windows=new LinkedList<Window>();
        while(res.next())
        {
            int productId=res.getInt("productId");
            Window window=new Window();
            window.setCount(res.getInt("count")).setDescription(res.getString("description")).setPrice(res.getInt("price"))
                    .setCategory(getCategory(res.getInt("categoryId")))
                    .setName(res.getString("name")).setProductId(res.getInt("productId")).setPictures(getProductPictures(productId))
                    .setStatus(res.getInt("status"));
            windows.add(window);
        }
        return windows;
    }
    
     /**
     * retrieves all window product (except the deleted ones)
     * @return a linked list of Windo
     * @throws SQLException 
     */
    public LinkedList<Window> getProductWindowsForUser() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_window` WHERE  `status`>0 AND `status`<4 ");
        ResultSet res=prep.executeQuery();
        LinkedList<Window> windows=new LinkedList<Window>();
        while(res.next())
        {
            int productId=res.getInt("productId");
            Window window=new Window();
            window.setCount(res.getInt("count")).setDescription(res.getString("description")).setPrice(res.getInt("price"))
                    .setCategory(getCategory(res.getInt("categoryId")))
                    .setName(res.getString("name")).setProductId(res.getInt("productId")).setPictures(getProductPictures(productId))
                    .setStatus(res.getInt("status"));
            windows.add(window);
        }
        return windows;
    }
    
    /**
     * retrieves a window from data base
     * @param productId
     * @return a Window object
     * @throws SQLException 
     */
    public Window getProductWindow(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_window` WHERE `productId`=?");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        Window window=new Window();
        if(!res.first())
        {
            return window;
        }
        
        window.setCount(res.getInt("count")).setDescription(res.getString("description")).setPrice(res.getInt("price"))
                .setCategory(getCategory(res.getInt("categoryId")))
                .setName(res.getString("name")).setProductId(res.getInt("productId")).setPictures(getProductPictures(productId))
                .setStatus(res.getInt("status"));
        
        return window;
        
    }
    
    /**
     * adds a window product to database 
     * @param name
     * @param categoryId
     * @param price
     * @param count
     * @param description
     * @return the id of the added product window
     * @throws SQLException 
     */
    public int addProductWindow(String name,int categoryId,int price,int count,String description) throws SQLException
    {
        int productId;
        Random random=new Random();
        while(true)
        {
            productId=Math.abs(random.nextInt());
            if(!productIdExists(productId))
                break;
        }
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `product_window` (`productId`,`name`,`price`,`count`,`description`,`categoryId`) VALUES (?,?,?,?,?,?)");
        prep.setInt(1, productId);
        prep.setString(2, name);
        prep.setInt(3, price);
        prep.setInt(4, count);
        prep.setString(5,description);
        prep.setInt(6, categoryId);
        prep.execute();
        return productId;
    }
    /**
     * changes the status of a window product to -1
     * @param productId the id of the product that is being deleted
     * @throws SQLException 
     */
    public void deleteWindow(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_window` SET `status`=-1 WHERE `productId`=?");
        prep.setInt(1, productId);
        prep.execute();
    }
    
    /**
     * edits a window based on it's id
     * @param productId the id of the window product
     * @param name
     * @param categoryId
     * @param price
     * @param count
     * @param description
     * @param status
     * @throws SQLException 
     */
    public void editWindow(int productId,String name,int categoryId,int price,int count,String description,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_window` SET `categoryId`=?,`name`=?,`price`=?,`count`=?,`description`=?,`status`=? WHERE `productId`=?");
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
     * shows if the category id is already used or not
     * @param categoryId
     * @return true if yes
     * @throws SQLException 
     */
    public boolean categoryIdExists(int categoryId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_category` WHERE `categoryId`=?");
        prep.setInt(1, categoryId);
        return prep.executeQuery().first();
    }
    /**
     * adds a category to data base 
     * @param name
     * @return the id of the added category
     * @throws SQLException 
     */
    public int addCategory(String name) throws SQLException
    {
        int categoryId;
        Random random=new Random();
        while(true)
        {
            categoryId=Math.abs(random.nextInt());
            if(!categoryIdExists(categoryId))
                break;
        }
            
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `product_category` (`categoryId`,`name`) VALUES (?,?)");
        prep.setInt(1, categoryId);
        prep.setString(2, name);
        prep.execute();
        return categoryId;
    }
    
    /**
     * shows if the name is already added in the category or not
     * @param name
     * @return true if yes and false if it not
     * @throws SQLException 
     */
    public boolean categoryExists(String name) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_category` WHERE `name`=?");
        prep.setString(1, name);
        return prep.executeQuery().first();
    }
    
    /**
     * deletes a category from data base 
     * @param categoryId
     * @throws SQLException 
     */
    public void deleteCategory(int categoryId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM `product_category` WHERE `categoryId`=?");
        prep.setInt(1, categoryId);
        prep.execute();
    }
        
    /**
     * shows if the selected scale value is valid or not
     * @param productId
     * @param scaleValueId
     * @return
     * @throws SQLException 
     */
    public boolean isScaleValueValid(int productId,int scaleValueId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product` " +
"INNER JOIN `product_scale_value` " +
" ON `product_scale_value`.`scaleId`=`product_scale_value`.scaleId " +
" WHERE `product`.`productId`=? " +
" AND `product_scale_value`.`valueId`=?"
+ " AND `product_scale_value`.`status`=1");
        prep.setInt(1, productId);
        prep.setInt(2, scaleValueId);
        return prep.executeQuery().first();
        
    }
    
    /**
     * shows if the selected package is valid under the scalevalue or not 
     * in order to have the 100% result, you must check the validation of scale value
     * with <code>isScaleValueValid</code> first and then check scale package
     * @param scaleValueId
     * @param packageId
     * @return
     * @throws SQLException 
     */
    public boolean isScalePackageValid(int scaleValueId,int packageId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `scale_package` WHERE `scaleValueId`=? AND `packageId`=? AND `status`!=-1");
        prep.setInt(1, scaleValueId);
        prep.setInt(2, packageId);
        return prep.executeQuery().first();
    }

    /**
     * shows if the property is valid and available for the product
     * @param productId
     * @param propertyId
     * @return
     * @throws SQLException 
     */
    public boolean isPropertyValid(int productId,int propertyId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_property` WHERE `productId`=? AND `propertyId`=? AND `status`=2");
        prep.setInt(1, productId);
        prep.setInt(2, propertyId);
        return prep.executeQuery().first();
    }
    
    /**
     * shows if the property value is valid under property or not 
     * must validate property with <code>isPropertyValid</code> first
     * @param propertyId
     * @param valueId
     * @return
     * @throws SQLException 
     */
    public boolean isPropertyValueValid(int propertyId,int valueId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_property_value` WHERE `propertyId`=? AND `valueId`=? AND `status`!=-1");
        prep.setInt(1, propertyId);
        prep.setInt(2, valueId);
        return prep.executeQuery().first();
    }
    
    /**
     * decreases the number of bought windows from data base
     * @param productId
     * @param count 
     */
    public void decreaseWindowCount(int productId,int count) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_window` SET `count`=`count`-? WHERE `productId`=?");
        prep.setInt(1, count);
        prep.setInt(2, productId);
        prep.execute();
    }
    
    /**
     * sets the status of the windows that have less than 0 count to 2 means its finished
     * @throws SQLException 
     */
    public void runOutWindows() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_window` SET `status`=2 WHERE `count`<=0");
        prep.execute();
    }
    
    /**
     * retrieves all color maps
     * @return
     * @throws SQLException 
     */
    public LinkedList<ColorMap> getColorMaps() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `colormap`");
        ResultSet res=prep.executeQuery();
        LinkedList<ColorMap> colormaps=new LinkedList<ColorMap>();
        while(res.next())
        {
            ColorMap colormap=new ColorMap();
            colormap.setColor(res.getString("color")).setTaste(res.getString("taste")).setId(res.getInt("id"));
            colormaps.add(colormap);
        }
        
        return colormaps;
    }
    
    /**
     * adds a color map
     * @param taste
     * @param color
     * @throws SQLException 
     */
    public void addColorMap(String taste,String color) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `colormap` (`color`,`taste`) VALUES (?,?)");
        prep.setString(1, color);
        prep.setString(2, taste);
        prep.execute();
    }
    
    /**
     * deletes a color map completely from data base
     * @param id
     * @throws SQLException 
     */
    public void deleteColorMap(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM `colormap` WHERE `id`=?");
        prep.setInt(1, id);
        prep.execute();
    }
}

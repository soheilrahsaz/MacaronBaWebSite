/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Products;

import Objects.Communication.Comment;
import Objects.Product.Window;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class WindowJsoner {
    private String urlPic="/MacaronBaData/Products/";
    private ProductJsoner productJsoner;
    public WindowJsoner(Connection connection) throws SQLException
    {
        this.productJsoner=new ProductJsoner(connection);
    }
    
    
    
    public JSONArray getWindowsArray(LinkedList<Window> windows)
    {
        JSONArray windowsArray=new JSONArray();
        int i=0;
        for(Window window:windows)
        {
            if(window.getStatus()==1 || window.getStatus()==2 || window.getStatus()==3)
                windowsArray.put(i,getWindowJson(window));
            i++;
        }
        return windowsArray;
    }
    /**
     * gets a window and turns it to a json object
     * @param window
     * @return 
     */
    public JSONObject getWindowJson(Window window)
    {
        JSONObject windowObject=new JSONObject();
        windowObject.put("productId", window.getProductId());
        windowObject.put("name", window.getName());
        windowObject.put("description", window.getDescription());
        windowObject.put("price", window.getPrice());
        windowObject.put("count", window.getCount());
        windowObject.put("status", window.getStatus());
        windowObject.put("category", this.productJsoner.getCategoryJson(window.getCategory()));
        windowObject.put("picture", this.productJsoner.getPicturesJson(window.getProductId(),window.getPictures()));
        
        return windowObject;
    }
    
    /**
     * gets a window and turns it to a json object
     * @param window
     * @return 
     */
    public JSONObject getWindowJson(Window window,LinkedList<Comment> comments)
    {
        JSONObject windowObject=new JSONObject();
        windowObject.put("productId", window.getProductId());
        windowObject.put("name", window.getName());
        windowObject.put("description", window.getDescription());
        windowObject.put("price", window.getPrice());
        windowObject.put("count", window.getCount());
        windowObject.put("status", window.getStatus());
        windowObject.put("category", this.productJsoner.getCategoryJson(window.getCategory()));
        windowObject.put("picture", this.productJsoner.getPicturesJson(window.getProductId(),window.getPictures()));
        windowObject.put("comment", this.productJsoner.getCommentArray(comments));
        
        return windowObject;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Orders;

import Methods.API.Products.ProductJsoner;
import Methods.API.Products.WindowJsoner;
import Objects.Order.Part;
import Objects.Order.Receipt;
import Objects.Product.Product;
import Objects.Product.Window;
import SQL.Commands.ProductSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class OrderChecker {
    private ProductJsoner proudctJsoner;
    private WindowJsoner windowJsoner;
    private ProductSQLCommands productSQL;
    private Receipt receipt;
    private boolean isValid=true;
    private LinkedList<Part> ranOuts=new LinkedList<Part>();
    
    public OrderChecker(Connection connection,Receipt receipt) throws SQLException
    {
        this.proudctJsoner=new ProductJsoner(connection);
        this.windowJsoner=new WindowJsoner(connection);
        this.productSQL=new ProductSQLCommands(connection);
        this.receipt=receipt;
        validate();
    }
    
    private void validate()
    {
        for(Part part:receipt.getParts())
        {
            if(part.isWindow())
            {
                validateWindow(part);
            }else
            {
                validateProduct(part);
            }
        }
    }
    
    private void validateWindow(Part part)
    {
        Window window=part.getProductWindow();
        if(window.getStatus()!=1 || window.getCount()-part.getCount()<0)
        {
            this.ranOuts.add(part);
            this.isValid=false;
        }
        
    }
    
    private void validateProduct(Part part)
    {
        Product product=part.getProduct();
        if(product.getStatus()!=1)
        {
            this.ranOuts.add(part);
            this.isValid=false;
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public LinkedList<Part> getRanOuts() {
        return ranOuts;
    }
        
    public JSONArray getRanOutsResponse()
    {
        JSONArray ranoutsArray=new JSONArray();
        int i=0;
        
        for(Part part:this.ranOuts)
        {
            if(part.isWindow())
            {
                ranoutsArray.put(i,this.windowJsoner.getWindowJson(part.getProductWindow()));
            }else
            {
                ranoutsArray.put(i,this.proudctJsoner.getProductJsonOnlyMainJson(part.getProduct()));
            }
            i++;
        }
        return ranoutsArray;
    }
    
}

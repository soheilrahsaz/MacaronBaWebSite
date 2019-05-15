/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import Objects.Panel.Buttons;
import Objects.Panel.Labels;
import Objects.Product.Category;
import Objects.Product.ColorMap;
import Objects.Product.Product;
import Objects.Product.Picture;
import Objects.Product.Property;
import Objects.Product.PropertyValue;
import Objects.Product.Scale;
import Objects.Product.ScalePackage;
import Objects.Product.ScaleValue;
import Objects.Product.Window;
import SQL.Commands.ProductSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Moses
 */
public class Products {
    private Connection connection;
    private ProductSQLCommands productSQL;
    private HashMap<Integer,String> productStatuses;
    
    
    public Products(Connection connection)
    {
        this.connection=connection;
        this.productSQL=new ProductSQLCommands(connection);
        this.productStatuses=new Parameters().getProductStatuses();
        
    }
    
    public String printProducts() throws SQLException
    {
        String products="<!-- Content Header (Page header) -->\n" +
"   <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">لیست محصولات</h3>\n" +
"\n" +
"              <div class=\"box-tools\">\n" +
"                <div class=\"input-group input-group-sm\" style=\"width: 150px;\">\n" +
"                  <input type=\"text\" name=\"table_search\" class=\"form-control pull-right\" placeholder=\"جستجو\">\n" +
"\n" +
"                  <div class=\"input-group-btn\">\n" +
"                    <button type=\"submit\" class=\"btn btn-default\"><i class=\"fa fa-search\"></i></button>\n" +
"                  </div>\n" +
"                </div>\n" +
"              </div>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>نام محصول</th>\n" +
"                  <th>عکس</th>\n" +
"                  <th>قیمت پایه</th>\n" +
"                  <th>وضعیت</th>\n" +
"                </tr>\n" +
                getProductTable()+
"                \n" +
"              </table>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>";
        
        return products;
    }
    
    private String getProductTable() throws SQLException
    {
        String table="";
        LinkedList<Product> products=this.productSQL.getAllProductsForShow();
        for(Product product:products)
        {
            table+="<tr onclick=\"window.location='List/"+product.getProductId()+"'\" style=\"cursor: pointer;\" >"
                  +"<td>"+product.getName()+"</td>"  
                  +"<td>"+"<img src=\""
                    +
                    (!product.getPictures().isEmpty() ? "/MacaronBaData/Products/"+product.getProductId()+"/"+product.getPictures().getFirst().getUrl():"/AdminPanel/dist/img/noPic.png")
                    +
                    "\" style=\"width: 15%;\">"+"</td>"  
                  +"<td>"+product.getBasePrice()+"</td>"  
                  +"<td>"+getStatus(product.getStatus())+"</td>"  
                    +"</tr>";
        }
        
        return table;
    }
    
    /**
     * 0:notReady/1:forSale/2:ranOut/3:commingSoon/4:hidden
     * @param status
     * @return 
     */
    private String getStatus(int status)
    {
        Labels label=new Labels();
        switch(status)
        {
            case 0:return label.getInfo("ناتمام");
            case 1:return label.getSuccess("موجود");
            case 2:return label.getWarning("ناموجود");
            case 3:return label.getPrimary("به زودی");
            case 4:return label.getDanger("مخفی");
            default: return "";
        }
    }
    
    
    public String addProduct() throws SQLException
    {
        String addproduct="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">"
                + "<div class=\"box box-warning\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">اضافه کردن محصول</h3>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body\">\n" +
"              <form role=\"form\" method=\"post\" enctype=\"multipart/form-data\" action=\"AddProduct\" >\n"
                + "<div class=\"form-group\">\n" +
"                  <label>دسته</label>\n" +
"                  <select class=\"form-control\" name=\"category\">\n" +
                    getCategoryOptions()+
"                  </select>\n" +
"                </div>" +
                
"                <!-- text input -->\n" +
"                <div class=\"form-group\">\n" +
"                  <label>نام محصول</label>\n" +
"                  <input type=\"text\" name=\"productName\" class=\"form-control\" placeholder=\"نام محصول\">\n" +
"                </div>\n" +
"\n" +
"                <!-- textarea -->\n" +
"                <div class=\"form-group\">\n" +
"                  <label>توضیحات محصول</label>\n" +
"                  <textarea class=\"form-control\" name=\"description\" rows=\"3\" placeholder=\"توضیحات\"></textarea>\n" +
"                </div>\n" +

                getScalesRadio()+
"                <div class=\"form-group\">\n" +
"                  <label>قیمت پایه به ازای هر واحد(تومان)</label>\n" +
"                  <input type=\"number\" name=\"basePrice\" class=\"form-control\" placeholder=\"قیمت پایه\">\n" +
"                </div>\n" +
"                <div class=\"form-group\">\n" +
"                  <label>عکس محصول(تا 4 عکس می توانید انتخاب کنید)</label>\n" +
"                  <input type=\"file\" name=\"picture\" accept=\"image/*\" multiple class=\"form-control\" >\n" +
"                </div>\n"
                
                + "<div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-primary\">اضافه کردن</button>\n" +
"              </div>" +


"\n" +
"              </form>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>"
                + "</div>\n" +
"        <!--/.col (right) -->\n" +
"      </div>\n" +
"      <!-- /.row -->\n" +
"    </section>";
        
        return addproduct;
    }
    
    private String getScalesRadio() throws SQLException
    {
        LinkedList<Scale> scales=this.productSQL.getScales();
        String scaleRadio="<!-- radio -->\n" +
                        "<div class=\"form-group\">\n"
                + "<label>واحد فروش</label>"; 
        
        for(Scale scale:scales)
        {
            scaleRadio+="<div class=\"radio\">\n" +
                        "<label>\n" +
                        "<input type=\"radio\" name=\"scale\" id=\"scale\" value=\""+scale.getName()+"\" checked>\n" +
                        scale.getName()+
                        "</label>\n" +
                        "</div>\n" ;  
        }
        
        scaleRadio+="</div><br>\n";
        
        
        return scaleRadio;
    }
    
    private String getCategoryOptions() throws SQLException
    {
        String options="";
        LinkedList<Category> categories=this.productSQL.getAllCategories();
        
        for(Category category:categories)
        {
            options+="<option>"+category.getName()+"</option>";
        }
        
        return options;
    }
    
    public String getCategoryOptionsValues() throws SQLException
    {
        String options="";
        LinkedList<Category> categories=this.productSQL.getAllCategories();
        
        for(Category category:categories)
        {
            options+="<option value=\""+category.getCategoryId()+"\">"+category.getName()+"</option>";
        }
        
        return options;
    }
    
    private String getCategoryOptionsValues(int value) throws SQLException
    {
        String options="";
        LinkedList<Category> categories=this.productSQL.getAllCategories();
        
        for(Category category:categories)
        {
            options+="<option value=\""+category.getCategoryId()+"\" "
                    + (value==category.getCategoryId() ? "selected":"")
                    + " >"+category.getName()+"</option>";
        }
        
        return options;
    }
    
    public String printProduct(String url) throws SQLException
    {
        int productId=0,propertyId=0,propertyValueId=0;
        url=url.substring(url.indexOf("List")+5);
        String []ids=url.split("/");
        if(ids.length==1)
        {
            productId=Integer.parseInt(ids[0]);
            return showProduct(productId);
        }
        
        if(ids.length>=2)
        {
            productId=Integer.parseInt(ids[0]);
            if(ids[1].charAt(0)=='v')
            {
                propertyId=Integer.parseInt(ids[1].substring(1));
                return showPackages(productId,propertyId);
            }
            propertyId=Integer.parseInt(ids[1]);
            return showProperty(productId,propertyId);
        }
        
        if(ids.length==3)
        {
            productId=Integer.parseInt(ids[0]);
            propertyId=Integer.parseInt(ids[1]);
            propertyValueId=Integer.parseInt(ids[2]);
            return showPropertyValue(propertyValueId);
        }
        
        
        
            return "";
        
    }
    
    private String showProduct(int productId) throws SQLException
    {
        Product product=this.productSQL.getProduct(productId);
        String show="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">"
                + "<div class=\"box box-warning\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">محصول</h3>\n" +
                
                "              <div class=\"box-tools\">\n" +
"                <div class=\"input-group input-group-sm\" >\n"
                + "<form method=\"post\" action=\"../DeleteProduct\" >"
                + "<input type=\"hidden\" name=\"productId\" value=\""+productId+"\" >"
                +new Buttons().btn_Normal_Danger("حذف محصول", "submit", "sm", false, false)
                + "</form>" +
"                </div>\n" +
"              </div>\n"+
                
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body\">\n" +
"              <form role=\"form\" method=\"post\"  action=\"../EditProduct\" >\n"
                + "<input type=\"hidden\" name=\"productId\" value=\""+product.getProductId()+"\"> "
                
                + "<div class=\"form-group\">\n" +
"                  <label>دسته</label>\n" +
"                  <select class=\"form-control\" name=\"category\">\n" +
                    getCategoryOptions(product.getCategory().getName())+
"                  </select>\n" +
"                </div>" +
                
"                <!-- text input -->\n" +
"                <div class=\"form-group\">\n" +
"                  <label>نام محصول</label>\n" +
"                  <input type=\"text\" name=\"productName\" class=\"form-control\" value=\""+product.getName()+"\" placeholder=\"نام محصول\">\n" +
"                </div>\n" +
"\n" +
"                <!-- textarea -->\n" +
"                <div class=\"form-group\">\n" +
"                  <label>توضیحات محصول</label>\n" +
"                  <textarea class=\"form-control\" name=\"description\" rows=\"3\" placeholder=\"توضیحات\">"+product.getDescription()+"</textarea>\n" +
"                </div>\n" +

"                <div class=\"form-group\">\n" +
"                  <label>قیمت پایه به ازای هر واحد(تومان)</label>\n" +
"                  <input type=\"number\" name=\"basePrice\" class=\"form-control\" value=\""+product.getBasePrice()+"\" placeholder=\"قیمت پایه\">\n" +
"                </div>\n" 
                
                                + "<div class=\"form-group\">\n" +
"                  <label>وضعیت</label>\n" +
"                  <select class=\"form-control\" name=\"status\">\n" +
                    getStatusOptions(product.getStatus())+
"                  </select>\n" +
"                </div>" +
                
"<div class=\"form-group\">\n" +
"                <button type=\"submit\" class=\"btn btn-primary\">اعمال تغییرات</button>\n" +
"                </div>\n"+
        
                "              </form>\n"
                
                + 
             "<div class=\"box-footer\">\n" +
"                  <label  >عکس محصول</label><br>\n"+
"                <div class=\"form-group\" style=\"display:flex;\" >\n";
            for(Picture picture:product.getPictures())
            {
               show+= "<figure class=\"product\">\n" +
            "      <img class=\"product__img\" style=\"width:100%;\" src=\"/MacaronBaData/Products/"+product.getProductId()+"/"+picture.getUrl()+" \" alt=\"\">\n" +
            "      <a class=\"product__del\" href=\"../DeletePicture?pictureId="+picture.getPictureId()+"\"><i class=\"fa fa-window-close\"></i></a>\n" +
            "    </figure>";
            }
                 if(product.getPictures().size()<4)
                 {
                     show+="</div>"
                             + "<br>\n<label>افزودن عکس"
                         + "(میتوانید تا "
                         + (4-product.getPictures().size())
                         + " عکس اضافه کنید)</label>"+
                         "<form role=\"form\" method=\"post\" enctype=\"multipart/form-data\"  action=\"../AddPicture\" >\n"
                             + "<input type=\"hidden\" name=\"productId\" value=\""+productId+"\">"
                              +"<div class=\"form-group\">\n"  +
                    " <input type=\"file\" name=\"picture\" accept=\"image/*\" multiple class=\"form-control\" >\n"
                             + "</div>"
                             +"<div class=\"form-group\">\n" 
                      +" <button type=\"submit\" class=\"btn btn-success\">افزودن</button>\n"
                             + "</div>"    
                             
                         + "</form>" ;
                 }
                         
                show+=" </div>\n"+
                      
                        
//                        "<div class=\"box-footer\">\n"+
//                        "                <div class=\"form-group\">\n" +
//"                  <label>واحد فروش</label>\n" +
//"                  <input type=\"text\" name=\"productName\" class=\"form-control\" value=\""+product.getScale().getName()+"\" placeholder=\"واحد فروش\">\n" +
//"                </div>\n";
//                Buttons button=new Buttons();
//                for(ScaleValue value:product.getScale().getValues())
//                {
//                    show+=button.btn_Normal_Info(value.getValue()+" %"+value.getEffect(), "button", "", false, false);
//                }
//    
//                        show+= "</div>" +
                



"\n" +

"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>"
                + "</div>\n" +
"        <!--/.col (right) -->\n" +
"      </div>\n" +
"      <!-- /.row -->\n" +
                        
"    </section>"
                        +getScaleTable(product)
                        +getPropertyTable(product);
        
        
        return show;
    }
    
    private String getCategoryOptions(String option) throws SQLException
    {
        String options="";
        LinkedList<Category> categories=this.productSQL.getAllCategories();
        
        for(Category category:categories)
        {
            String catName=category.getName();
            options+="<option "
                    +(catName.equalsIgnoreCase(option) ? "selected":"")
                    + ">"
                    +catName+"</option>";
        }
        
        return options;
    }
    
    /**
     * 0:notReady/1:forSale/2:ranOut/3:commingSoon/4:hidden
     * @param status
     * @return
     * @throws SQLException 
     */
     private String getStatusOptions(int status) throws SQLException
    {
        String options="";
        int i=0;
        for(String str:this.productStatuses.values())
        {
            options+="<option value=\""+i+"\" "
                    + (i==status ? "selected":"")
                    + ">"
                    + str
                    + "</option>";
            i++;
        }
        
        return options;
    }
    
    private String getScaleTable(Product product)
    {
        String show="<section class=\"content\">\n"
                + "<!-- Content Header (Page header) -->\n" +
"   <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">واحد فروش</h3>\n" +
"\n" +

"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>میزان واحد</th>\n" +
"                  <th>میزان تاثیر قیمت</th>\n" +
"                  <th>قیمت</th>\n" +
"                  <th>توضیحات</th>\n" +
"                  <th>عکس</th>\n" +
"                  <th>عملیات</th>\n" +
"                </tr>\n" +
                getScaleRows(product)+
"                \n" +
"              </table>\n"+
                
                         "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>واحد فروش</label>\n"
                + "<form method=\"post\" action=\"../EditScale\"> "
                + "<table>"
                + "<tr>"
                + "<td>"
                +"<input  type=\"text\" name=\"scaleName\" class=\"form-control\" value=\""+product.getScale().getName()+"\">\n"
                +"<input  type=\"hidden\" name=\"prodcutId\" class=\"form-control\" value=\""+product.getProductId()+"\" >\n"
                +"<input  type=\"hidden\" name=\"scaleId\" class=\"form-control\" value=\""+product.getScale().getScaleId()+"\" >\n"
                +"</td>"
                + "<td>"
                + new Buttons().btn_Normal_Primary("تغییر", "submit", "", false, false)
                +"</td>"
                + "</tr>"
                + "</table>"
                
                + "</form>" +
"                </div>\n"
                
                + "</div>"+        
                
  
                                         "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>افزودن واحد</label>\n"
                + "<form method=\"post\" enctype=\"multipart/form-data\" action=\"../AddScale\"> "
                +"<input  type=\"hidden\" name=\"productId\" class=\"form-control\" value=\""+product.getProductId()+"\" >\n"
                +"<input  type=\"hidden\" name=\"scaleId\" class=\"form-control\" value=\""+product.getScale().getScaleId()+"\" >\n"
                + "<table>"
                + "<tr>"
                
                + "<td>"
                 +"<input  type=\"number\" min=\"0\" step=\"0.01\" name=\"value\" class=\"form-control\"  placeholder=\"میزان واحد\">\n"
                +"</td>"
                
                 + "<td style=\"width:18%;\">"
                 +"<input  type=\"number\"  min=\"0\" max=\"100\" step=\"0.01\" class=\"form-control\" name=\"effect\"  placeholder=\"میزان تاثیر قیمت(درصد)\">\n"
                +"</td>"
                
                + "<td style=\"width:30%;\">"
                +"<input  type=\"text\" style=\"width:90%;\" name=\"description\" class=\"form-control\"  placeholder=\"توضیحات\">\n"
                + "</td>"
                
                +"<td>"
                +"                <div class=\"form-group\">\n" +
"                  <label><small>عکس</small></label>\n" +
"                  <input type=\"file\" style=\"width:90%;\" name=\"picture\" accept=\"image/*\" class=\"form-control\" >\n" +
"                </div>\n"
                +"</td>"
                + "<td>"
                
                + new Buttons().btn_Normal_Success("افزودن", "submit", "", false, false)
                +"</td>"
                + "</tr>"
                + "</table>"
                
                + "</form>" +
"                </div>\n"
                
                + "</div>"+    
                
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>"
                + "</section>";
        
        return show;
    }
    
    private String getScaleRows(Product product)
    {
        String rows="";
        LinkedList<ScaleValue> values=product.getScale().getValues();
        for(ScaleValue value:values)
        {
            rows+="<tr onclick=\"window.location='"+product.getProductId()+"/v"+value.getValueId()+"'\"  style=\"cursor:pointer;\">"
                    + "<td>"+value.getValue()+"</td>"
                    + "<td>"+value.getEffect()+"</td>"
                    + "<td>"+(int)(product.getBasePrice()*value.getValue()*value.getEffect()/100)+"</td>"
                    
                    + "<td>"+(Methods.Methods.isNullOrEmpty(value.getDescription()) ? "":value.getDescription())+"</td>"
                    
                    + "<td>"
                    +"<img style=\"width:15%;\" src=\""+(Methods.Methods.isNullOrEmpty(value.getPicture()) ? "/AdminPanel/dist/img/noPic.png":"/MacaronBaData/Products/"+product.getProductId()+"/"+value.getPicture())+"\" >"
                    +"</td>"
                    
                    + "<td>"
                    + "<form method=\"post\" action=\"../RemoveScaleValue\">"
                    + "<input type=\"hidden\" name=\"scaleValueId\" value=\""+value.getValueId()+"\">"
                    +"<input  type=\"hidden\" name=\"productId\" class=\"form-control\" value=\""+product.getProductId()+"\" >\n"
                    + new Buttons().btn_Normal_Danger("حذف", "submit", "", false, false)
                    + "</form>"
                    +"</td>"
                    
                 +"</tr>";
        }
        
        return rows;
    }
    
    private String getPropertyTable(Product product)
    {
        String show="<section class=\"content\">\n"
                + "<!-- Content Header (Page header) -->\n" +
"   <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">ویژگی ها</h3>\n" +
"\n" +

"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>نام ویژگی</th>\n" +
"                  <th>ویژگی ها</th>\n" +
"                  <th>قابلیت چند انتخابی</th>\n" +
"                  <th>تاثیر روی همه</th>\n" +
"                  <th>توضیحات</th>\n" +
"                  <th>وضعیت</th>\n" +
"                </tr>\n" +
                getPropertyRows(product)+
"                \n" +
"              </table>\n"+
                
                                  "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>افزودن ویژگی</label>\n"
                + "<form method=\"post\" action=\"../AddProperty\"> "
                +"<input  type=\"hidden\" name=\"productId\" class=\"form-control\" value=\""+product.getProductId()+"\" >\n"
                + "<table>"
                + "<tr>"
                + "<td>"
                 +"<input  type=\"text\" name=\"name\" class=\"form-control\"  placeholder=\"نام ویژگی\">\n"
                +"</td>"
                + "<td>"
                 +"<input  type=\"text\" name=\"description\" class=\"form-control\"  placeholder=\"توضیحات\">\n"
                +"</td>"
                + "<td style=\"width:20%\">"
                +"<div class=\"checkbox\">\n" +
                    " <label>" +
                    " <input type=\"checkbox\" name=\"multiple\">\n" +
                    "چند انتخابی" +
                    "   </label>\n" +
                    "   </div>"
                + "</td>"
                + "<td style=\"width:20%\">"
                +"<div class=\"checkbox\">\n" +
                    " <label>" +
                    " <input type=\"checkbox\" name=\"effectAll\">\n" +
                    "تاثیر روی همه" +
                    "   </label>\n" +
                    "   </div>"
                + "</td>"
                + "<td>"
                + new Buttons().btn_Normal_Success("افزودن", "submit", "", false, false)
                +"</td>"
                + "</tr>"
                + "</table>"
                
                + "</form>" +
"                </div>\n"
                + "</div>"+ 
                
                
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>"
                + "</section>";
        
        return show;
    }
    
    private String getPropertyRows(Product product)
    {
        String rows="";
        LinkedList<Property> properties=product.getProperties();
        for(Property property:properties)
        {
            rows+="<tr onclick=\"window.location='"+product.getProductId()+"/"+property.getPropertyId()+"'\" style=\"cursor: pointer;\">"
                    + "<td>"+property.getName()+"</td>"
                    + "<td>";
            LinkedList<PropertyValue> values=property.getValues();
            Buttons button=new Buttons();
            for(PropertyValue value:values)
            {
                rows+=button.btn_Normal_Info(value.getName(), "button", "", false, false);
            }
            
            rows+= "</td>"
                    + "<td>"+(property.isMultiple() ? "دارد<i class=\"fa fa-fw fa-check\"></i>":"ندارد<i class=\"fa fa-window-close\"></i>")+"</td>"
                    + "<td>"+(property.isEffectAll()? "دارد<i class=\"fa fa-fw fa-check\"></i>":"ندارد<i class=\"fa fa-window-close\"></i>")+"</td>"
                    + "<td>"+(Methods.Methods.isNullOrEmpty(property.getDescription()) ? "":property.getDescription())+"</td>"
                    + "<td>"+getPropertyStatus(property.getStatus())+"</td>"
            + "</tr>";
        }
        
        return rows;
    }
    /**
     * -1:off/0:notReadyYet/1:Ready	
     * @param status
     * @return 
     */
    private String getPropertyStatus(int status)
    {
        Labels label=new Labels();
        switch(status)
        {
            case 1:return label.getInfo("ناتمام");
            case 2:return label.getSuccess("موجود");
            case 0:return label.getDanger("غیرفعال");
            default: return "";
        }
    }
    private String showProperty(int productId,int propertyId) throws SQLException
    {
        Property property=this.productSQL.getProperty(propertyId);
        String show="<section class=\"content\">\n"
                + "<!-- Content Header (Page header) -->\n" +
"   <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">ویژگی "
                + property.getName()
                + "</h3>\n" +
"\n" +
                
          "<div class=\"box-tools\">\n" +
"            <div class=\"input-group input-group-sm\" >\n"
                + "<form style=\"display:inline;\" method=\"post\" action=\"../../DeleteProperty\" >"
                + "<input type=\"hidden\" name=\"productId\" value=\""+productId+"\" >"
                + "<input type=\"hidden\" name=\"propertyId\" value=\""+propertyId+"\" >"
                +new Buttons().btn_Normal_Danger("حذف ویژگی", "submit", "", false, false)
                + "</form>" 
                + "<form style=\"display:inline;\" method=\"post\" action=\"../"+productId+"\" >"
                +new Buttons().btn_Normal_Primary("بازگشت به محصول", "submit", "", false, false)
                + "</form>" +
"           </div>\n" +
          " </div>\n"+
                

"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>نام</th>\n" +
"                  <th>قیمت به ازای واحد</th>\n" +
"                  <th>عملیات</th>\n" +
"                </tr>\n" +
                getPropertyValueRows(productId,property)+
"                \n" +
"              </table>\n"+
                
                               
                                  "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>تغییر ویژگی</label>\n"
                + "<form method=\"post\" action=\"../../EditProperty\"> "
                +"<input  type=\"hidden\" name=\"productId\" class=\"form-control\" value=\""+productId+"\" >\n"
                +"<input  type=\"hidden\" name=\"propertyId\" class=\"form-control\" value=\""+propertyId+"\" >\n"
                + "<table>"
                + "<tr>"
                + "<td>"
                 +"<input  type=\"text\" name=\"name\" class=\"form-control\"  placeholder=\"نام ویژگی\" value=\""+property.getName()+"\" >\n"
                +"</td>"
                + "<td style=\"width:25%\">"
                +"<div class=\"checkbox\">\n" +
                    " <label>" +
                    " <input type=\"checkbox\" name=\"multiple\" "
                + (property.isMultiple() ? "checked":"")
                + " >\n" +
                    "چند انتخابی" +
                    "   </label>\n" +
                    "   </div>"
                + "</td>"
                
                + "<td style=\"width:25%\">"
                +"<div class=\"checkbox\">\n" +
                    " <label>" +
                    " <input type=\"checkbox\" name=\"effectAll\" "
                + (property.isEffectAll()? "checked":"")
                + " >\n" +
                    "تاثیر روی همه" +
                    "   </label>\n" +
                    "   </div>"
                + "</td>"
                
                + "<td style=\"width:30%\">"
                +   "<div class=\"form-group\">\n" +
                " <label>وضعیت</label>\n" +
                " <select class=\"form-control\" style=\"width:80%\" name=\"status\">\n" 
                    +getPropertyStatusOptions(property.getStatus())+
                "    </select>\n" +
                "    </div>" 
                + "</td>"
                +"<td>"
                 +"<input  type=\"text\" name=\"description\" class=\"form-control\" value=\""+property.getDescription()+"\"  placeholder=\"توضیحات\">\n"
                +"</td>"
                + "<td>"
                + new Buttons().btn_Normal_Primary("تغییر", "submit", "", false, false)
                +"</td>"
                + "</tr>"
                + "</table>"
                
                + "</form>" +
"                </div>\n"
                
                + "</div>"+ 
                
                
                
                                         "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>افزودن مقدار</label>\n"
                + "<form method=\"post\" action=\"../../AddPropertyValue\"> "
                +"<input  type=\"hidden\" name=\"propertyId\" class=\"form-control\" value=\""+propertyId+"\" >\n"
                +"<input  type=\"hidden\" name=\"productId\" class=\"form-control\" value=\""+productId+"\" >\n"
                + "<table>"
                + "<tr>"
                + "<td>"
                 +"<input  type=\"text\" name=\"name\" class=\"form-control\"  placeholder=\"نام \">\n"
                +"</td>"
                 + "<td style=\"width:50%;\">"
                 +"<input  type=\"number\"  min=\"0\"  class=\"form-control\" name=\"price\"  placeholder=\"قیمت به ازای واحد\">\n"
                +"</td>"
                + "<td>"
                + new Buttons().btn_Normal_Success("افزودن", "submit", "", false, false)
                +"</td>"
                + "</tr>"
                + "</table>"
                
                + "</form>" +
"                </div>\n"
                
                + "</div>"+    
                
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>"
                + "</section>";
        
        
        return show;
        
    }
    
    private String getPropertyValueRows(int productId,Property property)
    {
        String rows="";
        Buttons buttons=new Buttons();
        for(PropertyValue value:property.getValues())
        {
            rows+="<tr>"
                    + "<td>"+value.getName()+"</td>"
                    + "<td>"+value.getPriceByScale()+"</td>"
                    + "<td>"
                    +"<form method=\"post\" action=\"../../DeletePropertyValue\" >"
                    +"<input  type=\"hidden\" name=\"productId\" class=\"form-control\" value=\""+productId+"\" >\n"
                    +"<input  type=\"hidden\" name=\"propertyId\" class=\"form-control\" value=\""+property.getPropertyId()+"\" >\n"
                    +"<input  type=\"hidden\" name=\"valueId\" class=\"form-control\" value=\""+value.getValueId()+"\" >\n"
                    + buttons.btn_Normal_Danger("حذف", "submit", "", false, false)
                    + "</form>"
                    +"</td>"
                    + "</tr>";
        }
        
        return rows;
    }
    
    private String getPropertyStatusOptions(int status)
    {
        HashMap<Integer,String> statuses=new Parameters().getPropertyStatuses();
        String options="";
        
        int i=0;
        for(String str:statuses.values())
        {
            options+="<option value=\""+i+"\" "
                    + (i==status ? "selected":"")
                    + ">"
                    + str
                    + "</option>";
            i++;
        }
        
        return options;
    }
    
    private String showPropertyValue(int propertyValueId)
    {
        String show="";
        
        
        return show;
    }
    
    private String showPackages(int productId,int valueId) throws SQLException
    {
        ScaleValue value=this.productSQL.getScaleValue(valueId);
        String show="<section class=\"content\">\n"
                + "<!-- Content Header (Page header) -->\n" +
"   <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">بسته بندی</h3>\n" +
"\n" +
          "<div class=\"box-tools\">\n" +
"            <div class=\"input-group input-group-sm\" >\n"
                + "<form style=\"display:inline;\" method=\"post\" action=\"../"+productId+"\" >"
                +new Buttons().btn_Normal_Primary("بازگشت به محصول", "submit", "", false, false)
                + "</form>" +
"           </div>\n" +
          " </div>\n"+
                
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>نام</th>\n" +
"                  <th>عکس</th>\n" +
"                  <th>قیمت</th>\n" +
 "                  <th>توضیح</th>\n" +
"                  <th>عملیات</th>\n" +
"                </tr>\n" +
                getPackagesRows(productId,value)+
"                \n" +
"              </table>\n"+
                
                
                         "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" 

                + "<form method=\"post\" enctype=\"multipart/form-data\" action=\"../../EditScaleValue\"> "
                +"<input  type=\"hidden\" name=\"prodcutId\" class=\"form-control\" value=\""+productId+"\" >\n"
                +"<input  type=\"hidden\" name=\"valueId\" class=\"form-control\" value=\""+valueId+"\" >\n"+
//                "<input  type=\"hidden\" name=\"valueId\" class=\"form-control\" value=\""+value.getValueId()+"\" >\n"+
                
                "                <div class=\"form-group\">\n" +
"                  <label>تغییر واحد</label>\n" +
"                </div>\n"+
                
"                <div class=\"form-group\">\n" +
"                  <label>میزان واحد</label>\n" 
+"<input  type=\"number\" step=\"0.01\" min=\"0\" name=\"value\" class=\"form-control\" value=\""+value.getValue()+"\">\n"+
"                </div>\n"+
                
"                <div class=\"form-group\">\n" +
"                  <label>میزان تاثیر قیمت</label>\n" 
+"<input  type=\"number\" step=\"0.01\" min=\"0\" name=\"effect\" class=\"form-control\" value=\""+value.getEffect()+"\">\n"+
"                </div>\n"+
                "                <div class=\"form-group\">\n" +
"                  <label>توضیحات</label>\n" 
+"<input  type=\"text\"  name=\"description\" class=\"form-control\" value=\""+value.getDescription()+"\">\n"+
"                </div>\n"+
                
                                "                <div class=\"form-group\">\n" +
"                  <label>عکس</label>\n" 
+"<input  type=\"file\"  name=\"picture\" class=\"form-control\" accept=\"image/*\" >\n"+
"                </div>\n"+
                
                                "                <div class=\"form-group\">\n" 
+ new Buttons().btn_Normal_Primary("تغییر", "submit", "", false, false)+
"                </div>\n"+
                
                

                 "</form>" +
"                </div>\n"
                
                + "</div>"+        
                
  
                                         "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>افزودن بسته بندی</label>\n"
                + "<form method=\"post\" enctype=\"multipart/form-data\" action=\"../../AddPackage\"> "
                +"<input  type=\"hidden\" name=\"productId\" class=\"form-control\" value=\""+productId+"\" >\n"
                +"<input  type=\"hidden\" name=\"valueId\" class=\"form-control\" value=\""+valueId+"\" >\n"
                + "<table>"
                + "<tr>"
                
                + "<td>"
                 +"<input  type=\"text\"  name=\"name\" class=\"form-control\"  placeholder=\"نام \">\n"
                +"</td>"
                
                 + "<td style=\"width:18%;\">"
                 +"<input  type=\"number\"  min=\"0\"  step=\"1\" class=\"form-control\" name=\"price\"  placeholder=\"قیمت\">\n"
                +"</td>"
                
                + "<td style=\"width:30%;\">"
                +"<input  type=\"text\" style=\"width:90%;\" name=\"description\" class=\"form-control\"  placeholder=\"توضیحات\">\n"
                + "</td>"
                
                +"<td>"
                +"                <div class=\"form-group\">\n" +
"                  <label><small>عکس</small></label>\n" +
"                  <input type=\"file\" style=\"width:90%;\" name=\"picture\" accept=\"image/*\" class=\"form-control\" >\n" +
"                </div>\n"
                +"</td>"
                + "<td>"
                
                + new Buttons().btn_Normal_Success("افزودن", "submit", "", false, false)
                +"</td>"
                + "</tr>"
                + "</table>"
                
                + "</form>" +
"                </div>\n"
                
                + "</div>"+    
                
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>"
                + "</section>";
        
        return show;
    }
        
    private String getPackagesRows(int productId,ScaleValue value)
    {
        Buttons buttons=new Buttons();
        String rows="";
        for(ScalePackage scalePackage:value.getPackages())
        {
            rows+="<tr>"
                    + "<td>"+scalePackage.getName()+"</td>"
                    + "<td>"+"<img style=\"width:15%;\" src=\""+(Methods.Methods.isNullOrEmpty(scalePackage.getPicture()) ? "/AdminPanel/dist/img/noPic.png":"/MacaronBaData/Products/"+productId+"/"+scalePackage.getPicture())+"\" >"+"</td>"
                    + "<td>"+scalePackage.getPrice()+"</td>"
                    + "<td>"+scalePackage.getDescription()+"</td>"
                    + "<td>"
                    +"<form method=\"post\" action=\"../../DeleteScalePackage\">"
                    + "<input type=\"hidden\" name=\"productId\" value=\""+productId+"\">"
                    + "<input type=\"hidden\" name=\"valueId\" value=\""+value.getValueId()+"\">"
                    + "<input type=\"hidden\" name=\"packageId\" value=\""+scalePackage.getPackageId()+"\">"
                    + buttons.btn_Normal_Danger("حذف", "submit", "", false, false)
                    + "</form>"
                    +"</td>"
                    + "</tr>";
        }
        
        return rows;
    }
        
    public String getProductWindowRows() throws SQLException
    {
        LinkedList<Window> windows=this.productSQL.getProductWindowsList();
        String rows="";
        for(Window window:windows)
        {
            rows+="<tr onclick=\"window.location='Windows/List/"+window.getProductId()+"'\" style=\"cursor:pointer\" >"
                    + "<td>"+window.getName()+"</td>"
                    + "<td>"+"<img style=\"width:10%;\" src=\""
                    +(!window.getPictures().isEmpty() ? "/MacaronBaData/Products/"+window.getProductId()+"/"+window.getPictures().getFirst().getUrl():"/AdminPanel/dist/img/noPic.png")
                    +"\" >"
                    +"</td>"
                    + "<td>"+window.getPrice()+"</td>"
                    + "<td>"+window.getCount()+"</td>"
                    + "<td>"+getStatus(window.getStatus())+"</td>"
                    + "</tr>";
        }
        
        return rows;
    }
        
    public String printWindow(String uri) throws SQLException
    {
        uri=uri.substring(uri.indexOf("List")+5);
        int productId=Integer.parseInt(uri);
        Window window=this.productSQL.getProductWindow(productId);
        
        String show="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">مشخصات ویترین</h3>\n" +
                                "              <div class=\"box-tools\">\n" +
"                <div class=\"input-group input-group-sm\" >\n"
                + "<form method=\"post\" action=\"../DeleteWindow\" >"
                + "<input type=\"hidden\" name=\"productId\" value=\""+productId+"\" >"
                +new Buttons().btn_Normal_Danger("حذف محصول", "submit", "sm", false, false)
                + "</form>" +
"                </div>\n" +
"              </div>\n"+
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditWindow\" >\n"
                + "<input type=\"hidden\" name=\"productId\" value=\""+window.getProductId()+"\">" +
"              <div class=\"box-body\">\n" +
"                <div class=\"form-group\">\n" +
"                  <label>دسته بندی</label>\n" +
"                  <select name=\"category\" class=\"form-control\">\n" +
                        getCategoryOptionsValues(window.getCategory().getCategoryId())+
"                  </select> \n" +
"                </div>\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label >نام</label>\n" +
"                  <input type=\"text\" name=\"name\" class=\"form-control\"  placeholder=\"نام محصول\" value=\""+window.getName()+"\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >قیمت</label>\n" +
"                  <input type=\"number\" name=\"price\" min=\"0\" class=\"form-control\"  placeholder=\"قیمت\" value=\""+window.getPrice()+"\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >تعداد موجود</label>\n" +
"                  <input type=\"number\" name=\"count\" min=\"0\" class=\"form-control\"  placeholder=\"تعداد\" value=\""+window.getCount()+"\">\n" +
"                </div>\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label>توضیحات</label>\n" +
"                  <textarea name=\"description\" class=\"form-control\" placeholder=\"توضیحات\">"+window.getDescription()+"</textarea>\n" +
"                </div>\n" +
 "                <div class=\"form-group\">\n" +
"                  <label>وضعیت</label>\n" +
"                  <select name=\"status\" class=\"form-control\">\n" +
                        getStatusOptions(window.getStatus())+
"                  </select> \n" +
"                </div>\n" +
                
"                \n" +

"                \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +

"                <button type=\"submit\" class=\"btn btn-warning\">اعمال تغییرات</button>\n" +

"            </form>\n" +
                             "<div class=\"box-footer\">\n" +
"                  <label  >عکس محصول</label><br>\n"+
"                <div class=\"form-group\" style=\"display:flex;\" >\n";
            for(Picture picture:window.getPictures())
            {
               show+= "<figure class=\"product\">\n" +
            "      <img class=\"product__img\" style=\"width:100%;\" src=\"/MacaronBaData/Products/"+window.getProductId()+"/"+picture.getUrl()+" \" alt=\"\">\n" +
            "      <a class=\"product__del\" href=\"../../DeletePicture?type=window&pictureId="+picture.getPictureId()+"\"><i class=\"fa fa-window-close\"></i></a>\n" +
            "    </figure>";
            }
                 if(window.getPictures().size()<4)
                 {
                     show+="</div>"
                             + "<br>\n<label>افزودن عکس"
                         + "(میتوانید تا "
                         + (4-window.getPictures().size())
                         + " عکس اضافه کنید)</label>"+
                         "<form role=\"form\" method=\"post\" enctype=\"multipart/form-data\"  action=\"../../AddPicture\" >\n"
                             + "<input type=\"hidden\" name=\"productId\" value=\""+productId+"\">"
                             + "<input type=\"hidden\" name=\"type\" value=\"window\">"
                              +"<div class=\"form-group\">\n"  +
                    " <input type=\"file\" name=\"picture\" accept=\"image/*\" multiple class=\"form-control\" >\n"
                             + "</div>"
                             +"<div class=\"form-group\">\n" 
                      +" <button type=\"submit\" class=\"btn btn-success\">افزودن</button>\n"
                             + "</div>"    
                             
                         + "</form>" ;
                 }
                         
                show+=" </div>\n"
                        + "</div>"+
                        
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        return show;
    }
    
    public String getCategoryRows() throws SQLException
    {
        
        LinkedList<Category> categories=this.productSQL.getAllCategories();
        String rows="";
        Buttons buttons=new Buttons();
        for(Category category:categories)
        {
            rows+="<tr>"
                    + "<td>"+category.getName()+"</td>"
                    + "<td>"
                    +"<form method=\"post\" action=\"DeleteCategory\">"
                    + "<input type=\"hidden\" name=\"categoryId\" value=\""+category.getCategoryId()+"\">"
                    + buttons.btn_Normal_Danger("حذف", "sumbit", "", false, false)
                    + "</form>"
                    +"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    public String getColorOptions()
    {
        String options="";
        HashMap<String,String> colors=new Parameters().getColorMap();
        for(Map.Entry<String,String> entry:colors.entrySet())
        {
            options+="<option "
                    + "value=\""
                    + entry.getKey()
                    + "\">"
                    + entry.getValue()
                    + "</option>";
        }
        return options;
    }
    
    public String getColorMapRows() throws SQLException
    {
        HashMap<String,String> colors=new Parameters().getColorMap();
        Buttons buttons=new Buttons();
        LinkedList<ColorMap> colormaps=this.productSQL.getColorMaps();
        String rows="";
        for(ColorMap colorMap:colormaps)
        {
            rows+="<tr>"
                    + "<td>"+colorMap.getTaste()+"</td>"
                    + "<td>"+colors.get(colorMap.getColor())+"</td>"
                    + "<td>"
                    +"<form method=\"post\" action=\"DeleteColorMap\">"
                    + "<input type=\"hidden\" name=\"id\" value=\""+colorMap.getId()+"\">"
                    + buttons.btn_Normal_Danger("حذف", "sumbit", "", false, false)
                    + "</form>"
                    +"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
}

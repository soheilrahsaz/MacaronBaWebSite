/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.Order.Part;
import Objects.Order.Receipt;
import Objects.Panel.Buttons;
import Objects.Panel.Labels;
import Objects.Partner.Partner;
import Objects.Partner.PartnerCategory;
import Objects.Partner.PartnerPart;
import Objects.Partner.PartnerProduct;
import Objects.Partner.PartnerReceipt;
import Objects.Partner.PartnerSeries;
import Objects.Payment.Payment;
import Objects.Payment.PaymentType;
import Objects.Product.Category;
import Objects.Product.Picture;
import Objects.Ship.Ship;
import Objects.Ship.ShipType;
import Objects.User.UserAddress;
import SQL.Commands.OrderSQLCommands;
import SQL.Commands.PartnerSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Moses
 */
public class Partners {
    private Connection connection;
    private PartnerSQLCommands partnerSQL;
    private HashMap<Integer,String> productStatuses;
    private Parameters parameters=new Parameters();
    private OrderSQLCommands orderSQL;
    public Partners(Connection connection)
    {
        this.connection=connection;
        this.partnerSQL=new PartnerSQLCommands(connection);
        this.productStatuses=new Parameters().getProductStatuses();
        this.orderSQL=new OrderSQLCommands(connection);
    }
    
    public String getCategoryRows() throws SQLException
    {
        String rows="";
        LinkedList<PartnerCategory> categories=this.partnerSQL.getPartnerCategories();
        Buttons buttons=new Buttons();
        for(PartnerCategory category:categories)
        {
            rows+="<tr>"
                    + "<td>"+category.getName()+"</td>"
                    + "<td>"
                    +"<form method=\"post\" action=\"DeletePartnerCategory\">"
                    + "<input type=\"hidden\" name=\"categoryId\" value=\""+category.getId()+"\">"
                    + buttons.btn_Normal_Danger("حذف", "sumbit", "", false, false)
                    + "</form>"+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    public String getPartnerSeriesRows() throws SQLException
    {
        String rows="";
        LinkedList<PartnerSeries> series=this.partnerSQL.getPartnerSeries();
        Buttons buttons=new Buttons();
        for(PartnerSeries seri:series)
        {
            rows+="<tr onclick=\"window.location='SeriesList/"+seri.getId()+"'\" style=\"cursor:pointer;\">"
                    + "<td>"+seri.getName()+"</td>"
                    + "<td>"+seri.getEffect()+"</td>"
                    + "<td>"
                    +"<form method=\"post\" action=\"DeletePartnerSeries\">"
                    + "<input type=\"hidden\" name=\"seriesId\" value=\""+seri.getId()+"\">"
                    + buttons.btn_Normal_Danger("حذف", "sumbit", "", false, false)
                    + "</form>"+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    public String showPartnerSeries(String uri) throws SQLException
    {
        uri=uri.substring(uri.indexOf("SeriesList")+11);
        int seriesId=Integer.parseInt(uri);
        PartnerSeries seris=this.partnerSQL.getPartnerSeries(seriesId);
        
        String out="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">تغییر دسته بندی</h3>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditPartnerSeries\">\n"
                + "<input type=\"hidden\" name=\"seriesId\" value=\""+seris.getId()+"\" >" +
"              <div class=\"box-body\">\n" +
"                <div class=\"form-group\">\n" +
"                  <label >نام دسته بندی</label>\n" +
"                  <input type=\"text\" name=\"name\" class=\"form-control\" value=\""+seris.getName()+"\" placeholder=\"نام دسته بندی\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >تاثیر قیمت</label>\n" +
"                  <input type=\"number\" min=\"0\" max=\"100\" step=\"0.01\" name=\"effect\" class=\"form-control\" value=\""+seris.getEffect()+"\"  placeholder=\"تاثیر قیمت\">\n" +
"                </div>\n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
"              <div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-warning\">تغییر</button>\n" +
"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        return out;
    }
    
    public String getPartnerSeriesOptions(Partner partner) throws SQLException
    {
        LinkedList<PartnerSeries> series=this.partnerSQL.getPartnerSeries();
        String options="";
        for(PartnerSeries seri:series)
        {
            options+="<option "
                    + "value=\""
                    + seri.getId()
                    
                    + "\""
                    + (partner.getSeries().getId()==seri.getId() ? "selected":"")
                    + ">"
                    + seri.getName()
                    + "</option>";
        }
        return options;
        
    }
    
    public String getPartnerSeriesOptions() throws SQLException
    {
        LinkedList<PartnerSeries> series=this.partnerSQL.getPartnerSeries();
        String options="";
        for(PartnerSeries seri:series)
        {
            options+="<option "
                    + "value=\""
                    + seri.getId()
                    + "\""
                    + ">"
                    + seri.getName()
                    + "</option>";
        }
        return options;
        
    }
    
    public String getCategoriesOptions() throws SQLException
    {
        String options="";
        LinkedList<PartnerCategory> categories=this.partnerSQL.getPartnerCategories();
        for(PartnerCategory category:categories)
        {
            options+="<option "
                    + "value=\""
                    + category.getId()
                    + "\""
                    + ">"
                    + category.getName()
                    + "</option>";
        }
        return options;
    }
    
    public String getProductRows() throws SQLException
    {
        String rows="";
        LinkedList<PartnerProduct> products=this.partnerSQL.getPartnerProducts();
        for(PartnerProduct product:products)
        {
            rows+="<tr onclick=\"window.location='Products/"+product.getProductId()+"'\" style=\"cursor:pointer\">"
                    + "<td>"+product.getName()+"</td>"
                    + "<td><img style=\"width:15%\" src=\""+
                    (!product.getPictures().isEmpty() ? "/MacaronBaData/Products/"+product.getProductId()+"/"+product.getPictures().getFirst().getUrl():"/AdminPanel/dist/img/noPic.png")
                    +"\"></td>"
                    + "<td>"+product.getPrice()+"</td>"
                    + "<td>"+getProductStatus(product.getStatus())+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    /**
     * 0:notReady/1:forSale/2:ranOut/3:commingSoon/4:hidden
     * @param status
     * @return 
     */
    private String getProductStatus(int status)
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
    
    public String getPartnerRows() throws SQLException
    {
        StringBuilder rows=new StringBuilder();
        LinkedList<Partner> partners=this.partnerSQL.getPartners();
        for(Partner partner:partners)
        {
            rows
                .append("<tr onclick=\"window.location='List/"+partner.getPartnerId()+"'\" style=\"cursor:pointer;\" >")
                .append("<td>").append(partner.getName()).append("</td>")
                .append("<td>").append(Methods.Methods.getValOrDash(partner.getSeries().getName())).append("</td>")
                .append("<td>").append(Methods.Methods.getShamsiDate(partner.getDateRegistered())).append("</td>")
                .append("<td>").append(partner.getPhoneNumber()).append("</td>")
                .append("</tr>");
        }
        
        return rows.toString();
    }
    
    public String showPartner(String uri) throws SQLException, DoesntExistException
    {
        uri=uri.substring(uri.indexOf("List")+5);
        int partnerId=Integer.parseInt(uri);
        Partner partner=this.partnerSQL.getPartner(partnerId);
        return partnerData(partner)+getChangePassword(partner)+getAddresses(partnerId)+getPartnerOrderList(partner);
        
    }
    
    
    
    private String partnerData(Partner partner) throws SQLException
    {
        String data="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">نمایش همکار</h3>\n" +
                 "              <div class=\"box-tools\">\n" +
"                <div class=\"input-group input-group-sm\" >\n"
                + "<form method=\"post\" action=\"../DeletePartner\" >"
                + "<input type=\"hidden\" name=\"partnerId\" value=\""+partner.getPartnerId()+"\" >"
                +new Buttons().btn_Normal_Danger("حذف همکار", "submit", "sm", false, false)
                + "</form>" +
"                </div>\n" +
"              </div>\n"+
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditPartner\" >\n"
                + "<input type=\"hidden\" name=\"partnerId\" value=\""+partner.getPartnerId()+"\" >" +
"              <div class=\"box-body\">\n" +
"                \n" +
"                  \n"
                + "<div class=\"form-group\">\n" +
"                  <label >دسته</label>\n" +
"                  <select class=\"form-control\" name=\"series\">\n" +
                    getPartnerSeriesOptions(partner) +
"                  </select>\n" +
"                </div>" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >نام</label>\n" +
"                  <input type=\"text\" name=\"name\" class=\"form-control\" value=\""+partner.getName()+"\"  placeholder=\"نام\" >\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >ایمیل</label>\n" +
"                  <input type=\"text\" name=\"email\" class=\"form-control\" value=\""+Methods.Methods.getValOrDash(partner.getEmail())+"\"  placeholder=\"ایمیل\" disabled>\n" +
"                </div>\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label >شماره موبایل</label>\n" +
"                  <input type=\"text\" name=\"phoneNumber\" class=\"form-control\" value=\""+partner.getPhoneNumber()+"\"  placeholder=\"شماره موبایل\" >\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >تاریخ ثبت نام</label>\n" +
"                  <input type=\"text\" name=\"dateRegistered\" class=\"form-control\" value=\""+Methods.Methods.getShamsiDate(partner.getDateRegistered())+"\"  placeholder=\"تاریخ ثبت نام\" disabled>\n" +
"                </div>\n" +
"                \n" +
"                \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n"
                + "<div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-warning\">تغییر</button>\n" +
"              </div>" +
"\n" +

"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        return data;
    }
    
    private String getChangePassword(Partner partner)
    {
        String data="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">تغییر رمز</h3>\n" +

"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../changePartnerPassword\" >\n"
                + "<input type=\"hidden\" name=\"partnerId\" value=\""+partner.getPartnerId()+"\" >" +
"              <div class=\"box-body\">\n" +
"                \n" +

"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >رمز عبور</label>\n" +
"                  <input type=\"text\" name=\"password\" class=\"form-control\"   placeholder=\"رمز عبور\" >\n" +
"                </div>\n" +
"                  \n" +

"                \n" +
"                \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n"
                + "<div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-warning\">تغییر</button>\n" +
"              </div>" +
"\n" +

"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        return data;
    }
    
    private String getAddresses(int partnerId) throws SQLException
    {
        
        String table="<section class=\"content\">\n" +
"          <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">آدرس ها</h3>\n" +
         
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th style=\"width:20%\">آدرس</th>\n" +
"                  <th>کد پستی</th>\n" +
"                  <th>شماره تماس</th>\n" +
"                  <th>لینک مکان</th>\n" +
"                </tr>\n" +
                getAddressRows(partnerId)+
"              </table>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"      </section>";
        
        return table;
        
        
    }
    
    private String getAddressRows(int userId) throws SQLException
    {
        LinkedList<UserAddress> addresses=this.partnerSQL.getPartnerAddresses(userId);
        String rows="";
        for(UserAddress address:addresses)
        {
            rows+="<tr>"
                    + "<td>"+address.getAddress()+"</td>"
                    + "<td>"+address.getPostalCode()+"</td>"
                    + "<td>"+address.getPhoneNumber()+"</td>"
                    + "<td>"+"not yet"+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    
    public String printProduct(String uri) throws SQLException
    {
        uri=uri.substring(uri.indexOf("Products")+9);
        int productId=Integer.parseInt(uri);
        PartnerProduct product=this.partnerSQL.getPartnerProduct(productId);
        
        String show="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">مشخصات محصول</h3>\n" +
                                "              <div class=\"box-tools\">\n" +
"                <div class=\"input-group input-group-sm\" >\n"
                + "<form method=\"post\" action=\"../DeletePartnerProduct\" >"
                + "<input type=\"hidden\" name=\"productId\" value=\""+productId+"\" >"
                +new Buttons().btn_Normal_Danger("حذف محصول", "submit", "sm", false, false)
                + "</form>" +
"                </div>\n" +
"              </div>\n"+
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditPartnerProduct\" >\n"
                + "<input type=\"hidden\" name=\"productId\" value=\""+product.getProductId()+"\">" +
"              <div class=\"box-body\">\n" +
"                <div class=\"form-group\">\n" +
"                  <label>دسته بندی</label>\n" +
"                  <select name=\"category\" class=\"form-control\">\n" +
                        getCategoryOptionsValues(product.getCategory().getId())+
"                  </select> \n" +
"                </div>\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label >نام</label>\n" +
"                  <input type=\"text\" name=\"name\" class=\"form-control\"  placeholder=\"نام محصول\" value=\""+product.getName()+"\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >قیمت</label>\n" +
"                  <input type=\"number\" name=\"price\" min=\"0\" class=\"form-control\"  placeholder=\"قیمت\" value=\""+product.getPrice()+"\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >تعداد موجود</label>\n" +
"                  <input type=\"number\" name=\"count\" min=\"0\" class=\"form-control\"  placeholder=\"تعداد\" value=\""+product.getCount()+"\">\n" +
"                </div>\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label>توضیحات</label>\n" +
"                  <textarea name=\"description\" class=\"form-control\" placeholder=\"توضیحات\">"+product.getDescription()+"</textarea>\n" +
"                </div>\n" +
 "                <div class=\"form-group\">\n" +
"                  <label>وضعیت</label>\n" +
"                  <select name=\"status\" class=\"form-control\">\n" +
                        getStatusOptions(product.getStatus())+
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
            for(Picture picture:product.getPictures())
            {
               show+= "<figure class=\"product\">\n" +
            "      <img class=\"product__img\" style=\"width:100%;\" src=\"/MacaronBaData/Products/"+product.getProductId()+"/"+picture.getUrl()+" \" alt=\"\">\n" +
            "      <a class=\"product__del\" href=\"../../DeletePicture?type=window&pictureId="+picture.getPictureId()+"\"><i class=\"fa fa-window-close\"></i></a>\n" +
            "    </figure>";
            }
                 if(product.getPictures().size()<4)
                 {
                     show+="</div>"
                             + "<br>\n<label>افزودن عکس"
                         + "(میتوانید تا "
                         + (4-product.getPictures().size())
                         + " عکس اضافه کنید)</label>"+
                         "<form role=\"form\" method=\"post\" enctype=\"multipart/form-data\"  action=\"../../AddPartnerPicture\" >\n"
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
     
     
     private String getCategoryOptionsValues(int value) throws SQLException
    {
        String options="";
        LinkedList<PartnerCategory> categories=this.partnerSQL.getPartnerCategories();
        
        for(PartnerCategory category:categories)
        {
            options+="<option value=\""+category.getId()+"\" "
                    + (value==category.getId() ? "selected":"")
                    + " >"+category.getName()+"</option>";
        }
        
        return options;
    }
     
    private String getPartnerOrderList(Partner partner) throws SQLException, DoesntExistException 
    {
        String table="<section class=\"content\">\n" +
"          <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">سفارشات</h3>\n" +

                
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>نام کاربر</th>\n" +
"                  <th>تاریخ</th>\n" +
"                  <th>قیمت</th>\n" +
"                  <th>پرداخت</th>\n" +
"                  <th>ارسال</th>\n" +
"                  <th>وضعیت</th>\n" +
"                </tr>\n" +
                    getOrderRows(partner)+ 
"              </table>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"      </section>";
        
        return table;
    } 
     
    private String getOrderRows(Partner partner) throws SQLException, DoesntExistException
    {
        String table="";
        LinkedList<PartnerReceipt> receipts=this.partnerSQL.getPartnerReceipts(partner.getPartnerId());
        
        for(PartnerReceipt receipt:receipts)
        {
            Timestamp date=receipt.getDate();
            table+="<tr onclick=\"window.location='../Orders/"+receipt.getOrderId()+"'\" style=\"cursor:pointer;\" >"
                    + "<td>"+receipt.getPartner().getName()+"</td>"
                    + "<td>"+(date==null ? "":date.toString())+"</td>"
                    + "<td>"+getPrice(receipt)+"</td>"
                    + "<td>"+getPaymentStatus(receipt.getPayment())+"</td>"
                    + "<td>"+getShipStatus(receipt.getShip())+"</td>"
                    + "<td>"+getReceiptStatus(receipt)+"</td>"
                    + "</tr>";
        }
         
        
        return table;
    }
    
      public String getOrdersList(HttpServletRequest request) throws SQLException, DoesntExistException
    {
        int receiptStatusId,paymentStatusId,shipStatusId;
        String receiptStatus=request.getParameter("receiptStatus");
        String paymentStatus=request.getParameter("paymentStatus");
        String shipStatus=request.getParameter("shipStatus");
        if(receiptStatus!=null)
        {
            receiptStatusId=this.parameters.getReceiptStatus(receiptStatus);
        }
        if(paymentStatus!=null)
        {
            paymentStatusId=this.parameters.getPaymentStatus(paymentStatus);
        }
        if(shipStatus!=null)
        {
            shipStatusId=this.parameters.getShipStatus(shipStatus);
        }
        
        String table="";
        LinkedList<PartnerReceipt> receipts=this.partnerSQL.getPartnerReceipts();
        
        for(PartnerReceipt receipt:receipts)
        {
            Timestamp date=receipt.getDate();
            table+="<tr onclick=\"window.location='Orders/"+receipt.getOrderId()+"'\" style=\"cursor:pointer;\" >"
                    + "<td>"+receipt.getPartner().getName()+"</td>"
                    + "<td>"+(date==null ? "":date.toString())+"</td>"
                    + "<td>"+getPrice(receipt)+"</td>"
                    + "<td>"+getPaymentStatus(receipt.getPayment())+"</td>"
                    + "<td>"+getShipStatus(receipt.getShip())+"</td>"
                    + "<td>"+getReceiptStatus(receipt)+"</td>"
                    + "</tr>";
        }
         
        
        return table;
    }
      
      
      /**
     * -1:deleted/0:unSubmittedCart/1:submited/2:done
     * @param receipt
     * @return 
     */
    public String getReceiptStatus(PartnerReceipt receipt)
    {
        int status=receipt.getStatus();
        HashMap<Integer,String> receiptStatuses=this.parameters.getReceiptStatuses();
        Labels labels=new Labels();
        switch(status)
        {
            case -1:return labels.getDanger(receiptStatuses.get(-1));
            case 0:return labels.getWarning(receiptStatuses.get(0));
            case 1:return labels.getWarning(receiptStatuses.get(1));
            case 2:return labels.getPrimary(receiptStatuses.get(2));
            case 3:return labels.getSuccess(receiptStatuses.get(3));
            case 4:return labels.getDanger(receiptStatuses.get(4));
            default: return "";
        }
    }
    
    
    /**
     * -1:deleted/0:pending/1:delivered
     * @param ship
     * @return 
     */
    public String getShipStatus(Ship ship) 
    {
        int status=ship.getStatus();
        HashMap<Integer,String> shipStatuses=this.parameters.getShipStatuses();
        Labels labels=new Labels();
        switch(status)
        {
//            case -1:return labels.getDanger(shipStatuses.get(-1));
            case 0:return labels.getWarning(shipStatuses.get(0));
            case 1:return labels.getPrimary(shipStatuses.get(1));
            case 2:return labels.getSuccess(shipStatuses.get(2));
            default: return "";
        }
    }  
      
    /**
     * -1:deleted/0:pending/1:paid
     * @param payment
     * @return 
     */
    public String getPaymentStatus(Payment payment)
    {
        int status=payment.getStatus();
        HashMap<Integer,String> paymentStatuses=this.parameters.getPaymentStatuses();
        Labels labels=new Labels();
        switch(status)
        {
            case -1:return labels.getDanger(paymentStatuses.get(-1));
            case 0:return labels.getWarning(paymentStatuses.get(0));
            case 1:return labels.getSuccess(paymentStatuses.get(1));
            
            default: return "";
        }
    }  
      private int getPrice(PartnerReceipt receipt)
      {
          int price=0;
          for(PartnerPart part:receipt.getParts())
          {
              price+=part.getFinalPrice();
          }
          return price;
      }
      
      
    public String printShowOrder(String uri) throws SQLException, DoesntExistException
    {
        uri=uri.substring(uri.indexOf("Orders")+7);
        int orderId=Integer.parseInt(uri);
        PartnerReceipt receipt=this.partnerSQL.getPartnerReceipt(orderId);
        
        String order=printPaymentSection(receipt)+
"      \n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-6\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">حمل و نقل</h3>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditPartnerShip\">\n"
                + "<input type=\"hidden\" name=\"orderId\" value=\""+orderId+"\" > " +
                 "<input type=\"hidden\" name=\"shipId\" value=\""+receipt.getShip().getShipId()+"\" > " +
"              <div class=\"box-body\">\n" +
"                \n" +
"                <div class=\"form-group\">\n" +
"                  <label >آدرس</label>\n" +
"                  <textarea name=\"address\" class=\"form-control\" rows=\"3\" placeholder=\"آدرس\" disabled=\"\">"+Methods.Methods.getValOrDash(receipt.getShip().getAddress().getAddress())+"</textarea>\n" +
"                </div>\n" +
                "                <div class=\"form-group\">\n" +
"                  <label >کد پستی</label>\n" +
"                  <input name=\"postalCode\" class=\"form-control\"  placeholder=\"کد پستی\" disabled=\"\" value=\""+Methods.Methods.getValOrDash(receipt.getShip().getAddress().getPostalCode())+"\">\n" +
"                </div>\n" +
                                "                <div class=\"form-group\">\n" +
"                  <label >شماره تماس</label>\n" +
"                  <input name=\"phoneNumber\" class=\"form-control\"  placeholder=\"شماره تماس\" disabled=\"\" value=\""+Methods.Methods.getValOrDash(receipt.getShip().getAddress().getPhoneNumber())+"\">\n" +
"                </div>\n" +
"               <div class=\"form-group\">\n" +
"                  <label>وضعیت</label>\n" +
"                  <select class=\"form-control\" name=\"shipStatus\">\n" +
                            getShipStatusOptions(receipt)+
"                  </select>\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label>نوع ارسال</label>\n" +
"                  <select class=\"form-control\" name=\"shipTypeId\">\n" +
                            getShipTypeOptions(receipt)+
"                  </select>\n" +
"                </div>\n" +
"\n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
"              <div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-primary\">اعمال</button>\n" +
"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>"
                
                
                
                +"<section class=\"content\">\n" +
"          <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">لیست خرید</h3>\n" +
"\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>نام محصول</th>\n" +
"                  <th>عکس</th>\n" +
"                  <th>تعداد</th>\n" +
"                  <th>قیمت نهایی</th>\n" +
"                </tr>\n" +
                getPartsTable(receipt)+
"               \n" +
"              </table>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"      </section>"
     
                
     +"<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">وضعیت کلی سفارش</h3>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditPartnerOrder\">\n" +
"              <div class=\"box-body\">\n" +
 "<input type=\"hidden\" name=\"orderId\" value=\""+orderId+"\" > " +
"                  <div class=\"form-group\">\n" +
"                  <label>وضعیت</label>\n" +
"                  <select class=\"form-control\" name=\"status\">\n" +
                    getReceiptOptions(receipt)+
"                  </select>\n" +
"                </div>\n" +
"                  \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
"              <div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-primary\">اعمال</button>\n" +
"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
 "          </section>"; 
        
        
        return order;
    
    }
    
    private String getReceiptOptions(PartnerReceipt receipt)
    {
        HashMap<Integer,String> paymentStatuses=this.parameters.getReceiptStatuses();
        String already=paymentStatuses.get(receipt.getStatus());
        String options="";
        int i=1;
        for(String status:paymentStatuses.values())
        {
            options+="<option value=\""+i+"\" "
                    + (status.equalsIgnoreCase(already) ? "selected":"")
                    + ">"
                    + status
                    + "</option>";
            i++;
        }
        
        
        return options;
    }
    
    
    private String getPartsTable(PartnerReceipt receipt)
    {
        String rows="";
        for(PartnerPart part:receipt.getParts())
        {
            rows+="<tr "
                    + ">"
                    + "<td>"+(part.getProduct().getName())+"</td>"
                    + "<td><img style=\"width:15%;\" src=\"/MacaronBaData/Products/"+(part.getProduct().getPictures().isEmpty() ? "":part.getProduct().getProductId()+"/"+part.getProduct().getPictures().getFirst().getUrl())+"\"></td>"
                    + "<td>"+part.getCount()+"</td>"
                    + "<td>"+part.getFinalPrice()+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    private String getShipStatusOptions(PartnerReceipt receipt)
    {
        HashMap<Integer,String> paymentStatuses=this.parameters.getShipStatuses();
        String already=paymentStatuses.get(receipt.getShip().getStatus());
        String options="";
        int i=0;
        for(String status:paymentStatuses.values())
        {
            options+="<option value=\""+i+"\" "
                    + (status.equalsIgnoreCase(already) ? "selected":"")
                    + ">"
                    + status
                    + "</option>";
            i++;
        }
        
        
        return options;
    }
    
    private String getShipTypeOptions(PartnerReceipt receipt) throws SQLException
    {
        LinkedList<ShipType> types=this.orderSQL.getShipTypes();
        String options="";
        for(ShipType type:types)
        {
            options+="<option value=\""+type.getShipTypeId()+"\" "
                    + (type.getShipTypeId()==receipt.getShip().getShipType().getShipTypeId()? "selected":"")
                    + ">"
                    + type.getName()
                    + "</option>";
        }
        
        
        return options; 
    }
    
    private String printPaymentSection(PartnerReceipt receipt) throws SQLException
    {
        
        String show="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-6\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">پرداخت</h3>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditPartnerPayment\">\n" 
                + "<input type=\"hidden\" name=\"orderId\" value=\""+receipt.getOrderId()+"\" > " 
                + "<input type=\"hidden\" name=\"paymentId\" value=\""+receipt.getPayment().getPaymentId()+"\" > " +
"              <div class=\"box-body\">\n" +
"                <div class=\"form-group\">\n" +
"                  <label >قیمت</label>\n" +
"                  <input type=\"text\" name=\"price\" class=\"form-control\" id=\"exampleInputEmail1\" disabled=\"true\" value=\""+getPrice(receipt)+"\">\n" +
"                </div>\n" +
"                <div class=\"form-group\">\n" +
"                  <label >تاییدیه</label>\n" +
"                  <textarea name=\"approve\" class=\"form-control\" rows=\"3\" placeholder=\"تاییدیه\">"+Methods.Methods.getValOrDash(receipt.getPayment().getApprove())+"</textarea>\n" +
"                </div>\n" +
"               <div class=\"form-group\">\n" +
"                  <label>وضعیت</label>\n" +
"                  <select class=\"form-control\" name=\"paymentStatus\">\n" +
                    getPaymentStatusOptions(receipt)+
"                  </select>\n" +
"                </div>\n" +
"\n" +
"                  <div class=\"form-group\">\n" +
"                  <label>نوع پرداخت</label>\n" +
"                  <select class=\"form-control\" name=\"paymentTypeId\">\n" +
                    getPaymentTypeOptions(receipt)+
"                  </select>\n" +
"                </div>\n" +
"                  \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
"              <div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-primary\">اعمال</button>\n" +
"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      \n" +
" \n";
        
        return show;
    }
    
    private String getPaymentStatusOptions(PartnerReceipt receipt)
    {
        HashMap<Integer,String> paymentStatuses=this.parameters.getPaymentStatuses();
        String already=paymentStatuses.get(receipt.getPayment().getStatus());
        String options="";
        int i=0;
        for(String status:paymentStatuses.values())
        {
            options+="<option value=\""+i+"\" "
                    + (status.equalsIgnoreCase(already) ? "selected":"")
                    + ">"
                    + status
                    + "</option>";
            i++;
        }
        
        
        return options;
    }
    
    private String getPaymentTypeOptions(PartnerReceipt receipt) throws SQLException
    {
        LinkedList<PaymentType> types=this.orderSQL.getPaymentTypes();
        String options="";
        for(PaymentType type:types)
        {
            options+="<option value=\""+type.getPaymentTypeId()+"\" "
                    + (type.getPaymentTypeId()==receipt.getPayment().getPaymentType().getPaymentTypeId() ? "selected":"")
                    + ">"
                    + type.getName()
                    + "</option>";
        }
            
        
        
        return options;
    }
    
}

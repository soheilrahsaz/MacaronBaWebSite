/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.Order.Item;
import Objects.Order.ItemProperty;
import Objects.Order.Part;
import Objects.Order.Receipt;
import Objects.Panel.Buttons;
import Objects.Panel.Labels;
import Objects.Payment.Payment;
import Objects.Payment.PaymentType;
import Objects.Product.PropertyValue;
import Objects.Ship.Ship;
import Objects.Ship.ShipType;
import Objects.User.UserAddress;
import SQL.Commands.OrderHistorySQLCommands;
import SQL.Commands.OrderSQLCommands;
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
public class Orders {
    private OrderHistorySQLCommands orderHisSQL;
    private OrderSQLCommands orderSQL;
    private Parameters parameters=new Parameters();
    public Orders(Connection connection)
    {
        this.orderHisSQL=new OrderHistorySQLCommands(connection);
        this.orderSQL=new OrderSQLCommands(connection);
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
        LinkedList<Receipt> receipts=this.orderHisSQL.getOrderReceipts();
        
        for(Receipt receipt:receipts)
        {
            Timestamp date=receipt.getDate();
            table+="<tr onclick=\"window.location='List/"+receipt.getOderId()+"'\" style=\"cursor:pointer;\" >"
                    + "<td>"+receipt.getUser().getFirstName()+" "+receipt.getUser().getLastName()+"</td>"
                    + "<td>"+(date==null ? "":date.toString())+"</td>"
                    + "<td>"+getPrice(receipt)+"</td>"
                    + "<td>%"+receipt.getDiscount().getEffect()+"</td>"
                    
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
    public String getReceiptStatus(Receipt receipt)
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
    
    /**
     * gets the total price of a receipt
     * @param orderItems
     * @return 
     */
    public int getPrice(Receipt receipt)
    {
        int price=0;
        for(Part part:receipt.getParts())
        {
            price+=part.getFinalPrice();
        }
        price=(int)(price*(100-receipt.getDiscount().getEffect())/100);
        return price;
    }
        
    public String printShowOrder(String uri) throws SQLException, DoesntExistException
    {
        int productId=0,propertyId=0,propertyValueId=0;
        uri=uri.substring(uri.indexOf("List")+5);
        String ids[]=uri.split("/");
        if(ids.length==1)
        {
            int orderId=Integer.parseInt(ids[0]);
            return printOrder(orderId);
        }
        if(ids.length==2)
        {
            int orderId=Integer.parseInt(ids[0]);
            if(ids[1].charAt(0)=='v')
            {
                int partId=Integer.parseInt(ids[1].substring(1));
                return printPartWindow(orderId,partId);
            }
            int partId=Integer.parseInt(ids[1]);
            return printPart(orderId,partId);
        }
//        if(ids.length==3)
//        {
//            int orderId=Integer.parseInt(ids[0]);
//            int partId=Integer.parseInt(ids[1]);
//            int itemId=Integer.parseInt(ids[2]);
//            return printItem(orderId,partId,itemId);
//        }
        
        return "";
    }
    
    private String printOrder(int orderId) throws SQLException, DoesntExistException
    {
        
        Receipt receipt=this.orderHisSQL.getOrderReceipt(orderId);
        
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
"            <form role=\"form\" method=\"post\" action=\"../EditShip\">\n"
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
                
                                "                <div class=\"form-group\">\n" +
"                  <label >لوکیشن</label>\n"+
                (receipt.getShip().getAddress().getLatitude()==0 ? "ندارد":"<a href=\""+getAddressLinkLocation(receipt.getShip().getAddress())+"\">Location</a>")+
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
"                  <th>واحد</th>\n" +
"                  <th>بسته بندی</th>\n" +
"                  <th>تعداد</th>\n" +
"                  <th>قیمت نهایی</th>\n" +
"                  <th>ویترین</th>\n" +
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
"            <form role=\"form\" method=\"post\" action=\"../EditOrder\">\n" +
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
    
    private String getAddressLinkLocation(UserAddress address)
    {
        String link="https://www.google.com/maps/?q=";
        link+=address.getLatitude()+","+address.getLongitude();
        
        
        return link;
    }
    
    private String printPaymentSection(Receipt receipt) throws SQLException
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
"            <form role=\"form\" method=\"post\" action=\"../EditPayment\">\n" 
                + "<input type=\"hidden\" name=\"orderId\" value=\""+receipt.getOderId()+"\" > " 
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
    
    private String printPartWindow(int orderId,int partId)
    {
        String parts="";
        
        return parts;
    }
    
    private String printPart(int orderId,int partId) throws SQLException, DoesntExistException
    {
        Part part=this.orderHisSQL.getPart(partId);
        
        String parts="<section class=\"content\">\n" +
"          <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">لیست ایتم ها</h3>\n" +
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
"                  <th>ردیف</th>\n" +
"                  <th>ویژگی ها</th>\n" +
"                  <th>تعداد</th>\n" +
"                </tr>\n" +
                getItemsTable(part)+
"               \n" +
"              </table>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"      </section>";
        
        return parts;
    }
    
    private String getItemsTable(Part part)
    {
        String table="";
        int i=1;
        for(Item item:part.getItems())
        {
            table+="<tr>"
                    + "<td>"+i+"</td>"
                    + "<td>"+
                    getItemProperties(item)
                    +"</td>"
                    + "<td>"+item.getCount()+"</td>"
                    + "</tr>";
            i++;
        }
        return table;
    }
    
    private String getItemProperties(Item item)
    {
        Buttons buttons=new Buttons();
        String show="<table>";
        for(ItemProperty property:item.getItemProperties())
        {
            show+="<tr>"
                    +"<td>"+buttons.btn_Normal_Info(property.getProperty().getName(), "button", "", false, false)+"</td>"
                    +"<td>";
                    for(PropertyValue value:property.getPropertyValues())
                    {
                        show+=buttons.btn_Normal_Primary(value.getName(), "button", "", false, false);
                    }
            show+="</td>"
                    +"</tr>";
                    
        }
        show+="</table>";
        return show;
    }
    
//    private String printItem(int orderId,int partId,int itemId)
//    {
//        String items="";
//        
//        return items;
//    }
//        
    private String getPartsTable(Receipt receipt)
    {
        String rows="";
        for(Part part:receipt.getParts())
        {
            rows+="<tr "
                    + (!part.isWindow() ? "style=\"cursor:pointer;\" onclick=\"window.location='"+receipt.getOderId()+"/"+part.getPartId()+ "'\"":"")
                    + ">"
                    + "<td>"+(part.isWindow() ? part.getProductWindow().getName():part.getProduct().getName())+"</td>"
                    + "<td><img style=\"width:15%;\" src=\"/MacaronBaData/Products/"+(part.isWindow() ? (part.getProductWindow().getPictures().isEmpty() ? "":part.getProductWindow().getProductId()+"/"+part.getProductWindow().getPictures().getFirst().getUrl()):part.getProduct().getProductId()+"/"+part.getScalePackage().getPicture())+"\"></td>"
                    + "<td>"+Methods.Methods.getValOrDash(part.getScaleValue().getDescription())+"</td>"
                    + "<td>"+Methods.Methods.getValOrDash(part.getScalePackage().getName())+"</td>"
                    + "<td>"+part.getCount()+"</td>"
                    + "<td>"+part.getFinalPrice()+"</td>"
                    + "<td>"+(part.isWindow() ? "<i class=\"fa fa-fw fa-check\"></i>":"<i class=\"fa fa-window-close\"></i>")+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    private String getPaymentStatusOptions(Receipt receipt)
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
    
    private String getPaymentTypeOptions(Receipt receipt) throws SQLException
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
    
    private String getShipStatusOptions(Receipt receipt)
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
    
    private String getShipTypeOptions(Receipt receipt) throws SQLException
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
    
    private String getReceiptOptions(Receipt receipt)
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
}

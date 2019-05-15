/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.InPerson.InPerson;
import Objects.InPerson.InPersonItem;
import Objects.Order.Receipt;
import Objects.Panel.Buttons;
import Objects.Panel.Labels;
import Objects.User.DiscountTicket;
import Objects.User.User;
import Objects.User.UserAddress;
import SQL.Commands.OrderHistorySQLCommands;
import SQL.Commands.UserSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Users {
    private UserSQLCommands userSQL;
    private OrderHistorySQLCommands orderHisSQL;
    private Orders orders;
    public Users(Connection connection)
    {
        this.userSQL=new UserSQLCommands(connection);
        this.orderHisSQL=new OrderHistorySQLCommands(connection);
        this.orders=new Orders(connection);
    }
    
    public String printUsersList() throws SQLException
    {
        
        
        LinkedList<User> users=this.userSQL.getAllUsers();
        String table="";
        for(User user:users)
        {
            table+="<tr onclick=\"window.location='List/"+user.getUserId()+"'\" style=\"cursor:pointer;\">"
                    + "<td>"+Methods.Methods.getValOrDash(user.getFirstName())+" "+Methods.Methods.getValOrDash(user.getLastName())+"</td>"
                    + "<td>"+(user.getRegisterType()==0 ? "در سایت":"توسط ادمین")+"</td>"
                    + "<td>"+Methods.Methods.getShamsiDate(user.getDateRegistered())+"</td>"
                    + "<td>"+user.getPhoneNumber()+"</td>"
                    + "</tr>";
        }
     return table;       
    }
    
    public String printUser(String uri) throws SQLException, DoesntExistException
    {
        uri=uri.substring(uri.indexOf("List")+5);
        String []ids=uri.split("/");
        if(ids.length==1)
        {
            int userId=Integer.parseInt(ids[0]);
            return printUserInfo(userId);
        }
        
        if(ids.length==2)
        {
            int userId=Integer.parseInt(ids[0]);
            int inpersonId=Integer.parseInt(ids[1]);
            return printInPerson(userId,inpersonId);
        }
        
        
        return "";
    }
        
    private String printInPerson(int userId,int inpersonId) throws SQLException
    {
         String show="<section class=\"content\">\n" +
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
"                  <th>توضیح</th>\n" +
"                  <th>قیمت</th>\n" +
"                  <th>عملیات</th>\n" +
"                </tr>\n" +
                getInpersonItemRows(userId,inpersonId)+
"              </table>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
                 
                 
                 
                 "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>افزودن به لیست</label>\n"
                + "<form method=\"post\" action=\"../../AddInPersonItem\"> "
                +"<input  type=\"hidden\" name=\"userId\" class=\"form-control\" value=\""+userId+"\" >\n"
                +"<input  type=\"hidden\" name=\"inpersonId\" class=\"form-control\" value=\""+inpersonId+"\" >\n"
                + "<table>"
                + "<tr>"
                 
                + "<td>"
                 +"<input  type=\"text\" name=\"description\" class=\"form-control\"  placeholder=\"توضیحات\">\n"
                +"</td>"
                 
                 + "<td>"
                 +"<input  type=\"number\" name=\"price\" class=\"form-control\"  placeholder=\"قیمت\">\n"
                +"</td>"
                 
                + "<td>"
                + new Buttons().btn_Normal_Success("افزودن", "submit", "", false, false)
                +"</td>"
                + "</tr>"
                + "</table>"
                
                + "</form>" +
"                </div>\n"
                + "</div>"+ 
                 
                 
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"      </section>";
        
        return show;
    }
    
    private String getInpersonItemRows(int userId,int inpersonId) throws SQLException
    {
        LinkedList<InPersonItem> items=this.orderHisSQL.getInPersonItems(inpersonId);
        String rows="";
        Buttons buttons=new Buttons();
        for(InPersonItem item:items)
        {
            rows+="<tr>"
                    + "<td>"+item.getDescription()+"</td>"
                    + "<td>"+item.getPrice()+"</td>"
                    + "<td>"
                    + "<form method=\"post\" action=\"../../DeleteInpersonItem\">"
                    +"<input  type=\"hidden\" name=\"userId\" class=\"form-control\" value=\""+userId+"\" >\n"
                +"<input  type=\"hidden\" name=\"inpersonId\" class=\"form-control\" value=\""+inpersonId+"\" >\n"
                +"<input  type=\"hidden\" name=\"itemId\" class=\"form-control\" value=\""+item.getItemId()+"\" >\n"
                    + buttons.btn_Normal_Danger("حذف", "submit", "", false, false)
                    + "</form>"
                    +"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    private String printUserInfo(int userId) throws SQLException, DoesntExistException
    {
        User user=this.userSQL.getUserFull(userId);
        
        String show=" <section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
                
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">مشخصات کاربر</h3>\n"
              + "<div class=\"box-tools\">\n"
                + "<form method=\"post\" action=\"../../Sms/SendSms/"+userId+"\" style=\"display:inline;\"> "
                + new Buttons().btn_Normal_Primary("ارسال پیامک", "submit", "", false, false)
                + "</form>" 
                
                + "<form method=\"post\" action=\"../DeleteUser\" style=\"display:inline;\">"
                + "<input type=\"hidden\" name=\"userId\" value=\""+user.getUserId()+"\" >"
                +new Buttons().btn_Normal_Danger("حذف کاربر", "submit", "", false, false)
                + "</form>" +
                
"                </div>" +
"            </div>\n" +
                
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\">\n" +
"              <div class=\"box-body\">\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label for=\"exampleInputEmail1\">نام</label>\n" +
"                  <input type=\"text\" name=\"\" class=\"form-control\"  placeholder=\"نام\" value=\""+user.getFirstName()+"\" disabled>\n" +
"                </div>\n" +
                "                <div class=\"form-group\">\n" +
"                  <label for=\"exampleInputEmail1\">نام خانوادگی</label>\n" +
"                  <input type=\"text\" name=\"\" class=\"form-control\"  placeholder=\"نام خانوادگی\"  value=\""+user.getLastName()+"\" disabled>\n" +
"                </div>\n" +
                "                <div class=\"form-group\">\n" +
"                  <label for=\"exampleInputEmail1\">ایمیل</label>\n" +
"                  <input type=\"email\" name=\"email\" class=\"form-control\"  placeholder=\"ایمیل\"  value=\""+user.getEmail()+"\" disabled>\n" +
"                </div>\n" +
                                "                <div class=\"form-group\">\n" +
"                  <label for=\"exampleInputEmail1\">شماره موبایل</label>\n" +
"                  <input type=\"email\" name=\"phoneNumber\" class=\"form-control\"  placeholder=\"شماره موبایل\" value=\""+user.getPhoneNumber()+"\"  disabled>\n" +
"                </div>\n" +
                  "                <div class=\"form-group\">\n" +
"                  <label for=\"exampleInputEmail1\">تاریخ تولد</label>\n" +
"                  <input type=\"email\" name=\"birthDate\" class=\"form-control\"  placeholder=\"تاریخ تولد\" value=\""+Methods.Methods.getShamsiDate(user.getBirthDate())+"\"  disabled>\n" +
"                </div>\n" +
                                  "                <div class=\"form-group\">\n" +
"                  <label for=\"exampleInputEmail1\">تاریخ ثبت نام</label>\n" +
"                  <input type=\"email\" name=\"dateRegistered\" class=\"form-control\"  placeholder=\"تاریخ ثبت نام\" value=\""+Methods.Methods.getShamsiDate(user.getDateRegistered())+"\"  disabled>\n" +
"                </div>\n" +
"               \n" +
"                \n" +
"                \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
//"              <div class=\"box-footer\">\n" +
//"                <button type=\"submit\" class=\"btn btn-primary\">ارسال</button>\n" +
//"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"    </section>\n" +
"    <!-- /.content -->"
                +getChangePassword(user)
                +getAddresses(userId)
                +getUsersInvited(userId)
                +getOrdersTable(userId)
                +getInPersonTable(userId)
                +getDiscountTable(userId)
                ;
        
        return show;
    }
    
    private String getChangePassword(User user)
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
"            <form role=\"form\" method=\"post\" action=\"../changeUserPassword\" >\n"
                + "<input type=\"hidden\" name=\"userId\" value=\""+user.getUserId()+"\" >" +
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
    
    private String getDiscountTable(int userId) throws SQLException
    {
        String show="<section class=\"content\">\n" +
"          <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">کد های تخفیف</h3>\n" +
"\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>درصد تخفیف</th>\n" +
"                  <th>وضعیت</th>\n" +
"                  <th>تاریخ انقضا</th>\n" +

"                </tr>\n" +
                getDiscountRows(userId)+
"              </table>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
                
                "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>افزودن تخفیف</label>\n"
                + "<form method=\"post\" action=\"../AddDiscountTicket\"> "
                +"<input  type=\"hidden\" name=\"userId\" class=\"form-control\" value=\""+userId+"\" >\n"
                + "<table>"
                + "<tr>"
                + "<td style=\"width:50%;\">"
                + "<input type=\"number\" min=\"0\" max=\"100\" step=\"0.01\" class=\"form-control\" name=\"effect\">"
                + "</td>"
                + "<td>"
               +
"                <div class=\"input-group date\">\n" +
"                  <div class=\"input-group-addon\">\n" +
"                    <i class=\"fa fa-calendar\"></i>\n" +
"                  </div>\n" +
"                  <input type=\"text\" id=\"tarikh\" name=\"date\" class=\"form-control pull-right\">\n" +
"                  <input type=\"hidden\" id=\"tarikhAlt\" name=\"dateAlt\" class=\"form-control pull-right\">\n" +
"                </div>\n" 

                + "</td>"
                + "<td>"
                + new Buttons().btn_Normal_Success("افزودن", "submit", "", false, false)
                + "</td>"
                + "</tr>"
                + "</table>"
                
                
                + "</form>" +
"                </div>\n"
                + "</div>"+ 
                
                
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"      </section>";
        
        return show;
    }
    
    private String getDiscountRows(int userId) throws SQLException
    {
        String rows="";
        Buttons buttons=new Buttons();
        LinkedList<DiscountTicket> discounts=this.userSQL.getDiscounts(userId);
        for(DiscountTicket discount:discounts)
        {
            rows+="<tr>"
                    + "<td>"+discount.getEffect()+"</td>"
                    + "<td>"+getDiscountStatus(discount.getStatus())+"</td>"
                    + "<td>"+Methods.Methods.getShamsiDate(discount.getExpireDate())+"</td>"
                    + "<td>"
                    + "<form  method=\"post\" action=\"../DeleteDiscount\">"
                    +"<input  type=\"hidden\" name=\"userId\" class=\"form-control\" value=\""+userId+"\" >\n"
                    + "<input type=\"hidden\" name=\"discountId\" value=\""+discount.getDiscountId()+"\" >"
                    + buttons.btn_Normal_Danger("حذف تخفیف", "submit", "", false, false)
                    + "</form>"
                    +"</td>"
                    + "</tr>";
        }
            
        return rows;
    }
    
    private String getDiscountStatus(int status)
    {
        Labels labels=new Labels();
        switch(status)
        {
            case 0:return labels.getSuccess("قابل استفاده");
            case 1:return labels.getDanger("استفاده شده");
            default: return "";
        }
    }
    
    private String getInPersonTable(int userId) throws SQLException, DoesntExistException
    {
        
        String show="<section class=\"content\">\n" +
"          <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">خرید های حضوری</h3>\n" +
"\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>تاریخ</th>\n" +
"                  <th>قیمت</th>\n" +
"                  <th>عملیات</th>\n" +

"                </tr>\n" +
                getInpersonRows(userId)+
"              </table>\n" +
"            </div>\n" +
"            <!-- /.box-body -->\n" +
                
                "<div class=\"box-footer\">\n"+
                        " <div class=\"form-group\">\n" +
"                  <label>افزودن به لیست</label>\n"
                + "<form method=\"post\" action=\"../AddInPerson\"> "
                +"<input  type=\"hidden\" name=\"userId\" class=\"form-control\" value=\""+userId+"\" >\n"
                + new Buttons().btn_Normal_Success("افزودن", "submit", "", false, false)
                + "</form>" +
"                </div>\n"
                + "</div>"+ 
                
                
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"      </section>";
        
        return show;
    }
    
    private String getInpersonRows(int userId) throws SQLException, DoesntExistException
    {
        String rows="";
        Buttons buttons=new Buttons();
        LinkedList<InPerson> inpersons=this.orderHisSQL.getInpersons(userId);
        for(InPerson inperson:inpersons)
        {
            rows+="<tr onclick=\"window.location='"+userId+"/"+inperson.getInpersonId()+"'\" style=\"cursor:pointer;\" >"
                    + "<td>"+inperson.getDate().toString()+"</td>"
                    + "<td>"+getPrice(inperson)+"</td>"
                    + "<td>"
                    + "<form method=\"post\" action=\"../DeleteInperson\">" 
                    +"<input  type=\"hidden\" name=\"userId\" class=\"form-control\" value=\""+userId+"\" >\n"
                    +"<input  type=\"hidden\" name=\"inpersonId\" class=\"form-control\" value=\""+inperson.getInpersonId()+"\" >\n"
                    + buttons.btn_Normal_Danger("حذف", "submit", "", false, false)
                    + "</form>"
                    +"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    private int getPrice(InPerson inperson)
    {
        int price=0;
        for(InPersonItem item:inperson.getItems())
        {
            price+=item.getPrice();
        }
        return price;
    }
        
    private String getUsersInvited(int userId) throws SQLException
    {
        String table="<section class=\"content\">\n" +
"          <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">کاربر های دعوت شده</h3>\n" +
            
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th >نام کاربر</th>\n" +
"                  <th>تاریخ ثبت نام</th>\n" +
"                  <th>شماره</th>\n" +
"                </tr>\n" +
                getUsersInvitedRows(userId)+
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
    
    private String getUsersInvitedRows(int userId) throws SQLException
    {
        LinkedList<User> users=this.userSQL.getUsersInvitedBy(userId);
        String rows="";
        for(User user:users)
        {
            rows+="<tr onclick=\"window.location='"+user.getUserId()+"'\" style=\"cursor:pointer;\">"
                    + "<td>"+
                    Methods.Methods.getValOrDash(user.getFirstName())
                    +" "+
                    Methods.Methods.getValOrDash(user.getLastName())+"</td>"
                    + "<td>"+Methods.Methods.getShamsiDate(user.getDateRegistered())+"</td>"
                    + "<td>"+user.getPhoneNumber()+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    private String getAddresses(int userId) throws SQLException
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
                getAddressRows(userId)+
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
        LinkedList<UserAddress> addresses=this.userSQL.getAddresses(userId);
        String rows="";
        for(UserAddress address:addresses)
        {
            rows+="<tr>"
                    + "<td>"+address.getAddress()+"</td>"
                    + "<td>"+address.getPostalCode()+"</td>"
                    + "<td>"+address.getPhoneNumber()+"</td>"
                    + "<td>"+(address.getLatitude()==0 ? "ندارد":"<a href=\""+getAddressLinkLocation(address)+"\">Location</a>")+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    private String getAddressLinkLocation(UserAddress address)
    {
        String link="https://www.google.com/maps/?q=";
        link+=address.getLatitude()+","+address.getLongitude();
        
        
        return link;
    }
    
    private String getOrdersTable(int userId) throws SQLException, DoesntExistException
    {
        String table="<section class=\"content\">\n" +
"          <div class=\"row\">\n" +
"        <div class=\"col-xs-12\">\n" +
"          <div class=\"box\">\n" +
"            <div class=\"box-header\">\n" +
"              <h3 class=\"box-title\">سفارشات</h3>\n" +
"\n" +
//"              <div class=\"box-tools\">\n" +
//"                <div class=\"input-group input-group-sm\" style=\"width: 150px;\">\n" +
//"                  <input type=\"text\" name=\"table_search\" class=\"form-control pull-right\" placeholder=\"جستجو\">\n" +
//"\n" +
//"                  <div class=\"input-group-btn\">\n" +
//"                    <button type=\"submit\" class=\"btn btn-default\"><i class=\"fa fa-search\"></i></button>\n" +
//"                  </div>\n" +
//"                </div>\n" +
//"              </div>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body table-responsive no-padding\">\n" +
"              <table class=\"table table-hover\">\n" +
"                <tr>\n" +
"                  <th>تاریخ</th>\n" +
"                  <th>قیمت</th>\n" +
"                  <th>تخفیف</th>\n" +
"                  <th>پرداخت</th>\n" +
"                  <th>ارسال</th>\n" +
"                  <th>وضعیت</th>\n" +
"                </tr>\n" +
                getOrderRows(userId)+
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
    
    private String getOrderRows(int userId) throws SQLException, DoesntExistException
    {
        
        LinkedList<Receipt> receipts=this.orderHisSQL.getOrderReceipts(userId);
        String rows="";
        for(Receipt receipt:receipts)
        {
            Timestamp date=receipt.getDate();
            rows+="<tr onclick=\"window.location='../../Orders/List/"+receipt.getOderId()+"'\" style=\"cursor:pointer;\" >"
//                    + "<td>"+receipt.getUser().getFirstName()+" "+receipt.getUser().getLastName()+"</td>"
                    + "<td>"+(date==null ? "":date.toString())+"</td>"
                    + "<td>"+this.orders.getPrice(receipt)+"</td>"
                    + "<td>%"+receipt.getDiscount().getEffect()+"</td>"
                    + "<td>"+this.orders.getPaymentStatus(receipt.getPayment())+"</td>"
                    + "<td>"+this.orders.getShipStatus(receipt.getShip())+"</td>"
                    + "<td>"+this.orders.getReceiptStatus(receipt)+"</td>"
                    + "</tr>";
        }
        
        return rows;
    }
    
    public String getBirthDayRows() throws SQLException
    {
        String rows="";
        LinkedList<User> users=this.userSQL.getNearBirthDays();
        for(User user:users)
        {
            rows+="<tr onclick=\"window.location='../Users/List/"+user.getUserId()+"'\" style=\"cursor:pointer;\" >"
                    + "<td>"+user.getFirstName()+" "+user.getLastName()+"</td>"
                    + "<td>"+Methods.Methods.getShamsiDate(user.getBirthDate())+"</td>"
                    + "</tr>";
        }
            
        
        return rows;
    }
    
}

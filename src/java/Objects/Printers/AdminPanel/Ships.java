/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.Panel.Buttons;
import Objects.Ship.Ship;
import Objects.Ship.ShipType;
import SQL.Commands.OrderHistorySQLCommands;
import SQL.Commands.OrderSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Moses
 */
public class Ships {
    private OrderSQLCommands orderSQL;
    private OrderHistorySQLCommands orderHisSQL;
    private Orders orders;
    public Ships(Connection connection)
    {
        this.orderSQL=new OrderSQLCommands(connection);
        this.orderHisSQL=new OrderHistorySQLCommands(connection);
        this.orders=new Orders(connection);
    }
    
    public String getShipTypeRows() throws SQLException
    {
        LinkedList<ShipType> types=this.orderSQL.getShipTypes();
        Buttons buttons=new Buttons();
        String rows="";
        for(ShipType type:types)
        {
            rows+="<tr>"
                    + "<td>"+type.getName()+"</td>"
                    + "<td>"+type.getDescription()+"</td>"
                    + "<td>"
                    +"<form method=\"post\" action=\"DeleteShipType\">"
                    + "<input type=\"hidden\" name=\"shipTypeId\" value=\""+type.getShipTypeId()+"\" >"
                    + buttons.btn_Normal_Danger("حذف", "submit", "",false, false)
                    + "</form>"
                    +"</td>"
                    + "</tr>";
        }
        
        return rows;
    }
    
    public String getShipRows(HttpServletRequest request) throws SQLException, DoesntExistException
    {
        LinkedList<Ship> ships=this.orderHisSQL.getShips();
        String rows="";
        for(Ship ship:ships)
        {
            rows+="<tr onclick=\"window.location='List/"+ship.getShipId()+"'\" style=\"cursor:pointer;\" >"
                    + "<td>"+ship.getAddress().getAddress()+"</td>"
                    + "<td>"+ship.getDate().toString()+"</td>"
                    + "<td>"+this.orders.getShipStatus(ship)+"</td>"
                    + "</tr>";
        }
        return rows;
        
    }
    
    public String getShipInfo(String uri) throws SQLException, DoesntExistException
    {
        uri=uri.substring(uri.indexOf("List")+5);
        int shipId=Integer.parseInt(uri);
        Ship ship=this.orderHisSQL.getShip(shipId);
        
        
        String info="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">اطلاعات ارسال</h3>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\">\n" +
"              <div class=\"box-body\">\n" +
"                \n" +
"                <div class=\"form-group\">\n" +
"                  <label>آدرس</label>\n" +
"                  <textarea name=\"address\" class=\"form-control\" placeholder=\"آدرس\" disabled=\"\">"+ship.getAddress().getAddress()+"</textarea>\n" +
"                </div>\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label >کد پستی</label>\n" +
"                  <input type=\"text\" name=\"postalcode\" class=\"form-control\"  placeholder=\"کد پستی\" disabled=\"\" value=\""+ship.getAddress().getPostalCode()+"\">\n" +
"                </div>\n" +
"                \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >شماره تماس</label>\n" +
"                  <input type=\"text\" name=\"phoneNumber\" class=\"form-control\"  placeholder=\"شماره تماس\" disabled=\"\" value=\""+ship.getAddress().getPhoneNumber()+"\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >تاریخ</label>\n" +
"                  <input type=\"text\" name=\"date\" class=\"form-control\"  placeholder=\"تاریخ\" disabled=\"\" value=\""+ship.getDate().toString()+"\">\n" +
"                </div>\n" +
"                  <div class=\"form-group\">\n" +
"                  <label >وضعیت</label>\n" +
"                  <input type=\"text\" name=\"status\" class=\"form-control\"  placeholder=\"وضعیت\" disabled=\"\" value=\""+new Parameters().getShipStatuses().get(ship.getStatus())+"\">\n" +
"                </div>\n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
"<!--              <div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-success\">افزودن</button>\n" +
"              </div>-->\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        return info;
    }
        
}

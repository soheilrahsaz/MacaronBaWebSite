/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.Admin.Admin;
import Objects.Panel.Buttons;
import Parameters.AdminAccess;
import SQL.Commands.AdminSQLCommands;
import SQL.Commands.AuthenticatorSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Admins {
    private AuthenticatorSQLCommands authSQL;
    private AdminSQLCommands adminSQL;
    private AdminAccess adminAccess;
    
    public Admins(Connection connection) throws SQLException
    {
        this.adminSQL=new AdminSQLCommands(connection);
        this.authSQL=new AuthenticatorSQLCommands(connection); 
        this.adminAccess=new AdminAccess();
    }
    
    public String getAccessCheckBoxes()
    {
        HashMap<String,String> accesses=this.adminAccess.getAdminAccess();
        String checkBox="";
        for(String str:accesses.keySet())
        {
            checkBox+="<div class=\"checkbox\">\n" +
"                    <label>\n" +
"                      <input type=\"checkbox\" name=\""+str+"\" >\n" +
                         accesses.get(str)+
"                    </label>\n" +
"                  </div>";
        }
        return checkBox;
    }
    
    public String getAdminRows() throws SQLException
    {
        LinkedList<Admin> admins=this.adminSQL.getAdmins();
        String rows="";
        for(Admin admin:admins)
        {
            rows+="<tr onclick=\"window.location='List/"+admin.getAdminId()+"'\" style=\"cursor:pointer;\" >"
                    + "<td>"+admin.getUserName()+"</td>"
                    + "<td>"+admin.getDateAdded().toString()+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    public String showAdmin(String uri) throws SQLException, DoesntExistException
    {
        uri=uri.substring(uri.indexOf("List")+5);
        int adminId=Integer.parseInt(uri);
        Admin admin=this.authSQL.getAdmin(adminId);
        
        String show="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">اطلاعات مدیر</h3>\n"
                
                + "<div class=\"box-tools\">\n"
                + "<form method=\"post\" action=\"../DeleteAdmin\">"
                + "<input type=\"hidden\" name=\"adminId\" value=\""+adminId+"\">" 
                + new Buttons().btn_Normal_Danger("حذف مدیر", "submit", "", false, false)
                + "</form>" +
                
"              </div>" +
                
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditAdminPassword\" >\n"
                + "<input type=\"hidden\" name=\"adminId\" value=\""+adminId+"\">" +
"              <div class=\"box-body\">\n" +
"                \n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label >نام کاربری</label>\n" +
"                  <input type=\"text\" name=\"userName\" class=\"form-control\"  placeholder=\"نام کاربری\" value=\""+admin.getUserName()+"\" disabled>\n" +
"                </div>\n" +
                
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >رمز عبور</label>\n" +
"                  <input type=\"text\" name=\"password\"  class=\"form-control\"  placeholder=\"رمز عبور\">\n" +
"                </div>\n" +
"               \n" +
                
  
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                <button type=\"submit\" class=\"btn btn-warning\">تغییر رمز</button>\n" +
"                </div>\n" +

                
     "              <div class=\"box-footer\">\n" +

"              </div>\n"
                + "</form>"
                
                
                + "<form method=\"post\" action=\"../EditAdmin\">" 
                + "<input type=\"hidden\" name=\"adminId\" value=\""+adminId+"\">" +
                 "<input type=\"hidden\" name=\"accessId\" value=\""+admin.getAccess().getAccessId()+"\">" +
                
"                 <div class=\"form-group\">\n" +
"                     <label >سطوح دسترسی</label>\n" +
                    getAccessCheckBoxes(admin)+
"                  \n" +
"                </div> \n" +
"                \n" +

"\n" +
"              <div class=\"form-group\">\n" +
"                <button type=\"submit\" class=\"btn btn-warning\">تغییر سطوح دسترسی</button>\n" +
"              </div>\n" +
"            </form>\n" +
                "              </div>\n" +
"              <!-- /.box-body -->\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        return show;
    }
        
    public String getAccessCheckBoxes(Admin admin)
    {
        HashMap<String,String> accesses=this.adminAccess.getAdminAccess();
        String checkBox="";
        if(admin.getAccess().isMaster())
        {
            checkBox="<div class=\"form-group\">"
                    + "مالک سایت"
                    + "</div>";
            return checkBox;
        }
        
        for(String str:accesses.keySet())
        {
            checkBox+="<div class=\"checkbox\">\n" +
"                    <label>\n" +
"                      <input type=\"checkbox\" name=\""+str+"\" "
                    + (admin.haveAccessTo(str) ? "checked":"")
                    + ">\n" +
                         accesses.get(str)+
"                    </label>\n" +
"                  </div>";
        }
        return checkBox;
    }
        
}

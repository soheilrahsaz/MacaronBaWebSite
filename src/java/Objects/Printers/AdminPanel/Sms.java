/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.Communication.SmsSent;
import Objects.SiteSetting.SmsConfig;
import Objects.User.User;
import Parameters.SmsConfigs;
import SQL.Commands.CommunicationSQLCommands;
import SQL.Commands.SiteSettingSQLCommands;
import SQL.Commands.UserSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Sms {
    private CommunicationSQLCommands comSQL;
    private SiteSettingSQLCommands siteSQL;
    private UserSQLCommands userSQL;
    private String adminPhone;
    public Sms(Connection connection)
    {
        this.comSQL=new CommunicationSQLCommands(connection);
        this.siteSQL=new SiteSettingSQLCommands(connection);
        this.userSQL=new UserSQLCommands(connection);
    }
    
    public String getSmsRows() throws SQLException, DoesntExistException
    {
        LinkedList<SmsSent> smssents=this.comSQL.getSmsSents();
        String rows="";
        for(SmsSent smssent:smssents)
        {
            rows+="<tr>"
                    + "<td>"
                    + Methods.Methods.getValOrDash(smssent.getUser().getFirstName())
                    +" "+Methods.Methods.getValOrDash(smssent.getUser().getLastName())
                    +" "+smssent.getUser().getPhoneNumber()+"</td>"
                    + "<td>"+smssent.getDate().toString()+"</td>"
                    + "<td>"+smssent.getText()+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    public String getSmsConfigCheckBox() throws SQLException
    {
        HashMap<String,String> configs=new SmsConfigs().getSmsConfig();
        SmsConfig smsConfig=this.siteSQL.getSmsConfig();
        this.adminPhone=smsConfig.getAdminNumber();
        String checkBox="";
        for(String str:configs.keySet())
        {
            checkBox+="<div class=\"checkbox\">\n" +
"                    <label>\n" +
"                      <input type=\"checkbox\" name=\""+str+"\" "
                    + (smsConfig.isConfig(str) ? "checked":"")
                    + ">\n" +
                         configs.get(str)+
"                    </label>\n" +
"                  </div>";
        }
        return checkBox;
    }
    
    public String printSendSms(String uri) throws SQLException, DoesntExistException
    {
        uri=uri.substring(uri.indexOf("SendSms")+8);
        int userId=Integer.parseInt(uri);
        User user=this.userSQL.getUserFull(userId); 
        
        String show="<section class=\"content\">\n" + 
"      <div class=\"row\">\n" + 
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">ارسال اس ام اس</h3>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../SendSmsMessage\" >\n"
                + "<input type=\"hidden\" name=\"userId\" value=\""+userId+"\" >" +
"              <div class=\"box-body\">\n" +
"                \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >نام کاربر</label>\n" +
"                  <input type=\"text\" name=\"name\" "
                + "value=\""+Methods.Methods.getValOrDash(user.getFirstName())+" "+Methods.Methods.getValOrDash(user.getLastName())+"\"  "
        + "class=\"form-control\"  placeholder=\"نام کاربر\" disabled=\"\">\n" +
"                </div>\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label >شماره موبایل</label>\n" +
"                  <input type=\"text\" name=\"phoneNumber\" value=\""+user.getPhoneNumber()+"\" class=\"form-control\"  placeholder=\"شماره موبایل\" disabled=\"\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >متن</label>\n" +
"                  <textarea class=\"form-control\" maxlength=\"180\" name=\"text\" placeholder=\"متن\"></textarea>\n" +
"                </div>\n" +
"                \n" +
"                \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
"              <div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-success\">ارسال</button>\n" +
"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        return show;
    }
    
    public String getAdminPhone()
    {
        return this.adminPhone;
    }
        
}

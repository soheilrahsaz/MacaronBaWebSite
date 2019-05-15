/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.Communication.Message;
import Objects.Panel.Labels;
import Objects.Communication.Comment;
import Objects.Panel.Buttons;
import Objects.Product.Category;
import Objects.Text.Text;
import SQL.Commands.CommunicationSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Moses
 */
public class Communications {
    private CommunicationSQLCommands comSQL;
    
    public Communications(Connection connection)
    {
        this.comSQL=new CommunicationSQLCommands(connection);
    }
    
    public String getMessageRows() throws SQLException, DoesntExistException
    {
        LinkedList<Message> messages=this.comSQL.getMessages();
        this.comSQL.readAllContactUs();
        String rows="";
        for(Message message:messages)
        {
            rows+="<tr>"
                    + "<td>"+(message.isIsUser() ? message.getUser().getFirstName()+" "+message.getUser().getLastName():message.getName())+"</td>"
                    + "<td>"+(message.isIsUser() ? message.getUser().getPhoneNumber():message.getPhoneNumber())+"</td>"
                    + "<td>"+(message.isIsUser() ? "<i class=\"fa fa-fw fa-check\"></i>":"<i class=\"fa fa-window-close\"></i>")+"</td>"
                    + "<td>"+message.getText()+"</td>"
                    + "<td>"+getMessageStatus(message.getStatus())+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    public String getCommentRows() throws SQLException, DoesntExistException
    {
        LinkedList<Comment> comments=this.comSQL.getComments();
        String rows="";
        Buttons buttons=new Buttons();
        for(Comment comment:comments)
        {
            rows+="<tr>"
                    + "<td>"+comment.getUser().getFirstName()+" "+comment.getUser().getLastName()+"</td>"
                    + "<td>"+comment.getProductName()+"</td>"
                    + "<td>"+comment.getComment()+"</td>"
                    + "<td>"+getCommentStatus(comment.getStatus())+"</td>"
                    + "<td>"
                    + "<form method=\"post\" action=\"DeleteComment\" style=\"display:inline;\">"
                    + "<input type=\"hidden\" name=\"commentId\" value=\""+comment.getCommentId()+"\">"
                    + buttons.btn_Normal_Danger("حذف", "submit", "", false, false)
                    + "</form>";
            if(comment.getStatus()!=1)
            {
                rows+="<form method=\"post\" action=\"AcceptComment\" style=\"display:inline;\">"
                    + "<input type=\"hidden\" name=\"commentId\" value=\""+comment.getCommentId()+"\">"
                    + buttons.btn_Normal_Success("تایید", "submit", "", false, false)    
                        + "</form>";
            }
                    rows+="</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    private String getCommentStatus(int status)
    {
        Labels labels=new Labels();
        switch(status)
        {
            case 0:return labels.getWarning("منتظر تایید");
            case 1:return labels.getSuccess("تایید شده");
            default:return "";
        }
    }
    
    private String getMessageStatus(int status)
    {
        Labels labels=new Labels();
        switch(status)
        {
            case 0:return labels.getWarning("خوانده نشده");
            case 1:return labels.getSuccess("خوانده شده");
            default:return "";
        }
    }
    
    public String getTextRows() throws SQLException
    {
        String rows="";
        LinkedList<Text> texts=this.comSQL.getTexts();
        for(Text text:texts)
        {
            rows+="<tr onclick=\"window.location='List/"+text.getTextId()+"'\" style=\"cursor:pointer\">"
                    + "<td>"+text.getSubject()+"</td>"
//                    + "<td>"+text.getText()/*(text.getText().length()<100 ? text.getText():text.getText().substring(0,99)+"...")*/+"</td>"
                    + "<td>"+getTextStatus(text.getStatus())+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    private String getTextStatus(int status)
    {
        Labels labels=new Labels();
        switch(status)
        {
            case 0:return labels.getSuccess("نمایان");
            case 1:return labels.getWarning("مخفی");
            default: return "";
        }
    }
    
    public String printText(String uri) throws SQLException
    {
        uri=uri.substring(uri.indexOf("List")+5);
        int textId=Integer.parseInt(uri);
        Text text=this.comSQL.getText(textId);
        String data="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">نمایش مطلب</h3>\n" +
                "<div class=\"box-tools\">\n" +
"                <div class=\"input-group input-group-sm\" >\n"
                + "<form method=\"post\" action=\"../DeleteText\" >"
                + "<input type=\"hidden\" name=\"textId\" value=\""+textId+"\" >"
                +new Buttons().btn_Normal_Danger("حذف مطلب", "submit", "sm", false, false)
                + "</form>" +
"                </div>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditText\">\n"
                + "<input type=\"hidden\" name=\"textId\" value=\""+text.getTextId()+"\">" +
"                \n" +
"              <div class=\"box-body\">\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label >نام مطلب</label>\n" +
"                  <input type=\"text\" name=\"subject\" class=\"form-control\" value=\""+text.getSubject()+"\"  placeholder=\"نام دسته بندی\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >لینک مطلب(لینک انگلیسی)</label>\n" +
"                  <input type=\"text\" name=\"link\" class=\"form-control\" value=\""+text.getLink()+"\" placeholder=\"نام دسته بندی\">\n" +
"                </div>\n" +
"                  \n" +
"                  \n" +
"                  \n" +
"            \n" +
"            <!-- /.box-header -->\n" +
"            <div class=\"box-body pad\">\n" +
"             \n" +
"                <textarea name=\"text\" id=\"mytextarea\" style=\"width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;\">"+text.getText()+"</textarea>\n" +
"              \n" +
"            </div>\n"
                + ""
                + "<div class=\"form-group\">\n" +
"                  <label>وضعیت</label>\n" +
"                  <select name=\"status\" class=\"form-control\">\n" +
                    getTextStatusOptions(text.getStatus())+
"                  </select> \n" +
"                </div>" +
                
                
"          \n" +
"                  \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
"              <div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-success\">اعمال</button>\n" +
"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"              \n" +
"              \n" +
"          </section>";
        
        return data;
    }
    
    private String getTextStatusOptions(int value)
    {
        
        String options="";
        HashMap<Integer,String> statuses=new Parameters().getTextStatuses();
        
        for(Map.Entry<Integer,String> entry:statuses.entrySet())
        {
            options+="<option value=\""+entry.getKey()+"\" "
                    + (value==entry.getKey()? "selected":"")
                    + " >"+entry.getValue()+"</option>";
        }
        
        return options;
    
    }
}

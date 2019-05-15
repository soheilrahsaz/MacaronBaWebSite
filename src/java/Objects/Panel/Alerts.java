/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Panel;

/**
 *
 * @author Soheil
 */
public class Alerts {
    
    public String getCallOut_Danger(String title,String text)
    {
        String callOut="<div class=\"callout callout-danger\">\n" +
"                <h4>"+title+"</h4>\n" +
"\n" +
"                <p>"+text+"</p>\n" +
"              </div>";
        
        
        return callOut;
    }
    
    public String getCallOut_Info(String title,String text)
    {
        String callOut="<div class=\"callout callout-info\">\n" +
"                <h4>"+title+"</h4>\n" +
"\n" +
"                <p>"+text+"</p>\n" +
"              </div>";
        
        
        return callOut;
    }
    
    public String getCallOut_Warning(String title,String text)
    {
        String callOut="<div class=\"callout callout-warning\">\n" +
"                <h4>"+title+"</h4>\n" +
"\n" +
"                <p>"+text+"</p>\n" +
"              </div>";
        
        
        return callOut;
    }
    
    public String getCallOut_Success(String title,String text)
    {
        String callOut="<div class=\"callout callout-success\">\n" +
"                <h4>"+title+"</h4>\n" +
"\n" +
"                <p>"+text+"</p>\n" +
"              </div>";
        
        
        return callOut;
    }
    
    public String getAlert_Danger(String title,String text)
    {
        String alert="<div class=\"alert alert-danger alert-dismissible\">\n" +
"                <button type=\"button\" class=\"close pull-left\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>\n" +
"                <h4><i class=\"icon fa fa-ban\"></i>"+title+"</h4>\n" +
"                "+text+"\n" +
"              </div>";
        
        return alert;
    }
    
    public String getAlert_Info(String title,String text)
    {
        String alert="<div class=\"alert alert-info alert-dismissible\">\n" +
"                <button type=\"button\" class=\"close pull-left\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>\n" +
"                <h4><i class=\"icon fa fa-info\"></i>"+title+"</h4>\n" +
"                "+text+"\n" +
"              </div>";
        
        return alert;
    }
    
    public String getAlert_Warning(String title,String text)
    {
        String alert="<div class=\"alert alert-warning alert-dismissible\">\n" +
"                <button type=\"button\" class=\"close pull-left\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>\n" +
"                <h4><i class=\"icon fa fa-warning\"></i>"+title+"</h4>\n" +
"                "+text+"\n" +
"              </div>";
        
        return alert;
    }
    
    public String getAlert_Success(String title,String text)
    {
        String alert="<div class=\"alert alert-success alert-dismissible\">\n" +
"                <button type=\"button\" class=\"close pull-left\" data-dismiss=\"alert\" aria-hidden=\"true\">×</button>\n" +
"                <h4><i class=\"icon fa fa-check\"></i>"+title+"</h4>\n" +
"                "+text+"\n" +
"              </div>";
        
        return alert;
    }
    
}

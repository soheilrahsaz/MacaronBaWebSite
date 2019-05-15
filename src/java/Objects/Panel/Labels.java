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
public class Labels {
    
    public String getSuccess(String text)
    {
         String label="<span class=\"label label-success\">"+text+"</span>";
        return label;
    }
    
    
    public String getWarning(String text)
    {
         String label="<span class=\"label label-warning\">"+text+"</span>";
        return label;
    }
    
    public String getPrimary(String text)
    {
         String label="<span class=\"label label-primary\">"+text+"</span>";
        return label;
    }
    
    public String getDanger(String text)
    {
         String label="<span class=\"label label-danger\">"+text+"</span>";
        return label;
    }
    
    public String getInfo(String text)
    {
         String label="<span class=\"label label-info\">"+text+"</span>";
        return label;
    }
    
}

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
public class Buttons {
    
    
    /**
     * 
     * 
     * @param text text in the button
     * @param block if true, it will fill the whole line
     * @param size put ( "","lg","sm","xs","flat")
     * @param disabled true for disabled, false for active
     * @param type put ("button","submit",...)
     * @return 
     */
    public String btn_Normal_Default(String text,String type,String size,boolean block,boolean disabled)
    {
        String btn="<button "
                + "type=\""+type+"\""
                + " class=\"btn ";
                if(block){btn+=" btn-block ";} 
                btn+= " btn-default ";
                if(!size.isEmpty()){btn+=" btn-"+size;}
                btn+= " \"";
                if(disabled){btn+= " disabled ";}
                btn+=">"+text+"</button>";
        
        return btn;
    }
    
    
    /**
     * 
     * 
     * @param text text in the button
     * @param block if true, it will fill the whole line
     * @param size put ( "","lg","sm","xs","flat")
     * @param disabled true for disabled, false for active
     * @return 
     */
    public String btn_Normal_Primary(String text,String type,String size,boolean block,boolean disabled)
    {
        String btn="<button "
                + "type=\""+type+"\""
                + " class=\"btn ";
                if(block){btn+=" btn-block ";} 
                btn+= " btn-primary ";
                if(!size.isEmpty()){btn+=" btn-"+size;}
                btn+= " \"";
                if(disabled){btn+= " disabled ";}
                btn+=">"+text+"</button>";
        
        return btn;
    }
    
    
    /**
     * 
     * 
     * @param text text in the button
     * @param block if true, it will fill the whole line
     * @param size put ( "","lg","sm","xs","flat")
     * @param disabled true for disabled, false for active
     * @return 
     */
    public String btn_Normal_Success(String text,String type,String size,boolean block,boolean disabled)
    {
       String btn="<button "
                + "type=\""+type+"\""
                + " class=\"btn ";
                if(block){btn+=" btn-block ";} 
                btn+= " btn-success ";
                if(!size.isEmpty()){btn+=" btn-"+size;}
                btn+= " \"";
                if(disabled){btn+= " disabled ";}
                btn+=">"+text+"</button>";
        
        return btn;
    }
    
    
    /**
     * 
     * 
     * @param text text in the button
     * @param block if true, it will fill the whole line
     * @param size put ( "","lg","sm","xs","flat")
     * @param disabled true for disabled, false for active
     * @return 
     */
    public String btn_Normal_Info(String text,String type,String size,boolean block,boolean disabled)
    {
        String btn="<button "
                + "type=\""+type+"\""
                + " class=\"btn ";
                if(block){btn+=" btn-block ";} 
                btn+= " btn-info ";
                if(!size.isEmpty()){btn+=" btn-"+size;}
                btn+= " \"";
                if(disabled){btn+= " disabled ";}
                btn+=">"+text+"</button>";
        
        return btn;
    }
    
    
    /**
     * 
     * 
     * @param text text in the button
     * @param block if true, it will fill the whole line
     * @param size put ( "","lg","sm","xs","flat")
     * @param disabled true for disabled, false for active
     * @return 
     */
    public String btn_Normal_Danger(String text,String type,String size,boolean block,boolean disabled)
    {
        String btn="<button "
                + "type=\""+type+"\""
                + " class=\"btn ";
                if(block){btn+=" btn-block ";} 
                btn+= " btn-danger ";
                if(!size.isEmpty()){btn+=" btn-"+size;}
                btn+= " \"";
                if(disabled){btn+= " disabled ";}
                btn+=" >"+text+"</button>";
        
        return btn;
    }
    
    
    /**
     * 
     * 
     * @param text text in the button
     * @param block if true, it will fill the whole line
     * @param size put ( "","lg","sm","xs","flat")
     * @param disabled true for disabled, false for active
     * @return 
     */
    public String btn_Normal_Warning(String text,String type,String size,boolean block,boolean disabled)
    {
        String btn="<button "
                + "type=\""+type+"\""
                + " class=\"btn ";
                if(block){btn+=" btn-block ";} 
                btn+= " btn-warning ";
                if(!size.isEmpty()){btn+=" btn-"+size;}
                btn+= " \"";
                if(disabled){btn+= " disabled";}
                btn+=" >"+text+"</button>";
        
        return btn;
    }
    
}

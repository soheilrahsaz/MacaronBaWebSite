/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Panel;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Moses
 */
public class AlertCode {
    private final HashMap<String,String> codesWarning=new HashMap<String,String>();
    private final HashMap<String,String> codesSuccess=new HashMap<String,String>();
    private final HashMap<String,String> codesDanger=new HashMap<String,String>();
    public AlertCode()
    {
        this.codesWarning.put("noaccess", "شما به این قسمت دسترسی ندارید");
        this.codesWarning.put("empty", "مقادیر نمی تواند خالی باشد");
        this.codesWarning.put("exists", "این مقدار قبلا موجود می باشد");
        this.codesWarning.put("nameexists", "این نام قبلا موجود می باشد");
        this.codesWarning.put("loginfirst", "باید ابتدا وارد شوید");
        this.codesWarning.put("wrong", "نام کاربری یا رمز عبور غلط می باشد");
        this.codesWarning.put("nofile", "هیچ فایلی انتخاب نشده است");
        this.codesWarning.put("toolong", "مقدار بسیار بلند است");
        
        this.codesSuccess.put("ok", "با موفقیت انجام شد");
        
        this.codesDanger.put("block", "بدلیل تلاش بیش از حد، شما 3 دقیقه از لاگین کردن ممنوع شدید");
        this.codesDanger.put("ban", "اکانت مدیریت شما بسته شده است");
    }
    
    public String getSentence(HttpServletRequest request)
    {
        try 
        {
            String att=request.getParameter("att").toLowerCase();
            String sentence="";
            sentence+=this.codesWarning.get(att);
            if(!sentence.startsWith("null"))
            {
                return new Alerts().getAlert_Warning("Alert", sentence);
            }
            sentence="";
            
            sentence+=this.codesSuccess.get(att);
            if(!sentence.startsWith("null"))
            {
                return new Alerts().getAlert_Success("Success", sentence);
            }
            sentence="";
            
            sentence+=this.codesDanger.get(att);
            if(!sentence.startsWith("null"))
            {
                return new Alerts().getAlert_Danger("Danger", sentence);
            }
        } catch (Exception e) 
        {
        }
        
        return "";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.ULocale;
import java.io.File;
import java.sql.Timestamp;
import java.util.Random;

/**
 *
 * @author Moses
 */
public class Methods {
    public static String convertToPersianDigits(String value) {
        String newValue = value.replace("1","١").replace("2","٢").replace("3","٣").replace("4","٤").replace("5","٥")
                .replace("6","٦").replace("7","٧").replace("8","٨").replace("9","٩").replace("0","٠");
        return newValue;
    }
    public static String convertToEnglishDigits(String text)
    {
        text = text.replace("١", "1").replace("٢", "2").replace("٣", "3").replace("٤", "4").replace("٥", "5")
                .replace("٦", "6").replace("٧", "7").replace("٨", "8").replace("٩", "9").replace("٠", "0")
                .replace("۱", "1").replace("۲", "2").replace("۳", "3").replace("۴", "4").replace("۵", "5")
                .replace("۶", "6").replace("۷", "7").replace("۸", "8").replace("۹", "9").replace("۰", "0");
        return text;
    }
    
    public static boolean isEnoughLong(String text,int max)
    {
        return text.length()<=max;
    }
    public static boolean isEnoughLong(long text,int min,int max)
    {
        int textLen=0;
        while(text>0)
        {
            text/=10;
            textLen++;
        }
        if(textLen>=min && textLen<=max)
            return true;
        else
            return false;
    }
    public static boolean isPhoneNumber(String text)
    {
        try
        {
            text=convertToEnglishDigits(text);
            long t=Long.parseLong(text);
            return isEnoughLong(t,7,15);
            
        }catch(Exception ex)
        {
            return false;
        }
    }
    
    public static boolean isNumber(String text)
    {
        try
        {
            text=convertToEnglishDigits(text);
            Long.parseLong(text);
            return true;
        }catch(Exception ex)
        {
            return false;
        }
    }
    
    public static boolean isNumberDouble(String text)
    {
        try
        {
            text=convertToEnglishDigits(text);
            Double.parseDouble(text);
            return true;
        }catch(Exception ex)
        {
            return false;
        }
    }
    public static boolean isMelliId(String text)
    {
        text=convertToEnglishDigits(text);
        if(text.length()!=10)
        {
            return false;
        }
        
        try{
            char[] code=text.toCharArray();
            int sum=0;
            for (int i = 0; i < 9; i++)
            {
                sum+=(10-i)*(code[i]-48);
            }

            sum=sum%11;
            if(sum<2)
            {
                if(sum==(code[9]-48))
                {
                    return true;
                }else
                {
                    return false;
                }
            }else
            {
                if((11-sum)==(code[9]-48))
                {
                    return true;
                }else
                {
                    return false;
                }
            }
        }
        catch(Exception ex)
        {
            return false;
        }
        
    }
    
    public static int getRandomInt(int max)
    {
        Random rand=new Random();
        return rand.nextInt(max);
    }
    
    public static int getRandomInt()
    {
        Random rand=new Random();
        return rand.nextInt();
    }
    /**
     * checks parameters to see whether they're null or empty and return true if it has 1 null nor empty and return false if it's all full
     * @param parameters
     * @return true if it has at least 1 null or empty parameter and returns false if all of the data are full
     */
    public static boolean isNullOrEmpty(String... parameters)
    {
        for(String parameter:parameters)
        {
            if(parameter==null)
            {
                return true;
            }
            
            if(parameter.trim().isEmpty())
            {
                return true;
            }
        }
        
        return false;
    }
    
    public static String mkdirForSlideShow(String relative)
    {
        String rootPath = System.getProperty("catalina.home");
        String dir =  rootPath+"/"+relative;
        File directory = new File(dir);
        if (!directory.exists()){directory.mkdir();}
        System.out.println(dir);
        return dir+"/";
    }    
    
    public static String mkdirForProuductPic(String relativePath, int prodcutId)
    {
        String rootPath = System.getProperty("catalina.home");
        String dir =  rootPath+"/"+relativePath+"/"+String.valueOf(prodcutId);
        File directory = new File(dir);
        if (!directory.exists()){directory.mkdir();}
        
        return dir+"/";
    }
    
    public static String mkdirForVideo(String relativePath,int sectionId)
    {
        String rootPath = System.getProperty("catalina.home");
        String dir =  rootPath+"/"+relativePath+"/";
        File directory = new File(dir);
        if (!directory.exists()){directory.mkdir();}
        
        return dir+"/";
    }
    
    public static String getExtension(String fileName)
    {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        
        if (i > 0) 
            extension = fileName.substring(i+1);
        
        return extension;
        
    }
    
    public static String getShamsiDate(Timestamp timestamp)
    {
        if(timestamp==null)
        {
            return "";
        }
        ULocale locale=new ULocale("fa_IR@calendar=persian");
        DateFormat df=DateFormat.getDateInstance(DateFormat.FULL,locale);
        return df.format(timestamp);
    }
    
    public static String getValOrDash(String value)
    {
        if(isNullOrEmpty(value))
        {
            return "-";
        }else
        {
            return value;
        }
    }
    
    /**
     * gets phoneNumber and turns to 98xxxxxxxx form
     * @param phone
     * @return 
     */
    public static String normalizePhone(String phone)
    {
        if(phone.startsWith("0098"))
        {
            phone=phone.substring(2);
            return phone;
        }
        
        if(phone.startsWith("0"))
        {
            phone="98"+phone.substring(1);
            return phone;
        }
        
        if(phone.startsWith("+98"))
        {
            phone=phone.substring(1);
            return phone;
        }
        if(phone.startsWith("98"))
        {
            return phone;
        }
        
        if(phone.startsWith("9"))
        {
            phone="98"+phone;
            return phone;
        }
        
        return phone;
    }
}

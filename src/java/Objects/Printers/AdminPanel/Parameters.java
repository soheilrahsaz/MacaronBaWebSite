/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class Parameters {
    private HashMap<Integer,String> productStatuses=new HashMap<Integer,String>();
    private HashMap<Integer,String> propertyStatuses=new HashMap<Integer,String>();
    private HashMap<Integer,String> receiptStatuses=new HashMap<Integer,String>();
    private HashMap<Integer,String> paymentStatuses=new HashMap<Integer,String>();
    private HashMap<Integer,String> shipStatuses=new HashMap<Integer,String>();
    private HashMap<Integer,String> textStatuses=new HashMap<Integer,String>();
    private HashMap<String,String> colorMap=new HashMap<String,String>();
    
    
    public Parameters()
    {
        this.productStatuses.put(0, "نا تمام");
        this.productStatuses.put(1, "موجود");
        this.productStatuses.put(2, "ناموجود");
        this.productStatuses.put(3, "به زودی");
        this.productStatuses.put(4, "مخفی");
        
        this.propertyStatuses.put(0, "غیر فعال");
        this.propertyStatuses.put(1, "ناتمام");
        this.propertyStatuses.put(2, "موجود");
        
        this.receiptStatuses.put(-1, "پاک شده");
//        this.receiptStatuses.put(0, "سبدخرید ثبت نشده");
        this.receiptStatuses.put(1, "در صف انتظار");
        this.receiptStatuses.put(2, "در حال پیگیری");
        this.receiptStatuses.put(3, "انجام شده");
        this.receiptStatuses.put(4, "رد شده");
        
//        this.paymentStatuses.put(-1, "پاک شده");
        this.paymentStatuses.put(0, "منتظر پرداخت");
        this.paymentStatuses.put(1, "پرداخت شده");
        
//        this.shipStatuses.put(-1, "پاک شده");
        this.shipStatuses.put(0, "منتظر دریافت");
        this.shipStatuses.put(1, "ارسال شده");
        this.shipStatuses.put(2, "دریافت شده");
        
        this.textStatuses.put(0, "نمایان");
        this.textStatuses.put(1, "مخفی");
        
        this.colorMap.put("red","قرمز");
        this.colorMap.put("yellow","زرد");
        this.colorMap.put("purple","بنفش");
        this.colorMap.put("lime","سبز");
        this.colorMap.put("orange","نارنجی");
        this.colorMap.put("black","سیاه");
        this.colorMap.put("cream","کرمی");
        this.colorMap.put("pink","صورتی");
        this.colorMap.put("brown","فهوه ای");
    }

    public HashMap<String, String> getColorMap() {
        return colorMap;
    }
    
    

    public HashMap<Integer, String> getTextStatuses() {
        return textStatuses;
    }
    
    
    
    public HashMap<Integer,String> getPropertyStatuses()
    {
        return this.propertyStatuses;
    }
    
    public int getPropertyStatus(String status)
    {
        int i=0;
        for(String str:this.propertyStatuses.values())
        {
            if(str.equalsIgnoreCase(status))
                return i;
            i++;
        }
        return i; 
    }
    
    public HashMap<Integer,String> getProductStatuses()
    {
        return this.productStatuses;
    }
    
    public int getProductStatus(String status)
    {
        int i=0;
        for(String str:this.productStatuses.values())
        {
            if(str.equalsIgnoreCase(status))
                return i;
            i++;
        }
        return i;
    }
    
    public HashMap<Integer,String> getReceiptStatuses()
    {
        return this.receiptStatuses;
    }
    
    public HashMap<Integer,String> getPaymentStatuses()
    {
        return this.paymentStatuses;
    }
    
    public HashMap<Integer,String> getShipStatuses()
    {
        return this.shipStatuses;
    }
    
    public int getReceiptStatus(String status)
    {
        int i=-1;
        for(String str:this.receiptStatuses.values())
        {
            if(str.equalsIgnoreCase(status))
                return i;
            i++;
        }
        return i; 
    }
    
    public int getPaymentStatus(String status)
    {
        int i=0;
        for(String str:this.paymentStatuses.values())
        {
            if(str.equalsIgnoreCase(status))
                return i;
            i++;
        }
        return i; 
    }
    
    public int getShipStatus(String status)
    {
        int i=0;
        for(String str:this.shipStatuses.values())
        {
            if(str.equalsIgnoreCase(status))
                return i;
            i++;
        }
        return i; 
    }
        
}

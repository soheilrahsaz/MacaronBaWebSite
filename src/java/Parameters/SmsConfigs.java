/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parameters;

import java.util.HashMap;

/**
 *
 * @author Moses
 */
public class SmsConfigs {
    private HashMap<String,String> smsConfig=new HashMap<String,String>();
    
    public SmsConfigs()
    {
        smsConfig.put("register", "ارسال اس ام اس خوش آمد گویی بعد از ثبت نام");
        
        smsConfig.put("setOrder", "ارسال اس ام اس بعد از ثبت سفارش");
        smsConfig.put("payOrder", "ارسال اس ام اس بعد از پرداخت هزینه سفارش");
        smsConfig.put("sendOrder", "ارسال اس ام اس بعد از ارسال سفارش");
//        smsConfig.put("approveOrder", "ارسال اس ام اس بعد از تایید سفارش");
//        smsConfig.put("receiveOrder", "ارسال اس ام اس بعد از دریافت سفارش");
        smsConfig.put("statusOrder", "ارسال اس ام اس بعد از تغییر وضعیت سفارش");
        
        smsConfig.put("setOrderManeger", "ارسال اس ام اس به مدیر بعد از ثبت هر سفارش");
        
        smsConfig.put("addUser", "ارسال اس ام اس بعد از افزودن کاربر توسط مدیر");
        
        smsConfig.put("birthDay", "ارسال اتوماتیک اس ام اس تبریک تولد در روز تولد");
        
        
    }

    public HashMap<String, String> getSmsConfig() {
        return smsConfig;
    }
    
    
}

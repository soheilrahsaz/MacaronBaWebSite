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
public class AdminAccess {
    private HashMap<String,String> access=new HashMap<String,String>();
    
    public AdminAccess()
    {
        access.put("addProduct", "افزودن محصول");
        access.put("removeProduct", "حذف کردن محصول");
        access.put("editProduct", "ویرایش محصول");
        access.put("viewProduct", "نمایش محصول");
        
        access.put("addWindow", "افزودن به ویترین");
        access.put("editWindow", "ویرایش محصول ویترین");
        access.put("removeWindow", "حذف محصول ویترین");
        access.put("viewWindow", "نمایش محصول ویترین");
        
        access.put("addCategory", "افزودن دسته بندی");
        access.put("removeCategory", "حذف دسته بندی");
        
        access.put("viewOrder", "نمایش سفارشات");
        access.put("editOrder", "ویرایش سفارشات");
        
        access.put("viewUsers", "نمایش کاربران");
        access.put("addUser", "افزودن کاربر");
        access.put("editUsers", "ویرایش کاربران");
        access.put("deleteUser", "حذف کاربران");
        
        access.put("viewShip", "نمایش حمل و نقل");
        access.put("addShipType", "افزودن روش حمل و نقل");
        access.put("deleteShipType", "حذف کردن روش حمل و نقل");
        
        access.put("viewPayment", "نمایش پرداخت ها");
        access.put("editPaymentType", "ویرایش روش پرداخت");
        
        access.put("viewMessage", "نمایش پیغام ها");
        access.put("viewComment", "نمایش نظرات");
        access.put("editComment", "ویرایش نظرات");
        
        access.put("editSms", "تنظیمات پنل اس ام اس");
        access.put("sendSms", "ارسال اس ام اس");
        
        access.put("addVideo", "اپلود ویدیو");
        access.put("editVideo", "تغییر ویدیو");
        access.put("addSection", "افزودن بخش ویدیو");
        access.put("editSection", "تغییر بخش ویدیو");
        
        access.put("editSiteView", "تغییر ظاهرسایت");
        
        access.put("editInPerson", "بخش خرید های حضوری");
        
        access.put("showPartners", "نمایش همکاران");
        access.put("addPartner", "افزودن همکار");
        access.put("editPartner", "تغییر همکار");
        access.put("orderPartner", "سفارش همکاران");
        access.put("productPartner", "محصولات همکاران");
        
        access.put("texts", "متن ها");
    }
        
    public HashMap<String,String> getAdminAccess()
    {
        return access;
    }
}

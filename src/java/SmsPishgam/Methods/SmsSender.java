/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SmsPishgam.Methods;

import SQL.Commands.SiteSettingSQLCommands;
import SmsPishgam.Objects.Sms;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class SmsSender {
    private SiteSettingSQLCommands siteSQL;
    public SmsSender(Connection connection)
    {
        this.siteSQL=new SiteSettingSQLCommands(connection);
    }
    
    /**
     * sends the sms and if it was successful it will add it to data base
     * @param sms
     * @return true if successful and false if fails
     */
    public boolean sendSms(Sms sms)
    {
            HashMap<String,String> data=new HashMap<String,String>();
            data.put("password", Parameters.PASSOWRD);
            data.put("Username", Parameters.USER_NAME);
            data.put("SmsText", sms.getText());
            data.put("Receivers", sms.getReciever());
      
        try {
            
            String stats;
            stats=new ConnectionToPhp().setUrl(Parameters.URL).setHttpConnectionSettings(0).setDataToSend(data, 2)
                    .excute().get();
            
            JSONObject response=new JSONObject(stats);
            boolean status=response.optBoolean("IsSuccessful");
            
            if(status && sms.getUserId()!=0)
            {
                this.siteSQL.addSMS(sms.getUserId(), sms.getText());
            }

            return status;
            
        } catch (Exception ex) {
//            Logger.getLogger(SmsSender.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
        
    }
}

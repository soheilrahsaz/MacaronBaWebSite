/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.SiteSetting;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Moses
 */
public class SmsConfig {
    private ArrayList<String> configs;
    private String adminNumber;
    
      
    
    public SmsConfig setConfigs(ArrayList<String> configs) {
        this.configs = configs;
        return this;
    }
    
    public SmsConfig setConfigs(String configs)
    {
        this.configs=new ArrayList<String>();
        String strs[]=configs.split(";");
        this.configs.addAll(Arrays.asList(strs));
        return this;
    }
    
    /**
     * 
     * @param config 'register', 'setOrder', 'payOrder', 'sendOrder', 'birthDay', 'setOrderManeger', 'addUser', 'statusOrder'
     * @return 
     */
    public boolean isConfig(String config)
    {
        return this.configs.contains(config);
    }

    public String getAdminNumber() {
        return adminNumber;
    }

    public SmsConfig setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
        return this;
    }
    
    
}

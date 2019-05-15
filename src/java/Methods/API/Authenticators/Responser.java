/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Authenticators;

import Objects.Authenticators.User.AuthSafe;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class Responser {
    
    public JSONObject response(JSONObject data,int status)
    {
        JSONObject object=new JSONObject();
        object.put("status", status);
        object.put("data", data);
        return object;
    }
    
    public JSONObject response(JSONArray data,int status)
    {
        JSONObject object=new JSONObject();
        object.put("status", status);
        object.put("data", data);
        return object;
    }
    
    public JSONObject response(int status)
    {
        JSONObject object=new JSONObject();
        object.put("status", status);
        object.put("data", new JSONObject());
        return object;
    }
    
    public JSONObject getAuthObject(AuthSafe auth)
    {
        JSONObject authObject=new JSONObject();
        authObject.put("time", auth.getAuth().getAuthTime()+600000);
        authObject.put("valid", auth.isValid());
        authObject.put("blockRequest", auth.isAuthRequestBlock());
        return authObject;
    }
}

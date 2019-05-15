/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Communications;

import Objects.Chat.Chat;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class CommunicationJsoner {
    
    public JSONArray getChatsArray(LinkedList<Chat> chats)
    {
        JSONArray chatsArray=new JSONArray();
        int i=0;
        for(Chat chat:chats)
        {
            chatsArray.put(i,getChatJson(chat));
            i++;
        }
        return chatsArray;
    }
    
    public JSONObject getChatJson(Chat chat)
    {
        JSONObject chatObject=new JSONObject();
        chatObject.put("id", chat.getId());
        chatObject.put("date", chat.getDate().toString());
        chatObject.put("sender", chat.getSender());
        chatObject.put("status", chat.getStatus());
        chatObject.put("text", chat.getText());
        return chatObject;
    }
    
    public JSONArray getAdminChatsArray(LinkedList<Chat> chats)
    {
        JSONArray chatsArray=new JSONArray();
        int i=0;
        for(Chat chat:chats)
        {
            chatsArray.put(i,getAdminChatJson(chat));
            i++;
        }
        return chatsArray;
    }
    
    public JSONObject getAdminChatJson(Chat chat)
    {
        JSONObject chatObject=new JSONObject();
        chatObject.put("id", chat.getId());
        chatObject.put("userId", chat.getUserId());
        chatObject.put("name", chat.getName());
        chatObject.put("date", chat.getDate().toString());
        chatObject.put("sender", chat.getSender());
        chatObject.put("status", chat.getStatus());
        chatObject.put("text", chat.getText());
        return chatObject;
    }
}

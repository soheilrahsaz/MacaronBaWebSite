/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.Communications;

import Objects.Chat.Chat;
import Objects.Communication.Comment;
import Objects.Communication.Message;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class CommunicationObjector {
    public Comment getCommentObject(JSONObject commentJson)
    {
        Comment comment=new Comment();
        comment.setComment(commentJson.optString("comment"));
        comment.setProductId(commentJson.optInt("productId"));
        return comment;
    }
    
    public Message getMessageObject(JSONObject messageJson)
    {
        Message message=new Message();
        message.setName(messageJson.optString("name"));
        message.setPhoneNumber(messageJson.optString("phoneNumber"));
        message.setText(messageJson.optString("text"));
        return message;
    }
    
    public Chat getChatObject(JSONObject chatJson)
    {
        Chat chat=new Chat();
        chat.setText(chatJson.optString("text"));
        chat.setUserId(chatJson.optInt("userId"));
        return chat;
    }
}

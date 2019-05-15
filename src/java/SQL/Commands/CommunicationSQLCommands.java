/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import ExceptionsChi.DoesntExistException;
import Objects.Chat.Chat;
import Objects.Communication.Message;
import Objects.Communication.Comment;
import Objects.Communication.SmsSent;
import Objects.Text.Text;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Moses
 */
public class CommunicationSQLCommands {
     private Connection connection;
    private UserSQLCommands userSQL;
    private ProductSQLCommands productSQL;
    
    public CommunicationSQLCommands(Connection connection)
    {
        this.connection=connection;
        this.userSQL=new UserSQLCommands(connection);
        this.productSQL=new ProductSQLCommands(connection);
    }
    
    /**
     * retrieves all comments based on given status
     * @param status 
     * @return -1:deleted/0:pending/1:approved
     * @throws SQLException 
     */
    public LinkedList<Comment> getComments(int status) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_comment` WHERE `status`=?");
        prep.setInt(1, status);
        ResultSet res=prep.executeQuery();
        LinkedList<Comment> comments=new LinkedList<Comment>();
        while(res.next())
        {
            Comment comment=new Comment();
            comment.setComment(res.getString("comment")).setCommentId(res.getInt("commentId"))
                    .setDate(res.getTimestamp("date")).setProductId(res.getInt("productId"))
                    .setProductName(getProductName(res.getInt("productId")))
                    .setUser(this.userSQL.getUserFull(res.getInt("userId")))
                    .setStatus(status);
            comments.add(comment);
        }
        return comments;
    }
    
    /**
     * retrieves all comments based on given status
     * @param status 
     * @return -1:deleted/0:pending/1:approved
     * @throws SQLException 
     */
    public LinkedList<Comment> getComments() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_comment` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Comment> comments=new LinkedList<Comment>();
        while(res.next())
        {
            Comment comment=new Comment();
            comment.setComment(res.getString("comment")).setCommentId(res.getInt("commentId"))
                    .setDate(res.getTimestamp("date")).setProductId(res.getInt("productId"))
                    .setProductName(getProductName(res.getInt("productId")))
                    .setUser(this.userSQL.getUserFull(res.getInt("userId")))
                    .setStatus(res.getInt("status"));
            comments.add(comment);
        }
        return comments;
    }
    
    /**
     * retrieves all comments based on given status
     * @param status 
     * @return -1:deleted/0:pending/1:approved
     * @throws SQLException 
     */
    public LinkedList<Comment> getCommentsApproved(int productId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_comment` WHERE `productId`=? AND `status`=1");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        LinkedList<Comment> comments=new LinkedList<Comment>();
        while(res.next())
        {
            Comment comment=new Comment();
            comment.setComment(res.getString("comment")).setCommentId(res.getInt("commentId"))
                    .setDate(res.getTimestamp("date")).setProductId(res.getInt("productId"))
                    .setProductName(getProductName(res.getInt("productId")))
                    .setUser(this.userSQL.getUserFull(res.getInt("userId")))
                    .setStatus(res.getInt("status"));
            comments.add(comment);
        }
        return comments;
    }
    
    /**
     * retrieves one comment based on it's unique id 
     * @param commentId
     * @return a comment object
     * @throws SQLException
     * @throws DoesntExistException if it doesn't exist
     */
    public Comment getComment(int commentId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_comment` WHERE `commentId`=?");
        prep.setInt(1, commentId);
        ResultSet res=prep.executeQuery();
         Comment comment=new Comment();
        if(!res.first())
        {
            return comment;
        }
       
        comment.setComment(res.getString("comment")).setCommentId(commentId)
            .setDate(res.getTimestamp("date")).setProductId(res.getInt("productId"))
                .setProductName(getProductName(res.getInt("productId")))
                .setUser(this.userSQL.getUserFull(res.getInt("userId")))
            .setStatus(res.getInt("status"));
        return comment;
    } 
    /**
     * retrieves all messages from data base 
     * @return a linked list of Message Objects
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Message> getMessages() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `message` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Message> messages=new LinkedList<Message>();
        while(res.next())
        {
            Message message=new Message();
            message.setMesssageId(res.getInt("messageId")).setText(res.getString("text"))
                    .setStatus(res.getInt("status")).setIsUser(res.getBoolean("isUser"));
            if(message.isIsUser())
            {
                message.setUser(this.userSQL.getUserFull(res.getInt("userId")));
            }else
            {
                message.setName(res.getString("name")).setPhoneNumber(res.getString("phoneNumber"));
            }
            messages.add(message);
        }
        return messages;
    }
    
    /**
     * finds the name of a product whether it's window or product
     * @param productId the if of the product or window
     * @return the name of the thing
     * @throws SQLException 
     */
    public String getProductName(int productId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `name` FROM `product` WHERE `productId`=?");
        prep.setInt(1, productId);
        ResultSet res=prep.executeQuery();
        if(res.first())
        {
            return res.getString("name");
        }
        
        PreparedStatement prep2=this.connection.prepareStatement("SELECT `name` FROM `product_window` WHERE `productId`=?");
        prep2.setInt(1, productId);
        ResultSet res2=prep2.executeQuery();
        if(res2.first())
        {
            return res2.getString("name");
        }
        
        return "none";
    }
        
    /**
     * retrieves all sms sent
     * @return
     * @throws SQLException 
     */
    public LinkedList<SmsSent> getSmsSents() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `sms_sent` WHERE `userId`!=-3");
        ResultSet res=prep.executeQuery();
        LinkedList<SmsSent> SmsSents=new LinkedList<SmsSent>();
        while(res.next())
        {
            SmsSent smssent=new SmsSent();
            smssent.setId(res.getInt("id")).setDate(res.getTimestamp("date"))
                    .setText(res.getString("text")).setUser(this.userSQL.getUserFull(res.getInt("userId")));
            SmsSents.add(smssent);
        }
        return SmsSents;
    }
    
    /**
     * deletes a comment completely
     * @param commentId
     * @throws SQLException 
     */
    public void deleteComment(int commentId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM `product_comment` WHERE `commentId`=?");
        prep.setInt(1, commentId);
        prep.execute();
    }
    
    /**
     * changes the status of a comment to 1
     * @param commentId
     * @throws SQLException 
     */
    public void acceptComment(int commentId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `product_comment` SET `status`=1 WHERE `commentId`=?");
        prep.setInt(1, commentId);
        prep.execute();
    }
    
    /**
     * shows if comment id exists or not
     * @param commentId
     * @return true if it does and false if it doesn't
     * @throws SQLException 
     */
    public boolean commentIdExists(int commentId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `product_comment` WHERE `commentId`=?");
        prep.setInt(1, commentId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds a comment to database and returns the id of the added comment
     * @param productId
     * @param userId
     * @param comment
     * @return the id of the added comment
     * @throws SQLException 
     */
    public int addComment(int productId,int userId,String comment) throws SQLException
    {
        int commentId;
        Random random=new Random();
        while(true)
        {
            commentId=Math.abs(random.nextInt());
            if(!commentIdExists(commentId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `product_comment` (`commentId`,`productId`,`userId`,`comment`) VALUES (?,?,?,?)");
        prep.setInt(1, commentId);
        prep.setInt(2, productId);
        prep.setInt(3, userId);
        prep.setString(4, comment);
        prep.execute();
        
        return commentId;
    }
    
    /**
     * shows if message id exists or not 
     * @param messageId
     * @return true if it does and false if it doesn't
     * @throws SQLException 
     */
    public boolean messageIdExists(int messageId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `message` WHERE `messageId`=?");
        prep.setInt(1, messageId);
        return prep.executeQuery().first();
    }
    
    /**
     * adds a message from a non user client
     * @param name
     * @param phoneNumber
     * @param text
     * @return the id of the added message
     * @throws SQLException 
     */
    public int addMessage(String name,String phoneNumber,String text) throws SQLException
    {
        int messageId;
        Random random=new Random();
        while(true)
        {
            messageId=Math.abs(random.nextInt());
            if(!messageIdExists(messageId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `message` (`messageId`,`isUser`,`name`,`phoneNumber`,`text`) VALUES (?,?,?,?,?)");
        prep.setInt(1, messageId);
        prep.setInt(2, 0);
        prep.setString(3, name);
        prep.setString(4, phoneNumber);
        prep.setString(5, text);
        prep.execute();
        
        return messageId;
    }
    
    /**
     * adds a message for a user client 
     * @param useId
     * @param text
     * @return the id of the added message
     * @throws SQLException 
     */
    public int addMessage(int useId,String text) throws SQLException
    {
        int messageId;
        Random random=new Random();
        while(true)
        {
            messageId=Math.abs(random.nextInt());
            if(!messageIdExists(messageId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `message` (`messageId`,`isUser`,`userId`,`text`) VALUES (?,?,?,?)");
        prep.setInt(1, messageId);
        prep.setInt(2, 1);
        prep.setInt(3, useId);
        prep.setString(4, text);
        prep.execute();
        
        return messageId;
    }
    
    public void readAllContactUs() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `message` SET `status`=1");
        prep.execute();
    }
    
    /**
     * retrieves all texts except the deleted ones
     * @return
     * @throws SQLException 
     */
    public LinkedList<Text> getTexts() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `text` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Text> texts=new LinkedList<Text>();
        while(res.next())
        {
            Text text=new Text();
            text.setTextId(res.getInt("id")).setSubject(res.getString("subject")).setLink(res.getString("link"))
                    .setText(res.getString("text"))
                    .setStatus(res.getInt("status"));
            texts.add(text);
        }
        return texts;
    }
    
    /**
     * retrieves a text
     * @param textId
     * @return
     * @throws SQLException 
     */
    public Text getText(int textId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `text` WHERE `id`=?");
        prep.setInt(1, textId);
        ResultSet res=prep.executeQuery();
        Text text=new Text();
        if(!res.first())
        {
            return text;
        }
        text.setTextId(res.getInt("id")).setSubject(res.getString("subject")).setLink(res.getString("link"))
                .setText(res.getString("text"))
                .setStatus(res.getInt("status"));
        return text;
        
    }
    /**
     * adds a text to data base
     * @param subject
     * @param link
     * @param text
     * @throws SQLException 
     */
    public void addText(String subject,String link,String text) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `text` (`subject`,`link`,`text`) VALUES (?,?,?)");
        prep.setString(1, subject);
        prep.setString(2, link);
        prep.setString(3, text);
        prep.execute();
    }
    
    /**
     * updates the values of a text 
     * @param textId
     * @param subject
     * @param link
     * @param text
     * @param status
     * @throws SQLException 
     */
    public void updateText(int textId,String subject,String link,String text,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `text` SET `subject`=?,`link`=?,`text`=?,`status`=? WHERE `id`=?");
        prep.setString(1, subject);
        prep.setString(2, link);
        prep.setString(3, text);
        prep.setInt(4, status);
        prep.setInt(5, textId);
        prep.execute();
    }
    
    /**
     * changes the status of the text to -1
     * @param textId
     * @throws SQLException 
     */
    public void deleteText(int textId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `text` SET `status`=-1 WHERE `id`=?");
        prep.setInt(1, textId);
        prep.execute();
    }

    /**
     * adds a chat message into data base,
     * @param userId
     * @param text
     * @param sender 0 for user and 1 for admin
     * @param status 0:notReadByUser/1:readByUser
     * @param adminStatus 0:notReadByAdmin/1:readByAdmin
     * @throws SQLException 
     */
    public void addChat(int userId,String text,int sender,int status,int adminStatus) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `chat_message` (`userId`,`text`,`sender`,`status`,`adminStatus`) VALUES (?,?,?,?,?)");
        prep.setInt(1, userId);
        prep.setString(2, text);
        prep.setInt(3, sender);
        prep.setInt(4, status);
        prep.setInt(5, adminStatus);
        prep.execute();
    }
    
    /**
     * returns all chats from a user based on its userId
     * @param userId
     * @return
     * @throws SQLException 
     */
    public LinkedList<Chat> getChats(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `chat_message` WHERE `userId`=?");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<Chat> chats=new LinkedList<>();
        while(res.next())
        {
            Chat chat=new Chat();
            chat.setId(res.getInt("id")).setUserId(res.getInt("userId")).setDate(res.getTimestamp("date"))
                    .setAdminStatus(res.getInt("adminStatus"))
                    .setText(res.getString("text")).setStatus(res.getInt("status")).setSender(res.getInt("sender"));
            chats.add(chat);
        }
        return chats;
    }
    
    /**
     * each user can only send 20 message every minutes, this function shows if the user is allowed to send more messages at the moment or not 
     * @param userId
     * @return
     * @throws SQLException 
     */
    public boolean isChatFree(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `chat_message` WHERE `userId`=? AND `date`>?");
        prep.setInt(1, userId);
        prep.setTimestamp(2, new Timestamp(System.currentTimeMillis()-60000));
        ResultSet res=prep.executeQuery();
        res.last();
        if(res.getRow()>=20)
        {
            return false;
        }
        return true;
    }
    
    /**
     * this function is meant to be called repeatedly to get new chats
     * @param userId
     * @return
     * @throws SQLException 
     */
    public LinkedList<Chat> getNewChats(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `chat_message` WHERE `userId`=? AND `status`=0");
        prep.setInt(1, userId);
        ResultSet res=prep.executeQuery();
        LinkedList<Chat> chats=new LinkedList<Chat>();
        while(res.next())
        {
            Chat chat=new Chat();
            chat.setId(res.getInt("id")).setUserId(res.getInt("userId")).setDate(res.getTimestamp("date")).setAdminStatus(res.getInt("adminStatus"))
                    .setText(res.getString("text")).setStatus(res.getInt("status")).setSender(res.getInt("sender"));
            chats.add(chat);
        }
        return chats;
    }
    
    /**
     * marks all chats as read
     * @param userId
     * @throws SQLException 
     */
    public void markReadChats(int userId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `chat_message` SET `status`=1 WHERE `userId`=?");
        prep.setInt(1, userId);
        prep.execute();
    }
    
    /**
     * this function is meant to be called repeatedly from admin panel to get chats from all users
     * @return
     * @throws SQLException 
     */
    public LinkedList<Chat> getAdminNewChats() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `chat_message` WHERE `adminStatus`=0");
        ResultSet res=prep.executeQuery();
        LinkedList<Chat> chats=new LinkedList<Chat>();
        while(res.next())
        {
            Chat chat=new Chat();
            chat.setId(res.getInt("id")).setUserId(res.getInt("userId")).setDate(res.getTimestamp("date"))
                    .setAdminStatus(res.getInt("adminStatus"))
                    .setText(res.getString("text")).setStatus(res.getInt("status")).setSender(res.getInt("sender"));
            chats.add(chat);
        }
        return chats;
    }
    
    /**
     * marks chats as read for admin
     * @throws SQLException 
     */
    public void markReadAdminChats() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `chat_message` SET `adminStatus`=1 ");
        prep.execute();
    }
    
    /**
     * puts the names of the users in the chats
     * @param chats
     * @return
     * @throws SQLException 
     */
    public void putNames(LinkedList<Chat> chats) throws SQLException
    {
        int userId=0;
        String name="";
        for(Chat chat:chats)
        {
            if(chat.getUserId()==userId)
            {
                chat.setName(name);
            }else
            {
                userId=chat.getUserId();
                name=this.userSQL.getName(userId);
                chat.setName(name);
            }
        }
        
    }
}

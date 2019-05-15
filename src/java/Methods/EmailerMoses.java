/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Soheil
 */
public class EmailerMoses {
    private Connection connection;
    private String EmailServer;
    private String hostSMTP;
    private String hostPOP3;
    private String hostIMAP;
    private String user;
    private String password;
    private Authenticator auth;
    private Properties propsForSmtp;
    private Properties propsForPop3;
    private Properties propsForIMAP;
    private Session sessionSMTP;
    private Session sessionPOP3;
    private Session sessionIMAP;
    private int portSMTP;
    private int portPOP3;
    private int portIMAP;
    
    /**
     * 
     * @param user email address of the user
     * @param password password of the user
     * @throws Exception 
     */
    public EmailerMoses(String user,String password,String host) throws Exception
    {
        
        this.user=user;
        this.password=password;
        this.portSMTP=465;
        this.portPOP3=995;
        this.portIMAP=993;
//        setConnection();
//        getEmailServer();
        
        
//        setHosts();
        this.hostSMTP=host;
        this.hostPOP3=host;
        this.hostIMAP=host;
        setConfigs();
        
        

        
    }
    
    private void setConfigs()
    {
        setAuth();
        setPropertiesForPop3();
        setPropertiesForSmtp();
        setPropertiesForIMAP();
        this.sessionPOP3=Session.getInstance(this.propsForPop3,this.auth);
        this.sessionSMTP=Session.getInstance(this.propsForSmtp,this.auth);
        this.sessionIMAP=Session.getInstance(this.propsForIMAP,this.auth);
    }
    
    /**
     * 
     * @param user email address of the user
     * @param password password of the user
     * @param portSMTP giving manual port for SMTP protocol,it's default is 465
     * @param portPOP3 giving manual port for POP3 protocol,it's default is 995
     * @param portIMAP giving manual port for IMAP protocol,it's default is 993
     * @throws Exception 
     */
    public EmailerMoses(String user,String password,int portSMTP,int portPOP3,int portIMAP,String host) throws Exception
    {
        this.user=user;
        this.hostSMTP=host;
        this.hostPOP3=host;
        this.hostIMAP=host;
        this.password=password;
        this.portSMTP=portSMTP;
        this.portPOP3=portPOP3;
        this.portIMAP=portIMAP;
        setConfigs();
        
    }
    /**
     * sends a normal email to an email address with SMTP protocol
     * @param to target email   
     * @param text content text 
     * @param subject email subject
     * @throws MessagingException
     * @throws SQLException 
     */
    public void sendEmail(String to,String text,String subject) throws MessagingException, SQLException
    {
        MimeMessage message=new MimeMessage(this.sessionSMTP);
        
        message.setSubject(subject,"UTF-8");
        message.setRecipients(Message.RecipientType.TO, to);
        message.setText(text, "UTF-8");
        message.setHeader("from", this.user);
        Transport.send(message);
        addSentEmail(to,text,subject);
    }
    /**
     * sends a normal email to multiple email addresses with SMTP protocol
     * @param to target email   
     * @param text content text 
     * @param subject email subject
     * @throws MessagingException
     * @throws SQLException 
     */
    public void sendEmail(String[] to,String text,String subject) throws MessagingException, SQLException
    {
        MimeMessage message=new MimeMessage(this.sessionSMTP);
        message.setSubject(subject);
        Address[] addresses=new Address[to.length];
        for (int i = 0; i < to.length; i++)
        {
            addresses[i]=new InternetAddress(to[i]);
        }
        
        message.setRecipients(Message.RecipientType.TO, addresses);
        message.setText(text, "UTF-8");
        Transport.send(message);
        addSentEmail(to,text,subject);
        
    }
     /**
     * sends and email with attachment to one email address with SMTP protocol
     * @param to email address of targets
     * @param text email content text
     * @param subject email subject
     * @param fileName fileName to be send
     * @param filePath path of file to be send
     * @throws AddressException
     * @throws MessagingException 
     */
    public void sendEmailWithAttachment(String to,String text,String subject,String fileName,String filePath) throws AddressException, MessagingException
    {
        MimeMessage message2=new MimeMessage(this.sessionSMTP);
        message2.setFrom(new InternetAddress(user));
        message2.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message2.setSubject(subject);

        BodyPart messageBodyPart1=new MimeBodyPart();
        messageBodyPart1.setText(text);

        MimeBodyPart messageBodyPart2=new MimeBodyPart();

        DataSource source=new FileDataSource(filePath);
        messageBodyPart2.setDataHandler(new DataHandler(source));
        messageBodyPart2.setFileName(fileName);


        Multipart multipart=new MimeMultipart();
        multipart.addBodyPart(messageBodyPart1);
        multipart.addBodyPart(messageBodyPart2);

        message2.setContent(multipart);

        Transport.send(message2);
        addSentEmailWithAttach(to,text,subject,fileName,filePath);

    }
    /**
     * sends and email with attachment to multiple email addresses with SMTP protocol
     * @param to email addresses of targets
     * @param text email content text
     * @param subject email subject
     * @param fileName fileName to be send
     * @param filePath path of file to be send
     * @throws AddressException
     * @throws MessagingException 
     */
    public void sendEmailWithAttachment(String[] to,String text,String subject,String fileName,String filePath) throws AddressException, MessagingException
    {
        Address[] addresses=new Address[to.length];
        for (int i = 0; i < to.length; i++)
        {
            addresses[i]=new InternetAddress(to[i]);
        }
        
        MimeMessage message2=new MimeMessage(this.sessionSMTP);
        message2.setFrom(new InternetAddress(user));
        message2.addRecipients(Message.RecipientType.TO,addresses);
        message2.setSubject(subject);

        BodyPart messageBodyPart1=new MimeBodyPart();
        messageBodyPart1.setText(text);

        MimeBodyPart messageBodyPart2=new MimeBodyPart();

        DataSource source=new FileDataSource(filePath);
        messageBodyPart2.setDataHandler(new DataHandler(source));
        messageBodyPart2.setFileName(fileName);


        Multipart multipart=new MimeMultipart();
        multipart.addBodyPart(messageBodyPart1);
        multipart.addBodyPart(messageBodyPart2);

        message2.setContent(multipart);

        Transport.send(message2);
        addSentEmailWithAttach(to,text,subject,fileName,filePath);
    }
    
    /**
     * receives emails with pop3 protocol that returns unread emails
     * @param folderName "INBOX" ...
     * @param openType READ_ONLY =1 , READ_WRITE=2
     * @return
     * @throws NoSuchProviderException
     * @throws MessagingException 
     */
    public Message[] getMessagesPOP3(String folderName,int openType) throws NoSuchProviderException, MessagingException
    {
        Store store=this.sessionPOP3.getStore("pop3");
        store.connect();

        Folder folder=store.getFolder(folderName);
        folder.open(openType);

        return folder.getMessages();
    }
    
    /**
     * receives emails with pop3 protocol that returns all emails
     * @param folderName "INBOX" ...
     * @param openType READ_ONLY =1 , READ_WRITE=2
     * @return
     * @throws NoSuchProviderException
     * @throws MessagingException 
     */
    public Message[] getMessagesIMAP(String folderName,int openType) throws NoSuchProviderException, MessagingException
    {
        Store store=this.sessionIMAP.getStore("imap");
        store.connect();

        Folder folder=store.getFolder(folderName);
        folder.open(openType);

        return folder.getMessages();
    }
    
    private void addSentEmailWithAttach(String[] to,String text,String subject,String fileName,String filePath)
    {
        
    }
    
    private void addSentEmailWithAttach(String to,String text,String subject,String fileName,String filePath)
    {
        
    }
    
    private void addSentEmail(String to,String text,String subject) throws SQLException
    {
        
    }
    private void addSentEmail(String[] to,String text,String subject) throws SQLException
    {
        
    }
    
    private void setPropertiesForPop3()
    {
        this.propsForPop3=new Properties();
        this.propsForPop3.put("mail.pop3.host", hostPOP3);
        this.propsForPop3.put("mail.pop3.user",user);
        this.propsForPop3.put("mail.pop3.starttls.enable","true");
        this.propsForPop3.put("mail.pop3.port",this.portPOP3);
        this.propsForPop3.put("mail.pop3.socketFactory" , this.portPOP3 );
        this.propsForPop3.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        
    }
    
    private void setPropertiesForIMAP()
    {
        this.propsForIMAP=new Properties();
        this.propsForIMAP.put("mail.imap.host", hostIMAP);
        this.propsForIMAP.put("mail.imap.user",user);
        this.propsForIMAP.put("mail.imap.starttls.enable","true");
        this.propsForIMAP.put("mail.imap.port",this.portIMAP);
        this.propsForIMAP.put("mail.imap.socketFactory" , this.portIMAP );
        this.propsForIMAP.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }
    
    private void setPropertiesForSmtp()
    {
        this.propsForSmtp=new Properties();
        this.propsForSmtp.put("mail.smtp.host",this.hostSMTP);  
        this.propsForSmtp.put("mail.smtp.user",this.user);
        this.propsForSmtp.put("mail.smtp.from",this.user);
        this.propsForSmtp.put("mail.smtp.auth", "true");
        this.propsForSmtp.put("mail.smtp.debug","true");
        this.propsForSmtp.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        this.propsForSmtp.put("mail.smtp.starttls.enable","true");
        this.propsForSmtp.put("mail.smtp.port", this.portSMTP);
        this.propsForSmtp.put("mail.smtp.socketFactoryPort",this.portSMTP);
    }
    
//    private void setHosts() throws SQLException, Exception
//    {
//        PreparedStatement prep=this.connection.prepareStatement("SELECT `smtp`,`pop3`,`imap` FROM tbl_emailservers WHERE `emailServer`=?");
//        prep.setString(1, this.EmailServer);
//        ResultSet res=prep.executeQuery();
//        res.last();
//        if(res.getRow()==0)
//        {
//            throw new Exception("EmailServer Not Found");
//        }
//        res.first();
//        
//        this.hostSMTP=res.getString("smtp");
//        this.hostPOP3=res.getString("pop3");
//        this.hostIMAP=res.getString("imap");
//    }
    
    private void setAuth()
    {
        this.auth=new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(user, password);
            }
        };
    }
    
//    private void getEmailServer()
//    {
//        int atSign=this.user.indexOf("@");
//        int dot=this.user.indexOf(".", atSign);
//        this.EmailServer=this.user.substring(atSign+1,dot);
//
//    }
    
//    private void setConnection() throws ClassNotFoundException, SQLException
//    {
//        Class.forName("com.mysql.jdbc.Driver");
//        this.connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/email?useUnicode=true&characterEncoding=UTF-8"
//                , "root", "");
//    }
    /*
        CREATE TABLE IF NOT EXISTS `email`.`email.send`
    `id` INT NOT NULL AUTO_INCREMENT , `email` VARCHAR(50) NOT NULL ,
                 `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,"
                 `content` TEXT NOT NULL ,
                 PRIMARY KEY (`id`)) ENGINE = InnoDB;
        -----------
    CREATE TABLE `email`.`email.sender` ( `id` INT NOT NULL AUTO_INCREMENT , `emailId` INT NOT NULL , `to` VARCHAR(50) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;
        
    
    */
}

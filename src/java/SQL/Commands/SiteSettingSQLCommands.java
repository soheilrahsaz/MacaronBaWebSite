/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL.Commands;

import ExceptionsChi.DoesntExistException;
import Objects.SiteSetting.Advertisment;
import Objects.SiteSetting.LinkStat;
import Objects.SiteSetting.SiteInfo;
import Objects.SiteSetting.SlideShow;
import Objects.SiteSetting.Video;
import Objects.SiteSetting.VideoSection;
import Objects.SiteSetting.SmsConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Moses
 */
public class SiteSettingSQLCommands {
    private Connection connection;
    private AuthenticatorSQLCommands authSQL;
    private UserSQLCommands userSQL;
    private AdminSQLCommands adminSQL;
    
    public SiteSettingSQLCommands(Connection connection) 
    {
        this.connection=connection;
        this.userSQL=new UserSQLCommands(connection);
        this.adminSQL=new AdminSQLCommands(connection);
        this.authSQL=new AuthenticatorSQLCommands(connection);
    }
    
    /**
     * retrieves all videos
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Video> getVideos() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `video` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<Video> videos=new LinkedList<Video>();
        while(res.next())
        {
            Video video=new Video();
            video.setVideoId(res.getInt("videoId")).setUrl(res.getString("url"))
                    .setName(res.getString("name")).setTags(res.getString("tags"))
                    .setSection(getVideoSection(res.getInt("section"))).setDescription(res.getString("description"))
                    .setStatus(res.getInt("status")).setUploadType(res.getInt("uploaderType"));
            
            if(video.isUploadType())
            {
                video.setUploadByAdmin(this.authSQL.getAdmin(res.getInt("uploadBy")));
            }else
            {
                video.setUploadByUser(this.userSQL.getUserFull(res.getInt("uploadBy")));
            }
            videos.add(video);
        }
        return videos;
    }
    
    /**
     * retrieves all approved videos
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public LinkedList<Video> getVideosApproved() throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `video` WHERE `status`=1");
        ResultSet res=prep.executeQuery();
        LinkedList<Video> videos=new LinkedList<Video>();
        while(res.next())
        {
            Video video=new Video();
            video.setVideoId(res.getInt("videoId")).setUrl(res.getString("url"))
                    .setName(res.getString("name")).setTags(res.getString("tags"))
                    .setSection(getVideoSection(res.getInt("section"))).setDescription(res.getString("description"))
                    .setStatus(res.getInt("status")).setUploadType(res.getInt("uploaderType"));
            
            if(video.isUploadType())
            {
                video.setUploadByAdmin(this.authSQL.getAdmin(res.getInt("uploadBy")));
            }else
            {
                video.setUploadByUser(this.userSQL.getUserFull(res.getInt("uploadBy")));
            }
            videos.add(video);
        }
        return videos;
    }
    
    /**
     * retrieves the section
     * @param sectionId
     * @return
     * @throws SQLException 
     */
    public VideoSection getVideoSection(int sectionId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `video_section` WHERE `sectionId`=?");
        prep.setInt(1, sectionId);
        ResultSet res=prep.executeQuery();
        VideoSection section=new VideoSection();
        if(!res.first())
        {
            return section;
        }
        section.setName(res.getString("name")).setType(res.getInt("type")).setDescription(res.getString("description"))
                .setSectionId(sectionId).setStatus(res.getInt("status"));
        return section;
    }
       
    /**
     * retrieves all sections from data base
     * @return a linked list of VidoSection
     * @throws SQLException 
     */
    public LinkedList<VideoSection> getVideoSections() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `video_section` WHERE `status`!=-1");
        ResultSet res=prep.executeQuery();
        LinkedList<VideoSection> sections=new LinkedList<VideoSection>();
        while(res.next())
        {
            VideoSection section=new VideoSection();
            section.setName(res.getString("name")).setType(res.getInt("type"))
                    .setDescription(res.getString("description"))
                    .setSectionId(res.getInt("sectionId")).setStatus(res.getInt("status"));
            sections.add(section);
        }
        return sections;
    }
    
    /**
     * returns the configs of the sms panel
     * @return a SmsConfig Object
     * @throws SQLException 
     */
    public SmsConfig getSmsConfig() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `site_config` WHERE `name`='smsConfig' OR `name`='adminPhoneNumber'");
        ResultSet res=prep.executeQuery();
        SmsConfig smsConfig=new SmsConfig();
        
        while(res.next())
        {
            if(res.getString("name").equals("smsConfig"))
                smsConfig.setConfigs(res.getString("data"));         
            
            if(res.getString("name").equals("adminPhoneNumber"))
                smsConfig.setAdminNumber(res.getString("data"));
        }
        
        return smsConfig;
    }
    
    /**
     * sets smsConfig to data base to "smsConfig" name in site_data
     * @param configs
     * @throws SQLException 
     */
    public void setSmsConfig(LinkedList<String> configs) throws SQLException
    {
        String conf="";
        for(String config:configs)
        {
            conf+=config+";";
        }
        
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `site_config` SET `data`=? WHERE `name`='smsConfig'");
        prep.setString(1, conf);
        prep.execute();
    }
    
    /**
     * gets a video from data base
     * @param videoId
     * @return
     * @throws SQLException
     * @throws DoesntExistException 
     */
    public Video getVideo(int videoId) throws SQLException, DoesntExistException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `video` WHERE `videoId`=?");
        prep.setInt(1, videoId);
        ResultSet res=prep.executeQuery();
        Video video=new Video();
        if(!res.first())
        {
            return video;
        }
        video.setVideoId(res.getInt("videoId")).setUrl(res.getString("url"))
                    .setName(res.getString("name")).setTags(res.getString("tags"))
                    .setSection(getVideoSection(res.getInt("section"))).setDescription(res.getString("description"))
                    .setStatus(res.getInt("status")).setUploadType(res.getInt("uploaderType"));
            
            if(video.isUploadType())
            {
                video.setUploadByAdmin(this.authSQL.getAdmin(res.getInt("uploadBy")));
            }else
            {
                video.setUploadByUser(this.userSQL.getUserFull(res.getInt("uploadBy")));
            }
            
            return video;
    }
    
    /**
     * shows if the video id is used before or not 
     * @param videoId
     * @return
     * @throws SQLException 
     */
    public boolean videoIdExists(int videoId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `video` WHERE `videoId`=?");
        prep.setInt(1, videoId);
        return prep.executeQuery().first();
    }
    /**
     * adds a video to data base
     * @param url
     * @param name
     * @param description
     * @param tags
     * @param section
     * @param uploadBy
     * @param uploaderType 0:user/1:admin
     * @param status -1:deleted/0:pending/1:approved	
     * @return the id of the added video
     * @throws SQLException 
     */
    public int addVideo(String url,String name,String description,String tags,int section,int uploadBy,int uploaderType,int status) throws SQLException
    {
        int videoId;
        Random random=new Random();
        while(true)
        {
            videoId=Math.abs(random.nextInt());
            if(!videoIdExists(videoId))
                break;
        }
        
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `video` (`videoId`,`url`,`name`,`description`,`tags`,`section`,`uploadBy`,`uploaderType`,`status`) VALUES (?,?,?,?,?,?,?,?,?)");
        prep.setInt(1, videoId);
        prep.setString(2, url);
        prep.setString(3, name);
        prep.setString(4, description);
        prep.setString(5, tags);
        prep.setInt(6, section);
        prep.setInt(7, uploadBy);
        prep.setInt(8, uploaderType);
        prep.setInt(9, status);
        prep.execute();
        return videoId;
    }
    
    /**
     * shows if section id exists or not 
     * @param sectionId
     * @return true if yes 
     * @throws SQLException 
     */
    public boolean videoSectionIdExists(int sectionId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `id` FROM `video_section` WHERE `sectionId`=?");
        prep.setInt(1, sectionId);
        return prep.executeQuery().first();
        
    }
    
    /**
     * adds a section to data base 
     * @param name
     * @param description
     * @param type
     * @return the id of the added section
     * @throws SQLException 
     */
    public int addVideoSection(String name,String description,int type) throws SQLException
    {
        int sectionId;
        Random random=new Random();
        while(true)
        {
            sectionId=Math.abs(random.nextInt());
            if(!videoSectionIdExists(sectionId))
                break;
        }
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `video_section` (`sectionId`,`description`,`name`,`type`) VALUES (?,?,?,?)");
        prep.setInt(1, sectionId);
        prep.setString(2, description);
        prep.setString(3, name);
        prep.setInt(4, type);
        prep.execute();
        return sectionId;
    }
    
    /**
     * sets the video id of the main video to site_data in 'mainVideoId' row
     * @param videoId
     * @throws SQLException 
     */
    public void setMainVideo(int videoId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `site_data` SET `data`=? WHERE `name`='mainVideoId'");
        prep.setInt(1, videoId);
        prep.execute();
    }
    
    /**
     * edits a video based on it's id
     * @param videoId the id of the video
     * @param name
     * @param description
     * @param tags
     * @param seciton
     * @param status
     * @throws SQLException 
     */
    public void editVideo(int videoId,String name,String description,String tags,int seciton,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `video` SET `name`=?,`description`=?,`tags`=?,`section`=?,`status`=?  WHERE `videoId`=?");
        prep.setString(1, name);
        prep.setString(2, description);
        prep.setString(3, tags);
        prep.setInt(4, seciton);
        prep.setInt(5, status);
        prep.setInt(6, videoId);
        prep.execute();
        
    }
    
    /**
     * edits a video section
     * @param sectionId
     * @param name
     * @param description
     * @param type
     * @param status
     * @throws SQLException 
     */
    public void editVideoSection(int sectionId,String name,String description,int type,int status) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `video_section` SET `name`=?,`description`=?,`type`=?,`status`=? WHERE `sectionId`=?");
        prep.setString(1, name);
        prep.setString(2, description);
        prep.setInt(3, type);
        prep.setInt(4, status);
        prep.setInt(5, sectionId);
        prep.execute();
    }
        
    /**
     * deletes a video from data base
     * @param videoId
     * @throws SQLException 
     */
    public void deleteVideo(int videoId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM `video` WHERE `videoId`=?");
        prep.setInt(1, videoId);
        prep.execute();
    }

    /**
     * sets the text of contactus page
     * @param text
     * @throws SQLException 
     */
    public void setContactUs(String text) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `site_data` SET `data`=? WHERE `name`='contactUsText'");
        prep.setString(1, text);
        prep.execute();
    }
    
    /**
     * 
     * @return
     * @throws SQLException 
     */
    public String getContactUs() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT `data` FROM `site_data` WHERE `name`='contactUsText'");
        ResultSet res=prep.executeQuery();
        res.first();
        return res.getString("data");
    }
    
    public HashMap<String,String> getSiteInfo() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `site_data` ");
        ResultSet res=prep.executeQuery();
        HashMap<String,String> data=new HashMap<String,String>();
        while(res.next())
        {
            data.put(res.getString("name"), res.getString("data"));
        }
        return data;
    }
    
    /**
     * sets auto discount adding
     * @param count count of invite
     * @param effect effect of discount
     * @throws SQLException 
     */
    public void setAutoDiscountConfig(int count,double effect) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `site_config` SET `data`=? WHERE `name`='inviteDiscountCount'");
        prep.setInt(1, count);
        prep.execute();
        
        prep=this.connection.prepareStatement("UPDATE `site_config` SET `data`=? WHERE `name`='inviteDiscountEffect' ");
        prep.setDouble(1, effect);
        prep.execute();
        
        
    }
    
    /**
     * 
     * @return the data in a hashmap with keys 'inviteDiscountCount' and 'inviteDiscountEffect'
     * @throws SQLException 
     */
    public HashMap<String,String> getAutoDiscountConfig() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `site_config`");
        ResultSet res=prep.executeQuery();
        HashMap<String,String> data=new HashMap<String,String>();
        while(res.next())
        {
            if(res.getString("name").equals("inviteDiscountCount"))
            {
                data.put("inviteDiscountCount", res.getString("data"));
            }
            
            if(res.getString("name").equals("inviteDiscountEffect"))
            {
                data.put("inviteDiscountEffect", res.getString("data"));
            }
            
        }
        
        return data;
    }

    /**
     * adds a sms to data base
     * @param userId
     * @param text
     * @throws SQLException 
     */
    public void addSMS(int userId,String text) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `sms_sent` (`userId`,`text`) VALUES (?,?)");
        prep.setInt(1, userId);
        prep.setString(2, text);
        prep.execute();
    }
    
    /**
     * sets the admin phone number inside site_config under 'adminPhoneNumber' row
     * @param phoneNumber
     * @throws SQLException 
     */
    public void setAdminPhoneNumber(String phoneNumber) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `site_config` SET `data`=? WHERE `name`='adminPhoneNumber'");
        prep.setString(1, phoneNumber);
        prep.execute();
    }
    
    /**
     * changes the value of address and phone number in site_data
     * @param address
     * @param phoneNumber
     * @throws SQLException 
     */
    public void setSiteInfo(String address,String phoneNumber,String aboutUs) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `site_data` SET `data`=? WHERE `name`='address'");
        prep.setString(1, address);
        prep.execute();
        
        prep=this.connection.prepareStatement("UPDATE `site_data` SET `data`=? WHERE `name`='phoneNumber'");
        prep.setString(1, phoneNumber);
        prep.execute();
        
        prep=this.connection.prepareStatement("UPDATE `site_data` SET `data`=? WHERE `name`='aboutUs'");
        prep.setString(1, aboutUs);
        prep.execute();
    }
    
    /**
     * retrieves all slide shows
     * @return
     * @throws SQLException 
     */
    public LinkedList<SlideShow> getSlideShows() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `slideshow`");
        ResultSet res=prep.executeQuery();
        LinkedList<SlideShow> slideShows=new LinkedList<SlideShow>();
        while(res.next())
        {
            SlideShow slideshow=new SlideShow();
            slideshow.setId(res.getInt("id")).setLink(res.getString("link"))
                    .setPicture(res.getString("picture"))
                    .setText(res.getString("text"));
            slideShows.add(slideshow);
        }
        return slideShows;
    }
    
    /**
     * adds a slide show
     * @param link
     * @param text
     * @param picture
     * @throws SQLException 
     */
    public void addSlideShow(String link,String text,String picture) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `slideshow` (`link`,`text`,`picture`) VALUES (?,?,?)");
        prep.setString(1, link);
        prep.setString(2, text);
        prep.setString(3, picture);
        prep.execute();
    }
    
    /**
     * deletes a slideshow from data base completely
     * @param id
     * @throws SQLException 
     */
    public void deleteSlideShow(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM `slideshow` WHERE `id`=?");
        prep.setInt(1, id);
        prep.execute();
    }
    
    
    /**
     * retrieves all advertisments
     * @return
     * @throws SQLException 
     */
    public LinkedList<Advertisment> getAdvertisments() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `advertisment`");
        ResultSet res=prep.executeQuery();
        LinkedList<Advertisment> advertisments=new LinkedList<Advertisment>();
        while(res.next())
        {
            Advertisment advertisment=new Advertisment();
            advertisment.setId(res.getInt("id")).setLink(res.getString("link"))
                    .setPicture(res.getString("picture"))
                    .setText(res.getString("text"));
            advertisments.add(advertisment);
        }
        return advertisments;
    }
    
    /**
     * adds a sadvertisment
     * @param link
     * @param text
     * @param picture
     * @throws SQLException 
     */
    public void addAdvertisment(String link,String text,String picture) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `advertisment` (`link`,`text`,`picture`) VALUES (?,?,?)");
        prep.setString(1, link);
        prep.setString(2, text);
        prep.setString(3, picture);
        prep.execute();
    }
    
    /**
     * deletes a advertisment from data base completely
     * @param id
     * @throws SQLException 
     */
    public void deleteAdvertisment(int id) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("DELETE FROM `advertisment` WHERE `id`=?");
        prep.setInt(1, id);
        prep.execute();
    }
    
    /**
     * shows the count of video uploaded by a user to a section
     * @param userId
     * @param sectionId
     * @return
     * @throws SQLException 
     */
    public int uploadedBySection(int userId,int sectionId) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT COUNT(id) AS count FROM `video` WHERE uploadBy=? AND section=?");
        prep.setInt(1, userId);
        prep.setInt(2, sectionId);
        ResultSet res=prep.executeQuery();
        if(!res.first())
        {
            return 0;
        }
        return res.getInt("count");
    }
    
    /**
     * adds a count to view statistics
     * @throws SQLException 
     */
    public void addView() throws SQLException
    {
        Calendar today=Calendar.getInstance();
        String date=today.get(Calendar.YEAR)+"/"+today.get(Calendar.MONTH)+"/"+today.get(Calendar.DAY_OF_MONTH);
        PreparedStatement prep=this.connection.prepareStatement("INSERT INTO `viewcount` (`day`,`count`) VALUES (?,1) ON DUPLICATE KEY UPDATE `count`=count+1");
        prep.setString(1, date);
        prep.execute();
        
    }
    
    public HashMap<String,Integer> getViews() throws SQLException
    {
        Calendar today=Calendar.getInstance();
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `viewcount`");
        ResultSet res=prep.executeQuery();
        int day=0;
        int week=0;
        int month=0;
        while(res.next())
        {
            String dateVal=res.getString("day");
            String vals[]=dateVal.split("/"); 
            int dayVal=Integer.parseInt(vals[2]);
            int monthVal=Integer.parseInt(vals[1]);
            int yearVal=Integer.parseInt(vals[0]);
            
            Calendar date=Calendar.getInstance();
            date.set(yearVal, monthVal, dayVal);
//            System.out.println(date.getTimeInMillis());
//            System.out.println(System.currentTimeMillis());
            
            long compare=Math.abs(date.getTimeInMillis()-today.getTimeInMillis());
            if(compare<24*3600000-100)
            {
                day+=res.getInt("count");
                week+=res.getInt("count");
                month+=res.getInt("count");
            }else if(compare<24*3600000*7-100)
            {
                week+=res.getInt("count");
                month+=res.getInt("count");
            }else if(compare<24*3600000*7*30-100)
            {
                month+=res.getInt("count");
            }
            
        }
        
        HashMap<String,Integer> statistics=new HashMap<String,Integer>();
        statistics.put("day", day);
        statistics.put("week", week);
        statistics.put("month", month);
        
        return statistics;
        
    }
    
    /**
     * retrieves all link stats
     * @return
     * @throws SQLException 
     */
    public LinkedList<LinkStat> getLinkStats() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `links`");
        ResultSet res=prep.executeQuery();
        LinkedList<LinkStat> linkstats=new LinkedList<LinkStat>();
        while(res.next())
        {
            LinkStat linkstat=new LinkStat();
            linkstat.setId(res.getInt("id")).setLink(res.getString("link"))
                    .setText(res.getString("text")).setStatus(res.getInt("status"));
            linkstats.add(linkstat);
        }
        return linkstats;
    }
    
    /**
     * edits link activation and their texts
     * @param linkstats
     * @throws SQLException 
     */
    public void editLinkStats(LinkedList<LinkStat> linkstats) throws SQLException
    {
        for(LinkStat linkstat:linkstats)
        {
            PreparedStatement prep=this.connection.prepareStatement("UPDATE `links` SET `text`=? , `status`=? WHERE `link`=?");
            prep.setString(1, linkstat.getText());
            prep.setInt(2, linkstat.getStatus());
            prep.setString(3, linkstat.getLink());
            prep.execute();
        }
    }
    
    /**
     * shows if view statistics should be shown in public or not 
     * @return
     * @throws SQLException 
     */
    public boolean isViewsAvailable() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `site_config` WHERE `name`=?");
        prep.setString(1, "views");
        ResultSet res=prep.executeQuery();
        res.first();
        return res.getString("data").equals("true");
    }
    
    /**
     * shows if chat is active or not 
     * @return
     * @throws SQLException 
     */
    public boolean isChatAvailable() throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("SELECT * FROM `site_config` WHERE `name`=?");
        prep.setString(1, "chat");
        ResultSet res=prep.executeQuery();
        res.first();
        return res.getString("data").equals("true");
    }
    
    
    /**
     * changes view availability to true or false
     * @param available
     * @throws SQLException 
     */
    public void setViewsAvailable(boolean available) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `site_config` SET `data`=? WHERE `name`=?");
        prep.setString(1, (available ? "true":"false"));
        prep.setString(2, "views");
        prep.execute();
    }
    
    public void setChatsAvailable(boolean available) throws SQLException
    {
        PreparedStatement prep=this.connection.prepareStatement("UPDATE `site_config` SET `data`=? WHERE `name`=?");
        prep.setString(1, (available ? "true":"false"));
        prep.setString(2, "chat");
        prep.execute();
    }
}

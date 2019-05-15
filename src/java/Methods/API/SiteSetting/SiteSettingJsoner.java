/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.SiteSetting;

import ExceptionsChi.DoesntExistException;
import Objects.SiteSetting.Advertisment;
import Objects.SiteSetting.LinkStat;
import Objects.SiteSetting.SlideShow;
import Objects.Text.Text;
import SQL.Commands.SiteSettingSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class SiteSettingJsoner {
    private VideosJsoner videoJsoner=new VideosJsoner();
    private Connection connection;
    private SiteSettingSQLCommands siteSQL;
    
    public SiteSettingJsoner(Connection connection)
    {
        this.connection=connection;
        this.siteSQL=new SiteSettingSQLCommands(connection);
    }
    /**
     * gets a hashmap that includes the site data 
     * @param data
     * @param texts
     * @param slideShows
     * @param advertisments
     * @param linkstats
     * @param views
     * @return 
     * @throws java.sql.SQLException 
     * @throws ExceptionsChi.DoesntExistException 
     */
    public JSONObject getSiteDataJson(HashMap<String,String> data,LinkedList<Text> texts,LinkedList<SlideShow> slideShows,
            LinkedList<Advertisment> advertisments,LinkedList<LinkStat> linkstats,HashMap<String,Integer> views) 
            throws SQLException, DoesntExistException
    {
        JSONObject dataObject=new JSONObject();
        for(String key:data.keySet())
        {
            if(key.equals("mainVideoId"))
            {
                dataObject.put("mainVideo", this.videoJsoner.getVideoObject(this.siteSQL.getVideo(Integer.parseInt(data.get(key)))));
                continue;
            }
            dataObject.put(key, data.get(key));
        }
        dataObject.put("texts",getTextsArray(texts) );
        dataObject.put("slideShows",getSlideShowsArray(slideShows));
        dataObject.put("advertisments", getAdvertismentsArray(advertisments));
        dataObject.put("linkstats", getLinkStatsArray(linkstats));
        dataObject.put("views", getViewsObject(views));
        dataObject.put("date", Methods.Methods.getShamsiDate(new Timestamp(System.currentTimeMillis())));
        dataObject.put("chat", getChatData());
        return dataObject;
    }
    
    public JSONObject getChatData() throws SQLException
    {
        boolean chatActive=new SiteSettingSQLCommands(connection).isChatAvailable();
        JSONObject chatDataObject=new JSONObject();
        chatDataObject.put("active", chatActive);
        return chatDataObject;
    }
    
    public JSONObject getViewsObject(HashMap<String,Integer> views) throws SQLException
    {
        JSONObject viewsObject=new JSONObject();
        viewsObject.put("day", views.get("day"));
        viewsObject.put("month", views.get("month"));
        viewsObject.put("week", views.get("week"));
        viewsObject.put("show", (this.siteSQL.isViewsAvailable() ? true:false));
        return viewsObject;
    }
    
    public JSONArray getLinkStatsArray(LinkedList<LinkStat> linkstats)
    {
        JSONArray linkstatsArray=new JSONArray();
        int i=0;
        for(LinkStat linkstat:linkstats)
        {
            linkstatsArray.put(i,getLinkStatJson(linkstat));
            i++;
        }
        return linkstatsArray;
    }
    
    public JSONObject getLinkStatJson(LinkStat linkstat)
    {
        JSONObject linkstatObject=new JSONObject();
        linkstatObject.put("id", linkstat.getId());
        linkstatObject.put("link", linkstat.getLink());
        linkstatObject.put("text", linkstat.getText());
        linkstatObject.put("status", linkstat.getStatus());
        return linkstatObject;
    }
    
    public JSONArray getTextsArray(LinkedList<Text> texts)
    {
        JSONArray textsArray=new JSONArray();
        int i=0;
        for(Text text:texts)
        {
            if(text.getStatus()==1)
                continue;
            
            textsArray.put(i,getTextJson(text));
            i++;
        }
        return textsArray;
    }
    
    
    public JSONObject getTextJson(Text text)
    {
        JSONObject textObject=new JSONObject();
        textObject.put("textId", text.getTextId());
        textObject.put("subject", text.getSubject());
        textObject.put("link", text.getLink());
        textObject.put("text", text.getText());
        return textObject;
    }
    
    public JSONArray getSlideShowsArray(LinkedList<SlideShow> slideShows)
    {
        JSONArray slideShowArray=new JSONArray();
        int i=0;
        for(SlideShow slideShow:slideShows)
        {
            slideShowArray.put(i,getSlideShowJson(slideShow));
            i++;
        }
        return slideShowArray;
    }
    
    public JSONObject getSlideShowJson(SlideShow slideShow)
    {
        JSONObject slideShowObject=new JSONObject();
        slideShowObject.put("id", slideShow.getId());
        slideShowObject.put("link", slideShow.getLink());
        slideShowObject.put("text", slideShow.getText());
        slideShowObject.put("picture", "/MacaronBaData/SlideShows/"+slideShow.getPicture());
        return slideShowObject;
    }
    
    public JSONArray getAdvertismentsArray(LinkedList<Advertisment> advertisments)
    {
        JSONArray slideShowArray=new JSONArray();
        int i=0;
        for(Advertisment advertisment:advertisments)
        {
            slideShowArray.put(i,getAdvertismentJson(advertisment));
            i++;
        }
        return slideShowArray;
    }
    
    public JSONObject getAdvertismentJson(Advertisment advertisment)
    {
        JSONObject slideShowObject=new JSONObject();
        slideShowObject.put("id", advertisment.getId());
        slideShowObject.put("link", advertisment.getLink());
        slideShowObject.put("text", advertisment.getText());
        slideShowObject.put("picture", "/MacaronBaData/Advertisments/"+advertisment.getPicture());
        return slideShowObject;
    }
}

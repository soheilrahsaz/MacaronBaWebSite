/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods.API.SiteSetting;

import Objects.SiteSetting.Video;
import Objects.SiteSetting.VideoSection;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Moses
 */
public class VideosJsoner {
    private final String URL_VIDEO="/MacaronBaData/Videos/";
    
    /**
     * gets a linked list of video sections and turns them to a json array
     * @param sections
     * @return 
     */
    public JSONArray getVideoSectionsArray(LinkedList<VideoSection> sections)
    {
        JSONArray videosArray=new JSONArray();
        int i=0;
        for(VideoSection section:sections)
        {
            videosArray.put(i,getVideoSection(section));
            i++;
        }
        return videosArray;
    }
        
    
    
    /**
     * gets a linked list of videos and turns them to json
     * @param videos
     * @return 
     */
    public JSONArray getVideosArray(LinkedList<Video> videos)
    {
        JSONArray videosArray=new JSONArray();
        int i=0;
        for(Video video:videos)
        {
            videosArray.put(i,getVideoObject(video));
            i++;
        }
        return videosArray;
            
    }
    
    /**
     * gets a video and turns it to json object
     * @param video
     * @return 
     */
    public JSONObject getVideoObject(Video video)
    {
        JSONObject videoObject=new JSONObject();
        
        if(video.getVideoId()==0)
            return videoObject;
        
        videoObject.put("videoId", video.getVideoId());
        videoObject.put("name", video.getName());
        videoObject.put("description", video.getDescription());
        videoObject.put("url",URL_VIDEO+ video.getUrl());
        videoObject.put("tags",video.getTags());
        videoObject.put("section",getVideoSection(video.getSection()));
        
        if(video.getVideoId()==0)
            return videoObject;
        
        if(video.isUploadType())
        {
            videoObject.put("uploaderName", video.getUploadByAdmin().getUserName());
        }else
        {
            videoObject.put("uploaderName", video.getUploadByUser().getFirstName()+" "+video.getUploadByUser().getLastName());
        }
        return videoObject;
        
    }
    
    /**
     * gets a video section and turns it to json
     * @param section
     * @return 
     */
    public JSONObject getVideoSection(VideoSection section)
    {
        JSONObject sectionObject=new JSONObject();
        sectionObject.put("sectionId", section.getSectionId());
        sectionObject.put("type", section.getType());
        sectionObject.put("name", section.getName());
        sectionObject.put("description", section.getDescription());
        sectionObject.put("status", section.getStatus());
        return sectionObject;
    }
}

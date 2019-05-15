/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.SiteSetting;

import Objects.Admin.Admin;
import Objects.User.User;

/**
 *
 * @author Moses
 */
public class Video {
    private int videoId;
    private String url;
    private String name;
    private String description;
    private String tags;
    private VideoSection section;
    private User uploadByUser;
    private Admin uploadByAdmin;
    private boolean uploadType;
    private int status;

    public String getUrl() {
        return url;
    }

    public Video setUrl(String url) {
        this.url = url;
        return this;
    }

    
    
    public int getVideoId() {
        return videoId;
    }

    public Video setVideoId(int videoId) {
        this.videoId = videoId;
       
    return this;}

    public String getName() {
        return name;
    }

    public Video setName(String name) {
        this.name = name;
    return this;}

    public String getDescription() {
        return description;
    }

    public Video setDescription(String description) {
        this.description = description;
    return this;}

    public String getTags() {
        return tags;
    }

    public Video setTags(String tags) {
        this.tags = tags;
    return this;}

    public VideoSection getSection() {
        return section;
    }

    public Video setSection(VideoSection section) {
        this.section = section;
    return this;}

    public User getUploadByUser() {
        return uploadByUser;
    }

    public Video setUploadByUser(User uploadByUser) {
        this.uploadByUser = uploadByUser;
    return this;}

    public Admin getUploadByAdmin() {
        return uploadByAdmin;
    }

    public Video setUploadByAdmin(Admin uploadByAdmin) {
        this.uploadByAdmin = uploadByAdmin;
    return this;}

    /**
     * true for admin and false for user
     * @return 
     */
    public boolean isUploadType() {
        return uploadType;
    }

    public Video setUploadType(int uploadType) {
        this.uploadType = uploadType==1;
    return this;}

    public int getStatus() {
        return status;
    }

    public Video setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.SiteSetting;

/**
 *
 * @author Moses
 */
public class VideoSection {
    private int sectionId;
    private String name;
    private String description;
    private int status;
    private int type;

    public int getType() {
        return type;
    }

    public VideoSection setType(int type) {
        this.type = type;
        return this;
    }
    
    

    public String getDescription() {
        return description;
    }

    public VideoSection setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public int getSectionId() {
        return sectionId;
    }

    public VideoSection setSectionId(int sectionId) {
        this.sectionId = sectionId;
        return this;
    }

    public String getName() {
        return name;
    }

    public VideoSection setName(String name) {
        this.name = name;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public VideoSection setStatus(int status) {
        this.status = status;
        return this;
    }
    
    
}

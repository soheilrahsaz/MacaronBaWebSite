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
public class LinkStat {
    private int id;
    private String link;
    private String text;
    private int status;

    public int getId() {
        return id;
    }

    public LinkStat setId(int id) {
        this.id = id;
        
    return this;}

    public String getLink() {
        return link;
    }

    public LinkStat setLink(String link) {
        this.link = link;
    return this;}

    public String getText() {
        return text;
    }

    public LinkStat setText(String text) {
        this.text = text;
    return this;}

    public int getStatus() {
        return status;
    }

    public LinkStat setStatus(int status) {
        this.status = status;
    return this;}
    
    
}

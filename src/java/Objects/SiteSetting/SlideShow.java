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
public class SlideShow {
    private int id;
    private String text;
    private String link;
    private String picture;

    public int getId() {
        return id;
    }

    public SlideShow setId(int id) {
        this.id = id;
    return this;}

    public String getText() {
        return text;
    }

    public SlideShow setText(String text) {
        this.text = text;
    return this;}

    public String getLink() {
        return link;
    }

    public SlideShow setLink(String link) {
        this.link = link;
    return this;}

    public String getPicture() {
        return picture;
    }

    public SlideShow setPicture(String picture) {
        this.picture = picture;
    return this;}
    
    
}

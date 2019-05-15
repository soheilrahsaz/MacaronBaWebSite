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
public class Advertisment {
    private int id;
    private String link;
    private String text;
    private String picture;

    public int getId() {
        return id;
    }

    public Advertisment setId(int id) {
        this.id = id;
    return this;}

    public String getLink() {
        return link;
    }

    public Advertisment setLink(String link) {
        this.link = link;
    return this;}

    public String getText() {
        return text;
    }

    public Advertisment setText(String text) {
        this.text = text;
    return this;}

    public String getPicture() {
        return picture;
    }

    public Advertisment setPicture(String picture) {
        this.picture = picture;
    return this;}
    
    
}

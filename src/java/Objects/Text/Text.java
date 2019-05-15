/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Text;

/**
 *
 * @author Moses
 */
public class Text {
    private int textId;
    private String subject;
    private String link;
    private String text;
    private int status;

    public int getTextId() {
        return textId;
    }

    public Text setTextId(int textId) {
        this.textId = textId;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Text setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Text setLink(String link) {
        this.link = link;
        return this;
    }

    public String getText() {
        return text;
    }

    public Text setText(String text) {
        this.text = text;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Text setStatus(int status) {
        this.status = status;
        return this;
    }
    
    
}

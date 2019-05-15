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
public class SiteInfo {
    private SmsConfig smsConfig;
    private String contactUs;
    private String address;
    private String phoneNumber;
    private Video mainVideo;
    private String mainPageDownPic;

    public SmsConfig getSmsConfig() {
        return smsConfig;
    }

    public SiteInfo setSmsConfig(SmsConfig smsConfig) {
        this.smsConfig = smsConfig;
        return this;
    }

    public String getContactUs() {
        return contactUs;
    }

    public SiteInfo setContactUs(String contactUs) {
        this.contactUs = contactUs;
    return this;}

    public String getAddress() {
        return address;
    }

    public SiteInfo setAddress(String address) {
        this.address = address;
    return this;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public SiteInfo setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    return this;}

    public Video getMainVideo() {
        return mainVideo;
    }

    public SiteInfo setMainVideo(Video mainVideo) {
        this.mainVideo = mainVideo;
    return this;}

    public String getMainPageDownPic() {
        return mainPageDownPic;
    }

    public SiteInfo setMainPageDownPic(String mainPageDownPic) {
        this.mainPageDownPic = mainPageDownPic;
    return this;}
    
    
}

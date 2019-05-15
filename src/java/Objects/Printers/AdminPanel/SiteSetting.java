/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Printers.AdminPanel;

import ExceptionsChi.DoesntExistException;
import Objects.Panel.Buttons;
import Objects.Panel.Labels;
import Objects.SiteSetting.Advertisment;
import Objects.SiteSetting.LinkStat;
import Objects.SiteSetting.SlideShow;
import Objects.SiteSetting.Video;
import Objects.SiteSetting.VideoSection;
import SQL.Commands.SiteSettingSQLCommands;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Moses
 */
public class SiteSetting { 
    private SiteSettingSQLCommands siteSQL;
    
    public SiteSetting(Connection connection)
    {
        this.siteSQL=new SiteSettingSQLCommands(connection);
    }
    
    public String getViedosRows() throws SQLException, DoesntExistException
    {
        LinkedList<Video> videos=this.siteSQL.getVideos();
        String rows="";
        for(Video video:videos)
        {
            rows+="<tr onclick=\"window.location='Videos/"+video.getVideoId()+"'\" style=\"cursor:pointer;\">"
                    + "<td>"+video.getName()+"</td>"
                    + "<td>"+video.getSection().getName()+"</td>"
                    + "<td>"+(video.isUploadType() ? "مدیر "+video.getUploadByAdmin().getUserName():"کاربر "+video.getUploadByUser().getFirstName()+" "+video.getUploadByUser().getLastName())+"</td>"
                    + "<td>"+getVideoStatus(video.getStatus())+"</td>"
                    + "</tr>";
        }
        return rows;
    }
    /**
     * -1:deleted/0:pending/1:approved	
     * @param status
     * @return 
     */
    private String getVideoStatus(int status)
    {
        Labels labels=new Labels();
        switch(status)
        {
            case -1:return labels.getDanger("حذف شده");
            case 0:return labels.getWarning("منتظر تایید");
            case 1:return labels.getSuccess("تایید شده");
            default:return "";
        }
            
    }
    
    private String getVideoStatusOptions(int status)
    {
        String options="";
        
        options+="<option value=\"0\" "+(status==0 ? "selected":"")+" >منتظر تایید</option>"
                +"<option value=\"1\" "+(status==1 ? "selected":"")+" >تایید شده</option>";
        
        return options;
    }
        
    
    public String getSectionRows() throws SQLException
    {
        LinkedList<VideoSection> sections=this.siteSQL.getVideoSections();
        String rows="";
        for(VideoSection section:sections)
        {
            rows+="<tr onclick=\"window.location='Sections/"+section.getSectionId()+"'\" style=\"cursor:pointer;\" >"
                    + "<td>"+section.getName()+"</td>"
                    + "<td>"+section.getDescription()+"</td>"
                    + "<td>"+getSectionType(section.getType())+"</td>"
                    + "<td>"+getSectionStatus(section.getStatus())+"</td>"
                    + "</tr>";
        }
            return rows;
    }
    
    private String getSectionType(int type)
    {
        switch(type)
        {
            case 0:return"کاربران";
            case 1:return"مدیران";
            case 2:return"مدیران و کاربران";
            default:return "";
        }
    }
    
    private String getSectionStatus(int status)
    {
        Labels labels=new Labels();
        switch(status)
        {
            case -1:return labels.getDanger("حذف شده");
            case 0:return labels.getWarning("غیرفعال");
            case 1:return labels.getSuccess("فعال");
            default:return "";
        }
    }
    
    private String getSectionStatusOptions(int status)
    {
        String options="";
        
        options+="<option value=\"0\" "+(status==0 ? "selected":"")+" >غیرفعال</option>"
                +"<option value=\"1\" "+(status==1 ? "selected":"")+" >فعال</option>";
        
        return options;
    }
    
    public String getSectionOptions() throws SQLException
    {
        LinkedList<VideoSection> sections=this.siteSQL.getVideoSections();
        String options="";
        for(VideoSection section:sections)
        {
            options+="<option "
                    + "value=\""+section.getSectionId()+"\""
                    + ">"
                    + section.getName()
                    + "</option>";
        }
        
        return options;
    }
    
    public String getSectionOptions(int sectionId) throws SQLException
    {
        LinkedList<VideoSection> sections=this.siteSQL.getVideoSections();
        String options="";
        for(VideoSection section:sections)
        {
            options+="<option "
                    + "value=\""+section.getSectionId()+"\""
                    + (sectionId==section.getSectionId() ? "selected":"")
                    + ">"
                    + section.getName()
                    + "</option>";
        }
        
        return options;
    }
    
    public String showSection(String uri) throws SQLException
    {
        uri=uri.substring(uri.indexOf("Sections")+9);
        int sectionId=Integer.parseInt(uri);
        VideoSection section=this.siteSQL.getVideoSection(sectionId);
        String show="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">اطلاعات بخش</h3>\n" +
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditSection\">\n"
                + "<input type=\"hidden\" name=\"sectionId\" value=\""+section.getSectionId()+"\" > " +
"              <div class=\"box-body\">\n" +
"                  \n"
                
                + "<div class=\"form-group\">\n" +
"                      <label>نوع</label>\n" +
"                      <select class=\"form-control\" name=\"type\">\n" +
"                          <option value=\"0\" "+(section.getType()==0 ? "selected":"")+" >کاربران</option>\n" +
"                          <option value=\"1\" "+(section.getType()==1 ? "selected":"")+" >مدیران</option>\n" +
"                          <option value=\"2\" "+(section.getType()==2 ? "selected":"")+" >کاربران و مدیران</option>\n" +
"                      </select>\n" +
"                  </div>" +
                
"                <div class=\"form-group\">\n" +
"                  <label >نام بخش</label>\n" +
"                  <input type=\"name\" name=\"name\" class=\"form-control\" value=\""+section.getName()+"\"  placeholder=\"نام بخش\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >توضیحات</label>\n" +
"                  <textarea name=\"description\" class=\"form-control\" rows=\"3\" placeholder=\"توضیحات\">"+section.getDescription()+"</textarea>\n" +
"                </div>\n" +
"                  \n" +
                                 "<div class=\"form-group\">\n" +
"                      <label>وضعیت</label>\n" +
"                      <select class=\"form-control\" name=\"status\">\n" +
                        getSectionStatusOptions(section.getStatus())+
"                      </select>\n" +
"                  </div>" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +

"              <div class=\"box-footer\">\n" +
"                <button type=\"submit\" class=\"btn btn-warning\">اعمال تغییر</button>\n" +
"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        return show;
    }
    
    public String showVideo(String uri) throws SQLException, DoesntExistException
    {
        uri=uri.substring(uri.indexOf("Videos")+7);
        int videoId=Integer.parseInt(uri);
        Video video=this.siteSQL.getVideo(videoId);
        
        String show="<section class=\"content\">\n" +
"      <div class=\"row\">\n" +
"        <!-- left column -->\n" +
"        <div class=\"col-md-12\">\n" +
"          <!-- general form elements -->\n" +
"          <div class=\"box box-primary\">\n" +
"            <div class=\"box-header with-border\">\n" +
"              <h3 class=\"box-title\">اپلود فیلم</h3>\n" +
                
                "<div class=\"box-tools\">\n" +
"                <div class=\"input-group input-group-sm\" >\n"
                + "<form method=\"post\" action=\"../DeleteVideo\" >"
                + "<input type=\"hidden\" name=\"videoId\" value=\""+videoId+"\" >"
                +new Buttons().btn_Normal_Danger("حذف ویدیو", "submit", "sm", false, false)
                + "</form>" +
"                </div>\n" +
"              </div>\n"+
                
"            </div>\n" +
"            <!-- /.box-header -->\n" +
"            <!-- form start -->\n" +
"            <form role=\"form\" method=\"post\" action=\"../EditVideo\" >\n"
                + "<input type=\"hidden\" name=\"videoId\" value=\""+video.getVideoId()+"\">" +
"              <div class=\"box-body\">\n" +
"                 \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >بخش </label>\n" +
"                  <select class=\"form-control\" name=\"sectionId\">\n" +
                        getSectionOptions(video.getSection().getSectionId())+
"                  </select>\n" +
"                </div>\n" +
"                  \n" +
"                <div class=\"form-group\">\n" +
"                  <label >نام فیلم</label>\n" +
"                  <input type=\"text\" name=\"name\" class=\"form-control\" value=\""+video.getName()+"\" placeholder=\"نام فیلم\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >توضیحات فیلم</label>\n" +
"                  <textarea name=\"description\" class=\"form-control\" rows=\"3\" placeholder=\"توضیحات\">"+video.getDescription()+"</textarea>\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label >تگ های فیلم</label>\n" +
"                  <input type=\"text\" name=\"tags\" class=\"form-control\" value=\""+video.getTags()+"\"  placeholder=\" تگ فیلم\">\n" +
"                </div>\n" +
"                  \n" +
"                  <div class=\"form-group\">\n" +
"                  <label ></label>\n"+
                 "<video width=\"320\" height=\"240\" controls>"
                + "<source src=\"/MacaronBaData/Videos/"+video.getUrl()+"\">"
                + "</video>" +
                
"                </div>\n" +
                  "<div class=\"form-group\">\n" +
"                      <label>وضعیت</label>\n" +
"                      <select class=\"form-control\" name=\"status\">\n" +
                        getVideoStatusOptions(video.getStatus())+
"                      </select>\n" +
"                  </div>" +
"                  \n" +
"              </div>\n" +
"              <!-- /.box-body -->\n" +
"\n" +
"              <div class=\"box-footer\">\n" +
"                  <button type=\"submit\" class=\"btn btn-warning\">اعمال تغییر</button>\n" +
"              </div>\n" +
"            </form>\n" +
"          </div>\n" +
"          <!-- /.box -->\n" +
"        </div>\n" +
"      </div>\n" +
"          </section>";
        
        
        return show;
    }
    
    public String getContactUs() throws SQLException
    {
        return this.siteSQL.getContactUs();
    }
        
    public String getSlideShowRows() throws SQLException
    {
        Buttons buttons=new Buttons();
        String rows="";
        LinkedList<SlideShow> slideShows=this.siteSQL.getSlideShows();
        for(SlideShow slideShow:slideShows)
        {
            rows+="<tr>"
                    + "<td><img style=\"width:15%;\" src=\"/MacaronBaData/SlideShows/"+slideShow.getPicture()+"\"</td>"
                    + "<td>"+Methods.Methods.getValOrDash(slideShow.getLink())+"</td>"
                    + "<td style=\"width:40%\">"+Methods.Methods.getValOrDash(slideShow.getText())+"</td>"
                    + "<td>"
                    + "<form method=\"post\" action=\"DeleteSlideShow\">"
                    + "<input type=\"hidden\" name=\"id\" value=\""+slideShow.getId()+"\" >"
                    + buttons.btn_Normal_Danger("حذف", "submit", "", false, false)
                    + "</form>"
                    + "</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    public String getAdvertismentRows() throws SQLException
    {
        Buttons buttons=new Buttons();
        String rows="";
        LinkedList<Advertisment> advertisments=this.siteSQL.getAdvertisments();
        for(Advertisment advertisment:advertisments)
        {
            rows+="<tr>"
                    + "<td><img style=\"width:15%;\" src=\"/MacaronBaData/Advertisments/"+advertisment.getPicture()+"\"</td>"
                    + "<td>"+Methods.Methods.getValOrDash(advertisment.getLink())+"</td>"
                    + "<td style=\"width:40%\">"+Methods.Methods.getValOrDash(advertisment.getText())+"</td>"
                    + "<td>"
                    + "<form method=\"post\" action=\"DeleteAdvertisment\">"
                    + "<input type=\"hidden\" name=\"id\" value=\""+advertisment.getId()+"\" >"
                    + buttons.btn_Normal_Danger("حذف", "submit", "", false, false)
                    + "</form>"
                    + "</td>"
                    + "</tr>";
        }
        return rows;
    }
    
    public String getLinksStatus() throws SQLException
    {
        boolean views=this.siteSQL.isViewsAvailable();
        boolean chat=this.siteSQL.isChatAvailable();
        
        LinkedList<LinkStat> linkstats=this.siteSQL.getLinkStats();
        String data="<div class=\"box-body\"><div class=\"form-group\">";
        for(LinkStat linkstat:linkstats)
        {
            data+="<div class=\"col-lg-12\">\n" +
"                  <div class=\"input-group\">\n" +
"                        <span class=\"input-group-addon\">\n" +
"                          <input type=\"checkbox\" name=\""+linkstat.getLink()+"box\" "
                    + (linkstat.getStatus()==0 ? "checked":"")
                    + " >\n" +
"                        </span>\n" +
"                    <input type=\"text\" name=\""+linkstat.getLink()+"\" class=\"form-control\" value=\""+linkstat.getText()+"\">\n" +
"                  </div>\n" +
"                  <!-- /input-group -->\n" +
"                </div>";
        }
        data+="</div></div>";
        
        data+="<div class=\"box-footer\"><div class=\"form-group\">"
                + "<div class=\"checkbox\">\n" +
"                    <label>\n" +
"                      <input type=\"checkbox\" name=\"views\" "
                + (views ? "checked":"")
                + ">\n" +
                         "آمار بازدید"+
"                    </label>\n" +
"                  </div>"
                
                +"<div class=\"form-group\">"
                + "<div class=\"checkbox\">\n" +
"                    <label>\n" +
"                      <input type=\"checkbox\" name=\"chat\" "
                + (chat ? "checked":"")
                + ">\n" +
                         "چت پشتیبانی"+
"                    </label>\n" +
"                  </div>"
                
               + "</div>"
                
                + "</div>";
        return data;
    }
}

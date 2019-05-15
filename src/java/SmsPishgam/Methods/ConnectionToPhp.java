package SmsPishgam.Methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Megnatis - FarshiD Naqizadeh
 */
public class ConnectionToPhp {
    private URL url;
    private String dataToSend;
    private HttpURLConnection http;
    private String getFromServer;
    private String[] charsetStandard = new String[]{"US-ASCII","ISO-8859-1","UTF-8","UTF-16BE","UTF-16LE","UTF-16"};
    private String[] method = new String[]{"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE"};
    private String selectedMethod = "";
    private String charset;
    /**
     * 
     * @param url آدرس ساخته شده برای تبادل اطلاعات با آن
     * @return یو آر برای تنظیم ارتباط
     */
    public ConnectionToPhp setUrl(URL url){
    this.url = url;
    return this;
    }
    /**
     * 
     * @param url رشته اطلاعاتی برای تبادل اطلاعات
     * @return یو آر برای تنظیم ارتباط
     * @throws MalformedURLException  ممکنه خطای تنظیم آدرس دهی بندازد!
     */
    public ConnectionToPhp setUrl(String url) throws MalformedURLException{
    this.url = new URL(url);
    return this;
    }
    /**
     * @param method  0="GET", 1="POST", 2="HEAD", 3="OPTIONS", 4="PUT", 5="DELETE", 6="TRACE"
     * انتخاب یکی از آبشن های بالا قابل قبول می باشد
     * تنظیم تنظیمات ارجاع شده به کانکتشن
     * @return تنظیمات اتصال به آدرس اعمال شد!
     * @throws IOException 
     */
    public ConnectionToPhp setHttpConnectionSettings(int method) throws IOException{
        
        selectedMethod = this.method[method];

    return this;
    }
    /**
     * 
     * @param hashDataToSend اطلاعات تبدیل شده به هش مپ
     * @param charset   فرمت رمزگذاری اطلاعات برای ارسال به دیتابیس 
     * 0="US-ASCII",1="ISO-8859-1",2="UTF-8",3="UTF-16BE",4="UTF-16LE",5="UTF-16"
     * @return  تنظیم اطلاعات برای ارسال
     * @throws UnsupportedEncodingException 
     */
    public ConnectionToPhp setDataToSend(HashMap<String , String> hashDataToSend , int charset) throws UnsupportedEncodingException{
    String dataForFormatToURL = (selectedMethod.equalsIgnoreCase(this.method[0])) ? "?" : ""; //افزودن علامت سوال به پارامتر در صورت وجود متد گت
    this.charset = charsetStandard[charset];
    for(String key : hashDataToSend.keySet()){
    dataForFormatToURL = dataForFormatToURL + key + "=" + URLEncoder.encode(hashDataToSend.get(key),this.charset)+"&";
    }
//    dataForFormatToURL = dataForFormatToURL+"\b";//پاک کردن اتصال دهنده بعد از حلقه
        dataForFormatToURL=dataForFormatToURL.substring(0,dataForFormatToURL.length()-1);
    dataToSend = dataForFormatToURL;
    return this;
    }
    /**
     * 
     * @return ارسال اطلاعات به سرور
     * خواندن اطلاعات سرور
     * @throws IOException امکان پرتاب استثنا
     */
    public ConnectionToPhp excute() throws IOException{
        if(this.selectedMethod.equalsIgnoreCase("GET"))
        {
            this.url = new URL(this.url.toString()+dataToSend);
        }
        
        URLConnection con = this.url.openConnection();
        this.http = (HttpURLConnection)con;
        this.http.setRequestMethod(selectedMethod);
//        this.http.setDoOutput(true);
        
//    byte[] out = this.dataToSend.getBytes(this.charset);
//    int length = out.length;
//    this.http.setFixedLengthStreamingMode(length);
//    this.http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    this.http.connect();
//    OutputStream os = http.getOutputStream();
//    os.write(out);

//        Map<String,List<String>> headers=http.getHeaderFields();
//        for(Map.Entry<String,List<String>> entry:headers.entrySet())
//        {
//            System.out.print(entry.getKey());
//            for(String str:entry.getValue())
//            {
//                System.out.print(str+",");
//            }
//            System.out.println();
//        }
//        
//    System.out.println(dataToSend);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.http.getInputStream()))) {//این نوع تلاش امکان بستن فایل را به شما به صورت اتوماتیک میدهد
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            this.getFromServer = response.toString();
        }
    return this;
    }
    /**
     * 
     * @return  برمیگرداند اطلاعات خوانده شده از صفحه مد نظر را
     */
    public String get(){
    return getFromServer;
    }
}

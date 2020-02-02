package prayer_times;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.net.URLEncoder;

public class SalahTimeScraper {
    public static void main(String[] args) {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String searchUrl = "www.salah.com";
            HtmlPage page = client.getPage(searchUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package offres_emploi;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WebScraper {
    private Morphia morphia;
    private Datastore datastore;

    public WebScraper() {
        morphia = new Morphia();
        datastore = morphia.createDatastore(new MongoClient(), "find_a_job");
    }

    public void pageScraper(int pageNumber) {

        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("offres_emploi");

        // create the Datastore connecting to the default port on the local host
        datastore.ensureIndexes();
        String searchQuery = String.valueOf(pageNumber);
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String url = "https://www.emploisenegal.com/recherche-jobs-senegal";
            url = pageNumber == 0 ? url : url + "?page=" + URLEncoder.encode(searchQuery, "UTF-8");
            System.out.println(url);
            HtmlPage page = client.getPage(url);

            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='job-description-wrapper']");
            if (items.isEmpty()) {
                System.out.println("No items found !");
            } else {
                for (HtmlElement htmlItem : items) {
                    String jobUrl = htmlItem.getAttribute("data-href");
                    String[] jobUrlTab = jobUrl.split("-");
                    String jobReference = jobUrlTab[jobUrlTab.length - 1];
                    HtmlParagraph itemRecruiter = htmlItem.getFirstByXPath(".//p[@class='job-recruiter']");

                    HtmlElement itemDescription = htmlItem.getFirstByXPath(".//div[@class='search-description']");
                    HtmlElement itemRegion = htmlItem.getFirstByXPath(".//div[@class='col-lg-5 col-md-5 col-sm-7 col-xs-12 job-title']/p[not(@class)]");
                    HtmlElement itemJob = htmlItem.getFirstByXPath(".//div[@class='col-lg-5 col-md-5 col-sm-7 col-xs-12 job-title']/h5");

                    String recruiter = itemRecruiter.asText().split("\\|")[1];
                    String stringDate = itemRecruiter.asText().split("\\|")[0];

                    //COnvert date from String to Date
                    Date date = getDate(stringDate);
                    String description = itemDescription.asText();
                    String title = itemJob.asText();
                    String location = itemRegion.asText().split(" :")[1];

                    final Job job = new Job(title, description, location, recruiter, jobUrl, jobReference, date);
                    try {
                        datastore.save(job);
                    } catch (DuplicateKeyException e) {
                        e.printStackTrace();
                    }
                }
            }
            List<HtmlElement> next = (List<HtmlElement>) page.getByXPath("//li[@class='pager-item active']");
            pageNumber++;
            if (pageNumber < next.size())
                pageScraper(pageNumber);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Date getDate(String stringDate) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}

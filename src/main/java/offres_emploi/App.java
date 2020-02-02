package offres_emploi;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        WebScraper webScraper = new WebScraper();
        webScraper.pageScraper(0);
    }
}

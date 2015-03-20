import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class HBOMovies {
	public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = args[0];
        System.out.println("Fetching %s..." + url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        Elements data_d = doc.select("data-d");
        Elements data_t = doc.select("data-t");

        int counter = 1;
        java.util.List<HBODigitalContent> movies = new java.util.ArrayList<HBODigitalContent>();
        for (Element link : links) {
            if(counter < 100) {
              try {
                Date date = new Date(Long.parseLong(link.attr("data-d")));
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);
                String url_string = "http://stats.grok.se/json/en/" + year + month + "/" + link.attr("data-t");
                try {
                  URI uri = new URI(url_string.replace(" ", "%20"));
                  try{
                    Document doc2 = Jsoup.connect(uri.toString()).get();
                    Elements body = doc2.select("body");
                    String bodyString = body.toString();
                    int curlyIndex = bodyString.indexOf("{");
                    bodyString = bodyString.substring(curlyIndex, bodyString.length());
                    int lessthanIndex = bodyString.indexOf("<");
                    bodyString = bodyString.substring(0, lessthanIndex);
                    HBODigitalContent content = new HBODigitalContent();
                    movies.add(content);
                    int totalViews = content.getTotalViewsPerMonth(bodyString);  
                  }
                  catch(java.net.SocketTimeoutException ste){
                    //ste.printStackTrace();	
                  }
                }
                catch(URISyntaxException se){
                  se.printStackTrace();
                }
                counter ++;
              }
              catch (NumberFormatException nfe) {
              	nfe.printStackTrace();
              	System.out.println("TITLE: " + link.attr("data-t"));
              }
            }
        }
        java.util.Collections.sort(movies);
        java.util.List sortedMovies = new java.util.ArrayList();
        movies.forEach(movie ->
        {
        	JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", movie.getTitle());
            jsonObject.put("monthly_views", movie.getTotalMonthlyViews());
            sortedMovies.add(jsonObject);
        }
        );
        System.out.println("Sorted: " + sortedMovies);
    }
}
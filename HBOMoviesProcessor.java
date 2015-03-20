import java.lang.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.lang.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.Calendar;
import java.net.URI;
import java.net.URISyntaxException;

public class HBOMoviesProcessor implements MoviesProcessor {
  private Document document;
  private Elements movieLinks;
  private int counter = 0;
  private java.util.List<HBODigitalContent> movies = new java.util.ArrayList<HBODigitalContent>();

  public void setDocument(Document doc){
  	document = doc;
  }
  
  public java.util.List processMovies(){
  	movieLinks = document.select("a[href]");
    for (Element link : movieLinks) {
      if(counter < 50) {
        try {
          Date date = new Date(Long.parseLong(link.attr("data-d")));
          Calendar cal = Calendar.getInstance();
          cal.setTime(date);
          int month = cal.get(Calendar.MONTH) + 1;
          int year = cal.get(Calendar.YEAR);
          String url_string = "http://stats.grok.se/json/en/" + year + month + "/" + link.attr("data-t");
          URI uri = new URI(url_string.replace(" ", "%20"));
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
          counter ++;
        }
        catch (NumberFormatException nfe) {
          nfe.printStackTrace();
        }
        catch(java.net.SocketTimeoutException ste){
          ste.printStackTrace();	
        }
        catch(java.io.IOException ioe){
          ioe.printStackTrace();
        }
        catch(URISyntaxException se){
          se.printStackTrace();
        }
      }
    }
    return movies;
  }

  public java.util.List sortMovies(java.util.List unsortedMovies){
    java.util.Collections.sort(movies);
    java.util.List sortedMovies = new java.util.ArrayList();
    movies.forEach(movie ->
      {
        org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
        jsonObject.put("title", movie.getTitle());
        jsonObject.put("monthly_views", movie.getTotalMonthlyViews());
        sortedMovies.add(jsonObject);
      }
    );
    return sortedMovies;
  }
}
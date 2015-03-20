package movies;

import java.lang.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.Calendar;
import java.net.URI;
import java.net.URISyntaxException;

/**
* <h1>HBO Movies Processor</h1>
* Service used to process a list of movies producing an ordered list based on the total number of monthly viewings per movies
*
* @author  Alex Bunardzic
* @version 1.0
* @since   2015-03-20
*/
public class HBOMoviesProcessor implements MoviesProcessor {
  private Document document;
  private Elements movieLinks;
  private URI uri;
  private java.util.List<HBODigitalContent> movies = new java.util.ArrayList<HBODigitalContent>();
  private java.io.PrintWriter pw;

  /**
   * This constructor is used to instantiate the movie processor error log
  */
  public HBOMoviesProcessor(){
  	try{
  	  java.io.File error_log_file = new java.io.File("movie_processor_error_log.txt");
  	  pw = new java.io.PrintWriter(error_log_file);		
  	}
  	catch(java.io.FileNotFoundException fnfe){
  	  fnfe.printStackTrace();
  	}
  }

  /**
   * This method is used to set the Document for the Movies Processor
   * @param org.jsoup.nodes.Document
   * @return nothing
  */
  public void setDocument(Document doc){
  	document = doc;
  }
  
  /**
   * This method is used to process the list of movies
   * @return java.util.List -- list of movies
   * @exception MovieProcessingException -- custom exception of processing error
  */
  public java.util.List processMovies() throws MovieProcessingException {
  	movieLinks = document.select("a[href]");
    for (Element link : movieLinks) {
      calculateURI(link);
      HBODigitalContent content = new HBODigitalContent();
      movies.add(content);
      String jsonString = getJSON();
      try{
        int totalViews = content.getTotalViewsPerMonth(jsonString);
      }
      catch(MovieProcessingException pe){
        throw pe;
      }      		
    }
    return movies;
  }

  /**
   * This method is used to sort the list of movies by the total viewings per month
   * @param java.util.List -- list of unsorted movies
   * @return java.util.list -- list of sorted movies
  */
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

  /* 
   * Private methods 
   */

  /**
   * This method is used to calculate correct URL from the raw link
   * @param org.jsoup.nodes.Element -- raw link obtained from the JSON representation of a movie
   * @return nothing
  */
  private void calculateURI(Element link){ //throws URISyntaxException{
  	try{
      Date date = new Date(Long.parseLong(link.attr("data-d")));
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      String url_string = "http://stats.grok.se/json/en/" + cal.get(Calendar.YEAR) + (cal.get(Calendar.MONTH) + 1) + "/" + link.attr("data-t");
      try{
        uri = new URI(url_string.replace(" ", "%20"));
      }
      catch(URISyntaxException se){
        se.printStackTrace(pw);
      }
  	}
  	catch(NumberFormatException nfe){
  	  nfe.printStackTrace(pw);
  	}
  }

  /**
   * This method is used to fetch JSON representation of a movie
   * @return String -- sanitized JSON representation of a movie
  */
  private String getJSON(){ //throws java.io.IOException{
  	String bodyString = "";
  	try{
      Document doc = Jsoup.connect(uri.toString()).get();
      Elements body = doc.select("body");
      bodyString = body.toString();
      int curlyIndex = bodyString.indexOf("{");
      bodyString = bodyString.substring(curlyIndex, bodyString.length());
      int lessthanIndex = bodyString.indexOf("<");
      bodyString = bodyString.substring(0, lessthanIndex);
  	}
    catch(java.io.IOException ioe){
      ioe.printStackTrace(pw);
    }
    return bodyString;  
  }
}
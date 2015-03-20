package movies;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
* <h1>HBO Digital Content</h1>
* Represents a movie that can be sorted based on the total number of viewings per month
*
* @author  Alex Bunardzic
* @version 1.0
* @since   2015-03-20
*/
public class HBODigitalContent implements DigitalContent, Comparable<HBODigitalContent> {
  private String title, url;
  private int genreId, releaseYear, totalMonthlyViews;
  private java.util.Date originalAirDate;
  private JSONObject jsonBody, json, json2;
  private java.io.PrintWriter pw;

  /**
   * This constructor is used to instantiate the digital content error log
  */
  public HBODigitalContent(){
    try{
      java.io.File error_log_file = new java.io.File("digital_content_error_log.txt");
      pw = new java.io.PrintWriter(error_log_file);   
    }
    catch(java.io.FileNotFoundException fnfe){
      fnfe.printStackTrace();
    }
  }

  /**
   * This method is used to set the movie title
   * @param String -- movie title
  */
  public void setTitle(String newTitle){
    title = newTitle;
  }

  /**
   * This method is used to pbtain the movie title
   * @return String -- movie title
  */
  public String getTitle(){
    return title;
  }

  /**
   * This method is used to set the total monthly viewings
   * @param int -- total viewings
  */
  public void setTotalMonthlyViews(int totalViews){
    totalMonthlyViews = totalViews;
  }

  /**
   * This method is used to obtain the number of monthly viewings
   * @return int -- total viewings
  */
  public int getTotalMonthlyViews(){
    return totalMonthlyViews;
  }

  /**
   * This method is used to calculate total monthly viewings for a movie
   * @param String -- JSOn representation of a movie
   * @return int -- total number of viewings for the movie per month
  */
  public int getTotalViewsPerMonth(String bodyString) throws MovieProcessingException {
  	int totalViews = 0;
  	try{
  	  jsonBody = (JSONObject)new JSONParser().parse(bodyString);
  	  this.setTitle(jsonBody.get("title").toString());
  	  json = (JSONObject)new JSONParser().parse(jsonBody.get("daily_views").toString());
  	  json2 = (JSONObject)new JSONParser().parse(json.toString());
  	  totalViews = tallyUpViews(json2, jsonBody.get("month"));
    }
    catch(org.json.simple.parser.ParseException pe){
      //throw new MovieProcessingException("Error in parsing JSON");
      pe.printStackTrace(pw);
    }
  	this.setTotalMonthlyViews(totalViews);
    return totalViews;
  }

  /**
   * This method is used to compare total number of monthly viewings and use the result to order the movies by the total number of viewings in descending order
   * @param HBODigitalContent -- digital content (i.e. a movie)
   * @return int -- ranking based on the total number of monthly viewings
  */
  public int compareTo(HBODigitalContent content) {
	  int compareQuantity = ((HBODigitalContent) content).getTotalMonthlyViews(); 
	  //descending order
	  return compareQuantity - this.totalMonthlyViews;
  }

  /**
   * Private method
  */

  /**
   * This method is used to calculate total monthly viewings for a movie
   * @param JSONObject -- JSON representation of a movie
   * @param Object -- representing month
   * @return int -- calculated value for total monthly viewings for a movie
  */
  private int tallyUpViews(JSONObject json, Object month){
  	int totalViews = 0;
  	Object dayTotal = null;
  	String yearMonth = month.toString();
  	yearMonth = yearMonth.substring(0, 4) + "-" + yearMonth.substring(4, 6);
  	String day = "";
  	for(int i = 1; i < 32; i++){
  	  day = i + "";
  	  if(day.length() < 2)
  	  	day = "0" + day;
  	  dayTotal = json2.get(yearMonth + "-" + day);
      if(dayTotal != null){
        int totalDaily = Integer.parseInt(dayTotal.toString());
        totalViews += totalDaily;
      }
  	}
  	return totalViews;
  }
}
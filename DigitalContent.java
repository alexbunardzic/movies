package movies;

import java.lang.*;

/**
* <h1>Digital Content</h1>
* Interface defining the API for the digital content service
*
* @author  Alex Bunardzic
* @version 1.0
* @since   2015-03-19
*/
public interface DigitalContent {
  public void setTitle(String newTitle);
  public String getTitle();
  public void setTotalMonthlyViews(int totalViews);
  public int getTotalMonthlyViews();
  public int getTotalViewsPerMonth(String dailyViews) throws MovieProcessingException;
}
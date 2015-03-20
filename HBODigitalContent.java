//package com.hbo.content;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class HBODigitalContent implements DigitalContent, Comparable<HBODigitalContent> {
  private String title, url;
  private int genreId, releaseYear, totalMonthlyViews;
  private java.util.Date originalAirDate;
  private JSONObject jsonBody, json, json2;

  public void setTitle(String theTitle){
  	title = theTitle;
  }
  
  public String getTitle(){
  	return title;
  }

  public void setEntityURL(String theUrl){
  	url = theUrl;
  }

  public String getEntityURL(){
  	return url;
  }

  public void setGenreId(int id){
  	genreId = id;
  }

  public int getGenreId(){
  	return genreId;
  }

  public void setReleaseYear(int year){
  	releaseYear = year;
  }

  public int getReleaseYear(){
  	return releaseYear;
  }

  public void setOriginalAirDate(java.util.Date airDate){
  	originalAirDate = airDate;
  }

  public java.util.Date getOriginalAirDate(){
  	return originalAirDate;
  }

  public void setTotalMonthlyViews(int total){
  	totalMonthlyViews = total;
  }

  public int getTotalMonthlyViews(){
  	return totalMonthlyViews;
  }

  public int getTotalViewsPerMonth(String bodyString){
  	int totalViews = 0;
  	try{
  	  jsonBody = (JSONObject)new JSONParser().parse(bodyString);
  	  this.setTitle(jsonBody.get("title").toString());
  	  json = (JSONObject)new JSONParser().parse(jsonBody.get("daily_views").toString());
  	  json2 = (JSONObject)new JSONParser().parse(json.toString());
  	  totalViews = tallyUpViews(json2, jsonBody.get("month"));
    }
    catch(ParseException pe){
      System.out.println("position: " + pe.getPosition());
      System.out.println(pe);
    }
  	this.setTotalMonthlyViews(totalViews);
    return totalViews;
  }

  public int compareTo(HBODigitalContent content) {
	int compareQuantity = ((HBODigitalContent) content).getTotalMonthlyViews(); 
	//ascending order
	//return this.totalMonthlyViews - compareQuantity;
	//descending order
	return compareQuantity - this.totalMonthlyViews;
  }

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









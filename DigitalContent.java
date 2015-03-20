//package com.hbo.content;

import java.lang.*;

public interface DigitalContent {
  public void setTitle(String title);
  public String getTitle();
  public void setEntityURL(String url);
  public String getEntityURL();
  public void setGenreId(int id);
  public int getGenreId();
  public void setReleaseYear(int year);
  public int getReleaseYear();
  public void setOriginalAirDate(java.util.Date airDate);
  public java.util.Date getOriginalAirDate();
  public void setTotalMonthlyViews(int total);
  public int getTotalMonthlyViews();
  public int getTotalViewsPerMonth(String dailyViews) throws MovieProcessingException;
}
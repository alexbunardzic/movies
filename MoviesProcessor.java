package movies;

import java.lang.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
* <h1>Movies Processor</h1>
* Interface defining the API for the movies processing service
*
* @author  Alex Bunardzic
* @version 1.0
* @since   2015-03-19
*/
public interface MoviesProcessor {
  public void setDocument(Document doc);
  public java.util.List processMovies() throws MovieProcessingException;
  public java.util.List sortMovies(java.util.List movies);
}
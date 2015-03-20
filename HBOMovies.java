package movies;

import java.lang.*;

/**
* <h1>HBO Movies</h1>
* Ranks HBO GO available movies by popularity based on matching Wikipedia page views.
* The program fetches the list of HBO movies from http://xfinitytv.comcast.net/movie.widget.
* For each movie title name found in the list, the program looks for Wikipedia page views using the end-point: http://stats.grok.se/json/en/<YYYYMM>/<title name>.
* The program then adds up daily views for the movie and stores the total number of monthly views for the movie title.
* <p>Finally, the program sorts the list of movies by the total number of monthly views.</p>
* <p><strong>Exception handling:</strong> variety of exceptions can potentially occur, such as java.lang.NumberFormatException (in case of an empty string), 
* java.net.SocketTimeoutException (read timeout), and java.net.URISyntaxException (in case of detecting an illegal character in the supplied URL).
* Such exceptions are caught and written to the error log.</p>
*
* @author  Alex Bunardzic
* @version 1.0
* @since   2015-03-20
*/
public class HBOMovies {
    /**
    * This is the main method which uses the Movie Processor
    * @param args -- used if the user passes in the URL
    * @return Nothing
    * @exception IOException on input error
    * @see IOException
    */
	public static void main(String[] args) throws java.io.IOException {
      // Test if the user had submitted the URL from the command line; if not, default to http://xfinitytv.comcast.net/movie.widget
      String url;
      if(args.length == 1){
        url = args[0];
      } else {
        url = "http://xfinitytv.comcast.net/movie.widget";        
      }

      // Instantiate the Movie Processor and pass in the URL in order to get JSON representation of the movies
      MoviesProcessor moviesProcessor = new HBOMoviesProcessor();
      moviesProcessor.setDocument(org.jsoup.Jsoup.connect(url).get());
      try{
        // Traverse the list of movies and process them by totalling daily views for each movie
        java.util.List<HBODigitalContent> movies = moviesProcessor.processMovies();
        // Sort movies by total number of monthly views
        java.util.List sortedMovies = moviesProcessor.sortMovies(movies);
        System.out.println("HBO movies sorted by views per month (most viewed first):\n " + sortedMovies);
      }
      catch(MovieProcessingException pe){
        pe.printStackTrace();
      }
    }
}
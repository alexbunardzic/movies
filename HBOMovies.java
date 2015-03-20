import java.lang.*;

public class HBOMovies {
	public static void main(String[] args) throws java.io.IOException {
      String url;
      if(args.length == 1){
        url = args[0];
      } else {
        url = "http://xfinitytv.comcast.net/movie.widget";        
      }

      MoviesProcessor moviesProcessor = new HBOMoviesProcessor();
      moviesProcessor.setDocument(org.jsoup.Jsoup.connect(url).get()); 
      java.util.List<HBODigitalContent> movies = moviesProcessor.processMovies();
      java.util.List sortedMovies = moviesProcessor.sortMovies(movies);

      System.out.println("Sorted movies:\n " + sortedMovies);
    }
}
import java.lang.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public interface MoviesProcessor {
  public void setDocument(Document doc);
  public java.util.List processMovies();
  public java.util.List sortMovies(java.util.List movies);
}
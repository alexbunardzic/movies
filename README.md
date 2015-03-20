# HBO Movies Rating by Monthly Views
Ranks HBO GO available movies by popularity based on matching Wikipedia page views. The program fetches the list of HBO movies from http://xfinitytv.comcast.net/movie.widget. For each movie title name found in the list, the program looks for Wikipedia page views using the end-point: http://stats.grok.se/json/en/<http://stats.grok.se/json/en/<YYYYMM>/<title name>. The program then adds up daily views for the movie and stores the total number of monthly views for the movie title.

Finally, the program sorts the list of movies by the total number of monthly views.

## Third Party Libraries
We're using jsoup-1.8.1.jar (http://jsoup.org) for parsing the HTML document, and json-simple-1.1.1.jar (https://code.google.com/p/json-simple/) for processing JSON.
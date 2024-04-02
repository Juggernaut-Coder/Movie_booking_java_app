package CreateMovDatabase;

public class Movie {
    private String movieName;
    private String time;
   
    public Movie(String mn, String t) {
        this.movieName = mn;
        this.time = t;
    }
    public String getMovieName() {
        return movieName;
    }
    public String getTime() {
        return time;
    }
}

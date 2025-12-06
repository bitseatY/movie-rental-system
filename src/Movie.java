import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Movie {
    private final String title;
    private final List<String> genre;
    private  final Date yearOfRelease;
    private  final List<String > leadingActors;
    private  final String shortDescription;
    public enum  State {
        RENTED, AVAILABLE
    }
    private  State state;
    public  Movie(String title,List<String> genre,Date yearOfRelease,List<String > leadingActors,String shortDescription ){
        this.title=title;
        state=State.AVAILABLE;
        this.genre=genre;
        this.leadingActors=leadingActors;
        this.shortDescription=shortDescription;
        this.yearOfRelease=yearOfRelease;
    }

    public String getTitle() {
        return title;
    }

}

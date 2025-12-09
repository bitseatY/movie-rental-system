import java.sql.Date;
import java.util.List;

public class Movie {
    private final String title;
    private final List<String> genre;
    private  final Date yearOfRelease;
    private  final List<String > leadingActors;
    private  final String shortDescription;
    private   double costPerDay;
    public enum  State {
        RENTED, AVAILABLE
    }
    private  State state;
    public  Movie(String title,List<String> genre,Date yearOfRelease,List<String > leadingActors,String shortDescription,double costPerDay){
        this.title=title;
        state=State.AVAILABLE;
        this.genre=genre;
        this.leadingActors=leadingActors;
        this.shortDescription=shortDescription;
        this.yearOfRelease=yearOfRelease;
        this.costPerDay=costPerDay;
    }


    public String getTitle() {
        return title;
    }

}

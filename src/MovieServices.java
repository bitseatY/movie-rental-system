import java.sql.Connection;
import java.sql.SQLException;
import java.time.Year;
import java.util.*;

// add movies , add new customer , charge customer , see all rented and available movies

public class MovieServices {
    Scanner scanner=new Scanner(System.in);
    private  Connection connection;
    public MovieServices(Connection connection){
        this.connection =connection;
    }
    public  void  addMovie() throws SQLException{
         String title="the thief";
         String description="a thief tries to steal a girls heart ";
         String yearReleased="2000";
         int numOfCopies=4;
         List<String> genres=List.of("thriller","action","comedy");
         List<String> actors=List.of("will smith","selena","abel","selam");
         double cost=20;
         new MoviesDao(connection).addMovie(title,description,yearReleased,numOfCopies,genres,actors,cost);

    }



}

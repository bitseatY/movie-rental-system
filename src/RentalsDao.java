import java.sql.*;
import java.time.LocalDate;    //https://github.com/bitseatY/movie-rental-system.git
import java.time.LocalDateTime;

public class RentalsDao {
    private Connection connection;
    public RentalsDao(Connection connection){
        this.connection=connection;
    }
    public void  rent(String customerName, String movieTitle, LocalDate dueDate) throws SQLException {
        int customerId=new UsersDao(connection).findUserId(customerName);
        int movieId=new MoviesDao(connection).findMovieId(movieTitle);

        if(customerId!=0&&movieId!=0) {
            String query="call rent_a_movie(?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieId);
                ps.setInt(2, customerId);
                ps.setDate(3, Date.valueOf(dueDate));
                ps.executeUpdate();
            }
        }
    }

    public  void  returnMovie(String movieTitle,String customerName) throws SQLException{
        int customerId=new UsersDao(connection).findUserId(customerName);
        int movieId=new MoviesDao(connection).findMovieId(movieTitle);
        if(customerId!=0&&movieId!=0) {
            String query="call return_a_movie(?,?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieId);
                ps.setInt(2, customerId);
                ps.executeUpdate();

            }
        }
    }





}

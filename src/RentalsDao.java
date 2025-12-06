import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;    //https://github.com/bitseatY/movie-rental-system.git

public class RentalsDao {
    private Connection connection;
    public RentalsDao(Connection connection){
        this.connection=connection;
    }
    public void  rent(String username, String movieTitle, LocalDate dueDate , int noCopiesRented) throws SQLException {
        int userId=new UsersDao(connection).findUserId(username);
        int movieId=new MoviesDao(connection).findMovieId(movieTitle);
        String query;
        if(userId!=0&&movieId!=0) {
            query = "insert into rentals(m_id,c_id,due_date) values (?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieId);
                ps.setInt(2, userId);
                ps.setDate(3, java.sql.Date.valueOf(dueDate));
                ps.executeUpdate();
            }

            query = "call update_qty(?,?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieId);
                ps.setInt(2, noCopiesRented);
                ps.executeUpdate();
            }

        }
    }





}

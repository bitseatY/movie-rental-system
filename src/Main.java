import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws SQLException {
       Connection connection =DBConnection.getConnection("jdbc:mysql://localhost:3306/Movie_Rental", "root", "b:tse@t1996");
       RentalsDao rentalsDao=new RentalsDao(connection);

       rentalsDao.rent("leila","the thief",LocalDate.now(),2);

    }
}
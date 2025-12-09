import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection = DBConnection.getConnection("jdbc:mysql://localhost:3306/Movie_Rental", "root", "b:tse@t1996");
       new RentalsDao(connection).returnMovie("the thief","hanna");
    }
}
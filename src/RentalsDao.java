import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class RentalsDao {
    private double overDueFee;
    private final Connection connection;
    public RentalsDao(Connection connection) {
        this.connection = connection;
    }
    public void setOverDueFee(double overDueFee) {
        this.overDueFee = overDueFee;
    }

    public void rent(String customerName, String movieTitle, LocalDate dueDate) throws SQLException {
        int customerId = new UsersDao(connection).findUserId(customerName);
        int movieId = new MoviesDao(connection).findMovieId(movieTitle);

        if (customerId != 0 && movieId != 0) {
            String query = "call rent_a_movie(?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieId);
                ps.setInt(2, customerId);
                ps.setDate(3, Date.valueOf(dueDate));
                ps.executeUpdate();
            }
        }
    }

    public void returnMovie(String movieTitle, String customerName) throws SQLException {
        int customerId = new UsersDao(connection).findUserId(customerName);
        int movieId = new MoviesDao(connection).findMovieId(movieTitle);
        BigDecimal totalFee=getTotalFee(findFirstUnreturnedRentalId(movieId,customerId));
        if (customerId != 0 && movieId != 0&& !Objects.equals(totalFee, BigDecimal.ZERO)) {
            String query = "call return_a_movie(?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieId);
                ps.setInt(2, customerId);
                ps.setBigDecimal(3,totalFee);
                ps.executeUpdate();

            }
        }
    }

    public int findFirstUnreturnedRentalId(int movieId, int customerId) throws SQLException {
        if (customerId != 0 && movieId != 0) {
            String query = "  select id from rentals where movie_id=? \n" +
                    "    and customer_id= ? and date_returned is null order by id desc limit 1 ";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieId);
                ps.setInt(2, customerId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");

                }

            }
        }
        return 0;
    }

    public BigDecimal getTotalFee(int rentalId) throws SQLException {
        BigDecimal totalFee=BigDecimal.ZERO;
        double costPerDay;
        LocalDate rentalDate;
        LocalDate dueDate;
        String query = "select movies.cost_per_day,rentals.rental_date,rentals.due_date " +
                "   from movies join rentals on movies.id=rentals.movie_id  where rentals.id=? ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, rentalId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                costPerDay = rs.getDouble("cost_per_day");
                dueDate = rs.getDate("due_date").toLocalDate();

                rentalDate = rs.getDate("rental_date").toLocalDate();

                long daysDiff1= ChronoUnit.DAYS.between( rentalDate, dueDate);
                long daysDiff2=ChronoUnit.DAYS.between(dueDate, LocalDate.now().plusDays(30));
                totalFee= BigDecimal.valueOf(costPerDay).multiply(BigDecimal.valueOf(daysDiff1)).
                        add(BigDecimal.valueOf(overDueFee).multiply(BigDecimal.valueOf(daysDiff2))).setScale(2, RoundingMode.DOWN);
            }
        }
        System.out.println(totalFee);
       return  totalFee;
    }
}
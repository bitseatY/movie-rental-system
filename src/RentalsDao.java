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
            connection.setAutoCommit(false);
            try {
                PreparedStatement ps1=connection.prepareStatement(" update available_copies set no_av_copies=no_av_copies-1 where movie_id= ?;");
                ps1.setInt(1,movieId);
                ps1.executeUpdate();
                PreparedStatement ps2=connection.prepareStatement("  insert into rentals (movie_id,customer_id,due_date) values (?,?,?)");
                ps2.setInt(1,movieId);
                ps2.setInt(2,customerId);
                ps2.setDate(3,Date.valueOf(dueDate));
                ps2.executeUpdate();
                connection.commit();
            }catch (Exception e){
                connection.rollback();
            }
            connection.setAutoCommit(true);
        }
    }

    public void returnMovie(String movieTitle, String customerName) throws SQLException {
        int customerId = new UsersDao(connection).findUserId(customerName);
        int movieId = new MoviesDao(connection).findMovieId(movieTitle);
        BigDecimal totalFee=getTotalFee(findFirstUnreturnedRentalId(movieId,customerId));
        if (customerId != 0 && movieId != 0&& !Objects.equals(totalFee, BigDecimal.ZERO)) {
            connection.setAutoCommit(false);
            try {
                PreparedStatement ps1=connection.prepareStatement("update rentals set date_returned=current_date()," +
                        " total_fee_charged=? , is_returned='yes' where id=?");
                ps1.setBigDecimal(1,totalFee);
                ps1.setInt(2,findFirstUnreturnedRentalId(movieId,customerId));
                ps1.executeUpdate();
                PreparedStatement ps2=connection.prepareStatement("update available_copies set no_av_copies=no_av_copies+1 where movie_id=?");
                ps2.setInt(1,movieId);
                ps2.executeUpdate();
                connection.commit();
            }catch (Exception e){
                System.out.println(1);
                connection.rollback();
            }
            connection.setAutoCommit(true);
        }
    }

    public int findFirstUnreturnedRentalId(int movieId, int customerId) throws SQLException {
        if (customerId != 0 && movieId != 0) {
            String query = "  select id from rentals where movie_id=? \n" +
                    "    and customer_id= ? and date_returned='9999-01-01' order by id desc limit 1 ";

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
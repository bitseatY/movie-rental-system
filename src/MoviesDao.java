import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

public class MoviesDao {
    private Connection connection;

    public MoviesDao(Connection connection){
        this.connection=connection;
    }

    public void addNewMovie(String title, String description, String yearReleased, int numOfCopies,
                            List<String> genres, List<String> actors, double cost  ) throws SQLException {

        String query="insert  into movies (title,description,year_released,no_copies) values (?,?,?,?)";
        try (PreparedStatement ps=connection.prepareStatement(query)){
            ps.setString(1,title);
            ps.setString(2,description);
            ps.setString(3,yearReleased);
            ps.setInt(4,numOfCopies);
            ps.executeUpdate();
        }
        query="insert into genres (id,genre) values (?,?)";
        int id=findMovieId(title);
        if(id==0)
            return;
        for(String genre:genres){
            try (PreparedStatement ps=connection.prepareStatement(query)){
                ps.setInt(1,id);
                ps.setString(2,genre);
                ps.executeUpdate();
            }
        }
        query="insert into actors (id,actor) values (?,?)";
        for(String actor:actors){
            try (PreparedStatement ps=connection.prepareStatement(query)){
                ps.setInt(1,id);
                ps.setString(2,actor);
                ps.executeUpdate();
            }
        }
        query="insert into costs(id,cost_per_day) values (?,?)";
        try (PreparedStatement ps=connection.prepareStatement(query)){
            ps.setInt(1,id);
            ps.setDouble(2,cost);
            ps.executeUpdate();
        }
    }

    public void  addCopies(String title,int noCopies) throws SQLException{
        String query="update available_copies set no=no + ? where id=? ;";
        try (PreparedStatement ps=connection.prepareStatement(query)){
            ps.setInt(1,noCopies);
            ps.setInt(2,findMovieId(title));
            ps.executeUpdate();
        }
    }









    public int findMovieId(String title) throws SQLException {
        String query="select id from movies where title=?";
        try (PreparedStatement ps=connection.prepareStatement(query)){
            ps.setString(1,title);
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                return rs.getInt("id");
            return  0;
        }

    }
    public void returnMovie(String title) throws SQLException {
        String query = "update movies set state='available' where title=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, title);
            ps.executeUpdate();

        }
    }






}

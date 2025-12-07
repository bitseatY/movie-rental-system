import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDao {
    private Connection connection;
    public UsersDao(Connection connection){
        this.connection=connection;
    }
    public  int findUserId(String name) throws SQLException  {
        String query="select id  from users where name=?";
        try (PreparedStatement ps=connection.prepareStatement(query)){
            ps.setString(1,name);
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                return  rs.getInt("id");
            return  0;
        }
    }




}

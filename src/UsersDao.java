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

    public void  addUser(String name,String password,String role) throws SQLException   {
        String query="insert  into users (name,password,role) values (?,?,?)";
        try (PreparedStatement ps=connection.prepareStatement(query)){
            ps.setString(1,name);
            ps.setString(2,password);
            ps.setString(3,role);
            ps.executeUpdate();
        }





    }


}

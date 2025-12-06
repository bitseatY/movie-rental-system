import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String url;
    private  final String username;
    private  final String password;
    private  static  DBConnection dbConnection;
    private DBConnection(String url,String username,String password){
         this.username=username;
         this.password=password;
         this.url=url;
    }
    public static Connection getConnection(String url,String username,String password) throws SQLException {
         if(dbConnection==null)
             dbConnection=new DBConnection(url,username,password);
         return DriverManager.getConnection(dbConnection.url,dbConnection.username,dbConnection.password);
    }
}

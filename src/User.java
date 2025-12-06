public class User {
    public  enum  Role{ CUSTOMER, Admin,Staff}
    private final   String name;
    private final   String password;
    private  final   Role role;
    public User(String name,String password,Role role){
        this.password=password;
        this.name=name;
        this.role=role;
    }


}

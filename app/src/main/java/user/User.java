package user;

/**
 * Created by User on 2/1/2016.
 */
public class User {
    String username, password, email, location, gender;
    int user_id;

    public User(int user_id, String username, String password, String email, String location, String gender){
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.location = location;
        this.gender = gender;
    }

    public User(String username, String password, String email, String location, String gender){
        this.username = username;
        this.password = password;
        this.email = email;
        this.location = location;
        this.gender = gender;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

    public int getUser_id() {
        return user_id;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.email = "";
        this.location = "";
        this.gender = "";
    }
}

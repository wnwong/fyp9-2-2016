package user;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt("user_id", user.user_id);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.putString("email", user.email);
        spEditor.putString("location", user.location);
        spEditor.putString("gender", user.gender);
        spEditor.putString("phone", user.phone);
        spEditor.commit();
    }

    public User getLoggedInUser(){
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }
        int user_id = userLocalDatabase.getInt("user_id", 0);
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String email = userLocalDatabase.getString("email", "");
        String location = userLocalDatabase.getString("location", "");
        String gender = userLocalDatabase.getString("gender", "");
        String phone = userLocalDatabase.getString("phone", "");

        return new User(user_id, username, password, email, location, gender, phone);
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void setRefreshStatus(boolean refresh){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("refresh", refresh);
        spEditor.commit();
    }

    public boolean getRefreshStatus(){
        return userLocalDatabase.getBoolean("refresh", true);
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}

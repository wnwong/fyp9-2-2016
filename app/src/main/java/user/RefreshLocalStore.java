package user;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 17/2/2016.
 */
public class RefreshLocalStore {
    public static final String SP_NAME = "refreshDetails";
    SharedPreferences refreshLocalDatabase;
    public RefreshLocalStore(Context context){
        refreshLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void setRefreshStatus(boolean refresh){
        SharedPreferences.Editor spEditor = refreshLocalDatabase.edit();
        spEditor.putBoolean("refresh", refresh);
        spEditor.commit();
    }

    public boolean getRefreshStatus(){
        return refreshLocalDatabase.getBoolean("refresh", true);
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = refreshLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}

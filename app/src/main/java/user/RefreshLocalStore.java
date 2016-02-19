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

    public void clearRefreshData(){
        SharedPreferences.Editor spEditor = refreshLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
  public void setTradeDetail(userGadget userGadget){
      SharedPreferences.Editor spEditor = refreshLocalDatabase.edit();
      spEditor.putString("seller", userGadget.getSeller());
      spEditor.putString("time", userGadget.getTime());
      spEditor.putString("date", userGadget.getDate());
      spEditor.putString("location", userGadget.getLocation());
      spEditor.putString("product", userGadget.getProduct());
      spEditor.commit();
  }

    public userGadget getTradeDetail(){
        String product = refreshLocalDatabase.getString("product", "");
        String seller = refreshLocalDatabase.getString("seller", "");
        String time = refreshLocalDatabase.getString("time", "");
        String date = refreshLocalDatabase.getString("date", "");
        String location = refreshLocalDatabase.getString("location", "");

        return new userGadget(product, seller, time, date, location);
    }
}

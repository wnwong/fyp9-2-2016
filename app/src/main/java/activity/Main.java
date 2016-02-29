package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.secondhandtradingplatform.CreatePost;
import com.example.user.secondhandtradingplatform.Login;
import com.example.user.secondhandtradingplatform.R;
import com.example.user.secondhandtradingplatform.Register;
import com.example.user.secondhandtradingplatform.SearchResultActivity;
import com.example.user.secondhandtradingplatform.UserProfile;
import com.example.user.secondhandtradingplatform.customHandler;
import com.pushbots.push.Pushbots;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmGadget;
import RealmModel.RealmProduct;
import RealmQuery.QueryCamera;
import io.realm.Realm;
import io.realm.RealmResults;
import server.GetTradeCallback;
import server.ServerRequests;
import user.ProcessingTradeFragment;
import user.RefreshLocalStore;
import user.TradeHistoryFragment;
import user.UserLocalStore;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RefreshLocalStore refreshLocalStore;
    UserLocalStore userLocalStore;
    ProgressDialog progressDialog;
    ImageButton profilePic;
    ServerRequests serverRequests;
    private Realm realm;
    private SearchView sv;
    private boolean switchFragment = true;
    public static final String TAG = "getProductList";
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(customHandler.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        userLocalStore = new UserLocalStore(this);
        refreshLocalStore = new RefreshLocalStore(this);
 /*       if (refreshLocalStore.getRefreshStatus() == true) {
            Log.i("Refresh", "Refreshing");
            showProgress();
            refreshDB();
        }
 */       //Switch to Tablet Fragment when launched
        switchToDefaultFragment();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userLocalStore.getLoggedInUser() != null) {
                    startActivity(new Intent(Main.this, CreatePost.class));
                } else {
                    startActivity(new Intent(Main.this, Login.class));
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        profilePic = (ImageButton) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverRequests = new ServerRequests(getApplicationContext());
                serverRequests.fetchTradeDataInBackground(new GetTradeCallback() {
                    @Override
                    public void done(List<RealmGadget> realmGadgets) {
                        Realm realm = Realm.getInstance(getApplicationContext());
                        for (int i = 0; i < realmGadgets.size(); i++) {
                            RealmGadget currentGadget = realmGadgets.get(i);
                            RealmGadget toEdit = realm.where(RealmGadget.class).equalTo("product_id", currentGadget.getProduct_id()).findFirst();
                            if (toEdit != null) {
                                realm.beginTransaction();
                                toEdit.setAvailability(currentGadget.getAvailability());
                                toEdit.setBuyer(currentGadget.getBuyer());
                                toEdit.setBuyer_location(currentGadget.getBuyer_location());
                                toEdit.setTrade_date(currentGadget.getTrade_date());
                                toEdit.setTrade_time(currentGadget.getTrade_time());
                                toEdit.setRating(currentGadget.getRating());
                                realm.commitTransaction();
                            }

                        }
                        Message message = new Message();
                        message.what = 1;
                        Message message1 = new Message();
                        message1.what = 1;
                        TradeHistoryFragment.mHandler.sendMessage(message);
                        ProcessingTradeFragment.mHandler.sendMessage(message1);
                    }
                });

                startActivity(new Intent(getApplicationContext(), UserProfile.class));
            }
        });
        PackageManager m = getPackageManager();
        String s = getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);
            System.out.println("s" + s);
            System.out.println("package by package" + p);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        s = p.applicationInfo.dataDir;
    }


    @Override
    protected void onStart() {
        super.onStart();
        authenticate();
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //    switchToDefaultFragment();
        // After User login change the login button into logout button
        if (authenticate() == true) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setTitle(getString(R.string.logout));
            menu.findItem(R.id.nav_register).setVisible(false);
            //Update nav_header
            profilePic.setVisibility(View.VISIBLE);
            TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv);
            TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
            username.setText(userLocalStore.getLoggedInUser().getUsername().toString());
            email.setText(userLocalStore.getLoggedInUser().getEmail().toString());
        } else {
            profilePic.setVisibility(View.INVISIBLE);
        }
        //switchDefaultFragment();
        if (sv != null) {
            sv.setIconified(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is pr
        // esent.
        getMenuInflater().inflate(R.menu.main, menu);
        //Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        this.sv = searchView;
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                //Text has changed, apply filtering
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Perform the final search
                QueryCamera queryCamera = new QueryCamera(getApplicationContext());
                RealmResults<RealmProduct> result = queryCamera.searchProductsByModel(query);
                List<RealmProduct> results = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    results.add(result.get(i));
                }
                SearchResultActivity.results = results;
                Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                startActivity(intent);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refreshLocalStore.setRefreshStatus(true);
            showProgress();
            realm = Realm.getInstance(this);
            clearDB(realm);
            refreshDB();
            return true;
        }
        if (id == R.id.action_search) {
            //   Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragment = new CameraFragment();
        } else if (id == R.id.nav_tablet) {
            fragment = new TabletFragment();
        } else if (id == R.id.nav_smartphone) {
            fragment = new SmartphoneFragment();
        } else if (id == R.id.nav_earphone) {
            fragment = new EarphoneFragment();
        } else if (id == R.id.nav_games) {
            fragment = new GameConsoleFragment();
        } else if (id == R.id.nav_login) {
            if (authenticate() == true) {
                logoutMessage();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();
                menu.findItem(R.id.nav_login).setTitle(getString(R.string.title_activity_login));
                menu.findItem(R.id.nav_register).setVisible(true);
                //Update nav_header
                TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv);
                TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
                username.setText("");
                email.setText("");
            } else {
                Intent myIntent = new Intent(this, Login.class);
                startActivity(myIntent);
                finish();
            }
        } else if (id == R.id.nav_register) {
            Intent myIntent = new Intent(this, Register.class);
            startActivity(myIntent);
            finish();
        }
        if (fragment != null) {//Perform Fragment Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutMessage() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Main.this);
        dialogBuilder.setMessage("確定要登出嗎？");
        dialogBuilder.setTitle("提示");
        dialogBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

            }
        });
        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.show();
    }

    public class loadAllProducts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            realm = Realm.getInstance(getApplicationContext());
            ///         clearDB(realm);
            String JSONResponse = getResponseFromServer("getPosts", null);
            Log.i(TAG, JSONResponse);
            try {
                JSONObject jObject = new JSONObject(JSONResponse);
                String gadget = jObject.getString("gadgets");
                JSONArray gadgetArray = new JSONArray(gadget);
                for (int i = 0; i < gadgetArray.length(); i++) {
                    JSONObject obj = gadgetArray.getJSONObject(i);

                    int pid = obj.getInt("product_id");
                    String brand = obj.getString("brand");
                    String model = obj.getString("model");
                    String warranty = obj.getString("warranty");
                    String price = obj.getString("price");
                    String seller_location = obj.getString("seller_location");
                    String seller_date = obj.getString("seller_date");
                    String seller_time_start = obj.getString("seller_time_start");
                    String seller_time_end = obj.getString("seller_time_end");
                    String type = obj.getString("type");
                    String seller = obj.getString("seller");
                    String scratch = obj.getString("scratch");
                    String color = obj.getString("color");
                    String image = obj.getString("path");
                    String image1 = obj.getString("path1");
                    String availability = obj.getString("availability");
                    String seller_date_2 = obj.getString("seller_date_2");
                    String seller_location_2 = obj.getString("seller_location_2");
                    String seller_time_start_2 = obj.getString("seller_time_start_2");
                    String seller_time_end_2 = obj.getString("seller_time_end_2");
                    createPostsEntry(realm, pid, brand, model, warranty, price, seller_location, type, seller, scratch, color, image, image1, seller_date, seller_time_start,
                           seller_time_end, availability, seller_date_2, seller_location_2, seller_time_start_2, seller_time_end_2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public class getProductList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            realm = Realm.getInstance(getApplicationContext());
            //    clearDB(realm);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("type", "smartphone");
            String query = builder.build().getEncodedQuery();
            String JSONResponse = getResponseFromServer("getProductList", query);
            Log.i(TAG, JSONResponse);
            try {
                JSONObject jObject = new JSONObject(JSONResponse);
                String product = jObject.getString("products");
                JSONArray productArray = new JSONArray(product);
                for (int i = 0; i < productArray.length(); i++) {
                    JSONObject obj = productArray.getJSONObject(i);
                    System.out.println("obj array" + obj);

                    String brand = obj.getString("brand");
                    String model = obj.getString("model");
                    String type = obj.getString("type");
                    String price = obj.getString("price");
                    String os = obj.getString("os");
                    String monitor = obj.getString("monitor");
                    String camera = obj.getString("camera");
                    String path = obj.getString("image_name");
                    //    Log.i(TAG, "Image_Name = " + path);
                    createProductEntry(realm, brand, model, type, price, os, monitor, camera, path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
//            progressBar.setVisibility(View.GONE);
  /*          if(switchFragment==true){
                switchToDefaultFragment();
                switchFragment=false;
            }*/
        }
    }

    public class getGameConsole extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            realm = Realm.getInstance(getApplicationContext());
            //    clearDB(realm);
            String JSONResponse = getResponseFromServer("getGameConsole", null);
            Log.i(TAG, JSONResponse);
            try {
                JSONObject jObject = new JSONObject(JSONResponse);
                String product = jObject.getString("products");
                JSONArray productArray = new JSONArray(product);
                for (int i = 0; i < productArray.length(); i++) {
                    JSONObject obj = productArray.getJSONObject(i);
                    System.out.println("obj array" + obj);

                    String brand = obj.getString("brand");
                    String model = obj.getString("model");
                    String type = obj.getString("type");
                    String price = obj.getString("price");
                    String os = obj.getString("os");
                    String monitor = obj.getString("monitor");
                    String camera = obj.getString("camera");
                    String path = obj.getString("image_name");
                    //    Log.i(TAG, "Image_Name = " + path);
                    createProductEntry(realm, brand, model, type, price, os, monitor, camera, path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public class getTablet extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            realm = Realm.getInstance(getApplicationContext());
            //    clearDB(realm);
            String JSONResponse = getResponseFromServer("getTablet", null);
            Log.i(TAG, JSONResponse);
            try {
                JSONObject jObject = new JSONObject(JSONResponse);
                String product = jObject.getString("products");
                JSONArray productArray = new JSONArray(product);
                for (int i = 0; i < productArray.length(); i++) {
                    JSONObject obj = productArray.getJSONObject(i);
                    System.out.println("obj array" + obj);

                    String brand = obj.getString("brand");
                    String model = obj.getString("model");
                    String type = obj.getString("type");
                    String price = obj.getString("price");
                    String os = obj.getString("os");
                    String monitor = obj.getString("monitor");
                    String camera = obj.getString("camera");
                    String path = obj.getString("image_name");
                    //    Log.i(TAG, "Image_Name = " + path);
                    createProductEntry(realm, brand, model, type, price, os, monitor, camera, path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class getCamera extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            realm = Realm.getInstance(getApplicationContext());
            //    clearDB(realm);
            String JSONResponse = getResponseFromServer("getCamera", null);
            Log.i(TAG, JSONResponse);
            try {
                JSONObject jObject = new JSONObject(JSONResponse);
                String product = jObject.getString("products");
                JSONArray productArray = new JSONArray(product);
                for (int i = 0; i < productArray.length(); i++) {
                    JSONObject obj = productArray.getJSONObject(i);
                    System.out.println("obj array" + obj);

                    String brand = obj.getString("brand");
                    String model = obj.getString("model");
                    String type = obj.getString("type");
                    String price = obj.getString("price");
                    String os = obj.getString("os");
                    String monitor = obj.getString("monitor");
                    String camera = obj.getString("camera");
                    String path = obj.getString("image_name");
                    //    Log.i(TAG, "Image_Name = " + path);
                    createProductEntry(realm, brand, model, type, price, os, monitor, camera, path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class getEarphone extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            realm = Realm.getInstance(getApplicationContext());
            //    clearDB(realm);
            String JSONResponse = getResponseFromServer("getEarphone", null);
            Log.i(TAG, JSONResponse);
            try {
                JSONObject jObject = new JSONObject(JSONResponse);
                String product = jObject.getString("products");
                JSONArray productArray = new JSONArray(product);
                for (int i = 0; i < productArray.length(); i++) {
                    JSONObject obj = productArray.getJSONObject(i);
                    System.out.println("obj array" + obj);

                    String brand = obj.getString("brand");
                    String model = obj.getString("model");
                    String type = obj.getString("type");
                    String price = obj.getString("price");
                    String os = obj.getString("os");
                    String monitor = obj.getString("monitor");
                    String camera = obj.getString("camera");
                    String path = obj.getString("image_name");
                    //    Log.i(TAG, "Image_Name = " + path);
                    createProductEntry(realm, brand, model, type, price, os, monitor, camera, path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void switchToDefaultFragment() {
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = SmartphoneFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("重新整理中");
        progressDialog.setMessage("請稍候...");
        progressDialog.show();
//        progressBar.setVisibility(View.VISIBLE);
    }

    private String getResponseFromServer(String php, String query) {
        String json = null;
        try {
            URL url = new URL(SERVER_ADDRESS + php + ".php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            if (query != null) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                Log.i(TAG, line);
            }
            json = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private void createProductEntry(Realm realm, String brand, String model, String type, String price, String os, String monitor, String camera, String path) {
        RealmProduct toEdit = (realm.where(RealmProduct.class).equalTo("model", model).findFirst());
        realm.beginTransaction();
        if (toEdit == null) {
            toEdit = realm.createObject(RealmProduct.class);
        }
        toEdit.setPrice(price);
        toEdit.setBrand(brand);
        toEdit.setModel(model);
        toEdit.setCamera(camera);
        toEdit.setMonitor(monitor);
        toEdit.setType(type);
        toEdit.setOs(os);
        toEdit.setPath(path);
        realm.commitTransaction();

    }

    private void createPostsEntry(Realm realm, int pid, String brand, String model, String warranty, String price, String seller_location, String type, String seller, String scratch, String color, String image,
                                  String image1, String seller_date, String seller_time_start, String seller_time_end, String availability, String seller_date_2,
                                  String seller_location_2,  String seller_time_start_2, String seller_time_end_2) {
        realm.beginTransaction();
        RealmGadget rc = realm.createObject(RealmGadget.class);
        rc.setProduct_id(pid);
        rc.setBrand(brand);
        rc.setModel(model);
        rc.setWarranty(warranty);
        rc.setPrice(price);
        rc.setType(type);
        rc.setSeller(seller);
        rc.setScratch(scratch);
        rc.setSeller_location(seller_location);
        rc.setColor(color);
        rc.setImage(image);
        rc.setImage1(image1);
        rc.setSeller_date(seller_date);
        rc.setSeller_time_start(seller_time_start);
        rc.setSeller_time_end(seller_time_end);
        rc.setAvailability(availability);
        rc.setSeller_date_2(seller_date_2);
        rc.setSeller_location_2(seller_location_2);
        rc.setSeller_time_start_2(seller_time_start_2);
        rc.setSeller_time_end_2(seller_time_end_2);
        realm.commitTransaction();
    }

    private void refreshDB() {
        new loadAllProducts().execute();
        new getGameConsole().execute();
        new getTablet().execute();
        new getCamera().execute();
        new getEarphone().execute();
        new getProductList().execute();
        refreshLocalStore.setRefreshStatus(false);
    }

    private void clearDB(Realm realm) {
        realm.beginTransaction();
//        realm.allObjects(RealmProduct.class).clear();
        realm.allObjects(RealmGadget.class).clear();
        realm.commitTransaction();
    }
}

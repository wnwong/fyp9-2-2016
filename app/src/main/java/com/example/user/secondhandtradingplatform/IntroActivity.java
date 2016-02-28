package com.example.user.secondhandtradingplatform;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import RealmModel.RealmGadget;
import RealmModel.RealmProduct;
import activity.Main;
import activity.SmartphoneFragment;
import io.realm.Realm;
import user.RefreshLocalStore;

public class IntroActivity extends Activity {
    RefreshLocalStore refreshLocalStore;
    public static String TAG = "Intro";
    TextView waitTV;
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";

    Realm realm;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Log.i(TAG, "INTROACTIVITY");
        waitTV = (TextView) findViewById(R.id.waitTV);
        refreshLocalStore = new RefreshLocalStore(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        if (refreshLocalStore.getRefreshStatus() == true) {
            Log.i("Refresh", "Refreshing");
            showProgress();
            refreshDB();
        }else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), Main.class));
                    finish();
                }
            }, 5000);

        }
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
                    //Base64 encoded gadget image
                    String image = obj.getString("path");
                    String image1 = obj.getString("path1");
                    String availability = obj.getString("availability");
                    //                  Log.i(TAG, brand + " " + model + " " + seller_location);
                    createPostsEntry(realm, pid, brand, model, warranty, price, seller_location, type, seller, scratch, color, image, image1, seller_date, seller_time_start, seller_time_end, availability);
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
            //  progressDialog.dismiss();
            progressBar.setVisibility(View.GONE);
            waitTV.setVisibility(View.GONE);
            startActivity(new Intent(getApplicationContext(), Main.class));
            finish();
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

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
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
                                  String image1, String seller_date, String seller_time_start, String seller_time_end, String availability) {
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

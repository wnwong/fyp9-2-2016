package server;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import RealmModel.RealmGadget;
import io.realm.Realm;
import user.User;

public class ServerRequests {
    ProgressDialog progressDialog;
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";
    public static final String TAG = "ServerRequests";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallback).execute();
    }

    public void storeTradeDataInBackground(String type, String brand, String model, String warranty, String color, String scratch, String price, String timeStart,
                                           String timeEnd, Bitmap image1, Bitmap image2, String seller_location, String datePattern, String seller, GetPostCallback getPostCallback) {
        progressDialog.show();
        new storeTradeDataAsyncTask(type, brand, model, warranty, color, scratch, price, timeStart,
                timeEnd, image1, image2, seller_location, datePattern, seller, getPostCallback).execute();
    }

    public class storeTradeDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private String type, brand, model, warranty, price, color, scratch, timeStart,
                timeEnd, seller_location, datePattern, seller;
        private Bitmap image1, image2;
        GetPostCallback getPostCallback;

        public storeTradeDataAsyncTask(String type, String brand, String model, String warranty, String color, String scratch, String price, String timeStart, String timeEnd, Bitmap image1,
                                       Bitmap image2, String seller_location, String datePattern, String seller, GetPostCallback getPostCallback) {
            this.type = type;
            this.brand = brand;
            this.model = model;
            this.warranty = warranty;
            this.color = color;
            this.scratch = scratch;
            this.price = price;
            this.timeStart = timeStart;
            this.timeEnd = timeEnd;
            this.image1 = image1;
            this.image2 = image2;
            this.seller_location = seller_location;
            this.datePattern = datePattern;
            this.seller = seller;
            this.getPostCallback = getPostCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "Sending to Server");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            String encodeImage = Base64.encodeToString(array, Base64.DEFAULT);
            Log.i(TAG, "encodeImage: " + encodeImage);
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            image2.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream2);
            byte[] array2 = byteArrayOutputStream2.toByteArray();
            String encodeImage2 = Base64.encodeToString(array2, Base64.DEFAULT);
            Log.i(TAG, "encodeImage2: " + encodeImage2);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("type", type)
                    .appendQueryParameter("brand", brand)
                    .appendQueryParameter("model", model)
                    .appendQueryParameter("color", color)
                    .appendQueryParameter("scratch", scratch)
                    .appendQueryParameter("warranty", warranty)
                    .appendQueryParameter("price", price)
                    .appendQueryParameter("timeStart", timeStart)
                    .appendQueryParameter("timeEnd", timeEnd)
                    .appendQueryParameter("image1", encodeImage)
                    .appendQueryParameter("image2", encodeImage2)
                    .appendQueryParameter("datePattern", datePattern)
                    .appendQueryParameter("seller", seller)
                    .appendQueryParameter("seller_location", seller_location)
                    .appendQueryParameter("availability", "放售中");
            String query = builder.build().getEncodedQuery();
            Log.i(TAG, "query:" + query);
            getResponseFromServer("postGadget", query);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            getPostCallback.done();
        }
    }

    public void fetchTradeDataInBackground(GetTradeCallback getTradeCallback) {
//        progressDialog.show();
        new fetchTradeDataAsyncTask(getTradeCallback).execute();
    }

    public class fetchTradeDataAsyncTask extends AsyncTask<Void, Void, List<RealmGadget>> {
        int pid, rating;
        String buyer, buyer_location, trade_date, trade_time, availability;
        GetTradeCallback getTradeCallback;
        List<RealmGadget> gadgets = new ArrayList<>();
        public fetchTradeDataAsyncTask(GetTradeCallback getTradeCallback) {
         this.getTradeCallback = getTradeCallback;
        }

        @Override
        protected List<RealmGadget> doInBackground(Void... params) {
            String JSONResponse = getResponseFromServer("getTradeDetail", null);
            Log.i(TAG, JSONResponse);
            try {
                JSONObject jObject = new JSONObject(JSONResponse);
                String gadget = jObject.getString("gadgets");
                JSONArray gadgetArray = new JSONArray(gadget);
                for (int i = 0; i < gadgetArray.length(); i++) {
                    JSONObject obj = gadgetArray.getJSONObject(i);
                     pid = obj.getInt("product_id");
                     rating = obj.getInt("rating");
                     buyer = obj.getString("buyer");
                     buyer_location = obj.getString("buyer_location");
                     trade_date = obj.getString("trade_date");
                     trade_time = obj.getString("trade_time");
                     availability = obj.getString("availability");
                    gadgets.add(new RealmGadget(pid, buyer, buyer_location, trade_date, trade_time, rating, availability));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return gadgets;
        }

        @Override
        protected void onPostExecute(List<RealmGadget> realmGadgets) {
            super.onPostExecute(realmGadgets);
            getTradeCallback.done(realmGadgets);
        //    progressDialog.dismiss();
        }
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection con = null;
            //Data to be sent to the Server
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.getUsername());
            dataToSend.put("password", user.getPassword());
            dataToSend.put("email", user.getEmail());
            dataToSend.put("location", user.getLocation());
            dataToSend.put("gender", user.getGender());

            //Encoded String
            String encodedStr = getEncodedData(dataToSend);

            try {
                //Conerting address String to URL
                URL url = new URL(SERVER_ADDRESS + "test.php");
                //Opening the connection
                con = (HttpURLConnection) url.openConnection();

                //POST method
                con.setRequestMethod("POST");
                //To enable inputting values using POST method
                con.setDoOutput(true);
                con.setDoInput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", user.getUsername())
                        .appendQueryParameter("password", user.getPassword())
                        .appendQueryParameter("email", user.getEmail())
                        .appendQueryParameter("location", user.getLocation())
                        .appendQueryParameter("gender", user.getGender());
                String query = builder.build().getEncodedQuery();

                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Write dataToSend to OutputStreamWriter
                writer.write(query);
                System.out.println("encodedStr:" + query);
                writer.flush();
                writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            HttpURLConnection con = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.getUsername());
            dataToSend.put("password", user.getPassword());

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;
            User returnedUser = null;
            System.out.println("before try");
            try {
                //Conerting address String to URL
                URL url = new URL(SERVER_ADDRESS + "login.php");
                //Opening the connection
                con = (HttpURLConnection) url.openConnection();
                System.out.println("connection opened");
                //POST method
                con.setRequestMethod("POST");
                //To enable inputting values using POST method
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Write dataToSend to OutputStreamWriter
                writer.write(encodedStr);
                Log.i("Writer Output", encodedStr);
                writer.flush();
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String json = sb.toString();

                Log.i("custom_check", "The values received in the login part are as follows:");
                Log.i("custom_check", json);

                JSONObject jObject = new JSONObject(json);
                //   JSONArray array = new JSONArray(json);
                //    JSONArray arr = jObject.getJSONArray("return_arr");

                if (jObject.length() == 0) {
                    returnedUser = null;
                    Log.i("custom_check", "No returnedUser!! ");
                } else {
                    String email = null;
                    String location = null;
                    String gender = null;
                    email = jObject.getString("email");
                    location = jObject.getString("location");
                    gender = jObject.getString("gender");
                    int user_id = jObject.getInt("user_id");
                    Log.i("custom_check", "the parsed JSON info are as follows:");
                    Log.i("custom_check", user_id + " " + email + " " + location + " " + gender);
                    returnedUser = new User(user_id, user.getUsername(), user.getPassword(), email, location, gender);
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

    private String getEncodedData(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (sb.length() > 0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
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
                Log.i(TAG, "Writing to Server!");
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
                Log.i(TAG, "Response from server" + line);
            }
            json = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}


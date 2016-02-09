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
import java.util.Map;

import user.User;

public class ServerRequests {
    ProgressDialog progressDialog;
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback userCallback){
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void>{
        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback){
            this.user = user;
            this.userCallback = userCallback;
        }
        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection con = null;
            //Data to be sent to the Server
            Map<String,String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.getUsername());
            dataToSend.put("password", user.getPassword());
            dataToSend.put("email", user.getEmail());
            dataToSend.put("location", user.getLocation());
            dataToSend.put("gender", user.getGender());

            //Encoded String
            String encodedStr = getEncodedData(dataToSend);

            try{
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
                        .appendQueryParameter ("location", user.getLocation())
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
                while ((line = reader.readLine())!= null) {
                    System.out.println(line);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                if(con != null){
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

    private String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(sb.length()>0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User>{
        User user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            HttpURLConnection con = null;
            Map<String,String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.getUsername());
            dataToSend.put("password", user.getPassword());

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;
            User returnedUser=null;
            System.out.println("before try");
            try{
                //Conerting address String to URL
                URL url = new URL(SERVER_ADDRESS + "login.php");
                //Opening the connection
                 con = (HttpURLConnection) url.openConnection();
                System.out.println("connection opened");
                //POST method
                con.setRequestMethod("POST");
                //To enable inputting values using POST method
                con.setDoOutput(true);
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Write dataToSend to OutputStreamWriter
                writer.write(encodedStr);
                Log.i("Writer Output", encodedStr);
                writer.flush();
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                String json = sb.toString();

                Log.i("custom_check", "The values received in the login part are as follows:");
                Log.i("custom_check", json);

                JSONObject jObject = new JSONObject(json);
             //   JSONArray array = new JSONArray(json);
            //    JSONArray arr = jObject.getJSONArray("return_arr");

                if(jObject.length() == 0){
                    returnedUser = null;
                    Log.i("custom_check", "No returnedUser!! ");
                }else{
                    String email = null;
                    String location = null;
                    String gender = null;
                         email = jObject.getString("email");
                         location = jObject.getString("location");
                         gender = jObject.getString("gender");
                        int user_id = jObject.getInt("user_id");
                    Log.i("custom_check", "the parsed JSON info are as follows:");
                    Log.i("custom_check", user_id + " " + email + " " + location + " " +gender);
                    returnedUser = new User(user_id, user.getUsername(), user.getPassword(), email, location, gender);
                    reader.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(con != null){
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
}


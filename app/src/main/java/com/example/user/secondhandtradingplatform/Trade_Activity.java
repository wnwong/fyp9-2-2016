package com.example.user.secondhandtradingplatform;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import RealmModel.RealmGadget;
import RealmQuery.QueryCamera;
import activity.Main;
import io.realm.Realm;
import user.RefreshLocalStore;
import user.UserLocalStore;
import user.userGadget;

public class Trade_Activity extends AppCompatActivity implements DatePickerDialogFragment.OnDatePickedListener, TimePickerDialogFragment.OnTimePickedListener {
    Button mDateButton, mTimeButton, confirmBtn, cancelBtn;
    ViewPager mPager;
    PagerAdapter mPagerAdapter;
    RealmGadget gadget;
    List<String> images = new ArrayList<>();
    String image, image1, seller_location, buyer_time, buyer_date, buyer_location, time, date, seller, product;
    int product_id;
    TextView tvDate, tvTime, tvLocation, tvPrice;
    Spinner locationSpinner;
    UserLocalStore userLocalStore;
    RefreshLocalStore refreshLocalStore;
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String SQL_TIME_FORMAT = "HH:mm:ss";
    public static final String TAG = "Trade_Activity";
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        product_id = getIntent().getIntExtra("id", 0);
        userLocalStore = new UserLocalStore(this);
        refreshLocalStore = new RefreshLocalStore(this);
        QueryCamera query = new QueryCamera(this);
        gadget = query.retrieveGadgetById(product_id);
        product = gadget.getBrand()+" "+gadget.getModel();
        seller = gadget.getSeller();
        if (gadget != null) {
            image = gadget.getImage();
             image1 = gadget.getImage1();
        }
        if (image != null) {
            images.add(image);
        }
        if (image1 != null) {
            images.add(image1);
        }
        // Setup Image Slide ViewPager
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ImageSlidePagerAdapter(this, images);
        mPager.setAdapter(mPagerAdapter);

        mDateButton = (Button) findViewById(R.id.mDateButton);
        mTimeButton = (Button) findViewById(R.id.mTimeButton);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerDialogFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerDialogFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        // Setup Seller Info TextView
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvPrice.setText("HK$" + gadget.getPrice());

        try { //Format the date and time obtained from the database
            tvDate = (TextView) findViewById(R.id.tvDate);
            tvDate.setText(gadget.getSeller_date());
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            Date time = simpleDateFormat.parse(gadget.getSeller_time_start());
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeStringStart = simpleDateFormat.format(time);
            tvTime = (TextView) findViewById(R.id.tvTime);
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            Date timeEnd = simpleDateFormat.parse(gadget.getSeller_time_end());
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeStringEnd = simpleDateFormat.format(timeEnd);
            tvTime = (TextView) findViewById(R.id.tvTime);
            tvTime.setText(timeStringStart + " - " + timeStringEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        seller_location = gadget.getSeller_location();
        tvLocation.setText(seller_location);
        // Setup Spinner
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        ArrayAdapter locationAdapter = new ArrayAdapter(this, R.layout.myspinner, getLocationList(seller_location));
        locationSpinner.setAdapter(locationAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buyer_location = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //Setup the buttons
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyer_time != null && buyer_date != null) {
                    confirmSendMessage();
                } else {
                    showErrorMessage();
                }
            }
        });
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(!gadget.getSeller_location_2().equals("")){
            addView();
        }
    }

    private void addView(){
        LayoutInflater layoutInflater =  (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.seller_trade_info, null);
        final LinearLayout ll = (LinearLayout) findViewById(R.id.sellerLinearLayout);
        ll.addView(addView);
        TextView datetv = (TextView) ll.findViewById(R.id.tvDateAdd);
        TextView time = (TextView) ll.findViewById(R.id.tvTimeAdd);
        TextView location = (TextView) ll.findViewById(R.id.tvLocationAdd);
        location.setText(gadget.getSeller_location_2());
        try { //Format the date and time obtained from the database
            //Set seller date 2
            datetv.setText(gadget.getSeller_date_2());

            //Convert from SQL time start format to time format
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            Date time2 = simpleDateFormat.parse(gadget.getSeller_time_start_2());
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeStringStart = simpleDateFormat.format(time2);

            //Convert from SQL time end format to time format
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            Date timeEnd = simpleDateFormat.parse(gadget.getSeller_time_end_2());
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeStringEnd = simpleDateFormat.format(timeEnd);

            //Set seller time
            time.setText(timeStringStart + " - " + timeStringEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void OnDatePicked(String msg) {
        mDateButton.setText(msg);
        try {
            date = msg;
            simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date date = simpleDateFormat.parse(msg);
            simpleDateFormat = new SimpleDateFormat(SQL_DATE_FORMAT);
            String dateString = simpleDateFormat.format(date);
            buyer_date = dateString;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnTimePicked(String msg) {
        mTimeButton.setText(msg);
        try {
            time = msg;
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            Date time = simpleDateFormat.parse(msg);
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            String timeString = simpleDateFormat.format(time);
            buyer_time = timeString;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ImageSlidePagerAdapter extends PagerAdapter {
        List<String> images = new ArrayList<>();
        Context mContext;

        public ImageSlidePagerAdapter(Context context, List<String> images) {
            this.images = images;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
            Picasso.with(getApplicationContext()).load(IMAGE_ADDRESS + images.get(position)).into(imageView);
            container.addView(layout);
            return layout;
        }
    }

    private String[] getLocationList(String seller_location) {
        String[] returnLocationList = null;
        switch (seller_location) {
            case "觀塘綫":
                returnLocationList = getResources().getStringArray(R.array.ktlt);
                break;
            case "荃灣綫":
                returnLocationList = getResources().getStringArray(R.array.twlt);
                break;
            case "港島綫":
                returnLocationList = getResources().getStringArray(R.array.islt);
                break;
            case "將軍澳綫":
                returnLocationList = getResources().getStringArray(R.array.tklt);
                break;
            case "機場快綫":
                returnLocationList = getResources().getStringArray(R.array.aelt);
                break;
            case "東涌綫":
                returnLocationList = getResources().getStringArray(R.array.tclt);
                break;
            case "迪士尼綫":
                returnLocationList = getResources().getStringArray(R.array.drlt);
                break;
            case "馬鞍山綫":
                returnLocationList = getResources().getStringArray(R.array.molt);
                break;
            case "西鐵綫":
                returnLocationList = getResources().getStringArray(R.array.wrlt);
                break;
            case "東鐵綫":
                returnLocationList = getResources().getStringArray(R.array.erlt);
                break;
            default:
                returnLocationList = new String[]{seller_location};
        }
        return returnLocationList;
    }

    private void confirmSendMessage() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Trade_Activity.this);
        dialogBuilder.setMessage("用户所提交之交易資料將不能再被更改，請確認所填寫之交易資料準確無誤");
        dialogBuilder.setTitle("提示");
        dialogBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new sendTradeDetail().execute();
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

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("請確認你已經選擇交易時間和日期");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void successMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("交易已被確認");
        dialogBuilder.show();
    }

    @TargetApi(19)
    private void setNotification(){
        Date mDate = null;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.putExtra("seller", gadget.getSeller());
        notificationIntent.putExtra("time", time);
        notificationIntent.putExtra("date", date);
        notificationIntent.putExtra("location", buyer_location);
        notificationIntent.putExtra("product",product);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        try{
            simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
             mDate = simpleDateFormat.parse(date);
        }catch(Exception e){ e.printStackTrace();}

        Calendar cal = Calendar.getInstance();
 //       cal.add(Calendar.SECOND, 30);
        cal.setTime(mDate);
        Log.i(TAG, mDate.toString());
 //       cal.set(mDate.getYear(), mDate.getMonth(), mDate.getDay());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }
    public class sendNotification extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("seller", seller)
                    .appendQueryParameter("time", time)
                    .appendQueryParameter("date", buyer_date)
                    .appendQueryParameter("notiDate", date)
                    .appendQueryParameter("product", product)
                    .appendQueryParameter("location", buyer_location);
            Log.i(TAG, seller);
            String query = builder.build().getEncodedQuery();
            getResponseFromServer("pushNotification", query);
            return null;
        }
    }
    public class sendTradeDetail extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("date", buyer_date)
                    .appendQueryParameter("time", buyer_time)
                    .appendQueryParameter("c_location", buyer_location)
                    .appendQueryParameter("product_id", String.valueOf(product_id))
                    .appendQueryParameter("availability", "已被預訂")
                    .appendQueryParameter("buyer_phone", userLocalStore.getLoggedInUser().getPhone())
                    .appendQueryParameter("buyer", (userLocalStore.getLoggedInUser().getUsername()));
            String query = builder.build().getEncodedQuery();
            getResponseFromServer("tradeDetail", query);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            successMessage();
//            refreshLocalStore.setTradeDetail(new userGadget(gadget.getBrand() + " " + gadget.getModel(),gadget.getSeller(), buyer_time, buyer_date, buyer_location));
            new sendNotification().execute();
            setNotification();
            finish();
        }

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

}

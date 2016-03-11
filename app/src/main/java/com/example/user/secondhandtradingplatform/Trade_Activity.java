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

import android.support.v4.content.res.TypedArrayUtils;
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
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import RealmModel.RealmGadget;
import RealmQuery.QueryCamera;
import activity.Main;
import adapter.ProductAdapter;
import io.realm.Realm;
import product.Product;
import user.RefreshLocalStore;
import user.UserLocalStore;
import user.userGadget;

public class Trade_Activity extends AppCompatActivity implements DatePickerDialogFragment.OnDatePickedListener, TimePickerDialogFragment.OnTimePickedListener {
    Button mDateButton, mTimeButton, confirmBtn, cancelBtn;
    Boolean Pattern1Check;
    Boolean Pattern2Check = false;
    Boolean TimePattern1Check = false;
    Boolean TimePattern2Check = false;
    Boolean havePattern2 = false;
    ViewPager mPager;
    PagerAdapter mPagerAdapter;
    RealmGadget gadget;
    Date timeStart, timeEnd, timeStart2, timeEnd2, pickedTime;
    Calendar calendar;
    int DayOfWeek;
    List<String> images = new ArrayList<>();
    HashMap<Integer, Date[]> DatePattern1List = new HashMap<>();
    HashMap<Integer, Date[]> DatePattern2List = new HashMap<>();
    String image, image1, seller_location, buyer_time, buyer_date, buyer_location, time, date, seller, product;
    int product_id, position;
    // First set of information
    TextView tvDate, tvTime, tvLocation, tvPrice;
    //Second set of information
    TextView datetv;
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
    private static final String DATE = "Date";
    private static final String TIME = "Time";
    private static final String DATE_ERROR = "DateError";
    private static final String TIME_ERROR = "TimeError";
    private static final String EMPTY_ERROR = "EmptyError";

    SimpleDateFormat simpleDateFormat;

    public interface OnDataChangedListener{
        void update();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        product_id = getIntent().getIntExtra("id", 0);
        position = getIntent().getIntExtra("position", 0);
        userLocalStore = new UserLocalStore(this);
        refreshLocalStore = new RefreshLocalStore(this);

        //Get the product for trade by making a query using product_id
        QueryCamera query = new QueryCamera(this);
        gadget = query.retrieveGadgetById(product_id);
        product = gadget.getBrand() + " " + gadget.getModel();
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
            //Display the seller date pattern
            tvDate = (TextView) findViewById(R.id.tvDate);
            tvDate.setText(gadget.getSeller_date());

            // Parse the SQL Time format
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            timeStart = simpleDateFormat.parse(gadget.getSeller_time_start());
            // Get the time format we want
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeStringStart = simpleDateFormat.format(timeStart);

            // Parse the SQL Time format
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            timeEnd = simpleDateFormat.parse(gadget.getSeller_time_end());
            // Get the time format we want
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeStringEnd = simpleDateFormat.format(timeEnd);
            //Display the seller time
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
                Log.i(TAG, "buyer_time OnClicked"+buyer_time);
                Log.i(TAG, "buyer_date OnClicked"+buyer_date);
                if (buyer_time != null && buyer_date != null) {
                    confirmSendMessage();
                } else {
                    showErrorMessage(EMPTY_ERROR);
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
        if (!gadget.getSeller_location_2().equals("")) {
            addView();
        }

        //Convert the seller dates into a list of integer that will be used for comparison and checking later on
        DatePattern1List = GetDatePatternList(gadget.getSeller_date(), 1);
        Log.i(TAG, "DatePattern1List:" + DatePattern1List.toString());

    }

    private void addView() {
        havePattern2 = true;
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.seller_trade_info, null);
        final LinearLayout ll = (LinearLayout) findViewById(R.id.sellerLinearLayout);
        ll.addView(addView);
        datetv = (TextView) ll.findViewById(R.id.tvDateAdd);
        TextView time = (TextView) ll.findViewById(R.id.tvTimeAdd);
        TextView location = (TextView) ll.findViewById(R.id.tvLocationAdd);
        location.setText(gadget.getSeller_location_2());
        try { //Format the date and time obtained from the database
            //Set seller date 2
            datetv.setText(gadget.getSeller_date_2());

            //Convert from SQL time start format to time format
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            timeStart2 = simpleDateFormat.parse(gadget.getSeller_time_start_2());
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeStringStart = simpleDateFormat.format(timeStart2);

            //Convert from SQL time end format to time format
            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            timeEnd2 = simpleDateFormat.parse(gadget.getSeller_time_end_2());
            simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeStringEnd = simpleDateFormat.format(timeEnd2);

            //Set seller time
            time.setText(timeStringStart + " - " + timeStringEnd);

            DatePattern2List = GetDatePatternList(gadget.getSeller_date_2(), 2);
            Log.i(TAG, "DatePattern2List:" + DatePattern2List.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<Integer, Date[]> GetDatePatternList(String dates, int pattern) {
        HashMap<Integer, Date[]> DateTimeMap = new HashMap<>();
        switch (pattern) {
            case 1:
                Date[] sellerTimeArray = {timeStart, timeEnd};
                if (dates.contains("一")) {
                    DateTimeMap.put(2, sellerTimeArray);
                }
                if (dates.contains("二")) {
                    DateTimeMap.put(3, sellerTimeArray);
                }
                if (dates.contains("三")) {
                    DateTimeMap.put(4, sellerTimeArray);
                }
                if (dates.contains("四")) {
                    DateTimeMap.put(5, sellerTimeArray);
                }
                if (dates.contains("五")) {
                    DateTimeMap.put(6, sellerTimeArray);
                }
                if (dates.contains("六")) {
                    DateTimeMap.put(7, sellerTimeArray);
                }
                if (dates.contains("日")) {
                    DateTimeMap.put(1, sellerTimeArray);
                }
                break;
            case 2:
                Date[] sellerTimeArray2 = {timeStart2, timeEnd2};
                Log.i(TAG, "timeStart2: " + timeStart2.toString());
                Log.i(TAG, "timeEnd2: " + timeEnd2.toString());
                if (dates.contains("一")) {
                    DateTimeMap.put(2, sellerTimeArray2);
                }
                if (dates.contains("二")) {
                    DateTimeMap.put(3, sellerTimeArray2);
                }
                if (dates.contains("三")) {
                    DateTimeMap.put(4, sellerTimeArray2);
                }
                if (dates.contains("四")) {
                    DateTimeMap.put(5, sellerTimeArray2);
                }
                if (dates.contains("五")) {
                    DateTimeMap.put(6, sellerTimeArray2);
                }
                if (dates.contains("六")) {
                    DateTimeMap.put(7, sellerTimeArray2);
                }
                if (dates.contains("日")) {
                    DateTimeMap.put(1, sellerTimeArray2);
                }
                break;
        }
        return DateTimeMap;
    }

    private boolean DayOfWeekCheck(int day, HashMap<Integer, Date[]> datePatternList, Date pickedTime) {
        if (datePatternList.get(day) != null) {
            Log.i(TAG, "DayOfWeekCheckResult: True");
            if (pickedTime != null) { //Check whether Time Picked lies in the Range
                if(mTimeButton.getText().toString().equals("")){
                    return true;
                }
                return TimeCheck(day, datePatternList, pickedTime);
            } else { // When only Date is chosen
                return true;
            }
        } else {

            Log.i(TAG, "DayOfWeekCheckResult: False");
            return false;
        }

    }

    private boolean TimeCheck(int day, HashMap<Integer, Date[]> datePatternList, Date pickedTime) {
        if (datePatternList.get(day) != null) { //If date already chosen
/*           Log.i(TAG, "TimeCheck-day: "+day);
           Log.i(TAG, "TimeCheck-pickedTime: "+pickedTime.toString());
           Log.i(TAG, "datePatternList: "+datePatternList.toString());
           Log.i(TAG, "Date Array Content" + datePatternList.get(day).toString());
           Log.i(TAG, "Date Array: "+datePatternList.get(day)[0].toString());
           Log.i(TAG, "Date Array: " + datePatternList.get(day)[1].toString());*/
            Date start = datePatternList.get(day)[0];
            Date end = datePatternList.get(day)[1];
            calendar = Calendar.getInstance();
            calendar.setTime(start);
            int startHour = calendar.get(Calendar.HOUR_OF_DAY);
            int startMinute = calendar.get(Calendar.MINUTE);
            calendar.setTime(end);
            int endHour = calendar.get(Calendar.HOUR_OF_DAY);
            int endMinute = calendar.get(Calendar.MINUTE);
            calendar.setTime(pickedTime);
            int pickedHour = calendar.get(Calendar.HOUR_OF_DAY);
            int pickedMinute = calendar.get(Calendar.MINUTE);
            //          Log.i(TAG, "Start: " + startHour + " "+"End: " + endHour + " "+"Picked: " + pickedHour);
            //          Log.i(TAG, "Start: " + startMinute + " "+"End: " + endMinute + " "+"Picked: " + pickedMinute);
            if (!(pickedHour < startHour) && !(pickedHour > endHour)) {// If the picked time lies between start and end, return true
                if (startHour == pickedHour && startHour != endHour) {
                    //                   Log.i(TAG, "Case startHour == pickedHour");
                    if (pickedMinute >= startMinute) {
                        return true;
                    }
                } else if (endHour == pickedHour && startHour != endHour) {
//                    Log.i(TAG, "Case endHour == pickedHour");
                    if (pickedMinute <= endMinute) {
                        return true;
                    }
                } else if (startHour == endHour) {
                    //                  Log.i(TAG, "Case startHour == endHour");
                    if (pickedMinute >= startMinute && pickedMinute <= endMinute) {
                        return true;
                    }
                } else {
                    return true;
                }
            } else { //The picked time is out of range
                return false;
            }
        } else { // Only Time is chosen
            return false;
        }
        return false;
    }

    private void ValidateInfo(String checkType, Object data) {
        if (checkType.equals(DATE)) {
            Pattern1Check = DayOfWeekCheck((int) data, DatePattern1List, pickedTime);
            Log.i(TAG, "Pattern1Check: " + Pattern1Check);
            if (havePattern2) {   //Validate Date Picked against Pattern2 if exist
                Pattern2Check = DayOfWeekCheck((int) data, DatePattern2List, pickedTime);
                Log.i(TAG, "Pattern2Check: " + Pattern2Check);
            }
            if ((Pattern1Check == false) && (Pattern2Check == false)) { //User has selected a wrong time date combination
                showErrorMessage(DATE_ERROR);
                mDateButton.setText("");
            }
            changeLocation();

        } else if (checkType.equals(TIME)) {
            TimePattern1Check = TimeCheck(DayOfWeek, DatePattern1List, (Date) data);
            Log.i(TAG, "TIME-Pattern1Check: " + Pattern1Check);
            if (havePattern2) {
                TimePattern2Check = TimeCheck(DayOfWeek, DatePattern2List, (Date) data);
                Log.i(TAG, "TIME-Pattern2Check: " + Pattern2Check);
            }
            if ((TimePattern1Check == false) && (TimePattern2Check == false)) {
                showErrorMessage(TIME_ERROR);
                mTimeButton.setText("");
            }ValidateInfo(DATE, DayOfWeek);
            changeLocation();
        }
    }

    private void changeLocation() {
        if (Pattern1Check == true && Pattern2Check == false) {
            Log.i(TAG, "Case P1 TRUE");
            ArrayAdapter locationAdapter = new ArrayAdapter(this, R.layout.myspinner, getLocationList(gadget.getSeller_location()));
            locationSpinner.setAdapter(locationAdapter);
        } else if (Pattern1Check == false && Pattern2Check == true) {
            Log.i(TAG, "Case P2 TRUE");
            ArrayAdapter locationAdapter = new ArrayAdapter(this, R.layout.myspinner, getLocationList(gadget.getSeller_location_2()));
            locationSpinner.setAdapter(locationAdapter);
        } else if (Pattern1Check == true && Pattern2Check == true) {
            Log.i(TAG, "Case P1 & P2 TRUE");
            if (TimePattern1Check == true && TimePattern2Check == false) {
                Log.i(TAG, "Case T1 TRUE");
                ArrayAdapter locationAdapter = new ArrayAdapter(this, R.layout.myspinner, getLocationList(gadget.getSeller_location()));
                locationSpinner.setAdapter(locationAdapter);
            } else if (TimePattern2Check == true && TimePattern1Check == false) {
                Log.i(TAG, "Case T2 TRUE");
                ArrayAdapter locationAdapter = new ArrayAdapter(this, R.layout.myspinner, getLocationList(gadget.getSeller_location_2()));
                locationSpinner.setAdapter(locationAdapter);
            }
        }
    }

    @Override
    public void OnDatePicked(String msg) {
        mDateButton.setText(msg);
        try {
            date = msg;
            simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date date = simpleDateFormat.parse(msg);
            // Check the validity of Date Picked
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            DayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            Log.i(TAG, "Day of Week 1: " + DayOfWeek);
            //Validate the Date Picked by buyer
            ValidateInfo(DATE, DayOfWeek);

            simpleDateFormat = new SimpleDateFormat(SQL_DATE_FORMAT);
            String dateString = simpleDateFormat.format(date);
            buyer_date = dateString;
            Log.i(TAG, "buyer_date OnPicked"+buyer_date);
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
            pickedTime = simpleDateFormat.parse(msg);
            ValidateInfo(TIME, pickedTime);

            simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
            String timeString = simpleDateFormat.format(pickedTime);
            buyer_time = timeString;
            Log.i(TAG, "buyer_time OnPicked"+buyer_time);
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

    private void showErrorMessage(String type) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        if (type.equals(EMPTY_ERROR)) {
            dialogBuilder.setMessage("請確認你已經選擇交易時間和日期");
        } else if (type.equals(DATE_ERROR)) {
            dialogBuilder.setMessage("這不是有效的交易日子");
        } else if (type.equals(TIME_ERROR)) {
            dialogBuilder.setMessage("這不是有效的交易時間");
        }
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void successMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("交易已被確認");
        dialogBuilder.show();
    }

    @TargetApi(19)
    private void setNotification() {
        Date mDate = null;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.putExtra("seller", gadget.getSeller());
        notificationIntent.putExtra("time", time);
        notificationIntent.putExtra("date", date);
        notificationIntent.putExtra("location", buyer_location);
        notificationIntent.putExtra("product", product);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            mDate = simpleDateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        //       cal.add(Calendar.SECOND, 30);
        cal.setTime(mDate);
        Log.i(TAG, mDate.toString());
        //       cal.set(mDate.getYear(), mDate.getMonth(), mDate.getDay());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }

    public class sendNotification extends AsyncTask<Void, Void, Void> {

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
        String response;
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
            response = getResponseFromServer("tradeDetail", query);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            successMessage();
//            refreshLocalStore.setTradeDetail(new userGadget(gadget.getBrand() + " " + gadget.getModel(),gadget.getSeller(), buyer_time, buyer_date, buyer_location));
            new sendNotification().execute();
            if(response.contains("Success"));{
                updateLocalDB();
                setNotification();
                startActivity(new Intent(getApplicationContext(), Main.class));
                finish();
            }
        }

    }
    private void updateLocalDB(){
        Realm realm = Realm.getInstance(this);
        RealmGadget toEdit = realm.where(RealmGadget.class).equalTo("product_id", product_id).findFirst();
        realm.beginTransaction();
        toEdit.setAvailability("已被預訂");
        toEdit.setBuyer(userLocalStore.getLoggedInUser().getUsername());
        toEdit.setBuyer_phone(userLocalStore.getLoggedInUser().getPhone());
        toEdit.setBuyer_location(buyer_location);
        toEdit.setTrade_date(buyer_date);
        toEdit.setTrade_time(buyer_time);
        realm.commitTransaction();
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

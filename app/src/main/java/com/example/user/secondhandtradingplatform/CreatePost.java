package com.example.user.secondhandtradingplatform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import server.ServerRequests;
import user.UserLocalStore;

public class CreatePost extends AppCompatActivity implements View.OnClickListener, TimePickerDialogFragment.OnTimePickedListener, AdapterView.OnItemSelectedListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAMERA = 10;
    private static final int RESULT_LOAD_IMAGE2 = 2;
    private static final int REQUEST_CAMERA2 = 20;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String SQL_TIME_FORMAT = "HH:mm:ss";
    private static final String TAG = "CreatePostActivity";
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";
    SimpleDateFormat simpleDateFormat;
    ImageView imageToUpload, imageToUpload2;
    boolean canAddCheckBoxes = true;
    UserLocalStore userLocalStore;
    Bitmap image1, image2;
    String timeSelectFrom, type, brand, model, warranty, color, scratch, price, timeStart,
            timeEnd, seller_location, datePattern, seller;
    ImageButton addCameraBtn, addGalleryBtn, addCameraBtn2, addGalleryBtn2;
    CheckBox Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
    Spinner productBrand, productModel, productType, locationSpinner, scratchSpinner, colorSpinner;
    EditText gPrice;
    RadioGroup rgroup;
    RadioButton yesBtn, noBtn;
    Button mTimeStartButton, mTimeEndButton, mTimeStartButtonNew, mTimeEndButtonNew, addViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userLocalStore = new UserLocalStore(this);
        seller = userLocalStore.getLoggedInUser().getUsername();
        //Initialize Views
        productBrand = (Spinner) findViewById(R.id.productBrand);
        productModel = (Spinner) findViewById(R.id.productModel);
        productType = (Spinner) findViewById(R.id.productType);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        scratchSpinner = (Spinner) findViewById(R.id.scratchSpinner);
        gPrice = (EditText) findViewById(R.id.gPrice);
        rgroup = (RadioGroup) findViewById(R.id.rgroup);
        yesBtn = (RadioButton) findViewById(R.id.yesButton);
        noBtn = (RadioButton) findViewById(R.id.noButton);
        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        imageToUpload2 = (ImageView) findViewById(R.id.imageToUpload2);
        mTimeStartButton = (Button) findViewById(R.id.mTimeStartButton);
        mTimeEndButton = (Button) findViewById(R.id.mTimeEndButton);
        addViewBtn = (Button) findViewById(R.id.addViewBtn);
        Monday = (CheckBox) findViewById(R.id.Monday);
        Tuesday = (CheckBox) findViewById(R.id.Tuesday);
        Wednesday = (CheckBox) findViewById(R.id.Wednesday);
        Thursday = (CheckBox) findViewById(R.id.Thursday);
        Friday = (CheckBox) findViewById(R.id.Friday);
        Saturday = (CheckBox) findViewById(R.id.Saturday);
        Sunday = (CheckBox) findViewById(R.id.Sunday);
        addCameraBtn = (ImageButton) findViewById(R.id.addCameraBtn);
        addCameraBtn2 = (ImageButton) findViewById(R.id.addCameraBtn2);
        addGalleryBtn = (ImageButton) findViewById(R.id.addGalleryBtn);
        addGalleryBtn2 = (ImageButton) findViewById(R.id.addGalleryBtn2);
        // Set Listeners
        addCameraBtn.setOnClickListener(this);
        addCameraBtn2.setOnClickListener(this);
        addGalleryBtn.setOnClickListener(this);
        addGalleryBtn2.setOnClickListener(this);
        mTimeStartButton.setOnClickListener(this);
        mTimeEndButton.setOnClickListener(this);
        rgroup.setOnCheckedChangeListener(listener);
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
        addViewBtn.setOnClickListener(this);
        productType.setOnItemSelectedListener(this);
        productBrand.setOnItemSelectedListener(this);
        productModel.setOnItemSelectedListener(this);
        locationSpinner.setOnItemSelectedListener(this);
        colorSpinner.setOnItemSelectedListener(this);
        scratchSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCameraBtn:
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
                break;
            case R.id.addCameraBtn2:
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA2);
                break;
            case R.id.addGalleryBtn:
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "Select Picture"), RESULT_LOAD_IMAGE);
                break;
            case R.id.addGalleryBtn2:
                startActivityForResult(Intent.createChooser(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "Select Picture"), RESULT_LOAD_IMAGE2);
                break;
            case R.id.mTimeStartButton:
                timeSelectFrom = "start";
                new TimePickerDialogFragment().show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.mTimeEndButton:
                timeSelectFrom = "end";
                new TimePickerDialogFragment().show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.mTimeStartButtonNew:
                timeSelectFrom = "newStart";
                new TimePickerDialogFragment().show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.mTimeEndButtonNew:
                timeSelectFrom = "newEnd";
                new TimePickerDialogFragment().show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.addViewBtn:
                addDatePatternOption();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            // Image captured
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageToUpload.setImageBitmap(bitmap);
            image1 = bitmap;
        }
        if (requestCode == REQUEST_CAMERA2 && resultCode == RESULT_OK) {
            //Image captured
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageToUpload2.setImageBitmap(bitmap);
            image2 = bitmap;
        } // Get image from Gallery
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Thumbnail image shown in imageview
                Bitmap minibm = ThumbnailUtils.extractThumbnail(bitmap, 680, 480);
                imageToUpload.setImageBitmap(minibm);
                image1 = minibm;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap minibm = ThumbnailUtils.extractThumbnail(bitmap, 680, 480);
                imageToUpload2.setImageBitmap(minibm);
                image2 = minibm;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.yesButton:
                    warranty = getString(R.string.yes);
                    Log.i(TAG, warranty);
                    break;
                case R.id.noButton:
                    warranty = getString(R.string.no);
                    Log.i(TAG, warranty);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                // User chose the "Confirm" item, show the app settings UI...
                //              Toast.makeText(getApplicationContext(), "Confirm Button Clicked", Toast.LENGTH_SHORT).show();
                datePattern = getCheckBoxValue();
                price = gPrice.getText().toString();
               ServerRequests serverRequests = new ServerRequests(this);
               serverRequests.storeTradeDataInBackground(type, brand, model, warranty, color, scratch, price, timeStart,
                        timeEnd, image1, image2, seller_location, datePattern, seller);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    private String getCheckBoxValue(){
        String dates = null;
        if(Monday.isChecked()){dates = (dates == null) ? getString(R.string.MON) : (dates + ", " + getString(R.string.MON));}
        if(Tuesday.isChecked()){dates = (dates == null) ? getString(R.string.TUE) : (dates + ", " + getString(R.string.TUE));}
        if(Wednesday.isChecked()){dates = (dates == null) ? getString(R.string.WED) : (dates + ", " + getString(R.string.WED));}
        if(Thursday.isChecked()){dates = (dates == null) ? getString(R.string.THU) : (dates + ", " + getString(R.string.THU));}
        if(Friday.isChecked()){dates = (dates == null) ? getString(R.string.FRI) : (dates + ", " + getString(R.string.FRI));}
        if(Saturday.isChecked()){dates = (dates == null) ? getString(R.string.SAT) : (dates + ", " + getString(R.string.SAT));}
        if(Sunday.isChecked()){dates = (dates == null) ? getString(R.string.SUN) : (dates + ", " + getString(R.string.SUN));}
        return dates;
    }

    private void addDatePatternOption(){
        // Handle dynamically added views
        if(canAddCheckBoxes){
            LayoutInflater layoutInflater =
                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.check_box_rows, null);
            final LinearLayout ll = (LinearLayout) findViewById(R.id.mainLayout);
            ll.addView(addView);
            addViewBtn.setVisibility(View.GONE);
            canAddCheckBoxes = false;
            LinearLayout newLayout = (LinearLayout) findViewById(R.id.newLinearLayout);
            mTimeStartButtonNew = (Button) newLayout.getChildAt(3);
            mTimeStartButtonNew.setOnClickListener(this);
            mTimeEndButtonNew = (Button) newLayout.getChildAt(5);
            mTimeEndButtonNew.setOnClickListener(this);
            final Button delBtn = (Button) findViewById(R.id.delViewBtn);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll.removeView(addView);
                    delBtn.setVisibility(View.GONE);
                    addViewBtn.setVisibility(View.VISIBLE);
                    canAddCheckBoxes = true;
                }
            });
            Spinner spinner = (Spinner) newLayout.getChildAt(7);
            spinner.setOnItemSelectedListener(this);
            Log.i(TAG, newLayout.getChildCount() + "");
        }
    }
    @Override
    public void OnTimePicked(String msg) {
       switch(timeSelectFrom){
           case "start":
               mTimeStartButton.setText(msg);
               try {
                   simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
                   Date time = simpleDateFormat.parse(msg);
                   simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
                   timeStart = simpleDateFormat.format(time);
               } catch (Exception e) {
                   e.printStackTrace();
               }
               break;
           case "end":
               mTimeEndButton.setText(msg);
               try {
                   simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
                   Date time = simpleDateFormat.parse(msg);
                   simpleDateFormat = new SimpleDateFormat(SQL_TIME_FORMAT);
                   timeEnd = simpleDateFormat.format(time);
               } catch (Exception e) {
                   e.printStackTrace();
               }
               break;
           case "newStart":
               if(mTimeStartButtonNew!=null)
               {mTimeStartButtonNew.setText(msg);}
               break;
           case "newEnd":
               if(mTimeEndButtonNew!=null)
               {mTimeEndButtonNew.setText(msg);}
               break;
       }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.locationSpinner:
                seller_location = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "seller_location:" + seller_location);
                break;
            case R.id.productBrand:
                brand = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "brand:" + brand);
                break;
            case R.id.productType:
                type = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "type:" + type);
                break;
            case R.id.productModel:
                model = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "model:" + model);
                break;
            case R.id.colorSpinner:
                color = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "color:" + color);
                break;
            case R.id.scratchSpinner:
                scratch = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "scratch:" + scratch);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

package com.example.user.secondhandtradingplatform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class CreatePost extends AppCompatActivity implements View.OnClickListener, TimePickerDialogFragment.OnTimePickedListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAMERA = 10;
    private static final int RESULT_LOAD_IMAGE2 = 2;
    private static final int REQUEST_CAMERA2 = 20;
    private static final String TAG = "CreatePostActivity";
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";
    ImageView imageToUpload, imageToUpload2;
    String timeSelectFrom;
    ImageButton addCameraBtn, addGalleryBtn, addCameraBtn2, addGalleryBtn2;
    CheckBox Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
    Spinner productBrand, productModel, productType, locationSpinner;
    EditText gPrice;
    RadioGroup rgroup;
    RadioButton yesBtn, noBtn;
    Button mTimeStartButton, mTimeEndButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initialize Views
        productBrand = (Spinner) findViewById(R.id.productBrand);
        productModel = (Spinner) findViewById(R.id.productModel);
        productType = (Spinner) findViewById(R.id.productType);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        gPrice = (EditText) findViewById(R.id.gPrice);
        rgroup = (RadioGroup) findViewById(R.id.rgroup);
        yesBtn = (RadioButton) findViewById(R.id.yesButton);
        noBtn = (RadioButton) findViewById(R.id.noButton);
        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        imageToUpload2 = (ImageView) findViewById(R.id.imageToUpload2);
        mTimeStartButton = (Button) findViewById(R.id.mTimeStartButton);
        mTimeEndButton = (Button) findViewById(R.id.mTimeEndButton);
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
                break;
            case R.id.addGalleryBtn2:
                break;
            case R.id.mTimeStartButton:
                timeSelectFrom = "start";
                new TimePickerDialogFragment().show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.mTimeEndButton:
                timeSelectFrom = "end";
                new TimePickerDialogFragment().show(getSupportFragmentManager(), "timePicker");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            // Image captured
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageToUpload.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CAMERA2 && resultCode == RESULT_OK) {
            //Image captured
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageToUpload2.setImageBitmap(bitmap);
        }
    }

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
                Toast.makeText(getApplicationContext(), "Confirm Button Clicked", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void OnTimePicked(String msg) {
        if (timeSelectFrom.equals("start")) {
            mTimeStartButton.setText(msg);
        } else {
            mTimeEndButton.setText(msg);
        }
    }
}

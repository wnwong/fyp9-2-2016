package com.example.user.secondhandtradingplatform;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import activity.Main;
import server.GetUserCallback;
import server.ServerRequests;
import user.User;

public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button reg;
    EditText uname, pwd, email, phone;
    RadioButton male,female;
    RadioGroup rgroup;
    String gender, line;
    Spinner lineSpinner, locationSpinner;
    ArrayAdapter spinnerArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize EditText and Button
        reg = (Button) findViewById(R.id.regBtn);
        uname = (EditText) findViewById(R.id.r_uname);
        pwd = (EditText) findViewById(R.id.r_pwd);
        email = (EditText) findViewById(R.id.r_email);
        male = (RadioButton) findViewById(R.id.mButton);
        female = (RadioButton) findViewById(R.id.fButton);
        rgroup = (RadioGroup) findViewById(R.id.rgroup);
        phone = (EditText) findViewById(R.id.r_phone);
        lineSpinner = (Spinner) findViewById(R.id.lineSpinner);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        lineSpinner.setOnItemSelectedListener(this);
        line = lineSpinner.getSelectedItem().toString();

        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getLocationArray(line)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(spinnerArrayAdapter);

        reg.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        rgroup.setOnCheckedChangeListener(listener);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regBtn:
                //Toast.makeText(getApplicationContext(), "Register button is selected!", Toast.LENGTH_SHORT).show();
                String rUsername = uname.getText().toString();
                String rPassword = pwd.getText().toString();
                String rEmail = email.getText().toString();
                String rLocation = null;
                if(locationSpinner.getSelectedItem().toString().equals("全線")){
                    rLocation = lineSpinner.getSelectedItem().toString();
                }else{
                    rLocation = locationSpinner.getSelectedItem().toString();
                }
                String uphone = phone.getText().toString();
                if(isConnected(this) == true){
                    Toast.makeText(getApplicationContext(), "You have connected to the Internet!", Toast.LENGTH_SHORT).show();
                }
                User user = new User(rUsername, rPassword, rEmail, rLocation, gender, uphone);
                registerUser(user);
                break;
        }
    }

    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(this, Main.class);
        startActivity(myIntent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent myIntent = new Intent(this, Main.class);
                startActivity(myIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int p = group.indexOfChild((RadioButton) findViewById(checkedId));
            int count = group.getChildCount();
            switch (checkedId){
                case R.id.mButton:
                    gender = "male";
                    break;
                case R.id.fButton:
                    gender = "female";
                    break;
                }
            }
        };
    public boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private String[] getLocationArray(String value) {
        String[] result = null;
        switch (value) {
            // Set Brand From Type
            case "觀塘綫":
                result = getResources().getStringArray(R.array.ktl);
                break;
            case "荃灣綫":
                result = getResources().getStringArray(R.array.twl);
                break;
            case "港島綫":
                result = getResources().getStringArray(R.array.isl);
                break;
            case "將軍澳綫":
                result = getResources().getStringArray(R.array.tkl);
                break;
            case "機場快綫":
                result = getResources().getStringArray(R.array.ael);
                break;
            // Set Model From Brand
            case "東涌綫":
                result = getResources().getStringArray(R.array.tcl);
                break;
            case "迪士尼綫":
                result = getResources().getStringArray(R.array.drl);
                break;
            case "東鐵綫":
                result = getResources().getStringArray(R.array.erl);
                break;
            case "馬鞍山綫":
                result = getResources().getStringArray(R.array.mol);
                break;
            case "西鐵綫":
                result = getResources().getStringArray(R.array.wrl);
                break;

        }
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lineSpinner:
                line = lineSpinner.getSelectedItem().toString();
                spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getLocationArray(line));
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locationSpinner.setAdapter(spinnerArrayAdapter);
                locationSpinner.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

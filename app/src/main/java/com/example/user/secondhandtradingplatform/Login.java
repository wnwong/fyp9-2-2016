package com.example.user.secondhandtradingplatform;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pushbots.push.Pushbots;

import activity.Main;
import server.GetUserCallback;
import server.ServerRequests;
import user.User;
import user.UserLocalStore;

public class Login extends Activity implements View.OnClickListener {

    Button login, register;
    EditText uname, pwd;
    UserLocalStore userLocalStore;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(this, Main.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
 //       setSupportActionBar(toolbar);
 //       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize TextView and Button
        uname = (EditText) findViewById(R.id.input_uname);
        pwd = (EditText) findViewById(R.id.input_pwd);
        login = (Button) findViewById(R.id.login_Button);
        register = (Button) findViewById(R.id.register);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_Button:
              //  Toast.makeText(getApplicationContext(), "Login button is selected!", Toast.LENGTH_SHORT).show();
                String username = uname.getText().toString();
                String password = pwd.getText().toString();
                User user = new User (username, password);
                // Check if username and password are correct
                authenticate(user);
            //    Snackbar.make(v, "You are now logged in !", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.register:
                startActivity(new Intent(this, Register.class));
                finish();
                break;
        }
    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null){
                    showErrorMessage();
                }else{
                    logUserIn(returnedUser);
                    Toast.makeText(getApplicationContext(), "You are now logged in !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("username", returnedUser.getUsername());
        intent.putExtra("email", returnedUser.getEmail());
        Pushbots.sharedInstance().setAlias(returnedUser.getUsername());
        startActivity(intent);
        finish();
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}

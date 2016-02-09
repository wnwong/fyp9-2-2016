package com.example.user.secondhandtradingplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import product.Camera;

public class DetailPageActivity extends AppCompatActivity {
public static Camera camera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_displayinfo);
        TextView seller = (TextView) findViewById(R.id.textView2);
        TextView phonNo = (TextView) findViewById(R.id.textView3);
        TextView email = (TextView) findViewById(R.id.textView4);
        TextView warranty = (TextView) findViewById(R.id.textView5);
        TextView newPrice = (TextView) findViewById(R.id.textView6);
        TextView price = (TextView) findViewById(R.id.textView7);

        if (camera != null) {
//            TextView toolbar = (TextView) findViewById(R.id.textView);
            System.out.println(camera.price);
            System.out.println(camera.name);
            seller.setText(seller.getText() + " " + camera.seller);
            phonNo.setText(phonNo.getText() + " " + camera.phonNo);
            email.setText(email.getText() + " "  + camera.email);
            warranty.setText(warranty.getText() + " " + camera.warranty);
            newPrice.setText(newPrice.getText() + " " + camera.newPrice);
            price.setText(price.getText() + " " + camera.place);
        }
    }
}

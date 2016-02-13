package com.example.user.secondhandtradingplatform;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmGadget;
import RealmModel.RealmProduct;
import RealmQuery.QueryCamera;
import activity.Main;
import activity.tragePage;
import adapter.ProductAdapter;
import io.realm.RealmResults;

public class ProductInfo extends AppCompatActivity {
     List<RealmGadget> gadgets = new ArrayList<>();
     public static RealmProduct realmProduct;
     public static Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView rv;
        rv = (RecyclerView) findViewById(R.id.rview);
        rv.setHasFixedSize(true);
        //use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        QueryCamera queryCamera = new QueryCamera(this);
        // Retrieve posts of specific gadget
        RealmResults<RealmGadget> result = queryCamera.retrieveProductsByModel(realmProduct.getModel());
        for(int i=0; i<result.size(); i++){
            gadgets.add(result.get(i));
        }

        ProductAdapter adapter =
                new ProductAdapter(gadgets, realmProduct.getBrand() + " " + realmProduct.getModel(), realmProduct.getPrice(), realmProduct.getOs(), realmProduct.getMonitor(), realmProduct.getCamera(), realmProduct.getPath());
        rv.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    System.out.println("get");
                    mHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            switch(msg.what){
                    case 1:
                     System.out.println("get from info page");
                        Integer id = (Integer) msg.obj;
                        Intent intent = new Intent(getApplicationContext(), Trade_Activity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        break;
                }
            }
        };  // pass the object date to detailPage
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Main.class));
    }
}
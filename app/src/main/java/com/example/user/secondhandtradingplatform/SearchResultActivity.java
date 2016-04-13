package com.example.user.secondhandtradingplatform;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmGadget;
import RealmModel.RealmProduct;
import adapter.RVAdapter;
import adapter.SearchAdapter;

public class SearchResultActivity extends AppCompatActivity {
    public static Handler mHandler;
    public static List<RealmProduct> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rv;
        rv = (RecyclerView) findViewById(R.id.rview);
        rv.setHasFixedSize(true);

        //use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        SearchAdapter adapter = new SearchAdapter(results, R.layout.cardview_search, this);
        rv.setAdapter(adapter);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Integer position = (Integer) msg.obj;
                        RealmProduct obj = results.get(position);
                        System.out.println(obj.getModel());
                        ProductInfo.realmProduct = obj;
                        Intent intent = new Intent(getApplicationContext(), ProductInfo.class);
                        startActivity(intent);
                        break;
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

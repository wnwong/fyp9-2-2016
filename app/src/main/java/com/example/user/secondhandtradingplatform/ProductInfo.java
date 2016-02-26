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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import RealmModel.RealmGadget;
import RealmModel.RealmProduct;
import RealmQuery.QueryCamera;
import activity.Main;
import activity.tragePage;
import adapter.ProductAdapter;
import io.realm.RealmResults;

public class ProductInfo extends AppCompatActivity{
    private static String TAG = "ProductInfo";
    List<RealmGadget> gadgets = new ArrayList<>();
    public static RealmProduct realmProduct;
    public static Handler mHandler;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        relativeLayout = (RelativeLayout) findViewById(R.id.container_body);
        RecyclerView rv;
        rv = (RecyclerView) findViewById(R.id.rview);
        rv.setHasFixedSize(true);
        //use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        QueryCamera queryCamera = new QueryCamera(this);
        // Retrieve posts of specific gadget
        RealmResults<RealmGadget> result = queryCamera.retrieveProductsByModel(realmProduct.getModel());
        for (int i = 0; i < result.size(); i++) {
            gadgets.add(result.get(i));
        }

        ProductAdapter adapter =
                new ProductAdapter(gadgets, realmProduct.getBrand() + " " + realmProduct.getModel(), realmProduct.getPrice(), realmProduct.getOs(), realmProduct.getMonitor(), realmProduct.getCamera(), realmProduct.getPath(), this);
        rv.setAdapter(adapter);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        System.out.println("get from info page");
                        Integer id = (Integer) msg.obj;
                        Intent intent = new Intent(getApplicationContext(), Trade_Activity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        break;
                    case 3:
                        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.graph, null);
                        GraphView graph = (GraphView) container.findViewById(R.id.graph);
                        graph.setFocusable(true);
                        graph.setTitle("過去三個月之平均成交價");
                        graph.setTitleTextSize(24);
                        //Plotting the graph
                        Calendar calendar = Calendar.getInstance();
                        int month1 = calendar.get(Calendar.MONTH)+1;
                        calendar.add(Calendar.MONTH, 1);
                        int month2 = calendar.get(Calendar.MONTH)+1;
                        calendar.add(Calendar.MONTH, 1);
                        int month3 = calendar.get(Calendar.MONTH)+1;
                        calendar.add(Calendar.MONTH, 1);
                        int month4 = calendar.get(Calendar.MONTH)+1;
                        calendar.add(Calendar.MONTH, 1);
                        int month5 = calendar.get(Calendar.MONTH)+1;
                        calendar.add(Calendar.MONTH, 1);
                        int month6 = calendar.get(Calendar.MONTH)+1;
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(month1, 4000),
                                new DataPoint(month2, 3800),
                                new DataPoint(month3, 3000),
                                new DataPoint(month4, 2400),
                        });
                        graph.addSeries(series);
                        series.setDrawBackground(true);
                        series.setDrawDataPoints(true);

                        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
                        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                            @Override
                            public String formatLabel(double value, boolean isValueX) {
                                if (isValueX) {
                                    // show normal x values
                                    return super.formatLabel(value, isValueX) + "月";
                                } else {
                                    // show currency for y values
                                    return"$" + value;
                                }
                            }
                        });
                        graph.getViewport().setXAxisBoundsManual(true);
                        // Create a Popup Window for Showing graph
                        popupWindow = new PopupWindow(container);
                        popupWindow.setFocusable(true);
                        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        popupWindow.showAtLocation(relativeLayout, Gravity.CENTER_HORIZONTAL,200,0);

                        container.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                popupWindow.dismiss();
                                return true;
                            }
                        });
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
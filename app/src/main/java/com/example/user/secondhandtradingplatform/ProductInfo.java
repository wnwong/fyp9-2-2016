package com.example.user.secondhandtradingplatform;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import RealmModel.RealmGadget;
import RealmModel.RealmProduct;
import RealmQuery.QueryCamera;
import activity.Main;
import adapter.ProductAdapter;
import io.realm.RealmResults;
import user.PersonalDetailsFragment;

public class ProductInfo extends AppCompatActivity{
    private static String TAG = "ProductInfo";
    List<RealmGadget> gadgets = new ArrayList<>();
    public static RealmProduct realmProduct;
    public static Handler mHandler;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;
    private ViewGroup container;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                new ProductAdapter(gadgets, realmProduct.getBrand() + " " + realmProduct.getModel(), realmProduct.getPrice(), realmProduct.getOs(), realmProduct.getMonitor(), realmProduct.getCamera(), realmProduct.getPath(), realmProduct.getType(), this);
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
                        container = (ViewGroup) layoutInflater.inflate(R.layout.graph, null);

                        //Plot the graph and display on the popup window
                        plotGraph();

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
        };
    }

 /*   @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Main.class));
    }
*/
    private Void plotGraph(){

        GraphView graph = (GraphView) container.findViewById(R.id.graph);
        graph.setFocusable(true);
        graph.setTitle("過去六個月之平均成交價");
        graph.setTitleTextSize(24);
        //Plotting the graph
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -5);
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
                new DataPoint(0, 4000),
                new DataPoint(1, 3800),
                new DataPoint(2, 3000),
                new DataPoint(3, 2400),
                new DataPoint(4, 2600),
                new DataPoint(5, 2050),

        });
        graph.addSeries(series);
        series.setDrawDataPoints(true);
        series.setColor(getResources().getColor(R.color.colorPrimaryDark));
        //Customize Horizontal Axis Labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{getMonthString(month1), getMonthString(month2), getMonthString(month3),
                                                          getMonthString(month4), getMonthString(month5), getMonthString(month6)});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.setTitleColor(Color.GRAY);
        //Set appearance of the graph
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.GRAY);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.GRAY);
        graph.getGridLabelRenderer().setGridColor(Color.GRAY);
        graph.getGridLabelRenderer().reloadStyles();

        return null;
    }

    private String getMonthString(int month){
        String MonthString = null;
        switch (month){
            case 1:
                MonthString = "1月";
                break;
            case 2:
                MonthString = "2月";
                break;
            case 3:
                MonthString = "3月";
                break;
            case 4:
                MonthString = "4月";
                break;
            case 5:
                MonthString = "5月";
                break;
            case 6:
                MonthString = "6月";
                break;
            case 7:
                MonthString = "7月";
                break;
            case 8:
                MonthString = "8月";
                break;
            case 9:
                MonthString = "9月";
                break;
            case 10:
                MonthString = "10月";
                break;
            case 11:
                MonthString = "11月";
                break;
            case 12:
                MonthString = "12月";
                break;
        }
        return MonthString;
    }

}
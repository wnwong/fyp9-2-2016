package activity;

import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;

import com.example.user.secondhandtradingplatform.R;

import RealmModel.RealmProduct;
import RealmQuery.QueryCamera;
import io.realm.RealmResults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.*;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.io.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import server.ServerRequests;
import user.User;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.ExpandableListView;


public class tragePage extends AppCompatActivity {
    ExpandableListView locationExpandableListView;
    View expandedView;
    public static final String SERVER_ADDRESS = "http://php-etrading.rhcloud.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmtrade);
        Intent intent = getIntent();
        locationExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
//        expandedView = (View)findViewById(R.id.expandedView);
        String pname = (String) intent.getStringExtra("productionPosition");
        System.out.println("pname:" + pname);
        QueryCamera queryCamera = new QueryCamera(getApplicationContext());
        RealmResults<RealmProduct> result = queryCamera.searchProductsByModel(pname);


        tradeRequest request = new tradeRequest();
//        request.execute();
        ExpandableListView list = new ExpandableListView(this);
        list.setGroupIndicator(null);
        list.setChildIndicator(null);
        String[] titles = {"A","B","C"};
        String[] fruits = {"a1","a2","a3"};
        String[] veggies = {"b1","b2","b3"};
        String[] meats = {"c1","c2","c3"};
        String[][] contents = {fruits,veggies,meats};
        final SimplerExpandableListAdapter adapter = new SimplerExpandableListAdapter(this,
                titles, contents);

        locationExpandableListView.setAdapter(adapter);
        locationExpandableListView.setIndicatorBounds(0, 500);
        locationExpandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.ic_action_search));

        locationExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) adapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });
        locationExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                System.out.println("groupPosition" + groupPosition);
                return false;
            }
        });

    }




//        setContentView(list);
//        list.setAdapter(list);










class SimplerExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private String[][] mContents;
    private String[] mTitles;

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    public SimplerExpandableListAdapter(Context context, String[] titles, String[][] contents) {
        super();
        if (titles.length != contents.length) {
            throw new IllegalArgumentException("Titles and Contents must be the same size.");
        }

        mContext = context;
        mContents = contents;
        mTitles = titles;
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mContents[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = getLayoutInflater();
        View row;
        row = inflater.inflate(R.layout.trading_location_listview, parent, false);
        TextView location;
        CheckBox option ;

        location = (TextView) row.findViewById(R.id.location);
        option=(CheckBox)row.findViewById(R.id.option);
        location.setText("good");
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click in child box");
            }
        });




        return (row);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mContents[groupPosition].length;
    }

    @Override
    public String[] getGroup(int groupPosition) {
        return mContents[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return mContents.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

//        LayoutInflater inflater = getLayoutInflater();
//        View row;
//        row = inflater.inflate(R.layout.trading_location_listview, parent, false);
//        TextView location;
//        CheckBox option ;
//
//        location = (TextView) row.findViewById(R.id.location);
//        option=(CheckBox)row.findViewById(R.id.option);






            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.mtr_line_section,
                    null);

        TextView item = (TextView) convertView.findViewById(R.id.lineName);
        item.setTypeface(null, Typeface.BOLD);
        item.setText("laptopName");

        return convertView;

//        return row;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

    class tradeRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("Do in BackGround");
            HttpURLConnection con = null;
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("c_location", "location");
            dataToSend.put("date", "1000");
            dataToSend.put("time","1000");
            dataToSend.put("buyer","philip");

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;
            User returnedUser = null;
            System.out.println("before try");
            try {
                //Conerting address String to URL
                URL url = new URL(SERVER_ADDRESS + "tradeDetail.php");
                //Opening the connection
                con = (HttpURLConnection) url.openConnection();
                System.out.println("connection opened");
                //POST method
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoOutput(true);
                con.setDoInput(true);
                 Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("c_location", "tset")
                         .appendQueryParameter("date", "1000").
                                 appendQueryParameter("time", "1000").
                                 appendQueryParameter("buyer","phuko");
                 String query = builder.build().getEncodedQuery();


                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Write dataToSend to OutputStreamWriter

                Log.i("Writer Output", encodedStr);
                 writer.write(query);
                writer.flush();
                writer.close();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = reader.readLine())!= null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                System.out.println("e.getMessage()" + e.getMessage());
            } finally {
                if (con != null) {
                    con.disconnect();
                    System.out.println("disconnect");

                }

                return null;
            }
        }
    }

    private String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(sb.length()>0)
                sb.append("&");

            sb.append(key + "=" + value);
            System.out.println("sb" + sb);

        }
        return sb.toString();
    }


}



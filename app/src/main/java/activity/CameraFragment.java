package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.secondhandtradingplatform.DetailPageActivity;
import com.example.user.secondhandtradingplatform.ProductInfo;
import com.example.user.secondhandtradingplatform.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import RealmModel.RealmCamera;
import RealmModel.RealmProduct;
import RealmQuery.QueryCamera;
import RealmQuery.callBackFinishInsert;
import adapter.passToDetailPageListener;
import io.realm.RealmResults;
import product.Camera;
import adapter.RVAdapter;


public class CameraFragment extends Fragment{

    public static Context context;
    public static Handler mHandler;
    final String productType = "相機鏡頭";
    List<RealmProduct> products = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        RecyclerView rv;
        rv = (RecyclerView) v.findViewById(R.id.rview);
        rv.setHasFixedSize(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(productType);
        //use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        context = getContext();
        System.out.println(productType);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Integer position = (Integer) msg.obj;
                        RealmProduct obj = products.get(position);
                        ProductInfo.realmProduct = obj;

                        System.out.println(obj.getBrand() + " " + obj.getModel() + " " + obj.getMonitor());
                        Intent intent = new Intent(context, ProductInfo.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        QueryCamera queryCamera = new QueryCamera(getContext());

        RealmResults<RealmProduct> result = queryCamera.retrieveProductsByType(productType);

        for (int i = 0; i < result.size(); i++) {
            products.add(result.get(i));
        }
        RVAdapter adapter = new RVAdapter(products, R.layout.cardview, getContext(), productType);
        Log.i("Refresh", "" + products.size());
        rv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        System.out.println("Inside Camera Fragment");
        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
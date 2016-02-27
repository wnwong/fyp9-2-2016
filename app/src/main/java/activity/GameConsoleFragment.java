package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.secondhandtradingplatform.ProductInfo;
import com.example.user.secondhandtradingplatform.R;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmProduct;
import RealmQuery.QueryCamera;
import adapter.RVAdapter;
import io.realm.RealmResults;


public class GameConsoleFragment extends Fragment {
    public static Handler mHandler;
    final String productType = "電子遊戲機";
    List<RealmProduct> products = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        RecyclerView rv;
        rv = (RecyclerView) v.findViewById(R.id.gameRview);
        rv.setHasFixedSize(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(productType);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Integer position = (Integer) msg.obj;
                        RealmProduct obj = products.get(position);
                        ProductInfo.realmProduct = obj;
                        Intent intent = new Intent(getContext(), ProductInfo.class);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gameconsole, container, false);
        return rootView;
    }
}

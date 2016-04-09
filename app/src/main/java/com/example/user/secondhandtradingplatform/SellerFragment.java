package com.example.user.secondhandtradingplatform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmGadget;
import RealmQuery.QueryCamera;
import adapter.SellerAdapter;
import adapter.TradeHistoryAdapter;
import user.UserLocalStore;

/**
 * Created by timothy on 9/4/2016.
 */
public class SellerFragment extends Fragment {
    RecyclerView rv;
    QueryCamera queryCamera;
    List<RealmGadget> realmGadgets = new ArrayList<>();
    SellerAdapter adapter;
    TextView tv;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Profile.username+"的出售記錄");
        View v = getView();
        rv = (RecyclerView) v.findViewById(R.id.rview);
        rv.setHasFixedSize(true);
        tv = (TextView) v.findViewById(R.id.tv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        queryCamera = new QueryCamera(getContext());
        realmGadgets = queryCamera.retrieveCompletedGadget(Profile.username);
        adapter = new SellerAdapter(realmGadgets, getContext());
        rv.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller, container, false);
    }
}

package user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.secondhandtradingplatform.R;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmGadget;
import RealmQuery.QueryCamera;
import adapter.ProcessingTradeAdapter;
import adapter.TradeHistoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TradeHistoryFragment extends Fragment {
    List<RealmGadget> realmGadgets = new ArrayList<>();
    UserLocalStore userLocalStore;
    TextView tv;
    RecyclerView rv;

    public TradeHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        rv = (RecyclerView) v.findViewById(R.id.rview);
        rv.setHasFixedSize(true);
        tv = (TextView) v.findViewById(R.id.tv);
        userLocalStore = new UserLocalStore(getContext());

        //use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        QueryCamera queryCamera = new QueryCamera(getContext());
        realmGadgets = queryCamera.retrieveCompletedGadgetBySeller(userLocalStore.getLoggedInUser().getUsername());
       TradeHistoryAdapter adapter = new TradeHistoryAdapter(realmGadgets, getContext());
        rv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_history, container, false);
    }

}

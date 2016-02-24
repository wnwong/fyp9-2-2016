package user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.secondhandtradingplatform.R;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Message;

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
    public static Handler mHandler;
    public static String TAG = "TradeHistoryFragment";

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
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        QueryCamera queryCamera = new QueryCamera(getContext());
                        realmGadgets = queryCamera.retrieveCompletedGadgetBySeller(userLocalStore.getLoggedInUser().getUsername());
                      for(int i=0; i<realmGadgets.size(); i++){
                          Log.i(TAG, "QueryResult:" +realmGadgets.get(i).getSeller() + realmGadgets.get(i).getBuyer()
                                  + realmGadgets.get(i).getBuyer_location() + realmGadgets.get(i).getAvailability());
                      }
                        TradeHistoryAdapter adapter = new TradeHistoryAdapter(realmGadgets, getContext());
                        rv.setAdapter(adapter);
                        break;
                    default:
                        break;
                }
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_history, container, false);
    }

}

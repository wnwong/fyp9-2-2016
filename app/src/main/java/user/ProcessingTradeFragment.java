package user;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProcessingTradeFragment extends Fragment {
    List<RealmGadget> realmGadgets = new ArrayList<>();
    UserLocalStore userLocalStore;
    TextView tv;
    RecyclerView rv;
    public static Handler mHandler;

    public ProcessingTradeFragment() {
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
               switch (msg.what){
                   case 1:
                       QueryCamera queryCamera = new QueryCamera(getContext());
                       realmGadgets = queryCamera.retrieveProcessingGadgetBySeller(userLocalStore.getLoggedInUser().getUsername());
                       ProcessingTradeAdapter adapter = new ProcessingTradeAdapter(realmGadgets, getContext());
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
        return inflater.inflate(R.layout.fragment_processing_trade, container, false);
    }

}

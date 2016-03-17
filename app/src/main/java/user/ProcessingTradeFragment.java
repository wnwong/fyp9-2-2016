package user;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.secondhandtradingplatform.R;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmGadget;
import RealmQuery.QueryCamera;
import adapter.ProcessingTradeAdapter;
import io.realm.Realm;
import server.GetPostCallback;
import server.ServerRequests;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProcessingTradeFragment extends Fragment {
    private static String TAG = "ProcessingTradeFragment";
    private List<RealmGadget> realmGadgets = new ArrayList<>();
    UserLocalStore userLocalStore;
    TextView tv;
    RecyclerView rv;
    private ServerRequests serverRequests;
    public static Handler mHandler;
    private ProcessingTradeAdapter adapter;
    private QueryCamera queryCamera;

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
        queryCamera = new QueryCamera(getContext());

        //use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        mHandler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                switch (msg.what) {
                    case 1:
                        realmGadgets = queryCamera.retrieveProcessingGadgetBySeller(userLocalStore.getLoggedInUser().getUsername());
                        adapter = new ProcessingTradeAdapter(realmGadgets, getContext());
                        rv.setAdapter(adapter);
                        break;
                    case 2:
                        final float rating = (float) msg.obj;
                        final int position = msg.getData().getInt("position");
                        final int pid = realmGadgets.get(position).getProduct_id();
                        serverRequests = new ServerRequests(getContext());
                        serverRequests.storeRatingInBackground(rating, pid, new GetPostCallback() {
                            @Override
                            public void done() {

                            }

                            @Override
                            public void done(String response) {
                                if (response.contains("Success")) {
                                    Log.i(TAG, "Updating Loacal DB");
                                    Realm realm = Realm.getInstance(getContext());
                                    RealmGadget toEdit = realm.where(RealmGadget.class).equalTo("product_id", pid).findFirst();
                                    realm.beginTransaction();
                                    toEdit.setRating(Float.parseFloat(rating + ""));
                                    toEdit.setAvailability("已出售");
                                    realm.commitTransaction();
                                }
                                realmGadgets = queryCamera.retrieveProcessingGadgetBySeller(userLocalStore.getLoggedInUser().getUsername());
                                adapter.notifyDataSetChanged();
                                adapter.notifyItemRangeChanged(0, realmGadgets.size());
                                updateConfirmedTrade();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };

    }

    private void updateConfirmedTrade(){
        Message message = new Message();
        message.what = 2;
        TradeHistoryFragment.mHandler.sendMessage(message);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_processing_trade, container, false);
    }

}

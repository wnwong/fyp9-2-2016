package user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.secondhandtradingplatform.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TradeHistoryFragment extends Fragment {


    public TradeHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        RecyclerView rv;
        rv = (RecyclerView) v.findViewById(R.id.rview);
        rv.setHasFixedSize(true);

        //use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_history, container, false);
    }

}

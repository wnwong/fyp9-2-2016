package tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.secondhandtradingplatform.R;
import com.example.user.secondhandtradingplatform.addGadget;

public class add_location extends Fragment implements View.OnClickListener {
    Button confirm;
    String location;
    ListView menuList, detailList;
    ArrayAdapter<String> menuAdapter, detailAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
//        location = (EditText) v.findViewById(R.id.location);
        confirm = (Button) v.findViewById(R.id.confirmBtn);
//        confirm.setOnClickListener(this);
        menuList = (ListView) v.findViewById(R.id.menuList);
        detailList = (ListView) v.findViewById(R.id.detailList);
        menuAdapter = new ArrayAdapter<String>(getActivity(), R.layout.myspinner, getResources().getStringArray(R.array.locationMenu));
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        detailAdapter = new ArrayAdapter<String>(getActivity(), R.layout.myspinner, getResources().getStringArray(R.array.mtr));
                        detailList.setAdapter(detailAdapter);
                        break;
                    case 1:
                        detailAdapter = new ArrayAdapter<String>(getActivity(), R.layout.myspinner, getResources().getStringArray(R.array.uni));
                        detailList.setAdapter(detailAdapter);
                        break;
                    case 2:
                        detailAdapter = new ArrayAdapter<String>(getActivity(), R.layout.myspinner, getResources().getStringArray(R.array.hkIsland));
                        detailList.setAdapter(detailAdapter);
                        break;
                    case 3:
                        detailAdapter = new ArrayAdapter<String>(getActivity(), R.layout.myspinner, getResources().getStringArray(R.array.kowloon));
                        detailList.setAdapter(detailAdapter);
                        break;
                    case 4:
                        detailAdapter = new ArrayAdapter<String>(getActivity(), R.layout.myspinner, getResources().getStringArray(R.array.nt));
                        detailList.setAdapter(detailAdapter);
                        break;
                    case 5:
                        detailAdapter = new ArrayAdapter<String>(getActivity(), R.layout.myspinner, getResources().getStringArray(R.array.oIsland));
                        detailList.setAdapter(detailAdapter);
                        break;
                }
            }
        });
 /*       detailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                location = parent.getItemAtPosition(position).toString();
            }
        });
*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_addlocation, container, false);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
 /*           case R.id.confirmBtn:
                if(location != null)
                {
                    ((addGadget) getActivity()).getLocation(location);
                }
                break;
        }
*/
        }
    }
}

package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class CameraFragment extends Fragment implements passToDetailPageListener {

public static Context context;
   public static  Handler mHandler;
//    List<Camera> cameras;
  public static  List<RealmProduct> products = new ArrayList<>();
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

        context = getContext();
        System.out.println("run to here");

          mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        //處理少量資訊或UI
                   System.out.println("Form past page");
                        Integer position = (Integer)msg.obj;
                        RealmProduct obj = products.get(position);
                        ProductInfo.realmProduct = obj;

                        System.out.println(obj.getBrand() + " " + obj.getModel() + " " + obj.getMonitor());
//                        Object anyobject = obj;
                        Intent intent = new Intent(context, ProductInfo.class);

//                        Bundle extras = new Bundle();
//                        extras.se
                        startActivity(intent);


                        break;
                }
            }
        };  // pass the object date to detailPage

//        Intent intent = new Intent(this.getContext(), DetailPageActivity.class);
//        startActivity(intent);




/**********************************************************************************************/
  //testing
        Camera cam = new Camera("Canon PowerShot G7 X", "2500","Yes", "MongKok", R.mipmap.ic_g7, "philip", "1000","999","Gmail");
        Camera cam1 = new Camera("Canon EOS 7D Mark II", "4300", "Yes", "Causeway Bay", R.mipmap.ic_7d,"philip", "2000","999","Gmail");
        Camera cam2 = new Camera("Canon EOS 760D", "5000", "No", "HongKongIsland", R.mipmap.ic_760d,"philip", "3000","999","Gmail");
        cam.add(cam);
        cam1.add(cam1);
        cam2.add(cam2);
        QueryCamera queryCamera = new QueryCamera(getContext());
//        cameras = Camera.cameras;
//        queryCamera.insertCameraToDb(cameras);
//       queryCamera.retrieveCameraByName("canon eOS 760D");
        RealmResults<RealmProduct> result = queryCamera.retrieveProductsByType("smartphone");
        for(int i=0; i<result.size(); i++){
            products.add(result.get(i));
        }


//        callBackFinishInsert call;
//        queryCamera.retrieveCameraByAllField("", "", "", "", -1, "", "", "", "G");
//        {
//            @Override
//            public String retriveCameraRealmList(String done) {
//               System.out.println("calll query" + done);
//                System.out.println("Callback String From queryCamera: " );
//                return "Callback String From queryCamera" ;
//            }
//        }) ;

//        callback.retriveCameraRealmList();

/**********************************************************************************************/
                RVAdapter adapter = new RVAdapter(products, R.layout.cardview);
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


    @Override
    public void passToDetail() {
        System.out.println("tseting to listener");
//        Message message = new Message();
//        message.what = 1;
//        mHandler.sendMessage(message);

    }


}
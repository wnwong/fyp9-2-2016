package adapter;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.user.secondhandtradingplatform.DetailPageActivity;
import com.example.user.secondhandtradingplatform.R;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmProduct;
import activity.CameraFragment;
import product.Camera;

import static android.support.v4.app.ActivityCompat.startActivity;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CameraViewHolder>{

    private List<RealmProduct> products = new ArrayList<>();
    private int rowLayout;
    public RVAdapter(List<RealmProduct> cam, int rowLayout){
        this.products = cam;
        this.rowLayout = rowLayout;
    }

    public static class CameraViewHolder extends RecyclerView.ViewHolder{
        CardView cview;
        TextView name;
        ImageView photo;

        CameraViewHolder(View itemView){
            super(itemView);
            cview = (CardView) itemView.findViewById(R.id.cview);
            name = (TextView) itemView.findViewById(R.id.product_name);
            photo = (ImageView) itemView.findViewById(R.id.product_photo);
        }
    }

    @Override
    public CameraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CameraViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CameraViewHolder holder, final int position) {
        // Get the list of product
        RealmProduct product =products.get(position);

        byte[] decodedString = Base64.decode(product.getPath(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        // Display the attributes of a product one by one
        holder.name.setText(product.getBrand() + " " + product.getModel());
        holder.photo.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println("tseting to click");
                System.out.println("position: " + position);
//                Context context = CameraFragment.context;
    //            passToDetailPageListener pass = new CameraFragment();
    //            pass.passToDetail();

                Message message = new Message();
                message.obj = new Integer(position);
                message.what = 1;
 /*               if(products.size()>0){
                    CameraFragment.products = products;
                }
 */               CameraFragment.mHandler.sendMessage(message);


            }



        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

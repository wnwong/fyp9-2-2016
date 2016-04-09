package adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.user.secondhandtradingplatform.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmProduct;
import activity.CameraFragment;
import activity.EarphoneFragment;
import activity.GameConsoleFragment;
import activity.NotebookFragment;
import activity.SmartphoneFragment;
import activity.TabletFragment;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CameraViewHolder> {
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    private List<RealmProduct> products = new ArrayList<>();
    private int rowLayout;
    private String type;
    Context context;

    public RVAdapter(List<RealmProduct> cam, int rowLayout, Context context, String type) {
        this.products = cam;
        this.rowLayout = rowLayout;
        this.context = context;
        this.type = type;
    }

    public static class CameraViewHolder extends RecyclerView.ViewHolder {
        CardView cview;
        TextView name;
        ImageView photo;

        CameraViewHolder(View itemView) {
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
        RealmProduct product = products.get(position);
        // Display the attributes of a product one by one
        Picasso.with(context).load(IMAGE_ADDRESS + product.getPath()).into(holder.photo);
        //       holder.photo.setImageBitmap(image);
        holder.name.setText(product.getBrand() + " " + product.getModel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.obj = new Integer(position);
                message.what = 1;
                if(type.equals("平板電腦")){
                    TabletFragment.mHandler.sendMessage(message);
                }else if(type.equals("相機鏡頭")){
                    CameraFragment.mHandler.sendMessage(message);
                }else if(type.equals("智能手機")){
                    SmartphoneFragment.mHandler.sendMessage(message);
                }else if(type.equals("電子遊戲機")){
                    GameConsoleFragment.mHandler.sendMessage(message);
                }else if(type.equals("耳機")){
                    EarphoneFragment.mHandler.sendMessage(message);
                }else if(type.equals("手提電腦")){
                    NotebookFragment.mHandler.sendMessage(message);
                }

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

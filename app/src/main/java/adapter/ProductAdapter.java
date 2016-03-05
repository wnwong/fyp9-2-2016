package adapter;


import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Toast;


import com.example.user.secondhandtradingplatform.ProductInfo;
import com.example.user.secondhandtradingplatform.R;
import com.example.user.secondhandtradingplatform.SearchResultActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmCamera;
import RealmModel.RealmGadget;
import activity.CameraFragment;
import product.Product;
import user.UserLocalStore;

import static android.support.v4.app.ActivityCompat.startActivity;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    private static final int TYPE_INFO = 1;
    private static final int TYPE_POST = 2;
    private static final String TAG = "ProductAdapter";
    List<RealmGadget> gadgets = new ArrayList<>();
    String os, mon, camera, pName, price, image, type;
    Context context;
    UserLocalStore userLocalStore;

    public ProductAdapter(List<RealmGadget> gadgets, String pName, String price, String os, String mon, String camera, String image, String type, Context context) {
        this.gadgets = gadgets;
        this.pName = pName;
        this.price = price;
        this.os = os;
        this.mon = mon;
        this.camera = camera;
        this.type = type;
        this.image = image;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        // Choose Product Info layout for the 1st card
        // Choose Seller post layout for the rest of the card
        return (position == 0 ? TYPE_INFO : TYPE_POST);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate different layout
        switch (viewType) {
            case TYPE_INFO:
                return new InfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_product_info, parent, false));
            case TYPE_POST:
                return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_posted_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        if (holder.getItemViewType() == TYPE_INFO) {
            InfoViewHolder iHolder = (InfoViewHolder) holder;
            Picasso.with(context).load(IMAGE_ADDRESS + image).into(iHolder.image);
            iHolder.pName.setText(pName);
            if(type.equals("相機鏡頭")){
                iHolder.tvmon.setText("尺寸");
                iHolder.mon.setText(mon);
            }else{
                iHolder.tvmon.setText("顯示屏");
                iHolder.mon.setText(mon + "吋");
            }

            //Weight or OS
            if(type.equals("相機鏡頭") | type.equals("電子遊戲機")){
                iHolder.tvos.setText("重量");
            }else{
                iHolder.tvos.setText("作業系統");
            }
            iHolder.os.setText(os);
            iHolder.price.setText("HK$" + price);

            //Camera
            if(type.equals("相機鏡頭")){
                iHolder.camera.setText(camera);
                iHolder.tvcamera.setText("種類");
            }else{
                iHolder.camera.setText(camera + "萬像素");
                iHolder.tvcamera.setText("鏡頭");
            }

            iHolder.graphBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Call the graph dialog
                    Log.i(TAG, "Graph buton clicked!");
                    Message message = new Message();
                    message.what = 3;
                    ProductInfo.mHandler.sendMessage(message);
                }
            });
        } else {
            PostViewHolder pHolder = (PostViewHolder) holder;
            if (gadgets != null) {
                final RealmGadget gadget = gadgets.get(position - 1);
                pHolder.sellerName.setText(gadget.getSeller());
                pHolder.sellerName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                pHolder.sellingPrice.setText("HK$" + gadget.getPrice());
                pHolder.tradePlace.setText(gadget.getSeller_location());
                pHolder.availability.setText(gadget.getAvailability());
                pHolder.color.setText(gadget.getColor());
                pHolder.scratch.setText(gadget.getScratch()+"刮花");
                if(!gadget.getWarranty().equals("0")){
                    pHolder.warranty.setText(gadget.getWarranty()+"個月保養");
                }else{
                    pHolder.warranty.setText("保養期外");
                }

                if (gadget.getAvailability().equals("放售中")){
                    pHolder.availability.setTextColor(Color.parseColor("#FFE4DA23"));
                    pHolder.tradeBtn.setVisibility(View.VISIBLE);
                }else if(gadget.getAvailability().equals("已被預訂")){
                    pHolder.availability.setTextColor(Color.parseColor("#FF16E42E"));
                    //Hide Trade Button
                    pHolder.tradeBtn.setVisibility(View.GONE);
                    pHolder.tradePlace.setPadding(0,0,0,8);
                }else{
                    pHolder.tradeBtn.setVisibility(View.GONE);
                    pHolder.tradePlace.setPadding(0,0,0,8);
                }

                Picasso.with(context).load(IMAGE_ADDRESS + gadget.getImage()).fit().into(pHolder.productPhoto);
                pHolder.tradeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userLocalStore = new UserLocalStore(context);
                        Message message = new Message();
                        if (userLocalStore.getLoggedInUser() != null) {
                            message.obj = new Integer(gadget.getProduct_id());
                            message.what = 1;
                            ProductInfo.mHandler.sendMessage(message);
                        } else {
                            message.what = 2;
                            message.obj = null;
                            ProductInfo.mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (gadgets != null) {
            return gadgets.size() + 1;
        } else {
            return 1;
        }

    }

    public class InfoViewHolder extends ProductViewHolder {
        TextView pName, price, os, mon, camera, tvos, tvcamera, tvmon;
        ImageView image;
        Button graphBtn;

        public InfoViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            pName = (TextView) itemView.findViewById(R.id.pName);
            price = (TextView) itemView.findViewById(R.id.price);
            os = (TextView) itemView.findViewById(R.id.etOS);
            mon = (TextView) itemView.findViewById(R.id.etMon);
            camera = (TextView) itemView.findViewById(R.id.etCam);
            graphBtn = (Button) itemView.findViewById(R.id.graphBtn);
            tvos = (TextView) itemView.findViewById(R.id.os);
            tvcamera = (TextView) itemView.findViewById(R.id.camera);
            tvmon = (TextView) itemView.findViewById(R.id.mon);
        }
    }

    public class PostViewHolder extends ProductViewHolder {
        TextView sellerName, sellingPrice, tradePlace, availability, color, scratch, warranty;
        ImageView productPhoto;
        Button tradeBtn;

        public PostViewHolder(View itemView) {
            super(itemView);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            sellerName = (TextView) itemView.findViewById(R.id.sellername);
            sellingPrice = (TextView) itemView.findViewById(R.id.sellingPrice);
            tradePlace = (TextView) itemView.findViewById(R.id.tradePlace);
            tradeBtn = (Button) itemView.findViewById(R.id.tradeBtn);
            availability = (TextView) itemView.findViewById(R.id.availability);
            color = (TextView) itemView.findViewById(R.id.color);
            scratch = (TextView) itemView.findViewById(R.id.scratch);
            warranty = (TextView) itemView.findViewById(R.id.warranty);
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ProductViewHolder(View itemView) {
            super(itemView);

        }
    }
}

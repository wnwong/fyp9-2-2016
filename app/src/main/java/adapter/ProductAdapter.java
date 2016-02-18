package adapter;


import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import user.UserLocalStore;

import static android.support.v4.app.ActivityCompat.startActivity;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    private static final int TYPE_INFO = 1;
    private static final int TYPE_POST = 2;
    List<RealmGadget> gadgets = new ArrayList<>();
    String os, mon, camera, pName, price, image;
    Context context;
    UserLocalStore userLocalStore;

    public ProductAdapter(List<RealmGadget> gadgets, String pName, String price, String os, String mon, String camera, String image, Context context) {
        this.gadgets = gadgets;
        this.pName = pName;
        this.price = price;
        this.os = os;
        this.mon = mon;
        this.camera = camera;
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
            iHolder.mon.setText(mon + "吋");
            iHolder.os.setText(os);
            iHolder.price.setText("HK$" + price);
            iHolder.camera.setText(camera + "萬像素");
        } else {
            PostViewHolder pHolder = (PostViewHolder) holder;
            if (gadgets != null) {
                final RealmGadget gadget = gadgets.get(position - 1);
                pHolder.sellerName.setText(gadget.getSeller());
                pHolder.sellingPrice.setText("HK$" + gadget.getPrice());
                pHolder.tradePlace.setText(gadget.getSeller_location());
                Picasso.with(context).load(IMAGE_ADDRESS + gadget.getImage()).fit().into(pHolder.productPhoto);
                pHolder.tradeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userLocalStore = new UserLocalStore(context);
                        Message message = new Message();
                        if(userLocalStore.getLoggedInUser()!=null){
                            message.obj = new Integer(gadget.getProduct_id());
                            message.what = 1;
                            ProductInfo.mHandler.sendMessage(message);
                        }else{
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
        TextView pName, price, os, mon, camera;
        ImageView image;

        public InfoViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            pName = (TextView) itemView.findViewById(R.id.pName);
            price = (TextView) itemView.findViewById(R.id.price);
            os = (TextView) itemView.findViewById(R.id.etOS);
            mon = (TextView) itemView.findViewById(R.id.etMon);
            camera = (TextView) itemView.findViewById(R.id.etCam);
        }
    }

    public class PostViewHolder extends ProductViewHolder {
        TextView sellerName, sellingPrice, tradePlace;
        ImageView productPhoto;
        Button tradeBtn;

        public PostViewHolder(View itemView) {
            super(itemView);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            sellerName = (TextView) itemView.findViewById(R.id.sellername);
            sellingPrice = (TextView) itemView.findViewById(R.id.sellingPrice);
            tradePlace = (TextView) itemView.findViewById(R.id.tradePlace);
            tradeBtn = (Button) itemView.findViewById(R.id.tradeBtn);
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ProductViewHolder(View itemView) {
            super(itemView);

        }
    }
}

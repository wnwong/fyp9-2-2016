package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.secondhandtradingplatform.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmGadget;

/**
 * Created by timothy on 9/4/2016.
 */
public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    Context context;
    List<RealmGadget> realmGadgets = new ArrayList<>();
    private static final String TAG = "SellerAdapter";

    public SellerAdapter(List<RealmGadget> realmGadgets, Context context) {
        this.realmGadgets = realmGadgets;
        this.context = context;
    }

    @Override
    public SellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_seller, parent, false);
        return new SellerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SellerViewHolder holder, int position) {
        RealmGadget realmGadget = realmGadgets.get(position);
        holder.product.setText(realmGadget.getBrand() + " " + realmGadget.getModel());
        holder.sellingPrice.setText("HK$" + realmGadget.getPrice());
        Picasso.with(context).load(IMAGE_ADDRESS + realmGadget.getImage()).fit().into(holder.productPhoto);
        holder.rating.setRating(realmGadget.getRating());
        holder.availability.setText(realmGadget.getAvailability());
    }

    @Override
    public int getItemCount() {
        return realmGadgets.size();
    }

    public static class SellerViewHolder extends RecyclerView.ViewHolder {
        TextView sellingPrice, product, tradeDate, buyer, availability;
        ImageView productPhoto;
        RatingBar rating;

        SellerViewHolder(View itemView) {
            super(itemView);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            sellingPrice = (TextView) itemView.findViewById(R.id.sellingPrice);
            product = (TextView) itemView.findViewById(R.id.product);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            availability = (TextView) itemView.findViewById(R.id.availability);
        }
    }
}

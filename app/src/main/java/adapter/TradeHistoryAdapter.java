package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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


public class TradeHistoryAdapter extends RecyclerView.Adapter<TradeHistoryAdapter.TradeHistoryViewHolder> {
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    Context context;
    List<RealmGadget> realmGadgets = new ArrayList<>();
    public TradeHistoryAdapter(List<RealmGadget> realmGadgets, Context context) {
        this.realmGadgets = realmGadgets;
        this.context = context;
    }

    @Override
    public TradeHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_trade_history, parent, false);
        return new TradeHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TradeHistoryViewHolder holder, int position) {
        RealmGadget realmGadget = realmGadgets.get(position);
        holder.product.setText(realmGadget.getBrand() + " " + realmGadget.getModel());
        holder.tradeDate.setText(realmGadget.getTrade_date());
        holder.sellingPrice.setText("HKS$" + realmGadget.getPrice());
        holder.buyer.setText(realmGadget.getBuyer());
        Picasso.with(context).load(IMAGE_ADDRESS + realmGadget.getImage()).fit().into(holder.productPhoto);
        holder.rating.setRating(realmGadget.getRating());
        holder.availability.setText(realmGadget.getAvailability());
    }

    @Override
    public int getItemCount() {
        return realmGadgets.size();
    }

    public static class TradeHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView sellingPrice, product, tradeDate, buyer, availability;
        ImageView productPhoto;
        RatingBar rating;
        TradeHistoryViewHolder(View itemView) {
            super(itemView);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            sellingPrice = (TextView) itemView.findViewById(R.id.sellingPrice);
            product = (TextView) itemView.findViewById(R.id.product);
            tradeDate = (TextView) itemView.findViewById(R.id.tradeDate);
            buyer = (TextView) itemView.findViewById(R.id.buyer);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            availability = (TextView) itemView.findViewById(R.id.availability);
        }
    }
}

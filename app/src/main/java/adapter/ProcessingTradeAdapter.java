package adapter;

import android.content.Context;
import android.graphics.Color;
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
import user.UserLocalStore;


public class ProcessingTradeAdapter extends RecyclerView.Adapter<ProcessingTradeAdapter.ProcessingTradeViewHolder> {
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    UserLocalStore userLocalStore;
    Context context;
    List<RealmGadget> realmGadgets = new ArrayList<>();
    public ProcessingTradeAdapter(List<RealmGadget> realmGadgets, Context context) {
        this.realmGadgets = realmGadgets;
        this.context = context;
        this.userLocalStore = new UserLocalStore(context);
    }

    @Override
    public ProcessingTradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_trade_processing, parent, false);
        return new ProcessingTradeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProcessingTradeViewHolder holder, int position) {
        RealmGadget realmGadget = realmGadgets.get(position);
        holder.product.setText(realmGadget.getBrand() + " " + realmGadget.getModel());
        holder.tradeDate.setText(realmGadget.getTrade_date());
        holder.sellingPrice.setText("HKS$" + realmGadget.getPrice());
        if(realmGadget.getSeller().equals(userLocalStore.getLoggedInUser().getUsername())){
            holder.buyer.setText(realmGadget.getBuyer());
        }else{
            holder.buyer.setText(realmGadget.getSeller());
        }

        holder.tradeDate.setText(realmGadget.getTrade_date());
        Picasso.with(context).load(IMAGE_ADDRESS + realmGadget.getImage()).fit().into(holder.productPhoto);
       holder.tradeLocation.setText(realmGadget.getBuyer_location());
        if(realmGadget.getSeller().equals(userLocalStore.getLoggedInUser().getUsername())){
            holder.sellerPhone.setText(realmGadget.getBuyer_phone());
        }else{
            holder.sellerPhone.setText(realmGadget.getSeller_phone());
        }
        holder.availability.setText(realmGadget.getAvailability());
        if(realmGadget.getAvailability().equals("放售中")){
            holder.availability.setTextColor(Color.parseColor("#FFE4DA23"));
            holder.place.setVisibility(View.INVISIBLE);
            holder.date.setVisibility(View.INVISIBLE);
            holder.person.setVisibility(View.INVISIBLE);
            holder.phone.setVisibility(View.INVISIBLE);
            holder.tradeDate.setVisibility(View.INVISIBLE);
            holder.tradeLocation.setVisibility(View.INVISIBLE);
            holder.sellerPhone.setVisibility(View.INVISIBLE);
            holder.buyer.setVisibility(View.INVISIBLE);
        }else{
            holder.availability.setTextColor(Color.parseColor("#FF16E42E"));
        }
    }

    @Override
    public int getItemCount() {
        return realmGadgets.size();
    }

    public static class ProcessingTradeViewHolder extends RecyclerView.ViewHolder {
        TextView sellingPrice, product, tradeDate, buyer, tradeLocation, sellerPhone, availability;
        ImageView productPhoto, place, date, person, phone;
        ProcessingTradeViewHolder(View itemView) {
            super(itemView);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            sellingPrice = (TextView) itemView.findViewById(R.id.sellingPrice);
            product = (TextView) itemView.findViewById(R.id.product);
            tradeDate = (TextView) itemView.findViewById(R.id.tradeDate);
            buyer = (TextView) itemView.findViewById(R.id.buyer);
            tradeLocation = (TextView) itemView.findViewById(R.id.tradeLocation);
            sellerPhone = (TextView) itemView.findViewById(R.id.sellerPhone);
            availability = (TextView) itemView.findViewById(R.id.availability);
            place = (ImageView) itemView.findViewById(R.id.place);
            date = (ImageView) itemView.findViewById(R.id.date);
            person = (ImageView) itemView.findViewById(R.id.person);
            phone = (ImageView) itemView.findViewById(R.id.phone);
        }
    }
}

package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.secondhandtradingplatform.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmGadget;
import user.ProcessingTradeFragment;
import user.UserLocalStore;


public class ProcessingTradeAdapter extends RecyclerView.Adapter<ProcessingTradeAdapter.ProcessingTradeViewHolder> {
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    UserLocalStore userLocalStore;
    Context context;
    List<RealmGadget> realmGadgets = new ArrayList<>();
    public static String TAG = "ProcessingTradeAdapter";

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
    public void onBindViewHolder(final ProcessingTradeViewHolder holder, int position) {
        RealmGadget realmGadget = realmGadgets.get(position);
        holder.product.setText(realmGadget.getBrand() + " " + realmGadget.getModel());
        holder.tradeDate.setText(realmGadget.getTrade_date());
        holder.sellingPrice.setText("HKS$" + realmGadget.getPrice());
        holder.availability.setText(realmGadget.getAvailability());
        holder.tradeDate.setText(realmGadget.getTrade_date());
        Picasso.with(context).load(IMAGE_ADDRESS + realmGadget.getImage()).fit().into(holder.productPhoto);
        holder.tradeLocation.setText(realmGadget.getBuyer_location());

        //Determine show buyer or seller name
        if (realmGadget.getSeller().equals(userLocalStore.getLoggedInUser().getUsername())) {
            holder.buyer.setText(realmGadget.getBuyer());
        } else {
            holder.buyer.setText(realmGadget.getSeller());
        }

        // Determine show buyer or seller phone
        if (realmGadget.getSeller().equals(userLocalStore.getLoggedInUser().getUsername())) {
            holder.sellerPhone.setText(realmGadget.getBuyer_phone());
        } else {
            holder.sellerPhone.setText(realmGadget.getSeller_phone());
        }

        if (realmGadget.getAvailability().equals("放售中")) {
            holder.availability.setTextColor(Color.parseColor("#FFE4DA23"));
            holder.place.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
            holder.person.setVisibility(View.GONE);
            holder.phone.setVisibility(View.GONE);
            holder.tradeDate.setVisibility(View.GONE);
            holder.tradeLocation.setVisibility(View.GONE);
            holder.sellerPhone.setVisibility(View.GONE);
            holder.buyer.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.confirmBtn.setVisibility(View.GONE);
            holder.sellingPrice.setPadding(0,0,0,24);

        } else {
            holder.availability.setTextColor(Color.parseColor("#FF16E42E"));
            holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.ratingBar.getRating() != 0) {
                        Message message = new Message();
                        message.what = 2;
                        message.obj = holder.ratingBar.getRating();
                        ProcessingTradeFragment.mHandler.sendMessage(message);
                    } else {
                       showAlertMessage();
                    }

                }
            });
            holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    Log.i(TAG, String.valueOf(rating));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return realmGadgets.size();
    }

    public static class ProcessingTradeViewHolder extends RecyclerView.ViewHolder {
        TextView sellingPrice, product, tradeDate, buyer, tradeLocation, sellerPhone, availability;
        ImageView productPhoto, place, date, person, phone;
        RatingBar ratingBar;
        Button confirmBtn;

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
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            confirmBtn = (Button) itemView.findViewById(R.id.confirmBtn);
        }
    }

    private void showAlertMessage(){
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        alerDialog.setMessage("Please Give a Rating!");
        alerDialog.show();
    }
}

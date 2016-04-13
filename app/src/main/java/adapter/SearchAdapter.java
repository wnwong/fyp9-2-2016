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
import com.example.user.secondhandtradingplatform.SearchResultActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmProduct;
import activity.CameraFragment;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    public static final String IMAGE_ADDRESS = "http://php-etrading.rhcloud.com/pictures/";
    private List<RealmProduct> products = new ArrayList<>();
    private int rowLayout;
    Context context;

    public SearchAdapter(List<RealmProduct> cam, int rowLayout, Context context) {
        this.products = cam;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        CardView cview;
        TextView name, type;
        ImageView photo;

        SearchViewHolder(View itemView) {
            super(itemView);
            cview = (CardView) itemView.findViewById(R.id.cview);
            name = (TextView) itemView.findViewById(R.id.product_name);
            type = (TextView) itemView.findViewById(R.id.product_type);
            photo = (ImageView) itemView.findViewById(R.id.product_photo);
        }
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {
        // Get the list of product
        RealmProduct product = products.get(position);
        // Display the attributes of a product one by one
        Picasso.with(context).load(IMAGE_ADDRESS + product.getPath()).into(holder.photo);
        //       holder.photo.setImageBitmap(image);
        holder.name.setText(product.getBrand() + " " + product.getModel());
        holder.type.setText(product.getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.obj = new Integer(position);
                message.what = 1;
                SearchResultActivity.mHandler.sendMessage(message);
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

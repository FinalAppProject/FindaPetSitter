package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Sitter;

import java.util.LinkedList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RecyclerViewAvailableSittersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static List<Sitter> availablSitters;
    private static Context mContext;

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.item_sitter, parent, false);
        viewHolder = new UserViewHolder(mContext, v1, availablSitters);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserViewHolder vh = (UserViewHolder) holder;
        Sitter sitter = availablSitters.get(position);
        vh.tvItemFirstName.setText(sitter.getFirstName());
        vh.tvItemLastName.setText(sitter.getLastName());
        vh.tvTagline.setText(sitter.getTagLine());

        Glide.with(mContext).load(sitter.getProfilepic_url())
                .bitmapTransform(new RoundedCornersTransformation(mContext, 10, 0))
                .override(150, 150).into(vh.ivItemProfilePic);

        vh.tvAmountCharged.setText("$"+sitter.getAmount_charged());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return this.availablSitters.size();
    }

    // Suitable constructor depending on the kind of dataset
    public RecyclerViewAvailableSittersAdapter(Context context, LinkedList<Sitter> availablSitters) {
        this.availablSitters = availablSitters;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
}
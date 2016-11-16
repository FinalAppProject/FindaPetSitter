package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.LinkedList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SittersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static List<User> mAvailableSitters;
    private static Context mContext;

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sitter, parent, false);
        viewHolder = new UserViewHolder(mContext, view, mAvailableSitters);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserViewHolder vh = (UserViewHolder) holder;
        User sitter = mAvailableSitters.get(position);
        vh.tvItemFirstName.setText(sitter.getFullName());
        //vh.tvTagline.setText(sitter.getDescription());
        vh.tvTagline.setText("Description/Tagline" /*sitter.getTagLine()*/);

        ImageHelper.loadImage(mContext, sitter.getProfileImage(), R.drawable.cat, vh.ivItemProfilePic);

        //TODO: need to figure this out, on how to use it
        /*Glide.with(mContext).load(sitter.getProfileImage())
                .bitmapTransform(new RoundedCornersTransformation(mContext, 10, 0))
                .placeholder(R.drawable.cat)
                .override(150, 150).into(vh.ivItemProfilePic);
        */

        vh.tvNumReviews.setText("45");
        vh.tvRatings.setText("4.8");
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return this.mAvailableSitters.size();
    }

    // Suitable constructor depending on the kind of dataset
    public SittersAdapter(Context context, LinkedList<User> availablSitters) {
        this.mAvailableSitters = availablSitters;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
}
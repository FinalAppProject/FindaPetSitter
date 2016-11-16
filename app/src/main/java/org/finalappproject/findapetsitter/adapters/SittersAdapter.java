package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.LinkedList;
import java.util.List;

public class SittersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static List<User> mAvailableSitters;
    private static Context mContext;

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

        vh.tvNumReviews.setText("45");
        vh.tvRatings.setText("4.8");
    }

    @Override
    public int getItemCount() {
        return this.mAvailableSitters.size();
    }

    public SittersAdapter(Context context, LinkedList<User> availablSitters) {
        this.mAvailableSitters = availablSitters;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }
}
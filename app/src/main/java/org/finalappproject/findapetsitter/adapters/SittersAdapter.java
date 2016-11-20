package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.UserProfileActivity;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.activities.UserProfileEditActivity.EXTRA_USER_OBJECT_ID;

public class SittersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static List<User> mAvailableSitters;
    private static Context mContext;

    private Context getContext() {
        return mContext;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvItemName)
        TextView tvItemFirstName;
        @BindView(R.id.ivItemProfileImage)
        ImageView ivItemProfilePic;
        @BindView(R.id.tvItemTagline)
        TextView tvTagline;
        @BindView(R.id.tvNumReviews)
        TextView tvNumReviews;
        @BindView(R.id.tvRatings)
        TextView tvRatings;
        @BindView(R.id.RlSitterItem)
        RelativeLayout RlSitterItem;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sitter, parent, false);
        RecyclerView.ViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        UserViewHolder vh = (UserViewHolder) holder;
        final User sitter = mAvailableSitters.get(position);
        vh.tvItemFirstName.setText(sitter.getFullName());
        //vh.tvTagline.setText(sitter.getDescription());
        vh.tvTagline.setText("Description/Tagline" /*sitter.getTagLine()*/);

        ImageHelper.loadImage(mContext, sitter.getProfileImage(), R.drawable.cat, vh.ivItemProfilePic);

        vh.tvNumReviews.setText("45");
        vh.tvRatings.setText("4.8");

        vh.RlSitterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userProfileIntent = new Intent(getContext(), UserProfileActivity.class);
                userProfileIntent.putExtra(EXTRA_USER_OBJECT_ID, sitter.getObjectId());
                getContext().startActivity(userProfileIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mAvailableSitters.size();
    }

    public SittersAdapter(Context context, LinkedList<User> availablSitters) {
        this.mAvailableSitters = availablSitters;
        mContext = context;
    }


}
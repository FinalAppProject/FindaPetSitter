package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserViewHolder extends RecyclerView.ViewHolder {

    private List<User> sitterlist;
    private Context context;

    @BindView(R.id.tvItemName) TextView tvItemFirstName;
    @BindView(R.id.ivItemProfileImage) ImageView ivItemProfilePic;
    @BindView(R.id.tvItemTagline) TextView tvTagline;
    @BindView(R.id.tvNumReviews) TextView tvNumReviews;
    @BindView(R.id.tvRatings) TextView tvRatings;
    @BindView(R.id.RlSitterItem) RelativeLayout RlSitterItem;

    public UserViewHolder(Context context, View itemView, List<User> sitterlist) {
        super(itemView);
        this.sitterlist = sitterlist;
        this.context = context;
        ButterKnife.bind(this, itemView);
    }
}
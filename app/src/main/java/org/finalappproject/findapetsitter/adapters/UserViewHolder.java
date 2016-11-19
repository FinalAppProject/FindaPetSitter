package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.RequestActivity;
import org.finalappproject.findapetsitter.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View v) {
        int position = getLayoutPosition(); // gets item position
        User sitter = sitterlist.get(position);

        Toast.makeText(context, "Will open request window", Toast.LENGTH_SHORT).show();
        launchRequestDialog(sitter);
    }

    void launchRequestDialog(User sitter){
        Intent i = new Intent(context, RequestActivity.class);
        i.putExtra("sitter_id", sitter.getObjectId());
        context.startActivity(i);
    }
}
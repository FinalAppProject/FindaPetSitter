package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Sitter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private List<Sitter> sitterlist;
    private Context context;

    @BindView(R.id.tvItemFirstName) TextView tvItemFirstName;
    @BindView(R.id.tvItemLastName) TextView tvItemLastName;
    @BindView(R.id.ivItemProfileImage) ImageView ivItemProfilePic;
    @BindView(R.id.tvItemAmountCharged) TextView tvAmountCharged;
    @BindView(R.id.tvItemTagline) TextView tvTagline;

    public UserViewHolder(Context context, View itemView, List<Sitter> sitterlist) {
        super(itemView);
        this.sitterlist = sitterlist;
        this.context = context;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "CLICKED", Toast.LENGTH_SHORT).show();
    }
}

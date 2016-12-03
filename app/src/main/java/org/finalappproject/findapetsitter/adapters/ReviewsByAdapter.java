package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Review;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsByAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Review> mReviews;
    private static Context mContext;

    private Context getContext() {
        return mContext;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvReviewItemName)
        TextView tvItemFirstName;
        @BindView(R.id.ivReviewItemProfileImage)
        ImageView ivItemProfilePic;
        @BindView(R.id.tvItemReview)
        TextView tvReview;
        @BindView(R.id.rbReviewRating)
        RatingBar rbReviewRating;
        @BindView(R.id.RlReviewItem)
        RelativeLayout RlReviewItem;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_review, parent, false);
        RecyclerView.ViewHolder viewHolder = new org.finalappproject.findapetsitter.adapters.ReviewsByAdapter.ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        org.finalappproject.findapetsitter.adapters.ReviewsByAdapter.ReviewViewHolder vh = (org.finalappproject.findapetsitter.adapters.ReviewsByAdapter.ReviewViewHolder) holder;
        final Review review = mReviews.get(position);
        review.toString();
        ImageHelper.loadImage(mContext, review.getReviewReceiver().getProfileImage(), R.drawable.cat, vh.ivItemProfilePic);
        vh.tvReview.setText(review.getReview());
        vh.rbReviewRating.setRating((float) review.getRating());
        vh.tvItemFirstName.setText(review.getReviewReceiver().getFullName()); //TODO:Failing if you call this first, need to figure out why

        vh.RlReviewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TDOD: open up detailed view, decide what details to give
                /*      Intent userProfileIntent = new Intent(getContext(), UserProfileActivity.class);
                    userProfileIntent.putExtra(EXTRA_USER_OBJECT_ID, sitter.getObjectId());
                    getContext().startActivity(userProfileIntent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mReviews.size();
    }

    public ReviewsByAdapter(Context context, List<Review> reviews) {
        this.mReviews = reviews;
        mContext = context;
    }
}


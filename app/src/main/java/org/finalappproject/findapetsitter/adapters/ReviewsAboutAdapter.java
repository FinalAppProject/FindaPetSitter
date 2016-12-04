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

public class ReviewsAboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
        RecyclerView.ViewHolder viewHolder = new org.finalappproject.findapetsitter.adapters.ReviewsAboutAdapter.ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        org.finalappproject.findapetsitter.adapters.ReviewsAboutAdapter.ReviewViewHolder vh = (org.finalappproject.findapetsitter.adapters.ReviewsAboutAdapter.ReviewViewHolder) holder;
        final Review review = mReviews.get(position);
        vh.tvReview.setText(review.getReview());
        vh.rbReviewRating.setRating((float)review.getRating());
        ImageHelper.loadImage(mContext, review.getReviewer().getProfileImage(), R.drawable.cat, vh.ivItemProfilePic);
        vh.tvItemFirstName.setText(review.getReviewer().getFullName());

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

    public ReviewsAboutAdapter(Context context, List<Review> reviews) {
        this.mReviews = reviews;
        mContext = context;
    }
}
package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.House;

import java.util.List;

/**
 * Created by Aoi on 11/9/2016.
 */

public class HouseListAdapter extends RecyclerView.Adapter<HouseListAdapter.ViewHolder>  {

    private List<House> mHouses;
    private Context mContext;

    public HouseListAdapter(Context context, List<House> houses) {
        mContext = context;
        mHouses = houses;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivHouseImage;
        public TextView tvHouseTitle;
        public RatingBar rbRating;
        public TextView tvReviewCount;

        public ViewHolder(View itemView) {
            super(itemView);

            ivHouseImage = (ImageView) itemView.findViewById(R.id.ivItemHouse);
            tvHouseTitle = (TextView) itemView.findViewById(R.id.tvHouseTitle);
            rbRating = (RatingBar) itemView.findViewById(R.id.ratingBar);
            tvReviewCount = (TextView) itemView.findViewById(R.id.tvReviewCount);
        }
    }

    @Override
    public HouseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View houseView = inflater.inflate(R.layout.item_houses, parent, false);
        ViewHolder viewHolder = new ViewHolder(houseView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final HouseListAdapter.ViewHolder viewHolder, int position) {
        House house = mHouses.get(position);

        TextView tvHouseTitle = viewHolder.tvHouseTitle;
        tvHouseTitle.setText(house.getHouseTitle());
        ImageView ivHouseImage = viewHolder.ivHouseImage;
        //TODO Piccaso or Glide setImage
        TextView tvReviewCount = viewHolder.tvReviewCount;
        tvReviewCount.setText("(" + house.getNumReviews()+ ")");

    }

    @Override
    public int getItemCount() {
        return mHouses.size();
    }
}
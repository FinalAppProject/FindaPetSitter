package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Pet;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Pets Adapter
 */
public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.ViewHolder> {

    private static final String LOG_TAG = "PetsAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPetProfileImage)
        public ImageView ivPetProfileImage;

        @BindView(R.id.tvPetName)
        public TextView tvPetName;

        public ViewHolder(View itemView) {
            super(itemView);
            // Bind views
            ButterKnife.bind(this, itemView);

        }
    }

    Context mContext;

    List<Pet> mPets;


    public PetsAdapter(Context context, List<Pet> pets) {
        mContext = context;
        mPets = pets;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public PetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate pet item layout, create and return view holder
        View petItemView = inflater.inflate(R.layout.item_pet, parent, false);
        ViewHolder viewHolder = new ViewHolder(petItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PetsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Pet pet = mPets.get(position);

        // Profile image
        ImageHelper.loadImage(mContext, pet.getProfileImage(), R.drawable.cat, holder.ivPetProfileImage);

        // Pet name
        holder.tvPetName.setText(pet.getName());
    }

    @Override
    public int getItemCount() {
        return mPets.size();
    }
}

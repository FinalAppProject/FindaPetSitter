package org.finalappproject.findapetsitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.HomeActivity;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.R.layout.item_sitter;

/**
 * Fragment that shows a list of pet sitters ordered by how close they are to the current user
 */
public class PetSittersFragment extends Fragment {

    private static final String LOG_TAG = "PetSittersFragment";
    private String mCurrentFragmentTag = "PetSitterFragment";

    public class PetSitterAdapter extends RecyclerView.Adapter<PetSitterAdapter.UserViewHolder> {

        public class UserViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tvItemName)
            TextView tvItemFirstName;

            @BindView(R.id.ivItemProfileImage)
            ImageView ivItemProfilePic;

            @BindView(R.id.tvItemTagline)
            TextView tvTagline;

            @BindView(R.id.tvDistance)
            TextView tvDistance;

            @BindView(R.id.RlSitterItem)
            RelativeLayout RlSitterItem;

            public UserViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public PetSitterAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(item_sitter, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PetSitterAdapter.UserViewHolder vh, final int position) {
            final User sitter = mPetSitters.get(position);
            vh.tvItemFirstName.setText(sitter.getFullName());
            vh.ivItemProfilePic.setImageResource(0);
            if(sitter.getDescription().length() == 0) {
                vh.tvTagline.setText("No Description Provided");
            } else {
                vh.tvTagline.setText("\"" + sitter.getDescription() + "\"");
            }
            ImageHelper.loadImage(getContext(), sitter.getProfileImage(), R.drawable.cat, vh.ivItemProfilePic);

            String distanceStr = "??? mi";
            if (ownerGeoPoint != null) {
                if ((sitter.getAddress() != null && sitter.getAddress().getGeoPoint() != null)) {
                    ParseGeoPoint sitterGeoPoint = sitter.getAddress().getGeoPoint();
                    double distance = ownerGeoPoint.distanceInMilesTo(sitterGeoPoint);
                    distanceStr = String.format("%.1f mi", distance);
                }
            }

            vh.tvDistance.setText(distanceStr);

            vh.RlSitterItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity) getContext()).showUserProfileFragment(sitter.getObjectId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPetSitters.size();
        }

    }

    private LinearLayoutManager mAvailableSittersLinearlayout;
    private ArrayList<User> mPetSitters;

    private PetSitterAdapter mAvailableSittersAdapter;
    ParseGeoPoint ownerGeoPoint;

    @BindView(R.id.rvAvailableSitters)
    RecyclerView mAvailableSittersRecyclerView;
    @BindView(R.id.swipeContainerAvailableSitters)
    SwipeRefreshLayout mAvailableSittersSwipeRefreshLayout;

    void populateList() {
        User.queryPetSittersNear(new FindCallback<User>() {
            @Override
            public void done(List<User> sitters, ParseException e) {
                // Cleanup data if not empty
                int currentSize = mPetSitters.size();
                if (currentSize != 0) {
                    mPetSitters.clear();
                    mAvailableSittersAdapter.notifyItemRangeRemoved(0, currentSize);
                }
                // Add retrieved sitters
                mPetSitters.addAll(sitters);
                mAvailableSittersAdapter.notifyItemRangeInserted(0, sitters.size());
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserGeoLocation(); // TODO in background
        populateList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);

        ownerGeoPoint = null;
        mPetSitters = new ArrayList<>();
        mAvailableSittersAdapter = new PetSitterAdapter();

        View availableSittersView = inflater.inflate(R.layout.fragment_available_sitters, parent, false);
        ButterKnife.bind(this, availableSittersView);

        mAvailableSittersRecyclerView.setAdapter(mAvailableSittersAdapter);
        mAvailableSittersLinearlayout = new LinearLayoutManager(getActivity());
        mAvailableSittersLinearlayout.setOrientation(LinearLayoutManager.VERTICAL);
        mAvailableSittersLinearlayout.scrollToPosition(0);
        mAvailableSittersRecyclerView.setLayoutManager(mAvailableSittersLinearlayout);

        mAvailableSittersSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateList();
                mAvailableSittersSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return availableSittersView;
    }

    void getUserGeoLocation() {
        // TODO fetch in background, deal with 2 concurrent requests (users list and current user/address fetch)
        User currentUser = (User) User.getCurrentUser();
        try {
            Address userAddress = currentUser.getAddress().fetchIfNeeded();
            ownerGeoPoint = userAddress.getGeoPoint();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to fetch user address coordinates", e);
        }
    }
}
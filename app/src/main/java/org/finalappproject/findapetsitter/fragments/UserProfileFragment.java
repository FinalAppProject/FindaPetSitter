package org.finalappproject.findapetsitter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.UserProfileEditActivity;
import org.finalappproject.findapetsitter.adapters.PetsAdapter;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.Pet;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;
import org.finalappproject.findapetsitter.util.recyclerview.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.model.User.queryUser;

/**
 * Created by Aoi on 11/15/2016.
 */

public class UserProfileFragment extends Fragment {

    @BindView(R.id.ivUserProfileImage)
    ImageView ivUserProfileImage;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvUserNickname)
    TextView tvUserNickname;

    @BindView(R.id.tvUserDescription)
    TextView tvUserDescription;

    @BindView(R.id.tvUserPhoneNumber)
    TextView tvUserPhoneNumber;

    @BindView(R.id.tvUserAddress)
    TextView tvUserAddress;

    @BindView(R.id.rvPets)
    RecyclerView rvPets;

    @BindView(R.id.btSendRequestOrEdit)
    Button btSendRequest;

    @BindView(R.id.btWriteReview)
    Button btWriteReview;

    @BindView(R.id.btViewReview)
    Button btViewReviews;

    @BindView(R.id.flReviewsContainer)
    FrameLayout flProfileReviewsContainer;

    @BindView(R.id.svProfileScroll)
    ScrollView svProfileScroll;

    User mUser;
    List<Pet> mPets;
    PetsAdapter mPetsAdapter;
    private Boolean isOtherUser;

    /**
     * Required empty public constructor,
     * use newInstance() factory method instead
     */
    @Deprecated
    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method used to create SitterHomeFragment
     * with parameters, use this method instead of
     * new SitterHomeFragment()
     *
     * @return A new instance of fragment SitterHomeFragment.
     */
    public static UserProfileFragment newInstance(String userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPets = new ArrayList<>();
        mPetsAdapter = new PetsAdapter(getContext(), mPets);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);
        setupPetsRecyclerView();

        String userObjectId = getArguments().getString("user_id");

        if (userObjectId != null && !userObjectId.isEmpty()) {
            queryUser(userObjectId, new GetCallback<User>() {
                @Override
                public void done(User user, ParseException e) {
                    if (e == null) {
                        isOtherUser = false;
                        mUser = user;
                        loadData();
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch user", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            isOtherUser = true;
            mUser = (User) User.getCurrentUser();
            loadData();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.miEdit:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupPetsRecyclerView() {
        rvPets.setAdapter(mPetsAdapter);
        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(getContext());
        linerLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        rvPets.setLayoutManager(linerLayoutManager);

        ItemClickSupport.addTo(rvPets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // TODO startPetProfileActivity(position);
                    }
                }
        );
    }


    private void loadData() {
        if (mUser == null) {
            return;
        }
        String a = mUser.getFullName();
        tvUserName.setText(mUser.getFullName());
        tvUserNickname.setText(mUser.getNickName());
        tvUserDescription.setText(mUser.getDescription());
        tvUserPhoneNumber.setText(String.format("Phone number: %s", mUser.getPhone()));
        //tvUserAddress.setText(String.format("Live in: %s, %s", mUser.getAddress().getCity(), mUser.getAddress().getState()));

        try {
            Address userAddress = mUser.getAddress().fetchIfNeeded();
            if (userAddress != null) {
                tvUserAddress.setText(String.format("Live in: %s, %s", mUser.getAddress().getCity(), mUser.getAddress().getState()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isOtherUser) {
            btSendRequest.setText("Send Request");
            btSendRequest.setVisibility(View.VISIBLE);
            btWriteReview.setText("Write Review");
            btWriteReview.setVisibility(View.VISIBLE);
            btViewReviews.setText("View Reviews");
            btViewReviews.setVisibility(View.VISIBLE);
        } else {
            btSendRequest.setVisibility(View.GONE);
            btWriteReview.setVisibility(View.GONE);
            btViewReviews.setVisibility(View.GONE);
        }

        btSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("sitter_id", mUser.getObjectId());

                RequestFragment requestFragmentDialog = new RequestFragment();
                requestFragmentDialog.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                requestFragmentDialog.show(fm, "request");
            }
        });

        btWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("sitter_id", mUser.getObjectId());

                WriteReviewFragment reviewFragmentDialog = new WriteReviewFragment();
                reviewFragmentDialog.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                reviewFragmentDialog.show(fm, "write_review");

            }
        });

        btViewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flProfileReviewsContainer.setVisibility(View.VISIBLE);
                FragmentManager fm = getFragmentManager();
                ReviewsAboutFragment reviewsAboutFragment = ReviewsAboutFragment.newInstance(mUser.getObjectId());
                fm.beginTransaction()
                        .add(R.id.flReviewsContainer, reviewsAboutFragment, "review_about_user")
                        .commit();
                fm.beginTransaction().show(reviewsAboutFragment).commit();
                focusOnView();
                //flProfileReviewsContainer.getParent().requestChildFocus(targetView,targetView);
            }
        });

        // Show user profile image
        ImageHelper.loadImage(getContext(), mUser.getProfileImage(), R.drawable.account_plus, ivUserProfileImage);
        
        loadPetsData();

        btSendRequest.setText("Edit Profile");
        btSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userProfileIntent = new Intent(getContext(), UserProfileEditActivity.class);
                startActivity(userProfileIntent);
            }
        });
    }

    private void loadPetsData() {
        // Clear existent
        int petsCount = mPets.size();
        mPets.clear();
        mPetsAdapter.notifyItemRangeRemoved(0, petsCount);
        // Add pets
        List<Pet> userPets = mUser.getPets();
        if (userPets != null) {
            mPets.addAll(userPets);
            mPetsAdapter.notifyItemRangeInserted(0, userPets.size());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private final void focusOnView() {
        svProfileScroll.post(new Runnable() {
            @Override
            public void run() {
                svProfileScroll.smoothScrollBy(0, flProfileReviewsContainer.getTop());
            }
        });
    }
}

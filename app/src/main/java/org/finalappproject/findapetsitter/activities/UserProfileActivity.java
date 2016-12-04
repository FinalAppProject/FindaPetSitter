package org.finalappproject.findapetsitter.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.adapters.PetsAdapter;
import org.finalappproject.findapetsitter.fragments.RequestFragment;
import org.finalappproject.findapetsitter.fragments.ReviewsAboutFragment;
import org.finalappproject.findapetsitter.fragments.WriteReviewFragment;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.Pet;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;
import org.finalappproject.findapetsitter.util.recyclerview.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.fragment;
import static android.R.attr.tag;
import static org.finalappproject.findapetsitter.R.id.btViewReview;
import static org.finalappproject.findapetsitter.activities.UserProfileEditActivity.EXTRA_USER_OBJECT_ID;
import static org.finalappproject.findapetsitter.model.User.queryUser;

/**
 * Created by Aoi on 11/18/2016.
 */

public class UserProfileActivity extends AppCompatActivity implements GetCallback<User> {

    private static final String LOG_TAG = "UserProfileActivity";

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

    @BindView(btViewReview)
    Button btViewReviews;

    @BindView(R.id.flReviewsContainer)
    FrameLayout profileReviewsContainer;


    User mUser;
    List<Pet> mPets;
    PetsAdapter mPetsAdapter;
    Boolean isOtherUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);
        // Bind views
        ButterKnife.bind(this);
        // Setup recycler view
        setupPetsRecyclerView();

        mUser = null;
        String userObjectId = getIntent().getStringExtra(EXTRA_USER_OBJECT_ID);

        if (userObjectId != null && !userObjectId.isEmpty()) {
            isOtherUser = true;
            queryUser(userObjectId, this);
        } else {
            isOtherUser = false;
            mUser = (User) User.getCurrentUser();
            loadData();
        }
    }

    private void setupPetsRecyclerView() {
        mPets = new ArrayList<>();
        mPetsAdapter = new PetsAdapter(this, mPets);
        rvPets.setAdapter(mPetsAdapter);
        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(this);
        linerLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        rvPets.setLayoutManager(linerLayoutManager);

        ItemClickSupport.addTo(rvPets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // TOD startPetProfileActivity(position);
                    }
                }
        );
    }


    private void loadData() {
        tvUserName.setText(mUser.getFullName());
        tvUserNickname.setText(mUser.getNickName());
        tvUserDescription.setText(mUser.getDescription());
        tvUserPhoneNumber.setText(String.format("Phone number: %s", mUser.getPhone()));

        try {
            Address address = mUser.getAddress().fetchIfNeeded();
            if (address != null) {
                tvUserAddress.setText(String.format("Live in: %s, %s", mUser.getAddress().getCity(), mUser.getAddress().getState()));
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to fetch address", e);
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

                FragmentManager fm = getSupportFragmentManager();
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

                FragmentManager fm = getSupportFragmentManager();
                reviewFragmentDialog.show(fm, "write_review");

            }
        });

        btViewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileReviewsContainer.setVisibility(View.VISIBLE);
                FragmentManager fm = getSupportFragmentManager();
                ReviewsAboutFragment reviewsAboutFragment = ReviewsAboutFragment.newInstance(mUser.getObjectId());
                fm.beginTransaction()
                        .add(R.id.flReviewsContainer, reviewsAboutFragment, "review_about_user")
                        .commit();
                fm.beginTransaction().show(reviewsAboutFragment).commit();
            }
        });

        ImageHelper.loadImage(this, mUser.getProfileImage(), R.drawable.account_plus, ivUserProfileImage);

        // Load pets list into the recycler view
        List<Pet> userPets = mUser.getPets();
        if (userPets != null) {
            mPets.addAll(userPets);
            mPetsAdapter.notifyItemRangeInserted(0, userPets.size());
        }
    }

    @Override
    public void done(User user, ParseException e) {
        if (e == null) {
            mUser = user;
            loadData();
        } else {
            Log.e(LOG_TAG, "Failed to fetch user", e);
            Toast.makeText(this, "Failed to fetch user", Toast.LENGTH_LONG).show();
        }
    }
}
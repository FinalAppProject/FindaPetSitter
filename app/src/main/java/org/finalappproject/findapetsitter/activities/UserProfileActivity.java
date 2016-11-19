package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.fragments.RequestFragment;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.btSendRequest)
    Button btSendRequest;

    // TODO following will be horizontal recyclerview
    @BindView(R.id.ivUserPet1)
    ImageView ivUserPet1;

    @BindView(R.id.ivUserPet2)
    ImageView ivUserPet2;


    User mUser;
    Boolean isOtherUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);
        ButterKnife.bind(this);

        mUser = null;
        Intent userProfileIntent = getIntent();
        String userObjectId = userProfileIntent.getStringExtra(EXTRA_USER_OBJECT_ID);

        if (userObjectId != null && !userObjectId.isEmpty()) {
            isOtherUser = true;
            queryUser(userObjectId, this);
        } else {
            isOtherUser = false;
            mUser = (User) User.getCurrentUser();
            loadData();
        }

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
            btSendRequest.setVisibility(View.VISIBLE);
        } else {
            btSendRequest.setVisibility(View.GONE);
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

        // TODO again, should be recyclerview later
        ImageHelper.loadImage(this, mUser.getProfileImage(), R.drawable.account_plus, ivUserProfileImage);
        //ImageHelper.loadImage(getContext(), mUser.getPets().get(0).getProfileImage(), R.drawable.cat, ivUserPet1);
        //ImageHelper.loadImage(getContext(), mUser.getPets().get(1).getProfileImage(), R.drawable.cat, ivUserPet2);

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

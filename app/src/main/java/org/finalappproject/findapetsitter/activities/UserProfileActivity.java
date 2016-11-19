package org.finalappproject.findapetsitter.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.finalappproject.findapetsitter.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aoi on 11/18/2016.
 */

public class UserProfileActivity extends AppCompatActivity {
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

    // TODO following will be horizontal recyclerview
    @BindView(R.id.ivUserPet1)
    ImageView ivUserPet1;

    @BindView(R.id.ivUserPet2)
    ImageView ivUserPet2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);
        ButterKnife.bind(this);
        loadData();
    }

    private void loadData() {
        tvUserName.setText(getIntent().getStringExtra("full_name"));
        tvUserNickname.setText(getIntent().getStringExtra("nickname"));
        tvUserDescription.setText(getIntent().getStringExtra("tagline"));
        tvUserPhoneNumber.setText(String.format("Phone number: %s", getIntent().getStringExtra("phoneNumber")));
        tvUserAddress.setText(String.format("Live in: %s, %s", getIntent().getStringExtra("city"), getIntent().getStringExtra("state")));


        // TODO again, should be recyclerview later
        Glide.with(this).load(getIntent().getStringExtra("profile_pic")).placeholder(R.drawable.account_plus).into(ivUserProfileImage);
//        ImageHelper.loadImage(getContext(), mUser.getPets().get(0).getProfileImage(), R.drawable.cat, ivUserPet1);
        //ImageHelper.loadImage(getContext(), mUser.getPets().get(1).getProfileImage(), R.drawable.cat, ivUserPet2);

    }
}

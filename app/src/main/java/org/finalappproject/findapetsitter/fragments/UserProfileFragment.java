package org.finalappproject.findapetsitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    // TODO following will be horizontal recyclerview
    @BindView(R.id.ivUserPet1)
    ImageView ivUserPet1;

    @BindView(R.id.ivUserPet2)
    ImageView ivUserPet2;

    User mUser;

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
    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        // Currently this fragment doesn't require parameters, in the future we may want to
        return fragment;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);

        mUser = (User)User.getCurrentUser();
        loadData();
        return view;
    }


    private void loadData() {
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
        // TODO again, should be recyclerview later
        ImageHelper.loadImage(getContext(), mUser.getProfileImage(), R.mipmap.ic_launcher, ivUserProfileImage);
        ImageHelper.loadImage(getContext(), mUser.getPets().get(0).getProfileImage(), R.drawable.cat, ivUserPet1);
        //ImageHelper.loadImage(getContext(), mUser.getPets().get(1).getProfileImage(), R.drawable.cat, ivUserPet2);
    }
}

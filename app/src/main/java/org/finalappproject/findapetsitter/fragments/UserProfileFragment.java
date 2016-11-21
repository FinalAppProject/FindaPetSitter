package org.finalappproject.findapetsitter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.UserProfileEditActivity;
import org.finalappproject.findapetsitter.adapters.PetsAdapter;
import org.finalappproject.findapetsitter.adapters.RequestsAdapter;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.Pet;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;
import org.finalappproject.findapetsitter.util.recyclerview.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.R.id.rvPets;

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

    User mUser;
    List<Pet> mPets;
    PetsAdapter mPetsAdapter;

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

        // Initialize member variables
        mUser = (User) User.getCurrentUser();
        mPets = new ArrayList<>();
        mPetsAdapter = new PetsAdapter(getContext(), mPets);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);
        setupPetsRecyclerView();

        loadData();

        return view;
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
}

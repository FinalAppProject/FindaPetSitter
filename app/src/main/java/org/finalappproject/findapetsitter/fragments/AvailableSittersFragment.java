package org.finalappproject.findapetsitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.adapters.SittersAdapter;
import org.finalappproject.findapetsitter.model.Address;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AvailableSittersFragment extends UserListFragment {

    private static final String LOG_TAG = "AvailableSittersFrag";

    private LinearLayoutManager mAvailableSittersLinearlayout;
    private ArrayList<SendAdapterObject> mAllSittersList;

    private SittersAdapter mAvailableSittersAdapter;
    ParseGeoPoint ownerGeoPoint;

    @BindView(R.id.rvAvailableSitters)
    RecyclerView mAvailableSittersRecyclerView;
    @BindView(R.id.swipeContainerAvailableSitters)
    SwipeRefreshLayout mAvailableSittersSwipeRefreshLayout;

    @Override
    void populateList() {
        getUserGeoLocation();
        User.queryPetSittersNear(ownerGeoPoint, new FindCallback<User>() {
            public void done(List<User> petSitters, ParseException e) {
                List<User> availableSittersList = new LinkedList<>();
                if (e == null) {
                    User curUser = (User) User.getCurrentUser();
                    for (User u : petSitters) {
                        if (u.getObjectId().equals(curUser.getObjectId())) {
                            continue;
                        }
                        availableSittersList.add(u);
                    }
                    getDistances(availableSittersList);
                    mAvailableSittersAdapter.notifyDataSetChanged();
                } else {
                    Log.e(LOG_TAG, "Failed to fetch pet sitters", e);
                    Toast.makeText(getContext(), "Failed to fetch pet sitters", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);

        mAllSittersList = new ArrayList<>();
        mAvailableSittersAdapter = new SittersAdapter(getContext(), mAllSittersList);

        View availableSittersView = inflater.inflate(R.layout.fragment_available_sitters, parent, false);
        ButterKnife.bind(this, availableSittersView);

        mAvailableSittersRecyclerView.setAdapter(mAvailableSittersAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mAvailableSittersRecyclerView.addItemDecoration(itemDecoration);

        mAvailableSittersLinearlayout = new LinearLayoutManager(getActivity());
        mAvailableSittersLinearlayout.setOrientation(LinearLayoutManager.VERTICAL);
        mAvailableSittersLinearlayout.scrollToPosition(0);
        mAvailableSittersRecyclerView.setLayoutManager(mAvailableSittersLinearlayout);

        mAvailableSittersSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAllSittersList.clear();
                mAvailableSittersAdapter.notifyDataSetChanged();
                populateList();
                mAvailableSittersSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mAllSittersList.clear();
        return availableSittersView;
    }

    void getUserGeoLocation() {
        User currentUser = (User) User.getCurrentUser();
        try {
            Address userAddress = currentUser.getAddress().fetchIfNeeded();
            ownerGeoPoint = userAddress.getGeoPoint();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to fetch user address coordinates", e);
        }
    }

    void getDistances(List<User> availableSittersList){
        for (User sitter :  availableSittersList) {
            try {
                Address userAddress = sitter.getAddress().fetchIfNeeded();
                ParseGeoPoint point = userAddress.getGeoPoint();
                double distance = ownerGeoPoint.distanceInMilesTo(point);
                mAllSittersList.add(new SendAdapterObject (sitter, distance));
            } catch (Exception e) {
                Log.e(LOG_TAG, "Failed to fetch user address coordinates", e);
            }
        }
        Collections.sort(mAllSittersList);
    }

    public class SendAdapterObject implements Comparable<SendAdapterObject>{
        public User user;
        public double distance;
        public SendAdapterObject(User u, double d){
            this.user = u;
            this.distance = d;
        }

        @Override
        public int compareTo(SendAdapterObject o) {
            return ((int)this.distance - (int)o.distance);
        }
    }
}
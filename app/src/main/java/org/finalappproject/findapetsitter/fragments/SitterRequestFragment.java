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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.adapters.RequestsAdapter;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aoi on 11/12/2016.
 */

public class SitterRequestFragment extends Fragment{

    private static final String LOG_TAG = "SitterRequestFragment";
    private ArrayList<Request> mRequests = new ArrayList<>();
    private RequestsAdapter aRequests;

    User mUser = (User) User.getCurrentUser();
    @BindView(R.id.rvRequestsToSitter)
    RecyclerView rvRequests;
    @BindView(R.id.swipeContainerReceivedRequest)
    SwipeRefreshLayout swipeLayoutRequests;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sitter_request, container, false);
        ButterKnife.bind(this, view);

        aRequests = new RequestsAdapter(getContext(), mRequests);
        rvRequests.setAdapter(aRequests);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvRequests.addItemDecoration(itemDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        rvRequests.setLayoutManager(linearLayoutManager);

        swipeLayoutRequests.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequests.clear();
                aRequests.notifyDataSetChanged();
                populateList();
                swipeLayoutRequests.setRefreshing(false);
            }
        });

        mRequests.clear();
        return view;
    }

    private void populateList() {
        Request.queryReceiver(mUser, new FindCallback<Request>() {
            @Override
            public void done(List<Request> objects, ParseException e) {
                if (e == null) {
                    mRequests.addAll(objects);
                    aRequests.notifyDataSetChanged();
                } else {
                    Log.e(LOG_TAG, "Failed to fetch request", e);
                    Toast.makeText(getContext(), "Failed to fetch requests", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateList();
    }

}

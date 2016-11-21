package org.finalappproject.findapetsitter.fragments;

import android.content.Context;
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
import butterknife.Unbinder;

import static java.util.Collections.addAll;

/**
 * Requests fragment shows a list of requests by sender or receiver, depending on the context
 */
public class RequestsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FindCallback<Request> {

    private static final String LOG_TAG = "RequestsFragment";

    private static final String ARGUMENT_IS_SITTER_FLOW = "is_pet_sitter_flow";

    @BindView(R.id.rvRequestsToSitter)
    RecyclerView rvRequests;

    @BindView(R.id.swipeContainerReceivedRequest)
    SwipeRefreshLayout swipeLayoutRequests;

    private Unbinder mUnbinder;
    private boolean mIsPetSitterFlow;
    private ArrayList<Request> mRequests;
    private RequestsAdapter mRequestsAdapter;
    User mUser;

    /**
     * Required empty public constructor,
     * use newInstance() factory method instead
     */
    @Deprecated
    public RequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method used to create PetOwnerHomeFragment
     * with parameters, use this method instead of
     * new RequestsFragment()
     *
     * @return A new instance of fragment RequestsFragment.
     */
    public static RequestsFragment newInstance(boolean isPetSitterFlow) {
        RequestsFragment fragment = new RequestsFragment();
        // Add arguments
        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putBoolean(ARGUMENT_IS_SITTER_FLOW, isPetSitterFlow);
        fragment.setArguments(savedInstanceState);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve arguments
        if (getArguments() != null) {
            mIsPetSitterFlow = getArguments().getBoolean(ARGUMENT_IS_SITTER_FLOW);
        }

        // Initialize member variables
        mRequests = new ArrayList<>();
        mRequestsAdapter = new RequestsAdapter(getContext(), mRequests, mIsPetSitterFlow);
        mUser = (User) User.getCurrentUser();
        // Fetch requests
        fetchRequests();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sitter_request, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        setupRecyclerView();
        setupSwipeRefresh();

        return view;
    }

    void setupRecyclerView() {
        rvRequests.setAdapter(mRequestsAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvRequests.addItemDecoration(itemDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        rvRequests.setLayoutManager(linearLayoutManager);
    }

    void setupSwipeRefresh() {
        swipeLayoutRequests.setOnRefreshListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    /**
     * SwipeRefresh onRefresh listener
     */
    @Override
    public void onRefresh() {
        int requestsCount = mRequests.size();
        mRequests.clear();
        mRequestsAdapter.notifyItemRangeRemoved(0, requestsCount);
        fetchRequests();
        swipeLayoutRequests.setRefreshing(false);

    }

    private void fetchRequests() {
        if (mIsPetSitterFlow) {
            Request.queryByReceiver(mUser, this);
        } else {
            Request.queryBySender(mUser, this);
        }
    }

    @Override
    public void done(List<Request> objects, ParseException e) {

        if (e == null) {
            for (Request request : objects) {
                try {
                    request.getReceiver().fetchIfNeeded();
                    request.getSender().fetchIfNeeded();
                } catch (ParseException ex) {
                    // This will occur when users associated to the request have been deleted
                    continue;
                }
                mRequests.add(request);
            }
            mRequestsAdapter.notifyDataSetChanged();
        } else {
            Log.e(LOG_TAG, "Failed to fetch request", e);
            Toast.makeText(getContext(), "Failed to fetch requests", Toast.LENGTH_LONG).show();
        }
    }

}

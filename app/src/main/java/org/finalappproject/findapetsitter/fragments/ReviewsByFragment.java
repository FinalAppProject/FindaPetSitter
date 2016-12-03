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
import org.finalappproject.findapetsitter.adapters.ReviewsByAdapter;
import org.finalappproject.findapetsitter.model.Review;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReviewsByFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FindCallback<Review> {

    private static final String LOG_TAG = "ReviewsByFragment";

    private ArrayList<Review> mReviewsBy;

    private ReviewsByAdapter mReviewsByAdapter;
    User mUser;
    private Unbinder mUnbinder;

    @BindView(R.id.rvReviewsBy)
    RecyclerView rvReviewsBy;
    @BindView(R.id.swipeContainerReviewsBy)
    SwipeRefreshLayout mReviewsBySwipeRefreshLayout;

    public static org.finalappproject.findapetsitter.fragments.ReviewsByFragment newInstance() {
        org.finalappproject.findapetsitter.fragments.ReviewsByFragment fragment = new org.finalappproject.findapetsitter.fragments.ReviewsByFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewsBy = new ArrayList<>();
        mReviewsByAdapter = new ReviewsByAdapter(getContext(), mReviewsBy);
        mUser = (User) User.getCurrentUser();
        fetchReviews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_reviews_by, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        setupRecyclerView();
        setupSwipeRefresh();

        return view;
    }

    void setupRecyclerView() {
        rvReviewsBy.setAdapter(mReviewsByAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvReviewsBy.addItemDecoration(itemDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        rvReviewsBy.setLayoutManager(linearLayoutManager);
    }

    void setupSwipeRefresh() {
        mReviewsBySwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onRefresh() {
        int reviewsCount = mReviewsBy.size();
        mReviewsBy.clear();
        mReviewsByAdapter.notifyItemRangeRemoved(0, reviewsCount);
        fetchReviews();
        mReviewsBySwipeRefreshLayout.setRefreshing(false);
    }

    private void fetchReviews() {
        Review.queryBySender(mUser, this);
    }

    @Override
    public void done(List<Review> objects, ParseException e) {

        if (e == null) {
            for (Review review : objects) {
                try {
                    review.getReviewer().fetchIfNeeded(); // TODO: sure?
                } catch (ParseException ex) {
                    // This will occur when users associated to the request have been deleted
                    continue;
                }
                mReviewsBy.add(review);
            }
            mReviewsByAdapter.notifyDataSetChanged();
        } else {
            Log.e(LOG_TAG, "Failed to fetch request", e);
            Toast.makeText(getContext(), "Failed to fetch requests", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unbind views
        mUnbinder.unbind();
    }

}

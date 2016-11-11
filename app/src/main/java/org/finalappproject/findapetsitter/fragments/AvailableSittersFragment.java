package org.finalappproject.findapetsitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.util.recyclerview.DividerItemDecoration;
import org.finalappproject.findapetsitter.adapters.SittersAdapter;
import org.finalappproject.findapetsitter.model.Sitter;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AvailableSittersFragment extends UserListFragment {

    private boolean isDebug = true;

    private LinearLayoutManager mAvailableSittersLinearlayout;
    private LinkedList<Sitter> mAvailableSitters;  //TODO: if we want to display search results based on distance, do we need linkedlist or arraylist is ok?
    private SittersAdapter mRecyclerViewAvailableSittersAdapter;

    @BindView(R.id.rvAvailableSitters)
    RecyclerView mRecyclerViewAvailableSitters;
    @BindView(R.id.swipeContainerAvailableSitters)
    SwipeRefreshLayout mAvailableSittersSwipeRefreshLayout;

    @Override
    void populateList() {
        if (isDebug) {
            populate_fake();
        }
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View availableSittersView = inflater.inflate(R.layout.fragment_available_sitters, parent, false);
        ButterKnife.bind(this, availableSittersView);

        mRecyclerViewAvailableSitters.setAdapter(mRecyclerViewAvailableSittersAdapter);


        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerViewAvailableSitters.addItemDecoration(itemDecoration);

        // Setup layout manager for items
        mAvailableSittersLinearlayout = new LinearLayoutManager(getActivity());
        // Control orientation of the items
        // also supports LinearLayoutManager.HORIZONTAL
        mAvailableSittersLinearlayout.setOrientation(LinearLayoutManager.VERTICAL);
        // Optionally customize the position you want to default scroll to
        mAvailableSittersLinearlayout.scrollToPosition(0);
        // Set layout manager to position the items
        // Attach the layout manager to the recycler view

        mRecyclerViewAvailableSitters.setLayoutManager(mAvailableSittersLinearlayout);

        return availableSittersView;
    }

    void populate_fake() {
    }
}
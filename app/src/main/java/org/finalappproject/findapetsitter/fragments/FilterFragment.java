package org.finalappproject.findapetsitter.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.finalappproject.findapetsitter.R;

//TODO WILL BE REPLACED BY ACTIVITY SEARCH!!!!

public class FilterFragment extends DialogFragment {

    //use this to get the fileds that we need to prepopulate in filters
    public static FilterFragment newInstance(String title) {
        FilterFragment frag = new FilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_filter, container);
        return inflater.inflate(R.layout.activity_search_filter, container);
    }


}

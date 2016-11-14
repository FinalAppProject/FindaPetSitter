package org.finalappproject.findapetsitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MyPetsFragment extends Fragment {

    public static MyPetsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MyPetsFragment fragment = new MyPetsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

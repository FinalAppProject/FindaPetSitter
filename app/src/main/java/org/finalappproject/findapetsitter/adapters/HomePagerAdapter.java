package org.finalappproject.findapetsitter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.finalappproject.findapetsitter.fragments.AvailableSittersFragment;
import org.finalappproject.findapetsitter.fragments.NearbySittersFragment;

import java.util.ArrayList;

public class HomePagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> tabsTitles = new ArrayList<String>(){{
        add("Pet Sitters");
        add("Nearby Pet Sitters");
    }};

    public HomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 2;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AvailableSittersFragment();
            case 1:
                return NearbySittersFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitles.get(position);
    }
}
package org.finalappproject.findapetsitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import org.finalappproject.findapetsitter.R;

/**
 * Created by Aoi on 11/14/2016.
 */

public class SitterHomeFragment extends Fragment {

    /**
     * Required empty public constructor,
     * use newInstance() factory method instead
     */
    @Deprecated
    public SitterHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method used to create SitterHomeFragment
     * with parameters, use this method instead of
     * new SitterHomeFragment()
     *
     * @return A new instance of fragment SitterHomeFragment.
     */
    public static SitterHomeFragment newInstance() {
        SitterHomeFragment fragment = new SitterHomeFragment();
        // Currently this fragment doesn't require parameters, in the future we may want to
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sitter_home, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerSitterHome);
        viewPager.setAdapter(new SitterHomeFragment.SitterHomeFragmentPagerAdapter(getFragmentManager()));

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabsSitterHome);
        tabsStrip.setViewPager(viewPager);

        return view;
    }

    public static class SitterHomeFragmentPagerAdapter extends FragmentPagerAdapter {
        private final static int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{"Timeline", "Requests"};
        private final SitterTimelineFragment sitterTimelineFragment = new SitterTimelineFragment();
        private final RequestsFragment sitterRequestFragment = RequestsFragment.newInstance(true);


        public SitterHomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return sitterTimelineFragment;
            } else if (position == 1) {
                return sitterRequestFragment;
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}

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
import org.finalappproject.findapetsitter.model.User;

public class ReviewsFragment extends Fragment {

    @Deprecated
    public ReviewsFragment() {

    }

    public static org.finalappproject.findapetsitter.fragments.ReviewsFragment newInstance() {
        org.finalappproject.findapetsitter.fragments.ReviewsFragment fragment = new org.finalappproject.findapetsitter.fragments.ReviewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerReviews);
        viewPager.setAdapter(new org.finalappproject.findapetsitter.fragments.ReviewsFragment.SitterHomeFragmentPagerAdapter(getFragmentManager()));

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabsReviews);
        tabsStrip.setViewPager(viewPager);

        return view;
    }

    public static class SitterHomeFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
        private final static int PAGE_COUNT = 2;
        private int tabIcons[] = new int[]{R.drawable.review_of_me, R.drawable.review_of_others};

        public SitterHomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                User user = (User) User.getCurrentUser();
                return ReviewsAboutFragment.newInstance(user.getObjectId());
            } else if (position == 1) {
                return ReviewsByFragment.newInstance();
            } else {
                return null;
            }
        }

        @Override
        public int getPageIconResId(int position) {
            return tabIcons[position];
        }

        @Override
        public int getCount() {
            return tabIcons.length;
        }
    }
}

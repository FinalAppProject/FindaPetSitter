package org.finalappproject.findapetsitter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.fragments.SitterRequestFragment;
import org.finalappproject.findapetsitter.fragments.SitterTimelineFragment;

/**
 * Created by Aoi on 11/12/2016.
 */

public class SitterHomeActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_home);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerSitterHome);
        viewPager.setAdapter(new SitterHomeFragmentPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabsSitterHome);
        tabsStrip.setViewPager(viewPager);
    }

    public static class SitterHomeFragmentPagerAdapter extends FragmentPagerAdapter {
        private final static int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{"Timeline", "Requests"};
        private final SitterTimelineFragment sitterTimelineFragment = new SitterTimelineFragment();
        private final SitterRequestFragment sitterRequestFragment = new SitterRequestFragment();


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

package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.adapters.HomePagerAdapter;
import org.finalappproject.findapetsitter.fragments.AvailableSittersFragment;
import org.finalappproject.findapetsitter.fragments.FavoriteSittersFragment;
import org.finalappproject.findapetsitter.fragments.FilterFragment;
import org.finalappproject.findapetsitter.fragments.MyPetsFragment;
import org.finalappproject.findapetsitter.fragments.PetOwnerHomeFragment;
import org.finalappproject.findapetsitter.fragments.SitterHomeFragment;
import org.finalappproject.findapetsitter.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static android.R.attr.fragment;
import static android.R.attr.orientation;
import static org.finalappproject.findapetsitter.R.id.vpPager;

import static com.parse.ParseUser.getCurrentUser;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG_LOG = "HomeActivity";

    private static final String STATE_CURRENT_FRAGMENT_TAG = "current_fragment_tag";
    private static final String TAG_OWNER_FRAGMENT = "owner_fragment";
    private static final String TAG_PROFILE_FRAGMENT = "profile_fragment";
    private static final String TAG_SITTER_FRAGMENT = "sitter_fragment";

    private ActionBarDrawerToggle drawerToggle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // @BindView(R.id.vpPager) ViewPager vpPager;
    // FragmentPagerAdapter mAdapterViewPager;

    // @BindView(R.id.fab)
    // FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nvView)
    NavigationView nvDrawer;


    private String mCurrentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setUpViews();

        if (savedInstanceState == null) {
            nvDrawer.getMenu().performIdentifierAction(R.id.nav_home_fragment, 0);
        }
    }

    void setUpViews() {
        setSupportActionBar(toolbar);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        //associate ViewPager with a new instance of our adapter:
        //mAdapterViewPager = new HomePagerAdapter(getSupportFragmentManager());
        //vpPager.setAdapter(mAdapterViewPager);


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Open up the Map Search View", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        setupDrawerContent(nvDrawer);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.miFilter:
                showFilterDialog();
                return true;
            case R.id.miUserProfile:
                startUserProfileActivity();
                return true;
            case R.id.miMap:
                startNearbySittersActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_CURRENT_FRAGMENT_TAG, mCurrentFragmentTag);
        // Save current fragment tag name
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore current fragment tag name
        mCurrentFragmentTag = savedInstanceState.getString(STATE_CURRENT_FRAGMENT_TAG);
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.show(fm, "fragment_filter");
    }

    private void startUserProfileActivity() {
        Intent userProfileIntent = new Intent(this, UserProfileEditActivity.class);
        startActivity(userProfileIntent);
    }

    private void startNearbySittersActivity() {
        Intent nearbySittersIntent = new Intent(this, NearbySittersActivity.class);
        startActivity(nearbySittersIntent);
    }

    //--------------- Navigation drawer ------------------------//
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

//    void loadContentFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        mCurrentFragment = fm.findFragmentByTag(TAG_CURRENT_FRAGMENT);
//
//        if (mCurrentFragment == null) {
//            // If there's no current fragment we load the pet owner's fragment as being the current fragment
//            mCurrentFragment = addPetOwnerHomeFragment(fm);
//        }
//
//        setCurrentFragment(fm, mCurrentFragment);
//    }

//    void setCurrentFragment(FragmentManager fm, Fragment fragment) {
//        // Replace content fragment with current fragment instance
//        fm.beginTransaction()
//                .replace(R.id.flContent, fragment)
//                .add(fragment, TAG_CURRENT_FRAGMENT)
//                .commit();
//    }


    public void selectDrawerItem(MenuItem menuItem) {
        FragmentManager fm = getSupportFragmentManager();
        String fragmentToShowTag = null;
        User user = (User) User.getCurrentUser();

        if (mCurrentFragmentTag != null) {
            fm.beginTransaction().hide(fm.findFragmentByTag(mCurrentFragmentTag)).commit();
        }

        switch (menuItem.getItemId()) {
//            case R.id.nav_sittersList_fragment:
//                fragmentClass = AvailableSittersFragment.class;
//                break;
//            case R.id.nav_favSittersList_fragment:
//                fragmentClass = FavoriteSittersFragment.class;
//                break;
            case R.id.nav_logout_fragment:
                Intent intentLogout = new Intent(this, LoginActivity.class);
                boolean isLogout = true;
                intentLogout.putExtra("logout", isLogout);
                startActivity(intentLogout);
                break;
            case R.id.nav_switch_owner_sitter:
                fragmentToShowTag = TAG_SITTER_FRAGMENT;
                break;
            case R.id.nav_profile_fragment:
                fragmentToShowTag = TAG_PROFILE_FRAGMENT;
                break;
            case R.id.nav_home_fragment:
            default:
                fragmentToShowTag = TAG_OWNER_FRAGMENT;
                break;
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();

        if (fragmentToShowTag != null) {
            mCurrentFragmentTag = fragmentToShowTag;
            Fragment fragmentToShow = fm.findFragmentByTag(fragmentToShowTag);
            // Create and add fragment
            if (fragmentToShow == null) {
                fragmentToShow = instantiateFragment(fm, fragmentToShowTag);
            }
            fm.beginTransaction().show(fragmentToShow).commit();
        }
    }

    private Fragment instantiateFragment(FragmentManager fm, String tag) {
        Fragment fragment = null;

        if (TAG_OWNER_FRAGMENT.equals(tag)) {
            fragment = PetOwnerHomeFragment.newInstance();
        } else if (TAG_PROFILE_FRAGMENT.equals(tag)) {
            fragment = UserProfileFragment.newInstance();
        } else if (TAG_SITTER_FRAGMENT.equals(tag)) {
            fragment = SitterHomeFragment.newInstance();
        }

        fm.beginTransaction()
                    .add(R.id.flContent, fragment, tag)
                    .commit();

        return fragment;
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


}
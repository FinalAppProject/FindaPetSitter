package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.LogOutCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.fragments.FilterFragment;
import org.finalappproject.findapetsitter.fragments.PetOwnerHomeFragment;
import org.finalappproject.findapetsitter.fragments.ReviewsAboutFragment;
import org.finalappproject.findapetsitter.fragments.ReviewsByFragment;
import org.finalappproject.findapetsitter.fragments.SitterHomeFragment;
import org.finalappproject.findapetsitter.fragments.UserProfileFragment;
import org.finalappproject.findapetsitter.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.application.AppConstants.PREFERENCE_CURRENT_USERNAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_LOG = "HomeActivity";

    private static final String STATE_CURRENT_FRAGMENT_TAG = "current_fragment_tag";
    private static final String TAG_OWNER_FRAGMENT = "owner_fragment";
    private static final String TAG_PROFILE_FRAGMENT = "profile_fragment";
    private static final String TAG_SITTER_FRAGMENT = "sitter_fragment";
    private static final String TAG_REVIEWS_ABOUT_FRAGMENT = "reviews_about_fragment";
    private static final String TAG_REVIEWS_BY_FRAGMENT = "reviews_by_fragment";


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nvView)
    NavigationView nvDrawer;

    private ActionBarDrawerToggle mDrawerToggle;

    private String mCurrentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Bind views
        ButterKnife.bind(this);
        // Set support toolbar
        setSupportActionBar(toolbar);
        // Setup navigation drawer
        setupNavigationDrawer();

        if (savedInstanceState == null) {
            nvDrawer.getMenu().performIdentifierAction(R.id.nav_home_fragment, 0);
        }
    }

    void setupNavigationDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        // Note that this activity implements NavigationView.OnNavigationItemSelectedListener
        // which requires onNavigationItemSelected for handling the Drawer Menu Item selection
        nvDrawer.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Have the action bar toggle handle the menu item selection
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // If menu item hasn't been handled it comes from the support toolbar
        switch (item.getItemId()) {
//            case R.id.miFilter:
//                showFilterDialog();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();

        String fragmentToShowTag = null;
        switch (item.getItemId()) {
            case R.id.nav_logout_fragment:
                logOut();
                break;
            case R.id.nav_request_fragment:
                fragmentToShowTag = TAG_SITTER_FRAGMENT;
                break;
            case R.id.nav_reviews_about:
                fragmentToShowTag = TAG_REVIEWS_ABOUT_FRAGMENT;
                break;
            case R.id.nav_reviews_by:
                fragmentToShowTag = TAG_REVIEWS_BY_FRAGMENT;
                break;
            case R.id.nav_profile_fragment:
                fragmentToShowTag = TAG_PROFILE_FRAGMENT;
                break;
            case R.id.nav_home_fragment:
            default:
                fragmentToShowTag = TAG_OWNER_FRAGMENT;
                break;
        }

        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawer.closeDrawers();

        //
        if (fragmentToShowTag != null) {
            // Hide current fragment
            if (mCurrentFragmentTag != null) {
                fm.beginTransaction().hide(fm.findFragmentByTag(mCurrentFragmentTag)).commit();
            }
            // Replace current fragment tag with selected fragment
            mCurrentFragmentTag = fragmentToShowTag;
            Fragment fragmentToShow = fm.findFragmentByTag(fragmentToShowTag);
            // Create and add selected fragment if that hasn't been done yet
            if (fragmentToShow == null) {
                fragmentToShow = instantiateFragment(fm, fragmentToShowTag);
            }
            // Show the selected fragment
            fm.beginTransaction().show(fragmentToShow).commit();
            return true;
        }

        return false;
    }

    private Fragment instantiateFragment(FragmentManager fm, String tag) {
        Fragment fragment = null;

        if (TAG_OWNER_FRAGMENT.equals(tag)) {
            fragment = PetOwnerHomeFragment.newInstance();
        } else if (TAG_PROFILE_FRAGMENT.equals(tag)) {
            fragment = UserProfileFragment.newInstance();
        } else if (TAG_SITTER_FRAGMENT.equals(tag)) {
            //fragment = RequestsFragment.newInstance(true);
            fragment = SitterHomeFragment.newInstance();
        } else if (TAG_REVIEWS_ABOUT_FRAGMENT.equals(tag)) {
            fragment = ReviewsAboutFragment.newInstance();
        } else if (TAG_REVIEWS_BY_FRAGMENT.equals(tag)) {
            fragment = ReviewsByFragment.newInstance();
        }

        fm.beginTransaction()
                .add(R.id.flContent, fragment, tag)
                .commit();

        return fragment;
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Current user log out
     */
    private void logOut() {
        User user = (User) User.getCurrentUser();
        // Save current user name into preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(PREFERENCE_CURRENT_USERNAME, user.getUsername()).apply();

        user.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                Intent intentLogout = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intentLogout);
            }
        });
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.show(fm, "fragment_filter");
    }

}
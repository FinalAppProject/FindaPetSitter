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
import android.view.View;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.fragments.FilterFragment;
import org.finalappproject.findapetsitter.fragments.PetOwnerHomeFragment;
import org.finalappproject.findapetsitter.fragments.ReviewsFragment;
import org.finalappproject.findapetsitter.fragments.SitterHomeFragment;
import org.finalappproject.findapetsitter.fragments.UserProfileFragment;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;
import static org.finalappproject.findapetsitter.application.AppConstants.PREFERENCE_CURRENT_USERNAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_LOG = "HomeActivity";

    private static final String STATE_CURRENT_FRAGMENT_TAG = "current_fragment_tag";
    private static final String TAG_OWNER_FRAGMENT = "owner_fragment";
    public static final String TAG_OWN_PROFILE_FRAGMENT = "profile_own_fragment";
    public static final String TAG_OTHERS_PROFILE_FRAGMENT = "profile_others_fragment";
    private static final String TAG_SITTER_FRAGMENT = "sitter_fragment";
    private static final String TAG_REVIEWS_FRAGMENT = "reviews_fragment";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nvView)
    NavigationView nvDrawer;

    private ActionBarDrawerToggle mDrawerToggle;
    private View headerLayout;

    TextView tvNavHeaderName;
    TextView tvNavHeaderUserName;
    CircleImageView ivProfilePic;


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

        User user = (User) User.getCurrentUser();
        // Start the edit profile if it's the 1st login
        if (user.getProfileImage() == null && user.getParseObject("address") == null) {
            Intent editProfileIntent = new Intent(this, UserProfileEditActivity.class);
            startActivity(editProfileIntent);
        }

    }

    void setupNavigationDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                setupNavigationDrawerHeader();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        // Note that this activity implements NavigationView.OnNavigationItemSelectedListener
        // which requires onNavigationItemSelected for handling the Drawer Menu Item selection
        nvDrawer.setNavigationItemSelectedListener(this);
    }

    void setupNavigationDrawerHeader(){
        bindHeader();
        User user = (User) User.getCurrentUser(); //TODO: make it class variable and use it for logout?
        loadData(user);
    }

    void bindHeader(){
        headerLayout = nvDrawer.getHeaderView(0);
        ivProfilePic = (CircleImageView) headerLayout.findViewById(R.id.ivNavHeaderProfileImage);
        tvNavHeaderName = (TextView) headerLayout.findViewById(R.id.tvNavHeaderName);
        tvNavHeaderUserName = (TextView) headerLayout.findViewById(R.id.tvNavHeaderUserName);
    }

    private void loadData(User user) {
        tvNavHeaderName.setText(user.getFullName());
        tvNavHeaderUserName.setText(user.getUsername());
        ImageHelper.loadImage(this, user.getProfileImage(), R.drawable.account_plus, ivProfilePic);
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
            case R.id.nav_reviews:
                fragmentToShowTag = TAG_REVIEWS_FRAGMENT;
                break;
            case R.id.nav_profile_fragment:
                fragmentToShowTag = TAG_OWN_PROFILE_FRAGMENT;
                break;
            case R.id.nav_home_fragment:
            default:
                fragmentToShowTag = TAG_OWNER_FRAGMENT;
                break;
        }

        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawer.closeDrawers();

        // Force removing sitter fragment if it's there
        Fragment sitterFragment = fm.findFragmentByTag(TAG_OTHERS_PROFILE_FRAGMENT);
        if (sitterFragment != null) {
            fm.beginTransaction().remove(sitterFragment).commit();
        }


        if (fragmentToShowTag != null) {
            if (mCurrentFragmentTag != null) {
                Fragment fragmentToHide = fm.findFragmentByTag(mCurrentFragmentTag);
                if (fragmentToHide != null) {
                    fm.beginTransaction().hide(fragmentToHide).commit();
                }
            }
            mCurrentFragmentTag = fragmentToShowTag;
            Fragment fragmentToShow = fm.findFragmentByTag(fragmentToShowTag);
            if (fragmentToShow == null) {
                fragmentToShow = instantiateFragment(fm, fragmentToShowTag);
            }
            fm.beginTransaction().show(fragmentToShow).commit();
            return true;
        }
        return false;
    }

    public void showUserProfileFragment(String petSitterObjectId) {
        FragmentManager fm = getSupportFragmentManager();

        // Hide current fragment
        if (mCurrentFragmentTag != null) {
            fm.beginTransaction().hide(fm.findFragmentByTag(mCurrentFragmentTag)).commit();
        }

        Fragment fragmentToShow = UserProfileFragment.newInstance(petSitterObjectId);
        fm.beginTransaction()
                .addToBackStack(null)
                .add(R.id.flContent, fragmentToShow, TAG_OTHERS_PROFILE_FRAGMENT)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Show current fragment
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().show(fm.findFragmentByTag(mCurrentFragmentTag)).commit();
    }

    private Fragment instantiateFragment(FragmentManager fm, String tag) {
        Fragment fragment = null;

        if (TAG_OWNER_FRAGMENT.equals(tag)) {
            fragment = PetOwnerHomeFragment.newInstance();
        } else if (TAG_OWN_PROFILE_FRAGMENT.equals(tag)) {
            fragment = UserProfileFragment.newInstance(null);
        } else if (TAG_SITTER_FRAGMENT.equals(tag)) {
            //fragment = RequestsFragment.newInstance(true);
            fragment = SitterHomeFragment.newInstance();
        } else if (TAG_REVIEWS_FRAGMENT.equals(tag)) {
            fragment = ReviewsFragment.newInstance();
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
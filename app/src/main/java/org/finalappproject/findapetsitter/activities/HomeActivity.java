package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.adapters.HomePagerAdapter;
import org.finalappproject.findapetsitter.fragments.AvailableSittersFragment;
import org.finalappproject.findapetsitter.fragments.FavoriteSittersFragment;
import org.finalappproject.findapetsitter.fragments.FilterFragment;
import org.finalappproject.findapetsitter.fragments.MyPetsFragment;
import org.finalappproject.findapetsitter.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static org.finalappproject.findapetsitter.R.id.vpPager;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "HomeActivity";
    public static final int HOME_NUM_TABS = 2;
    private ActionBarDrawerToggle drawerToggle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //@BindView(R.id.vpPager) ViewPager vpPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nvView) NavigationView nvDrawer;

    //FragmentPagerAdapter mAdapterViewPager;

    //TODO: is this the favorties section? If yes let me know and I will update
    //@BindView(R.id.rvRecentVisit)
    //RecyclerView rvRecentVisit;
    //private ArrayList<House> houses;


    void setUpViews() {
        setSupportActionBar(toolbar);

        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        //associate ViewPager with a new instance of our adapter:
        //mAdapterViewPager = new HomePagerAdapter(getSupportFragmentManager());
        //vpPager.setAdapter(mAdapterViewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Open up the Map Search View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Setup drawer view
        setupDrawerContent(nvDrawer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            // This is the up button
            case R.id.miFilter:
                showFilterDialog();
                return true;
            case R.id.miUserProfile:
                startUserProfileActivity();
                return true;
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.show(fm, "fragment_filter");
    }

    private void startUserProfileActivity() {
        Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
        startActivity(userProfileIntent);
    }


    /*public void setUpHouses() {
        houses = new ArrayList<>();
        // RecyclerView
        // TODO Get Houses
        HouseListAdapter adapter = new HouseListAdapter(this, houses);
        rvRecentVisit.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRecentVisit.setLayoutManager(linearLayoutManager);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Bind views
        //
        ButterKnife.bind(this);

        setUpViews();
        //setUpHouses();
        User user = (User) ParseUser.getCurrentUser();
        String fullName = user.getFullName();

        showHelloMessage(fullName);
    }

    private void showHelloMessage(String fullName) {
        Toast.makeText(getApplicationContext(), "Hi " + fullName, Toast.LENGTH_LONG).show();
        //tvHelloWorld.setText("Hi, " + fullName);
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

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_sittersList_fragment:
                fragmentClass = AvailableSittersFragment.class;
                break;
            case R.id.nav_favSittersList_fragment:
                fragmentClass = FavoriteSittersFragment.class;
                break;
            case R.id.nav_profile_fragment:
                fragmentClass = MyPetsFragment.class;
                break;
/*
            case R.id.nav_logout_fragment:
                fragmentClass = Logout.class;
                break;
*/
            default:
                fragmentClass = MyPetsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
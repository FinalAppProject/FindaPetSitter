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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.fragments.AvailableSittersFragment;
import org.finalappproject.findapetsitter.fragments.FavoriteSittersFragment;
import org.finalappproject.findapetsitter.fragments.FilterFragment;
import org.finalappproject.findapetsitter.fragments.MyPetsFragment;
import org.finalappproject.findapetsitter.fragments.SitterHomeFragment;
import org.finalappproject.findapetsitter.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "HomeActivity";
    public static final int HOME_NUM_TABS = 2;
    private ActionBarDrawerToggle drawerToggle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //@BindView(R.id.vpPager) ViewPager vpPager;
    //FragmentPagerAdapter mAdapterViewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nvView)
    NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setUpViews();
        User user = (User) ParseUser.getCurrentUser();
        String fullName = user.getFullName();

        showHelloMessage(fullName);
        callDefaultFragment();
    }

    void callDefaultFragment(){
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new AvailableSittersFragment());
        tx.commit();
    }

    void setUpViews() {
        setSupportActionBar(toolbar);
        drawerToggle = setupDrawerToggle();
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
            case R.id.nav_switch_owner_sitter:
                fragmentClass = SitterHomeFragment.class;
                break;
            default:
                fragmentClass = AvailableSittersFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
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
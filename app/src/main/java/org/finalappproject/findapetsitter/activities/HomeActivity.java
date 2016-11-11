package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.adapters.HomePagerAdapter;
import org.finalappproject.findapetsitter.adapters.HouseListAdapter;
import org.finalappproject.findapetsitter.application.AppConstants;
import org.finalappproject.findapetsitter.fragments.FilterFragment;
import org.finalappproject.findapetsitter.model.House;
import org.finalappproject.findapetsitter.model.UserProfile;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.id;
import static org.finalappproject.findapetsitter.R.id.rvRecentVisit;
import static org.finalappproject.findapetsitter.R.id.tvHelloWorld;
import static org.finalappproject.findapetsitter.R.id.vpPager;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "HomeActivity";
    public static final int HOME_NUM_TABS = 2;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.vpPager) ViewPager vpPager;
    @BindView(R.id.fab) FloatingActionButton fab;

    FragmentPagerAdapter mAdapterViewPager;

    //TODO: is this the favorties section? If yes let me know and I will update
    //@BindView(R.id.rvRecentVisit)
    //RecyclerView rvRecentVisit;
    //private ArrayList<House> houses;

    void setUpViews(){
        setSupportActionBar(toolbar);
        //TODO: setUpDrawer();

        //associate ViewPager with a new instance of our adapter:
        mAdapterViewPager = new HomePagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(mAdapterViewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Open up the Map Search View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case R.id.miFilter:
                showFilterDialog();
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

        Parcelable userProfileParcel = getIntent().getParcelableExtra(AppConstants.EXTRA_USER_PROFILE);

        String fullName = "Unknown!";
        if (userProfileParcel != null) {

            // TODO TODO TODO this doesn't seem to be working (Investigate issues with Parceler and ParseObjects)
            UserProfile userProfile = (UserProfile) Parcels.unwrap(userProfileParcel);
            fullName = userProfile.getFullName();

            showHelloMessage(fullName);
        } else {
            ParseQuery<UserProfile> query = ParseQuery.getQuery("UserProfile");
            query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
            // TODO check how to query single object
            query.findInBackground(new FindCallback<UserProfile>() {
                public void done(List<UserProfile> userProfiles, ParseException e) {
                    if (e == null) {
                        String fullName = "unknown";
                        if (userProfiles.size() > 0) {
                            fullName = userProfiles.get(0).getFullName();
                        }
                        showHelloMessage(fullName);
                    } else {
                        Log.d(LOG_TAG, "Error: " + e.getMessage(), e);
                    }
                }
            });
        }
    }

    private void showHelloMessage(String fullName) {
        Toast.makeText(getApplicationContext(), "Hi " + fullName, Toast.LENGTH_LONG).show();
        //tvHelloWorld.setText("Hi, " + fullName);
    }
}
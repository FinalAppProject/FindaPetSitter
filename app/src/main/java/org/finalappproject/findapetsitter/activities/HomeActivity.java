package org.finalappproject.findapetsitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.adapters.HouseListAdapter;
import org.finalappproject.findapetsitter.model.House;
import org.finalappproject.findapetsitter.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "HomeActivity";

    @BindView(R.id.tvHelloWorld)
    TextView tvHelloWorld;

    @BindView(R.id.rvRecentVisit)
    RecyclerView rvRecentVisit;

    private ArrayList<House> houses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        houses = new ArrayList<>();
        // Bind views
        ButterKnife.bind(this);

        // RecyclerView
        // TODO Get Houses
        HouseListAdapter adapter = new HouseListAdapter(this, houses);
        rvRecentVisit.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRecentVisit.setLayoutManager(linearLayoutManager);

        User user = (User) ParseUser.getCurrentUser();
        String fullName = user.getFullName();

        showHelloMessage(fullName);
    }

    private void showHelloMessage(String fullName) {

        tvHelloWorld.setText("Hi, " + fullName);

    }
}

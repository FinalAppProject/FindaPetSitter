package org.finalappproject.findapetsitter.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.fragments.RequestFragment;

public class RequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sitter_id = getIntent().getStringExtra("sitter_id");
        setContentView(R.layout.activity_request);
        launchRequestDailog(sitter_id);
    }

    void launchRequestDailog(String sitter_id){
        Bundle bundle = new Bundle();
        bundle.putString("sitter_id", sitter_id);

        RequestFragment requestFragmentDialog = new RequestFragment();
        requestFragmentDialog.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        requestFragmentDialog.show(fm, "request");
    }
}
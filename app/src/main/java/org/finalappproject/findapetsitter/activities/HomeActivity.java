package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.application.AppConstants;
import org.finalappproject.findapetsitter.model.UserProfile;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.parceler.Parcels.unwrap;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "HomeActivity";

    @BindView(R.id.tvHelloWorld)
    TextView tvHelloWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Bind views
        ButterKnife.bind(this);

        //
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
            //
        }


    }

    private void showHelloMessage(String fullName) {

        tvHelloWorld.setText("Hello " + fullName);

    }
}

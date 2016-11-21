package org.finalappproject.findapetsitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Pet;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_ACCEPTED;
import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_REJECTED;

public class ReceivedRequestActivity extends AppCompatActivity implements GetCallback<Request>, SaveCallback {

    @BindView(R.id.ivReceivedRequestProfile)
    ImageView ivProfilePic;
    @BindView(R.id.tvReceivedRequestFullName)
    TextView tvFullName;
    @BindView(R.id.ivReceivedRequestPetPic)
    ImageView ivPetPic;
    @BindView(R.id.tvReceivedRequestPetName)
    TextView tvPetName;
    @BindView(R.id.tvReceivedRequestDate)
    TextView tvDate;
    @BindView(R.id.tvReceivedRequestMessage)
    TextView tvMessage;
    @BindView(R.id.btRespondAccept)
    Button btAccept;
    @BindView(R.id.btRespondReject)
    Button btReject;


    private static final String LOG_TAG = "ReceivedRequestActivity";
    Request mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_request);
        ButterKnife.bind(this);

        String request_id = getIntent().getStringExtra("request_id");
        Request.queryRequest(request_id, this);
    }

    private void loadData() {

        User petOwner = mRequest.getSender();
        User petSitter = mRequest.getReceiver();

        try {
            // This seems to be allowing us to save the request/response
            // Prevents the error: java.lang.IllegalArgumentException: Cannot save a ParseUser that is not authenticated.
            petOwner.fetchIfNeeded();
            petSitter.fetchIfNeeded();
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Failed fetching user", e);
        }

        List<Pet> pets = petOwner.getPets();
        // Load user information
        ImageHelper.loadImage(this, petOwner.getProfileImage(), R.drawable.account_plus, ivProfilePic);
        tvFullName.setText(petOwner.getFullName());
        // Load request information
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        tvDate.setText(String.format("From %s \nto %s ?", sdf.format(mRequest.getBeginDate()), sdf.format(mRequest.getEndDate())));
        tvMessage.setText(mRequest.getNote());
        // Load pet information
        if (pets != null && !pets.isEmpty()) {
            ImageHelper.loadImage(this, mRequest.getSender().getPets().get(0).getProfileImage(), R.drawable.account_plus, ivPetPic);
            tvPetName.setText(mRequest.getSender().getPets().get(0).getName());
        }

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequest.setStatus(REQUEST_ACCEPTED);
                mRequest.saveInBackground(ReceivedRequestActivity.this);
            }
        });

        btReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequest.setStatus(REQUEST_REJECTED);
                mRequest.saveInBackground(ReceivedRequestActivity.this);
            }
        });
    }

    /**
     * Parse GetCallback<Request> implementation
     *
     * @param request
     * @param e       ParseException
     */
    @Override
    public void done(Request request, ParseException e) {
        if (e == null) {
            mRequest = request;
            loadData();
        } else {
            Log.e(LOG_TAG, "Failed to fetch received request", e);
            Toast.makeText(this, "Failed to fetch user", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Parse SaveCallback implementation
     *
     * @param e ParseException
     */
    @Override
    public void done(ParseException e) {
        if (e == null) {
            Toast.makeText(this, "Response sent", Toast.LENGTH_LONG).show();
        } else {
            Log.e(LOG_TAG, "Failed to send response", e);
            Toast.makeText(this, "Failed to send response " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import de.hdodenhof.circleimageview.CircleImageView;

import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_ACCEPTED;
import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_PENDING;
import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_REJECTED;

public class RequestDetailActivity extends AppCompatActivity implements GetCallback<Request>, SaveCallback {

    @BindView(R.id.ivReceivedRequestProfile)
    CircleImageView ivProfilePic;
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
    ImageView btAccept;
    @BindView(R.id.btRespondReject)
    ImageView btReject;
    @BindView(R.id.tvRequestDetailStatus)
    TextView tvRequestStatus;
    @BindView(R.id.ivRequestStatusIcon)
    ImageView ivRequestStatusIcon;
    @BindView(R.id.ivStartChat)
    ImageView ivStartChat;


    private static final String LOG_TAG = "RequestDetailActivity";
    Request mRequest;
    private static Boolean mIsReceivedRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        ButterKnife.bind(this);

        String request_id = getIntent().getStringExtra("request_id");
        mIsReceivedRequest = getIntent().getExtras().getBoolean("request_type");
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
        tvDate.setText(String.format("From %s \nto       %s ", sdf.format(mRequest.getBeginDate()), sdf.format(mRequest.getEndDate())));
        tvMessage.setText(mRequest.getNote());
        // Load pet information
        if (pets != null && !pets.isEmpty()) {
            ImageHelper.loadImage(this, mRequest.getSender().getPets().get(0).getProfileImage(), R.drawable.account_plus, ivPetPic);
            tvPetName.setText(mRequest.getSender().getPets().get(0).getName());
        }

        if (mIsReceivedRequest) {
            if (mRequest.getStatus() != REQUEST_PENDING) {
                btAccept.setVisibility(View.GONE);
                btReject.setVisibility(View.GONE);
                tvRequestStatus.setVisibility(View.VISIBLE);
                if (mRequest.getStatus() == REQUEST_ACCEPTED) {
                    tvRequestStatus.setText("You accepted the request!");
                    tvRequestStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvRequestStatus.setText("You rejected the request.");
                    tvRequestStatus.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        } else {
            switch (mRequest.getStatus()) {
                case REQUEST_ACCEPTED:
                    tvRequestStatus.setText("Request Accepted!");
                    tvRequestStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvRequestStatus.setTextSize(16);
                    ivRequestStatusIcon.setImageResource(R.drawable.request_responded);
                    ivStartChat.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_PENDING:
                    tvRequestStatus.setText("Request Pending...");
                    tvRequestStatus.setTextSize(16);
                    tvRequestStatus.setTextColor(getResources().getColor(R.color.twitterBlue));
                    ivRequestStatusIcon.setImageResource(R.drawable.request_pending);
                    break;
                case REQUEST_REJECTED:
                    tvRequestStatus.setText("Request Rejected.");
                    tvRequestStatus.setTextSize(16);
                    tvRequestStatus.setTextColor(getResources().getColor(R.color.dark_grey));
                    ivRequestStatusIcon.setImageResource(R.drawable.request_rejected);
                    break;
                default:
                    tvRequestStatus.setEnabled(false);
            }
            btAccept.setVisibility(View.GONE);
            btReject.setVisibility(View.GONE);
        }
    }

    private void disableButton() {
        btAccept.setEnabled(false);
        btReject.setEnabled(false);
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
            btAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRequest.setStatus(REQUEST_ACCEPTED);
                    mRequest.saveInBackground(RequestDetailActivity.this);
                    btAccept.setVisibility(View.GONE);
                    btReject.setVisibility(View.GONE);
                    tvRequestStatus.setVisibility(View.VISIBLE);
                    tvRequestStatus.setText("You accepted the request!");
                    tvRequestStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            });

            btReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRequest.setStatus(REQUEST_REJECTED);
                    mRequest.saveInBackground(RequestDetailActivity.this);
                    btAccept.setVisibility(View.GONE);
                    btReject.setVisibility(View.GONE);
                    tvRequestStatus.setVisibility(View.VISIBLE);
                    tvRequestStatus.setText("You rejected the request!");
                    tvRequestStatus.setTextColor(getResources().getColor(R.color.gray));
                }
            });

            ivStartChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.fromParts("sms", mRequest.getReceiver().getPhone(), null));
                    startActivity(intent);
                }
            });
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
            finish();
        } else {
            Log.e(LOG_TAG, "Failed to send response", e);
            Toast.makeText(this, "Failed to send response " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
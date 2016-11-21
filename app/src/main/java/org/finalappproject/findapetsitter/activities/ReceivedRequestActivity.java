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

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.util.ImageHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_ACCEPTED;
import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_REJECTED;

public class ReceivedRequestActivity extends AppCompatActivity implements GetCallback<Request> {

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
        ImageHelper.loadImage(this, mRequest.getSender().getProfileImage(), R.drawable.account_plus, ivProfilePic);
        ImageHelper.loadImage(this, mRequest.getSender().getPets().get(0).getProfileImage(), R.drawable.account_plus, ivPetPic);
        tvFullName.setText(mRequest.getSender().getFullName());
        tvPetName.setText(mRequest.getSender().getPets().get(0).getName());
        tvDate.setText(String.format("From %s \nto %s ?", mRequest.getBeginDate().toString(), mRequest.getEndDate().toString()));
        tvMessage.setText(mRequest.getNote());

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequest.setStatus(REQUEST_ACCEPTED);
            }
        });

        btReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequest.setStatus(REQUEST_REJECTED);
            }
        });
    }

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

}
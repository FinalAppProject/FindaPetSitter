package org.finalappproject.findapetsitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.Request;

import butterknife.ButterKnife;

public class ReceivedRequestActivity extends AppCompatActivity implements GetCallback<Request> {

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
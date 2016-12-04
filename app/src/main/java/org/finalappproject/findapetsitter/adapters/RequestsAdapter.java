package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.RequestDetailActivity;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_ACCEPTED;
import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_PENDING;
import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_REJECTED;

/**
 * Created by Aoi on 11/19/2016.
 */

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private static final String LOG_TAG = "RequestsAdapter";

    private List<Request> mRequests;
    private Context mContext;
    private boolean mIsPetSitterFlow;

    private Context getContext() {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivRequestProfile)
        ImageView ivRequestProfile;
        @BindView(R.id.tvRequestReceived)
        TextView tvRequestReceived;
        @BindView(R.id.rlRequestReceived)
        RelativeLayout rlRequestReceived;
        @BindView(R.id.tvRequestStatus)
        TextView tvRequestStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public RequestsAdapter(Context context, ArrayList<Request> requests, boolean isPetSitterFlow) {
        mContext = context;
        mRequests = requests;
        mIsPetSitterFlow = isPetSitterFlow;
    }

    public RequestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_request, parent, false);
        RequestsAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RequestsAdapter.ViewHolder viewHolder, int position) {
        final Request request = mRequests.get(position);

        final RequestsAdapter.ViewHolder vh = viewHolder;

        if (mIsPetSitterFlow) {

            request.getSender().fetchIfNeededInBackground(new GetCallback<User>() {
                @Override
                public void done(User sender, ParseException e) {
                    String message = getContext().getString(R.string.request_message_pet_sitter, sender.getFullName());
                    vh.tvRequestReceived.setText(message);
                    ImageHelper.loadImage(mContext, sender.getProfileImage(), R.drawable.cat, vh.ivRequestProfile);
                }
            });

            if (request.getStatus() != REQUEST_PENDING) {
                viewHolder.tvRequestStatus.setText("Responded");
                viewHolder.tvRequestStatus.setBackgroundResource(R.color.green);
            } else {
                viewHolder.tvRequestStatus.setText("Pending");
                viewHolder.tvRequestStatus.setBackgroundResource(R.color.blue);
            }

        } else {
            request.getReceiver().fetchIfNeededInBackground(new GetCallback<User>() {
                @Override
                public void done(User receiver, ParseException e) {
                    String message = getContext().getString(R.string.request_message_pet_owner, receiver.getFullName());
                    vh.tvRequestReceived.setText(message);
                    ImageHelper.loadImage(mContext, receiver.getProfileImage(), R.drawable.cat, vh.ivRequestProfile);
                }
            });

            if (request.getStatus() == REQUEST_ACCEPTED) {
                viewHolder.tvRequestStatus.setText("Accepted");
                viewHolder.tvRequestStatus.setBackgroundResource(R.color.green);
            } else if (request.getStatus() == REQUEST_REJECTED) {
                viewHolder.tvRequestStatus.setText("Rejected");
                viewHolder.tvRequestStatus.setBackgroundResource(R.color.gray);
            } else {
                viewHolder.tvRequestStatus.setText("Pending");
                viewHolder.tvRequestStatus.setBackgroundResource(R.color.blue);
            }
        }

        viewHolder.rlRequestReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RequestDetailActivity.class);
                intent.putExtra("request_id", request.getObjectId());
                intent.putExtra("request_type", mIsPetSitterFlow);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mRequests.size();
    }
}

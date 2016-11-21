package org.finalappproject.findapetsitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.activities.ReceivedRequestActivity;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        final User currentUser;
        final User otherUser;
        final String message;

        try {
            if (mIsPetSitterFlow) {
                currentUser = (User) request.getReceiver().fetchIfNeeded();
                otherUser = (User) request.getSender().fetchIfNeeded();
                message = getContext().getString(R.string.request_message_pet_sitter, otherUser.getFullName());

                viewHolder.rlRequestReceived.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ReceivedRequestActivity.class);
                        intent.putExtra("request_id", request.getObjectId());
                        getContext().startActivity(intent);
                    }
                });

            } else {
                currentUser = (User) request.getSender().fetchIfNeeded();
                otherUser = (User) request.getReceiver().fetchIfNeeded();
                message = getContext().getString(R.string.request_message_pet_owner, otherUser.getFullName());
            }

            //
            ImageHelper.loadImage(mContext, otherUser.getProfileImage(), R.drawable.cat, viewHolder.ivRequestProfile);
            //
            viewHolder.tvRequestReceived.setText(message);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Failed to fetch User", e);
            viewHolder.ivRequestProfile.setImageResource(0);
            viewHolder.tvRequestReceived.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return this.mRequests.size();
    }
}

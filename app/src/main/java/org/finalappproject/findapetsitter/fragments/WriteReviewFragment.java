    package org.finalappproject.findapetsitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.PetType;
import org.finalappproject.findapetsitter.model.Review;
import org.finalappproject.findapetsitter.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class WriteReviewFragment extends DialogFragment implements GetCallback<User>, SaveCallback {

    @BindView(R.id.spReviewPetType)
    Spinner spinnerPetType;

    @BindView(R.id.btnReviewSave)
    Button saveButton;

    @BindView(R.id.btnReviewCancel)
    Button cancelButton;

    @BindView(R.id.etReview)
    EditText reviewEditText;

    @BindView(R.id.rbReviewRating)
    RatingBar reviewRatingRatingBar;

    private static final String LOG_TAG = "ReviewFragment";
    CalendarPickerDialogFragment calendarPickerDialogFragment;

    Context mContext;

    Review newReview;
    String sitter_id;

    @Override
    public void done(User user, ParseException e) {
        if (e == null) {
            newReview.setReviewReceiver(user);
        } else {
            Log.e(LOG_TAG, "Failed to load sitter", e);
            //TODO what error message is this?
            Toast.makeText(getActivity(), "Failed to fetch added pet?????", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sitter_id = getArguments().getString("sitter_id");
        User.queryUser(sitter_id, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.fragment_write_review, container, false);
        ButterKnife.bind(this, vi);
        newReview = new Review();

        getDialog().setTitle("Write Review");

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pet_type_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPetType.setAdapter(spinnerAdapter);
        spinnerPetType.setSelection(0, true);

        reviewRatingRatingBar.setRating(0.00f);
        reviewRatingRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Log.d(LOG_TAG, "MMMM "+ratingBar.getRating() + " rating " + rating + " boolean "+ fromUser );
                newReview.setRating(reviewRatingRatingBar.getRating());
                Toast.makeText(getActivity(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
            }
        });
        return vi;
    }

    @OnClick(R.id.btnReviewCancel)
    public void onClickCancel(View v) {
        dismiss();
    }

    @OnClick(R.id.btnReviewSave)
    public void onClickSave(View v) {
        getReviewFromView();
        storeInParse();
        dismiss();
    }

    void storeInParse() {
        newReview.saveInBackground(this);
    }

    @Override
    public void done(ParseException e) {
        if (e == null) {
            Toast.makeText(mContext, "Review saved", Toast.LENGTH_LONG).show();
        } else {
            Log.e(LOG_TAG, "Failed to save review", e);
            Toast.makeText(mContext, "Failed to save review " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void getReviewFromView() {
        newReview.setReview(reviewEditText.getText().toString());
        newReview.setType(PetType.valueOf(spinnerPetType.getSelectedItem().toString()));
        setUserNames();
    }

    void setUserNames() {
        ParseUser user = ParseUser.getCurrentUser();
        newReview.setReviewer((User) user);
    }
}
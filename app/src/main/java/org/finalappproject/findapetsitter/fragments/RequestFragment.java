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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.PetType;
import org.finalappproject.findapetsitter.model.Request;
import org.finalappproject.findapetsitter.model.User;
import org.finalappproject.findapetsitter.pushmessage.PushMessageHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.finalappproject.findapetsitter.R.id.etSelectDates;
import static org.finalappproject.findapetsitter.application.AppConstants.REQUEST_PENDING;

public class RequestFragment extends DialogFragment implements CalendarPickerDialogFragment.OnDatesSelectedListener, GetCallback<User>, SaveCallback {

    @BindView(R.id.spPetType)
    Spinner spinnerPetType;

    @BindView(R.id.btnSend)
    Button sendButton;

    @BindView(R.id.btnCancel)
    Button cancelButton;

    @BindView(etSelectDates)
    EditText selecteDatesEditText;

    @BindView(R.id.etNote)
    EditText noteEditText;

    private static final String LOG_TAG = "RequestFragment";
    CalendarPickerDialogFragment calendarPickerDialogFragment;

    Context mContext;

    Request newRequest;
    String sitter_id;

    @Override
    public void done(User user, ParseException e) {
        if (e == null) {
            newRequest.setReceiver(user);
        } else {
            Log.e(LOG_TAG, "Failed to load sitter", e);
            Toast.makeText(getActivity(), "Failed to fetch added pet", Toast.LENGTH_LONG).show();
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

        View vi = inflater.inflate(R.layout.fragment_request, container, false);
        ButterKnife.bind(this, vi);
        newRequest = new Request();

        getDialog().setTitle("Send Request");

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pet_type_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPetType.setAdapter(spinnerAdapter);
        spinnerPetType.setSelection(0, true);

        return vi;
    }

    @OnClick(R.id.btnCancel)
    public void onClickCancel(View v) {
        dismiss();
    }

    @OnClick(R.id.btnSend)
    public void onClickSend(View v) {
        getRequestFromView();
        storeInParse();
        dismiss();
    }

    void storeInParse() {
        /*
        TODO try understanding parse ACL, is this needed ? It doesn't seem to be needed
        ParseACL groupACL = new ParseACL();
        User userList[] = {newRequest.getReceiver(), newRequest.getSender()};
        for (ParseUser user : userList) {
            groupACL.setReadAccess(user, true);
            groupACL.setWriteAccess(user, true);
        }
        newRequest.setACL(groupACL);
        */
        newRequest.saveInBackground(this);


        /*
        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder(getString(R.string.gcm_sender_id) + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(1)) // msgId.incrementAndGet()))
                .setTtl(R.string.address_text_view)
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .build());
        */
    }

    @Override
    public void done(ParseException e) {
        if (e == null) {
            Toast.makeText(mContext, "Request sent", Toast.LENGTH_LONG).show();

            PushMessageHelper.pushNotifyNewRequest(newRequest);

        } else {
            Log.e(LOG_TAG, "Failed to send request", e);
            Toast.makeText(mContext, "Failed to send request " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void getRequestFromView() {
        newRequest.setNote(noteEditText.getText().toString());
        newRequest.setType(PetType.valueOf(spinnerPetType.getSelectedItem().toString()));
        newRequest.setStatus(REQUEST_PENDING);
        setUserNames();
    }

    void setUserNames() {
        ParseUser user = ParseUser.getCurrentUser();
        newRequest.setSender((User) user);
    }

    @OnClick(etSelectDates)
    public void onClickBeginDate(View v) {
        CalendarPickerDialogFragment newFragment = new CalendarPickerDialogFragment();
        newFragment.setTargetFragment(RequestFragment.this, 300);
        newFragment.show(getFragmentManager(), "calendarfrag");
    }

    public StringBuilder generateFromatedDate(int year, int date, int month) {
        StringBuilder sb = new StringBuilder();
        sb.append(month);
        sb.append('/');
        sb.append(date);
        sb.append('/');
        sb.append(year);
        return sb;
    }

    StringBuilder getDateSplit(Date whole_date) {
        StringBuilder sbDate = new StringBuilder();

        HashMap<String, Integer> dateSplit = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(whole_date);
        dateSplit.put("month", cal.get(Calendar.MONTH) + 1); //month start from 0 in calendar. so Jan is 0
        dateSplit.put("date", cal.get(Calendar.DATE));
        dateSplit.put("year", cal.get(Calendar.YEAR));
        sbDate.append(generateFromatedDate(dateSplit.get("year"), dateSplit.get("date"), dateSplit.get("month")));

        return sbDate;
    }

    @Override
    public void onDatesSelected(ArrayList<Date> dates) {
        if (dates.size() == 0) {
            Log.e(LOG_TAG, "Error no dates selected!");
            return;
        }

        Date beginDate = dates.get(0);
        Date endDate = dates.get(dates.size() - 1);
        newRequest.setBeginDate(beginDate);
        newRequest.setEndDate(endDate);

        StringBuilder formattedDate = new StringBuilder();

        formattedDate.append(getDateSplit(beginDate));
        formattedDate.append(" - ");
        formattedDate.append(getDateSplit(endDate));

        selecteDatesEditText.setText(formattedDate.toString());

        Log.d(LOG_TAG, "MMMM " + beginDate.toString() + " and " + endDate.toString());
    }

}
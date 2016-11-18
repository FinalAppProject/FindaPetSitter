package org.finalappproject.findapetsitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import org.finalappproject.findapetsitter.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE;

public class CalendarPickerDialogFragment extends DialogFragment {

    private OnDatesSelectedListener onDatesSelectedListener;

    interface OnDatesSelectedListener {
        public void onDatesSelected(ArrayList<Date> dates);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getTargetFragment() instanceof OnDatesSelectedListener) {
            onDatesSelectedListener = (OnDatesSelectedListener) getTargetFragment();
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement CalendarPickerDialogFragment.OnItemSelectedListener");
        }
    }

    @BindView(R.id.calendar_view)
    CalendarPickerView calendar;

    @BindView(R.id.btDone)
    Button done;

    @OnClick(R.id.btDone)
    void onNextClicked(){
        ArrayList<Date> selectedDates = (ArrayList<Date>)calendar
                .getSelectedDates();
        Toast.makeText(getActivity(), selectedDates.toString(),
                Toast.LENGTH_LONG).show();
        onDatesSelectedListener.onDatesSelected(selectedDates);
        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View calendarPickerView = inflater.inflate(R.layout.fragment_calendar_picker, container, false);
        ButterKnife.bind(this, calendarPickerView);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                /*.withSelectedDate(today)*/ //Its a little annoying to have that set.
                .inMode(RANGE);
        return calendarPickerView;
    }

    //TODO: Consider making a toolbar instead of the "DONE" button will be cleaner view

}
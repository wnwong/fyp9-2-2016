package com.example.user.secondhandtradingplatform;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    OnDatePickedListener mCallback;
    // Container Activity must implement this interface
    public interface OnDatePickedListener{
         void OnDatePicked(String msg);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDatePickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        return new DatePickerDialog(getActivity(), this, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        mCallback.OnDatePicked(dateFormat.format(cal.getTime()));
    }
}

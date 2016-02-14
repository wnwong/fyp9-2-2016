package com.example.user.secondhandtradingplatform;


import android.app.Activity;
import android.app.Dialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;


import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    OnTimePickedListener mCallback;

    public interface OnTimePickedListener {
        void OnTimePicked(String msg);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTimePickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement  OnTimePickedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        return new TimePickerDialog(getActivity(), this, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(minute>9)
        {
            mCallback.OnTimePicked(hourOfDay + ":" + minute);
        }else{
            mCallback.OnTimePicked(hourOfDay + ":" + "0" + minute);
        }
    }
}

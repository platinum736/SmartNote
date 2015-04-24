package com.smartnote.rishabh_pc.smartnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rishabh-pc on 4/23/2015.
 */
public class tagScreen extends ActionBarActivity {

    TextView year;
    TextView date;
    TextView format;
    TextView time;
    TextView location;
    EditText Note;

    int c_year;
    int c_month;
    int c_day;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagscreen);
        Calendar calendar = Calendar.getInstance();
        Boolean timeformat = true;
        SimpleDateFormat sdf;

        year = (TextView) findViewById(R.id.year);
        date = (TextView) findViewById(R.id.date);
        format = (TextView) findViewById(R.id.clockType);
        time = (TextView) findViewById(R.id.time);
        location = (TextView) findViewById(R.id.tagLoc);
        Note = (EditText) findViewById(R.id.notificationNote);


        c_year = calendar.get(Calendar.YEAR);
        c_month = calendar.get(Calendar.MONTH);
        c_day = calendar.get(Calendar.DATE);

        sdf = new SimpleDateFormat("MMM");
        year.setText(calendar.get(Calendar.YEAR) + " " + sdf.format(calendar.getTime()));
        date.setText("" + calendar.get(Calendar.DATE));

        format.setText("24Hr Clock");
        if (timeformat) {
            sdf = new SimpleDateFormat("HH:mm");
            time.setText(sdf.format(new Date()));
        } else {
            sdf = new SimpleDateFormat("HH:mm a");
            time.setText(sdf.format(new Date()));
        }

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // android.app.DialogFragment newFragment = new TimePickerFragment();
                // newFragment.show(getSupportFragmentManager(), getString(R.string.datePickerTitle));
                //android.app.DialogFragment newFragment=new TimePickerFragment();
                //newFragment.show(getFragmentManager(),getString(R.string.timePickerTitle));

            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(tagScreen.this, map_Activity.class);
                startActivity(intent);
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // android.app.DialogFragment newFragment = new TimePickerFragment();
                // newFragment.show(getSupportFragmentManager(), getString(R.string.datePickerTitle));
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), getString(R.string.datePickerTitle));
                // date.setText(newFragment.getArguments().get("dayOfMonth").toString());
            }
        });
    }


    public void onBackPressed() {
        Intent intent=new Intent(tagScreen.this, MainActivity.class);
        startActivity(intent);
    }

    }

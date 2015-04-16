package com.smartnote.rishabh_pc.smartnote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartnote.rishabh_pc.smartnote.R;

public class LocationTagging extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tag_location, container, false);

        return rootView;
    }
}
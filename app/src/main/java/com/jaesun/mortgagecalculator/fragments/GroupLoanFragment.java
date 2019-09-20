package com.jaesun.mortgagecalculator.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaesun.mortgagecalculator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupLoanFragment extends Fragment {


    public GroupLoanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_group_loan, container, false);
        TextView titleView = getActivity().findViewById(R.id.navigation_title);
        titleView.setText("组合贷");

        return view;
    }

}

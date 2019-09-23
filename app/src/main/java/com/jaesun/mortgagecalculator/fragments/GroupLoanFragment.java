package com.jaesun.mortgagecalculator.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jaesun.mortgagecalculator.R;
import com.jaesun.mortgagecalculator.activities.ResultActivity;
import com.jaesun.mortgagecalculator.models.LoanModel;
import com.jaesun.mortgagecalculator.models.ResultModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupLoanFragment extends Fragment {

    LoanModel loanModel = new LoanModel();

    private EditText commercialAmountEditText;
    private TextView commercialRateTextView;
    private OptionsPickerView commercialRatePicker;
    private ArrayList<String> commercialRateList;

    private EditText fundAmountEditText;
    private TextView fundRateTextView;
    private OptionsPickerView fundRatePicker;
    private ArrayList<String> fundRateList;

    private TextView yearCountTextView;
    private ArrayList<String> yearList;
    private OptionsPickerView yearPicker;


    private Button   confirmButton;

    public GroupLoanFragment() {

        loanModel.setRate((float) 4.9);
        commercialRateList = new ArrayList<>();
        for (int i = 0; i < 7; i ++) {
            int count =  10 * i + 70;
            String msg = "基准利率";
            if (count < 100) {
                msg = String.format("下浮%d%%",100-count);
            }
            else if(count > 100) {
                msg = String.format("上浮%d%%",count-100);
            }
            float rate = (float) ((0.1 * i + 0.7) * 4.9);
            commercialRateList.add(String.format("%.2f(%s)",rate,msg));
        }

        loanModel.setFundRate((float) 3.25);
        fundRateList = new ArrayList<>();
        for (int i = 0; i < 7; i ++) {
            int count =  10 * i + 70;
            String msg = "基准利率";
            if (count < 100) {
                msg = String.format("下浮%d%%",100-count);
            }
            else if(count > 100) {
                msg = String.format("上浮%d%%",count-100);
            }
            float rate = (float) ((0.1 * i + 0.7) * 3.25);
            fundRateList.add(String.format("%.2f(%s)",rate,msg));
        }

        loanModel.setYearCount(30);
        yearList = new ArrayList<>();
        for (int i = 0; i < 6; i ++) {
            int count =  5 * i + 5;
            yearList.add(String.format("%d 年",count));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_group_loan, container, false);

        TextView titleView = getActivity().findViewById(R.id.navigation_title);
        titleView.setText("组合贷");


        commercialAmountEditText = view.findViewById(R.id.editTextCommercialAmount);
        commercialRateTextView = view.findViewById(R.id.textViewCommercialRate);

        fundAmountEditText = view.findViewById(R.id.editTextFundAmount);
        fundRateTextView = view.findViewById(R.id.textViewFundRate);

        yearCountTextView = view.findViewById(R.id.textViewYear);

        confirmButton = view.findViewById(R.id.buttonConfirm);

        initCommercialRatePicker();
        initFundRatePicker();
        initYearPicker();

        LinearLayout commercialRateContainer = view.findViewById(R.id.containerCommercialRate);
        commercialRateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard(view);
                commercialRatePicker.show();
            }
        });

        LinearLayout fundRateContainer = view.findViewById(R.id.containerFundRate);
        fundRateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard(view);
                fundRatePicker.show();
            }
        });

        LinearLayout yearContainer = view.findViewById(R.id.containerYear);
        yearContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard(view);
                yearPicker.show();
            }
        });

        confirmButton = view.findViewById(R.id.buttonConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commercialAmountEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext(),"请输入商业贷款金额",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(fundAmountEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext(),"请输入公积金贷款金额",Toast.LENGTH_SHORT).show();;
                    return;
                }
                else {
                    loanModel.setAmount(Float.valueOf(commercialAmountEditText.getText().toString().trim()));
                    loanModel.setFundAmount(Float.valueOf(fundAmountEditText.getText().toString().trim()));

                    Intent intent = new Intent(getContext(), ResultActivity.class);
                    intent.putExtra("loan",loanModel);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_in,R.anim.push_out);
                }
            }
        });

        return view;
    }

    private void initYearPicker() {
        // 条件选择器
        yearPicker = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                yearCountTextView.setText(String.format("%d",5 * options1 +5));
                loanModel.setYearCount(5*options1+5);
            }
        }).setDecorView(
                (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        yearPicker.setPicker(yearList);
    }

    private void initCommercialRatePicker() {
        // 条件选择器
        commercialRatePicker = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                float rate = (float) ((0.1 * options1 + 0.7) * 4.9);
                loanModel.setRate(rate);
                commercialRateTextView.setText(String.format("%.2f",rate));
            }
        }).setDecorView(
                (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        commercialRatePicker.setPicker(commercialRateList);
    }

    private void initFundRatePicker() {
        // 条件选择器
        fundRatePicker = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                float rate = (float) ((0.1 * options1 + 0.7) * 3.25);
                loanModel.setFundRate(rate);
                fundRateTextView.setText(String.format("%.2f",rate));
            }
        }).setDecorView(
                (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        fundRatePicker.setPicker(fundRateList);
    }

    private  void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

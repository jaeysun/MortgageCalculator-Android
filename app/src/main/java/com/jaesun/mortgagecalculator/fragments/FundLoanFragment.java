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
import com.jaesun.mortgagecalculator.activities.MainActivity;
import com.jaesun.mortgagecalculator.activities.ResultActivity;
import com.jaesun.mortgagecalculator.models.LoanModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FundLoanFragment extends Fragment {

    private LoanModel loanModel = new LoanModel();

    private EditText amountEditText;

    private OptionsPickerView ratePicker;
    private ArrayList<String> rateList;
    private TextView rateTextView;

    private OptionsPickerView yearPicker;
    private ArrayList<String> yearList;
    private TextView yearTextView;

    private Button confirmButton;

    public FundLoanFragment() {
        loanModel.setRate((float) 3.25);
        rateList = new ArrayList<>();
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
            rateList.add(String.format("%.2f(%s)",rate,msg));
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

        View view =  inflater.inflate(R.layout.fragment_fund_loan, container, false);

        TextView titleView = getActivity().findViewById(R.id.navigation_title);
        titleView.setText("公积金贷");

        // 贷款金额
        amountEditText = view.findViewById(R.id.editTextAmount);

        // 贷款利率
        LinearLayout rateContainer = view.findViewById(R.id.containerRate);
        rateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard(view);
                ratePicker.show();
            }
        });
        rateTextView = view.findViewById(R.id.textViewRate);
        initRatePicker();

        // 贷款年限
        LinearLayout yearContainer =view.findViewById(R.id.containerYear);
        yearContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard(view);
                yearPicker.show();
            }
        });
        yearTextView = view.findViewById(R.id.textViewYear);
        initYearPicker();

        confirmButton = view.findViewById(R.id.buttonConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amountEditText.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(), "请输入贷款金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    float amount = Float.valueOf(amountEditText.getText().toString().trim());
                    loanModel.setAmount(amount);

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
                yearTextView.setText(String.format("%d",5 * options1 +5));
                loanModel.setYearCount(5*options1+5);
            }
        }).setDecorView(
                (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        yearPicker.setPicker(yearList);
    }

    private void initRatePicker() {
        // 条件选择器
        ratePicker = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                float rate = (float) ((0.1 * options1 + 0.7) * 3.25);
                loanModel.setRate(rate);
                rateTextView.setText(String.format("%.2f",rate));
            }
        }).setDecorView(
                (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        ratePicker.setPicker(rateList);
    }

    private  void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

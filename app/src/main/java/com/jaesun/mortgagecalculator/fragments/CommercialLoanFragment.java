package com.jaesun.mortgagecalculator.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jaesun.mortgagecalculator.R;
import com.jaesun.mortgagecalculator.activities.ResultActivity;
import com.jaesun.mortgagecalculator.models.FormModel;
import com.jaesun.mortgagecalculator.models.LoanModel;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommercialLoanFragment extends Fragment {

    private RecyclerView formView;
    private OptionsPickerView ratePicker;
    private ArrayList<String> rateList;
    private OptionsPickerView datePicker;
    private ArrayList<String> dateList;
    private OptionsPickerView firstPicker;
    private ArrayList<String> firstList;

    private EditText loanAmountEditText; // 贷款总额
    private EditText areaEditText; // 房屋面积
    private EditText perPriceEditText; // 单价

    private LoanModel loanModel;

    private TextView rateTextView;
    private TextView dateTextView;
    private TextView firstTextView;

    private ArrayList<FormModel> formModels;


    public CommercialLoanFragment() {
        loanModel = new LoanModel();
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
            float rate = (float) ((0.1 * i + 0.7) * 4.9);
            rateList.add(String.format("%.2f(%s)",rate,msg));
        }

        dateList = new ArrayList<>();
        for (int i = 0; i < 6; i ++) {
            int count =  5 * i + 5;
            dateList.add(String.format("%d 年",count));
        }

        firstList = new ArrayList<>();
        for (int i = 0; i < 8; i ++) {
            int count =  i + 3;
            firstList.add(String.format("%d 成",count));
        }
    }

    private void initFormModels() {

        formModels = new ArrayList<FormModel>();
        formModels.add(new FormModel(R.layout.form_item_select,"计算方式","0",null));
        formModels.add(new FormModel(R.layout.form_item_input_default,"商业贷款(万元)","请输入金额"));
        formModels.add(new FormModel(R.layout.form_item_option,"实际贷款利率(%)","4.90",rateList));
        loanModel.setRate((float) 4.9);
        formModels.add(new FormModel(R.layout.form_item_option,"贷款期限(年)","30",dateList));
        loanModel.setYearCount(30);
        formModels.add(new FormModel(R.layout.form_foot_confirm_button,"计算"));
    }

    private void updateFormModels() {
        formModels = new ArrayList<FormModel>();
        formModels.add(new FormModel(R.layout.form_item_select,"计算方式","1",null));
        formModels.add(new FormModel(R.layout.form_item_input_default,"面积(平方米)","请输入住房面积"));
        formModels.add(new FormModel(R.layout.form_item_input_default,"单价(元/平方米)","请输入单价"));
        formModels.add(new FormModel(R.layout.form_item_option,"首付比例","3 成",firstList));
        formModels.add(new FormModel(R.layout.form_item_option,"实际贷款利率(%)","4.90",rateList));
        formModels.add(new FormModel(R.layout.form_item_option,"贷款期限(年)","30",dateList));
        formModels.add(new FormModel(R.layout.form_foot_confirm_button,"计算"));
    }
    private void initDatePicker() {
        // 条件选择器
        datePicker = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                dateTextView.setText(String.format("%d",5 * options1 +5));
                loanModel.setYearCount(options1+5);
            }
        }).setDecorView(
                (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        datePicker.setPicker(dateList);
    }
    private void initFirstPicker() {
        // 条件选择器
        firstPicker = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                firstTextView.setText(String.format("%d 成",options1 +3));
            }
        }).setDecorView(
                (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        firstPicker.setPicker(firstList);
    }
    private void initRatePicker() {
        // 条件选择器
        ratePicker = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                float rate = (float) ((0.1 * options1 + 0.7) * 4.90);
                loanModel.setRate(rate);
                rateTextView.setText(String.format("%.2f",rate));
            }
        }).setDecorView(
                (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        ratePicker.setPicker(rateList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFormModels();

        View view =  inflater.inflate(R.layout.fragment_commercial_loan, container, false);
        TextView titleView = getActivity().findViewById(R.id.navigation_title);
        titleView.setText("商贷");

        formView = view.findViewById(R.id.commercial_form);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        formView.setLayoutManager(layoutManager);

        FormAdapter formAdapter = new FormAdapter();
        formView.setAdapter(formAdapter);

        initRatePicker();
        initDatePicker();
        initFirstPicker();

        return view;
    }

    public class FormViewHolder extends RecyclerView.ViewHolder {
        public FormViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class FormAdapter extends RecyclerView.Adapter<FormViewHolder> {
        @NonNull
        @Override
        public FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
            return new FormViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            FormModel model = formModels.get(position);
            return model.getUiId();
        }

        @Override
        public void onBindViewHolder(@NonNull FormViewHolder holder, int position) {

            final Drawable selDrawable = getResources().getDrawable(R.drawable.background_primary_color_gradient);
            final Drawable norDrawable = getResources().getDrawable(R.drawable.background_rectangle_low_profile_border);
            final int lowProfileColor = getResources().getColor(R.color.colorLowProfile);

            final FormModel formModel = formModels.get(position);
            int viewType = formModel.getUiId();
            View view = holder.itemView;

            if (viewType != R.layout.form_foot_confirm_button) {
                TextView titleView = view.findViewById(R.id.form_item_title);
                titleView.setText(formModel.getTitle());

            }

            if (viewType == R.layout.form_item_select){
                final Button amountItem = view.findViewById(R.id.buttonAmount);
                final Button areaItem = view.findViewById(R.id.buttonArea);
                amountItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        amountItem.setBackground(selDrawable);
                        amountItem.setTextColor(Color.WHITE);
                        areaItem.setBackground(norDrawable);
                        areaItem.setTextColor(lowProfileColor);

                        initFormModels();
                        notifyDataSetChanged();
                    }
                });

                areaItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        areaItem.setBackground(selDrawable);
                        areaItem.setTextColor(Color.WHITE);
                        amountItem.setBackground(norDrawable);
                        amountItem.setTextColor(lowProfileColor);
                        updateFormModels();
                        notifyDataSetChanged();
                    }
                });

            }
            else if(viewType == R.layout.form_item_input_default) {
                EditText contentView = view.findViewById(R.id.formItemContent);
                if (formModel.getPlaceholder() != null) {
                    contentView.setHint(formModel.getPlaceholder());
                }
                if ("商业贷款(万元)".equals(formModel.getTitle())) {
                    loanAmountEditText = contentView;
                }
                else if("面积(平方米)".equals((formModel.getTitle()))) {
                    areaEditText = contentView;
                }
                else if("单价(元/平方米)".equals(formModel.getTitle())){
                    perPriceEditText = contentView;
                }
            }
            else if(viewType == R.layout.form_item_option) {
                TextView contentView = view.findViewById(R.id.textViewContent);
                if ((formModel.getValue() != null)) {
                    contentView.setText(formModel.getValue());
                }

                if ("实际贷款利率(%)".equals(formModel.getTitle())) {
                    rateTextView = contentView;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissKeyboard(view);
                            ratePicker.show(view);
                        }
                    });
                }
                else if ("贷款期限(年)".equals(formModel.getTitle())){
                    dateTextView = contentView;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissKeyboard(view);
                            datePicker.show(view);
                        }
                    });
                }
                else if ("首付比例".equals(formModel.getTitle())) {
                    firstTextView = contentView;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissKeyboard(view);
                            firstPicker.show(view);
                        }
                    });
                }
            }
            else if(viewType == R.layout.form_foot_confirm_button) {
                Button confirmButton = view.findViewById(R.id.buttonConfirm);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String countType;
                        countType = formModels.get(0).getValue();
                        if ("0".equals(countType)) {
                            if (loanAmountEditText.getText().toString().trim().equals("")) {
                                Toast.makeText(getContext(), "请输入贷款金额", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                               loanModel.setAmount(Float.parseFloat(loanAmountEditText.getText().toString().trim()));
                               Log.d("lanAmount",loanModel.getAmount()+"");
                            }
                        }
                        Intent intent = new Intent(getActivity(), ResultActivity.class);
                        intent.putExtra("loan",loanModel);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_in,R.anim.push_out);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return formModels.size();
        }

        private  void dismissKeyboard(View view) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}

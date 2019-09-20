package com.jaesun.mortgagecalculator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaesun.mortgagecalculator.R;
import com.jaesun.mortgagecalculator.models.LoanModel;
import com.jaesun.mortgagecalculator.models.ResultModel;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private LoanModel loanModel; // 基本数据

    private ArrayList<ResultModel> contentList = new ArrayList<>();
    private ArrayList<ResultModel> bxContentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageButton leftButton = findViewById(R.id.nav_left_button);
        leftButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_white_back));
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.pop_in,R.anim.pop_out);
            }
        });

        TextView titleView = findViewById(R.id.navigation_title);
        titleView.setText("计算结果");

        Intent intent =  getIntent();
        loanModel = intent.getParcelableExtra("loan");

        calculateResult();

        QMUIGroupListView groupListView = findViewById(R.id.resultListView);
        QMUIGroupListView.Section section = QMUIGroupListView
                .newSection(this)
                .setTitle("基本信息")
                .setDescription("");

        for (int i = 0; i < contentList.size(); i ++) {
            ResultModel model = contentList.get(i);
            QMUICommonListItemView itemView = groupListView.createItemView(
                    null,
                    model.getTitle(),
                    null,
                    QMUICommonListItemView.HORIZONTAL,
                    QMUICommonListItemView.ACCESSORY_TYPE_NONE);
            itemView.setDetailText(model.getContent());
            section.addItemView(itemView,null);
        }
        section.addTo(groupListView);

        QMUIGroupListView.Section section2 = QMUIGroupListView
                .newSection(this)
                .setTitle("等额本金")
                .setDescription("");

        for (int i = 0; i < bxContentList.size(); i ++) {
            ResultModel model = bxContentList.get(i);
            QMUICommonListItemView itemView = groupListView.createItemView(
                    null,
                    model.getTitle(),
                    null,
                    QMUICommonListItemView.HORIZONTAL,
                    QMUICommonListItemView.ACCESSORY_TYPE_NONE);
            itemView.setDetailText(model.getContent());
            section2.addItemView(itemView,null);
        }
        section2.addTo(groupListView);


    }

    @SuppressLint("DefaultLocale")
    protected void calculateResult() {

        // 基础数据
        BigDecimal rateRatio = new BigDecimal(100);
        BigDecimal moneyRatio = new BigDecimal(10000);
        BigDecimal monthRatio = new BigDecimal(12);
        BigDecimal foundRatio = new BigDecimal(1);
        BigDecimal towNumber = new BigDecimal(2);

        // 获取月利率
        BigDecimal yearRateNumber = new BigDecimal(loanModel.getRate());
        yearRateNumber = yearRateNumber.divide(rateRatio);
        BigDecimal monthRateNumber = yearRateNumber.divide(monthRatio,18, RoundingMode.CEILING);

        // 获取还款月数
        Integer monthCount = loanModel.getYearCount()*12;
        BigDecimal yearCountNumber = new BigDecimal(loanModel.getYearCount());
        BigDecimal monthCountNumber = yearCountNumber.multiply(monthRatio);

        // 贷款总金额
        BigDecimal loanMoneyNumber = new BigDecimal(loanModel.getAmount());
        loanMoneyNumber = loanMoneyNumber.multiply(moneyRatio);

        // 等额本息计算公式：〔贷款本金×月利率×（1＋月利率）＾还款月数〕÷〔（1＋月利率）＾还款月数－1〕

        // 1.贷款本金×月利率
        BigDecimal step1Number = loanMoneyNumber.multiply(monthRateNumber);
        // 2.（1＋月利率）
        BigDecimal step2Number = monthRateNumber.add(foundRatio);
        // 3.（1＋月利率）＾还款月数
        BigDecimal step3Number = step2Number.pow(monthCount);
        // 4.〔（1＋月利率）＾还款月数－1〕
        BigDecimal step4Number = step3Number.subtract(foundRatio);

        // 5.〔贷款本金×月利率×（1＋月利率）＾还款月数〕÷〔（1＋月利率）＾还款月数－1〕
        BigDecimal bxMonthPayMoneyNumber = step1Number.multiply(step3Number).divide(step4Number,18,RoundingMode.CEILING);

        Log.d("bxMonthPayMoneyNumber","-->" + bxMonthPayMoneyNumber);

        contentList.add(new ResultModel("贷款总额(万元)",String.format("%.2f",loanModel.getAmount())));
        contentList.add(new ResultModel("贷款期限",String.format("%d(%d期)",loanModel.getYearCount(),monthCountNumber.toBigInteger())));


        double bxMPayMoney = bxMonthPayMoneyNumber.doubleValue();
        double totalPayMoney = bxMPayMoney * monthCount / 10000;
        double totalInterestMoney = totalPayMoney - loanModel.getAmount();


        bxContentList.add(new ResultModel("每月还款(元)",String.format("%.2f",bxMPayMoney)));
        bxContentList.add(new ResultModel("总还款(万元)",String.format("%.2f",totalPayMoney)));
        bxContentList.add(new ResultModel("利息总额(万元)",String.format("%.2f",totalInterestMoney)));

    }
}

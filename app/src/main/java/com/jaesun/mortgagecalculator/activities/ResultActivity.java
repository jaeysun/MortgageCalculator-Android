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
import java.util.Collections;

public class ResultActivity extends AppCompatActivity {

    private LoanModel loanModel; // 基本数据


    private ArrayList<String> headerTitles = new ArrayList<>();

    private ArrayList<ArrayList> dataArray = new ArrayList<>();

    private ArrayList<ResultModel> contentList   = new ArrayList<>();
    private ArrayList<ResultModel> bxContentList = new ArrayList<>();
    private ArrayList<ResultModel> bjContentList = new ArrayList<>();

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

        headerTitles.add("基本信息");
        headerTitles.add("等额本息");
        headerTitles.add("等额本金");

        if (loanModel.getAmount() == 0) {
            calculateFundResult();

        }
        else if (loanModel.getFundAmount() == 0) {
            calculateCommercialResult();
        }
        else {
            headerTitles.clear();
            headerTitles.add("基本信息(商贷)");
            headerTitles.add("等额本息(商贷)");
            headerTitles.add("等额本金(商贷)");
            calculateCommercialResult();
            headerTitles.add("基本信息(公积金)");
            headerTitles.add("等额本息(公积金)");
            headerTitles.add("等额本金(公积金)");
            calculateFundResult();
        }

        for (int i = 0; i < headerTitles.size(); i ++) {
            String title = headerTitles.get(i);

            QMUIGroupListView groupListView = findViewById(R.id.resultListView);
            QMUIGroupListView.Section section = QMUIGroupListView
                    .newSection(this)
                    .setTitle(title)
                    .setDescription("");
            ArrayList<ResultModel> list = dataArray.get(i);
            for (int j = 0; j < list.size(); j ++) {
                ResultModel model = list.get(j);
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

        }

    }

    protected void calculateCommercialResult() {
        calculateResult(loanModel.getAmount(),loanModel.getRate(),loanModel.getYearCount());

        ArrayList newContentList = new ArrayList();
        Collections.addAll(newContentList, new Object[contentList.size()]);
        Collections.copy(newContentList, contentList);
        dataArray.add(newContentList);

        ArrayList newBXContentList = new ArrayList();
        Collections.addAll(newBXContentList, new Object[bxContentList.size()]);
        Collections.copy(newBXContentList, bxContentList);
        dataArray.add(newBXContentList);


        ArrayList newBJContentList = new ArrayList();
        Collections.addAll(newBJContentList, new Object[bjContentList.size()]);
        Collections.copy(newBJContentList, bjContentList);
        dataArray.add(newBJContentList);


        contentList.clear();
        bxContentList.clear();
        bjContentList.clear();
    }

    protected void calculateFundResult() {
        calculateResult(loanModel.getFundAmount(),loanModel.getFundRate(),loanModel.getYearCount());

        ArrayList newContentList = new ArrayList();
        Collections.addAll(newContentList, new Object[contentList.size()]);
        Collections.copy(newContentList, contentList);
        dataArray.add(newContentList);

        ArrayList newBXContentList = new ArrayList();
        Collections.addAll(newBXContentList, new Object[bxContentList.size()]);
        Collections.copy(newBXContentList, bxContentList);
        dataArray.add(newBXContentList);


        ArrayList newBJContentList = new ArrayList();
        Collections.addAll(newBJContentList, new Object[bjContentList.size()]);
        Collections.copy(newBJContentList, bjContentList);
        dataArray.add(newBJContentList);



        contentList.clear();
        bxContentList.clear();
        bjContentList.clear();
    }


    @SuppressLint("DefaultLocale")
    protected void calculateResult(float amount,float rate,int year) {

        // 基础数据
        BigDecimal rateRatio = new BigDecimal(100);
        BigDecimal moneyRatio = new BigDecimal(10000);
        BigDecimal monthRatio = new BigDecimal(12);
        BigDecimal foundRatio = new BigDecimal(1);
        BigDecimal towNumber = new BigDecimal(2);

        // 获取月利率
        BigDecimal yearRateNumber = new BigDecimal(rate);
        yearRateNumber = yearRateNumber.divide(rateRatio);
        BigDecimal monthRateNumber = yearRateNumber.divide(monthRatio,18, RoundingMode.CEILING);

        // 获取还款月数
        Integer monthCount = year * 12;
        BigDecimal yearCountNumber = new BigDecimal(year);
        BigDecimal monthCountNumber = yearCountNumber.multiply(monthRatio);

        // 贷款总金额
        BigDecimal loanMoneyNumber = new BigDecimal(amount);
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

        contentList.add(new ResultModel("贷款总额(万元)",String.format("%.2f",amount)));
        contentList.add(new ResultModel("贷款期限",String.format("%d(%d期)",year,monthCountNumber.toBigInteger())));


        double bxMPayMoney = bxMonthPayMoneyNumber.doubleValue();
        double totalPayMoney = bxMPayMoney * monthCount / 10000;
        double totalInterestMoney = totalPayMoney - amount;


        bxContentList.add(new ResultModel("每月还款(元)",String.format("%.2f",bxMPayMoney)));
        bxContentList.add(new ResultModel("总还款(万元)",String.format("%.2f",totalPayMoney)));
        bxContentList.add(new ResultModel("利息总额(万元)",String.format("%.2f",totalInterestMoney)));


        // 等额本金计算公式：每月还款金额 = （贷款本金 / 还款月数）+（本金 — （（贷款本金 / 还款月数 ）* 第几期）×每月利率 。

        // 1.（贷款本金 / 还款月数）
        BigDecimal cap1Number = loanMoneyNumber.divide(monthCountNumber,18,RoundingMode.CEILING);
        // 3.本金 × 每月利率
        BigDecimal cap3Number = loanMoneyNumber.multiply(monthRateNumber);
        // 首月还款
        BigDecimal capFirstMonthPayMoneyNumber = cap1Number.add(cap3Number);
        // 每月递减
        BigDecimal decreaseMoneyNumber = cap1Number.multiply(monthRateNumber);
        // 还款总利息
        BigDecimal totalInterestNumber = loanMoneyNumber.multiply(monthRateNumber);
        totalInterestNumber = totalInterestNumber.multiply(monthCountNumber.add(foundRatio));
        totalInterestNumber = totalInterestNumber.divide(towNumber,18,RoundingMode.CEILING);

        double capPerMonthDecMoney = decreaseMoneyNumber.doubleValue();
        double capResultMoney = capFirstMonthPayMoneyNumber.doubleValue();
        double capTotalInterestMoney = totalInterestNumber.doubleValue()/10000.0;
        double capTotalPayMoney = capTotalInterestMoney + amount;

        bjContentList.add(new ResultModel("首月还款(元)",String.format("%.2f",capResultMoney)));
        bjContentList.add(new ResultModel("每月递减(元)",String.format("%.2f",capPerMonthDecMoney)));
        bjContentList.add(new ResultModel("还款总额(万元)",String.format("%.2f",capTotalPayMoney)));
        bjContentList.add(new ResultModel("总利息(万元)",String.format("%.2f",capTotalInterestMoney)));

    }
}

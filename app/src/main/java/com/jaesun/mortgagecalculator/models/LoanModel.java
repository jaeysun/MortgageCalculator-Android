package com.jaesun.mortgagecalculator.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LoanModel implements Parcelable {
    float amount; // 贷款总金额
    float rate;   // 贷款利率
    float firstPurchaseRate; // 首付比例
    int yearCount; // 贷款年限
    float area;       // 房屋面积
    float perPrice;   // 房屋单价

    public LoanModel() {
    }

    protected LoanModel(Parcel in) {
        amount = in.readFloat();
        rate = in.readFloat();
        firstPurchaseRate = in.readFloat();
        yearCount = in.readInt();
        area = in.readFloat();
        perPrice = in.readFloat();
    }

    public static final Creator<LoanModel> CREATOR = new Creator<LoanModel>() {
        @Override
        public LoanModel createFromParcel(Parcel in) {
            return new LoanModel(in);
        }

        @Override
        public LoanModel[] newArray(int size) {
            return new LoanModel[size];
        }
    };

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getFirstPurchaseRate() {
        return firstPurchaseRate;
    }

    public void setFirstPurchaseRate(float firstPurchaseRate) {
        this.firstPurchaseRate = firstPurchaseRate;
    }

    public int getYearCount() {
        return yearCount;
    }

    public void setYearCount(int yearCount) {
        this.yearCount = yearCount;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(float perPrice) {
        this.perPrice = perPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(amount);
        parcel.writeFloat(rate);
        parcel.writeFloat(firstPurchaseRate);
        parcel.writeInt(yearCount);
        parcel.writeFloat(area);
        parcel.writeFloat(perPrice);
    }
}

package com.encore.piano.cardview.start;

import android.graphics.drawable.Drawable;

import com.encore.piano.R;

import java.util.ArrayList;

/**
 * Created by Kamran on 19-Nov-17.
 */

public class StartDataModel {
    private int mBackdrop;
    private String mText1;
    private String mText2;

    public StartDataModel(int backdrop, String text1, String text2){
        mBackdrop = backdrop;
        mText1 = text1;
        mText2 = text2;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public int getmBackdrop() {
        return mBackdrop;
    }

    public void setmBackdrop(int mBackdrop) {
        this.mBackdrop = mBackdrop;
    }

    public static ArrayList<StartDataModel> getDataSet() {
        ArrayList results = new ArrayList<>();
        StartDataModel obj = new StartDataModel(R.drawable.icons8_truck_100, "Assignments", "Pickup and Delivery");
        results.add(obj);
        obj = new StartDataModel(R.drawable.icons8_warehouse_100, "Warehouse", "Units in warehouse");
        results.add(obj);
        return results;
    }
}

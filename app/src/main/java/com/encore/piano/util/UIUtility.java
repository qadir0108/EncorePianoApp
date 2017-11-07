package com.encore.piano.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by Kamran on 25-Oct-17.
 */

public class UIUtility {

    public static void setVisible(Context context, int chk, boolean isVisible) {
        ((Activity)context).findViewById(chk).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public static boolean isChecked(Context context, int chk) {
        return ((CheckBox) ((Activity)context).findViewById(chk)).isChecked();
    }

}

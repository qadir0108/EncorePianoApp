package com.encore.piano.util;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 10/6/2017.
 */

public class AlertUtility {

    public static void ShowSuccess(Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Encore Piano App")
                .setContentText(message)
                .show();
    }

    public static void ShowError(Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Encore Piano App")
                .setContentText(message)
                .show();
    }
}

package com.encore.piano.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 12/6/2017.
 */

public class PermissionsUtility {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;

    public static void checkAndRequestPermissions(Activity activity) {
        int pCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int pLocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int pInternet = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int pExternalStorageWrite = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int pExternalStorageRead = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int pBlueTooth = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH);
        int pBlueToothAdmin = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN);
//        int pBlueToothPrivileged = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_PRIVILEGED);

        List<String> permissions = new ArrayList<>();
        if (pInternet != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.INTERNET);
        }
        if (pExternalStorageWrite != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (pExternalStorageWrite != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (pCamera != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (pLocation != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (pBlueTooth != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.BLUETOOTH);
        }
        if (pBlueToothAdmin != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
//        if (pBlueToothPrivileged != PackageManager.PERMISSION_GRANTED) {
//            permissions.add(Manifest.permission.BLUETOOTH_PRIVILEGED);
//        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }
}

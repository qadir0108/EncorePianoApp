package com.encore.piano.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Kamran on 23-Oct-17.
 */

public class FileUtility {

    private static final String RootFolder = Environment.getExternalStorageDirectory() + "/EncorePiano";
    private static final String ImagesRootFolder = RootFolder + "/Images/";
    private static final String PODRootFolder = RootFolder + "/POD/";
    private static final String DriversSignRootFolder = RootFolder + "/Drivers/";

    public static String getPODDirectory(String unitId) {
        return FileUtility.PODRootFolder + unitId + "/";
    }

    public static String getImagesDirectory(String unitId) {
        return FileUtility.ImagesRootFolder + unitId + "/";
    }

    public static String getDriversSignDirectory() {
        return FileUtility.DriversSignRootFolder;
    }

    public static void prepareDirectories()
    {
        prepareDirectory(RootFolder);
        prepareDirectory(ImagesRootFolder);
        prepareDirectory(PODRootFolder);
        prepareDirectory(DriversSignRootFolder);
    }

    public static boolean prepareDirectory(String directoryName)
    {
        try
        {
            return makeDirs(directoryName);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean makeDirs(String directoryName)
    {
        File tempDir = new File(directoryName);
        if (!tempDir.exists())
            tempDir.mkdirs();

        return (tempDir.isDirectory());
    }
}

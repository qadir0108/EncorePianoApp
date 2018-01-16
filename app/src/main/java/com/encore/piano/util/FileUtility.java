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
        String directory = FileUtility.PODRootFolder + unitId + "/";
        prepareDirectory(directory);
        return directory;
    }

    public static String getImagesDirectory(String unitId) {
        String directory = FileUtility.ImagesRootFolder + unitId + "/";
        prepareDirectory(directory);
        return directory;
    }

    public static String getDriversSignDirectory() {
        String directory = FileUtility.DriversSignRootFolder;
        prepareDirectory(directory);
        return directory;
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
            File directory = new File(directoryName);
            if (!directory.exists())
                directory.mkdirs();

            return (directory.isDirectory());

        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}

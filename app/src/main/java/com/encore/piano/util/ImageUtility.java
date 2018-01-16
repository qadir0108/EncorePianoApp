package com.encore.piano.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Kamran on 12-Nov-17.
 */

public class ImageUtility {

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static String encodePngToBase64(String filePath)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
    }

    public static String encodeJpegToBase64(String filePath)
    {
        filePath = filePath.replace("file://","");
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
    }

    //Bitmap bitmap = decodeBase64(encodedImage);
    //img.setImageBitmap(bitmap);
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input,0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}

package com.encore.piano.print;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.BitSet;

/**
 * Created by Kamran on 25-Dec-17.
 */
// https://stackoverflow.com/questions/14530058/how-can-i-print-an-image-on-a-bluetooth-printer-in-android
public class PrintUtils {

    private static final String TAG = "PRINT";
    private BitSet dots;
    private OutputStream mService;

    private void print_image(String file) throws IOException {
        File fl = new File(file);
        if (fl.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(file);
            convertBitmap(bmp);
            mService.write(PrinterCommands.SET_LINE_SPACING_24);

            int offset = 0;
            while (offset < bmp.getHeight()) {
                mService.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
                for (int x = 0; x < bmp.getWidth(); ++x) {

                    for (int k = 0; k < 3; ++k) {

                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }
                        mService.write(slice);
                    }
                }
                offset += 24;
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
            }
            mService.write(PrinterCommands.SET_LINE_SPACING_30);


        } else {
//            Toast.makeText(this, "file doesn't exists", Toast.LENGTH_SHORT)
//                    .show();
        }
    }

    public String convertBitmap(Bitmap inputBitmap) {

        int mWidth = inputBitmap.getWidth();
        int mHeight = inputBitmap.getHeight();

        convertArgbToGrayscale(inputBitmap, mWidth, mHeight);
        String mStatus = "ok";
        return mStatus;

    }

    private void convertArgbToGrayscale(Bitmap bmpOriginal, int width,
                                        int height) {
        int pixel;
        int k = 0;
        int B = 0, G = 0, R = 0;
        dots = new BitSet();
        try {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x);

                    // retrieve color of all channels
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k);//this is the bitset that i'm printing
                    }
                    k++;

                }


            }


        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.toString());
        }
    }
}

package com.encore.piano.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.encore.piano.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.util.UUID;

/**
 * Created by Kamran on 24-Dec-17.
 */

public class ZingCoder {

    private static int QRcodeWidth = 500;
    private static int QRcodeHeight = 500;

    public static Bitmap generateQRCode(Context context, String value) {

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeHeight, null
            );

            int bitMatrixWidth = bitMatrix.getWidth();

            int bitMatrixHeight = bitMatrix.getHeight();

            int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

            for (int y = 0; y < bitMatrixHeight; y++) {
                int offset = y * bitMatrixWidth;

                for (int x = 0; x < bitMatrixWidth; x++) {

                    pixels[offset + x] = bitMatrix.get(x, y) ?
                            context.getResources().getColor(R.color.colorBlack) : context.getResources().getColor(R.color.colorWhite);
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

            bitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);
            return bitmap;

        } catch (IllegalArgumentException exx) {
            exx.printStackTrace();
            return null;
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
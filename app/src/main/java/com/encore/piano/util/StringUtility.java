package com.encore.piano.util;

/**
 * Created by Kamran on 10/6/2017.
 */

public class StringUtility {
    public static boolean compare(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    public static boolean isNotNullOrEmpty(String str) {
        return (str != null && !str.isEmpty());
    }
}

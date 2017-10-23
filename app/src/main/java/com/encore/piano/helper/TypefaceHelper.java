package com.encore.piano.helper;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceHelper {
	
	public static final String ROBOTO_REGULAR = "Roboto-Regular";
	public static final String ROBOTO_BOLD = "Roboto-Bold";
	public static final String ROBOTO_ITALIC = "Roboto-Italic";
	public static final String ROBOTO_BOLD_ITALIC = "Roboto-BoldItalic";
	public static final String ROBOTO_MEDIUM = "Roboto-Medium";
	public static final String ROBOTO_MEDIUM_ITALIC = "Roboto-MediumItalic";
	public static final String ROBOTO_LIGHT = "Roboto-Light";
	public static final String ROBOTO_LIGHT_ITALIC = "Roboto-LightItalic";
	public static final String ROBOTO_THIN = "Roboto-Thin";
	public static final String ROBOTO_THIN_ITALIC = "Roboto-ThinItalic";

	private static final String FONT_PATH = "fonts/%s.ttf";
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface getTypeface(Context context, String typefaceName) {
		synchronized (cache) {
			if (!cache.containsKey(typefaceName)) {
				Typeface tf = Typeface.createFromAsset(context.getAssets(), String.format(FONT_PATH, typefaceName));
				cache.put(typefaceName, tf);
			}
			return cache.get(typefaceName);
		}
	}
	
}

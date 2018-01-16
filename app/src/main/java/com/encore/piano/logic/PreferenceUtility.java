package com.encore.piano.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.encore.piano.R;
import com.encore.piano.exceptions.ValueValidationException;

public class PreferenceUtility {

	public static String hostName;
	private static String port;
	public static String companyCode;
	public static String companyName;
	private static String gpsFrequency;

	public static PreferenceUtility GetPreferences(Context context){
		PreferenceUtility intance = new PreferenceUtility();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		hostName = preferences.getString("hostname", "http://10.0.2.2");
		port = preferences.getString("port", "8060");
		companyCode = preferences.getString("company_code", "");
		companyName = preferences.getString("company_name", "");
		gpsFrequency = preferences.getString("gps_frequency", "5");
		return intance;
	}

	public int getGpsFrequency() throws ValueValidationException {

		int value;
		try{
			value = Integer.parseInt(gpsFrequency);
		}
		catch(Exception ex)
		{
			throw new ValueValidationException();
		}

		return value;
	}

	public static int getPort(){

		int value = 80;
		try{
			value = Integer.parseInt(port);
		}
		catch(Exception ex)
		{
		}
		return value;
	}

    public static void setFirebaseInstanceId(Context context, String InstanceId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_firebase_instance_id_key),InstanceId);
        editor.apply();
    }

    public static String getFirebaseInstanceId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pref_firebase_instance_id_key);
        String default_value = context.getString(R.string.pref_firebase_instance_id_default_key);
        return sharedPreferences.getString(key, default_value);
    }
}

package com.encore.piano.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.encore.piano.R;
import com.encore.piano.exceptions.ValueValidationException;

public class PreferenceUtility {

	public static String Hostname;
	private static String Port;
	public static String CompanyCode;
	public static String CompanyName;
	private static String GpsFrequency;

	public static PreferenceUtility GetPreferences(Context context){

		PreferenceUtility intance = new PreferenceUtility();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

		Hostname = preferences.getString("hostname", "http://10.0.2.2");
		Port = preferences.getString("port", "8060");
		CompanyCode = preferences.getString("company_code", "");
		CompanyName = preferences.getString("company_name", "");
		GpsFrequency = preferences.getString("gps_frequency", "2");

		return intance;
	}

	public int GetGpsFrequency() throws ValueValidationException {

		int value;
		try{
			value = Integer.parseInt(GpsFrequency);
		}
		catch(Exception ex)
		{
			throw new ValueValidationException();
		}

		return value;
	}

	public static int GetPort(){

		int value = 80;
		try{
			value = Integer.parseInt(Port);
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

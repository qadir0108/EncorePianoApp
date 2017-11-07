package com.encore.piano.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.server.SyncService;
import com.encore.piano.util.Alerter;

public class SyncStart extends AsyncTask<String, Void, Object>
{
	private Context context;

	public SyncStart(Context c)
	{
		context = c;
	}

	@Override
	protected Object doInBackground(String... params)
	{
		try
		{
			SyncService dsp = new SyncService(context);
			dsp.syncStart(params[0]);
		} catch (DatabaseUpdateException ex)
		{
		} catch (UrlConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONNullableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkStatePermissionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptyStringException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result)
	{
		if (result instanceof Exception)
		{
            Alerter.error(context, "There is error." + ((Exception) result).getMessage());
			//Toast.makeText(context, result.getClass().getName(), Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}

}
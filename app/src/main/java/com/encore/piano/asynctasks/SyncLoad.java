package com.encore.piano.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.encore.piano.enums.TakenLocationEnum;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.server.SyncService;
import com.encore.piano.util.Alerter;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import java.io.IOException;

public class SyncLoad extends AsyncTask<String, Void, Object>
{
	private Context context;

	public SyncLoad(Context c)
	{
		context = c;
	}

	@Override
	protected Object doInBackground(String... params)
	{
		try
		{
			SyncService sync = new SyncService(context);
			sync.syncLoad(params[0], params[1]);
			sync.syncImages(params[0], params[1], TakenLocationEnum.Delivery.Value);
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
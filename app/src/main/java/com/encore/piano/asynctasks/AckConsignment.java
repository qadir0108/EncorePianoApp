package com.encore.piano.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.encore.piano.server.AcknowledgmentService;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;

public class AckConsignment extends AsyncTask<String, Void, Object>
{
	private Context context;

	public AckConsignment(Context c)
	{
		context = c;
	}

	@Override
	protected Object doInBackground(String... params)
	{
		String RunsheetId = (String)params[0];
		try
		{
			AcknowledgmentService ack = new AcknowledgmentService(context);
			ack.AcknowledgeConsignment(RunsheetId);
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
		} catch (DatabaseInsertException e) {
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
			Toast.makeText(context, result.getClass().getName(), Toast.LENGTH_LONG).show();
		}

		super.onPostExecute(result);
	}

}
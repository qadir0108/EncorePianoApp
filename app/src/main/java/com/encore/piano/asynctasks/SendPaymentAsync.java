package com.encore.piano.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.encore.piano.activities.AssignmentPayment;
import com.encore.piano.activities.StartScreen;
import com.encore.piano.data.StringConstants;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.server.PaymentService;
import com.encore.piano.server.SyncService;
import com.encore.piano.util.Alerter;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;

import java.io.IOException;

public class SendPaymentAsync extends AsyncTask<String, Void, Object>
{
	ProgressDialog progressDialog = null;
	private Activity context;

	public SendPaymentAsync(Activity c)
	{
		context = c;
	}

	protected void onPreExecute()
	{
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Please wait...");
		progressDialog.setMessage("Processing payment.");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		progressDialog.show();
		super.onPreExecute();
	};

	@Override
	protected Object doInBackground(String... params)
	{
        boolean success = false;
        PaymentService service = null;
        try {
            service = new PaymentService(context);
            success = service.process(params[0], params[1]);
            return success;
        } catch (UrlConnectionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JSONNullableException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (NetworkStatePermissionException e) {
            e.printStackTrace();
        } catch (EmptyStringException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DatabaseUpdateException e) {
            e.printStackTrace();
        }
		return success;
	}

	@Override
	protected void onPostExecute(Object result)
	{
        progressDialog.dismiss();

        if (result instanceof HttpHostConnectException)
            Alerter.error(context, "No connection");
        else if (result instanceof Exception && !("".equals(((Exception) result).getMessage())))
            Alerter.error(context, ((Exception) result).getMessage());
        else {
            boolean success = (boolean)result;
            if(success) {
                String message = "Credit card has been charged successfully.";
                Intent i = new Intent();
                i.putExtra(StringConstants.INTENT_KEY_MESSAGE, message);
                context.setResult(context.RESULT_OK, i);
                context.finish();
            } else {
                String message = "There was an error while processing payment";
                Alerter.error(context, message);
            }
        }
		super.onPostExecute(result);
	}

}
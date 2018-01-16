package com.encore.piano.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.encore.piano.activities.Login;
import com.encore.piano.activities.StartScreen;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.LoginException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.logic.PreferenceUtility;
import com.encore.piano.model.LoginModel;
import com.encore.piano.server.LoginService;
import com.encore.piano.server.Service;
import com.encore.piano.util.Alerter;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Kamran on 07-Nov-17.
 */

public class LoginAsync extends AsyncTask<LoginModel, Void, Object>
{
    ProgressDialog progressDialog = null;

    private Login context;
    public LoginAsync(Login c)
    {
        context = c;
    }

    protected void onPreExecute()
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Logging into application.");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
        super.onPreExecute();
    };

    String message = "";
    @Override
    protected Object doInBackground(LoginModel... params)
    {
        try
        {
            if (Service.loginService == null)
                Service.loginService = new LoginService(context);
            LoginModel loginModel = params[0];
            loginModel.setFCMToken(PreferenceUtility.getFirebaseInstanceId(context));
            message = Service.loginService.doLogin(loginModel);
            return message;

        } catch (UrlConnectionException e)
        {
            return e;
        } catch (JSONException e)
        {
            return e;
        } catch (JSONNullableException e)
        {
            return e;
        } catch (NotConnectedException e)
        {
            return e;
        } catch (NetworkStatePermissionException e)
        {
            return e;
        } catch (ClientProtocolException e)
        {
            return e;
        } catch (IOException e)
        {
            return e;
        } catch (LoginException e)
        {
            return e;
        } catch (EmptyStringException e)
        {
            return e;
        }

    }

    @Override
    protected void onPostExecute(Object result)
    {
        progressDialog.dismiss();

        if (result instanceof HttpHostConnectException)
            Alerter.error(context, "No connection");
        else if (result instanceof Exception && !("".equals(((Exception) result).getMessage())))
            Alerter.error(context, ((Exception) result).getMessage());
        else if (!message.equals(""))
        {
            Alerter.error(context, message);
        }
        else
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("UserName", context.txtUsername.getText().toString());
            editor.putString("Password", context.txtPassword.getText().toString());
            editor.commit();

            Intent i = new Intent(context, StartScreen.class);
            context.startActivity(i);
            context.finish();
        }
    }
}

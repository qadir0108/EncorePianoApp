package com.encore.piano.activities;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.encore.piano.R;
import com.encore.piano.services.LoginService;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.business.PreferenceUtility;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.LoginException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.LoginModel;
import com.encore.piano.util.ErrorLogUtility;

import io.fabric.sdk.android.Fabric;

public class Login extends AppCompatActivity implements OnClickListener, OnEditorActionListener {

    EditText Username;
    EditText Password;

    Button LoginButton;

    TextView tvCompanyName;

    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Fabric.with(this, new Crashlytics());
        Log.d("LOGIN_ACTIVITY", "ON_CREATE METHOD");

        ErrorLogUtility errorUtility = ErrorLogUtility.getInstance();
        errorUtility.Init(getApplicationContext());
        errorUtility.CheckCrashErrorAndSendLog(getApplicationContext());
//        int a = 0;
//        a = 1/0;

//		ActionBar actionBar = this.getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayShowTitleEnabled(false);

        Username = (EditText) findViewById(R.id.usernameEditText);
        Password = (EditText) findViewById(R.id.passwordEditText);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
        Username.setText(preferences.getString("UserName", "D101"));
        Password.setText(preferences.getString("Password", "P@kistan1"));

        Username.setOnEditorActionListener(this);
        Password.setOnEditorActionListener(this);

        LoginButton = (Button) findViewById(R.id.loginButton);
        LoginButton.setOnClickListener(this);

        PreferenceUtility.GetPreferences(this);
        tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
        tvCompanyName.setText(PreferenceUtility.CompanyName);

        try
        {
            ServiceUtility.loginService = new LoginService(Login.this);
            if (ServiceUtility.loginService.CheckLoginStatus())
            {
                Intent i = new Intent(this, StartScreen.class);
                startActivity(i);
                this.finish();
            }

        } catch (Exception e)
        {
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.loginButton:
                LoginModel loginModel = new LoginModel();
                loginModel.setUserName(Username.getText().toString());
                loginModel.setPassword(Password.getText().toString());
                new LoginAsync().execute(loginModel);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.mainmenu, menu);
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.settings:

                Intent i = new Intent(getApplicationContext(), Preferences.class);
                startActivity(i);

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoginAsync extends AsyncTask<LoginModel, Void, Object>
    {
        protected void onPreExecute()
        {

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setTitle("Logging");
            progressDialog.setMessage("Logging into application.");
            progressDialog.show();

            super.onPreExecute();
        };

        String message = "";

        @Override
        protected Object doInBackground(LoginModel... params)
        {
            try
            {

                if (ServiceUtility.loginService == null)
                    ServiceUtility.loginService = new LoginService(Login.this);

                LoginModel loginModel = params[0];
                loginModel.setFCMToken(PreferenceUtility.getFirebaseInstanceId(Login.this));
                message = ServiceUtility.loginService.Login(loginModel);

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
                Toast.makeText(Login.this, "No connection", Toast.LENGTH_SHORT).show();
            else if (result instanceof Exception && !("".equals(((Exception) result).getMessage())))
                Toast.makeText(Login.this, ((Exception) result).getMessage(), Toast.LENGTH_SHORT).show();
            else if (!message.equals(""))
            {
                ShowLoginMessage(message);
            }
            else
            {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserName", Username.getText().toString());
                editor.putString("Password", Password.getText().toString());
                editor.commit();

                Intent i = new Intent(Login.this, StartScreen.class);
                startActivity(i);
                Login.this.finish();
            }

        }

    }

    private void ShowLoginMessage(String message)
    {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        //	new LoginAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}

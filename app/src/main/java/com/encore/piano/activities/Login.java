package com.encore.piano.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.encore.piano.R;
import com.encore.piano.asynctasks.LoginAsync;
import com.encore.piano.data.StringConstants;
import com.encore.piano.security.AppSecurity;
import com.encore.piano.server.LoginService;
import com.encore.piano.server.Service;
import com.encore.piano.logic.PreferenceUtility;
import com.encore.piano.model.LoginModel;
import com.encore.piano.util.ErrorLogUtility;

import io.fabric.sdk.android.Fabric;

public class Login extends AppCompatActivity {

    public EditText txtUsername;
    public EditText txtPassword;
    Button btnLogin;
    TextView tvCompanyName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
//        startActivity(new Intent(this, AssignmentPayment.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Error Logging
        Fabric.with(this, new Crashlytics());
        ErrorLogUtility errorUtility = ErrorLogUtility.getInstance();
        errorUtility.Init(getApplicationContext());
        errorUtility.CheckCrashErrorAndSendLog(getApplicationContext());

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);

        loadSavedLogin();

        try
        {
            Service.loginService = new LoginService(Login.this);
            if (Service.loginService.checkLoginStatus())
            {
                Intent i = new Intent(this, StartScreen.class);
                startActivity(i);
                this.finish();
            }
        } catch (Exception e)
        {
        }

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.btnLogin:
                        LoginModel loginModel = new LoginModel();
                        loginModel.setUserName(txtUsername.getText().toString());
                        loginModel.setPassword(txtPassword.getText().toString());
                        new LoginAsync(Login.this).execute(loginModel);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void loadSavedLogin() {
        PreferenceUtility.GetPreferences(this);
        tvCompanyName.setText(StringConstants.COMPANY_NAME);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
        txtUsername.setText(preferences.getString("UserName", "D101"));
        txtPassword.setText(preferences.getString("Password", "P@kistan1"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.login, menu);
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
}
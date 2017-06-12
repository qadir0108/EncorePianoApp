package com.encore.piano.fcm;

import android.util.Log;

import com.encore.piano.business.PreferenceUtility;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Administrator on 9/6/2017.
 */

public class FirebaseRegistrationService extends FirebaseInstanceIdService {
    private static final String TAG = "EncorePiano";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
        PreferenceUtility.setFirebaseInstanceId(getApplicationContext(), refreshedToken);

    }
}

package com.encore.piano.server;

import android.content.Context;

import com.encore.piano.db.AssignmentDb;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.BaseModel;
import com.encore.piano.payment.PaymentProcessModel;
import com.encore.piano.util.DateTimeUtility;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class PaymentService extends BaseService {

	public PaymentService(Context context)
			throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		super(context);
	}

    public boolean process(String assignmentId, String token) throws JSONException, DatabaseUpdateException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
    {
        AssignmentModel model = Service.assignmentService.getAll(assignmentId);
        PaymentProcessModel syncModel = new PaymentProcessModel().fromModel(model, token);
        String url = ServiceUrls.getPaymentUrl(context).replace("[authtokenvalue]", syncModel.getAuthToken());

        if (post(url, syncModel.getJson())) {
			AssignmentDb.setPaid(context, model.getId(), DateTimeUtility.getCurrentTimeStamp());
			return true;
        } else
		{
			return false;
		}
    }

	@Override
	public URL getServiceUrl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent() throws JSONException, DatabaseInsertException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BaseModel> T decodeContent(JSONObject object)
	{
		// TODO Auto-generated method stub
		return null;
	}
}

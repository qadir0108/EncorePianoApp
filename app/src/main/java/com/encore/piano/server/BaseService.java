package com.encore.piano.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;
import android.net.ConnectivityManager;

import com.encore.piano.enums.JsonResponseEnum;
import com.encore.piano.enums.MessageEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.BaseModel;
import com.encore.piano.model.BaseModel.ServerResponse;

public abstract class BaseService {

	public abstract URL getServiceUrl();

	public abstract void setContent() throws JSONException,
			DatabaseInsertException;

	public abstract void fetchContent() throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException;

	public abstract <T extends BaseModel> T decodeContent(JSONObject object);

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String msg) {
		this.ErrorMessage = msg;
	}	

	private String ErrorMessage = "";
	protected static final String EMPTY_STRING = "";
	protected static Context context;

	public BaseService(Context context) {
		this.context = context;
	}

	public void initialize() throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		fetchContent();
		setContent();
	};

	protected synchronized ServerResponse getFromServer(
			URL jsonUrl, String arrayName) throws UrlConnectionException,
			JSONNullableException, JSONException, NotConnectedException,
			NetworkStatePermissionException {

		ServerResponse model = BaseModel.getServerResponse();
		JSONObject object = getJSONData(jsonUrl);

		model.setSuccess(setBooleanValueFromJSON(
				JsonResponseEnum.IsSucess.Value,
				object));

		model.setTokenValid(setBooleanValueFromJSON(
				JsonResponseEnum.IsTokenValid.Value,
				object));
		
		if (!model.isSuccess())
			model.setErrorMessage(setStringValueFromJSON(
					JsonResponseEnum.ErrorMessage.Value,
					object));
		else if(!model.isTokenValid()){
			model.setErrorMessage(Service.AUTH_TOKEN_INVALID);
		}
		else {
			model.setErrorMessage(EMPTY_STRING);
			
			try {
				model.setJsonArray(object.getJSONArray(arrayName));
			} catch (JSONException ex) {
				
			}
			
		}

		return model;
	}

	protected String setStringValueFromJSON(String property, JSONObject object) {
		String s;
		try {
			s = object.getString(property);
		} catch (JSONException ex) {
			return EMPTY_STRING;
		}
		return s;
	}

	protected int setIntValueFromJSON(String property, JSONObject object) {
		int i;
		try {
			i = object.getInt(property);
		} catch (JSONException ex) {
			i = 0;
		}
		return i;
	}

	protected double setDoubleValueFromJSON(String property, JSONObject object) {
		double i;
		try {
			i = object.getDouble(property);
		} catch (JSONException ex) {
			i = 0;
		}
		return i;
	}

	protected boolean setBooleanValueFromJSON(String property, JSONObject object) {
		boolean i;
		try {
			i = object.getBoolean(property);
		} catch (JSONException ex) {
			i = true; //only while authtokenvalid doesn not exist in all Message json objektima. Treba biti false;
		}
		return i;
	}

	protected String[] setStringArrayValuesFromJson(String property,
			JSONObject object) {
		String array[] = new String[5];

		try {
			JSONArray jsonArray = object.getJSONArray(property);

			int length;
			if (jsonArray.length() < 5)
				length = jsonArray.length();
			else
				length = 5;

			for (int i = 0; i < length; i++) {
				array[i] = jsonArray.getString(i);
			}
		} catch (JSONException ex) {
		}

		return array;
	}
	
	protected String StringToDate(String date, String dateFormat)
	{
		try {
			Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date);
			String returnV = new SimpleDateFormat(dateFormat).format(temp); 
			return returnV;
		} catch (ParseException e) {
			return EMPTY_STRING;
		}
		
		
	}

	protected JSONArray GetJSONArray(URL jsonUrl)
			throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		String json = downloadJSON(jsonUrl);

		JSONArray array = null;
		array = new JSONArray(json);

		if (array.equals(null))
			throw new JSONNullableException();

		return array;
	}

	protected JSONObject getJSONData(String json)
			throws JSONNullableException, JSONException {
		if (json.equals(EMPTY_STRING))
			throw new JSONNullableException();

		// if(json.startsWith("["))
		// json = json.replace("[", "");
		// if(json.endsWith("]"))
		// json = json.replace("]", "");

		JSONObject object = new JSONObject(json);

		if (object.equals(null))
			throw new JSONNullableException();

		return object;
	}

	protected JSONObject getJSONData(URL jsonUrl)
			throws UrlConnectionException, JSONNullableException,
			JSONException, NotConnectedException,
			NetworkStatePermissionException {
		String json = downloadJSON(jsonUrl);
		return getJSONData(json);
	}

	private synchronized static String downloadJSON(URL url)
			throws UrlConnectionException, NotConnectedException,
			NetworkStatePermissionException {

		isConnected();

		String value = "";
		try {
			URLConnection conn = url.openConnection();
			value = getString(conn.getInputStream());

		} catch (Exception e) {
			throw new UrlConnectionException();
		}

		return value;
	}

	protected static String getString(InputStream is)
			throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] b = new byte[4096];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		String value = os.toString();

		os.close();
		is.close();

		return value;
	}

	protected URL getURLFromString(String url) {
		URL u = null;
		try {
			u = new URL(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u;
	}

	public static void isConnected() throws NetworkStatePermissionException,
			NotConnectedException {
		try {
			ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (conman == null || conman.getActiveNetworkInfo() == null
					|| !conman.getActiveNetworkInfo().isConnectedOrConnecting())
				throw new NotConnectedException();
		} catch (SecurityException ex) {
			throw new NetworkStatePermissionException();
		}
	}

	public static boolean IsWiFiConnection(Context context) {
		try {
			ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (conman == null || conman.getActiveNetworkInfo() == null)
				throw new NotConnectedException();
			else
				return conman.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean postImage(String url, String imagePath)
			throws EmptyStringException {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
				httpClient.getParams(), httpClient.getConnectionManager()
						.getSchemeRegistry()), httpClient.getParams());

		HttpContext localContext = new BasicHttpContext();
		HttpPost signaturePost = new HttpPost(url);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (imagePath.contains("file://"))
			imagePath = imagePath.replace("file://", "");

		nameValuePairs.add(new BasicNameValuePair("attach1", imagePath));

		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			for (int index = 0; index < nameValuePairs.size(); index++) {
				if (nameValuePairs.get(index).getName()
						.equalsIgnoreCase("attach1")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(nameValuePairs.get(index).getName(),
							new FileBody(new File(nameValuePairs.get(index)
									.getValue())));
				}
			}

			signaturePost.setEntity(entity);
			HttpResponse signatureResponse = httpClient.execute(signaturePost,
					localContext);

			HttpEntity signatureResponseEntity = signatureResponse.getEntity();

			if (signatureResponseEntity != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						signatureResponseEntity.getContent()));

				String temp = "";
				StringBuilder responseStringBuilder = new StringBuilder();

				while ((temp = br.readLine()) != null) {
					responseStringBuilder.append(temp);
				}

				String response = responseStringBuilder.toString();

				if (response.equals(EMPTY_STRING))
					throw new EmptyStringException();
				 else
				 {
                     JSONObject object = getJSONData(response);
                     String messageJson = setStringValueFromJSON(MessageEnum.Message.Value, object);
                     boolean success = setBooleanValueFromJSON(JsonResponseEnum.IsSucess.Value,
                     getJSONData(messageJson));

                     if(!success)
                     setErrorMessage(setStringValueFromJSON(JsonResponseEnum.ErrorMessage.Value, getJSONData(messageJson)));
				 }
			}

		} catch (IOException e) {
			return false;
		} catch (JSONNullableException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
	}

	protected boolean post(String url, String requestJson)
			throws JSONException, ClientProtocolException, IOException,
			JSONNullableException {

		HttpPost postRequest = new HttpPost(url);
		postRequest.setHeader("Content-Type", "application/json");
		StringEntity loginEntity = new StringEntity(requestJson);
		postRequest.setEntity(loginEntity);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(postRequest);
		HttpEntity responseEntity = httpResponse.getEntity();
		if (responseEntity != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					responseEntity.getContent()));

			String temp = "";
			StringBuilder responseStringBuilder = new StringBuilder();

			while ((temp = br.readLine()) != null) {
				responseStringBuilder.append(temp);
			}
			String response = responseStringBuilder.toString();
			boolean success = setBooleanValueFromJSON(JsonResponseEnum.IsSucess.Value, getJSONData(response));
			if (!success)
				return false;
		}

		return true;
	}
}

package com.encore.piano.util;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import static com.encore.piano.util.CommonUtility.TAG;

public final class ServerUtility {

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     * @throws IOException propagated from POST.
     */
    private static boolean post(String endpoint, Map<String, String> params)
            throws IOException {
    	
    	boolean result = false;
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status == 200) {
            	BufferedReader in = null;  
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                
                String decodedString;
                while ((decodedString = in.readLine()) != null) {
                    //System.out.println(decodedString);
                	try {
						JSONObject tempObj = new JSONObject(decodedString);
						String tempStr = tempObj.getString("Message");
						JSONObject object = new JSONObject(tempStr);
						String resultStr = object.getString("IsSucess");
						result =  Boolean.parseBoolean(resultStr);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	//setStringValueFromJSON()
                    
                }
                in.close();
            }
            else
              throw new IOException("Post failed with error code " + status);
            
            return result;
            
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
}

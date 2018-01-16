package com.encore.piano.model;

import org.json.JSONArray;

public class BaseModel {

	public ServerResponse Instance;
	public static ServerResponse getServerResponse()
	{
		return new ServerResponse();
	}

    protected String AuthToken;
    public String getAuthToken() {
        return AuthToken;
    }
    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

	public static class ServerResponse extends BaseModel {
		private boolean IsSuccess;
        private boolean IsTokenValid;
        private String ErrorMessage;
		private JSONArray JsonArray = new JSONArray();
		public boolean isSuccess() {
			return IsSuccess;
		}
		public void setSuccess(boolean success) {
			IsSuccess = success;
		}
		public String getErrorMessage() {
			return ErrorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			ErrorMessage = errorMessage;
		}
		public JSONArray getJsonArray() {
			return JsonArray;
		}
		public void setJsonArray(JSONArray jsonArray) {
			JsonArray = jsonArray;
		}
		public boolean isTokenValid() {
			return IsTokenValid;
		}
		public void setTokenValid(boolean tokenValid) {
			IsTokenValid = tokenValid;
		}

	}
}

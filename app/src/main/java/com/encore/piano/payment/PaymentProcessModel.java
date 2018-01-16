package com.encore.piano.payment;


import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.BaseModel;
import com.encore.piano.server.Service;

import org.json.JSONException;
import org.json.JSONStringer;

public class PaymentProcessModel extends BaseModel {

    private String assignmentId;
    private String token;

	public PaymentProcessModel fromModel(AssignmentModel model, String token) {
		setAuthToken(Service.loginService.LoginModel.getAuthToken());
		setAssignmentId(model.getId());
		setToken(token);
		return this;
	}

	public String getJson() throws JSONException {
		JSONStringer stringer = new JSONStringer().object()
				.key(PaymentProcessEnum.AuthToken.Value).value(getAuthToken())
                .key(PaymentProcessEnum.assignmentId.Value).value(getAssignmentId())
                .key(PaymentProcessEnum.token.Value).value(getToken())
				.endObject();
		return stringer.toString();
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

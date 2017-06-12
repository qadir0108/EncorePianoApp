package com.encore.piano.model;

import com.encore.piano.services.ServiceUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SignatureModel extends BaseModel {

	private String Username;	
	private String LoginTime;	
	private String Signature;
	private String Guid;
	private String AuthToken;	

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}
	
	
	
	public String getUsername() {
		return Username;
	}

	public void setUsername() {
		Username = ServiceUtility.loginService.LoginModel.getUserName();
	}

	public String getLoginTime() {
		return LoginTime;
	}

	public void setLoginTime() {
		LoginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	public void setGuid(){
		UUID uuid = UUID.randomUUID();
		Guid = uuid.toString(); 
	}

	public String getGuid() {
		return Guid;
	}

	public String getAuthToken() {
		return AuthToken;
	}

	public void setAuthToken() {
		AuthToken = ServiceUtility.loginService.LoginModel.getAuthToken();
	}

	public enum SignatureEnum
	{		
		Signature("Signature"),
		LoginTime("timeStamp"),
		AuthToken("AuthToken"),		
		Guid("id"),
		CheckListItems("CheckListItems");
		
		public String Value;
		
		private SignatureEnum(String v){
			Value = v;
		}
	}
	
	public enum SignatureImageUploadEnum
	{		
		SignatureID("SignatureID"),
		AuthToken("AuthToken"),
		ImageID("ID");
		
		public String Value;
		
		private SignatureImageUploadEnum(String v){
			Value = v;
		}
	}
}

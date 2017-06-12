package com.encore.piano.interfaces;


// any body wanting a callback should implement this generic class
// and override the callback method
public interface CallBackHandler {
	
	/*
	 * requestCode is used for situations to tell the callback for what purpose the 
	 * request was given, typically usefull for callbacks in same class for different 
	 * purpose.
	 */
	public void callBack(Object response);
}

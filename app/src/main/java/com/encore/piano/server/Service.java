package com.encore.piano.server;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import android.os.Environment;
import android.util.Log;

public class Service {

    public static LoginService loginService = null;
	public static ConfirmationService confirmationService = null;
	public static SignatureService signatureService = null;
	public static AssignmentService assignmentService = null;
	public static UnitService unitService = null;
	public static TripService tripService = null;
	public static GalleryService galleryService = null;
	public static GPSTrackingService gpsTrackingService = null;
	public static GpxService gpxService = null;
	public static String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";

	public static final String MESSAGE_INTENT_KEY = "MESSAGE_KEY";
	public static final String CONSIGNMENT_IMAGE = "IMAGE";
	public static final String CUSTOMER_SIGN_CAPTURE_USERNAME = "CUSTOMER_SIGN_USERNAME";
	public static final String CUSTOMER_SIGN_CAPTURE_FILENAME = "CUSTOMER_SIGN_FILENAME";
	public static final String CUSTOMER_TRIP_STATUS = "CUSTOMER_TRIP_STATUS";
	public static final String CUSTOMER_POD_STATUS = "CUSTOMER_POD_STATUS";
	public static final String CUSTOMER_CONSIGNMENTID = "CONSIGMENTID";
	public static final String CONSIGNMENT_ID_LIST_KEY = "CONSIGNMENT_ID_LIST_KEY";
	public static final String CUSTOMER_SIGN_LIST = "CONSIGNMENT_SIGN_LIST";
	public static final String CONSIGNMENT_INTENT_KEY_MULTI = "CONSIGNMENT_ID";
	
	public static final String AUTH_TOKEN_INVALID = "$#authtokeninvalid$#";

}


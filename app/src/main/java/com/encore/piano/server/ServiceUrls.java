package com.encore.piano.server;

import android.content.Context;

import com.encore.piano.logic.PreferenceUtility;

/**
 * Created by Kamran on 6/6/2017.
 */

public class ServiceUrls {

    public static String getServiceUrl(Context context){
        PreferenceUtility preferenceUtility = PreferenceUtility.GetPreferences(context);
        return preferenceUtility.hostName + ":" + preferenceUtility.getPort();
    }

    public static String getLoginServiceUrl(Context context){
        return getServiceUrl(context) + "/api/user/login";
    }
    public static String getAssignmentsUrl(Context context){
        return getServiceUrl(context) + "/api/assignments?AuthToken=[authtokenvalue]";
    }
    public static String getAssignmentUrl(Context context){
        return getServiceUrl(context) + "/api/assignments?AuthToken=[authtokenvalue]&Id=[id]";
    }
    public static String getSyncStartUrl(Context context){
        return getServiceUrl(context) + "/api/sync/trip/start?AuthToken=[authtokenvalue]";
    }
    public static String getSyncStatusUrl(Context context){
        return getServiceUrl(context) + "/api/sync/trip/status?AuthToken=[authtokenvalue]";
    }
    public static String getSyncLoadUrl(Context context){
        return getServiceUrl(context) + "/api/sync/unit/load?AuthToken=[authtokenvalue]";
    }
    public static String getSyncDeliverUrl(Context context){
        return getServiceUrl(context) + "/api/sync/unit/deliver?AuthToken=[authtokenvalue]";
    }
    public static String getSyncImageUrl(Context context){
        return getServiceUrl(context) + "/api/sync/unit/image?AuthToken=[authtokenvalue]";
    }
    public static String getSendLogUrl(Context context){
        return getServiceUrl(context) + "/api/log";
    }
    public static String getPaymentUrl(Context context){
        return getServiceUrl(context) + "/api/payment/process?AuthToken=[authtokenvalue]";
    }

    public static String GetGpsSynchornizationUrl(Context context){
        return getServiceUrl(context);
    }
    public static String GetConfirmationConditionsUrl(Context context){
        return getServiceUrl(context) + "/WS/json/reply/GetCheckList?AuthToken=authtokenvalue";
    }
    public static String GetSignaturesUrl(Context context){
        return getServiceUrl(context) + "/WS/json/reply/SendDriverSignature";
    }
    public static String GetItemUrl(Context context){
        return getServiceUrl(context) + "/ws/json/syncreply/GetRunSheetItems?AuthToken=authtokenvalue&ConsignmentID=consignmentidvalue";
    }

    public static String GetTripUrl(Context context){
        return getServiceUrl(context);
    }
    public static String GetPodUrl(Context context){
        return getServiceUrl(context);
    }
    //
    public static String GetGpxUrl(Context context){
        return getServiceUrl(context) + "/WS/json/syncreply/GetRunSheetRoute?AuthToken=authtokenvalue&RunSheetID=runsheetidvalue";
    }

    public static String GetSignatureUploadServiceUrl(Context context){
        return getServiceUrl(context) + "/FileHandler/SendDriverSignature.aspx?ID=idimagevalue&SignatureID=signatureidvalue&AuthToken=authtokenvalue";
    }

    public static String GetConsignmentSignatureUploadServiceUrl(Context context){
        return getServiceUrl(context) + "/FileHandler/SendConsignmentSignature.aspx?AuthToken=authtokenvalue&ConsignmentID=consignmentidvalue";
    }
    public static String GetRunsheetAckUrl(Context context){
        return getServiceUrl(context) + "/ws/json/syncreply/UpdateRunsheetAck";
    }
    public static String GetConsignmentPhotosServiceUrl(Context context){
        return getServiceUrl(context) + "/FileHandler/SendConsignmentPhotos.aspx?AuthToken=authtokenvalue&ConsignmentID=consignmentidvalue&ID=imageidvalue";
    }

    public static String FinalizeConsignmentSynchonizationUrl(Context context){
        return getServiceUrl(context) + "/ws/json/syncreply/UpdateRunSheet";
    }

}

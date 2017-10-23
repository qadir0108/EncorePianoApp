package com.encore.piano.server;

import android.content.Context;

import com.encore.piano.logic.PreferenceUtility;

/**
 * Created by Kamran on 6/6/2017.
 */

public class ServiceUrls {

    public static String getServiceUrl(Context context){
        PreferenceUtility preferenceUtility = PreferenceUtility.GetPreferences(context);
        return preferenceUtility.Hostname + ":" + preferenceUtility.GetPort();
    }

    public static String getLoginServiceUrl(Context context){
        return getServiceUrl(context) + "/api/user/login";
    }
    public static String getFCMRegisterUrl(Context context){
        return getServiceUrl(context) + "/api/user/register";
    }
    public static String getConsignmentsUrl(Context context){
        return getServiceUrl(context) + "/api/consignments?AuthToken=[authtokenvalue]";
    }
    public static String getConsignmentUrl(Context context){
        return getServiceUrl(context) + "/api/consignment?AuthToken=[authtokenvalue]&Id=[id]";
    }
    public static String getSendLogUrl(Context context){
        return getServiceUrl(context) + "/ws/json/syncreply/SendErrorLog";
    }
    public static String GetDataSynchornizationUrl(Context context){
        return getServiceUrl(context) + "/ws/json/syncreply/UpdateConsignment";
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

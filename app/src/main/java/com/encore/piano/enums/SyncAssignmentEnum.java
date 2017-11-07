package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum SyncAssignmentEnum
{
    Id("Id"),
    tripStatus("tripStatus"),
    departureTime("departureTime"),
    estimatedTime("estimatedTime"),

    ConsignmentReference("ConsignmentReference"),
    ColCode("ColCode"),
    DeliveryCode("DeliveryCode"),
    CustomerReference("CustomerReference"),
    PodStatus("PodStatus"),
    AuthToken("AuthToken"),
    CustomerUsername("CustomerUsername"),
    DateSigned("DateSigned"),
    Images("Images"),
    Signed("Signed"),
    SignedBy("SignedBy"),
    SignatureImage("SignatureImage"),
    RunSheetID("RunSheetID"),
    MessageID("MessageID"),
    Acknowledged("Acknowledged"),
    Saved("Saved")
    ;

    public String Value;

    private SyncAssignmentEnum(String v){
        Value = v;
    }
}

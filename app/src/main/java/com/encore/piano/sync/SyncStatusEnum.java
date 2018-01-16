package com.encore.piano.sync;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum SyncStatusEnum
{
    AuthToken("AuthToken"),
    Id("Id"),
    tripStatus("tripStatus"),
    departureTime("departureTime"),
    estimatedTime("estimatedTime"),
    statusTime("statusTime"),
    ;

    public String Value;

    private SyncStatusEnum(String v){
        Value = v;
    }
}

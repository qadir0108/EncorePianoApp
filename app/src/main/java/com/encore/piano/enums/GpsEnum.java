package com.encore.piano.enums;

/**
 * Created by Kamran on 5/6/2017.
 */

public enum GpsEnum
{
    TableName("GPSDATA"),

    Latitude("Latitude"),
    Longitude("Longitude"),
    Timestamp("Timestamp"),
    IsSynced("IsSynced")
    ;

    public String Value;

    private GpsEnum(String v) {
        Value = v;
    }
}

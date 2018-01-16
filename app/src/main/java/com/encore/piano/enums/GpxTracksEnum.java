package com.encore.piano.enums;

/**
 * Created by Kamran on 5/6/2017.
 */

public enum GpxTracksEnum
{
    TableName("GPXTRACKS"),

    GpxTrackId("GpxTrackId"),
    GpxTrackName("GpxTrackName"),
    Latitude("Latitude"),
    Longitude("Longitude"),
    GpxOrder("GpxOrder"),
    ConsignmentId("AssignmentId"),
    Timestamp("Timestamp")
    ;

    public String Value;

    private GpxTracksEnum(String v) {
        Value = v;
    }
}

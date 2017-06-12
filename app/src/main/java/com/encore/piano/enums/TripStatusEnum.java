package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum TripStatusEnum
{
    NotStarted("NotStarted"),
    Started("Started"),
    OnWay("OnWay"),
    Completed("Completed");

    public String Value;

    private TripStatusEnum(String v) {
        Value = v;
    }
}

package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum CardEnum
{
    TableName("CARD"),

    ConsignmentId("AssignmentId"),
    CardDetail("CardDetail"),
    Timestamp("Timestamp"),
    synced("synced");

    public String Value;

    private CardEnum(String v) {
        Value = v;
    }
}

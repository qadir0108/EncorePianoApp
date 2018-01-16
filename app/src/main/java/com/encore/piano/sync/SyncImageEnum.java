package com.encore.piano.sync;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum SyncImageEnum
{
    AuthToken("AuthToken"),
    Id("Id"),
    assignmentId("assignmentId"),
    unitId("unitId"),
    image("image"),
    takenAt("takenAt"),
    takeLocation("takeLocation"),
    ;

    public String Value;

    private SyncImageEnum(String v){
        Value = v;
    }
}

package com.encore.piano.enums;

/**
 * Created by Kamran on 5/6/2017.
 */

public enum LoginEnum
{
    TableName("LOGIN"),

    UserId("UserId"),
    UserName("UserName"),
    VehicleCode("VehicleCode"),
    AuthToken("AuthToken"),
    AuthTokenExpiry("AuthTokenExpiry"),
    IsActive("IsActive"),
    ;

    public String Value;

    private LoginEnum(String v) {
        Value = v;
    }
}

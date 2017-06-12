package com.encore.piano.enums;

/**
 * Created by Administrator on 9/6/2017.
 */

public enum LoginModelEnum
{
    Username("UserName"),
    Password("Password"),
    AuthToken("AuthToken"),
    FCMToken("FCMToken");

    public String Value;

    private LoginModelEnum(String e){
        Value = e;
    }
}

package com.encore.piano.payment;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum PaymentProcessEnum
{
    AuthToken("AuthToken"),
    assignmentId("assignmentId"),
    token("token"),
    ;

    public String Value;

    private PaymentProcessEnum(String v){
        Value = v;
    }
}

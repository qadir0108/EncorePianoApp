package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum ConsignmentTypeEnum
{
    Normal("Normal"),
    Container("Container");

    public String Value;

    private ConsignmentTypeEnum(String v) {
        Value = v;
    }
}

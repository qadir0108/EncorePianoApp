package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum OrderTypeEnum
{
    Id("Id"),
    ConsignmentId("ConsignmentId"),
    Type("Type"),
    Name("Size"),
    Color("Color"),
    Make("Make"),
    Model("Model"),
    SerialNumber("SerialNumber"),
    IsStairs("IsPlayer"),
    IsBench("IsBench"),
    IsBoxed("IsBoxed");

    public String Value;

    private OrderTypeEnum(String v) {
        Value = v;
    }
}

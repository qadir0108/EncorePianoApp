package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum PianoEnum
{
    TableName("PIANO"),

    Id("Id"),
    ConsignmentId("ConsignmentId"),
    Type("Type"),
    Name("Name"),
    Color("Color"),
    Make("Make"),
    Model("Model"),
    SerialNumber("SerialNumber"),
    IsStairs("IsStairs"),
    IsBench("IsBench"),
    IsBoxed("IsBoxed"),

    createdAt("createdAt"),
    pianoStaus("pianoStaus");

    public String Value;

    private PianoEnum(String v) {
        Value = v;
    }
}

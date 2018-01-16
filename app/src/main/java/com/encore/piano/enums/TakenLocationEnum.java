package com.encore.piano.enums;

public enum TakenLocationEnum
{
    Pickup("Pickup"),
    Delivery("Delivery"),
    Warehouse("Warehouse")
    ;

    public String Value;

    private TakenLocationEnum(String v) {
        Value = v;
    }
}

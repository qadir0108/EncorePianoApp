package com.encore.piano.enums;

/**
 * Created by Kamran Qadir on 5/6/2017.
 */

public enum AdditionalItemStatusEnum
{
    NotAvailable("N/A"),
    Missing("Missing"),
    Loaded("Loaded"),
    Delivered("Delivered");

    public String Value;

    private AdditionalItemStatusEnum(String v) {
        Value = v;
    }
}

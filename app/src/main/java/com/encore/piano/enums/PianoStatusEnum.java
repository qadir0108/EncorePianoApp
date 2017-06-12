package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum PianoStatusEnum
{
    Perfect("Perfect"),
    Damaged("Damaged"),
    MinorDamaged("MinorDamaged");

    public String Value;

    private PianoStatusEnum(String v) {
        Value = v;
    }
}

package com.encore.piano.enums;

/**
 * Created by Kamran on 5/6/2017.
 */

public enum PianoStatusEnum
{
    Booked("Booked"),
    Picked("Picked"),
    Dispatched("Dispatched"),
    Delivered("Delivered"),
    Rejected("Rejected"),
    ExceptionsDefacts("ExceptionsDefacts"),
    ExceptionsScratches("ExceptionsScratches"),
    ExceptionsMissingAccessories("ExceptionsMissingAccessories");

    public String Value;

    private PianoStatusEnum(String v) {
        Value = v;
    }
}

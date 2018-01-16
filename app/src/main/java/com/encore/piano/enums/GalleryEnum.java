package com.encore.piano.enums;

/**
 * Created by Kamran on 5/6/2017.
 */

public enum GalleryEnum
{
    TableName("GALLERY"),

    Id("Id"),
    UnitId("UnitId"),
    ImagePath("ImagePath"),
    TakenAt("TakenAt"),
    TakeLocation("TakeLocation"),
    synced("synced")
    ;

    public String Value;

    private GalleryEnum(String v) {
        Value = v;
    }
}

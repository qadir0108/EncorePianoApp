package com.encore.piano.enums;

/**
 * Created by Kamran on 5/6/2017.
 */

public enum GalleryEnum
{
    TableName("GALLERY"),

    Id("Id"),
    ConsignmentId("ConsignmentId"),
    ImagePath("ImagePath"),
    ImageReference("ImageReference"),
    TakenAt("TakenAt")
    ;

    public String Value;

    private GalleryEnum(String v) {
        Value = v;
    }
}

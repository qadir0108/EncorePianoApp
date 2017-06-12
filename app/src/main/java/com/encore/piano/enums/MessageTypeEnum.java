package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum MessageTypeEnum {

    Id("Id"),
    Consignment("Consignment"),
    Message("Message");

    public String Value;

    private MessageTypeEnum(String v){
        Value = v;
    }
}

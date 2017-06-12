package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum JsonResponseEnum {

    IsSucess("IsSucess"),
    IsTokenValid("IsTokenValid"),
    ErrorMessage("ErrorMessage");

    public String Value;

    private JsonResponseEnum(String v){
        Value = v;
    }

}

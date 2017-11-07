package com.encore.piano.enums;

/**
 * Created by Kamran on 5/6/2017.
 */

public enum PianoEnum
{
    TableName("PIANO"),

    Id("Id"),
    ConsignmentId("ConsignmentId"),
    Category("Category"),
    Type("Type"),
    Size("Size"),
    Make("Make"),
    Model("Model"),
    Finish("Finish"),
    SerialNumber("SerialNumber"),
    IsBench("IsBench"),
    IsPlayer("IsPlayer"),
    IsBoxed("IsBoxed"),

    createdAt("createdAt"),
    pianoStatus("pianoStatus"),
    pickedAt("pickedAt"),
    additionalBenchesStatus("additionalBenchesStatus"),
    additionalCasterCupsStatus("additionalCasterCupsStatus"),
    additionalCoverStatus("additionalCoverStatus"),
    additionalLampStatus("additionalLampStatus"),
    additionalOwnersManualStatus("additionalOwnersManualStatus"),

    deliveredAt("deliveredAt"),
    benchesUnloaded("benchesUnloaded"),
    casterCupsUnloaded("casterCupsUnloaded"),
    coverUnloaded("coverUnloaded"),
    lampUnloaded("lampUnloaded"),
    ownersManualUnloaded("ownersManualUnloaded"),

    receiverName("receiverName"),
    receiverSignaturePath("receiverSignaturePath"),
    dateSigned("dateSigned"),
    signed("signed"),
    synced("synced")
    ;

    public String Value;

    private PianoEnum(String v) {
        Value = v;
    }
}

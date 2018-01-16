package com.encore.piano.enums;

/**
 * Created by Kamran on 5/6/2017.
 */

public enum PianoEnum
{
    TableName("PIANO"),

    Id("Id"),
    OrderId("OrderId"),
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
    pickerName("pickerName"),
    pickerSignaturePath("pickerSignaturePath"),
    additionalBench1Status("additionalBench1Status"),
    additionalBench2Status("additionalBench2Status"),
    additionalCasterCupsStatus("additionalCasterCupsStatus"),
    additionalCoverStatus("additionalCoverStatus"),
    additionalLampStatus("additionalLampStatus"),
    additionalOwnersManualStatus("additionalOwnersManualStatus"),
    additionalMisc1Status("additionalMisc1Status"),
    additionalMisc2Status("additionalMisc2Status"),
    additionalMisc3Status("additionalMisc3Status"),
    syncLoaded("syncLoaded"),

    deliveredAt("deliveredAt"),
    receiverName("receiverName"),
    receiverSignaturePath("receiverSignaturePath"),
    bench1Unloaded("bench1Unloaded"),
    bench2Unloaded("bench2Unloaded"),
    casterCupsUnloaded("casterCupsUnloaded"),
    coverUnloaded("coverUnloaded"),
    lampUnloaded("lampUnloaded"),
    ownersManualUnloaded("ownersManualUnloaded"),
    misc1Unloaded("misc1Unloaded"),
    misc2Unloaded("misc2Unloaded"),
    misc3Unloaded("misc3Unloaded"),
    syncDelivered("syncDelivered")
    ;


    public String Value;

    private PianoEnum(String v) {
        Value = v;
    }
}

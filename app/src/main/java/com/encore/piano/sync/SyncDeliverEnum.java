package com.encore.piano.sync;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum SyncDeliverEnum
{
    AuthToken("AuthToken"),
    Id("Id"),
    assignmentId("assignmentId"),
    pianoStatus("pianoStatus"),
    deliveredAt("deliveredAt"),
    receiverName("receiverName"),
    receiverSignature("receiverSignature"),
    bench1Unloaded("bench1Unloaded"),
    bench2Unloaded("bench2Unloaded"),
    casterCupsUnloaded("casterCupsUnloaded"),
    coverUnloaded("coverUnloaded"),
    lampUnloaded("lampUnloaded"),
    ownersManualUnloaded("ownersManualUnloaded"),
    misc1Unloaded("misc1Unloaded"),
    misc2Unloaded("misc2Unloaded"),
    misc3Unloaded("misc3Unloaded"),
    ;

    public String Value;

    private SyncDeliverEnum(String v){
        Value = v;
    }
}

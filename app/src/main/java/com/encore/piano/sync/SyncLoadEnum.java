package com.encore.piano.sync;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum SyncLoadEnum
{
    AuthToken("AuthToken"),
    Id("Id"),
    assignmentId("assignmentId"),
    isMainUnitLoaded("isMainUnitLoaded"),
    additionalBench1Status("additionalBench1Status"),
    additionalBench2Status("additionalBench2Status"),
    additionalCasterCupsStatus("additionalCasterCupsStatus"),
    additionalCoverStatus("additionalCoverStatus"),
    additionalLampStatus("additionalLampStatus"),
    additionalOwnersManualStatus("additionalOwnersManualStatus"),
    additionalMisc1Status("additionalMisc1Status"),
    additionalMisc2Status("additionalMisc2Status"),
    additionalMisc3Status("additionalMisc3Status"),
    loadingTimeStamp("loadingTimeStamp"),
    pickerName("pickerName"),
    pickerSignature("pickerSignature")
    ;

    public String Value;

    private SyncLoadEnum(String v){
        Value = v;
    }
}

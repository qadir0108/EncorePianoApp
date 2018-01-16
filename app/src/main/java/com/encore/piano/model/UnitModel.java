package com.encore.piano.model;

import com.encore.piano.enums.AdditionalItemStatusEnum;
import com.encore.piano.enums.PianoStatusEnum;

public class UnitModel extends BaseModel {

	private String Id;
	private String AssignmentId; // for View only, not using in db
	private String OrderId;
    private String Category;
    private String Type;
	private String Size;
    private String Make;
    private String Model;
    private String Finish;
    private String SerialNumber;
    private boolean IsBench;
    private boolean IsPlayer;
    private boolean IsBoxed;
    private String createdAt;
    private String pianoStatus;

    private String pickedAt;
    private String pickerName;
    private String pickerSignaturePath;
    private AdditionalItemStatusEnum additionalBench1Status;
    private AdditionalItemStatusEnum additionalBench2Status;
    private AdditionalItemStatusEnum additionalCasterCupsStatus;
    private AdditionalItemStatusEnum additionalCoverStatus;
    private AdditionalItemStatusEnum additionalLampStatus;
    private AdditionalItemStatusEnum additionalOwnersManualStatus;
    private AdditionalItemStatusEnum additionalMisc1Status;
    private AdditionalItemStatusEnum additionalMisc2Status;
    private AdditionalItemStatusEnum additionalMisc3Status;

    private String deliveredAt;
    private String receiverName;
    private String receiverSignaturePath;
    private boolean bench1Unloaded;
    private boolean bench2Unloaded;
    private boolean casterCupsUnloaded;
    private boolean coverUnloaded;
    private boolean lampUnloaded;
    private boolean ownersManualUnloaded;
    private boolean misc1Unloaded;
    private boolean misc2Unloaded;
    private boolean misc3Unloaded;

    private boolean syncLoaded;
    private boolean syncDelivered;

    public boolean isLoaded() {
        return PianoStatusEnum.Picked.name().equals(pianoStatus);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        this.OrderId = orderId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public boolean isPlayer() {
        return IsPlayer;
    }

    public void setPlayer(boolean player) {
        IsPlayer = player;
    }

    public boolean isBench() {
        return IsBench;
    }

    public void setBench(boolean bench) {
        IsBench = bench;
    }

    public boolean isBoxed() {
        return IsBoxed;
    }

    public void setBoxed(boolean boxed) {
        IsBoxed = boxed;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPianoStatus() {
        return pianoStatus;
    }

    public void setPianoStatus(String pianoStatus) {
        this.pianoStatus = pianoStatus;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getFinish() {
        return Finish;
    }

    public void setFinish(String finish) {
        Finish = finish;
    }

    public AdditionalItemStatusEnum getAdditionalLampStatus() {
        return additionalLampStatus;
    }

    public void setAdditionalLampStatus(AdditionalItemStatusEnum additionalLampStatus) {
        this.additionalLampStatus = additionalLampStatus;
    }

    public AdditionalItemStatusEnum getAdditionalOwnersManualStatus() {
        return additionalOwnersManualStatus;
    }

    public void setAdditionalOwnersManualStatus(AdditionalItemStatusEnum additionalOwnersManualStatus) {
        this.additionalOwnersManualStatus = additionalOwnersManualStatus;
    }

    public AdditionalItemStatusEnum getAdditionalCoverStatus() {
        return additionalCoverStatus;
    }

    public void setAdditionalCoverStatus(AdditionalItemStatusEnum additionalCoverStatus) {
        this.additionalCoverStatus = additionalCoverStatus;
    }

    public AdditionalItemStatusEnum getAdditionalCasterCupsStatus() {
        return additionalCasterCupsStatus;
    }

    public void setAdditionalCasterCupsStatus(AdditionalItemStatusEnum additionalCasterCupsStatus) {
        this.additionalCasterCupsStatus = additionalCasterCupsStatus;
    }

    public AdditionalItemStatusEnum getAdditionalBench1Status() {
        return additionalBench1Status;
    }

    public void setAdditionalBench1Status(AdditionalItemStatusEnum additionalBench1Status) {
        this.additionalBench1Status = additionalBench1Status;
    }

    public String getPickedAt() {
        return pickedAt;
    }

    public void setPickedAt(String pickedAt) {
        this.pickedAt = pickedAt;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public boolean isBench1Unloaded() {
        return bench1Unloaded;
    }

    public void setBench1Unloaded(boolean bench1Unloaded) {
        this.bench1Unloaded = bench1Unloaded;
    }

    public boolean isCasterCupsUnloaded() {
        return casterCupsUnloaded;
    }

    public void setCasterCupsUnloaded(boolean casterCupsUnloaded) {
        this.casterCupsUnloaded = casterCupsUnloaded;
    }

    public boolean isCoverUnloaded() {
        return coverUnloaded;
    }

    public void setCoverUnloaded(boolean coverUnloaded) {
        this.coverUnloaded = coverUnloaded;
    }

    public boolean isLampUnloaded() {
        return lampUnloaded;
    }

    public void setLampUnloaded(boolean lampUnloaded) {
        this.lampUnloaded = lampUnloaded;
    }

    public boolean isOwnersManualUnloaded() {
        return ownersManualUnloaded;
    }

    public void setOwnersManualUnloaded(boolean ownersManualUnloaded) {
        this.ownersManualUnloaded = ownersManualUnloaded;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverSignaturePath() {
        return receiverSignaturePath;
    }

    public void setReceiverSignaturePath(String receiverSignaturePath) {
        this.receiverSignaturePath = receiverSignaturePath;
    }

    public boolean isSyncLoaded() {
        return syncLoaded;
    }

    public void setSyncLoaded(boolean syncLoaded) {
        this.syncLoaded = syncLoaded;
    }

    public boolean isSyncDelivered() {
        return syncDelivered;
    }

    public void setSyncDelivered(boolean syncDelivered) {
        this.syncDelivered = syncDelivered;
    }

    public AdditionalItemStatusEnum getAdditionalBench2Status() {
        return additionalBench2Status;
    }

    public void setAdditionalBench2Status(AdditionalItemStatusEnum additionalBench2Status) {
        this.additionalBench2Status = additionalBench2Status;
    }

    public AdditionalItemStatusEnum getAdditionalMisc1Status() {
        return additionalMisc1Status;
    }

    public void setAdditionalMisc1Status(AdditionalItemStatusEnum additionalMisc1Status) {
        this.additionalMisc1Status = additionalMisc1Status;
    }

    public AdditionalItemStatusEnum getAdditionalMisc2Status() {
        return additionalMisc2Status;
    }

    public void setAdditionalMisc2Status(AdditionalItemStatusEnum additionalMisc2Status) {
        this.additionalMisc2Status = additionalMisc2Status;
    }

    public AdditionalItemStatusEnum getAdditionalMisc3Status() {
        return additionalMisc3Status;
    }

    public void setAdditionalMisc3Status(AdditionalItemStatusEnum additionalMisc3Status) {
        this.additionalMisc3Status = additionalMisc3Status;
    }

    public boolean isBench2Unloaded() {
        return bench2Unloaded;
    }

    public void setBench2Unloaded(boolean bench2Unloaded) {
        this.bench2Unloaded = bench2Unloaded;
    }

    public boolean isMisc1Unloaded() {
        return misc1Unloaded;
    }

    public void setMisc1Unloaded(boolean misc1Unloaded) {
        this.misc1Unloaded = misc1Unloaded;
    }

    public boolean isMisc2Unloaded() {
        return misc2Unloaded;
    }

    public void setMisc2Unloaded(boolean misc2Unloaded) {
        this.misc2Unloaded = misc2Unloaded;
    }

    public boolean isMisc3Unloaded() {
        return misc3Unloaded;
    }

    public void setMisc3Unloaded(boolean misc3Unloaded) {
        this.misc3Unloaded = misc3Unloaded;
    }

    public String getPickerName() {
        return pickerName;
    }

    public void setPickerName(String pickerName) {
        this.pickerName = pickerName;
    }

    public String getPickerSignaturePath() {
        return pickerSignaturePath;
    }

    public void setPickerSignaturePath(String pickerSignaturePath) {
        this.pickerSignaturePath = pickerSignaturePath;
    }

    public String getAssignmentId() {
        return AssignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        AssignmentId = assignmentId;
    }
}

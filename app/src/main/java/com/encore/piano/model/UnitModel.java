package com.encore.piano.model;

import com.encore.piano.enums.AdditionalItemStatusEnum;
import com.encore.piano.enums.PianoStatusEnum;

public class UnitModel extends BaseModel {

	private String Id;
	private String assignmentId;
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
    private AdditionalItemStatusEnum additionalBenchesStatus;
    private AdditionalItemStatusEnum additionalCasterCupsStatus;
    private AdditionalItemStatusEnum additionalCoverStatus;
    private AdditionalItemStatusEnum additionalLampStatus;
    private AdditionalItemStatusEnum additionalOwnersManualStatus;

    private String deliveredAt;
    private boolean benchesUnloaded;
    private boolean casterCupsUnloaded;
    private boolean coverUnloaded;
    private boolean lampUnloaded;
    private boolean ownersManualUnloaded;

    private String receiverName;
    private String receiverSignaturePath;
    private String dateSigned;
    private boolean signed;
    private boolean synced;

    public boolean isLoaded() {
        return PianoStatusEnum.Picked.name().equals(pianoStatus);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
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

    public AdditionalItemStatusEnum getAdditionalBenchesStatus() {
        return additionalBenchesStatus;
    }

    public void setAdditionalBenchesStatus(AdditionalItemStatusEnum additionalBenchesStatus) {
        this.additionalBenchesStatus = additionalBenchesStatus;
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

    public boolean isBenchesUnloaded() {
        return benchesUnloaded;
    }

    public void setBenchesUnloaded(boolean benchesUnloaded) {
        this.benchesUnloaded = benchesUnloaded;
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

    public String getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(String dateSigned) {
        this.dateSigned = dateSigned;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}

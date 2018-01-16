package com.encore.piano.sync;


import com.encore.piano.model.BaseModel;
import com.encore.piano.model.UnitModel;
import com.encore.piano.server.Service;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.ImageUtility;

import org.json.JSONException;
import org.json.JSONStringer;

public class SyncDeliverModel extends BaseModel {

    private String Id;
    private String assignmentId;
	private String pianoStatus;
    private String deliveredAt;
    private String receiverName;
    private String receiverSignature;
    private int bench1Unloaded;
    private int bench2Unloaded;
    private int casterCupsUnloaded;
    private int coverUnloaded;
    private int lampUnloaded;
    private int ownersManualUnloaded;
    private int misc1Unloaded;
    private int misc2Unloaded;
    private int misc3Unloaded;

	public SyncDeliverModel fromModel(UnitModel model) {
		setAuthToken(Service.loginService.LoginModel.getAuthToken());
		setId(model.getId());
		setAssignmentId(model.getAssignmentId());
        setPianoStatus(model.getPianoStatus());
        setDeliveredAt(DateTimeUtility.formatTimeStampToSend(DateTimeUtility.toDateTime(model.getDeliveredAt())));
        setReceiverName(model.getReceiverName());
        setReceiverSignature(ImageUtility.encodePngToBase64(model.getReceiverSignaturePath()));
        setBench1Unloaded(model.isBench1Unloaded() ? 1 : 0);
		setBench2Unloaded(model.isBench2Unloaded() ? 1 : 0);
        setCasterCupsUnloaded(model.isCasterCupsUnloaded() ? 1 : 0);
        setCoverUnloaded(model.isCoverUnloaded() ? 1 : 0);
        setLampUnloaded(model.isLampUnloaded() ? 1 : 0);
        setOwnersManualUnloaded(model.isOwnersManualUnloaded() ? 1 : 0);
        setMisc1Unloaded(model.isMisc1Unloaded() ? 1 : 0);
        setMisc2Unloaded(model.isMisc2Unloaded() ? 1 : 0);
        setMisc3Unloaded(model.isMisc3Unloaded() ? 1 : 0);
        return this;
	}

	public String getJson() throws JSONException {
		JSONStringer stringer = new JSONStringer().object()
				.key(SyncDeliverEnum.AuthToken.Value).value(getAuthToken())
                .key(SyncDeliverEnum.Id.Value).value(getId())
                .key(SyncDeliverEnum.assignmentId.Value).value(getAssignmentId())
                .key(SyncDeliverEnum.pianoStatus.Value).value(getPianoStatus())
                .key(SyncDeliverEnum.deliveredAt.Value).value(getDeliveredAt())
				.key(SyncDeliverEnum.bench1Unloaded.Value).value(getBench1Unloaded())
				.key(SyncDeliverEnum.bench2Unloaded.Value).value(getBench2Unloaded())
				.key(SyncDeliverEnum.casterCupsUnloaded.Value).value(getCasterCupsUnloaded())
				.key(SyncDeliverEnum.coverUnloaded.Value).value(getCoverUnloaded())
				.key(SyncDeliverEnum.lampUnloaded.Value).value(getLampUnloaded())
				.key(SyncDeliverEnum.ownersManualUnloaded.Value).value(getOwnersManualUnloaded())
				.key(SyncDeliverEnum.misc1Unloaded.Value).value(getMisc1Unloaded())
				.key(SyncDeliverEnum.misc2Unloaded.Value).value(getMisc2Unloaded())
				.key(SyncDeliverEnum.misc3Unloaded.Value).value(getMisc3Unloaded())
				.key(SyncDeliverEnum.receiverName.Value).value(getReceiverName())
				.key(SyncDeliverEnum.receiverSignature.Value).value(getReceiverSignature())
				.endObject();
		return stringer.toString();
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

    public String getPianoStatus() {
        return pianoStatus;
    }

    public void setPianoStatus(String pianoStatus) {
        this.pianoStatus = pianoStatus;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverSignature() {
        return receiverSignature;
    }

    public void setReceiverSignature(String receiverSignature) {
        this.receiverSignature = receiverSignature;
    }

    public int getBench1Unloaded() {
        return bench1Unloaded;
    }

    public void setBench1Unloaded(int bench1Unloaded) {
        this.bench1Unloaded = bench1Unloaded;
    }

    public int getCasterCupsUnloaded() {
        return casterCupsUnloaded;
    }

    public void setCasterCupsUnloaded(int casterCupsUnloaded) {
        this.casterCupsUnloaded = casterCupsUnloaded;
    }

    public int getCoverUnloaded() {
        return coverUnloaded;
    }

    public void setCoverUnloaded(int coverUnloaded) {
        this.coverUnloaded = coverUnloaded;
    }

    public int getLampUnloaded() {
        return lampUnloaded;
    }

    public void setLampUnloaded(int lampUnloaded) {
        this.lampUnloaded = lampUnloaded;
    }

    public int getOwnersManualUnloaded() {
        return ownersManualUnloaded;
    }

    public void setOwnersManualUnloaded(int ownersManualUnloaded) {
        this.ownersManualUnloaded = ownersManualUnloaded;
    }

    public int getBench2Unloaded() {
        return bench2Unloaded;
    }

    public void setBench2Unloaded(int bench2Unloaded) {
        this.bench2Unloaded = bench2Unloaded;
    }

    public int getMisc1Unloaded() {
        return misc1Unloaded;
    }

    public void setMisc1Unloaded(int misc1Unloaded) {
        this.misc1Unloaded = misc1Unloaded;
    }

    public int getMisc2Unloaded() {
        return misc2Unloaded;
    }

    public void setMisc2Unloaded(int misc2Unloaded) {
        this.misc2Unloaded = misc2Unloaded;
    }

    public int getMisc3Unloaded() {
        return misc3Unloaded;
    }

    public void setMisc3Unloaded(int misc3Unloaded) {
        this.misc3Unloaded = misc3Unloaded;
    }
}

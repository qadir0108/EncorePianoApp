package com.encore.piano.sync;


import com.encore.piano.model.BaseModel;
import com.encore.piano.model.UnitModel;
import com.encore.piano.server.Service;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.ImageUtility;

import org.json.JSONException;
import org.json.JSONStringer;

public class SyncLoadModel extends BaseModel {

    private String Id;
    private String assignmentId;
    private int isMainUnitLoaded;
	private int additionalBench1Status;
	private int additionalBench2Status;
	private int additionalCasterCupsStatus;
	private int additionalCoverStatus;
	private int additionalLampStatus;
	private int additionalOwnersManualStatus;
	private int additionalMisc1Status;
	private int additionalMisc2Status;
	private int additionalMisc3Status;
	private String loadingTimeStamp;
	private String pickerName;
	private String pickerSignature;

	public SyncLoadModel fromModel(UnitModel model) {
		setAuthToken(Service.loginService.LoginModel.getAuthToken());
		setId(model.getId());
		setAssignmentId(model.getAssignmentId());
		setIsMainUnitLoaded(model.isLoaded() ? 1 : 0);
        setAdditionalBench1Status(model.getAdditionalBench1Status().ordinal());
        setAdditionalBench2Status(model.getAdditionalBench2Status().ordinal());
        setAdditionalCasterCupsStatus(model.getAdditionalCasterCupsStatus().ordinal());
        setAdditionalCoverStatus(model.getAdditionalCoverStatus().ordinal());
        setAdditionalLampStatus(model.getAdditionalLampStatus().ordinal());
        setAdditionalOwnersManualStatus(model.getAdditionalOwnersManualStatus().ordinal());
        setAdditionalMisc1Status(model.getAdditionalMisc1Status().ordinal());
        setAdditionalMisc2Status(model.getAdditionalMisc2Status().ordinal());
        setAdditionalMisc3Status(model.getAdditionalMisc3Status().ordinal());
		setLoadingTimeStamp(DateTimeUtility.formatTimeStampToSend(DateTimeUtility.toDateTime(model.getPickedAt())));
		setPickerName(model.getPickerName());
		setPickerSignature(ImageUtility.encodePngToBase64(model.getPickerSignaturePath()));
		return this;
	}

	public String getJson() throws JSONException {
		JSONStringer stringer = new JSONStringer().object()
				.key(SyncLoadEnum.AuthToken.Value).value(getAuthToken())
                .key(SyncLoadEnum.Id.Value).value(getId())
                .key(SyncLoadEnum.assignmentId.Value).value(getAssignmentId())
                .key(SyncLoadEnum.isMainUnitLoaded.Value).value(getIsMainUnitLoaded())
				.key(SyncLoadEnum.additionalBench1Status.Value).value(getAdditionalBench1Status())
				.key(SyncLoadEnum.additionalBench2Status.Value).value(getAdditionalBench2Status())
				.key(SyncLoadEnum.additionalCasterCupsStatus.Value).value(getAdditionalCasterCupsStatus())
				.key(SyncLoadEnum.additionalCoverStatus.Value).value(getAdditionalCoverStatus())
				.key(SyncLoadEnum.additionalLampStatus.Value).value(getAdditionalLampStatus())
				.key(SyncLoadEnum.additionalOwnersManualStatus.Value).value(getAdditionalOwnersManualStatus())
				.key(SyncLoadEnum.additionalMisc1Status.Value).value(getAdditionalMisc1Status())
				.key(SyncLoadEnum.additionalMisc2Status.Value).value(getAdditionalMisc2Status())
				.key(SyncLoadEnum.additionalMisc3Status.Value).value(getAdditionalMisc3Status())
                .key(SyncLoadEnum.loadingTimeStamp.Value).value(getLoadingTimeStamp())
                .key(SyncLoadEnum.pickerName.Value).value(getPickerName())
                .key(SyncLoadEnum.pickerSignature.Value).value(getPickerSignature())
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

	public int getIsMainUnitLoaded() {
		return isMainUnitLoaded;
	}

	public void setIsMainUnitLoaded(int isMainUnitLoaded) {
		this.isMainUnitLoaded = isMainUnitLoaded;
	}

	public int getAdditionalBench1Status() {
		return additionalBench1Status;
	}

	public void setAdditionalBench1Status(int additionalBench1Status) {
		this.additionalBench1Status = additionalBench1Status;
	}

	public int getAdditionalCasterCupsStatus() {
		return additionalCasterCupsStatus;
	}

	public void setAdditionalCasterCupsStatus(int additionalCasterCupsStatus) {
		this.additionalCasterCupsStatus = additionalCasterCupsStatus;
	}

	public int getAdditionalCoverStatus() {
		return additionalCoverStatus;
	}

	public void setAdditionalCoverStatus(int additionalCoverStatus) {
		this.additionalCoverStatus = additionalCoverStatus;
	}

	public int getAdditionalLampStatus() {
		return additionalLampStatus;
	}

	public void setAdditionalLampStatus(int additionalLampStatus) {
		this.additionalLampStatus = additionalLampStatus;
	}

	public int getAdditionalOwnersManualStatus() {
		return additionalOwnersManualStatus;
	}

	public void setAdditionalOwnersManualStatus(int additionalOwnersManualStatus) {
		this.additionalOwnersManualStatus = additionalOwnersManualStatus;
	}

	public String getLoadingTimeStamp() {
		return loadingTimeStamp;
	}

	public void setLoadingTimeStamp(String loadingTimeStamp) {
		this.loadingTimeStamp = loadingTimeStamp;
	}

	public int getAdditionalBench2Status() {
		return additionalBench2Status;
	}

	public void setAdditionalBench2Status(int additionalBench2Status) {
		this.additionalBench2Status = additionalBench2Status;
	}

	public int getAdditionalMisc1Status() {
		return additionalMisc1Status;
	}

	public void setAdditionalMisc1Status(int additionalMisc1Status) {
		this.additionalMisc1Status = additionalMisc1Status;
	}

	public int getAdditionalMisc2Status() {
		return additionalMisc2Status;
	}

	public void setAdditionalMisc2Status(int additionalMisc2Status) {
		this.additionalMisc2Status = additionalMisc2Status;
	}

	public int getAdditionalMisc3Status() {
		return additionalMisc3Status;
	}

	public void setAdditionalMisc3Status(int additionalMisc3Status) {
		this.additionalMisc3Status = additionalMisc3Status;
	}

	public String getPickerName() {
		return pickerName;
	}

	public void setPickerName(String pickerName) {
		this.pickerName = pickerName;
	}

	public String getPickerSignature() {
		return pickerSignature;
	}

	public void setPickerSignature(String pickerSignature) {
		this.pickerSignature = pickerSignature;
	}
}

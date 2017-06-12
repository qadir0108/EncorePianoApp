package com.encore.piano.model;

public class PianoModel extends BaseModel {

	private String Id;
	private String ConsignmentId;
	private String Type;
	private String Name;
	private String Color;
	private String Model;
	private String Make;
	private String SerialNumber;
    private boolean IsStairs;
    private boolean IsBench;
    private boolean IsBoxed;

    private String createdAt;
    private String pianoStaus;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getConsignmentId() {
        return ConsignmentId;
    }

    public void setConsignmentId(String consignmentId) {
        ConsignmentId = consignmentId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public boolean isStairs() {
        return IsStairs;
    }

    public void setStairs(boolean stairs) {
        IsStairs = stairs;
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

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
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

    public String getPianoStaus() {
        return pianoStaus;
    }

    public void setPianoStaus(String pianoStaus) {
        this.pianoStaus = pianoStaus;
    }
}

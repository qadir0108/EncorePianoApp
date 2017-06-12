package com.encore.piano.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class ConsignmentModel extends BaseModel implements Serializable {

	private String Id;
	private String ConsignmentNumber;
	private String StartWarehouseName;
    private String StartWarehouseAddress;
    private String VehicleCode;
	private String VehicleName;
    private String DriverCode;
	private String DriverName;
	private String OrderId;
	private String OrderNumber;
    private String OrderType;
	private String OrderedAt;
	private String CallerName;
	private String CallerPhoneNumber;
	private String SpecialInstructions;
	private String PickupAddress;
	private String DeliveryAddress;
    private String CustomerCode;
    private String CustomerName;
    private int NumberOfItems;
    private PianoModel[] Pianos;

    private String createdAt;
    private String tripStatus;
    private String departureTime;
    private String arrivalTime;
	private LatLng pickupLocation;
	private String receiverSignaturePath;
	private String receiverName;
	private String dateSigned;
    private boolean signed;
	private boolean synced;
	private boolean saved;
	private boolean unread;

    // Not Used
    private String serviceType;

	@Override
	public String toString()
	{
		return this.getOrderNumber();
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getConsignmentNumber() {
		return ConsignmentNumber;
	}

	public void setConsignmentNumber(String consignmentNumber) {
		ConsignmentNumber = consignmentNumber;
	}

	public String getStartWarehouseName() {
		return StartWarehouseName;
	}

	public void setStartWarehouseName(String startWarehouseName) {
		StartWarehouseName = startWarehouseName;
	}

	public String getVehicleName() {
		return VehicleName;
	}

	public void setVehicleName(String vehicleName) {
		VehicleName = vehicleName;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}

	public String getOrderedAt() {
		return OrderedAt;
	}

	public void setOrderedAt(String orderedAt) {
		OrderedAt = orderedAt;
	}

	public String getCallerName() {
		return CallerName;
	}

	public void setCallerName(String callerName) {
		CallerName = callerName;
	}

	public String getCallerPhoneNumber() {
		return CallerPhoneNumber;
	}

	public void setCallerPhoneNumber(String callerPhoneNumber) {
		CallerPhoneNumber = callerPhoneNumber;
	}

	public String getSpecialInstructions() {
		return SpecialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		SpecialInstructions = specialInstructions;
	}

	public String getPickupAddress() {
		return PickupAddress;
	}

	public void setPickupAddress(String pickupAddress) {
		PickupAddress = pickupAddress;
	}

	public String getDeliveryAddress() {
		return DeliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		DeliveryAddress = deliveryAddress;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public LatLng getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(LatLng pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getReceiverSignaturePath() {
		return receiverSignaturePath;
	}

	public void setReceiverSignaturePath(String receiverSignaturePath) {
		this.receiverSignaturePath = receiverSignaturePath;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
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

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

    public String getVehicleCode() {
        return VehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        VehicleCode = vehicleCode;
    }

    public String getDriverCode() {
        return DriverCode;
    }

    public void setDriverCode(String driverCode) {
        DriverCode = driverCode;
    }

    public String getStartWarehouseAddress() {
        return StartWarehouseAddress;
    }

    public void setStartWarehouseAddress(String startWarehouseAddress) {
        StartWarehouseAddress = startWarehouseAddress;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getNumberOfItems() {
        return NumberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        NumberOfItems = numberOfItems;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public PianoModel[] getPianos() {
        return Pianos;
    }

    public void setPianos(PianoModel[] pianos) {
        Pianos = pianos;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}

package com.encore.piano.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class AssignmentModel extends BaseModel implements Serializable {

	private String Id;
	private String ConsignmentNumber;
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
	private String CallerPhoneNumberAlt;
	private String CallerEmail;

	private String PickupDate;
	private String PickupAddress;
	private String PickupPhoneNumber;
	private String PickupAlternateContact;
	private String PickupAlternatePhone;
	private String PickupNumberStairs;
	private String PickupNumberTurns;
	private String PickupInstructions;

	private String DeliveryDate;
	private String DeliveryAddress;
	private String DeliveryPhoneNumber;
	private String DeliveryAlternateContact;
	private String DeliveryAlternatePhone;
	private String DeliveryNumberStairs;
	private String DeliveryNumberTurns;
	private String DeliveryInstructions;

    private String CustomerCode;
    private String CustomerName;
    private int NumberOfItems;
    private UnitModel[] Pianos;

	private boolean unread;
	private String createdAt;
    private String tripStatus;
    private String departureTime;
    private String estimatedTime;
	private boolean synced;
	private boolean saved;

    // Not Used
    private String serviceType;
    private LatLng pickupLocation;

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

	public String getCallerPhoneNumberAlt() {
		return CallerPhoneNumberAlt;
	}

	public void setCallerPhoneNumberAlt(String callerPhoneNumberAlt) {
		CallerPhoneNumberAlt = callerPhoneNumberAlt;
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

	public String getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
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

    public UnitModel[] getPianos() {
        return Pianos;
    }

    public void setPianos(UnitModel[] pianos) {
        Pianos = pianos;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

	public String getCallerEmail() {
		return CallerEmail;
	}

	public void setCallerEmail(String callerEmail) {
		CallerEmail = callerEmail;
	}

	public String getPickupDate() {
		return PickupDate;
	}

	public void setPickupDate(String pickupDate) {
		PickupDate = pickupDate;
	}

	public String getPickupPhoneNumber() {
		return PickupPhoneNumber;
	}

	public void setPickupPhoneNumber(String pickupPhoneNumber) {
		PickupPhoneNumber = pickupPhoneNumber;
	}

	public String getPickupAlternateContact() {
		return PickupAlternateContact;
	}

	public void setPickupAlternateContact(String pickupAlternateContact) {
		PickupAlternateContact = pickupAlternateContact;
	}

	public String getPickupAlternatePhone() {
		return PickupAlternatePhone;
	}

	public void setPickupAlternatePhone(String pickupAlternatePhone) {
		PickupAlternatePhone = pickupAlternatePhone;
	}

	public String getPickupNumberStairs() {
		return PickupNumberStairs;
	}

	public void setPickupNumberStairs(String pickupNumberStairs) {
		PickupNumberStairs = pickupNumberStairs;
	}

	public String getPickupNumberTurns() {
		return PickupNumberTurns;
	}

	public void setPickupNumberTurns(String pickupNumberTurns) {
		PickupNumberTurns = pickupNumberTurns;
	}

	public String getPickupInstructions() {
		return PickupInstructions;
	}

	public void setPickupInstructions(String pickupInstructions) {
		PickupInstructions = pickupInstructions;
	}

	public String getDeliveryDate() {
		return DeliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		DeliveryDate = deliveryDate;
	}

	public String getDeliveryPhoneNumber() {
		return DeliveryPhoneNumber;
	}

	public void setDeliveryPhoneNumber(String deliveryPhoneNumber) {
		DeliveryPhoneNumber = deliveryPhoneNumber;
	}

	public String getDeliveryAlternateContact() {
		return DeliveryAlternateContact;
	}

	public void setDeliveryAlternateContact(String deliveryAlternateContact) {
		DeliveryAlternateContact = deliveryAlternateContact;
	}

	public String getDeliveryAlternatePhone() {
		return DeliveryAlternatePhone;
	}

	public void setDeliveryAlternatePhone(String deliveryAlternatePhone) {
		DeliveryAlternatePhone = deliveryAlternatePhone;
	}

	public String getDeliveryNumberStairs() {
		return DeliveryNumberStairs;
	}

	public void setDeliveryNumberStairs(String deliveryNumberStairs) {
		DeliveryNumberStairs = deliveryNumberStairs;
	}

	public String getDeliveryNumberTurns() {
		return DeliveryNumberTurns;
	}

	public void setDeliveryNumberTurns(String deliveryNumberTurns) {
		DeliveryNumberTurns = deliveryNumberTurns;
	}

	public String getDeliveryInstructions() {
		return DeliveryInstructions;
	}

	public void setDeliveryInstructions(String deliveryInstructions) {
		DeliveryInstructions = deliveryInstructions;
	}

    public void setPickupLocation(LatLng pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LatLng getPickupLocation() {
        return pickupLocation;
    }
}

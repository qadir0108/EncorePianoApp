package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum ConsignmentEnum
{
    TableName("CONSIGNMENT"),

    Id("Id"),
    ConsignmentNumber("ConsignmentNumber"),
    StartWarehouseName("StartWarehouseName"),
    StartWarehouseAddress("StartWarehouseAddress"),
    VehicleCode("FCMToken"),
    VehicleName("VehicleName"),
    DriverCode("DriverCode"),
    DriverName("DriverName"),
    OrderId("OrderId"),
    OrderNumber("OrderNumber"),
    OrderedAt("OrderedAt"),
    OrderType("OrderType"),
    CallerName("CallerName"),
    CallerPhoneNumber("CallerPhoneNumber"),
    SpecialInstructions("SpecialInstructions"),
    PickupAddress("PickupAddress"),
    DeliveryAddress("DeliveryAddress"),
    CustomerCode("CustomerCode"),
    CustomerName("CustomerName"),
    NumberOfItems("NumberOfItems"),

    createdAt("createdAt"),
    tripStaus("tripStaus"),
    unread("unread"),
    departureTime("departureTime"),
    arrivalTime("arrivalTime"),
    pickupLocation("pickupLocation"),
    receiverName("receiverName"),
    receiverSignaturePath("receiverSignaturePath"),
    dateSigned("dateSigned"),
    signed("signed"),
    saved("saved"),
    synced("synced");

    public String Value;

    private ConsignmentEnum(String v) {
        Value = v;
    }
}

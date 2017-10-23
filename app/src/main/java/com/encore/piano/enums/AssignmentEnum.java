package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum AssignmentEnum
{
    TableName("ASSIGNMENT"),

    Id("Id"),
    ConsignmentNumber("ConsignmentNumber"),
    VehicleCode("VehicleCode"),
    VehicleName("VehicleName"),
    DriverCode("DriverCode"),
    DriverName("DriverName"),

    OrderId("OrderId"),
    OrderNumber("OrderNumber"),
    OrderType("OrderType"),
    OrderedAt("OrderedAt"),
    CallerName("CallerName"),
    CallerPhoneNumber("CallerPhoneNumber"),
    CallerPhoneNumberAlt("CallerPhoneNumberAlt"),
    CallerEmail("CallerEmail"),

    PickupDate("PickupDate"),
    PickupAddress("PickupAddress"),
    PickupPhoneNumber("PickupPhoneNumber"),
    PickupAlternateContact("PickupAlternateContact"),
    PickupAlternatePhone("PickupAlternatePhone"),
    PickupNumberStairs("PickupNumberStairs"),
    PickupNumberTurns("PickupNumberTurns"),
    PickupInstructions("PickupInstructions"),

    DeliveryDate("DeliveryDate"),
    DeliveryAddress("DeliveryAddress"),
    DeliveryPhoneNumber("DeliveryPhoneNumber"),
    DeliveryAlternateContact("DeliveryAlternateContact"),
    DeliveryAlternatePhone("DeliveryAlternatePhone"),
    DeliveryNumberStairs("DeliveryNumberStairs"),
    DeliveryNumberTurns("DeliveryNumberTurns"),
    DeliveryInstructions("DeliveryInstructions"),

    CustomerCode("CustomerCode"),
    CustomerName("CustomerName"),
    NumberOfItems("NumberOfItems"),

    createdAt("createdAt"),
    tripStaus("tripStaus"),
    unread("unread"),
    departureTime("departureTime"),
    estimatedTime("estimatedTime"),

    pickupLocation("pickupLocation"),
    receiverName("receiverName"),
    receiverSignaturePath("receiverSignaturePath"),
    dateSigned("dateSigned"),
    signed("signed"),
    saved("saved"),
    synced("synced");

    public String Value;

    private AssignmentEnum(String v) {
        Value = v;
    }
}

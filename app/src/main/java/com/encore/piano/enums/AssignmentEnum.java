package com.encore.piano.enums;

/**
 * Created by Administrator on 5/6/2017.
 */

public enum AssignmentEnum
{
    TableName("ASSIGNMENT"),

    Id("Id"),
    AssignmentNumber("AssignmentNumber"),
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

    PickupName("PickupName"),
    PickupDate("PickupDate"),
    PickupAddress("PickupAddress"),
    PickupPhoneNumber("PickupPhoneNumber"),
    PickupAlternateContact("PickupAlternateContact"),
    PickupAlternatePhone("PickupAlternatePhone"),
    PickupNumberStairs("PickupNumberStairs"),
    PickupNumberTurns("PickupNumberTurns"),
    PickupInstructions("PickupInstructions"),

    DeliveryName("DeliveryName"),
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
    PaymentOption("PaymentOption"),
    PaymentAmount("PaymentAmount"),
    LegDate("LegDate"),
    LegFromLocation("LegFromLocation"),
    LegToLocation("LegToLocation"),
    NumberOfItems("NumberOfItems"),

    createdAt("createdAt"),
    tripStaus("tripStaus"),
    unread("unread"),
    pickupLocation("pickupLocation"),
    departureTime("departureTime"),
    estimatedTime("estimatedTime"),
    completionTime("completionTime"),
    saved("saved"),
    synced("synced"),

    paid("paid"),
    paymentTime("paymentTime");

    public String Value;

    private AssignmentEnum(String v) {
        Value = v;
    }
}

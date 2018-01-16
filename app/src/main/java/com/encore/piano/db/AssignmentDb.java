package com.encore.piano.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encore.piano.enums.AssignmentEnum;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.model.AssignmentModel;

import java.util.ArrayList;

public class AssignmentDb extends Database {

	public AssignmentDb(Context context) {
		super(context);
	}

	private static synchronized long writeAssignment(Context context, AssignmentModel consignment)
    {
        ContentValues cv = new ContentValues();
        cv.put(AssignmentEnum.Id.Value, consignment.getId());
        cv.put(AssignmentEnum.AssignmentNumber.Value, consignment.getAssignmentNumber());
		cv.put(AssignmentEnum.VehicleCode.Value, consignment.getVehicleCode());
		cv.put(AssignmentEnum.VehicleName.Value, consignment.getVehicleName());
		cv.put(AssignmentEnum.DriverCode.Value, consignment.getDriverCode());
		cv.put(AssignmentEnum.DriverName.Value, consignment.getDriverName());
		cv.put(AssignmentEnum.OrderId.Value, consignment.getOrderId());
		cv.put(AssignmentEnum.OrderNumber.Value, consignment.getOrderNumber());
		cv.put(AssignmentEnum.OrderType.Value, consignment.getOrderType());
		cv.put(AssignmentEnum.CallerName.Value, consignment.getCallerName());
		cv.put(AssignmentEnum.CallerPhoneNumber.Value, consignment.getCallerPhoneNumber());
		cv.put(AssignmentEnum.CallerPhoneNumberAlt.Value, consignment.getCallerPhoneNumberAlt());
		cv.put(AssignmentEnum.CallerEmail.Value, consignment.getCallerEmail());

		cv.put(AssignmentEnum.PickupName.Value, consignment.getPickupName());
		cv.put(AssignmentEnum.PickupDate.Value, consignment.getPickupDate());
        cv.put(AssignmentEnum.PickupAddress.Value, consignment.getPickupAddress());
        cv.put(AssignmentEnum.PickupPhoneNumber.Value, consignment.getPickupPhoneNumber());
        cv.put(AssignmentEnum.PickupAlternateContact.Value, consignment.getPickupAlternateContact());
        cv.put(AssignmentEnum.PickupAlternatePhone.Value, consignment.getPickupAlternatePhone());
        cv.put(AssignmentEnum.PickupNumberStairs.Value, consignment.getPickupNumberStairs());
        cv.put(AssignmentEnum.PickupNumberTurns.Value, consignment.getPickupNumberTurns());
        cv.put(AssignmentEnum.PickupInstructions.Value, consignment.getPickupInstructions());

		cv.put(AssignmentEnum.DeliveryName.Value, consignment.getDeliveryName());
		cv.put(AssignmentEnum.DeliveryDate.Value, consignment.getDeliveryDate());
		cv.put(AssignmentEnum.DeliveryAddress.Value, consignment.getDeliveryAddress());
		cv.put(AssignmentEnum.DeliveryPhoneNumber.Value, consignment.getDeliveryPhoneNumber());
		cv.put(AssignmentEnum.DeliveryAlternateContact.Value, consignment.getDeliveryAlternateContact());
		cv.put(AssignmentEnum.DeliveryAlternatePhone.Value, consignment.getDeliveryAlternatePhone());
		cv.put(AssignmentEnum.DeliveryNumberStairs.Value, consignment.getDeliveryNumberStairs());
		cv.put(AssignmentEnum.DeliveryNumberTurns.Value, consignment.getDeliveryNumberTurns());
		cv.put(AssignmentEnum.DeliveryInstructions.Value, consignment.getDeliveryInstructions());

        cv.put(AssignmentEnum.CustomerCode.Value, consignment.getCustomerCode());
        cv.put(AssignmentEnum.CustomerName.Value, consignment.getCustomerName());
        cv.put(AssignmentEnum.PaymentOption.Value, consignment.getPaymentOption());
        cv.put(AssignmentEnum.PaymentAmount.Value, consignment.getPaymentAmount());
        cv.put(AssignmentEnum.LegDate.Value, consignment.getLegDate());
        cv.put(AssignmentEnum.LegFromLocation.Value, consignment.getLegFromLocation());
        cv.put(AssignmentEnum.LegToLocation.Value, consignment.getLegToLocation());
        cv.put(AssignmentEnum.NumberOfItems.Value, consignment.getNumberOfItems());

        cv.put(AssignmentEnum.createdAt.Value, consignment.getCreatedAt());
        cv.put(AssignmentEnum.tripStaus.Value, consignment.getTripStatus());
        cv.put(AssignmentEnum.unread.Value, consignment.isUnread());

        SQLiteDatabase wdb = getSqliteHelper(context);
        long result = wdb.insert(AssignmentEnum.TableName.Value, null, cv);
        return result;
    }

    public static synchronized ArrayList<AssignmentModel> getAll(Context context, boolean showCompleted, boolean notSynced, String consignmentId)
    {
        SQLiteDatabase rdb = getSqliteHelper(context);
        ArrayList<AssignmentModel> models = new ArrayList<>();
        String cols[] = new String[] {
                AssignmentEnum.Id.Value,
                AssignmentEnum.AssignmentNumber.Value,
				AssignmentEnum.VehicleCode.Value,
				AssignmentEnum.VehicleName.Value,
				AssignmentEnum.DriverCode.Value,
				AssignmentEnum.DriverName.Value,

				AssignmentEnum.OrderId.Value, // 6
				AssignmentEnum.OrderNumber.Value,
				AssignmentEnum.OrderType.Value,
				AssignmentEnum.OrderedAt.Value,
				AssignmentEnum.CallerName.Value,
				AssignmentEnum.CallerPhoneNumber.Value,
				AssignmentEnum.CallerPhoneNumberAlt.Value,
                AssignmentEnum.CallerEmail.Value,

				AssignmentEnum.PickupName.Value, // 14
				AssignmentEnum.PickupDate.Value,
				AssignmentEnum.PickupAddress.Value,
				AssignmentEnum.PickupPhoneNumber.Value,
				AssignmentEnum.PickupAlternateContact.Value,
				AssignmentEnum.PickupAlternatePhone.Value,
				AssignmentEnum.PickupNumberStairs.Value,
				AssignmentEnum.PickupNumberTurns.Value,
				AssignmentEnum.PickupInstructions.Value,

				AssignmentEnum.DeliveryName.Value, // 23
				AssignmentEnum.DeliveryDate.Value,
				AssignmentEnum.DeliveryAddress.Value,
				AssignmentEnum.DeliveryPhoneNumber.Value,
				AssignmentEnum.DeliveryAlternateContact.Value,
				AssignmentEnum.DeliveryAlternatePhone.Value,
				AssignmentEnum.DeliveryNumberStairs.Value,
				AssignmentEnum.DeliveryNumberTurns.Value,
				AssignmentEnum.DeliveryInstructions.Value,

                AssignmentEnum.CustomerCode.Value, // 32
                AssignmentEnum.CustomerName.Value,
                AssignmentEnum.PaymentOption.Value,
                AssignmentEnum.PaymentAmount.Value,
                AssignmentEnum.LegDate.Value,
                AssignmentEnum.LegFromLocation.Value,
                AssignmentEnum.LegToLocation.Value,
                AssignmentEnum.NumberOfItems.Value,

                AssignmentEnum.createdAt.Value, // 40
                AssignmentEnum.tripStaus.Value,
                AssignmentEnum.unread.Value,
                AssignmentEnum.departureTime.Value,
                AssignmentEnum.estimatedTime.Value,
                AssignmentEnum.completionTime.Value,
                AssignmentEnum.saved.Value,
                AssignmentEnum.synced.Value,

                AssignmentEnum.paid.Value, // 48
                AssignmentEnum.paymentTime.Value,
        };

        Cursor results = null;
        if (showCompleted)
            results = rdb.query(AssignmentEnum.TableName.Value, cols, AssignmentEnum.tripStaus.Value + " = ? ", new String[] { TripStatusEnum.Completed.Value }, null, null, AssignmentEnum.OrderNumber + " ASC");
        else if(notSynced)
            results = rdb.query(AssignmentEnum.TableName.Value, cols, AssignmentEnum.saved.Value + " = ? AND " + AssignmentEnum.synced.Value + " = ? ", new String[] { String.valueOf(1), "0" }, null, null, AssignmentEnum.OrderNumber  + " ASC");
        else if(consignmentId != "")
            results = rdb.query(AssignmentEnum.TableName.Value, cols, AssignmentEnum.Id.Value + " = ? ", new String[] { consignmentId }, null, null, AssignmentEnum.OrderNumber  + " ASC");
        else if (!showCompleted)
            results = rdb.query(AssignmentEnum.TableName.Value, cols, AssignmentEnum.tripStaus.Value + " != ? ", new String[] { TripStatusEnum.Completed.Value }, null, null, AssignmentEnum.OrderNumber + " ASC");
        else
            results = rdb.query(AssignmentEnum.TableName.Value, cols, null, null, null, null, AssignmentEnum.OrderNumber  + " ASC");

        while (results.moveToNext())
        {
            AssignmentModel model = new AssignmentModel();
            model.setId(results.getString(0));
            model.setAssignmentNumber(results.getString(1));
            model.setVehicleCode(results.getString(2));
            model.setVehicleName(results.getString(3));
            model.setDriverCode(results.getString(4));
            model.setDriverName(results.getString(5));

            model.setOrderId(results.getString(6));
            model.setOrderNumber(results.getString(7));
			model.setOrderType(results.getString(8));
			model.setOrderedAt(results.getString(9));
            model.setCallerName(results.getString(10));
            model.setCallerPhoneNumber(results.getString(11));
            model.setCallerPhoneNumberAlt(results.getString(12));
			model.setCallerEmail(results.getString(13));

			model.setPickupName(results.getString(14));
			model.setPickupDate(results.getString(15));
			model.setPickupAddress(results.getString(16));
			model.setPickupPhoneNumber(results.getString(17));
			model.setPickupAlternateContact(results.getString(18));
			model.setPickupAlternatePhone(results.getString(19));
			model.setPickupNumberStairs(results.getString(20));
			model.setPickupNumberTurns(results.getString(21));
			model.setPickupInstructions(results.getString(22));

			model.setDeliveryName(results.getString(23));
			model.setDeliveryDate(results.getString(24));
			model.setDeliveryAddress(results.getString(25));
			model.setDeliveryPhoneNumber(results.getString(26));
			model.setDeliveryAlternateContact(results.getString(27));
			model.setDeliveryAlternatePhone(results.getString(28));
			model.setDeliveryNumberStairs(results.getString(29));
			model.setDeliveryNumberTurns(results.getString(30));
			model.setDeliveryInstructions(results.getString(31));

            model.setCustomerCode(results.getString(32));
            model.setCustomerName(results.getString(33));
            model.setPaymentOption(results.getString(34));
            model.setPaymentAmount(results.getString(35));
            model.setLegDate(results.getString(36));
            model.setLegFromLocation(results.getString(37));
            model.setLegToLocation(results.getString(38));
            model.setNumberOfItems(results.getInt(39));

            model.setCreatedAt(results.getString(40));
            model.setTripStatus(results.getString(41));
            model.setUnread(results.getInt(42) == 1);
            model.setDepartureTime(results.getString(43));
            model.setEstimatedTime(results.getString(44));
            model.setCompletionTime(results.getString(45));
            model.setSaved(results.getInt(46) == 1);
            model.setSynced(results.getInt(47) == 1);

            model.setPaid(results.getInt(48) == 1);
            model.setPaymentTime(results.getString(49));
            models.add(model);
        }

        results.close();
        rdb.close();
        return models;
    }

    public static synchronized int writeAll(Context context, ArrayList<AssignmentModel> consignments)
	{
		int imported = 0;
		try
		{
			for (int i = 0; i < consignments.size(); i++)
			{
				ArrayList<AssignmentModel> alreadys = getAll(context, false, false, consignments.get(i).getId());
				if (alreadys.size() != 0)
				{
					AssignmentModel already = alreadys.get(0);
					// if assignment is not yet started
					if (TripStatusEnum.NotStarted.Value.equals(already.getTripStatus())) {
                        // delete
                        delete(context, already.getId());
                        // delete units
                        UnitDb.delete(context, already.getId());

                        if (writeAssignment(context, consignments.get(i)) != -1)
                            imported++;
                    }
				} else // If it does not exists, simply writes
                {
                    if (writeAssignment(context, consignments.get(i)) != -1)
                        imported++;
                }
			}
		} catch (Exception ex)
		{
			imported = -1;
		}
		finally
		{
		}

		return imported;

	}

	public static synchronized void setSynced(Context context, String consignmentId) throws DatabaseUpdateException
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(AssignmentEnum.synced.Value, 1);
		if (wdb.update(AssignmentEnum.TableName.Value, cv, AssignmentEnum.Id.Value + " = ?", new String[] { consignmentId }) != 1)
			throw new DatabaseUpdateException();
		wdb.close();
	}

	public static synchronized boolean isAllSyned(Context context)
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		Cursor results = rdb.query(AssignmentEnum.TableName.Value, null, AssignmentEnum.synced + " = 0", null, null, null, null);
		int count = results.getCount();
		results.close();
		rdb.close();
		return count == 0 ? true : false;
	}

	public static synchronized void delete(Context context, ArrayList<AssignmentModel> cons)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		for (AssignmentModel assignmentModel : cons)
		{
			wdb.delete(AssignmentEnum.TableName.Value, AssignmentEnum.Id.Value + " = '" + assignmentModel.getId() + "'", null);
		}
		wdb.close();
	}

    public static synchronized void delete(Context context, String assignmentId)
    {
        SQLiteDatabase wdb = getSqliteHelper(context);
        wdb.delete(AssignmentEnum.TableName.Value, AssignmentEnum.Id.Value + " = '" + assignmentId + "'", null);
        wdb.close();
    }

	public static synchronized int setTripStatus(Context context, String consignmentId, String tripStatus, String statusTime)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
        cv.put(AssignmentEnum.tripStaus.Value, tripStatus);
		cv.put(AssignmentEnum.completionTime.Value, statusTime);
		cv.put(AssignmentEnum.saved.Value, 1);
        int result = wdb.update(AssignmentEnum.TableName.Value, cv, AssignmentEnum.Id.Value + " = ?", new String[] { consignmentId });
		wdb.close();
		return result;
	}

	public static synchronized int startTrip(Context context, String assignmentId, String departureTime, String estimatedTime)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
        cv.put(AssignmentEnum.tripStaus.Value, TripStatusEnum.Started.Value);
        cv.put(AssignmentEnum.departureTime.Value, departureTime);
        cv.put(AssignmentEnum.estimatedTime.Value, estimatedTime);
        cv.put(AssignmentEnum.saved.Value, 1);
		int result = wdb.update(AssignmentEnum.TableName.Value, cv, AssignmentEnum.Id.Value + " = ?", new String[] { assignmentId });
		wdb.close();
		return result;
	}

    public static synchronized int completeTrip(Context context, String assignmentId)
    {
        SQLiteDatabase wdb = getSqliteHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(AssignmentEnum.tripStaus.Value, TripStatusEnum.Completed.Value);
        cv.put(AssignmentEnum.saved.Value, 1);
        int result = wdb.update(AssignmentEnum.TableName.Value, cv, AssignmentEnum.Id.Value + " = ?", new String[] { assignmentId });
        wdb.close();
        return result;
    }

    public static synchronized int setPaid(Context context, String assignmentId, String paymentTime)
    {
        SQLiteDatabase wdb = getSqliteHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(AssignmentEnum.paid.Value, 1);
        cv.put(AssignmentEnum.paymentTime.Value, paymentTime);
        int result = wdb.update(AssignmentEnum.TableName.Value, cv, AssignmentEnum.Id.Value + " = ?", new String[] { assignmentId });
        wdb.close();
        return result;
    }
}

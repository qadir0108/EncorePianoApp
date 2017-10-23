package com.encore.piano.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encore.piano.enums.AssignmentEnum;
import com.encore.piano.enums.PianoEnum;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.util.DateTimeUtility;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class AssignmentDb extends Database {

	public AssignmentDb(Context context) {
		super(context);
	}

	private static synchronized long WriteConsignment(Context context, AssignmentModel consignment)
    {
        ContentValues cv = new ContentValues();
        cv.put(AssignmentEnum.Id.Value, consignment.getId());
        cv.put(AssignmentEnum.ConsignmentNumber.Value, consignment.getConsignmentNumber());
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

		cv.put(AssignmentEnum.PickupDate.Value, consignment.getPickupDate());
        cv.put(AssignmentEnum.PickupAddress.Value, consignment.getPickupAddress());
        cv.put(AssignmentEnum.PickupPhoneNumber.Value, consignment.getPickupPhoneNumber());
        cv.put(AssignmentEnum.PickupAlternateContact.Value, consignment.getPickupAlternateContact());
        cv.put(AssignmentEnum.PickupAlternatePhone.Value, consignment.getPickupAlternatePhone());
        cv.put(AssignmentEnum.PickupNumberStairs.Value, consignment.getPickupNumberStairs());
        cv.put(AssignmentEnum.PickupNumberTurns.Value, consignment.getPickupNumberTurns());
        cv.put(AssignmentEnum.PickupInstructions.Value, consignment.getPickupInstructions());

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
        ArrayList<AssignmentModel> listmodel = new ArrayList<AssignmentModel>();
        String cols[] = new String[] {
                AssignmentEnum.Id.Value,
                AssignmentEnum.ConsignmentNumber.Value,
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

				AssignmentEnum.PickupDate.Value, // 14
				AssignmentEnum.PickupAddress.Value,
				AssignmentEnum.PickupPhoneNumber.Value,
				AssignmentEnum.PickupAlternateContact.Value,
				AssignmentEnum.PickupAlternatePhone.Value,
				AssignmentEnum.PickupNumberStairs.Value,
				AssignmentEnum.PickupNumberTurns.Value,
				AssignmentEnum.PickupInstructions.Value,

				AssignmentEnum.DeliveryDate.Value, // 22
				AssignmentEnum.DeliveryAddress.Value,
				AssignmentEnum.DeliveryPhoneNumber.Value,
				AssignmentEnum.DeliveryAlternateContact.Value,
				AssignmentEnum.DeliveryAlternatePhone.Value,
				AssignmentEnum.DeliveryNumberStairs.Value,
				AssignmentEnum.DeliveryNumberTurns.Value,
				AssignmentEnum.DeliveryInstructions.Value,

                AssignmentEnum.CustomerCode.Value, // 30
                AssignmentEnum.CustomerName.Value,
                AssignmentEnum.NumberOfItems.Value,

                AssignmentEnum.createdAt.Value, // 33
                AssignmentEnum.tripStaus.Value,
                AssignmentEnum.unread.Value,
                AssignmentEnum.departureTime.Value,
                AssignmentEnum.estimatedTime.Value,
                AssignmentEnum.pickupLocation.Value,
                AssignmentEnum.receiverName.Value,
                AssignmentEnum.receiverSignaturePath.Value,
                AssignmentEnum.dateSigned.Value,
                AssignmentEnum.signed.Value,
                AssignmentEnum.saved.Value,
                AssignmentEnum.synced.Value,
        };

        Cursor results = null;
        if (showCompleted)
            results = rdb.query(AssignmentEnum.TableName.Value, cols, AssignmentEnum.tripStaus.Value + " = ? ", new String[] { TripStatusEnum.Completed.Value }, null, null, AssignmentEnum.OrderNumber + " ASC");
        else if(notSynced)
            results = rdb.query(AssignmentEnum.TableName.Value, cols, AssignmentEnum.saved.Value + " = ? AND " + AssignmentEnum.synced.Value + " = ? ", new String[] { String.valueOf(1), "0" }, null, null, AssignmentEnum.OrderNumber  + " ASC");
        else if(consignmentId != "")
            results = rdb.query(AssignmentEnum.TableName.Value, cols, AssignmentEnum.Id.Value + " = ? ", new String[] { consignmentId }, null, null, AssignmentEnum.OrderNumber  + " ASC");
        else
            results = rdb.query(AssignmentEnum.TableName.Value, cols, null, null, null, null, AssignmentEnum.OrderNumber  + " ASC");

        while (results.moveToNext())
        {
            AssignmentModel model = new AssignmentModel();
            model.setId(results.getString(0));
            model.setConsignmentNumber(results.getString(1));
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

			model.setPickupDate(results.getString(14));
			model.setPickupAddress(results.getString(15));
			model.setPickupPhoneNumber(results.getString(16));
			model.setPickupAlternateContact(results.getString(17));
			model.setPickupAlternatePhone(results.getString(18));
			model.setPickupNumberStairs(results.getString(19));
			model.setPickupNumberTurns(results.getString(20));
			model.setPickupInstructions(results.getString(21));

			model.setDeliveryDate(results.getString(22));
			model.setDeliveryAddress(results.getString(23));
			model.setDeliveryPhoneNumber(results.getString(24));
			model.setDeliveryAlternateContact(results.getString(25));
			model.setDeliveryAlternatePhone(results.getString(26));
			model.setDeliveryNumberStairs(results.getString(27));
			model.setDeliveryNumberTurns(results.getString(28));
			model.setDeliveryInstructions(results.getString(29));

            model.setCustomerCode(results.getString(30));
            model.setCustomerName(results.getString(31));
            model.setNumberOfItems(results.getInt(32));

            model.setCreatedAt(results.getString(33));
            model.setTripStatus(results.getString(34));
            model.setUnread(results.getInt(35) == 1);
            model.setDepartureTime(results.getString(36));
            model.setEstimatedTime(results.getString(37));
            model.setPickupLocation(new LatLng(results.getDouble(38),results.getDouble(38)));
            model.setReceiverName(results.getString(39));
            model.setReceiverSignaturePath(results.getString(40));
            model.setDateSigned(results.getString(41));
            model.setSigned(results.getInt(42) == 1);
            model.setSaved(results.getInt(43) == 1);
            model.setSynced(results.getInt(44) == 1);
            listmodel.add(model);
        }

        results.close();
        rdb.close();
        return listmodel;
    }

    public static synchronized int writeAll(Context context, ArrayList<AssignmentModel> consignments)
	{
		int imported = 0;
		try
		{
			for (int i = 0; i < consignments.size(); i++)
			{
				ArrayList<AssignmentModel> alreadyExistsConsigments = getAll(context, false, false, consignments.get(i).getId());
				if (alreadyExistsConsigments.size() != 0)
				{
					AssignmentModel already = alreadyExistsConsigments.get(0);
					// if assignmentService is not started yet
					if (!TripStatusEnum.NotStarted.Value.equals(already.getTripStatus())) {
                        // delete
                        SQLiteDatabase wdb = getSqliteHelper(context);
                        wdb.delete(AssignmentEnum.TableName.Value, AssignmentEnum.Id.Value + " = '" + already.getId() + "'", null);
                        wdb.close();
                        // delete units
                        SQLiteDatabase wdb1 = getSqliteHelper(context);
                        wdb1.delete(PianoEnum.TableName.Value, PianoEnum.ConsignmentId.Value + " = '" + already.getId() + "'", null);
                        wdb1.close();

                        if (WriteConsignment(context, consignments.get(i)) != -1)
                            imported++;
                    }
				} else // If it does not exists, simply writes
                {
                    if (WriteConsignment(context, consignments.get(i)) != -1)
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

	public static synchronized int writeCustomerSignature(Context context, String customerName, String customerSinaturePath, String consignmentId)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		int returnValue = 1;
		try
		{
			wdb.beginTransaction();
			ContentValues cv = new ContentValues();
			cv.put(AssignmentEnum.signed.Value, 1);
			cv.put(AssignmentEnum.receiverName.Value, customerName);
			cv.put(AssignmentEnum.receiverSignaturePath.Value, customerSinaturePath);
			cv.put(AssignmentEnum.dateSigned.Value, DateTimeUtility.getCurrentTimeStamp());
			if ((returnValue = wdb.update(AssignmentEnum.TableName.Value, cv, AssignmentEnum.Id.Value + " = ?", new String[] { consignmentId })) != 1)
				throw new DatabaseUpdateException();

			wdb.setTransactionSuccessful();
		} catch (Exception ex)
		{

		}
		finally
		{
			wdb.endTransaction();
		}

		wdb.close();
		return returnValue;
	}

	public static synchronized int setTripStatus(Context context, String consignmentId, String tripStatus)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
        cv.put(AssignmentEnum.tripStaus.Value, tripStatus);
        cv.put(AssignmentEnum.saved.Value, 1);
        int result = wdb.update(AssignmentEnum.TableName.Value, cv, AssignmentEnum.Id.Value + " = ?", new String[] { consignmentId });
		wdb.close();
		return result;
	}

	public static synchronized int startTrip(Context context, String consignmentId, String departureTime, String estimatedTime)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
        cv.put(AssignmentEnum.tripStaus.Value, TripStatusEnum.Started.Value);
        cv.put(AssignmentEnum.departureTime.Value, departureTime);
        cv.put(AssignmentEnum.estimatedTime.Value, estimatedTime);
        cv.put(AssignmentEnum.saved.Value, 1);
		int result = wdb.update(AssignmentEnum.TableName.Value, cv, AssignmentEnum.Id.Value + " = ?", new String[] { consignmentId });
		wdb.close();
		return result;
	}

}

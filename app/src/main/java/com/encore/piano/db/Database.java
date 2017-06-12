package com.encore.piano.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.encore.piano.services.ServiceUtility;
import com.google.android.gms.maps.model.LatLng;
import com.encore.piano.enums.ConsignmentEnum;
import com.encore.piano.enums.PianoEnum;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.exceptions.AuthTokenExpiredException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.EmptyAuthTokenException;
import com.encore.piano.model.ConsignmentModel;
import com.encore.piano.model.GPSTrackingModel;
import com.encore.piano.model.GalleryModel;
import com.encore.piano.model.GpxTrackModel;
import com.encore.piano.model.PianoModel;
import com.encore.piano.model.LoginModel;
import com.encore.piano.model.MessageModel;
import com.encore.piano.model.MessageModel.MessageFolderEnum;

public class Database extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/com.encore.piano/databases/";
	private static final String NAME = "ecorepiano.db";
	private static final int VERSION = 1;

    private static final String TABLE_LOGINDATA = "LOGINDATA";
	private static final String TABLE_GALLERY = "GALLERY";
	private static final String TABLE_MESSAGES = "MESSAGES";
	private static final String TABLE_GPS_COORDINATES = "GPSCOORDINATES";
	private static final String TABLE_GPX_TRACKS = "GPXTRACKS";

	private static final String COLUMN_IMAGE_ID = "ImageId";
	private static final String COLUMN_IMAGE_PATH = "ImagePath";
	private static final String COLUMN_IMAGE_REFERENCE = "ImageReference";

	private static final String COLUMN_MESSAGE_ID = "ID";
	private static final String COLUMN_MESSAGE_SENDER = "Sender";
	private static final String COLUMN_MESSAGE_RECIPIENT = "Recipient";
	private static final String COLUMN_MESSAGE_SUBJECT = "Subject";
	private static final String COLUMN_MESSAGE_TEXT = "Text";
	private static final String COLUMN_MESSAGE_TIMESTAMP = "Timestamp";
	private static final String COLUMN_MESSAGE_SENT = "Sent";
	private static final String COLUMN_MESSAGE_FOLDER = "Folder";

	private static final String COLUMN_LOGINDATA_AUTHTOKEN = "AuthToken";
	private static final String COLUMN_LOGINDATA_AUTHTOKEN_EXPIRY = "AuthTokenExpiry";
	private static final String COLUMN_LOGINDATA_USERID = "USER_ID";
	private static final String COLUMN_LOGINDATA_USERNAME = "USERNAME";
	private static final String COLUMN_LOGINDATA_ACTIVE = "ACTIVE";
	private static final String COLUMN_LOGINDATA_VEHICLE_CODE = "VEHICLECODE";

	private static final String COLUMN_COORDINATE_LATITUDE = "Latitude";
	private static final String COLUMN_COORDINATE_LONGITUDE = "Longitude";
	private static final String COLUMN_COORDINATE_TIMESTAMP = "Timestamp";
	private static final String COLUMN_COORDINATE_SYNCED = "Synced";

	private static final String COLUMN_GPX_NAME = "Name";
	private static final String COLUMN_GPX_TRACK_ID = "GpxTrackId";
	private static final String COLUMN_GPX_LATITUDE = "Latitude";
	private static final String COLUMN_GPX_LONGITUDE = "Longitude";
	private static final String COLUMN_GPX_RUNSHEETID = "RunSheetID";
	private static final String COLUMN_GPX_ORDER = "GpxOrder";

	public Database(Context context) {
		super(context, NAME, null, VERSION);

	}

	public static Database DbInstance = null;

	public static synchronized Database getInstance(Context context)
	{
		if (DbInstance == null)
		{
			DbInstance = new Database(context.getApplicationContext());
		}
		return DbInstance;
	}

	public synchronized static SQLiteDatabase GetSqliteHelper(Context context)
	{
		return getInstance(context).getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

		String sql = "create table " + ConsignmentEnum.TableName.Value + "(" +
                ConsignmentEnum.Id.Value + " text primary key," +
                ConsignmentEnum.ConsignmentNumber.Value + " text ," +
                ConsignmentEnum.OrderType.Value + " text ," +
                ConsignmentEnum.StartWarehouseName.Value + " text ," +
                ConsignmentEnum.StartWarehouseAddress.Value + " text ," +
                ConsignmentEnum.VehicleCode.Value + " text ," +
                ConsignmentEnum.VehicleName.Value + " text ," +
                ConsignmentEnum.DriverCode.Value + " text ," +
                ConsignmentEnum.DriverName.Value + " text ," +
                ConsignmentEnum.OrderId.Value + " text ," +
                ConsignmentEnum.OrderNumber.Value + " text ," +
                ConsignmentEnum.OrderedAt.Value + " text ," +
                ConsignmentEnum.CallerName.Value + " text ," +
                ConsignmentEnum.CallerPhoneNumber.Value + " text ," +
                ConsignmentEnum.SpecialInstructions.Value + " text ," +
                ConsignmentEnum.PickupAddress.Value + " text ," +
                ConsignmentEnum.DeliveryAddress.Value + " text ," +
                ConsignmentEnum.CustomerCode.Value + " text ," +
                ConsignmentEnum.CustomerName.Value + " text ," +
                ConsignmentEnum.NumberOfItems.Value + " integer ," +
                ConsignmentEnum.createdAt.Value + " text ," +
                ConsignmentEnum.tripStaus.Value + " text ," +
                ConsignmentEnum.unread.Value + " integer ," +
                ConsignmentEnum.departureTime.Value + " text ," +
                ConsignmentEnum.arrivalTime.Value + " text ," +
                ConsignmentEnum.pickupLocation.Value + " text ," +
                ConsignmentEnum.receiverName.Value + " text ," +
                ConsignmentEnum.receiverSignaturePath.Value + " text ," +
                ConsignmentEnum.dateSigned.Value + " text ," +
                ConsignmentEnum.signed.Value + " integer ," +
                ConsignmentEnum.saved.Value + " integer ," +
                ConsignmentEnum.synced.Value + " integer );";

		String sqlitem = "create table " + PianoEnum.TableName.Value + "(" +
				PianoEnum.Id.Value + " text primary key," +
				PianoEnum.ConsignmentId.Value + " text," +
				PianoEnum.Type.Value + " text," +
				PianoEnum.Name.Value + " text," +
				PianoEnum.Color.Value + " text," +
				PianoEnum.Make.Value + " text," +
				PianoEnum.Model.Value + " text," +
				PianoEnum.SerialNumber.Value + " text," +
				PianoEnum.IsStairs.Value + " integer," +
				PianoEnum.IsBench.Value + " integer," +
				PianoEnum.IsBoxed.Value + " integer," +
				PianoEnum.createdAt.Value + " text," +
				PianoEnum.pianoStaus.Value + " text );";

		String sqlimg = "create table " + TABLE_GALLERY + "(" +
                PianoEnum.Id.Value + " text, " +
                PianoEnum.ConsignmentId.Value + " text, " +
				COLUMN_IMAGE_REFERENCE + " text primary key," +
				COLUMN_IMAGE_PATH + " text);";

		String sqlmsg = "create table " + TABLE_MESSAGES + "(" +
				COLUMN_MESSAGE_ID + " text primary key," +
				COLUMN_MESSAGE_RECIPIENT + " text," +
				COLUMN_MESSAGE_SENDER + " text," +
				COLUMN_MESSAGE_SENT + " integer," +
				COLUMN_MESSAGE_SUBJECT + " text," +
				COLUMN_MESSAGE_TEXT + " text," +
				COLUMN_MESSAGE_TIMESTAMP + " text," +
				COLUMN_MESSAGE_FOLDER + " text);";

		String sqllogin = "create table " + TABLE_LOGINDATA + "(" + BaseColumns._ID + " integer primary key autoincrement," +
				COLUMN_LOGINDATA_AUTHTOKEN + " text, " +
				COLUMN_LOGINDATA_AUTHTOKEN_EXPIRY + " text," +
				COLUMN_LOGINDATA_USERNAME + " text," +
				COLUMN_LOGINDATA_ACTIVE + " integer," +
				COLUMN_LOGINDATA_VEHICLE_CODE + " text," +
				COLUMN_LOGINDATA_USERID + " integer);";

		String sqlgps = "create table " + TABLE_GPS_COORDINATES + "(" + BaseColumns._ID + " integer primary key autoincrement," +
				COLUMN_COORDINATE_LATITUDE + " text, " +
				COLUMN_COORDINATE_LONGITUDE + " text," +
				COLUMN_COORDINATE_TIMESTAMP + " text," +
				COLUMN_COORDINATE_SYNCED + " integer);";

		String sqlgpxcoors = "create table " + TABLE_GPX_TRACKS + "(" + BaseColumns._ID + " integer primary key autoincrement," +
				COLUMN_GPX_LATITUDE + " text," +
				COLUMN_GPX_LONGITUDE + " text," +
				COLUMN_GPX_NAME + " text," +
				COLUMN_GPX_ORDER + " text," +
				COLUMN_GPX_RUNSHEETID + " text);";

		db.execSQL(sql);
		db.execSQL(sqlitem);
		db.execSQL(sqlimg);
		db.execSQL(sqlmsg);
		db.execSQL(sqllogin);
		db.execSQL(sqlgps);
		db.execSQL(sqlgpxcoors);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

    private static synchronized long WriteConsignment(Context context, ConsignmentModel consignment)
    {
        ContentValues cv = new ContentValues();
        cv.put(ConsignmentEnum.Id.Value, consignment.getId());
        cv.put(ConsignmentEnum.ConsignmentNumber.Value, consignment.getConsignmentNumber());
        cv.put(ConsignmentEnum.OrderType.Value, consignment.getOrderType());
        cv.put(ConsignmentEnum.StartWarehouseName.Value, consignment.getStartWarehouseName());
        cv.put(ConsignmentEnum.StartWarehouseAddress.Value, consignment.getStartWarehouseAddress());
        cv.put(ConsignmentEnum.VehicleCode.Value, consignment.getVehicleCode());
        cv.put(ConsignmentEnum.VehicleName.Value, consignment.getVehicleName());
        cv.put(ConsignmentEnum.DriverCode.Value, consignment.getDriverCode());
        cv.put(ConsignmentEnum.DriverName.Value, consignment.getDriverName());
        cv.put(ConsignmentEnum.OrderId.Value, consignment.getOrderId());
        cv.put(ConsignmentEnum.OrderNumber.Value, consignment.getOrderNumber());
        cv.put(ConsignmentEnum.OrderedAt.Value, consignment.getOrderedAt());
        cv.put(ConsignmentEnum.CallerName.Value, consignment.getCallerName());
        cv.put(ConsignmentEnum.CallerPhoneNumber.Value, consignment.getCallerPhoneNumber());
        cv.put(ConsignmentEnum.SpecialInstructions.Value, consignment.getSpecialInstructions());
        cv.put(ConsignmentEnum.PickupAddress.Value, consignment.getPickupAddress());
        cv.put(ConsignmentEnum.DeliveryAddress.Value, consignment.getDeliveryAddress());
        cv.put(ConsignmentEnum.CustomerCode.Value, consignment.getCustomerCode());
        cv.put(ConsignmentEnum.CustomerName.Value, consignment.getCustomerName());
        cv.put(ConsignmentEnum.NumberOfItems.Value, consignment.getNumberOfItems());

        SQLiteDatabase wdb = GetSqliteHelper(context);
        long result = wdb.insert(ConsignmentEnum.TableName.Value, null, cv);
        return result;
    }


    public static synchronized ArrayList<ConsignmentModel> GetConsignments(Context context, boolean notCompleted, boolean notSynced, String consignmentId)
    {
        SQLiteDatabase rdb = GetSqliteHelper(context);
        ArrayList<ConsignmentModel> listmodel = new ArrayList<ConsignmentModel>();
        String cols[] = new String[] {
                ConsignmentEnum.Id.Value,
                ConsignmentEnum.ConsignmentNumber.Value,
                ConsignmentEnum.OrderType.Value,
                ConsignmentEnum.StartWarehouseName.Value,
                ConsignmentEnum.StartWarehouseAddress.Value,
                ConsignmentEnum.VehicleCode.Value,
                ConsignmentEnum.VehicleName.Value,
                ConsignmentEnum.DriverCode.Value,
                ConsignmentEnum.DriverName.Value,
                ConsignmentEnum.OrderId.Value,
                ConsignmentEnum.OrderNumber.Value,
                ConsignmentEnum.OrderedAt.Value,
                ConsignmentEnum.CallerName.Value,
                ConsignmentEnum.CallerPhoneNumber.Value,
                ConsignmentEnum.SpecialInstructions.Value,
                ConsignmentEnum.PickupAddress.Value,
                ConsignmentEnum.DeliveryAddress.Value,
                ConsignmentEnum.CustomerCode.Value,
                ConsignmentEnum.CustomerName.Value,
                ConsignmentEnum.NumberOfItems.Value,
                ConsignmentEnum.createdAt.Value,
                ConsignmentEnum.tripStaus.Value,
                ConsignmentEnum.unread.Value,
                ConsignmentEnum.departureTime.Value,
                ConsignmentEnum.arrivalTime.Value,
                ConsignmentEnum.pickupLocation.Value,
                ConsignmentEnum.receiverName.Value,
                ConsignmentEnum.receiverSignaturePath.Value,
                ConsignmentEnum.dateSigned.Value,
                ConsignmentEnum.signed.Value,
                ConsignmentEnum.saved.Value,
                ConsignmentEnum.synced.Value,
        };

        Cursor results = null;
        if (notCompleted)
            results = rdb.query(ConsignmentEnum.TableName.Value, cols, ConsignmentEnum.tripStaus.Value + " != ? ", new String[] { TripStatusEnum.Completed.Value }, null, null, ConsignmentEnum.OrderNumber + " ASC");
        else if(notSynced)
            results = rdb.query(ConsignmentEnum.TableName.Value, cols, ConsignmentEnum.saved.Value + " = ? AND " + ConsignmentEnum.synced.Value + " = ? ", new String[] { String.valueOf(1), "0" }, null, null, ConsignmentEnum.OrderNumber  + " ASC");
        else if(consignmentId != "")
            results = rdb.query(ConsignmentEnum.TableName.Value, cols, ConsignmentEnum.Id.Value + " = ? ", new String[] { consignmentId }, null, null, ConsignmentEnum.OrderNumber  + " ASC");
        else
            results = rdb.query(ConsignmentEnum.TableName.Value, cols, null, null, null, null, ConsignmentEnum.OrderNumber  + " ASC");

        while (results.moveToNext())
        {
            ConsignmentModel model = new ConsignmentModel();
            model.setId(results.getString(0));
            model.setConsignmentNumber(results.getString(1));
            model.setOrderType(results.getString(2));
            model.setStartWarehouseName(results.getString(3));
            model.setStartWarehouseAddress(results.getString(4));
            model.setVehicleCode(results.getString(5));
            model.setVehicleName(results.getString(6));
            model.setDriverCode(results.getString(7));
            model.setDriverName(results.getString(8));
            model.setOrderId(results.getString(9));
            model.setOrderNumber(results.getString(10));
            model.setOrderedAt(results.getString(11));
            model.setCallerName(results.getString(12));
            model.setCallerPhoneNumber(results.getString(13));
            model.setSpecialInstructions(results.getString(14));
            model.setPickupAddress(results.getString(15));
            model.setDeliveryAddress(results.getString(16));
            model.setCustomerCode(results.getString(17));
            model.setCustomerName(results.getString(18));
            model.setNumberOfItems(results.getInt(19));

            model.setCreatedAt(results.getString(20));
            model.setTripStatus(results.getString(21));
            model.setUnread(results.getInt(22) == 1);
            model.setDepartureTime(results.getString(23));
            model.setArrivalTime(results.getString(24));
            model.setPickupLocation(new LatLng(results.getDouble(25),results.getDouble(25)));
            model.setReceiverName(results.getString(26));
            model.setReceiverSignaturePath(results.getString(27));
            model.setDateSigned(results.getString(28));
            model.setSigned(results.getInt(29) == 1);
            model.setSaved(results.getInt(30) == 1);
            model.setSynced(results.getInt(31) == 1);
            listmodel.add(model);
        }

        results.close();
        rdb.close();
        return listmodel;
    }

    public static synchronized int WriteConsignments(Context context, ArrayList<ConsignmentModel> consignments)
	{
		int imported = 0;
		try
		{
			for (int i = 0; i < consignments.size(); i++)
			{
				ArrayList<ConsignmentModel> alreadyExistsConsigments = GetConsignments(context, false, false, consignments.get(i).getId());
				if (alreadyExistsConsigments.size() != 0)
				{
					// if consignmentService is not started yet
					if (!alreadyExistsConsigments.get(0).getTripStatus().equals(TripStatusEnum.NotStarted.Value)) {
                        // delete
                        SQLiteDatabase wdb = GetSqliteHelper(context);
                        wdb.delete(ConsignmentEnum.TableName.Value, ConsignmentEnum.Id.Value + " = '" + alreadyExistsConsigments.get(0).getId() + "'", null);
                        wdb.close();
                        // delete items
                        SQLiteDatabase wdb1 = GetSqliteHelper(context);
                        wdb1.delete(PianoEnum.TableName.Value, PianoEnum.ConsignmentId.Value + " = '" + alreadyExistsConsigments.get(0).getId() + "'", null);
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

	public static synchronized void SetConsignmentSynced(Context context, String consignmentId) throws DatabaseUpdateException
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(ConsignmentEnum.synced.Value, 1);
		if (wdb.update(ConsignmentEnum.TableName.Value, cv, ConsignmentEnum.Id.Value + " = ?", new String[] { consignmentId }) != 1)
			throw new DatabaseUpdateException();

		wdb.close();
	}

	public static synchronized boolean AreAllConsignmentsSynced(Context context)
	{
		SQLiteDatabase rdb = GetSqliteHelper(context);

		Cursor results = rdb.query(ConsignmentEnum.TableName.Value, null, ConsignmentEnum.synced + " = 0", null, null, null, null);

		int count = results.getCount();

		results.close();
		rdb.close();

		return count == 0 ? true : false;
	}

	public static synchronized void DeleteConsignments(Context context, ArrayList<ConsignmentModel> cons)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		for (ConsignmentModel consignmentModel : cons)
		{
			wdb.delete(ConsignmentEnum.TableName.Value, ConsignmentEnum.Id.Value + " = '" + consignmentModel.getId() + "'", null);
		}
		wdb.close();
	}

	/*
	 * 	ITEMS 
	 */

    public static synchronized int WriteItems(Context context, ArrayList<PianoModel> items)
    {
        SQLiteDatabase wdb = GetSqliteHelper(context);
        int numberOfItems = 0;
        try
        {
            for (int i = 0; i < items.size(); i++)
            {
                if (WriteItem(wdb, items.get(i)) != -1)
                    numberOfItems++;
            }
        } catch (Exception ex)
        {
            numberOfItems = -1;
        }
        finally
        {
        }

        wdb.close();
        return numberOfItems;

    }

    public static synchronized int WriteItems(Context context, String consignmentId, ArrayList<PianoModel> items)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		int numberOfItems = 0;
		try
		{
            ArrayList<ConsignmentModel> alreadyExistsConsigments = GetConsignments(context, false, false, consignmentId);
            // already exists
            if (alreadyExistsConsigments.size() != 0)
            {
                // if consignmentService is not started yet
                if (!alreadyExistsConsigments.get(0).getTripStatus().equals(TripStatusEnum.NotStarted.Value)) {
                    for (int i = 0; i < items.size(); i++)
                    {
                        if (WriteItem(wdb, items.get(i)) != -1)
                            numberOfItems++;
                    }
                }
            }
		} catch (Exception ex)
		{
			numberOfItems = -1;
		}
		finally
		{
		}

		wdb.close();
		return numberOfItems;

	}

	private static synchronized long WriteItem(SQLiteDatabase wdb, PianoModel item)
	{
		ContentValues cv = new ContentValues();
		cv.put(PianoEnum.Id.Value, item.getId());
		cv.put(PianoEnum.ConsignmentId.Value, item.getConsignmentId());
		cv.put(PianoEnum.Type.Value, item.getType());
		cv.put(PianoEnum.Name.Value, item.getName());
		cv.put(PianoEnum.Color.Value, item.getColor());
		cv.put(PianoEnum.Make.Value, item.getMake());
		cv.put(PianoEnum.Model.Value, item.getModel());
		cv.put(PianoEnum.SerialNumber.Value, item.getSerialNumber());
		cv.put(PianoEnum.IsStairs.Value, item.isStairs() ? 1 : 0);
		cv.put(PianoEnum.IsBench.Value, item.isBench() ? 1 : 0);
		cv.put(PianoEnum.IsBoxed.Value, item.isBoxed() ? 1 : 0);
        cv.put(PianoEnum.createdAt.Value, ServiceUtility.GetGMTTime());
		return wdb.insert(PianoEnum.TableName.Value, null, cv);
	}

	public static synchronized ArrayList<PianoModel> GetItems(Context context, String consignmentId)
	{
		SQLiteDatabase rdb = GetSqliteHelper(context);
		ArrayList<PianoModel> listmodel = new ArrayList<PianoModel>();
		String cols[] = new String[] {
                PianoEnum.Id.Value,
                PianoEnum.ConsignmentId.Value,
                PianoEnum.Type.Value,
                PianoEnum.Name.Value,
                PianoEnum.Color.Value,
                PianoEnum.Make.Value,
                PianoEnum.Model.Value,
                PianoEnum.SerialNumber.Value,
                PianoEnum.IsStairs.Value,
                PianoEnum.IsBench.Value,
                PianoEnum.IsBoxed.Value,
                PianoEnum.createdAt.Value,
                PianoEnum.pianoStaus.Value,
		};

		Cursor results = rdb.query(PianoEnum.TableName.Value, cols, PianoEnum.ConsignmentId.Value + "= ?", new String[] { consignmentId }, null, null, null);

		while (results.moveToNext())
		{
            PianoModel model = new PianoModel();
			model.setId(results.getString(0));
			model.setConsignmentId(results.getString(1));
			model.setType(results.getString(2));
			model.setName(results.getString(3));
			model.setColor(results.getString(4));
			model.setMake(results.getString(5));
			model.setModel(results.getString(6));
			model.setSerialNumber(results.getString(7));
			model.setStairs(results.getInt(8) == 1);
			model.setBench(results.getInt(9) == 1);
			model.setBoxed(results.getInt(10) == 1);
			model.setCreatedAt(results.getString(11));
            model.setPianoStaus(results.getString(12));
			listmodel.add(model);
		}

		rdb.close();
		results.close();
		return listmodel;
	}

	public static synchronized void DeleteConsignmentItems(Context context, String consignmentId)
	{

		SQLiteDatabase wdb = GetSqliteHelper(context);
		wdb.delete(PianoEnum.TableName.Value, PianoEnum.ConsignmentId.Value + " = '" + consignmentId + "'", null);
		wdb.close();

	}

	public static synchronized void DeleteConsignmentItems(SQLiteDatabase wdb, String consignmentId)
	{
		wdb.delete(PianoEnum.TableName.Value, PianoEnum.ConsignmentId.Value + " = '" + consignmentId + "'", null);

	}

	public static synchronized int WriteCustomerSignature(Context context, String customerName, String customerSinaturePath, String consignmentId)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		int returnValue = 1;
		try
		{
			wdb.beginTransaction();
			ContentValues cv = new ContentValues();
			cv.put(ConsignmentEnum.signed.Value, 1);
			cv.put(ConsignmentEnum.receiverName.Value, customerName);
			cv.put(ConsignmentEnum.receiverSignaturePath.Value, customerSinaturePath);
			cv.put(ConsignmentEnum.dateSigned.Value, ServiceUtility.GetGMTTime());
			if ((returnValue = wdb.update(ConsignmentEnum.TableName.Value, cv, ConsignmentEnum.Id.Value + " = ?", new String[] { consignmentId })) != 1)
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

	public static synchronized int SaveConsignment(Context context, String consignmentId, String tripStatus)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(ConsignmentEnum.tripStaus.Value, tripStatus);
		cv.put(ConsignmentEnum.saved.Value, 1);
		int result = wdb.update(ConsignmentEnum.TableName.Value, cv, ConsignmentEnum.Id.Value + " = ?", new String[] { consignmentId });

		wdb.close();

		return result;
	}

	public static synchronized int SetTripStatus(Context context, String consignmentId, String tripStatus)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		ContentValues cv = new ContentValues();
        cv.put(ConsignmentEnum.tripStaus.Value, tripStatus);
		int result = wdb.update(ConsignmentEnum.TableName.Value, cv, ConsignmentEnum.Id.Value + " = ?", new String[] { consignmentId });

		wdb.close();

		return result;
	}

    public static synchronized int SaveArrivalTime(Context context, String consignmentId, String tripStatus, String arrival)
    {
        SQLiteDatabase wdb = GetSqliteHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(ConsignmentEnum.tripStaus.Value, tripStatus);
        cv.put(ConsignmentEnum.arrivalTime.Value, arrival);
        int result = wdb.update(ConsignmentEnum.TableName.Value, cv, ConsignmentEnum.Id.Value + " = ?", new String[] { consignmentId });

        wdb.close();

        return result;
    }

	public static synchronized int SaveDepartureTime(Context context, String consignmentId, String dept)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		ContentValues cv = new ContentValues();
        cv.put(ConsignmentEnum.saved.Value, 1);
		cv.put(ConsignmentEnum.departureTime.Value, dept);
		int result = wdb.update(ConsignmentEnum.TableName.Value, cv, ConsignmentEnum.Id.Value + " = ?", new String[] { consignmentId });

		wdb.close();

		return result;
	}

	public static synchronized long WriteImageData(Context context, String consignmentId, String imagePath, String imageRef)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(ConsignmentEnum.Id.Value, consignmentId);
		cv.put(COLUMN_IMAGE_REFERENCE, imageRef);
		cv.put(COLUMN_IMAGE_PATH, imagePath);
		long result = wdb.insertOrThrow(TABLE_GALLERY, null, cv);

		wdb.close();

		return result;
	}

	public static synchronized ArrayList<GalleryModel> GetImagesForConsignment(Context context, String consignmentId)
	{

		SQLiteDatabase rdb = GetSqliteHelper(context);
		ArrayList<GalleryModel> models = new ArrayList<GalleryModel>();

		String cols[] = { PianoEnum.ConsignmentId.Value, COLUMN_IMAGE_PATH, COLUMN_IMAGE_REFERENCE };
		Cursor results = rdb.query(TABLE_GALLERY, cols, PianoEnum.ConsignmentId.Value + " = ?", new String[] { consignmentId }, null, null, null);

		while (results.moveToNext())
		{
			GalleryModel model = new GalleryModel();
			model.setConsignmentId(results.getString(0));
			model.setImagePath(results.getString(1));
			model.setImageReference(results.getString(2));
			models.add(model);
		}

		results.close();
		rdb.close();

		return models;
	}

	public static synchronized ArrayList<GalleryModel> GetImagesForConsignments(Context context, ArrayList<String> consignmentIds)
	{
		ArrayList<GalleryModel> galleryModel = new ArrayList<GalleryModel>();

		for (String id : consignmentIds)
		{
			galleryModel.addAll(GetImagesForConsignment(context, id));
		}

		return galleryModel;
	}

	public static synchronized int GetImageCountForConsignment(Context context, String consignmentId)
	{
		SQLiteDatabase rdb = GetSqliteHelper(context);
		Cursor results = rdb.query(TABLE_GALLERY, null, PianoEnum.ConsignmentId.Value + " = ?", new String[] { consignmentId }, null, null, null);
		int count = results.getCount();

		rdb.close();

		return count;
	}

	public static synchronized void DeleteImagesForConsignments(Context context, String consId) throws IOException
	{

		String root = ServiceUtility.ImagesRootFolder + "/" + String.valueOf(consId);

		File dir = new File(root);
		if (dir.exists())
			FileUtils.deleteDirectory(dir);

		ArrayList<GalleryModel> images = new ArrayList<GalleryModel>();

		SQLiteDatabase wdb = GetSqliteHelper(context);
		String cols[] = { COLUMN_IMAGE_PATH, COLUMN_IMAGE_REFERENCE };

		Cursor results = wdb.query(TABLE_GALLERY, cols, PianoEnum.ConsignmentId.Value + " = '" + consId + "'", null, null, null, null);

		while (results.moveToNext())
		{
			String path = results.getString(0);
			if (!path.contains("MULTI"))
			{

				GalleryModel model = new GalleryModel();
				model.setImageReference(results.getString(1));
				model.setImagePath(path);
				images.add(model);
			}
		}

		results.close();

		for (GalleryModel model : images)
		{
			wdb.delete(TABLE_GALLERY, COLUMN_IMAGE_REFERENCE + " = '" + model.getImageReference() + "'", null);
		}

		wdb.close();

	}

	public static synchronized void DeleteImageFromConsignments(Context context, GalleryModel galleryItem) throws IOException
	{

		String path = galleryItem.getImagePath().substring(5);

		File f = new File(path);
		if (f.exists())
			f.delete();

		SQLiteDatabase wdb = GetSqliteHelper(context);

		wdb.delete(TABLE_GALLERY, COLUMN_IMAGE_REFERENCE + " = '" + galleryItem.getImageReference() + "'", null);

		wdb.close();

	}

	public synchronized static int WriteMessages(Context context, ArrayList<MessageModel> messages)
	{
		int numberOfMessages = 0;
		try
		{
			for (int i = 0; i < messages.size(); i++)
				if (WriteMessage(context, messages.get(i)) != -1)
					numberOfMessages++;
		} catch (Exception ex)
		{
		}

		return numberOfMessages;
	}

	public synchronized static long WriteMessage(Context context, MessageModel message)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		long returnValue = -1;
		Cursor results = wdb.query(TABLE_MESSAGES, null, COLUMN_MESSAGE_ID + " = ? ", new String[] { String.valueOf(message.getID()) }, null, null, null);
		//rdb.close();

		//SQLiteDatabase wdb = GetSqliteHelper(context);
		if (!results.moveToFirst())
		{

			ContentValues cv = new ContentValues();
			cv.put(COLUMN_MESSAGE_ID, message.getID());
			cv.put(COLUMN_MESSAGE_RECIPIENT, message.getRecipient());
			cv.put(COLUMN_MESSAGE_SENDER, message.getSender());
			cv.put(COLUMN_MESSAGE_SUBJECT, message.getSubject());
			cv.put(COLUMN_MESSAGE_TEXT, message.getMessageText());
			cv.put(COLUMN_MESSAGE_TIMESTAMP, message.getTimestamp());
			cv.put(COLUMN_MESSAGE_FOLDER, message.getFolder());

			long no = wdb.insert(TABLE_MESSAGES, null, cv);

			returnValue = no;
		}

		results.close();

		wdb.close();

		return returnValue;

	}

	public static synchronized MessageModel GetMessageById(Context context, String messageId)
	{

		SQLiteDatabase wdb = GetSqliteHelper(context);
		MessageModel model = new MessageModel();

		String cols[] = {
				COLUMN_MESSAGE_ID,
				COLUMN_MESSAGE_RECIPIENT,
				COLUMN_MESSAGE_SENDER,
				COLUMN_MESSAGE_SUBJECT,
				COLUMN_MESSAGE_TEXT,
				COLUMN_MESSAGE_TIMESTAMP,
				COLUMN_MESSAGE_FOLDER
		};

		Cursor results = wdb.query(TABLE_MESSAGES, cols, COLUMN_MESSAGE_ID + " = ?", new String[] { String.valueOf(messageId) }, null, null, null);

		if (results.moveToFirst())
		{
			model.setID(results.getString(0));
			model.setRecipient(results.getString(1));
			model.setSender(results.getString(2));
			model.setSubject(results.getString(3));
			model.setMessageText(results.getString(4));
			model.setTimestamp(results.getString(5));
			model.setFolder(results.getString(6));
		}

		results.close();
		wdb.close();
		return model;
	}

	public static synchronized ArrayList<MessageModel> GetMyConversations(Context context, String UserName)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);

		ArrayList<MessageModel> messages = new ArrayList<MessageModel>();

		String cols[] = {
				COLUMN_MESSAGE_ID,
				COLUMN_MESSAGE_RECIPIENT,
				COLUMN_MESSAGE_SENDER,
				COLUMN_MESSAGE_SUBJECT,
				COLUMN_MESSAGE_TEXT,
				COLUMN_MESSAGE_TIMESTAMP
		};

		String TimeStampArranged = " substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,7,4)||" +
				" substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,4,2)||" +
				" substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,1,2)||" +
				" substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,12,2)||" +
				" substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,15,2)";

		String qry = "SELECT COUNT(ID),USER, SUBJECT, MAX(" + TimeStampArranged + ") FROM "
				+ " (SELECT ID, CASE WHEN SENDER = ? THEN RECIPIENT ELSE SENDER END AS 'USER', SUBJECT, TIMESTAMP "
				+ " FROM MESSAGES WHERE SENDER = ? OR RECIPIENT = ? ) "
				+ " WHERE USER != ? "
				+ " GROUP BY USER ORDER BY " + TimeStampArranged + " DESC ";

		Cursor results = wdb.rawQuery(qry, new String[] { UserName, UserName, UserName, UserName });

		while (results.moveToNext())
		{
			MessageModel model = new MessageModel();

			model.setID(results.getString(0));
			model.setSender(results.getString(1));
			model.setSubject(results.getString(2));
			model.setTimestamp(results.getString(3));

			messages.add(model);
		}

		results.close();
		wdb.close();
		return messages;
	}

	public static synchronized ArrayList<MessageModel> GetAllMessagesByUser(Context context, String UserId)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);

		ArrayList<MessageModel> messages = new ArrayList<MessageModel>();

		String cols[] = {
				COLUMN_MESSAGE_ID,
				COLUMN_MESSAGE_RECIPIENT,
				COLUMN_MESSAGE_SENDER,
				COLUMN_MESSAGE_SUBJECT,
				COLUMN_MESSAGE_TEXT,
				COLUMN_MESSAGE_TIMESTAMP
		};

		String TimeStampArranged = " substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,7,4)||" +
				" substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,4,2)||" +
				" substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,1,2)||" +
				" substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,12,2)||" +
				" substr(" + COLUMN_MESSAGE_TIMESTAMP + " ,15,2)";

		Cursor results = wdb.query(
				TABLE_MESSAGES,
				cols,
				COLUMN_MESSAGE_SENDER + " = ? OR " + COLUMN_MESSAGE_RECIPIENT + " = ? ",
				new String[] { UserId, UserId },
				null, null, TimeStampArranged + " DESC");

		while (results.moveToNext())
		{
			MessageModel model = new MessageModel();

			model.setID(results.getString(0));
			model.setRecipient(results.getString(1));
			model.setSender(results.getString(2));
			model.setSubject(results.getString(3));
			model.setMessageText(results.getString(4));
			model.setTimestamp(results.getString(5));

			messages.add(model);
		}

		results.close();
		wdb.close();
		return messages;
	}

	public static synchronized ArrayList<MessageModel> GetReceivedMessages(Context context)
	{
		SQLiteDatabase rdb = GetSqliteHelper(context);

		ArrayList<MessageModel> messages = new ArrayList<MessageModel>();

		String cols[] = {
				COLUMN_MESSAGE_ID,
				COLUMN_MESSAGE_RECIPIENT,
				COLUMN_MESSAGE_SENDER,
				COLUMN_MESSAGE_SUBJECT,
				COLUMN_MESSAGE_TEXT,
				COLUMN_MESSAGE_TIMESTAMP,
				COLUMN_MESSAGE_FOLDER
		};

		Cursor results = rdb.query(TABLE_MESSAGES, cols, COLUMN_MESSAGE_FOLDER + " = ?", new String[] { MessageFolderEnum.Inbox.Value }, null, null, COLUMN_MESSAGE_TIMESTAMP + " DESC");
		//Cursor results = rdb.query(TABLE_MESSAGES, cols, null, null, null, null, COLUMN_MESSAGE_ID + " DESC");

		while (results.moveToNext())
		{
			MessageModel model = new MessageModel();

			model.setID(results.getString(0));
			model.setRecipient(results.getString(1));
			model.setSender(results.getString(2));
			model.setSubject(results.getString(3));
			model.setMessageText(results.getString(4));
			model.setTimestamp(results.getString(5));
			model.setFolder(results.getString(6));

			messages.add(model);
		}

		results.close();
		rdb.close();
		return messages;
	}

	public static synchronized ArrayList<MessageModel> GetUnsentMessages(Context context)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);

		ArrayList<MessageModel> messages = new ArrayList<MessageModel>();

		String cols[] = {
				COLUMN_MESSAGE_ID,
				COLUMN_MESSAGE_RECIPIENT,
				COLUMN_MESSAGE_SENDER,
				COLUMN_MESSAGE_SUBJECT,
				COLUMN_MESSAGE_TEXT,
				COLUMN_MESSAGE_TIMESTAMP
		};

		Cursor results = wdb.query(
				TABLE_MESSAGES,
				cols,
				COLUMN_MESSAGE_FOLDER + " = ?",
				new String[] { MessageFolderEnum.Outbox.Value },
				null, null, COLUMN_MESSAGE_TIMESTAMP + " DESC");

		while (results.moveToNext())
		{
			MessageModel model = new MessageModel();

			model.setID(results.getString(0));
			model.setRecipient(results.getString(1));
			model.setSender(results.getString(2));
			model.setSubject(results.getString(3));
			model.setMessageText(results.getString(4));
			model.setTimestamp(results.getString(5));

			messages.add(model);
		}

		results.close();
		wdb.close();
		return messages;
	}

	public static synchronized ArrayList<MessageModel> GetSentMessages(Context context)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);

		ArrayList<MessageModel> messages = new ArrayList<MessageModel>();

		String cols[] = {
				COLUMN_MESSAGE_ID,
				COLUMN_MESSAGE_RECIPIENT,
				COLUMN_MESSAGE_SENDER,
				COLUMN_MESSAGE_SUBJECT,
				COLUMN_MESSAGE_TEXT,
				COLUMN_MESSAGE_TIMESTAMP
		};

		Cursor results = wdb.query(
				TABLE_MESSAGES,
				cols,
				COLUMN_MESSAGE_FOLDER + " = ?",
				new String[] { MessageFolderEnum.Sent.Value },
				null, null, COLUMN_MESSAGE_TIMESTAMP + " DESC");

		while (results.moveToNext())
		{
			MessageModel model = new MessageModel();

			model.setID(results.getString(0));
			model.setRecipient(results.getString(1));
			model.setSender(results.getString(2));
			model.setSubject(results.getString(3));
			model.setMessageText(results.getString(4));
			model.setTimestamp(results.getString(5));

			messages.add(model);
		}

		results.close();
		wdb.close();
		return messages;
	}

	//	public static synchronized void MarkMessageAsRead(Context context, int messageId) 
	//	{
	//		SQLiteDatabase wdb = GetSqliteHelper(context);
	//		ContentValues cv = new ContentValues();
	//		cv.put(COLUMN_MESSAGE_READ, 1);
	//		wdb.update(TABLE_MESSAGES, cv, COLUMN_MESSAGE_ID + " = ?", new String[]{String.valueOf(messageId)});	
	//		wdb.close();
	//	}
	//	
	public static synchronized void MarkMessageAsSent(Context context, String messageId)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_MESSAGE_SENT, 1);
		wdb.update(TABLE_MESSAGES, cv, COLUMN_MESSAGE_FOLDER + " = ?", new String[] { MessageFolderEnum.Sent.Value });
		wdb.close();
	}

	public static LoginModel GetLoginData(Context context) throws EmptyAuthTokenException, AuthTokenExpiredException
	{
		SQLiteDatabase rdb = GetSqliteHelper(context);
		String cols[] = { COLUMN_LOGINDATA_AUTHTOKEN_EXPIRY, COLUMN_LOGINDATA_AUTHTOKEN, COLUMN_LOGINDATA_USERID, COLUMN_LOGINDATA_USERNAME, COLUMN_LOGINDATA_VEHICLE_CODE };
		Cursor results = rdb.query(TABLE_LOGINDATA, cols, COLUMN_LOGINDATA_ACTIVE + " = ?", new String[] { String.valueOf(1) }, null, null, "_id DESC");

		if (results.moveToFirst())
		{
			String authToken = results.getString(1);

			if (authToken.equals(""))
				throw new EmptyAuthTokenException();

			long expiryTime = Long.parseLong(results.getString(0));

			if (expiryTime > ServiceUtility.GetMilisTimeToLong())
			{
				LoginModel model = new LoginModel();
				model.setAuthToken(authToken);
				model.setUserId(results.getInt(2));
				model.setUserName(results.getString(3));

				results.close();
				rdb.close();
				return model;
			}
			else
			{
				results.close();
				rdb.close();
				throw new AuthTokenExpiredException();
			}
		}
		else
		{
			results.close();
			rdb.close();
			throw new NullPointerException();
		}

	}

	public static synchronized long WriteLoginData(Context context, LoginModel loginModel)
	{

		SQLiteDatabase wdb = GetSqliteHelper(context);

		ContentValues cv = new ContentValues();
		cv.put(COLUMN_LOGINDATA_AUTHTOKEN, loginModel.getAuthToken());
		cv.put(COLUMN_LOGINDATA_AUTHTOKEN_EXPIRY, String.valueOf(loginModel.getTime()));
		cv.put(COLUMN_LOGINDATA_USERID, loginModel.getUserId());
		cv.put(COLUMN_LOGINDATA_USERNAME, loginModel.getUserName());
		//cv.put(COLUMN_LOGINDATA_ACTIVE, 0);
		cv.put(COLUMN_LOGINDATA_ACTIVE, 1);
		cv.put(COLUMN_LOGINDATA_VEHICLE_CODE, loginModel.getFCMToken());
		long no = wdb.insert(TABLE_LOGINDATA, null, cv);
		wdb.close();
		return no;
	}

	public static synchronized void ActivateLoginData(Context context) throws EmptyAuthTokenException
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);

		String query = "SELECT MAX(_id) FROM " + TABLE_LOGINDATA;
		Cursor results = wdb.rawQuery(query, null);

		int id = -1;
		if (results.moveToFirst())
		{
			id = results.getInt(0);
		}

		if (id == -1)
		{
			results.close();
			wdb.close();
			throw new EmptyAuthTokenException();
		}

		results.close();

		ContentValues cv = new ContentValues();
		cv.put(COLUMN_LOGINDATA_ACTIVE, 1);
		wdb.update(TABLE_LOGINDATA, cv, "_id = ?", new String[] { String.valueOf(id) });

		wdb.close();
	}

	public static synchronized void LogOff(Context context)
	{

		SQLiteDatabase wdb = GetSqliteHelper(context);

		ContentValues cv = new ContentValues();
		cv.put(COLUMN_LOGINDATA_ACTIVE, 0);
		wdb.update(TABLE_LOGINDATA, cv, COLUMN_LOGINDATA_ACTIVE + " = ?", new String[] { String.valueOf(1) });
		wdb.close();
	}

	public static synchronized void SaveGpsCoordinates(Context context, GPSTrackingModel model)
	{
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_COORDINATE_LATITUDE, model.getLatitude());
		cv.put(COLUMN_COORDINATE_LONGITUDE, model.getLongitude());
		cv.put(COLUMN_COORDINATE_TIMESTAMP, ServiceUtility.GetMilisTimeToString());
		cv.put(COLUMN_COORDINATE_SYNCED, false);

		SQLiteDatabase wdb = GetSqliteHelper(context);
		wdb.insert(TABLE_GPS_COORDINATES, null, cv);

		wdb.close();
	}

	public static synchronized void MarkGpsCoordinateModelsAsSynced(Context context, ArrayList<Integer> gPSCoordinatesModelIds)
	{

		for (Integer id : gPSCoordinatesModelIds)
			MarkGpsCoordinatesAsSynced(context, id);

	}

	private static synchronized void MarkGpsCoordinatesAsSynced(Context context, int gpsModelId)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_COORDINATE_SYNCED, true);
		wdb.update(TABLE_GPS_COORDINATES, cv, "_id = ?", new String[] { String.valueOf(gpsModelId) });
		wdb.close();
	}

	public static synchronized ArrayList<GPSTrackingModel> GetGPSCoordinates(Context context, boolean synced)
	{

		SQLiteDatabase rdb = GetSqliteHelper(context);
		String cols[] = { COLUMN_COORDINATE_LATITUDE, COLUMN_COORDINATE_LONGITUDE, COLUMN_COORDINATE_TIMESTAMP, COLUMN_COORDINATE_SYNCED, "_id" };
		Cursor results;

		if (synced)
			results = rdb.query(TABLE_GPS_COORDINATES, cols, COLUMN_COORDINATE_SYNCED + " = ?", new String[] { String.valueOf(synced) }, null, null, null);
		else
			results = rdb.query(TABLE_GPS_COORDINATES, cols, "_id > ?", new String[] { String.valueOf(0) }, null, null, null);

		ArrayList<GPSTrackingModel> models = new ArrayList<GPSTrackingModel>();

		while (results.moveToNext())
		{
			GPSTrackingModel model = new GPSTrackingModel();

			model.setLatitude(results.getString(0));
			model.setLongitude(results.getString(1));
			model.setTimestamp(results.getString(2));
			model.setSynced(results.getInt(3));
			model.setId(results.getInt(4));

			models.add(model);
		}

		results.close();
		rdb.close();

		return models;

	}

	public static synchronized GPSTrackingModel GetLastRecordedCoordinate(Context context)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);

		GPSTrackingModel model = null;
		String cols[] = { COLUMN_COORDINATE_LATITUDE, COLUMN_COORDINATE_LONGITUDE };

		Cursor results = wdb.query(TABLE_GPS_COORDINATES, cols, "_id > 0", null, null, null, "_id DESC");

		if (results.moveToFirst())
		{
			model = new GPSTrackingModel();
			model.setLatitude(results.getString(0));
			model.setLongitude(results.getString(1));
		}

		results.close();
		wdb.close();

		return model;

	}

	public static synchronized boolean GpxTracksExist(Context context)
	{

		SQLiteDatabase wdb = GetSqliteHelper(context);
		Cursor results = wdb.query(TABLE_GPX_TRACKS, null, null, null, null, null, null);

		boolean exist = results.getCount() > 0 ? true : false;

		results.close();
		wdb.close();

		return exist;

	}

	public static synchronized void WriteGpxTracks(Context context, ArrayList<GpxTrackModel> gpxTracks)
	{

		SQLiteDatabase wdb = GetSqliteHelper(context);

		for (GpxTrackModel gpxTrackModel : gpxTracks)
		{

			ContentValues cv = new ContentValues();
			cv.put("_id", gpxTrackModel.getOrder());
			cv.put(COLUMN_GPX_NAME, gpxTrackModel.getName());
			cv.put(COLUMN_GPX_RUNSHEETID, gpxTrackModel.getRunSheetID());
			cv.put(COLUMN_GPX_LATITUDE, gpxTrackModel.getLatitude());
			cv.put(COLUMN_GPX_LONGITUDE, gpxTrackModel.getLongitude());
			cv.put(COLUMN_GPX_ORDER, gpxTrackModel.getOrder());
			wdb.insert(TABLE_GPX_TRACKS, null, cv);

		}

		wdb.close();

	}

	public static synchronized int CountGpxTracks(Context context, String runSheetID)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);

		Cursor results = wdb.query(TABLE_GPX_TRACKS, null, COLUMN_GPX_RUNSHEETID + " = ?", new String[] { runSheetID }, null, null, null);

		int count = results.getCount();
		wdb.close();
		return count;
	}

	public static synchronized ArrayList<GpxTrackModel> GetGpxTracks(SQLiteDatabase wdb)
	{

		ArrayList<GpxTrackModel> tracks = new ArrayList<GpxTrackModel>();

		String cols[] = {
				"_id",
				COLUMN_GPX_NAME,
				COLUMN_GPX_RUNSHEETID,
				COLUMN_GPX_LATITUDE,
				COLUMN_GPX_LONGITUDE,
				COLUMN_GPX_ORDER
		};

		Cursor trackResults = wdb.query(TABLE_GPX_TRACKS, cols, "_id > 0", null, null, null, null);

		while (trackResults.moveToNext())
		{

			GpxTrackModel model = new GpxTrackModel();
			model.setName(trackResults.getString(1));
			model.setRunSheetID(trackResults.getString(2));
			model.setLatitude(trackResults.getDouble(3));
			model.setLongitude(trackResults.getDouble(4));
			model.setOrder(trackResults.getInt(5));

			tracks.add(model);
		}

		trackResults.close();

		wdb.close();
		return tracks;

	}

	public static synchronized void DeleteGpxTracks(Context context)
	{
		SQLiteDatabase wdb = GetSqliteHelper(context);

		wdb.delete(TABLE_GPX_TRACKS, null, null);
	}

}

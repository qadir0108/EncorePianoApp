package com.encore.piano.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.encore.piano.enums.AssignmentEnum;
import com.encore.piano.enums.GalleryEnum;
import com.encore.piano.enums.GpsEnum;
import com.encore.piano.enums.GpxTracksEnum;
import com.encore.piano.enums.LoginEnum;
import com.encore.piano.enums.PianoEnum;

public class Database extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/com.encore.piano/databases/";
	private static final String NAME = "ecorepiano.db";
	private static final int VERSION = 1;

	public Database(Context context) {
		super(context, NAME, null, VERSION);
	}

	public static Database instance = null;

	public static synchronized Database getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new Database(context.getApplicationContext());
		}
		return instance;
	}

	public synchronized static SQLiteDatabase getSqliteHelper(Context context)
	{
		return getInstance(context).getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

		String sql = "create table " + AssignmentEnum.TableName.Value + "(" +
                AssignmentEnum.Id.Value + " text primary key," +
                AssignmentEnum.ConsignmentNumber.Value + " text ," +
                AssignmentEnum.VehicleCode.Value + " text ," +
                AssignmentEnum.VehicleName.Value + " text ," +
                AssignmentEnum.DriverCode.Value + " text ," +
                AssignmentEnum.DriverName.Value + " text ," +
                AssignmentEnum.OrderId.Value + " text ," +
                AssignmentEnum.OrderNumber.Value + " text ," +
				AssignmentEnum.OrderType.Value + " text ," +
				AssignmentEnum.OrderedAt.Value + " text ," +
                AssignmentEnum.CallerName.Value + " text ," +
                AssignmentEnum.CallerPhoneNumber.Value + " text ," +
				AssignmentEnum.CallerPhoneNumberAlt.Value + " text ," +
				AssignmentEnum.CallerEmail.Value + " text ," +

				AssignmentEnum.PickupDate.Value + " text ," +
                AssignmentEnum.PickupAddress.Value + " text ," +
                AssignmentEnum.PickupPhoneNumber.Value + " text ," +
				AssignmentEnum.PickupAlternateContact.Value + " text ," +
				AssignmentEnum.PickupAlternatePhone.Value + " text ," +
				AssignmentEnum.PickupNumberStairs.Value + " text ," +
				AssignmentEnum.PickupNumberTurns.Value + " text ," +
				AssignmentEnum.PickupInstructions.Value + " text ," +

				AssignmentEnum.DeliveryDate.Value + " text ," +
				AssignmentEnum.DeliveryAddress.Value + " text ," +
				AssignmentEnum.DeliveryPhoneNumber.Value + " text ," +
				AssignmentEnum.DeliveryAlternateContact.Value + " text ," +
				AssignmentEnum.DeliveryAlternatePhone.Value + " text ," +
				AssignmentEnum.DeliveryNumberStairs.Value + " text ," +
				AssignmentEnum.DeliveryNumberTurns.Value + " text ," +
				AssignmentEnum.DeliveryInstructions.Value + " text ," +

                AssignmentEnum.CustomerCode.Value + " text ," +
                AssignmentEnum.CustomerName.Value + " text ," +
                AssignmentEnum.NumberOfItems.Value + " integer ," +
                AssignmentEnum.createdAt.Value + " text ," +
                AssignmentEnum.tripStaus.Value + " text ," +
                AssignmentEnum.unread.Value + " integer ," +
                AssignmentEnum.departureTime.Value + " text ," +
                AssignmentEnum.estimatedTime.Value + " text ," +
                AssignmentEnum.pickupLocation.Value + " text ," +
                AssignmentEnum.receiverName.Value + " text ," +
                AssignmentEnum.receiverSignaturePath.Value + " text ," +
                AssignmentEnum.dateSigned.Value + " text ," +
                AssignmentEnum.signed.Value + " integer ," +
                AssignmentEnum.saved.Value + " integer ," +
                AssignmentEnum.synced.Value + " integer );";

		String sqlitem = "create table " + PianoEnum.TableName.Value + "(" +
				PianoEnum.Id.Value + " text primary key," +
				PianoEnum.ConsignmentId.Value + " text," +
				PianoEnum.Category.Value + " text," +
				PianoEnum.Type.Value + " text," +
				PianoEnum.Size.Value + " text," +
				PianoEnum.Make.Value + " text," +
				PianoEnum.Model.Value + " text," +
				PianoEnum.Finish.Value + " text," +
				PianoEnum.SerialNumber.Value + " text," +
				PianoEnum.IsBench.Value + " integer," +
				PianoEnum.IsPlayer.Value + " integer," +
				PianoEnum.IsBoxed.Value + " integer," +

				PianoEnum.createdAt.Value + " text," +
				PianoEnum.pianoStatus.Value + " text," +
				PianoEnum.pickedAt.Value + " text," +
				PianoEnum.additionalLampStatus.Value + " integer," +
				PianoEnum.additionalOwnersManualStatus.Value + " integer," +
				PianoEnum.additionalCoverStatus.Value + " integer," +
				PianoEnum.additionalCasterCupsStatus.Value + " integer," +
				PianoEnum.additionalBenchesStatus.Value + " integer" + ");";

		String sqlImg = "create table " + GalleryEnum.TableName.Value + "(" +
                GalleryEnum.Id.Value + " text, " +
                GalleryEnum.ConsignmentId.Value + " text, " +
                GalleryEnum.ImageReference.Value + " text primary key," +
                GalleryEnum.ImagePath.Value + " text);";

		String sqlLogin = "create table " + LoginEnum.TableName.Value + "(" + BaseColumns._ID + " integer primary key autoincrement," +
                LoginEnum.AuthToken.Value + " text, " +
                LoginEnum.AuthTokenExpiry.Value + " text," +
                LoginEnum.UserName.Value + " text," +
                LoginEnum.IsActive.Value + " integer," +
                LoginEnum.VehicleCode.Value + " text," +
                LoginEnum.UserId.Value + " integer);";

		String sqlGps = "create table " + GpsEnum.TableName.Value + "(" + BaseColumns._ID + " integer primary key autoincrement," +
                GpsEnum.Latitude.Value + " text, " +
                GpsEnum.Longitude.Value + " text," +
                GpsEnum.Timestamp.Value + " text," +
                GpsEnum.IsSynced.Value + " integer);";

		String sqlGpx = "create table " + GpxTracksEnum.TableName.Value + "(" + BaseColumns._ID + " integer primary key autoincrement," +
                GpxTracksEnum.Latitude.Value + " text," +
                GpxTracksEnum.Longitude.Value + " text," +
                GpxTracksEnum.GpxTrackName.Value + " text," +
                GpxTracksEnum.GpxOrder.Value + " text," +
                GpxTracksEnum.ConsignmentId.Value + " text);";

		db.execSQL(sql);
		db.execSQL(sqlitem);
		db.execSQL(sqlImg);
		db.execSQL(sqlLogin);
		db.execSQL(sqlGps);
		db.execSQL(sqlGpx);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
	}

}

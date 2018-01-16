package com.encore.piano.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encore.piano.enums.GpsEnum;
import com.encore.piano.enums.GpxTracksEnum;
import com.encore.piano.model.GPSTrackingModel;
import com.encore.piano.model.GpxTrackModel;
import com.encore.piano.util.DateTimeUtility;

import java.util.ArrayList;

public class CardDb extends Database {

	public CardDb(Context context) {
		super(context);
	}

	public static synchronized void saveGpsCoordinates(Context context, GPSTrackingModel model)
	{
		ContentValues cv = new ContentValues();
		cv.put(GpsEnum.Latitude.Value, model.getLatitude());
		cv.put(GpsEnum.Longitude.Value, model.getLongitude());
		cv.put(GpsEnum.Timestamp.Value, DateTimeUtility.getCurrentTimeStampLongString());
		cv.put(GpsEnum.IsSynced.Value, false);
		SQLiteDatabase wdb = getSqliteHelper(context);
		wdb.insert(GpsEnum.TableName.Value, null, cv);
		wdb.close();
	}

	public static synchronized void markGpsCoordinateModelsAsSynced(Context context, ArrayList<Integer> gPSCoordinatesModelIds)
	{
		for (Integer id : gPSCoordinatesModelIds)
			MarkGpsCoordinatesAsSynced(context, id);
	}

	private static synchronized void MarkGpsCoordinatesAsSynced(Context context, int gpsModelId)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(GpsEnum.IsSynced.Value, true);
		wdb.update(GpsEnum.TableName.Value, cv, "_id = ?", new String[] { String.valueOf(gpsModelId) });
		wdb.close();
	}

	public static synchronized ArrayList<GPSTrackingModel> getGPSCoordinates(Context context, boolean synced)
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		String cols[] = { GpsEnum.Latitude.Value, GpsEnum.Longitude.Value, GpsEnum.Timestamp.Value, GpsEnum.IsSynced.Value, "_id" };
		Cursor results;

		if (synced)
			results = rdb.query(GpsEnum.TableName.Value, cols, GpsEnum.IsSynced.Value + " = ?", new String[] { String.valueOf(synced) }, null, null, null);
		else
			results = rdb.query(GpsEnum.TableName.Value, cols, "_id > ?", new String[] { String.valueOf(0) }, null, null, null);

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

	public static synchronized GPSTrackingModel getLastRecordedCoordinate(Context context)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		GPSTrackingModel model = null;
		String cols[] = { GpsEnum.Latitude.Value, GpsEnum.Longitude.Value};
		Cursor results = wdb.query(GpsEnum.TableName.Value, cols, "_id > 0", null, null, null, "_id DESC");
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
		SQLiteDatabase wdb = getSqliteHelper(context);
		Cursor results = wdb.query(GpxTracksEnum.TableName.Value, null, null, null, null, null, null);
		boolean exist = results.getCount() > 0 ? true : false;
		results.close();
		wdb.close();
		return exist;
	}

	public static synchronized void writeGpxTracks(Context context, ArrayList<GpxTrackModel> gpxTracks)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		for (GpxTrackModel gpxTrackModel : gpxTracks)
		{
			ContentValues cv = new ContentValues();
			cv.put("_id", gpxTrackModel.getOrder());
			cv.put(GpxTracksEnum.GpxTrackName.Value, gpxTrackModel.getName());
			cv.put(GpxTracksEnum.ConsignmentId.Value, gpxTrackModel.getConsignmentId());
			cv.put(GpxTracksEnum.Latitude.Value, gpxTrackModel.getLatitude());
			cv.put(GpxTracksEnum.Longitude.Value, gpxTrackModel.getLongitude());
			cv.put(GpxTracksEnum.GpxOrder.Value, gpxTrackModel.getOrder());
			wdb.insert(GpxTracksEnum.TableName.Value, null, cv);
		}
		wdb.close();
	}

	public static synchronized int countGpxTracks(Context context, String consignmentId)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		Cursor results = wdb.query(GpxTracksEnum.TableName.Value, null, GpxTracksEnum.ConsignmentId.Value + " = ?", new String[] { consignmentId }, null, null, null);
		int count = results.getCount();
		wdb.close();
		return count;
	}

	public static synchronized ArrayList<GpxTrackModel> GetGpxTracks(SQLiteDatabase wdb)
	{
		ArrayList<GpxTrackModel> tracks = new ArrayList<GpxTrackModel>();
		String cols[] = {
				"_id",
				GpxTracksEnum.GpxTrackName.Value,
				GpxTracksEnum.ConsignmentId.Value,
				GpxTracksEnum.Latitude.Value,
				GpxTracksEnum.Longitude.Value,
				GpxTracksEnum.GpxOrder.Value
		};
		Cursor trackResults = wdb.query(GpxTracksEnum.TableName.Value, cols, "_id > 0", null, null, null, null);
		while (trackResults.moveToNext())
		{
			GpxTrackModel model = new GpxTrackModel();
			model.setName(trackResults.getString(1));
			model.setConsignmentId(trackResults.getString(2));
			model.setLatitude(trackResults.getDouble(3));
			model.setLongitude(trackResults.getDouble(4));
			model.setOrder(trackResults.getInt(5));
			tracks.add(model);
		}
		trackResults.close();
		wdb.close();
		return tracks;
	}

	public static synchronized void deleteGpxTracks(Context context)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		wdb.delete(GpxTracksEnum.TableName.Value, null, null);
	}
}

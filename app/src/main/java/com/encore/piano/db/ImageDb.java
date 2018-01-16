package com.encore.piano.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encore.piano.enums.GalleryEnum;
import com.encore.piano.enums.PianoEnum;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.model.GalleryModel;
import com.encore.piano.util.FileUtility;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageDb extends Database {

	public ImageDb(Context context) {
		super(context);
	}

	public static synchronized long write(Context context, GalleryModel model)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(GalleryEnum.Id.Value, model.getId());
		cv.put(GalleryEnum.UnitId.Value, model.getUnitId());
		cv.put(GalleryEnum.ImagePath.Value, model.getImagePath());
		cv.put(GalleryEnum.TakenAt.Value, model.getTakenAt());
		cv.put(GalleryEnum.TakeLocation.Value, model.getTakenLocation());
		long result = wdb.insertOrThrow(GalleryEnum.TableName.Value, null, cv);
		wdb.close();
		return result;
	}

    public static synchronized void setSynced(Context context, String unitId) throws DatabaseUpdateException
    {
        SQLiteDatabase wdb = getSqliteHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(GalleryEnum.synced.Value, 1);
        if (wdb.update(GalleryEnum.TableName.Value, cv, GalleryEnum.Id.Value + " = ?", new String[] { unitId }) != 1)
            throw new DatabaseUpdateException();
        wdb.close();
    }

	public static synchronized ArrayList<GalleryModel> getImagesForUnit(Context context, String unitId)
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		ArrayList<GalleryModel> models = new ArrayList<GalleryModel>();
		String cols[] = {
				GalleryEnum.Id.Value,
                GalleryEnum.UnitId.Value,
                GalleryEnum.ImagePath.Value,
				GalleryEnum.TakenAt.Value,
				GalleryEnum.TakeLocation.Value,
                GalleryEnum.synced.Value
		};
		Cursor results = rdb.query(GalleryEnum.TableName.Value, cols, GalleryEnum.UnitId.Value + " = ?", new String[] { unitId }, null, null, null);
		while (results.moveToNext())
		{
			GalleryModel model = new GalleryModel();
			model.setId(results.getString(0));
			model.setUnitId(results.getString(1));
			model.setImagePath(results.getString(2));
			model.setTakenAt(results.getString(3));
			model.setTakenLocation(results.getString(4));
            model.setSynced(results.getInt(5) == 1);
			models.add(model);
		}
		results.close();
		rdb.close();
		return models;
	}

    public static synchronized ArrayList<GalleryModel> getImagesForUnit(Context context, String unitId, String takeLocation)
    {
        SQLiteDatabase rdb = getSqliteHelper(context);
        ArrayList<GalleryModel> models = new ArrayList<GalleryModel>();
        String cols[] = {
                GalleryEnum.Id.Value,
                GalleryEnum.UnitId.Value,
                GalleryEnum.ImagePath.Value,
                GalleryEnum.TakenAt.Value,
                GalleryEnum.TakeLocation.Value,
                GalleryEnum.synced.Value
        };
        Cursor results = rdb.query(GalleryEnum.TableName.Value, cols, GalleryEnum.UnitId.Value + " = ? AND " + GalleryEnum.TakeLocation.Value + " = ? "  , new String[] { unitId, takeLocation }, null, null, null);
        while (results.moveToNext())
        {
            GalleryModel model = new GalleryModel();
            model.setId(results.getString(0));
            model.setUnitId(results.getString(1));
            model.setImagePath(results.getString(2));
            model.setTakenAt(results.getString(3));
            model.setTakenLocation(results.getString(4));
            model.setSynced(results.getInt(5) == 1);
            models.add(model);
        }
        results.close();
        rdb.close();
        return models;
    }

	public static synchronized ArrayList<GalleryModel> getImagesForUnits(Context context, ArrayList<String> unitIds)
	{
		ArrayList<GalleryModel> galleryModel = new ArrayList<GalleryModel>();
		for (String id : unitIds)
		{
			galleryModel.addAll(getImagesForUnit(context, id));
		}
		return galleryModel;
	}

	public static synchronized int getImageCountForAssignment(Context context, String consignmentId)
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		Cursor results = rdb.query(GalleryEnum.TableName.Value, null, PianoEnum.OrderId.Value + " = ?", new String[] { consignmentId }, null, null, null);
		int count = results.getCount();
		rdb.close();
		return count;
	}

	public static synchronized void deleteImagesForAssignments(Context context, String unitId) throws IOException
	{
		String root = FileUtility.getImagesDirectory(unitId);
		File dir = new File(root);
		if (dir.exists())
			FileUtils.deleteDirectory(dir);

        ArrayList<GalleryModel> images = new ArrayList<GalleryModel>();
		SQLiteDatabase wdb = getSqliteHelper(context);
		String cols[] = { GalleryEnum.ImagePath.Value};
		Cursor results = wdb.query(GalleryEnum.TableName.Value, cols, GalleryEnum.UnitId.Value + " = '" + unitId + "'", null, null, null, null);

		while (results.moveToNext())
		{
			String path = results.getString(0);
            GalleryModel model = new GalleryModel();
            model.setImagePath(path);
            images.add(model);
		}
		results.close();
		for (GalleryModel model : images)
		{
			wdb.delete(GalleryEnum.TableName.Value, GalleryEnum.ImagePath.Value + " = '" + model.getImagePath() + "'", null);
		}
		wdb.close();
	}

	public static synchronized void deleteGalleryItem(Context context, GalleryModel galleryItem) throws IOException
	{
		String path = galleryItem.getImagePath().substring(5);
		File f = new File(path);
		if (f.exists())
			f.delete();

		SQLiteDatabase wdb = getSqliteHelper(context);
		wdb.delete(GalleryEnum.TableName.Value, GalleryEnum.ImagePath.Value + " = '" + galleryItem.getImagePath() + "'", null);

		wdb.close();

	}

}

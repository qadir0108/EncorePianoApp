package com.encore.piano.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encore.piano.data.StringConstants;
import com.encore.piano.enums.AssignmentEnum;
import com.encore.piano.enums.GalleryEnum;
import com.encore.piano.enums.PianoEnum;
import com.encore.piano.model.GalleryModel;
import com.encore.piano.server.Service;
import com.encore.piano.util.FileUtility;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageDb extends Database {

	public ImageDb(Context context) {
		super(context);
	}

	public static synchronized long write(Context context, String consignmentId, String imagePath, String imageRef)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(AssignmentEnum.Id.Value, consignmentId);
		cv.put(GalleryEnum.ImageReference.Value, imageRef);
		cv.put(GalleryEnum.ImagePath.Value, imagePath);
		long result = wdb.insertOrThrow(GalleryEnum.TableName.Value, null, cv);
		wdb.close();
		return result;
	}

	public static synchronized ArrayList<GalleryModel> getImagesForAssignment(Context context, String consignmentId)
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		ArrayList<GalleryModel> models = new ArrayList<GalleryModel>();

		String cols[] = { GalleryEnum.Id.Value, GalleryEnum.ImagePath.Value, GalleryEnum.ImageReference.Value};
		Cursor results = rdb.query(GalleryEnum.TableName.Value, cols, GalleryEnum.Id.Value + " = ?", new String[] { consignmentId }, null, null, null);

		while (results.moveToNext())
		{
			GalleryModel model = new GalleryModel();
			model.setId(results.getString(0));
			model.setConsignmentId(results.getString(0));
			model.setImagePath(results.getString(1));
			model.setImageReference(results.getString(2));
			models.add(model);
		}

		results.close();
		rdb.close();

		return models;
	}

	public static synchronized ArrayList<GalleryModel> getImagesForAssignments(Context context, ArrayList<String> consignmentIds)
	{
		ArrayList<GalleryModel> galleryModel = new ArrayList<GalleryModel>();

		for (String id : consignmentIds)
		{
			galleryModel.addAll(getImagesForAssignment(context, id));
		}

		return galleryModel;
	}

	public static synchronized int getImageCountForAssignment(Context context, String consignmentId)
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		Cursor results = rdb.query(GalleryEnum.TableName.Value, null, PianoEnum.ConsignmentId.Value + " = ?", new String[] { consignmentId }, null, null, null);
		int count = results.getCount();
		rdb.close();
		return count;
	}

	public static synchronized void deleteImagesForAssignments(Context context, String consId) throws IOException
	{
		String root = FileUtility.getImagesDirectory(consId);
		File dir = new File(root);
		if (dir.exists())
			FileUtils.deleteDirectory(dir);

        ArrayList<GalleryModel> images = new ArrayList<GalleryModel>();
		SQLiteDatabase wdb = getSqliteHelper(context);
		String cols[] = { GalleryEnum.ImagePath.Value, GalleryEnum.ImageReference.Value};
		Cursor results = wdb.query(GalleryEnum.TableName.Value, cols, PianoEnum.ConsignmentId.Value + " = '" + consId + "'", null, null, null, null);

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
			wdb.delete(GalleryEnum.TableName.Value, GalleryEnum.ImageReference.Value + " = '" + model.getImageReference() + "'", null);
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
		wdb.delete(GalleryEnum.TableName.Value, GalleryEnum.ImageReference.Value + " = '" + galleryItem.getImageReference() + "'", null);

		wdb.close();

	}

}

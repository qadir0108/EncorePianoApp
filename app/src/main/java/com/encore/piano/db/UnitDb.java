package com.encore.piano.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encore.piano.enums.AdditionalItemStatusEnum;
import com.encore.piano.enums.PianoEnum;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.UnitModel;

import java.io.File;
import java.util.ArrayList;

public class UnitDb extends Database {

	public UnitDb(Context context) {
		super(context);
	}

    public static synchronized int writeAll(Context context, ArrayList<UnitModel> items)
    {
        SQLiteDatabase wdb = getSqliteHelper(context);
        int numberOfItems = 0;
        try
        {
            for (int i = 0; i < items.size(); i++)
            {
                if (write(wdb, items.get(i)) != -1)
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

    public static synchronized int writeAll(Context context, String consignmentId, ArrayList<UnitModel> items)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		int numberOfItems = 0;
		try
		{
            ArrayList<AssignmentModel> alreadyAssigments = AssignmentDb.getAll(context, false, false, consignmentId);
            // already exists
            if (alreadyAssigments.size() != 0)
            {
                // if assignmentService is not started yet
                if (!alreadyAssigments.get(0).getTripStatus().equals(TripStatusEnum.NotStarted.Value)) {
                    for (int i = 0; i < items.size(); i++)
                    {
                        if (write(wdb, items.get(i)) != -1)
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

	private static synchronized long write(SQLiteDatabase wdb, UnitModel item)
	{
		ContentValues cv = new ContentValues();
		cv.put(PianoEnum.Id.Value, item.getId());
		cv.put(PianoEnum.ConsignmentId.Value, item.getAssignmentId());
		cv.put(PianoEnum.Category.Value, item.getCategory());
		cv.put(PianoEnum.Type.Value, item.getType());
		cv.put(PianoEnum.Size.Value, item.getSize());
		cv.put(PianoEnum.Make.Value, item.getMake());
		cv.put(PianoEnum.Model.Value, item.getModel());
		cv.put(PianoEnum.Finish.Value, item.getFinish());
		cv.put(PianoEnum.SerialNumber.Value, item.getSerialNumber());
		cv.put(PianoEnum.IsBench.Value, item.isBench() ? 1 : 0);
		cv.put(PianoEnum.IsPlayer.Value, item.isPlayer() ? 1 : 0);
		cv.put(PianoEnum.IsBoxed.Value, item.isBoxed() ? 1 : 0);

        cv.put(PianoEnum.createdAt.Value, item.getCreatedAt());
        cv.put(PianoEnum.pianoStatus.Value, item.getPianoStatus());
		return wdb.insert(PianoEnum.TableName.Value, null, cv);
	}

    public static synchronized ArrayList<UnitModel> getUnitsByAssignmentId(Context context, String assignmentId)
    {
        return getUnits(context, assignmentId, "", "");
    }

    public static synchronized ArrayList<UnitModel> getUnitsByUnitId(Context context, String unitId)
    {
        return getUnits(context, "", unitId, "");
    }

    public static synchronized ArrayList<UnitModel> getUnitsBySerialNumber(Context context, String serialNumber)
    {
        return getUnits(context, "", "", serialNumber);
    }

    public static synchronized ArrayList<UnitModel> getUnits(Context context, String assignmentId, String unitId, String serialNumber)
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		ArrayList<UnitModel> listmodel = new ArrayList<UnitModel>();
		String cols[] = new String[] {
                PianoEnum.Id.Value,
                PianoEnum.ConsignmentId.Value,
				PianoEnum.Category.Value,
                PianoEnum.Type.Value,
                PianoEnum.Size.Value,
				PianoEnum.Make.Value,
				PianoEnum.Model.Value,
                PianoEnum.Finish.Value,
                PianoEnum.SerialNumber.Value,
				PianoEnum.IsBench.Value,
                PianoEnum.IsPlayer.Value,
                PianoEnum.IsBoxed.Value,

                PianoEnum.createdAt.Value,
                PianoEnum.pianoStatus.Value,
                PianoEnum.pickedAt.Value,
                PianoEnum.additionalLampStatus.Value,
				PianoEnum.additionalOwnersManualStatus.Value,
				PianoEnum.additionalCoverStatus.Value,
				PianoEnum.additionalCasterCupsStatus.Value,
				PianoEnum.additionalBenchesStatus.Value,

                PianoEnum.deliveredAt.Value,
                PianoEnum.benchesUnloaded.Value,
                PianoEnum.casterCupsUnloaded.Value,
                PianoEnum.coverUnloaded.Value,
                PianoEnum.lampUnloaded.Value,
                PianoEnum.ownersManualUnloaded.Value,

                PianoEnum.receiverName.Value,
                PianoEnum.receiverSignaturePath.Value,
                PianoEnum.dateSigned.Value,
                PianoEnum.signed.Value,
                PianoEnum.synced.Value
		};

		Cursor results = null;
		if(assignmentId != "")
			results = rdb.query(PianoEnum.TableName.Value, cols, PianoEnum.ConsignmentId.Value + "= ?", new String[] { assignmentId }, null, null, null);
		else if(unitId != "")
			results = rdb.query(PianoEnum.TableName.Value, cols, PianoEnum.Id.Value + "= ?", new String[] { unitId }, null, null, null);
        else if(serialNumber != "")
            results = rdb.query(PianoEnum.TableName.Value, cols, PianoEnum.SerialNumber.Value + "= ?", new String[] { serialNumber }, null, null, null);
        else
			results = rdb.query(PianoEnum.TableName.Value, cols, null, null, null, null, PianoEnum.createdAt + " ASC");

		while (results.moveToNext())
		{
            UnitModel model = new UnitModel();
			model.setId(results.getString(0));
			model.setAssignmentId(results.getString(1));
			model.setCategory(results.getString(2));
			model.setType(results.getString(3));
			model.setSize(results.getString(4));
			model.setMake(results.getString(5));
			model.setModel(results.getString(6));
			model.setFinish(results.getString(7));
			model.setSerialNumber(results.getString(8));
			model.setBench(results.getInt(9) == 1);
			model.setPlayer(results.getInt(10) == 1);
			model.setBoxed(results.getInt(11) == 1);

			model.setCreatedAt(results.getString(12));
            model.setPianoStatus(results.getString(13));
            model.setPickedAt(results.getString(14));
			model.setAdditionalLampStatus((AdditionalItemStatusEnum.values()[results.getInt(15)]) );
			model.setAdditionalOwnersManualStatus((AdditionalItemStatusEnum.values()[results.getInt(16)]) );
			model.setAdditionalCoverStatus((AdditionalItemStatusEnum.values()[results.getInt(17)]) );
			model.setAdditionalCasterCupsStatus((AdditionalItemStatusEnum.values()[results.getInt(18)]) );
			model.setAdditionalBenchesStatus((AdditionalItemStatusEnum.values()[results.getInt(19)]) );

			listmodel.add(model);
		}

		rdb.close();
		results.close();
		return listmodel;
	}

    public static synchronized void setUnitLoaded(Context context, UnitModel model) throws DatabaseUpdateException {
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(PianoEnum.pianoStatus.Value, model.getPianoStatus());
		cv.put(PianoEnum.pickedAt.Value, model.getPickedAt());
		cv.put(PianoEnum.additionalBenchesStatus.Value, model.getAdditionalBenchesStatus().ordinal());
		cv.put(PianoEnum.additionalCasterCupsStatus.Value, model.getAdditionalCasterCupsStatus().ordinal());
		cv.put(PianoEnum.additionalCoverStatus.Value, model.getAdditionalCoverStatus().ordinal());
		cv.put(PianoEnum.additionalLampStatus.Value, model.getAdditionalOwnersManualStatus().ordinal());
		cv.put(PianoEnum.additionalOwnersManualStatus.Value, model.getAdditionalOwnersManualStatus().ordinal());
		if (wdb.update(PianoEnum.TableName.Value, cv, PianoEnum.Id.Value + " = ?", new String[] { model.getId() }) != 1)
			throw new DatabaseUpdateException();
		wdb.close();
    }

	public static synchronized void setUnitDelivered(Context context, UnitModel model) throws DatabaseUpdateException {
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(PianoEnum.pianoStatus.Value, model.getPianoStatus());
		cv.put(PianoEnum.deliveredAt.Value, model.getDeliveredAt());
		cv.put(PianoEnum.benchesUnloaded.Value, model.isBenchesUnloaded());
		cv.put(PianoEnum.casterCupsUnloaded.Value, model.isCasterCupsUnloaded());
		cv.put(PianoEnum.coverUnloaded.Value, model.isCoverUnloaded());
		cv.put(PianoEnum.lampUnloaded.Value, model.isLampUnloaded());
		cv.put(PianoEnum.ownersManualUnloaded.Value, model.isOwnersManualUnloaded());

        cv.put(PianoEnum.receiverName.Value, model.getReceiverName());
        cv.put(PianoEnum.receiverSignaturePath.Value, model.getReceiverSignaturePath());
        cv.put(PianoEnum.dateSigned.Value, model.getDateSigned());
        cv.put(PianoEnum.signed.Value, 1);

		if (wdb.update(PianoEnum.TableName.Value, cv, PianoEnum.Id.Value + " = ?", new String[] { model.getId() }) != 1)
			throw new DatabaseUpdateException();
		wdb.close();
	}

    public static synchronized void saveUnit(Context context, UnitModel model) throws DatabaseUpdateException {
        SQLiteDatabase wdb = getSqliteHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(PianoEnum.Category.Value, model.getCategory());
        cv.put(PianoEnum.Type.Value, model.getType());
        cv.put(PianoEnum.Size.Value, model.getSize());
        cv.put(PianoEnum.Make.Value, model.getMake());
        cv.put(PianoEnum.Model.Value, model.getModel());
        cv.put(PianoEnum.Finish.Value, model.getFinish());
        cv.put(PianoEnum.SerialNumber.Value, model.getSerialNumber());
        cv.put(PianoEnum.IsBench.Value, model.isBench() ? 1 : 0);
        cv.put(PianoEnum.IsPlayer.Value, model.isPlayer() ? 1 : 0);
        cv.put(PianoEnum.IsBoxed.Value, model.isBoxed() ? 1 : 0);

        if (wdb.update(PianoEnum.TableName.Value, cv, PianoEnum.Id.Value + " = ?", new String[] { model.getId() }) != 1)
            throw new DatabaseUpdateException();
        wdb.close();
    }

	public static synchronized void delete(Context context, String assignmentId)
	{
        ArrayList<UnitModel> units = getUnitsByAssignmentId(context, assignmentId);
        for (UnitModel unit : units)
        {
            if (unit.getReceiverSignaturePath() != null && !unit.getReceiverSignaturePath().equals(""))
            {
                File f = new File(unit.getReceiverSignaturePath());
                if (f.exists())
                    f.delete();
            }
        }
		SQLiteDatabase wdb = getSqliteHelper(context);
		wdb.delete(PianoEnum.TableName.Value, PianoEnum.ConsignmentId.Value + " = '" + assignmentId + "'", null);
		wdb.close();
	}

	public static synchronized void delete(SQLiteDatabase wdb, String consignmentId)
	{
		wdb.delete(PianoEnum.TableName.Value, PianoEnum.ConsignmentId.Value + " = '" + consignmentId + "'", null);
	}

}

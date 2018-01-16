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
		cv.put(PianoEnum.OrderId.Value, item.getOrderId());
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

    public static synchronized ArrayList<UnitModel> getUnitsByOrderId(Context context, String orderId)
    {
        return getUnits(context, orderId, "", "");
    }

    public static synchronized ArrayList<UnitModel> getUnitsByUnitId(Context context, String unitId)
    {
        return getUnits(context, "", unitId, "");
    }

    public static synchronized ArrayList<UnitModel> getUnitsBySerialNumber(Context context, String serialNumber)
    {
        return getUnits(context, "", "", serialNumber);
    }

    public static synchronized ArrayList<UnitModel> getUnits(Context context, String orderId, String unitId, String serialNumber)
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		ArrayList<UnitModel> listmodel = new ArrayList<UnitModel>();
		String cols[] = new String[] {
                PianoEnum.Id.Value,
                PianoEnum.OrderId.Value,
				PianoEnum.Category.Value,
                PianoEnum.Type.Value,
                PianoEnum.Size.Value,
				PianoEnum.Make.Value,
				PianoEnum.Model.Value,
                PianoEnum.Finish.Value,
                PianoEnum.SerialNumber.Value,
				PianoEnum.IsBench.Value,
                PianoEnum.IsPlayer.Value,
                PianoEnum.IsBoxed.Value, //11

                PianoEnum.createdAt.Value,
                PianoEnum.pianoStatus.Value,
                PianoEnum.pickedAt.Value,
                PianoEnum.pickerName.Value,
                PianoEnum.pickerSignaturePath.Value,
				PianoEnum.additionalBench1Status.Value,
				PianoEnum.additionalBench2Status.Value,
				PianoEnum.additionalCasterCupsStatus.Value,
				PianoEnum.additionalLampStatus.Value,
				PianoEnum.additionalCoverStatus.Value,
				PianoEnum.additionalOwnersManualStatus.Value,
				PianoEnum.additionalMisc1Status.Value,
				PianoEnum.additionalMisc2Status.Value,
				PianoEnum.additionalMisc3Status.Value,
				PianoEnum.syncLoaded.Value, // 26

                PianoEnum.deliveredAt.Value,
                PianoEnum.receiverName.Value,
                PianoEnum.receiverSignaturePath.Value,
                PianoEnum.bench1Unloaded.Value,
                PianoEnum.bench2Unloaded.Value,
                PianoEnum.casterCupsUnloaded.Value,
				PianoEnum.lampUnloaded.Value,
				PianoEnum.coverUnloaded.Value,
                PianoEnum.ownersManualUnloaded.Value,
                PianoEnum.misc1Unloaded.Value,
                PianoEnum.misc2Unloaded.Value,
                PianoEnum.misc3Unloaded.Value,
				PianoEnum.syncDelivered.Value // 39
		};

		Cursor results = null;
		if(orderId != "")
			results = rdb.query(PianoEnum.TableName.Value, cols, PianoEnum.OrderId.Value + "= ?", new String[] { orderId }, null, null, null);
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
			model.setOrderId(results.getString(1));
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
            model.setPickerName(results.getString(15));
            model.setPickerSignaturePath(results.getString(16));
            model.setAdditionalBench1Status((AdditionalItemStatusEnum.values()[results.getInt(17)]) );
            model.setAdditionalBench2Status((AdditionalItemStatusEnum.values()[results.getInt(18)]) );
            model.setAdditionalCasterCupsStatus((AdditionalItemStatusEnum.values()[results.getInt(19)]) );
            model.setAdditionalLampStatus((AdditionalItemStatusEnum.values()[results.getInt(20)]) );
            model.setAdditionalCoverStatus((AdditionalItemStatusEnum.values()[results.getInt(21)]) );
			model.setAdditionalOwnersManualStatus((AdditionalItemStatusEnum.values()[results.getInt(22)]) );
			model.setAdditionalMisc1Status((AdditionalItemStatusEnum.values()[results.getInt(23)]) );
			model.setAdditionalMisc2Status((AdditionalItemStatusEnum.values()[results.getInt(24)]) );
			model.setAdditionalMisc3Status((AdditionalItemStatusEnum.values()[results.getInt(25)]) );
            model.setSyncLoaded(results.getInt(26) == 1);

            model.setDeliveredAt(results.getString(27));
			model.setReceiverName(results.getString(28));
			model.setReceiverSignaturePath(results.getString(29));
            model.setBench1Unloaded(results.getInt(30) == 1);
            model.setBench2Unloaded(results.getInt(31) == 1);
            model.setCasterCupsUnloaded(results.getInt(32) == 1);
            model.setLampUnloaded(results.getInt(33) == 1);
            model.setCoverUnloaded(results.getInt(34) == 1);
            model.setOwnersManualUnloaded(results.getInt(35) == 1);
            model.setMisc1Unloaded(results.getInt(36) == 1);
            model.setMisc2Unloaded(results.getInt(37) == 1);
            model.setMisc3Unloaded(results.getInt(38) == 1);
            model.setSyncDelivered(results.getInt(39) == 1);

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
        cv.put(PianoEnum.pickerName.Value, model.getPickerName());
        cv.put(PianoEnum.pickerSignaturePath.Value, model.getPickerSignaturePath());
		cv.put(PianoEnum.additionalBench1Status.Value, model.getAdditionalBench1Status().ordinal());
		cv.put(PianoEnum.additionalBench2Status.Value, model.getAdditionalBench2Status().ordinal());
		cv.put(PianoEnum.additionalCasterCupsStatus.Value, model.getAdditionalCasterCupsStatus().ordinal());
        cv.put(PianoEnum.additionalLampStatus.Value, model.getAdditionalOwnersManualStatus().ordinal());
		cv.put(PianoEnum.additionalCoverStatus.Value, model.getAdditionalCoverStatus().ordinal());
		cv.put(PianoEnum.additionalOwnersManualStatus.Value, model.getAdditionalOwnersManualStatus().ordinal());
		cv.put(PianoEnum.additionalMisc1Status.Value, model.getAdditionalMisc1Status().ordinal());
		cv.put(PianoEnum.additionalMisc2Status.Value, model.getAdditionalMisc2Status().ordinal());
		cv.put(PianoEnum.additionalMisc3Status.Value, model.getAdditionalMisc3Status().ordinal());
		if (wdb.update(PianoEnum.TableName.Value, cv, PianoEnum.Id.Value + " = ?", new String[] { model.getId() }) != 1)
			throw new DatabaseUpdateException();
		wdb.close();
    }

	public static synchronized void setSyncedLoaded(Context context, String unitId) throws DatabaseUpdateException
    {
        SQLiteDatabase wdb = getSqliteHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(PianoEnum.syncLoaded.Value, 1);
        if (wdb.update(PianoEnum.TableName.Value, cv, PianoEnum.Id.Value + " = ?", new String[] { unitId }) != 1)
            throw new DatabaseUpdateException();
        wdb.close();
    }

	public static synchronized void setUnitDelivered(Context context, UnitModel model) throws DatabaseUpdateException {
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(PianoEnum.pianoStatus.Value, model.getPianoStatus());
		cv.put(PianoEnum.deliveredAt.Value, model.getDeliveredAt());
        cv.put(PianoEnum.receiverName.Value, model.getReceiverName());
        cv.put(PianoEnum.receiverSignaturePath.Value, model.getReceiverSignaturePath());
		cv.put(PianoEnum.bench1Unloaded.Value, model.isBench1Unloaded());
		cv.put(PianoEnum.bench2Unloaded.Value, model.isBench2Unloaded());
		cv.put(PianoEnum.casterCupsUnloaded.Value, model.isCasterCupsUnloaded());
        cv.put(PianoEnum.lampUnloaded.Value, model.isLampUnloaded());
		cv.put(PianoEnum.coverUnloaded.Value, model.isCoverUnloaded());
		cv.put(PianoEnum.ownersManualUnloaded.Value, model.isOwnersManualUnloaded());
		cv.put(PianoEnum.misc1Unloaded.Value, model.isMisc1Unloaded());
		cv.put(PianoEnum.misc2Unloaded.Value, model.isMisc2Unloaded());
		cv.put(PianoEnum.misc3Unloaded.Value, model.isMisc3Unloaded());

		if (wdb.update(PianoEnum.TableName.Value, cv, PianoEnum.Id.Value + " = ?", new String[] { model.getId() }) != 1)
			throw new DatabaseUpdateException();
		wdb.close();
	}

    public static synchronized void setSyncedDelivered(Context context, String unitId) throws DatabaseUpdateException
    {
        SQLiteDatabase wdb = getSqliteHelper(context);
        ContentValues cv = new ContentValues();
        cv.put(PianoEnum.syncDelivered.Value, 1);
        if (wdb.update(PianoEnum.TableName.Value, cv, PianoEnum.Id.Value + " = ?", new String[] { unitId }) != 1)
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

	public static synchronized void delete(Context context, String orderId)
	{
        ArrayList<UnitModel> units = getUnitsByOrderId(context, orderId);
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
		wdb.delete(PianoEnum.TableName.Value, PianoEnum.OrderId.Value + " = '" + orderId + "'", null);
		wdb.close();
	}

	public static synchronized void delete(SQLiteDatabase wdb, String orderId)
	{
		wdb.delete(PianoEnum.TableName.Value, PianoEnum.OrderId.Value + " = '" + orderId + "'", null);
	}

}

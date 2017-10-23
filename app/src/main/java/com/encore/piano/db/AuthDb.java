package com.encore.piano.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encore.piano.enums.LoginEnum;
import com.encore.piano.exceptions.AuthTokenExpiredException;
import com.encore.piano.exceptions.EmptyAuthTokenException;
import com.encore.piano.model.LoginModel;
import com.encore.piano.util.DateTimeUtility;

public class AuthDb extends Database {

	public AuthDb(Context context) {
		super(context);
	}

	public static LoginModel getLoginData(Context context) throws EmptyAuthTokenException, AuthTokenExpiredException
	{
		SQLiteDatabase rdb = getSqliteHelper(context);
		String cols[] = {LoginEnum.AuthTokenExpiry.Value, LoginEnum.AuthToken.Value, LoginEnum.UserId.Value, LoginEnum.UserName.Value, LoginEnum.VehicleCode.Value};
		Cursor results = rdb.query(LoginEnum.TableName.Value, cols, LoginEnum.IsActive.Value + " = ?", new String[] { String.valueOf(1) }, null, null, "_id DESC");

		if (results.moveToFirst())
		{
			String authToken = results.getString(1);
			if (authToken.equals(""))
				throw new EmptyAuthTokenException();
			long expiryTime = Long.parseLong(results.getString(0));

			if (expiryTime > DateTimeUtility.getCurrentTimeStampLong())
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

	public static synchronized long write(Context context, LoginModel loginModel)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(LoginEnum.AuthToken.Value, loginModel.getAuthToken());
		cv.put(LoginEnum.AuthTokenExpiry.Value, String.valueOf(loginModel.getTime()));
		cv.put(LoginEnum.UserId.Value, loginModel.getUserId());
		cv.put(LoginEnum.UserName.Value, loginModel.getUserName());
		cv.put(LoginEnum.IsActive.Value, 1);
		cv.put(LoginEnum.VehicleCode.Value, loginModel.getFCMToken());
		long no = wdb.insert(LoginEnum.TableName.Value, null, cv);
		wdb.close();
		return no;
	}

	public static synchronized void activateLoginData(Context context) throws EmptyAuthTokenException
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		String query = "SELECT MAX(_id) FROM " + LoginEnum.TableName.Value;
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
		cv.put(LoginEnum.IsActive.Value, 1);
		wdb.update(LoginEnum.TableName.Value, cv, "_id = ?", new String[] { String.valueOf(id) });
		wdb.close();
	}

	public static synchronized void logOff(Context context)
	{
		SQLiteDatabase wdb = getSqliteHelper(context);
		ContentValues cv = new ContentValues();
		cv.put(LoginEnum.IsActive.Value, 0);
		wdb.update(LoginEnum.TableName.Value, cv, LoginEnum.IsActive.Value + " = ?", new String[] { String.valueOf(1) });
		wdb.close();
	}

}

package com.encore.piano.listview.unit;


import android.widget.Button;
import android.widget.TextView;

public class UnitViewHolder extends ViewHolder{

	TextView tvUnitId;
	TextView tvAssignmentId;
	TextView tvCategory;
	TextView tvType;
	TextView tvSize;
	TextView tvMake;
	TextView tvModel;
	TextView tvFinish;
	TextView tvSerialNumber;
	TextView tvBench;
	TextView tvPlayer;
	TextView tvBoxed;
	TextView tvStatus;
	Button btnLoad;
	Button btnDeliver;

	public UnitViewHolder(TextView i, TextView ass, TextView c, TextView t, TextView s, TextView m, TextView mm, TextView f,
                          TextView ser, TextView b, TextView p, TextView bx, TextView st,
						  Button bl, Button bd)
	{
        tvUnitId = i;
        tvAssignmentId = ass;
		tvCategory = c;
		tvType = t;
		tvSize = s;
		tvMake = m;
		tvModel = mm;
		tvFinish = f;
		tvSerialNumber = ser;
		tvBench = b;
		tvPlayer = p;
		tvBoxed = bx;
        tvStatus = st;
		btnLoad = bl;
		btnDeliver = bd;
	}
}

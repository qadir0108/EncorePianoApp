package com.encore.piano.listview.assignment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AssignmentViewHolder extends ViewHolder{

	TextView tvConsigmentNumber;
	TextView tvPickupAddress;
	TextView tvDeliveryAddress;
	TextView tvPosition;
	TextView Id;	
	ImageView imgCompleted;
	CheckBox tvConsignmentId;
	public View rootView;
	
	public AssignmentViewHolder(TextView dc, TextView pa, TextView da, TextView p, ImageView c, TextView i, CheckBox s)
	{
		tvConsigmentNumber = dc;
		tvPickupAddress = pa;
		tvDeliveryAddress = da;
		tvPosition = p;
		imgCompleted = c;
		Id = i;
		tvConsignmentId = s;
	}
}

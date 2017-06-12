package com.encore.piano.listview.consignment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ConsignmentViewHolder extends ViewHolder{

	TextView DeliveryCode;
	TextView DeliveryAddress;
	TextView Position;
	TextView Id;	
	ImageView Completed;
	CheckBox SelectedItem;
	public View rootView;
	
	public ConsignmentViewHolder(TextView dc, TextView da, TextView p, ImageView c, TextView i, CheckBox s)
	{
		DeliveryCode = dc;
		DeliveryAddress = da;
		Position = p;
		Completed = c;
		Id = i;
		SelectedItem = s;
	}
}

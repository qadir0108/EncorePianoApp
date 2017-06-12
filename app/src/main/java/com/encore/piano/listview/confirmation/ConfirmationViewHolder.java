package com.encore.piano.listview.confirmation;

import android.widget.CheckBox;
import android.widget.TextView;

public class ConfirmationViewHolder extends ViewHolder{

	TextView Condition;
	CheckBox Confirmed;	
	
	public ConfirmationViewHolder(TextView cnd, CheckBox c)
	{
		Condition = cnd;
		Confirmed = c;		
	}
}

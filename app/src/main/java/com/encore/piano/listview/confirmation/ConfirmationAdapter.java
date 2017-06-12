package com.encore.piano.listview.confirmation;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.encore.piano.R;

public class ConfirmationAdapter extends ConfirmationAdapterBase {

	TextView Condition;
	CheckBox Confirmed;
	
	public ConfirmationAdapter(Context context) {
		super(context, R.layout.confirmationlistitem);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ViewHolder CreateHolder(View view) {
		Condition = (TextView)view.findViewById(R.id.confirmationTitleTextView);
		Confirmed = (CheckBox)view.findViewById(R.id.confirmationCheckBox);
		
		ConfirmationViewHolder holder = new ConfirmationViewHolder(Condition, Confirmed);
		return holder;
	}

	@Override
	protected void BindHolder(ViewHolder holder) {
		
		ConfirmationViewHolder h = (ConfirmationViewHolder)holder;		
		
		h.Condition.setText(h.Confirmation.getCondition());		
		h.Confirmed.setChecked(h.Confirmation.isConfirmed());
	}
}

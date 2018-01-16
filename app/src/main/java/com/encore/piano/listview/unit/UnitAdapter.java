package com.encore.piano.listview.unit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.encore.piano.R;
import com.encore.piano.activities.UnitDeliveryLoad;
import com.encore.piano.activities.UnitDeliveryUnLoad;
import com.encore.piano.data.NumberConstants;
import com.encore.piano.data.StringConstants;

public class UnitAdapter extends UnitAdapterBase {

	public UnitAdapter(Context context, String assignmentId, String orderId) {
		super(context, R.layout.assignment_units_row, assignmentId, orderId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ViewHolder CreateHolder(View view) {

		UnitViewHolder holder = new UnitViewHolder(
                (TextView)view.findViewById(R.id.tvUnitId),
                (TextView)view.findViewById(R.id.tvAssignmentId),
                (TextView)view.findViewById(R.id.tvOrderId),
                (TextView)view.findViewById(R.id.tvCategory),
				(TextView)view.findViewById(R.id.tvType),
				(TextView)view.findViewById(R.id.tvSize),
				(TextView)view.findViewById(R.id.tvMake),
				(TextView)view.findViewById(R.id.tvModel),
				(TextView)view.findViewById(R.id.tvFinish),
				(TextView)view.findViewById(R.id.tvSerialNumber),
				(TextView)view.findViewById(R.id.tvBench),
				(TextView)view.findViewById(R.id.tvPlayer),
				(TextView)view.findViewById(R.id.tvBoxed),
				(TextView)view.findViewById(R.id.tvStatus),
				(Button) view.findViewById(R.id.btnLoad),
				(Button)view.findViewById(R.id.btnDeliver)
		);

		return holder;
	}

	@Override
	protected void BindHolder(ViewHolder holder) {
		
		final UnitViewHolder h = (UnitViewHolder)holder;
        h.tvUnitId.setText(h.unit.getId());
        h.tvAssignmentId.setText(h.unit.getAssignmentId());
        h.tvOrderId.setText(h.unit.getOrderId());
		h.tvCategory.setText(h.unit.getCategory());
		h.tvType.setText(h.unit.getType());
		h.tvSize.setText(h.unit.getSize());
		h.tvMake.setText(h.unit.getMake());
		h.tvModel.setText(h.unit.getModel());
		h.tvFinish.setText(h.unit.getFinish());
		h.tvSerialNumber.setText(h.unit.getSerialNumber());
		h.tvStatus.setText(h.unit.getPianoStatus());
		h.tvBench.setText(h.unit.isBench() ? "W/B" : "N/B");
		h.tvPlayer.setText(h.unit.isPlayer() ? "Yes" : "No");
		h.tvBoxed.setText(h.unit.isBoxed() ? "Yes" : "No");

        h.btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0)
            {
                Intent i = new Intent(context, UnitDeliveryLoad.class);
                i.putExtra(StringConstants.INTENT_KEY_UNIT_ID, h.tvUnitId.getText().toString());
				i.putExtra(StringConstants.INTENT_KEY_ASSIGNMENT_ID, h.tvAssignmentId.getText().toString());
				i.putExtra(StringConstants.INTENT_KEY_ORDER_ID, h.tvOrderId.getText().toString());
				((Activity)context).startActivityForResult(i, NumberConstants.REQUEST_CODE_LOAD_UNIT);
            }
        });

        h.btnDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0)
            {
                Intent i = new Intent(context, UnitDeliveryUnLoad.class);
                i.putExtra(StringConstants.INTENT_KEY_UNIT_ID, h.tvUnitId.getText().toString());
				i.putExtra(StringConstants.INTENT_KEY_ASSIGNMENT_ID, h.tvAssignmentId.getText().toString());
                i.putExtra(StringConstants.INTENT_KEY_ORDER_ID, h.tvOrderId.getText().toString());
                ((Activity)context).startActivityForResult(i, NumberConstants.REQUEST_CODE_UNLOAD_UNIT);
            }
        });

	}

}

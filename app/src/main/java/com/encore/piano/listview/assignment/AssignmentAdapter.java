package com.encore.piano.listview.assignment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.AssignmentModel;

public class AssignmentAdapter extends AssignmentAdapterBase {

	TextView tvConsigmentNumber;
	TextView tvPickupAddress;
	TextView tvDeliveryAddress;
	TextView tvPosition;
	TextView tvConsignmentId;
	ImageView imgCompleted;
	CheckBox chkSelectedItem;
	
	HashMap<Integer, Integer> selectedItems = new HashMap<Integer, Integer>();
	private View rootView;
	
	public AssignmentAdapter(Context context, int layoutID, ArrayList<AssignmentModel> consignments)
	{
		super(context, layoutID);
		Consignments = consignments;
	}

	public AssignmentAdapter(Context context, boolean showCompleted) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException {
		super(context, R.layout.assignments_row, showCompleted);
		// TODO Auto-generated constructor stub
	}
	
	public void setSelection(int position){
		selectedItems.put(position, position);
		notifyDataSetChanged();
	}
	
	public void removeSelection(int position){
		selectedItems.remove(position);
		notifyDataSetChanged();
	}
	
	public void clearSelections(){
		selectedItems.clear();
		notifyDataSetChanged();
	}
	
	public HashMap<Integer, Integer> getSelectedItemPositions(){
		return selectedItems;
	}

	@Override
	protected ViewHolder CreateHolder(View view) {
		tvConsigmentNumber = (TextView)view.findViewById(R.id.tvConsigmentNumber);
		tvPickupAddress = (TextView)view.findViewById(R.id.tvPickupAddress);
		tvDeliveryAddress = (TextView)view.findViewById(R.id.tvOrderType);
		tvPosition = (TextView)view.findViewById(R.id.tvPosition);
		imgCompleted = (ImageView)view.findViewById(R.id.imgCompleted);
		tvConsignmentId = (TextView)view.findViewById(R.id.tvConsignmentId);
		chkSelectedItem = (CheckBox)view.findViewById(R.id.chkSelectedItem);
		rootView = (View) view.findViewById(R.id.consignmentBg);

		AssignmentViewHolder holder = new AssignmentViewHolder(tvConsigmentNumber,tvPickupAddress, tvDeliveryAddress, tvPosition, imgCompleted, tvConsignmentId, chkSelectedItem);
		holder.rootView = rootView;
		return holder;
	}

	@Override
	protected void BindHolder(ViewHolder holder) {
		
		AssignmentViewHolder h = (AssignmentViewHolder)holder;

		h.tvConsigmentNumber.setText(h.assignment.getConsignmentNumber());
		h.tvPickupAddress.setText(h.assignment.getPickupAddress());
		h.tvDeliveryAddress.setText(h.assignment.getDeliveryAddress());
		h.tvPosition.setText(String.valueOf(h.position+1));
		h.Id.setText(h.assignment.getId());
		
//		if (h.assignment.getTripStatus() == TripStatusEnum.Started.Value)
//			h.rootView.setBackgroundColor(0xFF63d5ff);
//		else
//			h.rootView.setBackgroundColor(0xFFffffff);
//
//		if (h.assignment.isUnread())
//			h.rootView.setBackgroundColor(0xFF63d5ff);
//		else
//			h.rootView.setBackgroundColor(0xFFffffff);

		if (selectedItems.size() > 0)
			h.tvConsignmentId.setVisibility(View.VISIBLE);
		else
			h.tvConsignmentId.setVisibility(View.GONE);

		if(selectedItems.containsKey(h.position))
			h.tvConsignmentId.setChecked(true);
		else
			h.tvConsignmentId.setChecked(false);
		
		if (h.assignment.getTripStatus() != TripStatusEnum.Completed.Value || !h.assignment.isSigned())
			h.imgCompleted.setVisibility(View.INVISIBLE);
		else
			h.imgCompleted.setVisibility(View.VISIBLE);
	}
}

package com.encore.piano.listview.consignment;

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
import com.encore.piano.model.ConsignmentModel;

public class ConsignmentAdapter extends ConsignmentAdapterBase {

	TextView DeliveryCode;
	TextView DeliveryAddress;
	TextView Position;
	TextView Id;
	ImageView Completed;
	CheckBox SelectedItem;
	
	HashMap<Integer, Integer> selectedItems = new HashMap<Integer, Integer>();
	private View rootView;
	
	public ConsignmentAdapter(Context context, int layoutID, ArrayList<ConsignmentModel> consignments)
	{
		super(context, layoutID);
		Consignments = consignments;
	}

	public ConsignmentAdapter(Context context, boolean showCompleted) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException {
		super(context, R.layout.runsheetlistitem, showCompleted);
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
		DeliveryCode = (TextView)view.findViewById(R.id.deliveriCodeTextView);
		DeliveryAddress = (TextView)view.findViewById(R.id.deliveryAddressTextView);
		Position = (TextView)view.findViewById(R.id.positionTextView);
		Completed = (ImageView)view.findViewById(R.id.completedImageView);
		Id = (TextView)view.findViewById(R.id.consignmentIdTextView);
		SelectedItem = (CheckBox)view.findViewById(R.id.selectedItem);
		rootView = (View) view.findViewById(R.id.consignmentBg);

		ConsignmentViewHolder holder = new ConsignmentViewHolder(DeliveryCode, DeliveryAddress, Position, Completed, Id, SelectedItem);
		holder.rootView = rootView;
		return holder;
	}

	@Override
	protected void BindHolder(ViewHolder holder) {
		
		ConsignmentViewHolder h = (ConsignmentViewHolder)holder;

		h.DeliveryCode.setText(h.Consignment.getConsignmentNumber());
		h.DeliveryAddress.setText(h.Consignment.getDeliveryAddress());
		h.Position.setText(String.valueOf(h.position+1));
		h.Id.setText(h.Consignment.getId());
		
		if (h.Consignment.getTripStatus() == TripStatusEnum.Started.Value)
			h.rootView.setBackgroundColor(0xFF63d5ff);
		else
			h.rootView.setBackgroundColor(0xFFffffff);

		//	if (h.Consignment.isUnread)
		//		h.rootView.setBackgroundColor(0xFF63d5ff);
		//	else
		//		h.rootView.setBackgroundColor(0xFFffffff);

		if (selectedItems.size() > 0)
			h.SelectedItem.setVisibility(View.VISIBLE);
		else
			h.SelectedItem.setVisibility(View.GONE);

		if(selectedItems.containsKey(h.position))
			h.SelectedItem.setChecked(true);
		else
			h.SelectedItem.setChecked(false);
		
		if (h.Consignment.getTripStatus() != TripStatusEnum.Completed.Value || !h.Consignment.isSigned())
			h.Completed.setVisibility(View.INVISIBLE);
		else
			h.Completed.setVisibility(View.VISIBLE);
	}
}

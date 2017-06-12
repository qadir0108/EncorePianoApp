package com.encore.piano.listview.item;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.encore.piano.R;

public class ItemAdapter extends ItemAdapterBase {

	TextView Name;
	TextView Code;
	TextView Barcode;
	TextView Quantity;
	TextView Pod;
	TextView Id;	
	
	public ItemAdapter(Context context, String consignmentId) {
		super(context, R.layout.itemslistitem, consignmentId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ViewHolder CreateHolder(View view) {
		Name = (TextView)view.findViewById(R.id.itemNameTextView);
		Code = (TextView)view.findViewById(R.id.itemCodeTextView);
		Barcode = (TextView)view.findViewById(R.id.itemBarcodeTextView);
		Quantity = (TextView)view.findViewById(R.id.itemQuantityTextView);
		Pod = (TextView)view.findViewById(R.id.itemPodTextView);
		Id = (TextView)view.findViewById(R.id.itemIdTextView);
		
		ItemViewHolder holder = new ItemViewHolder(Name, Code, Barcode, Quantity, Pod, Id);
		return holder;
	}

	@Override
	protected void BindHolder(ViewHolder holder) {
		
		ItemViewHolder h = (ItemViewHolder)holder;	
		
		h.Name.setText(h.Item.getName());
		h.Code.setText(h.Item.getType());
		h.Barcode.setText(h.Item.getMake());
		h.Quantity.setText(h.Item.getModel());
		h.Pod.setText(h.Item.getSerialNumber());
		h.Id.setText(String.valueOf(h.Item.isStairs()));
	}
}

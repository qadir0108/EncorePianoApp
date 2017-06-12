package com.encore.piano.listview.item;


import android.widget.TextView;

public class ItemViewHolder extends ViewHolder{

	TextView Name;
	TextView Code;
	TextView Barcode;
	TextView Quantity;
	TextView Pod;
	TextView Id;
	
	public ItemViewHolder(TextView n, TextView c, TextView b, TextView q, TextView p, TextView i)
	{
		Name = n;
		Code = c;
		Barcode = b;
		Quantity = q;
		Pod = p;
		Id = i;
	}
}

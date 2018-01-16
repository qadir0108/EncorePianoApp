package com.encore.piano.listview.gallery;


import android.widget.ImageView;
import android.widget.TextView;

public class GalleryViewHolder extends ViewHolder{

	TextView unitId;
	TextView ImagePath;
	ImageView ImageThumb;
	
	public GalleryViewHolder(ImageView i, TextView c, TextView ip)
	{
		ImageThumb = i;
		unitId = c;
		ImagePath = ip;
	}
}

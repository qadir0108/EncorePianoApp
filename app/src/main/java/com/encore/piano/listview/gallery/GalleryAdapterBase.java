package com.encore.piano.listview.gallery;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.encore.piano.server.Service;
import com.encore.piano.model.GalleryModel;

public abstract class GalleryAdapterBase extends BaseAdapter {

	Context context;
	int viewId;

	ArrayList<GalleryModel> images = new ArrayList<GalleryModel>();

	public GalleryAdapterBase(Context context, int viewId, String consignmentId, String takenLocation) {
		this.context = context;
		this.viewId = viewId;
		images.clear();
		images = Service.galleryService.getImagesForUnit(consignmentId, takenLocation);
	}

	@Override
	public int getCount()
	{
		return images.size();
	}

	@Override
	public GalleryModel getItem(int position)
	{
		return images.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		CheckableLayout l;
		ImageView i;

		ViewHolder viewHolder;
		if (view == null)
		{
			view = LayoutInflater.from(context).inflate(viewId, null);

			viewHolder = CreateHolder(view);
			view.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder) view.getTag();

		/*if (view == null)
		{
			i = new ImageView(context);
			//	i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			//	i.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
			l = new CheckableLayout(context);
			l.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,
					GridView.LayoutParams.WRAP_CONTENT));
			l.addView(i);
		}
		else
		{
			l = (CheckableLayout) view;
			i = (ImageView) l.getChildAt(0);
		}
		*/
		/*
				ViewHolder viewHolder;
				if(view == null)			
				{
					view = LayoutInflater.from(context).inflate(viewId, null);
					
					viewHolder = CreateHolder(view);
					view.setTag(viewHolder);			
				}
				else
					viewHolder = (ViewHolder)view.getTag();
				*/
		viewHolder.GalleryModel = getItem(position);
		viewHolder.position = position;
		BindHolder(viewHolder);

		return view;
	}

	protected abstract ViewHolder CreateHolder(View view);

	protected abstract void BindHolder(ViewHolder holder);



	public class CheckableLayout extends FrameLayout implements Checkable {
		private boolean mChecked;

		public CheckableLayout(Context context) {
			super(context);
		}

		public void setChecked(boolean checked)
		{
			mChecked = checked;
			setBackgroundColor(checked ?
					0xFFFF0000
					: 0xFF000000);
		}

		public boolean isChecked()
		{
			return mChecked;
		}

		public void toggle()
		{
			setChecked(!mChecked);
		}

	}
}

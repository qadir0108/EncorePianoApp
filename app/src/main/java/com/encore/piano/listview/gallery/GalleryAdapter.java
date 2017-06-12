package com.encore.piano.listview.gallery;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.model.GalleryModel;

public class GalleryAdapter extends GalleryAdapterBase {

	ImageView Image;
	TextView ConsignmentId;
	TextView ImagePath;

	public GalleryAdapter(Context context, String consignmentId, ArrayList<String> consignemntIds) {
		super(context, R.layout.consignmentgalleryitem, consignmentId, consignemntIds);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ViewHolder CreateHolder(View view)
	{
		Image = (ImageView) view.findViewById(R.id.galleryImage);
		ConsignmentId = (TextView) view.findViewById(R.id.galleryConsignmentId);
		ImagePath = (TextView) view.findViewById(R.id.imagePath);
		GalleryViewHolder holder = new GalleryViewHolder(Image, ConsignmentId, ImagePath);
		return holder;
	}

	@Override
	protected void BindHolder(ViewHolder holder)
	{

		GalleryViewHolder h = (GalleryViewHolder) holder;

		h.ConsignmentId.setText(h.GalleryModel.getConsignmentId());
		h.ImagePath.setText(h.GalleryModel.getImagePath());
		Bitmap b = scaleImage(h.GalleryModel.getImagePath().substring(7));
		h.ImageThumb.setImageBitmap(b);

		//h.ImageThumb.setImageURI(Uri.parse(h.GalleryModel.getImagePath()));
	}

	private Bitmap scaleImage(String filePath)
	{
		// Get image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);
		//		BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 200;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true)
		{
			if (width_tmp / 2 < REQUIRED_SIZE
					&& height_tmp / 2 < REQUIRED_SIZE)
			{
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeFile(filePath, o2);
	}

	public void removeItemAt(int i)
	{
		Images.remove(i);

	}

	public void removeAll(ArrayList<GalleryModel> imagesToDelete)
	{
		Images.removeAll(imagesToDelete);

	}
}

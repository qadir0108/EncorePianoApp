package com.encore.piano.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.encore.piano.R;
import com.encore.piano.server.Service;

public class Image extends Activity {

	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);

		String path = getIntent().getExtras().getString(Service.CONSIGNMENT_IMAGE);

		image = (ImageView) findViewById(R.id.consignmentImageLargeImageView);
		//image.setImageURI(Uri.parse(path));

		Bitmap myBitmap = scaleImage(path.substring(7));

		image.setImageBitmap(myBitmap);

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
}

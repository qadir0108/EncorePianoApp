package com.encore.piano.logic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.encore.piano.R;
import com.encore.piano.model.Dimension;
import com.encore.piano.model.ImageModel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;


public class ImageLoader 
{	
	
	
	FragmentActivity activity;
	public ImageLoader(FragmentActivity activity){
		this.activity = activity;
	}
	
	public ImageLoader(){}

	public static synchronized Bitmap LoadImageBitmap(String url)
	{
		Bitmap b=null;
		
		try {
			URL imageUrl=new URL(url);
			InputStream is = (InputStream)imageUrl.getContent();
			b=BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return b;
	}
	

	
	public static void setImage(Activity activity, Bitmap temp, ImageView a) {
		Dimension d = calculateAscpect(activity, temp.getHeight(), temp.getWidth());

		a.getLayoutParams().height=d.height;
		a.getLayoutParams().width=d.width;
		a.setImageBitmap(temp);
	}
	

	
	public static Dimension calculateAscpect(Activity activity, int height, int width) {
		DisplayMetrics display = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(display);
		float aspect = (float)width/(float)height;
		float w=(float)display.widthPixels;
		float h=w/aspect;
		int displayHeight=(int)h;
		int displayWidth=(int)w;
		
		Dimension d = new Dimension();
		d.height = displayHeight;
		d.width = displayWidth;		
		return d;	
	}
	
	public static synchronized void LoadAsyncImageModel(ImageModel model, FragmentActivity activity)
	{
		Thread t = new Thread(new LoadImageAsync(model, activity));
		t.start();		
	}
	
	static class LoadImageAsync implements Runnable
	{

		ImageModel Model;
		FragmentActivity Activity;
		public LoadImageAsync(ImageModel model, FragmentActivity activity){
			Model = model;
			Activity = activity;
		}
		
		@Override
		public void run() {
			try {			
				Model.Bitmap=null;
				
				Model.ImageUrl = Model.ImageUrl.replace(" ", "%20");
				URL imageUrl = new URL(Model.ImageUrl);
				InputStream is = (InputStream)imageUrl.getContent();
				Model.Bitmap=BitmapFactory.decodeStream(is);
				if(Model.Bitmap == null)
					Model.Bitmap = BitmapFactory.decodeResource(Activity.getResources(), R.drawable.ic_launcher);
				is.close();
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Model.ImageView.setImageBitmap(Model.Bitmap);
				}
			});
			
		}
		
	}
	
	
	static class LoadImage extends AsyncTask<ImageModel, Void, ImageModel>
	{

		@Override
		protected ImageModel doInBackground(ImageModel... params) {
			params[0].Bitmap = LoadImageBitmap(params[0].ImageUrl);
			return params[0];
		}
		
		@Override
		protected void onPostExecute(ImageModel result) {
			result.ImageView.setImageBitmap(result.Bitmap);
			super.onPostExecute(result);
		}
		
	}
	
	public static LinearLayout ResizeLinearLayoutInAspectOfBackground(Drawable drawable, LinearLayout linearLayout, Activity activity)
	{
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Dimension dimms = ImageLoader.calculateAscpect(activity, h, w);
		linearLayout.getLayoutParams().width = dimms.width;
		linearLayout.getLayoutParams().height = dimms.height;
		
		return linearLayout;
	}
	
	public static RotateAnimation GetRotateAnimation()
	{	
		RotateAnimation rotateAnimation1 = new RotateAnimation(0, 360,	Animation.RELATIVE_TO_SELF, 0.5f,	Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation1.setInterpolator(new LinearInterpolator());
		rotateAnimation1.setDuration(1000);
		rotateAnimation1.setRepeatCount(-1);		
		return rotateAnimation1;
	}
	
	public static String DecodeBitmapToString(String path){	

		path = path.replace("file:///", "");
		Bitmap b = BitmapFactory.decodeFile(path);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();		
		b.compress(Bitmap.CompressFormat.PNG, 90, out);
		
		Bitmap compressed = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.toByteArray().length);
		
		
		return DecodeBitmapToString(compressed);

	}
	
	public static String DecodeBitmapToString(Bitmap bitmap){		
		
		String bitmapAsString = "";
		if(bitmap != null){
			ByteArrayOutputStream outS = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, outS);
			bitmapAsString = Base64.encodeToString(outS.toByteArray(), Base64.DEFAULT);
		}
		return "image";
	}
	
}

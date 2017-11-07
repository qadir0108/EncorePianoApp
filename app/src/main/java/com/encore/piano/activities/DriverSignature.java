package com.encore.piano.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.encore.piano.R;
import com.encore.piano.data.StringConstants;
import com.encore.piano.server.Service;
import com.encore.piano.logic.TaskQueue;
import com.encore.piano.exceptions.EmptyAuthTokenException;
import com.encore.piano.interfaces.ProgressUpdateListener;
import com.encore.piano.model.ProgressUpdateModel;
import com.encore.piano.util.FileUtility;

public class DriverSignature extends Activity implements ProgressUpdateListener {

	LinearLayout signatureLayoutContent;
	signature mSignature;
	Button ClearButton, SaveButton;
	//public static String tempDir;
	public int count = 1;
	private Bitmap mBitmap;
	View mView;
	//File filePath;
	
	private String uniqueId;
	
	ActionBar actionBar;
	String tempDir;
	String fileName = UUID.randomUUID().toString() + ".png";
	File filePath;

	NotificationManager notificationManager = null;
	Builder mBuilder = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.driver_signature);
		
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		tempDir = FileUtility.getDriversSignDirectory();
		filePath = new File(tempDir,fileName);
		
		prepareDirectory();
		
		signatureLayoutContent = (LinearLayout) findViewById(R.id.signatureContentLayout);
		mSignature = new signature(this, null);
		mSignature.setBackgroundColor(Color.WHITE);
		signatureLayoutContent.addView(mSignature, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ClearButton = (Button)findViewById(R.id.clearbutton);
		SaveButton = (Button)findViewById(R.id.savebutton);
		SaveButton.setEnabled(false);
		mView = signatureLayoutContent;	
		
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		ClearButton.setOnClickListener(new OnClickListener() 
		{ 
			public void onClick(View v) 
			{
				mSignature.clear();
				SaveButton.setEnabled(false);
			}
		});
	
		SaveButton.setOnClickListener(new OnClickListener() 
		{ 
			public void onClick(View v) 
			{
				onSaveOptionClick();
			}
		});		
	}
	
	private boolean prepareDirectory() 
	{
		try
		{
			if (makedirs()) 
			{
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Could not initiate File System.. Is SDCard mounted properly?", 1000).show();
			return false;
		}
	}
	
	private boolean makedirs() 
	{
		File tempdir = new File(tempDir);
		if (!tempdir.exists())
		tempdir.mkdirs();

		return (tempdir.isDirectory());
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.sign, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.signaturesavemenuitem:
				onSaveOptionClick();
			break;
			case R.id.signatureclearmenuitem:
				onClearOptionClick();
				break;
			case android.R.id.home:				
				DriverSignature.this.finish();
				break;
			default:
				break;
		}
	
		return super.onOptionsItemSelected(item);
	}
	
	private void onSaveOptionClick(){
		mView.setDrawingCacheEnabled(true);
		mSignature.save(mView);
		//new SendSignature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
		
		try {
			Service.loginService.ActivateLoginData();
		} catch (EmptyAuthTokenException e) {
			ShowMessage("Error", "doLogin session not saved. Error occured.");
		}
		
		TaskQueue.getQueue().add(TaskQueue.EnumTask.SendSignature);
		
		Intent i = new Intent(DriverSignature.this, com.encore.piano.activities.StartScreen.class);
		i.putExtra("filePath", filePath.getAbsolutePath());
		i.putExtra("fileName", fileName);
		startActivity(i);
		
		setResult(RESULT_OK);
		finish();

	}
	
	private void onClearOptionClick(){
		mSignature.clear();
		//btnSave.setEnabled(false);
	}
	
	@Override
	protected void onDestroy() {
		Log.w("GetSignature", "onDestory");
		super.onDestroy();
	}

	public class signature extends View 
	{
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();
		
		private float lastTouchX;
		private float lastTouchY;
		private final RectF dirtyRect = new RectF();
		
		public signature(Context context, AttributeSet attrs) 
		{
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}
	
		public void save(View v) 
		{
			if(mBitmap == null)
			{
				mBitmap = Bitmap.createBitmap (signatureLayoutContent.getWidth(), signatureLayoutContent.getHeight(), Bitmap.Config.RGB_565);
			}
			
			Canvas canvas = new Canvas(mBitmap);
			
			try
			{
				SaveToCard(filePath, v, canvas);
			
			}
			catch(Exception e) 
			{ 
				Log.v("log_tag", e.toString()); 
			} 
		}


		private void SaveToCard(File path, View v, Canvas canvas) throws IOException{
			FileOutputStream mFileOutStream;
			mFileOutStream = new FileOutputStream(path);			
			
			v.draw(canvas); 
			
			mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);				
			mFileOutStream.flush();
			mFileOutStream.close();
		}
		
		public void clear() 
		{
			path.reset();
			invalidate();
		}
		
		@Override
		protected void onDraw(Canvas canvas) 
		{
			canvas.drawPath(path, paint);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) 
		{
			float eventX = event.getX();
			float eventY = event.getY();
			SaveButton.setEnabled(true);
			
			switch (event.getAction()) 
			{
				case MotionEvent.ACTION_DOWN:
				path.moveTo(eventX, eventY);
				lastTouchX = eventX;
				lastTouchY = eventY;
				return true;
				
				case MotionEvent.ACTION_MOVE:
				
				case MotionEvent.ACTION_UP:
				
				resetDirtyRect(eventX, eventY);
				int historySize = event.getHistorySize();
				for (int i = 0; i < historySize; i++) 
				{
					float historicalX = event.getHistoricalX(i);
					float historicalY = event.getHistoricalY(i);
					expandDirtyRect(historicalX, historicalY);
					path.lineTo(historicalX, historicalY);
				}
				path.lineTo(eventX, eventY);
				break;
				
				default:
					debug("Ignored touch event: " + event.toString());
				return false;
			}
			
			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
			(int) (dirtyRect.top - HALF_STROKE_WIDTH),
			(int) (dirtyRect.right + HALF_STROKE_WIDTH),
			(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
			
			lastTouchX = eventX;
			lastTouchY = eventY;
			
			return true;
		}
		
		private void debug(String string){
		}
		
		private void expandDirtyRect(float historicalX, float historicalY) 
		{
			if (historicalX < dirtyRect.left) 
			{
				dirtyRect.left = historicalX;
			} 
			else if (historicalX > dirtyRect.right) 
			{
				dirtyRect.right = historicalX;
			}
			
			if (historicalY < dirtyRect.top) 
			{
				dirtyRect.top = historicalY;
			} 
			else if (historicalY > dirtyRect.bottom) 
			{
				dirtyRect.bottom = historicalY;
			}
		}
		
		private void resetDirtyRect(float eventX, float eventY) 
		{
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}	
		
	}
	
	ProgressDialog progressDialog = null;	
	
	@Override
	protected void onPause()
	{
		//Service.SignatureService.UnRegisterProgressUpdateListener();
		super.onPause();
	}
	
	@Override
	public void OnProgressUpdateListener(ProgressUpdateModel model) {
//		if(progressDialog == null || !progressDialog.isShowing())
//		{		
//			progressDialog.setCancelable(false);
//			UpdateProgressDialog(receivers);
//			progressDialog.show();
//		}
//		else
//			UpdateProgressDialog(receivers);
		
	}
	
	private void UpdateProgressDialog(final ProgressUpdateModel model)
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() 
			{
				progressDialog.setTitle(model.getTitle());
				progressDialog.setMessage(model.getMessage());
			}
		});
	}
	
	private void updateProgressNotification(final int notificationID, final int total, final int step, final String title, final String message)
	{
		int stepPercentage = step / total * 100;
		
		mBuilder.setProgress(100, stepPercentage, false);
		mBuilder.setContentTitle(title);
	    mBuilder.setContentText(message);
	    
        // Displays the progress bar for the first time.
        notificationManager.notify(notificationID, mBuilder.build());
        
        if(stepPercentage == 100)
        {
            // Removes the progress bar
            mBuilder.setProgress(0,0,false);
            
            notificationManager.notify(notificationID, mBuilder.build());
        	
			Intent i = new Intent(DriverSignature.this, com.encore.piano.activities.StartScreen.class);
			startActivity(i);
			DriverSignature.this.finish();
			
        }
       
	}
	
	private void ShowMessage(String title, String message){
		
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(DriverSignature.this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	private void ShowNotificationMessage(final int notificationID, final String title, String message){
		
		mBuilder.setContentTitle(title);
		// When the loop is finished, updates the notification
        mBuilder.setContentText(message);
        // Removes the progress bar
        mBuilder.setProgress(0,0,false);
        
        notificationManager.notify(notificationID, mBuilder.build());
	}

}
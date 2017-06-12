package com.encore.piano.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SignatureView extends View {

	private static final float STROKE_WIDTH = 5f;
	private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
	private Paint paint = new Paint();
	private Path path = new Path();

	private float lastTouchX;
	private float lastTouchY;
	private final RectF dirtyRect = new RectF();
	private Bitmap mBitmap;
	private Button saveButton;

	public SignatureView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeWidth(STROKE_WIDTH);
		setDrawingCacheEnabled(true);
	}

	public SignatureView(Context context) {
		super(context);
	}

	public SignatureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setSaveButton(Button b)
	{
		saveButton = b;
	}

	public void SaveToCard(File path) throws IOException
	{
		if (mBitmap == null)
			mBitmap = getDrawingCache();

		FileOutputStream mFileOutStream;
		mFileOutStream = new FileOutputStream(path);
		/*
		this.draw(canvas);

		mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
		mFileOutStream.flush();
		mFileOutStream.close();
		*/

		mFileOutStream = new FileOutputStream(path);
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
		if (saveButton != null)
			saveButton.setEnabled(true);

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

	private void debug(String string)
	{
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

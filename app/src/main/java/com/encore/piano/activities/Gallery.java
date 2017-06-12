package com.encore.piano.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.db.Database;
import com.encore.piano.services.GalleryService;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.listview.gallery.GalleryAdapter;
import com.encore.piano.model.GalleryModel;

public class Gallery extends Activity implements OnItemClickListener, OnClickListener {

	GridView GalleryView;
	Button AddButton;

	GalleryAdapter Adapter;
	String consignmentId;
	ArrayList<String> consignmentIds = null;
	Uri imageUri = null;

	String fileName = "";
	boolean isMultiselect = false;

	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consignmentgallery);

//		actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayHomeAsUpEnabled(true);

		GalleryView = (GridView) findViewById(R.id.galleryGridView);
		AddButton = (Button) findViewById(R.id.imageAddButton);

		GalleryView.setOnItemClickListener(this);
		AddButton.setOnClickListener(this);

		GalleryView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
		GalleryView.setMultiChoiceModeListener(new MultiChoiceModeListener());

		if (getIntent().getExtras().getString(ServiceUtility.CONSIGNMENT_INTENT_KEY) != null)
			consignmentId = getIntent().getExtras().getString(ServiceUtility.CONSIGNMENT_INTENT_KEY);
		else
			consignmentIds = getIntent().getStringArrayListExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY_MULTI);

		new FetchImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.topmenu_consignment_change_status, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId()) {

		case R.id.backtochangestatus:
			Gallery.this.finish();
			break;

		//			case R.id.galleryaddmenuitem:
		//				onAddOptionClick();
		//			break;
		//			case android.R.id.home:				
		//				Gallery.this.finish();
		//				break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.imageAddButton:
			onAddOptionClick();
			break;
		default:
			break;
		}

	}

	class FetchImages extends AsyncTask<String, Void, Object>
	{

		String consignmentId;

		@Override
		protected Object doInBackground(String... params)
		{

			consignmentId = params[0];

			try
			{
				if (ServiceUtility.galleryService == null)
					ServiceUtility.galleryService = new GalleryService(Gallery.this);
			} catch (UrlConnectionException e)
			{
				return e;
			} catch (JSONException e)
			{
				return e;
			} catch (JSONNullableException e)
			{
				return e;
			} catch (NotConnectedException e)
			{
				return e;
			} catch (NetworkStatePermissionException e)
			{
				return e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object result)
		{
			if (result instanceof Exception)
			{
				Toast.makeText(Gallery.this, result.getClass().getName(), Toast.LENGTH_LONG).show();
			}
			else
			{
				Adapter = new GalleryAdapter(Gallery.this, consignmentId, consignmentIds);
				GalleryView.setAdapter(Adapter);
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3)
	{

		String fullpath = ((TextView) view.findViewById(R.id.imagePath)).getText().toString();
		Intent i = new Intent(Gallery.this, com.encore.piano.activities.Image.class);
		i.putExtra(ServiceUtility.CONSIGNMENT_IMAGE, fullpath);
		startActivity(i);
	}

	private void onAddOptionClick()
	{
		fileName = UUID.randomUUID().toString() + ".jpg";
		String imagesPath = "";

		if (consignmentIds == null)
			imagesPath = ServiceUtility.ImagesRootFolder + String.valueOf(consignmentId) + "/";
		else
			imagesPath = Environment.getExternalStorageDirectory() + "/Androidpod/ConsignmentImages/MULTISELECTED/";

		File imageDir = new File(imagesPath);
		if (!imageDir.exists())
		{
			imageDir.mkdirs();
		}

		File image = new File(imagesPath + fileName);
		imageUri = Uri.fromFile(image);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, ServiceUtility.IMAGE_CAPTURE);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.remove("imageuri");
		if (imageUri != null)
			outState.putString("imageuri", imageUri.toString());
		else
			outState.putString("imageuri", "");

		if (consignmentIds != null)
			outState.putBoolean("isMultiselect", true);
		else
			outState.putBoolean("isMultiselect", false);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		Log.d("gallery", "savedinstance" + savedInstanceState.getString("imageuri") + "#");
		String uri = savedInstanceState.getString("imageuri");
		if (!uri.equals(""))
		{
			imageUri = Uri.parse(uri);

		}

		isMultiselect = savedInstanceState.getBoolean("isMultiselect");

		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		switch (requestCode)
		{
		case ServiceUtility.IMAGE_CAPTURE:
			Log.d("gallery", "onactivity");
			String path = "";
			if (imageUri != null)
			{
				path = imageUri.toString();
				try
				{
					int height = 534;
					int width = 400;

					Bitmap photo = BitmapFactory.decodeFile(imageUri.getPath());
					Bitmap scaledphoto = null;
					scaledphoto = Bitmap.createScaledBitmap(photo, width, height, true);

					try
					{
						FileOutputStream out = new FileOutputStream(imageUri.getPath());
						scaledphoto.compress(Bitmap.CompressFormat.JPEG, 90, out);
					} catch (Exception e)
					{
						e.printStackTrace();
					}

					Log.d("gallery", "imagepath " + path);

					if (isMultiselect)
					{
						for (String consignmentId : consignmentIds)
						{
							//String filePath = CopyImage(path, String.valueOf(this.consignmentId));
							ServiceUtility.galleryService.WriteImageInfo(consignmentId, path, ServiceUtility.GetMilisTimeToString()); //we have to allow inserting same path in db so milis is primaty key
						}

						DeleteSharedImage(path);
					}
					else
						ServiceUtility.galleryService.WriteImageInfo(consignmentId, path, path); //second path variable is used as primary key reference

				} catch (DatabaseUpdateException e)
				{
					//Toast.makeText(Gallery.this, "Error occured. ImageInfo not written to database.", Toast.LENGTH_LONG).show();
				}
				//					catch (FileNotFoundException e) {
				//						// TODO Auto-generated catch block
				//						e.printStackTrace();
				//					} catch (IOException e) {
				//						// TODO Auto-generated catch block
				//						e.printStackTrace();
				//					}
				new FetchImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);
			}
			else
				Toast.makeText(Gallery.this, "Error occured. no image..", Toast.LENGTH_LONG).show();

			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String GetConsignmentImageFileName(String path, String consignmentId)
	{
		int indexOfLastSlash = path.lastIndexOf('/');
		String filename = path.substring(indexOfLastSlash + 1);

		//		int indexOfDirectory = path.indexOf("/MULTI");
		//		String pathPrefix = path.subSequence(0, indexOfDirectory).toString();

		return filename;

	}

	private String CopyImage(String path, String consignmentId) throws IOException
	{
		File f = new File(path);

		String newFileName = GetConsignmentImageFileName(path, String.valueOf(consignmentId));

		String newFilePath = ServiceUtility.ImagesRootFolder + consignmentId + "/" + newFileName;
		File newFile = new File(newFilePath);
		String parentDirectory = newFile.getParent();

		File dir = new File(parentDirectory);
		dir.mkdir();

		if (!newFile.exists())
			newFile.createNewFile();

		FileUtils.copyFile(f, newFile);

		return newFileName;

	}

	private void DeleteSelectedImages(ArrayList<GalleryModel> selected)
	{

		for (GalleryModel g : selected)
			try
			{
				Database.DeleteImageFromConsignments(this, g);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//		DeleteSharedImage(g.getImagePath().substring(5));
	}

	private void DeleteSharedImage(String path)
	{
		File f = new File(path);
		if (f.exists())
			f.delete();
	}

	public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
		public boolean onCreateActionMode(ActionMode mode, Menu menu)
		{
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.delete, menu);

			mode.setTitle("Select Items");
			mode.setSubtitle("One itemService selected");
			return true;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu)
		{
			return true;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item)
		{
			String item1;

			if (item.getItemId() == R.id.del)
			{

				ArrayList<GalleryModel> ImagesToDelete = new ArrayList<GalleryModel>();
				SparseBooleanArray checkedItems = GalleryView.getCheckedItemPositions();
				if (checkedItems != null)
				{
					for (int i = 0; i < GalleryView.getCount(); i++)
					{
						int key = checkedItems.keyAt(i);

						if (checkedItems.get(i))
						{
							//	item1 = Adapter.getItem(
							//			checkedItems.keyAt(i)).toString();
							ImagesToDelete.add(Adapter.getItem(i));

						}
					}

					DeleteSelectedImages(ImagesToDelete);
					Adapter.removeAll(ImagesToDelete);
					Adapter.notifyDataSetChanged();
					mode.finish();
					return true;
				}

			}
			return false;
		}

		public void onDestroyActionMode(ActionMode mode)
		{
			GalleryView.clearChoices();
		}

		public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
				boolean checked)
		{
			int selectCount = GalleryView.getCheckedItemCount();
			switch (selectCount) {
			case 1:
				mode.setSubtitle("One itemService selected");
				break;
			default:
				mode.setSubtitle("" + selectCount + " items selected");
				break;
			}
		}

	}
}

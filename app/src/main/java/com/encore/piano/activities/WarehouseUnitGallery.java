package com.encore.piano.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import com.encore.piano.data.NumberConstants;
import com.encore.piano.db.ImageDb;
import com.encore.piano.enums.TakenLocationEnum;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.listview.gallery.GalleryAdapter;
import com.encore.piano.data.StringConstants;
import com.encore.piano.model.GalleryModel;
import com.encore.piano.server.GalleryService;
import com.encore.piano.server.Service;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.FileUtility;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class WarehouseUnitGallery extends AppCompatActivity implements OnItemClickListener, OnClickListener {

	GridView gvGallery;
	Button btnAdd;
	Button btnCancel;

	GalleryAdapter Adapter;
	String unitId;
	Uri imageUri = null;

	String fileName = "";
	boolean isMultiselect = false;

	String takenLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warehouse_gallery);

		gvGallery = (GridView) findViewById(R.id.gvGallery);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		gvGallery.setOnItemClickListener(this);
		btnAdd.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		gvGallery.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
		gvGallery.setMultiChoiceModeListener(new MultiChoiceModeListener());

		if (getIntent().getExtras().getString(StringConstants.INTENT_KEY_UNIT_ID) != null)
			unitId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_UNIT_ID);

		takenLocation = TakenLocationEnum.Warehouse.Value;

		new FetchImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, unitId);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.assignment_status, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.backtodetails:
				WarehouseUnitGallery.this.finish();
				break;
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
			case R.id.btnAdd:
				onAddOptionClick();
				break;
			case R.id.btnCancel:
				setResult(RESULT_CANCELED);
				finish();
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
				if (Service.galleryService == null)
					Service.galleryService = new GalleryService(WarehouseUnitGallery.this);
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
				Toast.makeText(WarehouseUnitGallery.this, result.getClass().getName(), Toast.LENGTH_LONG).show();
			}
			else
			{
				Adapter = new GalleryAdapter(WarehouseUnitGallery.this, consignmentId, takenLocation);
				gvGallery.setAdapter(Adapter);
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3)
	{
		String fullpath = ((TextView) view.findViewById(R.id.imagePath)).getText().toString();
		Intent i = new Intent(WarehouseUnitGallery.this, Image.class);
		i.putExtra(Service.CONSIGNMENT_IMAGE, fullpath);
		startActivity(i);
	}

	private void onAddOptionClick()
	{
		fileName = UUID.randomUUID().toString() + ".jpg";
        String filePath = FileUtility.getImagesDirectory(unitId);
        FileUtility.prepareDirectory(filePath);
		File image = new File(filePath + fileName);
		imageUri = Uri.fromFile(image);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, NumberConstants.REQUEST_CODE_IMAGE_CAPTURE);

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
		case NumberConstants.REQUEST_CODE_IMAGE_CAPTURE:
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

					GalleryModel model = new GalleryModel();
					model.setId(UUID.randomUUID().toString());
					model.setUnitId(unitId);
					model.setImagePath(path);
					model.setTakenAt(DateTimeUtility.getCurrentTimeStamp());
					Service.galleryService.writeImage(model); //second path variable is used as primary key reference

				} catch (DatabaseUpdateException e)
				{
					//Toast.makeText(AssignmentGallery.this, "Error occured. ImageInfo not written to database.", Toast.LENGTH_LONG).show();
				}

				new FetchImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, unitId);
			}
			else
				Toast.makeText(WarehouseUnitGallery.this, "Error occured. no image..", Toast.LENGTH_LONG).show();
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
		String newFilePath = FileUtility.getImagesDirectory(consignmentId) + "/" + newFileName;
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
				ImageDb.deleteGalleryItem(this, g);
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

			mode.setTitle("Select Picture");
			mode.setSubtitle("One picture selected");
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
				SparseBooleanArray checkedItems = gvGallery.getCheckedItemPositions();
				if (checkedItems != null)
				{
					for (int i = 0; i < gvGallery.getCount(); i++)
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
			gvGallery.clearChoices();
		}

		public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
				boolean checked)
		{
			int selectCount = gvGallery.getCheckedItemCount();
			switch (selectCount) {
			case 1:
				mode.setSubtitle("One picture selected");
				break;
			default:
				mode.setSubtitle("" + selectCount + " pictures selected");
				break;
			}
		}

	}
}

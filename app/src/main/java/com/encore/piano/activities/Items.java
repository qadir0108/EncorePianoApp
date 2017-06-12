package com.encore.piano.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.listview.item.ItemAdapter;

public class Items extends AppCompatActivity {

	
	ListView itemsListView;
	ItemAdapter adapter;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items);
		
		itemsListView = (ListView)findViewById(R.id.itemsListView);
		
		String consignmentId = getIntent().getExtras().getString(ServiceUtility.ITEM_INTENT_KEY);
		
		adapter = new ItemAdapter(this, consignmentId);
		itemsListView.setAdapter(adapter);
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.topmenu_consignment_detail, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId())
		{
//			case R.id.consignmentsavemenuitem:
//				onSaveOptionClicked();
//				break;
//			case android.R.id.home:
//				ConsignmentStatusManagement.this.finish();
//				break;
			case R.id.backtoconsignment:
				this.finish();
				break;
			default:
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}

package com.encore.piano.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.encore.piano.R;
import com.encore.piano.data.NumberConstants;
import com.encore.piano.data.StringConstants;
import com.encore.piano.listview.unit.UnitAdapter;
import com.encore.piano.util.Alerter;

public class AssignmentUnits extends AppCompatActivity {

	ListView listView;
	UnitAdapter adapter;
    String assignmentId, orderId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignment_units);
		
		listView = (ListView)findViewById(R.id.listViewUnits);
		assignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
		orderId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ORDER_ID);
		adapter = new UnitAdapter(this, assignmentId, orderId);
		listView.setAdapter(adapter);
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.units_details, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.backtodetails:
				this.finish();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

	 	if (requestCode == NumberConstants.REQUEST_CODE_LOAD_UNIT && resultCode == RESULT_OK)
        {
			assignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
			orderId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ORDER_ID);
            adapter = new UnitAdapter(this, assignmentId, orderId);
            listView.setAdapter(adapter);
            Alerter.success(AssignmentUnits.this, "Unit loaded successfully.");
        } else if (requestCode == NumberConstants.REQUEST_CODE_UNLOAD_UNIT && resultCode == RESULT_OK)
        {
            assignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
            orderId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ORDER_ID);
            adapter = new UnitAdapter(this, assignmentId, orderId);
            listView.setAdapter(adapter);
            Alerter.success(AssignmentUnits.this, "Unit delivered successfully.");
        }
    }
}

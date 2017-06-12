package com.encore.piano.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.listview.consignment.ConsignmentAdapter;
import com.encore.piano.util.CommonUtility;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONException;

import java.util.Collection;

import static com.encore.piano.activities.Consignment.consignmentIds;

/**
 * Created by Administrator on 8/6/2017.
 */

@SuppressLint("ValidFragment")
public class ConsignmentSectionFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener
{

    SupportMapFragment supportMap = null;

    public static final String ARG_SECTION_NUMBER = "section_number";
    private ListView consignmentListView;
    private ConsignmentAdapter adapter;
    private CheckBox chkShowMap;
    private int numberOfSelectedItems;

    android.view.ActionMode ActionMode = null;
    ActionBar actionBar;

    FragmentTransaction fragmentTransaction = null;

    boolean showingCompleted = false;
    boolean showCompleted = false;

    ConsignmentReceiver receiver = null;

    public ConsignmentSectionFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.runsheetlist, container, false);
        Bundle args = getArguments();
        //	((TextView) rootView.findViewById(android.R.id.text1)).setText(
        //			getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));

        consignmentListView = (ListView) rootView.findViewById(R.id.consignmentListView);

        consignmentListView.setOnItemClickListener(this);
        consignmentListView.setMultiChoiceModeListener(this);

        IntentFilter filter = new IntentFilter(CommonUtility.ACTION_UPDATE_CONSIGNMENTS);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ConsignmentReceiver();
        ConsignmentSectionFragment.this.getActivity().registerReceiver(receiver, filter);

        try
        {
            adapter = new ConsignmentAdapter(this.getActivity(), false);

        } catch (UrlConnectionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONNullableException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotConnectedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NetworkStatePermissionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        consignmentListView.setAdapter(adapter);

        chkShowMap = (CheckBox) rootView.findViewById(R.id.chkShowMap);
        chkShowMap.setOnCheckedChangeListener(this);

        int pos = args.getInt(ARG_SECTION_NUMBER);
        if (pos == 1)
            showCompleted(false);
        else
            showCompleted(true);

        return rootView;
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        if (receiver != null)
            ConsignmentSectionFragment.this.getActivity().unregisterReceiver(receiver);

        super.onDestroy();
    }

    private void showCompleted(boolean flag)
    {
        if (showCompleted)
            showCompleted = false;
        else
            showCompleted = true;

        showCompleted = flag;

			/*		if (showCompleted)
						showCompletedButton.setText("Show Undelivered");
					else
						showCompletedButton.setText("Show Completed");
			*/
        try
        {
            //if (showCompleted)
            adapter = new ConsignmentAdapter(this.getActivity(), flag);
				/*	else
					{
						ArrayList<ConsignmentModel> list = Database.GetAllConsignments(this);
						adapter = new ConsignmentAdapter(this, R.layout.runsheetlistitem, list);
					}
					*/
        } catch (UrlConnectionException e)
        {
            e.printStackTrace();
        } catch (JSONException e)
        {
            e.printStackTrace();
        } catch (JSONNullableException e)
        {
            e.printStackTrace();
        } catch (NotConnectedException e)
        {
            e.printStackTrace();
        } catch (NetworkStatePermissionException e)
        {
            e.printStackTrace();
        }
        consignmentListView.setAdapter(adapter);

    }

    @Override
    public void onClick(View arg0)
    {
        // TODO Auto-generated method stub

    }

    // multichoicelistener
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {

        if (item.getItemId() == R.id.sign)
        {
            Intent i = new Intent(this.getActivity(), CustomerCaptureSignature.class);
            i.putExtra(ServiceUtility.CUSTOMER_SIGN_CAPTURE_USERNAME, "unknown");

            i.putExtra(ServiceUtility.CUSTOMER_TRIP_STATUS, "D");
            i.putExtra(ServiceUtility.CUSTOMER_POD_STATUS, "R");

            //
            Collection<Integer> positions = adapter.getSelectedItemPositions()
                    .values();

            consignmentIds.clear();

            for (Integer integer : positions)
            {
                consignmentIds.add(adapter.getItem(integer).getId());
            }

            i.putStringArrayListExtra(ServiceUtility.CUSTOMER_SIGN_LIST, consignmentIds);
            getActivity().startActivityForResult(i, ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE_MULTI);

				/*	Intent i = new Intent(this.getActivity(),
							com.androidpodui.activities.ConsignmentStatusManagement.class);

					i.putStringArrayListExtra(ServiceUtility.CONSIGNMENT_ID_LIST_KEY,
							consignmentIds);
					startActivityForResult(
							i,
							ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE_MULTI);

							*/
            mode.finish();
        }
        else if (item.getItemId() == R.id.showmap)
        {
            supportMap = GoogleMapFragment.newInstance();
            //		fragmentTransaction = this.getFragmentManager().beginTransaction();
            //		fragmentTransaction.add(R.id.layoutMap, supportMap);
            //		fragmentTransaction.commit();
        }

        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {

        if (!showingCompleted)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.multiplesignin, menu);

            ActionMode = mode;
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
        // TODO Auto-generated method stub
        adapter.clearSelections();
        numberOfSelectedItems = 0;
        ActionMode = null;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode arg0, Menu arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean checked)
    {

        if (checked)
        {
            numberOfSelectedItems++;
            adapter.setSelection(position);
        }
        else
        {
            numberOfSelectedItems--;
            adapter.removeSelection(position);
        }

        mode.setTitle(numberOfSelectedItems + " selected Order(s)");
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
                            long arg3)
    {
        Intent i = new Intent(this.getActivity(),
                ConsignmentDetails.class);

        String id = ((TextView) view.findViewById(R.id.consignmentIdTextView))
                .getText().toString();
        i.putExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY, id);
        getActivity().startActivityForResult(i, ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE);
    }


    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1)
    {

        if (chkShowMap.isChecked())
        {
            //supportMap  = SupportMapFragment.newInstance();
            supportMap = GoogleMapFragment.newInstance();
            fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.layoutMap, supportMap);
            fragmentTransaction.commit();

        }
        else
        {
            fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.remove(supportMap);
            fragmentTransaction.commit();
        }
    }

    public class ConsignmentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (adapter != null)
            {
                try
                {
                    adapter = new ConsignmentAdapter(ConsignmentSectionFragment.this.getActivity(), showCompleted);
                } catch (UrlConnectionException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONNullableException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NotConnectedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NetworkStatePermissionException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                consignmentListView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
        }
    }

}
package com.encore.piano.afragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Administrator on 5/6/2017.
 */


public class GoogleMapFragment extends SupportMapFragment {

    private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";

    public static interface OnGoogleMapFragmentListener {
        //void onMapReady(GoogleMap map);
        void onMapReady();
    }

    public static GoogleMapFragment newInstance()
    {
        return new GoogleMapFragment();
    }

    public static GoogleMapFragment newInstance(GoogleMapOptions options)
    {
        Bundle arguments = new Bundle();
        arguments.putParcelable(SUPPORT_MAP_BUNDLE_KEY, options);

        GoogleMapFragment fragment = new GoogleMapFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mCallback = (OnGoogleMapFragmentListener) getActivity();
        } catch (ClassCastException e)
        {
            throw new ClassCastException(getActivity().getClass().getName() + " must implement OnGoogleMapFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mCallback != null)
        {
            //mCallback.onMapReady(getMapAsync(this));
            mCallback.onMapReady();
        }
        return view;
    }

    private OnGoogleMapFragmentListener mCallback;

}

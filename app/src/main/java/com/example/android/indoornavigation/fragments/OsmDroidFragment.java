package com.example.android.indoornavigation.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;

/**
 * Created by mm5623s on 05.06.15.
 */
public class OsmDroidFragment extends Fragment {
    private MapView mMapView;
    private ResourceProxy mResourceProxy;

    /*public static OsmDroidFragment newInstance() {
        OsmDroidFragment fragment = new MainActivity.OsmDroidFragment();
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());
        mMapView = new MapView(inflater.getContext(), 256, mResourceProxy);
        // Call this method to turn off hardware acceleration at the View level.
        // setHardwareAccelerationOff();
        return mMapView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Context context = this.getActivity();

        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);

        mMapView.getController().setZoom(15);
        mMapView.getController().setCenter(new GeoPoint(50.93699, 6.37461));

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
           mMapView.setTileSource(TileSourceFactory.MAPNIK);
        } catch (final IllegalArgumentException e) {
            mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        }
    }
}

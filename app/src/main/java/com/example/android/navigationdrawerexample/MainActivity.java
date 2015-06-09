/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavigationDrawerTitles;

    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    private MyWebView mMyWebView;

    public static Context getAppContext() {
        return MainActivity.appContext;
    }

    public static Context getBContext() {
        return MainActivity.bContext;
    }

    private static Context appContext;
    private static Context bContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.appContext = getApplicationContext();
        MainActivity.bContext = this.getBaseContext();

        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerTitles = getResources().getStringArray(R.array.navdrawer_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        dataList = new ArrayList<DrawerItem>();
        dataList.add(new DrawerItem("Map - WebView", R.drawable.ic_map));
        dataList.add(new DrawerItem("Navigation", R.drawable.ic_navigation));
        dataList.add(new DrawerItem("Map - osmdroid", R.drawable.ic_map));
        dataList.add(new DrawerItem("Search", R.drawable.ic_search));
        dataList.add(new DrawerItem("Switch layers", R.drawable.ic_layers));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);
        mDrawerList.setAdapter(adapter);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        // mDrawerList.setAdapter(new ArrayAdapter<String>(this,
        //        R.layout.drawer_list_item, mNavigationDrawerTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;
        FragmentManager fragmentManager;

        if(position==1) {
            createCustomDialog();
        } else if (position==3) {
            // CursorLoaderListFragment cursorLoaderListFragment = new CursorLoaderListFragment();
            // fragmentManager = getFragmentManager();
            // fragmentManager.beginTransaction().replace(android.R.id.content, cursorLoaderListFragment).commit();
            startSearchActivity();
        } else if (position==4) {
            startNavigationActivity();
        }
        else {
            if(position==0) {
                fragment = new MapFragment();
                Bundle args = new Bundle();
                args.putString(MapFragment.URL, "http://www.openstreetmap.org/#map=18/50.93699/6.37461");
                fragment.setArguments(args);
            } else if(position==2) {
                fragment = new OSMDroidMapFragment();
            }

            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mNavigationDrawerTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    private void startNavigationActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(this, SearchableActivity.class);
        startActivity(intent);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static class MapFragment extends Fragment {
        public static final String URL = "url";

        public MapFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);

            String url = getArguments().getString(URL);

            MyWebView webViewMap = (MyWebView) rootView.findViewById(R.id.map);

            webViewMap.getSettings().setJavaScriptEnabled(true);
//            webViewMap.loadUrl(url);
            webViewMap.loadUrl("https://testsystem.navi.fh-aachen.de/");

            return webViewMap;
        }
    }

    public static class OSMDroidMapFragment extends Fragment {

        public OSMDroidMapFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_osmdroidmap, container, false);

            //MapView map = (MapView) rootView.findViewById(R.id.osmdroid_map);
            final MapView map = new MapView(MainActivity.getAppContext(), 256);

            map.setBuiltInZoomControls(true);
            map.setMultiTouchControls(true);
            map.setMaxZoomLevel(25);

            IMapController mapController = map.getController();
            mapController.setZoom(18);
            GeoPoint startPoint = new GeoPoint(50.93999, 6.37061);
            mapController.setCenter(startPoint);

            final MapTileProviderBasic tileProvider = new MapTileProviderBasic(MainActivity.getAppContext());
            final ITileSource tileSource = new XYTileSource("FH Juelich", null, 16, 25, 256, ".png",
                    new String[]{"https://testsystem.navi.fh-aachen.de/osm_tiles/"});
            tileProvider.setTileSource(tileSource);
            final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, MainActivity.getBContext());
            tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
//            map.getOverlays().clear();
//            map.getOverlays().add(tilesOverlay);

            map.setTileSource(tileSource);

            // map.setTileSource(TileSourceFactory.MAPQUESTOSM);

            return map;
        }
    }

    public static class CursorLoaderListFragment extends ListFragment
            implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

        // This is the Adapter being used to display the list's data.
        SimpleCursorAdapter mAdapter;

        // If non-null, this is the current filter the user has provided.
        String mCurFilter;

        @Override public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Give some text to display if there is no data.  In a real
            // application this would come from a resource.
            setEmptyText("No phone numbers");

            // We have a menu item to show in action bar.
            setHasOptionsMenu(true);

            // Create an empty adapter we will use to display the loaded data.
            mAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_2, null,
                    new String[] { ContactsContract.Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS },
                    new int[] { android.R.id.text1, android.R.id.text2 }, 0);
            setListAdapter(mAdapter);

            // Start out with a progress indicator.
            setListShown(false);

            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(0, null, this);
        }

        @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Place an action bar item for searching.
            MenuItem item = menu.add("search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            SearchView sv = new SearchView(getActivity());
            sv.setOnQueryTextListener(this);
            item.setActionView(sv);
        }

        public boolean onQueryTextChange(String newText) {
            // Called when the action bar search text has changed.  Update
            // the search filter, and restart the loader to do a new query
            // with this filter.
            mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
            getLoaderManager().restartLoader(0, null, this);
            return true;
        }

        @Override public boolean onQueryTextSubmit(String query) {
            // Don't care about this.
            return true;
        }

        @Override public void onListItemClick(ListView l, View v, int position, long id) {
            // Insert desired behavior here.
            Log.i("FragmentComplexList", "Item clicked: " + id);
        }

        // These are the Contacts rows that we will retrieve.
        static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
                Contacts._ID,
                Contacts.DISPLAY_NAME,
                Contacts.CONTACT_STATUS,
                Contacts.CONTACT_PRESENCE,
                Contacts.PHOTO_ID,
                Contacts.LOOKUP_KEY,
        };

        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // This is called when a new Loader needs to be created.  This
            // sample only has one Loader, so we don't care about the ID.
            // First, pick the base URI to use depending on whether we are
            // currently filtering.
            Uri baseUri;
            if (mCurFilter != null) {
                baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
                        Uri.encode(mCurFilter));
            } else {
                baseUri = Contacts.CONTENT_URI;
            }

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + Contacts.DISPLAY_NAME + " != '' ))";
            return new CursorLoader(getActivity(), baseUri,
                    CONTACTS_SUMMARY_PROJECTION, select, null,
                    Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        }

        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // Swap the new cursor in.  (The framework will take care of closing the
            // old cursor once we return.)
            mAdapter.swapCursor(data);

            // The list should now be shown.
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        public void onLoaderReset(Loader<Cursor> loader) {
            // This is called when the last Cursor provided to onLoadFinished()
            // above is about to be closed.  We need to make sure we are no
            // longer using it.
            mAdapter.swapCursor(null);
        }
    }

    public void createCustomDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.navigation_dialog);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                mMyWebView = (MyWebView) findViewById(R.id.map);
                mMyWebView.setDrawPath(1);
                mMyWebView.draw(new Canvas());

                selectItem(0);
            }
        });

        dialog.show();
    }
}
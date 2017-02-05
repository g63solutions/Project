package com.zmediaz.apps.fragtry;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.stetho.Stetho;
import com.zmediaz.apps.fragtry.data.MovieContract;
import com.zmediaz.apps.fragtry.sync.MovieSyncUtils;

/**
 * Created by Computer on 2/4/2017.
 */



public class FragmentMain extends Fragment
implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callback {
        public void onItemSelected(Uri columnId);
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String[] MAIN_MOVIE_SCREEN = {
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry._ID
    };

    public static final int INDEX_MOVIE_POSTER_PATH = 0;
    public static final int INDEX_MOVIE_ORIGINAL_TITLE = 1;
    public static final int INDEX_MOVIE_ID = 2;

    private static final int MOVIE_LOADER_INT = 7;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(getActivity());

        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.yout_movie, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_display);

        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(getActivity(), this);

        mRecyclerView.setAdapter(mMovieAdapter);

        showLoading();

        getActivity().getSupportLoaderManager().initLoader(MOVIE_LOADER_INT, null, this);

        MovieSyncUtils.initialize(getActivity());

        return rootView;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle args) {
        switch (loaderId) {

            case MOVIE_LOADER_INT:
                /* URI for all rows of weather data in our weather table */
                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;

                /*CursorLoader

                Added in API level 11
                CursorLoader (Context context,
                    Uri uri,
                    String[] projection,
                    String selection,
                    String[] selectionArgs,
                    String sortOrder)    */

                /*PARAMETERS

                URI-	Uri: The URI, using the content:// scheme, for the content to retrieve.

                PROJECTION-	String: A list of which columns to return. Passing null will
                return all columns, which is inefficient.

                SELECTION-	String: A filter declaring which rows to return, formatted as
                an SQL WHERE clause (excluding the WHERE itself). Passing null
                will return all rows for the given URI.

                SELECTION ARGS-	String: You may include ?s in selection, which will be
                replaced by the values from selectionArgs, in the order that they appear
                in the selection. The values will be bound as Strings.

                SORT ORDER-	String: How to order the rows, formatted as an SQL ORDER BY
                clause (excluding the ORDER BY itself). Passing null will use the default
                sort order, which may be unordered.*/

                return new CursorLoader(getActivity(),
                        movieQueryUri,
                        MAIN_MOVIE_SCREEN,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mMovieAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }


    @Override
    public void onClick(long columnId) {
        /*Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        Uri uriForTitleClicked = MovieContract.MovieEntry.buildMovieUriWithID(columnId);
        movieDetailIntent.setData(uriForTitleClicked);
        startActivity(movieDetailIntent);*/

        ((Callback) getActivity())
                .onItemSelected(MovieContract.MovieEntry.buildMovieUriWithID(columnId));


    }

    private void showMovieDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
   /* //TODO ON ActivityMain
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }*/

   /* //TODO ON ActivityMain
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();


        if (itemThatWasClickedId == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, Settings.class);
            startActivity(startSettingsActivity);
            return true;
        }
        // If you do NOT handle the menu click,
        // return super.onOptionsItemSelected to let Android handle the menu click
        return super.onOptionsItemSelected(item);
    }*/


    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    public void onStart() {
        super.onStart();

        /*TODO 3.5 Flag tied to this on change loder is restarted
         * If the preferences for location or units have changed since the user was last in
         * MainActivity, perform another query and set the flag to false.
         *
         * This isn't the ideal solution because there really isn't a need to perform another
         * GET request just to change the units, but this is the simplest solution that gets the
         * job done for now.
         */

        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");

            getActivity().getSupportLoaderManager().restartLoader(MOVIE_LOADER_INT, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }
}

/*
Toast.makeText(context, "This Is A Toast Android", Toast.LENGTH_SHORT)
        .show();*/
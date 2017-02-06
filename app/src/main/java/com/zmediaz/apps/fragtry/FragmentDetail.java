package com.zmediaz.apps.fragtry;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zmediaz.apps.fragtry.data.MovieContract;

/**
 * Created by Computer on 2/4/2017.
 */

/*      "backdrop_sizes": ["w300","w780","w1280","original"],
        "logo_sizes": ["w45","w92","w154","w300","w500","original"],
        "poster_sizes": ["w92","w154","w185","w342","w500","w780","original"],
        "profile_sizes": ["w45","w185","h632","original"],
        "still_sizes": ["w92","w185","w300","original"]*/




public class FragmentDetail extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String DETAIL_URI = "URI";

    private static final String MOVIE_SHARE_HASHTAG = " #DRIVETHRU";

    static final String POSTER_URL = "https://image.tmdb.org/t/p/w500/";

    private String mMovieSummary;

    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };

    public static final int INDEX_POSTER_PATH = 0;
    public static final int INDEX_OVERVIEW = 1;
    public static final int INDEX_RELEASE_DATE = 2;
    public static final int INDEX_MOVIE_ID = 3;
    public static final int INDEX_ORIGINAL_TITLE = 4;
    public static final int INDEX_BACKDROP_PATH = 5;
    public static final int INDEX_VOTE_AVERAGE = 6;


    /*private TextView mPosterPath;*/
    private ImageView mPosterPath;

    private TextView mOverview;
    private TextView mReleaseDate;
    private TextView mMovieId;
    private TextView mOriginalTitle;
    private TextView mBackdropPath;
    private TextView mVoteAverage;

    private static final int ID_DETAIL_LOADER = 2924;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(FragmentDetail.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.layout_fragment_detail, container, false);
        /*mPosterPath = (TextView) rootView.findViewById(R.id.poster_path);*/
        mPosterPath = (ImageView) rootView.findViewById(R.id.poster_path);

        mOverview = (TextView) rootView.findViewById(R.id.overview);
        mReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mMovieId = (TextView) rootView.findViewById(R.id.movie_id);
        mOriginalTitle = (TextView) rootView.findViewById(R.id.original_title);
        mBackdropPath = (TextView) rootView.findViewById(R.id.backdrop_path);
        mVoteAverage = (TextView) rootView.findViewById(R.id.vote_average);



        //if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        //getActivity().getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(mMovieSummary + MOVIE_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(getActivity(), Settings.class);
            startActivity(startSettingsActivity);
            return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

       if(null != mUri) {


           return new CursorLoader(getActivity(),
                   mUri,
                   MOVIE_DETAIL_PROJECTION,
                   null,
                   null,
                   null);
       }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }


        String poster_path = data.getString(INDEX_POSTER_PATH);
        /*mPosterPath.setText(poster_path);*/
        Picasso.with(getActivity())
                .load(POSTER_URL+poster_path)
                .into(mPosterPath);

        String overview = data.getString(INDEX_OVERVIEW);
        mOverview.setText(overview);
        String release_date = data.getString(INDEX_RELEASE_DATE);
        mReleaseDate.setText(release_date);
        String movie_id = data.getString(INDEX_MOVIE_ID);
        mMovieId.setText(movie_id);
        String original_title = data.getString(INDEX_ORIGINAL_TITLE);
        mOriginalTitle.setText(original_title);
        String backdrop_path = data.getString(INDEX_BACKDROP_PATH);
        mBackdropPath.setText(backdrop_path);
        String vote_average = data.getString(INDEX_VOTE_AVERAGE);
        mVoteAverage.setText(vote_average);

        mMovieSummary = String.format("%s - %s - %s", original_title, release_date, vote_average);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

/*
35 35 35*/


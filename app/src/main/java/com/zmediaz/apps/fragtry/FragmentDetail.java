package com.zmediaz.apps.fragtry;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.florent37.picassopalette.PicassoPalette;
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
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public interface buttonClickedListener {
        public void onButtonClicked();
    };

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


    private ImageView mBackdropPath;
    private ImageView mPosterPath;

    private TextView mOverview;
    private TextView mReleaseDate;
    private TextView mMovieId;
    private TextView mOriginalTitle;
    private Button mFavorite;


    private TextView mVoteAverage;

    private View rootView;



    private NestedScrollView mNest;

    private static final int ID_DETAIL_LOADER = 2924;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(FragmentDetail.DETAIL_URI);
        }


        rootView = inflater.inflate(R.layout.layout_fragment_detail, container, false);
        /*mPosterPath = (TextView) rootView.findViewById(R.id.poster_path);*/
        mBackdropPath = (ImageView) getActivity().findViewById(R.id.backdrop_path);
        mPosterPath = (ImageView) rootView.findViewById(R.id.poster_path);

        mOverview = (TextView) rootView.findViewById(R.id.overview);
        mReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mMovieId = (TextView) rootView.findViewById(R.id.movie_id);
        rootView.setTag(R.id.poster_path, mMovieId);
        mOriginalTitle = (TextView) rootView.findViewById(R.id.original_title);

        mFavorite = (Button) rootView.findViewById(R.id.favorite_button);
        mFavorite.setOnClickListener(this);

        mVoteAverage = (TextView) rootView.findViewById(R.id.vote_average);
        mNest = (NestedScrollView) getActivity().findViewById(R.id.nested_scroll_view);



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

        if (null != mUri) {


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

        String backdrop_path = data.getString(INDEX_BACKDROP_PATH);
        /*mBackdropPath.setText(backdrop_path);*/
        /*Picasso.with(getActivity())
                .load(POSTER_URL+backdrop_path)
                .into(mBackdropPath);*/

        Picasso.with(getActivity()).load(POSTER_URL + backdrop_path).into(mBackdropPath,
                PicassoPalette.with(POSTER_URL + backdrop_path, mBackdropPath)
                        .use(PicassoPalette.Profile.MUTED_LIGHT)
                        .intoBackground(mNest)
                        .use(PicassoPalette.Profile.MUTED_LIGHT)
                        .intoTextColor(mOverview, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                        .use(PicassoPalette.Profile.VIBRANT_LIGHT)
                        .intoTextColor(mOriginalTitle, PicassoPalette.Swatch.TITLE_TEXT_COLOR)
                        /*.intoTextColor(textView)*/

                        /*.use(PicassoPalette.Profile.VIBRANT)
                        .intoBackground(titleView, PicassoPalette.Swatch.RGB)
                        .intoTextColor(titleView, PicassoPalette.Swatch.BODY_TEXT_COLOR)*/
        );


        String poster_path = data.getString(INDEX_POSTER_PATH);
        /*mPosterPath.setText(poster_path);*/
        Picasso.with(getActivity())
                .load(POSTER_URL + poster_path)
                .into(mPosterPath);

        String overview = data.getString(INDEX_OVERVIEW);
        mOverview.setText(overview);
        String release_date = data.getString(INDEX_RELEASE_DATE);
        mReleaseDate.setText(release_date);
        String movie_id = data.getString(INDEX_MOVIE_ID);
        mMovieId.setText(movie_id);
        rootView.setTag(R.id.poster_path, movie_id);

        String original_title = data.getString(INDEX_ORIGINAL_TITLE);
        mOriginalTitle.setText(original_title);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(original_title);

        String vote_average = data.getString(INDEX_VOTE_AVERAGE);
        mVoteAverage.setText(vote_average);

        mMovieSummary = String.format("%s - %s - %s", original_title, release_date, vote_average);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


     @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorite_button:
                buttonClick(v);
                Toast.makeText(getActivity().getApplicationContext(), "This Is A Toast Android" + rootView.getTag(R.id.poster_path), Toast.LENGTH_SHORT)
                        .show();

                break;
            /*case R.id.another_view:
                someMethod(param);
                break;*/
        }
    }

    public void buttonClick(View v){
        ((buttonClickedListener) getActivity()).onButtonClicked();
        /*Toast.makeText(getActivity(), "This Is A Toast Android Mainn", Toast.LENGTH_SHORT)
                .show();*/
    }


}

/*
35 3
5 35*/


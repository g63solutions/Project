package com.zmediaz.apps.fragtry.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.zmediaz.apps.fragtry.R;
import com.zmediaz.apps.fragtry.data.MovieContract;
import com.zmediaz.apps.fragtry.data.MovieModel;
import com.zmediaz.apps.fragtry.utilities.NetworkUtils;
import com.zmediaz.apps.fragtry.utilities.TMDBJsonUtils;

import java.net.URL;
import java.util.concurrent.TimeUnit;


import static android.os.Looper.getMainLooper;

/**
 * Created by Computer on 1/18/2017.
 */

public class MovieUtils {

    private static final int SYNC_INTERVAL_HOURS = 3;
    /*private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.
            HOURS.toSeconds(SYNC_INTERVAL_HOURS);*/
    private static final int SYNC_INTERVAL_SECONDS = 30;


    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static Toast mToast;

    private static boolean sInitialized;

    private static final String MOVIE__SYNC_TAG = "movie-sync";


    static void addToFavorites(Context context, MovieModel movieModel) {
        /*Uri mFavoritesUri;*/
        ContentResolver favoritesContentResolver = context.getContentResolver();
        ContentValues mFavoritesCV = new ContentValues();

        ContentResolver movieContentResolver = context.getContentResolver();
        ContentValues mMovieCV = new ContentValues();

        mFavoritesCV.put(MovieContract.FavoritesEntry.COLUMN_POSTER_PATH,
                movieModel.getPosterPath());
        mFavoritesCV.put(MovieContract.FavoritesEntry.COLUMN_OVERVIEW,
                movieModel.getOverview());
        mFavoritesCV.put(MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE,
                movieModel.getReleaseDate());
        mFavoritesCV.put(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID,
                movieModel.getMovieId());
        mFavoritesCV.put(MovieContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE,
                movieModel.getOriginalTitle());
        mFavoritesCV.put(MovieContract.FavoritesEntry.COLUMN_BACKDROP_PATH,
                movieModel.getBackdropPath());
        mFavoritesCV.put(MovieContract.FavoritesEntry.COLUMN_VOTE_AVERAGE,
                movieModel.getVoteAverage());

        /*mMovieCV.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE,
                movieModel.getFavorites());*/
        mMovieCV.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE,
                "TRUE");

       /* mFavoritesUri = favoritesContentResolver.insert(*/
        final Uri returnUri;
        returnUri = favoritesContentResolver.insert(
                MovieContract.FavoritesEntry.CONTENT_URI,
                mFavoritesCV
        );


        String movieID = movieModel.getMovieId();
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieID).build();

        movieContentResolver.update(
                uri,
                mMovieCV,
                null,
                null

        );


        if (returnUri != null) {
            final Context tContext = context;
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText
                            (tContext, "Added to FAVORITES " + returnUri, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
        }


    }

    /*Deletes Favorites*/
    static void deleteFavorite(Context context, MovieModel movieModel) {

        String movieID = movieModel.getMovieId();

        String mSelectionClause = MovieContract.FavoritesEntry.COLUMN_MOVIE_ID;
        String[] mSelectionArgs = {movieID};
        Uri uri = MovieContract.FavoritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieID).build();

        int mRowsDeleted = 0;
        ContentResolver favoriteDeleteContentResolver = context.getContentResolver();

        mRowsDeleted = favoriteDeleteContentResolver.delete(
                uri,
                null,
                null
        );
        if (mRowsDeleted > 0) {
            final Context tContext = context;
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText
                            (tContext, "Deleted from FAVORITES ", Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
        }
    }

    public static String key;

    synchronized public static void movieSync(Context context) {

        try {

            key = context.getResources().getString(R.string.api);

            URL movieRequestUrl = NetworkUtils.buildUrl(context, key);

            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

            ContentValues[] movieValues = TMDBJsonUtils
                    .getSimpleMovieCVFromJson(jsonMovieResponse);

            if (movieValues != null && movieValues.length != 0) {

                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null
                );

                movieContentResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues

                );

                final Context cContext = context;
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (mToast != null) mToast.cancel();
                        mToast = Toast.makeText
                                (cContext, "This Is An Insert", Toast.LENGTH_SHORT);
                        mToast.show();


                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncMovieJob = dispatcher.newJobBuilder()
                .setService(MovieFirebaseJobService.class)
                .setTag(MOVIE__SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncMovieJob);

    }

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        Thread checkForEmpty = new Thread(new Runnable() {

            @Override
            public void run() {
                android.os.Debug.waitForDebugger();

                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                String[] projectionColumns = {MovieContract.MovieEntry._ID};
                Cursor cursor = context.getContentResolver().query(
                        movieQueryUri,
                        projectionColumns,
                        null,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }
                cursor.close();
            }


        });

        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {

        /*Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        context.startService(intentToSyncImmediately);*/

        Intent syncMovie = new Intent(context, MovieSyncIntentService.class);
        syncMovie.setAction(MovieSyncTask.ACTION_MOVIE_SYNC);
        context.startService(syncMovie);
    }
}

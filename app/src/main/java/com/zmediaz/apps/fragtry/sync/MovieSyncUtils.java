package com.zmediaz.apps.fragtry.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.zmediaz.apps.fragtry.data.MovieContract;

import java.util.concurrent.TimeUnit;

/**
 * Created by Computer on 1/18/2017.
 */

public class MovieSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.
            HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static Toast  mToast;

    private static boolean sInitialized;

    private static final String MOVIE__SYNC_TAG = "movie-sync";

    static void addDeleteFavorite(Context context){

        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, "This Is A Toast Android On The Main", Toast.LENGTH_SHORT);
        mToast.show();
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

        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}

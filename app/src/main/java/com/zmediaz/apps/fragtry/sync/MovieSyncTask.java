package com.zmediaz.apps.fragtry.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.zmediaz.apps.fragtry.data.MovieContract;
import com.zmediaz.apps.fragtry.utilities.NetworkUtils;
import com.zmediaz.apps.fragtry.utilities.TMDBJsonUtils;

import java.net.URL;

/**
 * Created by Computer on 1/18/2017.
 */

/*public class MovieSyncTask {


    synchronized public static void syncMovie(Context context, String key) {

        try {

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}*/

public class MovieSyncTask {
    public static final String ACTION_ADD_DELETE_FAVORITE = "add-delete-favorite";

    public static void executeTask(Context context, String action){
        if (ACTION_ADD_DELETE_FAVORITE.equals(action)){
            tAddDeleteFavorite(context);
        }
    }

    private static void tAddDeleteFavorite(Context context){
        MovieSyncUtils.addDeleteFavorite(context);

    }
}

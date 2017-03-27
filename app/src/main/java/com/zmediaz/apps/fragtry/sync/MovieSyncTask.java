package com.zmediaz.apps.fragtry.sync;

import android.content.Context;

import com.zmediaz.apps.fragtry.data.MovieModel;

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

    public static final String ACTION_DELETE_FAVORITE = "add-delete-favorite";
    public static final String ACTION_MOVIE_SYNC = "movie-sync";
    public static final String ACTION_ADD_FAVORITES = "addToFavorites";

    public static void executeTask(Context context, String action, MovieModel movieModel) {
        if (ACTION_DELETE_FAVORITE.equals(action)) {
            tDeleteFavorite(context, movieModel);
        } else if (ACTION_MOVIE_SYNC.equals(action)) {
            tMovieSync(context);
        } else if (ACTION_ADD_FAVORITES.equals(action)){
            tAddFavorites(context, movieModel);
        }



    }

    private static void tDeleteFavorite(Context context, MovieModel movieModel) {
        MovieUtils.deleteFavorite(context, movieModel);
    }

    private static void tMovieSync(Context context) {
        MovieUtils.movieSync(context);
    }

    private static void tAddFavorites(Context context, MovieModel movieModel){
        MovieUtils.addToFavorites(context, movieModel);
    }
}

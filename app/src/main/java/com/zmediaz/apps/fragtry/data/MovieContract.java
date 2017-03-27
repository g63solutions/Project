package com.zmediaz.apps.fragtry.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Computer on 1/6/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.zmediaz.apps.fragtry";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAVORITES = "favorites";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";


        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_IS_FAVORITE = "favorite";

        public static Uri buildMovieUriWithID(long columnId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(columnId))
                    .build();
        }
       /* public static Uri buildFavoriteMovieUriWithID(String columnId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(columnId)
                    .build();
        }*/

    }

    public static class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";


        public static Uri buildFavoritesUriWithID(long columnId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(columnId))
                    .build();

        }


    }

}

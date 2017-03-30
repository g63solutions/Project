package com.zmediaz.apps.fragtry.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Computer on 1/6/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 19;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*SPACES AFTER ALL THE GREEN OR THE SYNTAX MONSTER WILL EAT YOU*/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(" +

                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUMN_POSTER_PATH + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " STRING NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " STRING NOT NULL, " +
                        /*MovieContract.MovieEntry.COLUMN_IS_FAVORITE + " BOOLEAN DEFAULT FALSE, " +*/

                        " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE " + MovieContract.FavoritesEntry.TABLE_NAME + "(" +
                        MovieContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.FavoritesEntry.COLUMN_POSTER_PATH + " STRING NOT NULL, " +
                        MovieContract.FavoritesEntry.COLUMN_OVERVIEW+ " STRING DEFAULT LOVE, " +
                        MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, " +
                        MovieContract.FavoritesEntry.COLUMN_MOVIE_ID + " STRING NOT NULL, " +
                        MovieContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE + " STRING NOT NULL, " +
                        MovieContract.FavoritesEntry.COLUMN_BACKDROP_PATH + " STRING NOT NULL, " +
                        MovieContract.FavoritesEntry.COLUMN_VOTE_AVERAGE + " STRING NOT NULL, " +
                        MovieContract.FavoritesEntry.COLUMN_IS_FAVORITE + " BOOLEAN DEFAULT TRUE, " +


                        " UNIQUE (" + MovieContract.FavoritesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}

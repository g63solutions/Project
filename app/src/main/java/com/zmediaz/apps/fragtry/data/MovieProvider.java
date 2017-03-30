package com.zmediaz.apps.fragtry.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.R.id.content;

/**
 * Created by Computer on 1/9/2017.
 */

public class MovieProvider extends ContentProvider {

    public static final int POSITION = 6;

    public static final int CODE_MOVIE = 100;
    public static final int CODE_ADD_FAVORITE = 200;
    /*public static final int CODE_MOVIE_WITH_COLUMN_ID = 101;*/
    public static final int CODE_MOVIE_WITH_MOVIE_ID = 102;
    public static final int CODE_DELETE_FAVORITE_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES, CODE_ADD_FAVORITE);
        /*matcher.addURI(authority, MovieContract.PATH_FAVORITES, CODE_DELETE_FAVORITE_WITH_ID);*/

        /*matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#",
        CODE_MOVIE_WITH_COLUMN_ID);*/

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#",
                CODE_MOVIE_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/#",
                CODE_DELETE_FAVORITE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());

        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE: {
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            }
            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

           /* case CODE_MOVIE_WITH_COLUMN_ID: {

                String movieTitle = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{movieTitle};

                cursor = mOpenHelper.getReadableDatabase().query(

                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }*/

            case CODE_MOVIE_WITH_MOVIE_ID: {
               /* String movieTitle = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{movieTitle};

                cursor = mOpenHelper.getReadableDatabase().query(

                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;*/


                SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();

                _QB.setTables(MovieContract.MovieEntry.TABLE_NAME +
                        " LEFT OUTER JOIN " +
                        MovieContract.FavoritesEntry.TABLE_NAME +
                        " ON " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " +
                        MovieContract.FavoritesEntry.COLUMN_MOVIE_ID);

                String movieTitle = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{movieTitle};

                SQLiteDatabase db = mOpenHelper.getWritableDatabase();

                cursor = _QB.query(db,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);



                break;
            }

            case CODE_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;


            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        long insertReturn;
        Uri returnUri;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();


        switch (sUriMatcher.match(uri)) {

            case CODE_ADD_FAVORITE: {

                insertReturn = db.insert(MovieContract.FavoritesEntry.TABLE_NAME,
                        null, contentValues);
                if (insertReturn > 0) {
                    returnUri = MovieContract.FavoritesEntry.buildFavoritesUriWithID(insertReturn);

                } else
                    throw new android.database
                            .SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        /*getContext().getContentResolver().notifyChange(uri, null);*/
        getContext().getContentResolver().notifyChange(Uri.parse("content://com.zmediaz.apps.fragtry/movie"), null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE: {
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }

            case CODE_DELETE_FAVORITE_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieContract.FavoritesEntry.TABLE_NAME,
                        "f_movie_id=?",
                        new String[]{id}
                );


                break;
            }

            default:
                throw new UnsupportedOperationException("Unkown uri:" + uri);
        }
        if (numRowsDeleted != 0) {
            /*getContext().getContentResolver().notifyChange(uri, null);*/
            getContext().getContentResolver().notifyChange(Uri.parse("content://com.zmediaz.apps.fragtry/movie"), null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        int numRowsUpdated;


        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE_WITH_MOVIE_ID: {

                String id = uri.getPathSegments().get(1);
                numRowsUpdated = mOpenHelper.getWritableDatabase().update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        contentValues,
                        "movie_id=?",
                        new String[]{id}
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unkown uri:" + uri);
        }
        if (numRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);


        }

        return numRowsUpdated;
    }
}

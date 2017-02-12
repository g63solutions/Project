package com.zmediaz.apps.fragtry;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;



/**
 * Created by Computer on 12/29/2016.
 */

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final Context mContext;

    private static final int FIRST_MOVIE = 0;

    static final String POSTER_URL = "https://image.tmdb.org/t/p/w500/";

    final private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(long _id);
    }

    private Cursor mCursor;

    public MovieAdapter(@NonNull Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.layout_main_list_item, viewGroup, false);

        view.setFocusable(true);


        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        /*MOVIE SUMMARY*/
        /*String poster_path = mCursor.getString(MainActivity.INDEX_MOVIE_POSTER_PATH);*/
        String poster_path = mCursor.getString(FragmentMain.INDEX_MOVIE_POSTER_PATH);
        Context context = movieAdapterViewHolder.mPosterPath.getContext();
        Picasso.with(context)
                .load(POSTER_URL+poster_path)
                .into(movieAdapterViewHolder.mPosterPath);

        /*String original_title = mCursor.getString(MainActivity.INDEX_MOVIE_ORIGINAL_TITLE);

        String mMovieTextView = poster_path + " - " + original_title;

        movieAdapterViewHolder.mMovieTextView.setText(mMovieTextView);*/

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        /*final TextView mMovieTextView;*/
        final ImageView mPosterPath;

        MovieAdapterViewHolder(View view) {
            super(view);
            mPosterPath = (ImageView) view.findViewById(R.id.poster_path);
            /*mMovieTextView = (TextView) view.findViewById(R.id.tv_movie_data);*/
            view.setOnClickListener(this);
        }





        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            /*long movieID = mCursor.getLong(FragmentMain.INDEX__ID);*/
            long movieID = mCursor.getLong(FragmentMain.INDEX_MOVIE_MOVIE_ID);
           /* long movieID = mCursor.getLong(MainActivity.INDEX_MOVIE_ID);*/
            mClickHandler.onClick(movieID);

            /*String movie = mMovieTextView.getText().toString();
            mClickHandler.onClick(movie);*/
        }


    }
}









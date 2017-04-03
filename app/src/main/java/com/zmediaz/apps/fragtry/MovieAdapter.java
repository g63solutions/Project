package com.zmediaz.apps.fragtry;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;
import com.zmediaz.apps.fragtry.data.MovieModel;


/**
 * Created by Computer on 12/29/2016.
 */

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {





    MovieModel mMovieModel;

    private final Context mContext;

    private ImageButton mHeartButton;

    private static final int FIRST_MOVIE = 0;

    static final String POSTER_URL = "https://image.tmdb.org/t/p/w500/";

    final private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(long _id);
        void buttonClick(MovieModel movieModel, String situation);

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
        /*Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(mCursor));*/

        mCursor.moveToPosition(position);

        String favoriteState = mCursor.getString(FragmentMain.INDEX_IS_FAVORITE);
       /* if (favoriteState != null) {
            mHeartButton.setSelected(true);
        }else {
            mHeartButton.setSelected(false);
        }*/

        if (favoriteState == null) {
            mHeartButton.setSelected(false);
        }else if (favoriteState.equalsIgnoreCase("TRUE")){
            mHeartButton.setSelected(true);
        }else {
            mHeartButton.setSelected(false);
        }

        /*MOVIE SUMMARY*/
        /*String poster_path = mCursor.getString(MainActivity.INDEX_MOVIE_POSTER_PATH);*/
        String poster_path = mCursor.getString(FragmentMain.INDEX_MOVIE_POSTER_PATH);
        /*Context context = movieAdapterViewHolder.mPosterPath.getContext();*/
        Context context = movieAdapterViewHolder.mPosterPath.getContext();

        //Normal Picasso without the Palette
        /*Picasso.with(context)
                .load(POSTER_URL+poster_path)
                .into(movieAdapterViewHolder.mPosterPath);*/

        Picasso.with(context).load(POSTER_URL + poster_path).into(movieAdapterViewHolder.mPosterPath,
                PicassoPalette.with(POSTER_URL + poster_path, movieAdapterViewHolder.mPosterPath)
                        .use(PicassoPalette.Profile.VIBRANT)
                        .intoBackground(movieAdapterViewHolder.placeHolder)
                        .intoTextColor(movieAdapterViewHolder.mVoteAverage, PicassoPalette.Swatch.BODY_TEXT_COLOR)

                        /*.use(PicassoPalette.Profile.VIBRANT)
                        .intoBackground(titleView, PicassoPalette.Swatch.RGB)
                        .intoTextColor(titleView, PicassoPalette.Swatch.BODY_TEXT_COLOR)*/
        );

        String vote_average = mCursor.getString(FragmentMain.INDEX_MOVIE_VOTE_AVERAGE);

        String mVoteAverage = vote_average;

        movieAdapterViewHolder.mVoteAverage.setText(mVoteAverage);

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
        Log.v("Cursor MOVIE ADAPTER", DatabaseUtils.dumpCursorToString(mCursor));
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        /*final TextView mMovieTextView;*/
        /*final ImageView mPosterPath;*/
        final TextView mVoteAverage;
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        final ImageView mPosterPath;
        /*private ImageButton mHeartButton;*/



        MovieAdapterViewHolder(View view) {
            super(view);
            mHeartButton = (ImageButton) view.findViewById(R.id.selector_favorite);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            mVoteAverage = (TextView) view.findViewById(R.id.vote_average);
            mPosterPath = (ImageView) view.findViewById(R.id.poster_path);
            /*mMovieTextView = (TextView) view.findViewById(R.id.tv_movie_data);*/
            view.setOnClickListener(this);
            mHeartButton.setOnClickListener(this);
            mPosterPath.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {



            switch (view.getId()) {

                case R.id.selector_favorite: {
                    if (view.isSelected()) {
                        mClickHandler.buttonClick(makeMovieModel(view), "deleteFavoriteButton");
                        view.setSelected(false);
                        break;
                    } else {
                        mClickHandler.buttonClick(makeMovieModel(view), "addFavoriteButton");
                        view.setSelected(true);
                        break;
                    }
                }
                    case R.id.poster_path:{
                        movieID(view);
                    }

            }

            /*String movie = mMovieTextView.getText().toString();
            mClickHandler.onClick(movie);*/
        }



        /*public void movieID2(Context context){
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(6);
            *//*long movieID = mCursor.getLong(FragmentMain.INDEX__ID);*//*
            long movieID = mCursor.getLong(FragmentMain.INDEX_MOVIE_MOVIE_ID);
           *//* long movieID = mCursor.getLong(MainActivity.INDEX_MOVIE_ID);*//*
            mClickHandler.onClick(movieID);
        }*/

        public void defaultMovie() {

            mCursor.moveToPosition(3);
            /*long movieID = mCursor.getLong(FragmentMain.INDEX__ID);*/
            long movieID = mCursor.getLong(FragmentMain.INDEX_MOVIE_MOVIE_ID);
           /* long movieID = mCursor.getLong(MainActivity.INDEX_MOVIE_ID);*/
            mClickHandler.onClick(movieID);
        }


        public void movieID(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            /*long movieID = mCursor.getLong(FragmentMain.INDEX__ID);*/
            long movieID = mCursor.getLong(FragmentMain.INDEX_MOVIE_MOVIE_ID);
           /* long movieID = mCursor.getLong(MainActivity.INDEX_MOVIE_ID);*/
            mClickHandler.onClick(movieID);
        }

        public MovieModel makeMovieModel(View view) {
            mMovieModel = new MovieModel();

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            String is_favorite = mCursor.getString(FragmentMain.INDEX_IS_FAVORITE);
            mMovieModel.setFavorites(is_favorite);

            String poster_path = mCursor.getString(FragmentMain.INDEX_MOVIE_POSTER_PATH);
            mMovieModel.setPosterPath(poster_path);
            String original_title = mCursor.getString(FragmentMain.INDEX_MOVIE_ORIGINAL_TITLE);
            mMovieModel.setOriginalTitle(original_title);
            String movie_ID = mCursor.getString(FragmentMain.INDEX_MOVIE_MOVIE_ID);
            mMovieModel.setMovieId(movie_ID);
            String vote_average = mCursor.getString(FragmentMain.INDEX_MOVIE_VOTE_AVERAGE);
            mMovieModel.setVoteAverage(vote_average);
            String backdrop_path = mCursor.getString(FragmentMain.INDEX_MOVIE_BACDROP_PATH);
            mMovieModel.setBackdropPath(backdrop_path);
            String movie_overview = mCursor.getString(FragmentMain.INDEX_MOVIE_OVERVIEW);
            mMovieModel.setOverview(movie_overview);
            String release_date = mCursor.getString(FragmentMain.INDEX_RELEASE_DATE);
            mMovieModel.setReleaseDate(release_date);


            return mMovieModel;
        }

    }



}









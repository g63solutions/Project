package com.zmediaz.apps.fragtry.sync;

import android.app.IntentService;
import android.content.Intent;

import com.zmediaz.apps.fragtry.R;
import com.zmediaz.apps.fragtry.data.MovieModel;

/**
 * Created by Computer on 1/18/2017.
 */

/*
public class MovieSyncIntentService extends IntentService {
    String mKey;

    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mKey = getString(R.string.api);

        MovieSyncTask.syncMovie(this, mKey);
    }
}
*/



public class MovieSyncIntentService extends IntentService {
    String mKey;

    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MovieModel movieModel = (MovieModel)intent.getSerializableExtra("MovieModel");
       String action =intent.getAction();
        MovieSyncTask.executeTask(this, action, movieModel);
    }
}
